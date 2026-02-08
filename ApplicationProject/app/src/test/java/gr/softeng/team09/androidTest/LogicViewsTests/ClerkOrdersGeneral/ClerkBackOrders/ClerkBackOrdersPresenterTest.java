package gr.softeng.team09.androidTest.LogicViewsTests.ClerkOrdersGeneral.ClerkBackOrders;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Queue;

import gr.softeng.team09.domain.Backorder;
import gr.softeng.team09.domain.BackorderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;

// άλλαξε αυτά αν στο project σου λέγονται αλλιώς:
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders.ClerkBackOrdersPresenter;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders.ClerkBackOrdersView;

import static org.junit.Assert.*;

/**
 * The type Clerk back orders presenter test.
 */
public class ClerkBackOrdersPresenterTest {

    private ClerkBackOrdersPresenter presenter;

    private BackorderDAOMemory backorderDAO;

    // "inline view state"
    private String lastError;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        // καθάρισμα static λιστών του BackorderDAOMemory για να μην "λερώνουν" άλλα tests
        clearBackorderDAOMemoryStatics();

        backorderDAO = new BackorderDAOMemory();

        // init inventory στο MemoryStore (ο presenter παίρνει inventory από MemoryStore.getInventory())
        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                backorderDAO,
                new ReservationDAOMemory()
        );

        presenter = new ClerkBackOrdersPresenter();

        lastError = null;

        presenter.setView(new ClerkBackOrdersView() {
            @Override
            public String buildRow(Backorder back) {
                return "row";
            }

            @Override
            public void showError(String msg) {
                lastError = msg;
            }

            @Override
            public void showMessage(String msg) {
                // δεν μας χρειάζεται στα tests αυτά
            }
        });
    }

    /**
     * Flow covers all branches.
     */
    @Test
    public void flow_coversAllBranches() {
        // -------- Arrange: backorders σε 2 pharmacies --------
        Pharmacy ph111 = new Pharmacy("Pharm111", 111, null, null, "pw");
        Pharmacy ph222 = new Pharmacy("Pharm222", 222, null, null, "pw");

        Backorder b1 = new Backorder(1, ph111); // BACKORDER
        Backorder b2 = new Backorder(2, ph111); // θα γίνει CANCELED
        b2.cancel();

        Backorder b3 = new Backorder(3, ph222); // θα γίνει COMPLETEDBACKORDER
        b3.addLine(new BackorderLine(null, 0)); // fulfilled
        b3.tryMarkCompleted();

        backorderDAO.enqueue(b1);
        backorderDAO.enqueue(b2);
        backorderDAO.enqueue(b3);

        // -------- 1) loadActiveBackorders: μόνο BACKORDER --------
        presenter.loadActiveBackorders();
        List<Backorder> active = presenter.getLastShownBackorders();

        assertEquals(1, active.size());
        assertEquals(OrderState.BACKORDER, active.get(0).getState());
        assertEquals(1, active.get(0).getId());

        // getBackorderAt bounds + valid
        assertNull(presenter.getBackorderAt(-1));
        assertNull(presenter.getBackorderAt(1)); // out of bounds (size=1)
        assertNotNull(presenter.getBackorderAt(0));
        assertEquals(1, presenter.getBackorderAt(0).getId());

        // -------- 2) loadAllBackorders: όλα --------
        presenter.loadAllBackorders();
        List<Backorder> all = presenter.getLastShownBackorders();
        assertEquals(3, all.size());

        // -------- 3) findAllBackOrdersFromPharm: AFM required --------
        lastError = null;
        presenter.setPharmAFM(null);
        presenter.findAllBackOrdersFromPharm();
        assertEquals("AFM required", lastError);

        // -------- 4) findAllBackOrdersFromPharm: Invalid AFM --------
        lastError = null;
        presenter.setPharmAFM("abc");
        presenter.findAllBackOrdersFromPharm();
        assertEquals("Invalid AFM", lastError);

        // -------- 5) findAllBackOrdersFromPharm: valid AFM -> returns only that pharmacy --------
        lastError = null;
        presenter.setPharmAFM("111");
        presenter.findAllBackOrdersFromPharm();

        assertNull(lastError);
        List<Backorder> only111 = presenter.getLastShownBackorders();
        assertEquals(2, only111.size()); // b1 + b2 (και τα 2 είναι για AFM=111)

        // επιβεβαίωση ότι έκανε reset pharmAFM=null:
        // αν ξανακαλέσουμε χωρίς setPharmAFM, θα βγάλει "AFM required"
        lastError = null;
        presenter.findAllBackOrdersFromPharm();
        assertEquals("AFM required", lastError);
    }

    // ---------------- helpers ----------------

    private static void clearBackorderDAOMemoryStatics() throws Exception {
        Field fifoField = BackorderDAOMemory.class.getDeclaredField("fifo");
        fifoField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Queue<Backorder> fifo = (Queue<Backorder>) fifoField.get(null);
        fifo.clear();

        Field allField = BackorderDAOMemory.class.getDeclaredField("all");
        allField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Backorder> all = (List<Backorder>) allField.get(null);
        all.clear();
    }
}
