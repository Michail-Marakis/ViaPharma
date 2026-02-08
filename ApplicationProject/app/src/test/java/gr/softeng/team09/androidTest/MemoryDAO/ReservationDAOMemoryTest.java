package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * The type Reservation dao memory test.
 */
public class ReservationDAOMemoryTest {

    private ReservationDAOMemory dao;
    private Product product;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        dao = new ReservationDAOMemory();
        product = new Product("P1", ProductCategory.OTHER, 1, 1.0);
        
        Field field = ReservationDAOMemory.class.getDeclaredField("byOrderId");
        field.setAccessible(true);
        ((Map<?, ?>) field.get(null)).clear();
    }

    /**
     * Put and get stores reservation.
     */
    @Test
    public void putAndGet_StoresReservation() {
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 5);

        dao.put(100, items);

        assertTrue("Should contain order 100", dao.contains(100));

        Map<Product, Integer> retrieved = dao.get(100);
        assertNotNull(retrieved);
        assertEquals("Should have reserved 5 items", (Integer) 5, retrieved.get(product));
    }

    /**
     * Put null map creates empty reservation.
     */
    @Test
    public void put_NullMap_CreatesEmptyReservation() {

        dao.put(200, null);

        assertTrue(dao.contains(200));
        assertTrue(dao.get(200).isEmpty());
    }

    /**
     * Remove deletes reservation.
     */
    @Test
    public void remove_DeletesReservation() {
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 2);
        dao.put(100, items);

        dao.remove(100);

        assertFalse("Should not contain order 100 after remove", dao.contains(100));
        assertNull("Get should return null", dao.get(100));
    }

    /**
     * Gets returns copy of map.
     */
    @Test
    public void get_ReturnsCopyOfMap() {
        Map<Product, Integer> items = new HashMap<>();
        items.put(product, 10);
        dao.put(100, items);


        Map<Product, Integer> retrieved = dao.get(100);
        retrieved.put(product, 999);

        Map<Product, Integer> retrievedAgain = dao.get(100);
        assertEquals((Integer) 10, retrievedAgain.get(product));
    }

    /**
     * Contains returns false for unknown id.
     */
    @Test
    public void contains_ReturnsFalseForUnknownId() {
        assertFalse(dao.contains(999));
    }
}
