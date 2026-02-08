package gr.softeng.team09.androidTest.LogicViewsTests.PharmacyViewCompletedOrders;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersPresenter;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy view completed orders presenter test.
 */
public class PharmacyViewCompletedOrdersPresenterTest {

    private PharmacyViewCompletedOrdersPresenter presenter;

    private List<Order> shownOrders;
    private Order capturedOrderForDetails;

    private Pharmacy pharmacy;
    private OrderDAOMemory orderDAOMemory;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        // init inventory στο MemoryStore (ο presenter το διαβάζει στο constructor)
        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );

        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 123, "pw");
        pharmacy = new Pharmacy("PharmA", 111, owner, null, "pw");
        MemoryStore.setPharmacy(pharmacy);

        shownOrders = null;
        capturedOrderForDetails = null;

        presenter = new PharmacyViewCompletedOrdersPresenter();
        presenter.setView(new PharmacyViewCompletedOrdersView() {

            @Override
            public void showCompletedOrders(List<Order> orders) {
                shownOrders = orders;
            }

            @Override
            public void showError(String msg) {}

            @Override
            public void showOrderDetails(Order order) {
                capturedOrderForDetails = order;
            }
        });

        // inject δικό μας OrderDAOMemory (όπως είχες)
        orderDAOMemory = new OrderDAOMemory();
        injectOrderDAO(presenter, orderDAOMemory);
    }

    /**
     * Load completed orders when pharmacy null returns and does not call view.
     *
     * @throws Exception the exception
     */
    @Test
    public void loadCompletedOrders_whenPharmacyNull_returnsAndDoesNotCallView() throws Exception {
        injectPharmacy(presenter, null);

        presenter.loadCompletedOrders();

        assertNull(shownOrders);
        assertNull(presenter.getOrders());
    }

    /**
     * Load completed orders when pharmacy set shows completed orders and stores orders.
     */
    @Test
    public void loadCompletedOrders_whenPharmacySet_showsCompletedOrdersAndStoresOrders() {
        // Arrange
        Order completed = new Order(1, pharmacy, System.currentTimeMillis());
        completed.setState(OrderState.COMPLETED);

        Order pending = new Order(2, pharmacy, System.currentTimeMillis());
        pending.setState(OrderState.PENDING);

        orderDAOMemory.saveCompleted(completed);
        orderDAOMemory.savePending(pending);

        // Act
        presenter.loadCompletedOrders();

        // Assert
        assertNotNull(shownOrders);
        assertNotNull(presenter.getOrders());

        boolean foundCompleted = false;
        for (Order o : shownOrders) {
            if (o.getId() == 1 && o.getState() == OrderState.COMPLETED) {
                foundCompleted = true;
            }
            assertEquals("Expected only COMPLETED orders", OrderState.COMPLETED, o.getState());
            assertEquals(pharmacy, o.getPharmacy());
        }
        assertTrue(foundCompleted);
    }

    // ===================== ΑΥΤΑ ΕΙΝΑΙ ΤΑ 2 TESTS ΠΟΥ ΣΟΥ ΛΕΙΠΟΥΝ =====================

    /**
     * Calculate total covers for loop and sum.
     */
    @Test
    public void calculateTotal_coversForLoopAndSum() {
        ProductCategory cat = ProductCategory.values()[0];

        // priceWithVAT = price * 1.24
        Product p1 = new Product("P1", cat, 100, 10.0); // 12.4
        Product p2 = new Product("P2", cat, 101, 5.0);  // 6.2

        Order o = new Order(10, pharmacy, System.currentTimeMillis());
        o.addLine(new OrderLine(p1, 2)); // 24.8
        o.addLine(new OrderLine(p2, 3)); // 18.6
        // total = 43.4

        double total = presenter.calculateTotal(o);

        assertEquals(43.4, total, 0.000001);
    }

    /**
     * On selected covers if true and if false.
     *
     * @throws Exception the exception
     */
    @Test
    public void onSelected_coversIfTrue_andIfFalse() throws Exception {
        // ΠΡΕΠΕΙ να γεμίσουμε το private field "orders" αλλιώς δεν καλύπτεται το if.
        Order o1 = new Order(1, pharmacy, System.currentTimeMillis());
        Order o2 = new Order(2, pharmacy, System.currentTimeMillis());
        injectOrders(presenter, Arrays.asList(o1, o2));

        // --- IF FALSE (pos < 0) ---
        capturedOrderForDetails = null;
        presenter.onSelected(-1);
        assertNull(capturedOrderForDetails);

        // --- IF FALSE (pos >= size) ---
        capturedOrderForDetails = null;
        presenter.onSelected(2);
        assertNull(capturedOrderForDetails);

        // --- IF TRUE (valid pos) -> καλύπτει view.showOrderDetails(...) ---
        capturedOrderForDetails = null;
        presenter.onSelected(0);
        assertNotNull(capturedOrderForDetails);
        assertEquals(1, capturedOrderForDetails.getId());

        capturedOrderForDetails = null;
        presenter.onSelected(1);
        assertNotNull(capturedOrderForDetails);
        assertEquals(2, capturedOrderForDetails.getId());
    }

    // ---------- reflection helpers ----------

    private static void injectOrderDAO(PharmacyViewCompletedOrdersPresenter presenter, OrderDAOMemory dao)
            throws Exception {
        Field f = PharmacyViewCompletedOrdersPresenter.class.getDeclaredField("orderDAO");
        f.setAccessible(true);
        f.set(presenter, dao);
    }

    private static void injectPharmacy(PharmacyViewCompletedOrdersPresenter presenter, Pharmacy ph)
            throws Exception {
        Field f = PharmacyViewCompletedOrdersPresenter.class.getDeclaredField("pharmacy");
        f.setAccessible(true);
        f.set(presenter, ph);
    }

    private static void injectOrders(PharmacyViewCompletedOrdersPresenter presenter, List<Order> list)
            throws Exception {
        Field f = PharmacyViewCompletedOrdersPresenter.class.getDeclaredField("orders");
        f.setAccessible(true);
        f.set(presenter, list);
    }
}
