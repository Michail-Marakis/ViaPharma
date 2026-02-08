package gr.softeng.team09;

import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * The type Batch test.
 */
public class BatchTest {

    private Product product;
    private Batch batch;
    private Pharmacy pharmacy1;
    private Pharmacy pharmacy2;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        product = new Product("Proda", ProductCategory.ANTIBIOTIC, 12345, 10.0);
        batch = new Batch(1, product, 111, 10);

        Pharmacist ph1 = new Pharmacist("Nikos", "P", "p3230267@aueb.gr",4344343,"d");
        Pharmacist ph2 = new Pharmacist("Giorgos", "K", "p3230267@aueb.gr", 6545435, "dsd");
        Address addr1 = new Address("Οδός1", 1, "Πόλη1", "11111");
        Address addr2 = new Address("Οδός2", 2, "Πόλη2", "22222");

        pharmacy1 = new Pharmacy("Pharmacy1",1,ph1,addr1,"123456789");
        pharmacy2 = new Pharmacy("Pharmacy2", 2 , ph2, addr2,"987654321");
    }

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(1, batch.getId());
        assertEquals(product, batch.getProduct());
        assertEquals(111, batch.getBatchNumber());
        assertEquals(10, batch.getQuantity());
        assertNotNull(batch.getDistributedTo());
        assertTrue(batch.getDistributedTo().isEmpty());
    }

    /**
     * Test quantity cannot be negative in constructor.
     */
    @Test
    public void testQuantityCannotBeNegativeInConstructor() {
        Batch b2 = new Batch(2, product, 222, -5);
        assertEquals(0, b2.getQuantity());
    }

    /**
     * Test consume normal.
     */
    @Test
    public void testConsumeNormal() {
        int consumed = batch.consume(4,pharmacy1);
        assertEquals(4, consumed);
        assertEquals(6, batch.getQuantity());
    }

    /**
     * Test consume more than available.
     */
    @Test
    public void testConsumeMoreThanAvailable() {
        int consumed = batch.consume(100,pharmacy1);
        assertEquals(10, consumed);       //όσο είχε
        assertEquals(0, batch.getQuantity());
    }

    /**
     * Test consume zero or negative.
     */
    @Test
    public void testConsumeZeroOrNegative() {
        int consumed0 = batch.consume(0,pharmacy1);
        assertEquals(0, consumed0);
        assertEquals(10, batch.getQuantity());

        int consumedNeg = batch.consume(-5,pharmacy1);
        assertEquals(0, consumedNeg);
        assertEquals(10, batch.getQuantity());
    }

    /**
     * Test logic fixed:
     * Pharmacies are added to the list via consume(), not addPharmacy().
     */
    @Test
    public void testRecallSetsQuantityToZero() {
        // Distribute some items so pharmacies are tracked
        batch.consume(2, pharmacy1);
        batch.consume(3, pharmacy2);

        assertEquals(5, batch.getQuantity()); // 10 - 2 - 3 = 5
        assertEquals(2, batch.getDistributedTo().size());

        // Call recall
        batch.recall();

        // Quantity becomes 0
        assertEquals(0, batch.getQuantity());

        // Traceability list remains populated
        assertEquals(2, batch.getDistributedTo().size());
    }

    /**
     * Test equals and hash code.
     */
    @Test
    public void testEqualsAndHashCode() {
        Batch b2 = new Batch(1, product, 999, 5); //same id
        Batch b3 = new Batch(3, product, 111, 10); //diff id

        assertEquals(batch, b2);
        assertEquals(b2, batch);
        assertEquals(batch.hashCode(), b2.hashCode());

        assertNotEquals(batch, b3);
        assertNotEquals(b3, batch);

        assertEquals(batch, batch);
        assertNotEquals(null, batch);
        assertNotEquals("string", batch);
    }

    /**
     *
     * Tests getDistributedTotal() and getDistributedQty().
     */
    @Test
    public void testGetDistributedTotalAndQty() {
        batch.consume(2, pharmacy1);
        batch.consume(3, pharmacy2);

        assertEquals(5, batch.getDistributedTotal());

        Map<Pharmacy, Integer> map = batch.getDistributedQty();
        assertEquals(2, map.size());
        assertEquals(Integer.valueOf(2), map.get(pharmacy1));
        assertEquals(Integer.valueOf(3), map.get(pharmacy2));
    }
}