package gr.softeng.team09.androidTest.LogicViewsTests.PharmacyCart;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartPresenter;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy cart presenter test.
 */
public class PharmacyCartPresenterTest {

    private PharmacyCartPresenter presenter;

    // captured view outputs
    private String lastError;
    private String lastSuccess;
    private Double lastTotal;
    private Order lastShownOrder;
    private int budgetDialogCount;
    private int navigateBackCount;

    private Pharmacy pharmacy;
    private Inventory inventory;
    private Order order;
    private Product product;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // init inventory στο MemoryStore
        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );
        inventory = MemoryStore.getInventory();
        assertNotNull(inventory);

        presenter = new PharmacyCartPresenter();

        resetViewCaptures();

        presenter.setView(new PharmacyCartView() {
            @Override public void showError(String msg) { lastError = msg; }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void showTotal(double total) { lastTotal = total; }
            @Override public void showOrder(Order order) { lastShownOrder = order; }
            @Override public void showBudgetDialog() { budgetDialogCount++; }

            @Override
            public void showBackOrderDialog(int budget) {

            }

            @Override public void navigateBack() { navigateBackCount++; }
        });

        // domain objects
        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 123, "pw");
        pharmacy = new Pharmacy("PharmA", 111, owner, null, "pw");
        MemoryStore.setPharmacy(pharmacy);

        ProductCategory cat = ProductCategory.values()[0];
        product = new Product("Panadol", cat, 123, 4.5);

        order = new Order(1, pharmacy, System.currentTimeMillis());
        // NOTE: Το addToCart προσθέτει και στο order lines και στο pharmacy.cart
        pharmacy.addToCart(order, new OrderLine(product, 2));

        MemoryStore.setActiveOrder(order);
    }

    private void resetViewCaptures() {
        lastError = null;
        lastSuccess = null;
        lastTotal = null;
        lastShownOrder = null;
        budgetDialogCount = 0;
        navigateBackCount = 0;
    }

    /**
     * Load cart when pharmacy null returns without view calls.
     */
    @Test
    public void loadCart_whenPharmacyNull_returnsWithoutViewCalls() {
        resetViewCaptures();
        MemoryStore.setPharmacy(null);

        presenter.loadCart();

        assertNull(lastError);
        assertNull(lastSuccess);
        assertNull(lastTotal);
        assertNull(lastShownOrder);
        assertEquals(0, budgetDialogCount);
        assertEquals(0, navigateBackCount);
    }

    /**
     * Load cart when cart empty shows error total zero and null order.
     */
    @Test
    public void loadCart_whenCartEmpty_showsErrorTotalZeroAndNullOrder() {
        resetViewCaptures();

        MemoryStore.setActiveOrder(new Order(2, pharmacy, System.currentTimeMillis())); // empty lines

        presenter.loadCart();

        assertEquals("Cart is empty", lastError);
        assertEquals(Double.valueOf(0.0), lastTotal);
        assertNull(lastShownOrder);
    }

    /**
     * Load cart when has lines shows order and total.
     */
    @Test
    public void loadCart_whenHasLines_showsOrderAndTotal() {
        resetViewCaptures();

        presenter.loadCart();

        assertNull(lastError);
        assertNotNull(lastShownOrder);
        assertNotNull(lastTotal);

        // total = qty * priceWithVAT
        double expected = 2 * product.getPriceWithVAT();
        assertEquals(expected, lastTotal, 1e-9);
    }

    /**
     * On submit clicked when empty shows error.
     */
    @Test
    public void onSubmitClicked_whenEmpty_showsError() {
        resetViewCaptures();
        MemoryStore.setActiveOrder(new Order(3, pharmacy, System.currentTimeMillis())); // empty

        presenter.onSubmitClicked();

        assertEquals("Cart is empty.", lastError);
        assertEquals(0, budgetDialogCount);
    }

    /**
     * On submit clicked when not empty shows budget dialog.
     */
    @Test
    public void onSubmitClicked_whenNotEmpty_showsBudgetDialog() {
        resetViewCaptures();

        presenter.onSubmitClicked();

        assertNull(lastError);
        assertEquals(1, budgetDialogCount);
    }

    /**
     * Submit order when current order null does nothing.
     */
    @Test
    public void submitOrder_whenCurrentOrderNull_doesNothing() {
        resetViewCaptures();

        // κάνουμε on purpose: currentOrder μέσα στον presenter είναι null
        presenter.submitOrder(999, true);

        assertNull(lastError);
        assertNull(lastSuccess);
        assertEquals(0, navigateBackCount);
    }

    /**
     * Submit order when payment not enough shows error.
     */
    @Test
    public void submitOrder_whenPaymentNotEnough_showsError() {
        resetViewCaptures();

        presenter.loadCart(); // ώστε presenter.currentOrder != null
        presenter.submitOrder(0, true);

        assertEquals("Balance not enough for the payment.", lastError);
        assertNull(lastSuccess);
        assertEquals(0, navigateBackCount);
    }

    /**
     * Submit order when payment enough success clears session and navigates back.
     */
    @Test
    public void submitOrder_whenPaymentEnough_successClearsSessionAndNavigatesBack() {
        resetViewCaptures();

        presenter.loadCart();
        int budgetEnough = 1_000;

        presenter.submitOrder(budgetEnough, true);

        assertEquals("Order submitted", lastSuccess);
        assertNull(lastError);
        assertEquals(1, navigateBackCount);

        // session cleared
        assertNull(MemoryStore.getActiveOrder());
        assertTrue(pharmacy.cart.isEmpty());
    }

    /**
     * Save draft when current order not null saves and clears and navigates back.
     */
    @Test
    public void saveDraft_whenCurrentOrderNotNull_savesAndClearsAndNavigatesBack() {
        resetViewCaptures();

        presenter.loadCart(); // currentOrder set
        presenter.saveDraft();

        // στο saveDraft χρησιμοποιεί showError("Saved to Drafts.")
        assertEquals("Saved to Drafts.", lastError);
        assertEquals(1, navigateBackCount);
        assertNull(MemoryStore.getActiveOrder());
        assertTrue(pharmacy.cart.isEmpty());
    }

    /**
     * Save draft when current order null just navigates back.
     */
    @Test
    public void saveDraft_whenCurrentOrderNull_justNavigatesBack() {
        resetViewCaptures();

        // Δεν κάνουμε loadCart, άρα currentOrder μένει null στον presenter
        presenter.saveDraft();

        assertEquals(1, navigateBackCount);
        assertNull(lastError);
        assertNull(lastSuccess);
    }

    /**
     * On back pressed navigates back.
     */
    @Test
    public void onBackPressed_navigatesBack() {
        resetViewCaptures();

        presenter.onBackPressed();

        assertEquals(1, navigateBackCount);
    }

    /**
     * Update line quantity when zero removes line and updates view.
     */
    @Test
    public void updateLineQuantity_whenZero_removesLineAndUpdatesView() {
        resetViewCaptures();

        presenter.loadCart(); // pharmacy/currentOrder set inside presenter

        OrderLine line = order.getLines().get(0);
        presenter.updateLineQuantity(line, 0);

        assertNotNull(lastShownOrder);
        assertTrue("Expected line removed", lastShownOrder.getLines().isEmpty());
        assertEquals(Double.valueOf(0.0), lastTotal);
    }

    /**
     * Update line quantity when positive updates quantity and updates total.
     */
    @Test
    public void updateLineQuantity_whenPositive_updatesQuantityAndUpdatesTotal() {
        resetViewCaptures();

        presenter.loadCart(); // pharmacy/currentOrder set

        OrderLine line = order.getLines().get(0);
        presenter.updateLineQuantity(line, 5);

        assertEquals(5, line.getQuantity());
        assertNotNull(lastTotal);

        double expected = 5 * product.getPriceWithVAT();
        assertEquals(expected, lastTotal, 1e-9);
    }
}
