package gr.softeng.team09.androidTest.LogicViewsTests;

import org.junit.Before;

import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.ReservationDAOMemory;


/**
 * The type Presenter test base.
 */
// Base class preventing MemoryStore and DAOMemory leakage from previous JUnit tests
public abstract class PresenterTestBase {

    /**
     * Reset global state.
     */
    @Before
    public void resetGlobalState() {
        // Previoos Sesh
        try { MemoryStore.clearSessionClerk(); } catch (Exception ignored) {}
        try { MemoryStore.clearSessionPharma(); } catch (Exception ignored) {}

        // Active orders
        try { MemoryStore.clearActiveOrder(); } catch (Exception ignored) {}

        // Inventory (same init as StartingScreenScreen)
        try {
            MemoryStore.initInventoryIfNeeded(
                    null,
                    new BatchDAOMemory(),
                    new InventoryLineDAOMemory(),
                    new BackorderDAOMemory(),
                    new ReservationDAOMemory()
            );
        } catch (Exception ignored) {}
    }
}
