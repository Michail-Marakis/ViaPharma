package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Backorder;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.BackorderDAOMemory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Queue;
import static org.junit.Assert.*;

/**
 * The type Backorder dao memory test.
 */
public class BackorderDAOMemoryTest {
    private BackorderDAOMemory dao;
    private Pharmacy pharmacy;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        dao = new BackorderDAOMemory();

    
        pharmacy = new Pharmacy("TestPharma", 12345, null, null, "password");


        Field fifoField = BackorderDAOMemory.class.getDeclaredField("fifo");
        fifoField.setAccessible(true);
        ((Queue<?>) fifoField.get(null)).clear();

        Field allField = BackorderDAOMemory.class.getDeclaredField("all");
        allField.setAccessible(true);
        ((List<?>) allField.get(null)).clear();
    }

    /**
     * Enqueue adds to fifo and all.
     */
    @Test
    public void enqueue_AddsToFifoAndAll() {
        Backorder b = new Backorder(1, pharmacy);
        dao.enqueue(b);

        assertEquals("Should be added to 'all' list", 1, dao.findAll().size());
        assertTrue("Flag should be set to true", dao.getFlag());
        assertEquals("Poll should return the backorder", b, dao.poll());
    }

    /**
     * Enqueue null do nothing.
     */
    @Test
    public void enqueue_Null_DoNothing() {
        dao.enqueue(null);
        assertEquals(0, dao.findAll().size());
    }

    /**
     * Save updates existing or adds new.
     */
    @Test
    public void save_UpdatesExistingOrAddsNew() {
        Backorder b1 = new Backorder(1, pharmacy);
        dao.save(b1);
        assertEquals(OrderState.BACKORDER, dao.find(1).getState());


        Backorder b1Updated = new Backorder(1, pharmacy);
        b1Updated.cancel();

        dao.save(b1Updated);

        assertEquals(1, dao.findAll().size());
        assertEquals(OrderState.CANCELED, dao.find(1).getState());
    }

    /**
     * Find by pharmacy test.
     */
    @Test
    public void findByPharmacy_Test() {
        Backorder b1 = new Backorder(1, pharmacy);

        Pharmacy otherPharmacy = new Pharmacy("Other", 99999, null, null, "pass");
        Backorder b2 = new Backorder(2, otherPharmacy);


        assertNotEquals("Τα φαρμακεία πρέπει να είναι διαφορετικά", pharmacy, otherPharmacy);

        dao.save(b1);
        dao.save(b2);

        List<Backorder> results = dao.findByPharmacy(pharmacy);

        assertEquals("Πρέπει να βρει μόνο 1 παραγγελία", 1, results.size());
        assertEquals(b1, results.get(0));

        assertEquals(0, dao.findByPharmacy(null).size());
    }

    /**
     * Find by state test.
     */
    @Test
    public void findByState_Test() {
        Backorder b1 = new Backorder(1, pharmacy);
        dao.save(b1);

        Backorder b2 = new Backorder(2, pharmacy);
        b2.cancel();
        dao.save(b2);

        Backorder b3 = new Backorder(3, pharmacy);
        b3.tryMarkCompleted();
        dao.save(b3);

        assertEquals(1, dao.findByState(OrderState.BACKORDER).size());
        assertEquals(1, dao.findByState(OrderState.CANCELED).size());
        assertEquals(1, dao.findByState(OrderState.COMPLETEDBACKORDER).size());
    }

    /**
     * Find returns null if not found.
     */
    @Test
    public void find_ReturnsNullIfNotFound() {
        assertNull(dao.find(999));
    }
}
