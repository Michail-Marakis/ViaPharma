package gr.softeng.team09.androidTest.LogicViewsTests.ClerkOrdersGeneral.ClerkShowOrders;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;

import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders.ClerkShowOrdersPresenter;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders.ClerkShowOrdersView;

import static org.junit.Assert.*;

/**
 * The type Clerk show orders presenter test.
 */
public class ClerkShowOrdersPresenterTest {

    private ClerkShowOrdersPresenter presenter;

    // capture από view (inline, όχι FakeView class)
    private String lastError;
    private String lastMessage;
    private List<String> lastShownOrders;
    private Order lastDetailsOrder;

    private Inventory inventory;
    private BackorderDAOMemory backorderDAO;
    private BatchDAOMemory batchDAO;

    // άλλαξε αυτά τα 2 imports αν τα δικά σου memory daos έχουν άλλο όνομα
    private InventoryLineDAOMemory inventoryLineDAO;
    private ReservationDAOMemory reservationDAO;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset MemoryStore inventory (αν στο project σου δεν γίνεται, δεν πειράζει — αλλά ιδανικά κάθε test καθαρό)
        backorderDAO = new BackorderDAOMemory();
        batchDAO = new BatchDAOMemory();
        inventoryLineDAO = new InventoryLineDAOMemory();
        reservationDAO = new ReservationDAOMemory();

        MemoryStore.initInventoryIfNeeded(null, batchDAO, inventoryLineDAO, backorderDAO, reservationDAO);
        inventory = MemoryStore.getInventory();
        assertNotNull(inventory);

        presenter = new ClerkShowOrdersPresenter();

        lastError = null;
        lastMessage = null;
        lastShownOrders = null;
        lastDetailsOrder = null;

        presenter.setView(new ClerkShowOrdersView() {
            @Override
            public void showOrders(List<String> orders) {
                lastShownOrders = orders;
            }

            @Override
            public void showOrderDetails(Order order) {
                lastDetailsOrder = order;
            }

            @Override
            public void showError(String msg) {
                lastError = msg;
            }

            @Override
            public void showMessage(String msg) {
                lastMessage = msg;
            }
        });
    }

    /**
     * Flow covers most branches.
     */
    @Test
    public void flow_coversMostBranches() {
        // ---------- Arrange ----------
        ProductCategory anyCat = ProductCategory.values()[0];

        Product p = new Product("Panadol", anyCat, 123, 4.5);

        Pharmacy ph = new Pharmacy("PharmA", 111, null, null, "pw");

        // Stock: έχουμε 5 διαθέσιμα
        inventoryLineDAO.save(new InventoryLine(p, 5));
        // Batch με 5 ώστε allocateFromBatches να μπορεί να "φάει"
        batchDAO.save(new Batch(1, p, 100, 5));

        // Order1: ζητάει 3 -> εκτέλεση χωρίς backorder
        Order o1 = new Order(1, ph, System.currentTimeMillis());
        o1.addLine(new OrderLine(p, 3));
        o1.setState(OrderState.PENDING);
        inventory.addToAll(o1);

        // Order2: ζητάει 10 αλλά έχουμε 5 -> θα γίνει ship 5 και backorder 5 (flag true)
        Order o2 = new Order(2, ph, System.currentTimeMillis());
        o2.addLine(new OrderLine(p, 10));
        o2.setState(OrderState.PENDING);
        inventory.addToAll(o2);

        // ---------- loadAllOrders ----------
        presenter.loadAllOrders();
        assertNotNull(lastShownOrders);
        assertTrue(lastShownOrders.size() >= 2);

        // ---------- loadCompletedOrders (αρχικά 0) ----------
        presenter.loadCompletedOrders();
        assertNotNull(lastShownOrders);
        // δεν κάνουμε assert exact γιατί depends από άλλα tests/flows, αλλά συνήθως 0 εδώ

        // ---------- parseId errors ----------
        lastError = null;
        presenter.executeById(null, true);
        assertEquals("Order ID required", lastError);

        lastError = null;
        presenter.executeById("abc", true);
        assertEquals("Invalid Order ID", lastError);

        // ---------- executeById: cannot (δεν υπάρχει id=999) ----------
        lastError = null;
        presenter.executeById("999", true);
        assertEquals("Cannot execute order #999", lastError);

        // ---------- executeById: success, NO backorder ----------
        lastMessage = null;
        presenter.executeById("1", true);
        assertEquals("Order #1 executed. NO BackOrder created.", lastMessage);
        assertNotNull(lastShownOrders); // loadAllOrders called

        // ---------- executeById: success, backorder created ----------
        lastMessage = null;
        presenter.executeById("2", true);
        assertEquals("Order #2 executed. BackOrder created.", lastMessage);
        assertNotNull(lastShownOrders);

        // ---------- postponeById: success (βάζουμε νέο pending order) ----------
        Order o3 = new Order(3, ph, System.currentTimeMillis());
        o3.addLine(new OrderLine(p, 1));
        o3.setState(OrderState.PENDING);
        inventory.addToAll(o3);

        lastMessage = null;
        lastError = null;
        presenter.postponeById("3");
        assertEquals("Order #3 postponed", lastMessage);
        assertNull(lastError);

        // ---------- postponeById: fail (id δεν υπάρχει) ----------
        lastError = null;
        presenter.postponeById("777");
        assertEquals("Cannot postpone order #777", lastError);

        // ---------- onOrderSelected bounds + valid ----------
        presenter.loadAllOrders();
        lastDetailsOrder = null;

        presenter.onOrderSelected(-1);
        assertNull(lastDetailsOrder);

        if (lastShownOrders != null && !lastShownOrders.isEmpty()) {
            // position 0 valid -> should call showOrderDetails
            presenter.onOrderSelected(0);
            assertNotNull(lastDetailsOrder);
        }

        // ---------- calculateTotal ----------
        // o1 έχει 3 τεμ * priceWithVAT(4.5*1.24 = 5.58) => 16.74
        double total = presenter.calculateTotal(o1);
        assertEquals(3 * (4.5 * 1.24), total, 1e-9);

        // ---------- cancelById: parseId errors ----------
        lastError = null;
        lastMessage = null;

        presenter.cancelById(null);
        assertEquals("Order ID required", lastError);

        lastError = null;
        presenter.cancelById("abc");
        assertEquals("Invalid Order ID", lastError);

// ---------- cancelById: cannot cancel (order doesn't exist) ----------
        lastError = null;
        presenter.cancelById("999");
        assertEquals("Cannot cancel order #999", lastError);

// ---------- cancelById: cannot cancel (order exists but state not allowed) ----------
// π.χ. PENDING -> Inventory.cancelOrderById επιστρέφει false
        Order oPending = new Order(50, ph, System.currentTimeMillis());
        oPending.addLine(new OrderLine(p, 1));
        oPending.setState(OrderState.PENDING);
        inventory.addToAll(oPending);

        lastError = null;
        presenter.cancelById("50");
        assertEquals("Cannot cancel order #50", lastError);

// ---------- cancelById: success (state TOCANCEL) ----------
        Order oToCancel = new Order(60, ph, System.currentTimeMillis());
        oToCancel.addLine(new OrderLine(p, 1));
        oToCancel.setState(OrderState.TOCANCEL);
        inventory.addToAll(oToCancel);

        lastError = null;
        lastMessage = null;
        lastShownOrders = null;

        presenter.cancelById("60");

        assertNull(lastError);
        assertEquals("Cancelled order #60", lastMessage);

        assertNotNull(lastShownOrders);

    }


}
