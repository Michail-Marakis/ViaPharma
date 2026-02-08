package gr.softeng.team09.androidTest.LogicViewsTests.ClerkBatches.Recall;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.ReservationDAOMemory;
import gr.softeng.team09.view.ClerkBatches.Recall.ClerkRecallBatchPresenter;
import gr.softeng.team09.view.ClerkBatches.Recall.ClerkRecallBatchView;

import static org.junit.Assert.*;

/**
 * The type Clerk recall batch presenter test.
 */
public class ClerkRecallBatchPresenterTest {

    private BatchDAOMemory batchDAO;
    private Inventory inventory;
    private ClerkRecallBatchPresenter presenter;

    // view captures
    private String lastError;
    private final List<String> successes = new ArrayList<>();
    private int recallCallCount;
    private int recallCalledWith;


    private Clerk clerkOwner;

    private void resetInventorySingleton() {
        try {
            Field f = MemoryStore.class.getDeclaredField("inventory");
            f.setAccessible(true);
            f.set(null, null);
        } catch (Exception ignored) {}
    }

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        resetInventorySingleton();

        batchDAO = new BatchDAOMemory();


        clerkOwner = new Clerk("John", "Doe", "2100000000", "clerk@mail.com", "pw");

        MemoryStore.initInventoryIfNeeded(
                clerkOwner,
                batchDAO,
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );

        inventory = MemoryStore.getInventory();
        assertNotNull(inventory);

        lastError = null;
        successes.clear();
        recallCallCount = 0;
        recallCalledWith = -1;

        presenter = new ClerkRecallBatchPresenter();
        presenter.setView(new ClerkRecallBatchView() {
            @Override
            public void showError(String message) {
                lastError = message;
            }

            @Override
            public void showSuccess(String message) {
                successes.add(message);
            }

            @Override
            public void recallBatchCall(int batchId) {
                recallCallCount++;
                recallCalledWith = batchId;
            }


            @Override public void loadBatchesToList() {}
            @Override public String buildRow(Batch b) { return ""; }
        });
    }

    /**
     * Ready to recall when id missing shows give batch id.
     */
    @Test
    public void readyToRecall_whenIdMissing_showsGiveBatchId() {
        presenter.setIdToRecall(0);
        presenter.readyToRecall();

        assertEquals("Give batch id", lastError);
        assertEquals(0, recallCallCount);
        assertTrue(successes.isEmpty());
    }

    /**
     * Ready to recall when not found shows batch not found and resets id.
     */
    @Test
    public void readyToRecall_whenNotFound_showsBatchNotFound_andResetsId() {

        presenter.setIdToRecall(999);
        presenter.readyToRecall();

        assertEquals("Batch not found", lastError);
        assertEquals(0, recallCallCount);
        assertTrue(successes.isEmpty());


        lastError = null;
        presenter.readyToRecall();
        assertEquals("Give batch id", lastError);
    }

    /**
     * Ready to recall when found sends recall call success and emails loop and resets id.
     */
    @Test
    public void readyToRecall_whenFound_sendsRecallCall_success_andEmailsLoop_andResetsId() {

        Product p = new Product("Aspirin", ProductCategory.OTHER, 123, 2.0);
        Batch b = new Batch(10, p, 777, 50);

        Pharmacist ph1Owner = new Pharmacist("A", "B", "ph1@mail.com", 111, "pw");
        Pharmacist ph2Owner = new Pharmacist("C", "D", "ph2@mail.com", 222, "pw");

        Pharmacy ph1 = new Pharmacy("Pharm1", 111111111, ph1Owner, null, "pw");
        Pharmacy ph2 = new Pharmacy("Pharm2", 222222222, ph2Owner, null, "pw");


        b.consume(5, ph1);
        b.consume(7, ph2);

        batchDAO.save(b);


        presenter.setIdToRecall(10);
        presenter.readyToRecall();
        assertEquals(1, recallCallCount);
        assertEquals(10, recallCalledWith);

        assertNull("No error expected", lastError);



        assertTrue(successes.contains("Batch recalled successfully"));

        assertTrue(successes.contains("Email sent to " + ph1Owner.getEmail()));
        assertTrue(successes.contains("Email sent to " + ph2Owner.getEmail()));


        lastError = null;
        presenter.readyToRecall();
        assertEquals("Give batch id", lastError);
    }
}
