package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;
import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.BatchDAOMemory;

import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Batch dao memory test.
 */
public class BatchDAOMemoryTest {

    private BatchDAOMemory dao;
    private Product product;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dao = new BatchDAOMemory();

        product = new Product("Cream", ProductCategory.OTHER, 200, 15.0);
    }

    /**
     * Save adds batch and history.
     */
    @Test
    public void save_AddsBatchAndHistory() {
        // Σωστός Constructor: id, product, batchNumber, quantity
        Batch b = new Batch(1, product, 1001, 50);

        dao.save(b);

        assertEquals("Should find batch by ID", b, dao.find(1));
        assertEquals("Should find active batch by product", 1, dao.findActiveByProduct(product).size());
        assertEquals("Total batches should be 1", 1, dao.findAll().size());
    }

    /**
     * Save null batch do nothing.
     */
    @Test
    public void save_NullBatch_DoNothing() {
        dao.save(null);
        assertEquals(0, dao.findAll().size());
    }

    /**
     * Save with zero quantity removes from active.
     */
    @Test
    public void save_WithZeroQuantity_RemovesFromActive() {

        Batch b = new Batch(1, product, 1001, 50);
        dao.save(b);

        assertEquals("Should be active initially", 1, dao.findActiveByProduct(product).size());

        b.consume(50, null);

        assertEquals("Quantity inside object should be 0", 0, b.getQuantity());


        dao.save(b);

        assertNotNull("Should still exist in history", dao.find(1));

        
        List<Batch> active = dao.findActiveByProduct(product);
        assertEquals("Should be removed from active list when quantity becomes 0", 0, active.size());
    }

    /**
     * Remove from active if empty handling.
     */
    @Test
    public void removeFromActiveIfEmpty_Handling() {

        dao.removeFromActiveIfEmpty(null);


        Batch b = new Batch(1, product, 1001, 0);

        dao.save(b);


        dao.removeFromActiveIfEmpty(b);

        assertEquals(0, dao.findActiveByProduct(product).size());
    }

    /**
     * Next id calculates correctly.
     */
    @Test
    public void nextId_CalculatesCorrectly() {
        assertEquals(1, dao.nextId());

        Batch b = new Batch(10, product, 2002, 5);

        dao.save(b);

        assertEquals(11, dao.nextId());
    }

    /**
     * Batch counter test.
     */
    @Test
    public void batchCounter_Test() {
        assertEquals(0, dao.getTotalBatches());
        dao.BatchesCounter();
        assertEquals(1, dao.getTotalBatches());
    }

    /**
     * Find active by product null check.
     */
    @Test
    public void findActiveByProduct_NullCheck() {
        assertEquals(0, dao.findActiveByProduct(null).size());
        assertEquals(0, dao.findActiveByProduct(new Product("Other", ProductCategory.OTHER, 1, 1)).size());
    }
}
