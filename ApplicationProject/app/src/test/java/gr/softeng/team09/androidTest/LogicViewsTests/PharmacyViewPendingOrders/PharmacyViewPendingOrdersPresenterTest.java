package gr.softeng.team09.androidTest.LogicViewsTests.PharmacyViewPendingOrders;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;

import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersPresenter;
import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy view pending orders presenter test.
 */
public class PharmacyViewPendingOrdersPresenterTest {

    private PharmacyViewPendingOrdersPresenter presenter;

    private List<Order> shownPending;
    private int cancellationDialogCount;
    private String lastError;

    private Pharmacy pharmacy;
    private Inventory inventory;
    private OrderDAOMemory dao;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        // init inventory in MemoryStore (presenter reads it)
        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );
        inventory = MemoryStore.getInventory();
        assertNotNull(inventory);

        // pharmacy in MemoryStore
        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 123, "pw");
        pharmacy = new Pharmacy("PharmA", 111, owner, null, "pw");
        MemoryStore.setPharmacy(pharmacy);

        // view captures
        shownPending = null;
        cancellationDialogCount = 0;
        lastError = null;

        presenter = new PharmacyViewPendingOrdersPresenter();
        presenter.setView(new PharmacyViewPendingOrdersView() {
            @Override public void showPendingOrders(List<Order> orders) { shownPending = orders; }
            @Override public void showCancellationDialog() { cancellationDialogCount++; }

            @Override
            public void refreshOrders() {

            }

            @Override public void showError(String msg) { lastError = msg; }
        });

        // Inject controlled DAO + pharmacy + inventory into presenter to avoid flakiness
        dao = new OrderDAOMemory();
        injectField(presenter, "orderDAO", dao);
        injectField(presenter, "pharmacy", pharmacy);
        injectField(presenter, "inv", inventory);
    }

    /**
     * Load pending orders when pharmacy null returns without calling view.
     *
     * @throws Exception the exception
     */
    @Test
    public void loadPendingOrders_whenPharmacyNull_returnsWithoutCallingView() throws Exception {
        injectField(presenter, "pharmacy", null);

        presenter.loadPendingOrders();

        assertNull(shownPending);
        assertNull(presenter.getOrders());
    }

    /**
     * Load pending orders when pharmacy set shows pending orders and stores orders.
     */
    @Test
    public void loadPendingOrders_whenPharmacySet_showsPendingOrdersAndStoresOrders() {
        // Arrange: 2 pending, 1 completed
        Order p1 = new Order(1, pharmacy, System.currentTimeMillis());
        p1.setState(OrderState.PENDING);

        Order p2 = new Order(2, pharmacy, System.currentTimeMillis());
        p2.setState(OrderState.PENDING);

        Order completed = new Order(3, pharmacy, System.currentTimeMillis());
        completed.setState(OrderState.COMPLETED);

        // Adapt to your OrderDAOMemory API if needed
        dao.savePending(p1);
        dao.savePending(p2);
        dao.saveCompleted(completed);

        presenter.loadPendingOrders();

        assertNotNull(shownPending);
        assertNotNull(presenter.getOrders());

        for (Order o : shownPending) {
            assertEquals(OrderState.PENDING, o.getState());
            assertEquals(pharmacy, o.getPharmacy());
        }
        assertTrue(shownPending.size() >= 2);
    }

    /**
     * Initiate cancellation sets pending and shows dialog.
     */
    @Test
    public void initiateCancellation_setsPendingAndShowsDialog() {
        Order p1 = new Order(10, pharmacy, System.currentTimeMillis());
        p1.setState(OrderState.PENDING);

        presenter.initiateCancellation(p1);

        assertEquals(1, cancellationDialogCount);
    }

    /**
     * Submit order when not confirmed clears pending and returns.
     */
    @Test
    public void submitOrder_whenNotConfirmed_clearsPendingAndReturns() {
        // pending set but not confirmed
        Order p1 = new Order(10, pharmacy, System.currentTimeMillis());
        presenter.initiateCancellation(p1);

        presenter.submitOrder(false);

        // no error, no further actions
        assertNull(lastError);
    }

    /**
     * Submit order when confirmed but no pending order just returns.
     */
    @Test
    public void submitOrder_whenConfirmedButNoPendingOrder_justReturns() {
        presenter.submitOrder(true);
        assertNull(lastError);
    }

    /**
     * Submit order when confirmed executes cancellation flow and reloads.
     */
    @Test
    public void submitOrder_whenConfirmed_executesCancellationFlow_andReloads() {
        // Arrange: put pending order in personalPendingOrders to cover deleteSubmittedOrders path
        Order p1 = new Order(77, pharmacy, System.currentTimeMillis());
        p1.setState(OrderState.PENDING);
        pharmacy.personalPendingOrders.add(p1);

        // also store in DAO so loadPendingOrders returns it before cancel
        dao.savePending(p1);

        // initiate
        presenter.initiateCancellation(p1);

        // Act
        shownPending = null;
        presenter.submitOrder(true);

        // Assert
        assertEquals("Order #77 asked to get canceled", lastError);
        assertEquals(OrderState.TOCANCEL, p1.getState());

        // should remove from pharmacy.personalPendingOrders
        boolean stillThere = false;
        for (Order o : pharmacy.personalPendingOrders) {
            if (o.getId() == 77) stillThere = true;
        }
        assertFalse(stillThere);

        // loadPendingOrders called at end => showPendingOrders invoked
        assertNotNull(shownPending);
    }

    // ---------- reflection helper ----------
    private static void injectField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
