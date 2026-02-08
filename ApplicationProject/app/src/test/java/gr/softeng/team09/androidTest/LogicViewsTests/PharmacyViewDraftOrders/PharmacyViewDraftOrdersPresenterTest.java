package gr.softeng.team09.androidTest.LogicViewsTests.PharmacyViewDraftOrders;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.OrderDAOMemory;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersPresenter;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy view draft orders presenter test.
 */
public class PharmacyViewDraftOrdersPresenterTest {

    private PharmacyViewDraftOrdersPresenter presenter;

    private String lastError;
    private List<Order> lastDraftsShown;
    private int navigateToCartCount;

    private Pharmacy pharmacy;
    private OrderDAOMemory dao;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        // reset MemoryStore session
        MemoryStore.setPharmacy(null);
        MemoryStore.clearActiveOrder();

        // create pharmacy in MemoryStore
        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 123, "pw");
        pharmacy = new Pharmacy("PharmA", 111, owner, null, "pw");
        MemoryStore.setPharmacy(pharmacy);

        // view captures
        lastError = null;
        lastDraftsShown = null;
        navigateToCartCount = 0;

        presenter = new PharmacyViewDraftOrdersPresenter();
        presenter.setView(new PharmacyViewDraftOrdersView() {
            @Override public void showError(String msg) { lastError = msg; }
            @Override public void showDraftOrders(List<Order> drafts) { lastDraftsShown = drafts; }
            @Override public void navigateToCart() { navigateToCartCount++; }
        });

        // Inject controlled DAO so we can set up draft orders deterministically
        dao = new OrderDAOMemory();
        injectOrderDAO(presenter, dao);
        injectPharmacy(presenter, pharmacy);
    }

    /**
     * Load draft orders when pharmacy null shows error and returns.
     *
     * @throws Exception the exception
     */
    @Test
    public void loadDraftOrders_whenPharmacyNull_showsErrorAndReturns() throws Exception {
        injectPharmacy(presenter, null);

        presenter.loadDraftOrders();

        assertEquals("No pharmacy found", lastError);
        assertNull(lastDraftsShown);
    }

    /**
     * Load draft orders when pharmacy set shows draft orders.
     */
    @Test
    public void loadDraftOrders_whenPharmacySet_showsDraftOrders() {
        // Arrange: 2 draft orders for this pharmacy, 1 non-draft
        Order d1 = new Order(1, pharmacy, System.currentTimeMillis());
        d1.setState(OrderState.DRAFT);

        Order d2 = new Order(2, pharmacy, System.currentTimeMillis());
        d2.setState(OrderState.DRAFT);

        Order pending = new Order(3, pharmacy, System.currentTimeMillis());
        pending.setState(OrderState.PENDING);

        // IMPORTANT: adapt these calls to your OrderDAOMemory API if names differ
        dao.saveDraft(d1);
        dao.saveDraft(d2);
        dao.savePending(pending);

        // Act
        presenter.loadDraftOrders();

        // Assert
        assertNull(lastError);
        assertNotNull(lastDraftsShown);

        // should include only DRAFT
        for (Order o : lastDraftsShown) {
            assertEquals(OrderState.DRAFT, o.getState());
            assertEquals(pharmacy, o.getPharmacy());
        }
        assertTrue(lastDraftsShown.size() >= 2);
    }

    /**
     * On draft order selected when active order different saves active sets new active updates cart navigates.
     */
    @Test
    public void onDraftOrderSelected_whenActiveOrderDifferent_savesActive_setsNewActive_updatesCart_navigates() {
        // Arrange: active order in MemoryStore that is different from selectedDraft
        ProductCategory cat = ProductCategory.values()[0];
        Product p = new Product("Panadol", cat, 123, 4.5);

        Order active = new Order(10, pharmacy, System.currentTimeMillis());
        active.addLine(new OrderLine(p, 1));
        active.setState(OrderState.DRAFT);
        MemoryStore.setActiveOrder(active);

        Order selected = new Order(20, pharmacy, System.currentTimeMillis());
        selected.addLine(new OrderLine(p, 3));
        selected.setState(OrderState.DRAFT);

        // cart starts non-empty to verify clear+addAll
        pharmacy.cart.add(new OrderLine(p, 999));

        // Act
        presenter.onDraftOrderSelected(selected);

        // Assert: active order changed
        assertEquals(selected, MemoryStore.getActiveOrder());

        // cart updated to selected lines
        assertEquals(1, pharmacy.cart.size());
        assertEquals(3, pharmacy.cart.get(0).getQuantity());

        // navigated
        assertEquals(1, navigateToCartCount);
    }

    /**
     * On draft order selected when active order same does not need save but still navigates and updates cart.
     */
    @Test
    public void onDraftOrderSelected_whenActiveOrderSame_doesNotNeedSaveButStillNavigatesAndUpdatesCart() {
        ProductCategory cat = ProductCategory.values()[0];
        Product p = new Product("Panadol", cat, 123, 4.5);

        Order selected = new Order(20, pharmacy, System.currentTimeMillis());
        selected.addLine(new OrderLine(p, 2));
        selected.setState(OrderState.DRAFT);

        // active == selected
        MemoryStore.setActiveOrder(selected);

        // cart starts something else
        pharmacy.cart.add(new OrderLine(p, 99));

        presenter.onDraftOrderSelected(selected);

        assertEquals(selected, MemoryStore.getActiveOrder());
        assertEquals(1, pharmacy.cart.size());
        assertEquals(2, pharmacy.cart.get(0).getQuantity());
        assertEquals(1, navigateToCartCount);
    }

    // -------- reflection helpers --------

    private static void injectOrderDAO(PharmacyViewDraftOrdersPresenter presenter, OrderDAOMemory dao)
            throws Exception {
        Field f = PharmacyViewDraftOrdersPresenter.class.getDeclaredField("orderDAO");
        f.setAccessible(true);
        f.set(presenter, dao);
    }

    private static void injectPharmacy(PharmacyViewDraftOrdersPresenter presenter, Pharmacy ph)
            throws Exception {
        Field f = PharmacyViewDraftOrdersPresenter.class.getDeclaredField("pharmacy");
        f.setAccessible(true);
        f.set(presenter, ph);
    }
}
