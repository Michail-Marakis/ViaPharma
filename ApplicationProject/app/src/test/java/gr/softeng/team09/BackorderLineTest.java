package gr.softeng.team09;

import gr.softeng.team09.domain.BackorderLine;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Backorder line test.
 */
public class BackorderLineTest {

    private Product product;
    private BackorderLine line;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        product = new Product("Proda", ProductCategory.ANTIBIOTIC, 12345, 10.0);
        line = new BackorderLine(product, 5);
    }

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(product, line.getProduct());
        assertEquals(5, line.getQuantityPending());
        assertEquals(5, line.getQuantityRequested());
        assertFalse(line.isFulfilled());
    }

    /**
     * Test constructor negative quantity becomes zero.
     */
    @Test
    public void testConstructorNegativeQuantityBecomesZero() {
        BackorderLine l2 = new BackorderLine(product, -3);
        assertEquals(0, l2.getQuantityPending());
        assertEquals(0, l2.getQuantityRequested());
        assertTrue(l2.isFulfilled());
    }

    /**
     * Test decrease pending positive delta.
     */
    @Test
    public void testDecreasePendingPositiveDelta() {
        line.decreasePending(2);
        assertEquals(3, line.getQuantityPending());
        assertFalse(line.isFulfilled());

        line.decreasePending(3);
        assertEquals(0, line.getQuantityPending());
        assertTrue(line.isFulfilled());
    }

    /**
     * Test decrease pending zero or negative does nothing.
     */
    @Test
    public void testDecreasePendingZeroOrNegativeDoesNothing() {
        line.decreasePending(0);
        assertEquals(5, line.getQuantityPending());

        line.decreasePending(-2);
        assertEquals(5, line.getQuantityPending());
    }

    /**
     * Test decrease pending cannot become negative.
     */
    @Test
    public void testDecreasePendingCannotBecomeNegative() {
        line.decreasePending(100);
        assertEquals(0, line.getQuantityPending());
        assertTrue(line.isFulfilled());
    }

    /**
     * Νέο Test για κάλυψη της μεθόδου getQuantityFulfilled().
     * Ελέγχει τον υπολογισμό: quantityRequested - quantityPending.
     */
    @Test
    public void testGetQuantityFulfilled() {
        assertEquals(0, line.getQuantityFulfilled());


        line.decreasePending(2);
        assertEquals(2, line.getQuantityFulfilled());

        line.decreasePending(100);
        assertEquals(5, line.getQuantityFulfilled());
    }
}