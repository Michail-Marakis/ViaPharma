package gr.softeng.team09;

import gr.softeng.team09.domain.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Backorder test.
 */
public class BackorderTest {

    private Pharmacy pharmacy;
    private Pharmacist pharmacist;
    private Product productA;
    private Product productB;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        pharmacist = new Pharmacist("Nikos", "P", "nikos@aueb.gr", 123456, "pass");
        pharmacy = new Pharmacy("TestPharmacy", 999999, pharmacist,
                new Address("Street", 1, "City", "12345"), "pwd");

        productA = new Product("ProdA", ProductCategory.ANTIBIOTIC, 1, 10.0);
        productB = new Product("ProdB", ProductCategory.ANALGESIC, 2, 5.0);
    }

    /**
     * Test constructors and getters.
     */
    @Test
    public void testConstructorsAndGetters() {
        // Constructor με default timestamp
        Backorder bo1 = new Backorder(1, pharmacy);
        assertEquals(1, bo1.getId());
        assertEquals(pharmacy, bo1.getPharmacy());
        assertEquals(pharmacist, bo1.getPharmacist());
        assertEquals(OrderState.BACKORDER, bo1.getState());
        assertNotNull(bo1.getLines());

        // Constructor με explicit timestamp
        long ts = System.currentTimeMillis();
        Backorder bo2 = new Backorder(2, pharmacy, ts);
        assertEquals(2, bo2.getId());
        assertEquals(pharmacy, bo2.getPharmacy());
    }

    /**
     * Test add line.
     */
    @Test
    public void testAddLine() {
        Backorder bo = new Backorder(3, pharmacy);
        BackorderLine line1 = new BackorderLine(productA, 5);
        BackorderLine line2 = new BackorderLine(productB, 3);

        // Προσθήκη μη null γραμμής
        bo.addLine(line1);
        assertEquals(1, bo.getLines().size());
        assertTrue(bo.getLines().contains(line1));

        // Προσθήκη null γραμμής → δεν προστίθεται
        bo.addLine(null);
        assertEquals(1, bo.getLines().size());

        // Προσθήκη ακόμα μίας γραμμής
        bo.addLine(line2);
        assertEquals(2, bo.getLines().size());
        assertTrue(bo.getLines().contains(line2));
    }

    /**
     * Test is fully fulfilled.
     */
    @Test
    public void testIsFullyFulfilled() {
        Backorder bo = new Backorder(4, pharmacy);
        BackorderLine line1 = new BackorderLine(productA, 5);
        BackorderLine line2 = new BackorderLine(productB, 3);

        bo.addLine(line1);
        bo.addLine(line2);

        // Αρχικά κανένα δεν είναι fulfilled
        assertFalse(bo.isFullyFulfilled());

        // Σημαίνουμε γραμμές ως fulfilled
        line1.decreasePending(5);
        assertFalse(bo.isFullyFulfilled()); // line2 όχι ακόμα

        line2.decreasePending(3);
        assertTrue(bo.isFullyFulfilled());
    }

    /**
     * Test try mark completed.
     */
    @Test
    public void testTryMarkCompleted() {
        Backorder bo = new Backorder(5, pharmacy);
        BackorderLine line1 = new BackorderLine(productA, 5);
        bo.addLine(line1);

        // Μη πλήρως εκπληρωμένη → παραμένει BACKORDER
        bo.tryMarkCompleted();
        assertEquals(OrderState.BACKORDER, bo.getState());

        // Πλήρως εκπληρωμένη → γίνεται COMPLETEDBACKORDER
        line1.decreasePending(5);
        bo.tryMarkCompleted();
        assertEquals(OrderState.COMPLETEDBACKORDER, bo.getState());

        // Αν η κατάσταση δεν είναι BACKORDER, δεν αλλάζει
        bo.tryMarkCompleted();
        assertEquals(OrderState.COMPLETEDBACKORDER, bo.getState());
    }

    /**
     * Test cancel.
     */
    @Test
    public void testCancel() {
        Backorder bo = new Backorder(6, pharmacy);
        bo.cancel();
        assertEquals(OrderState.CANCELED, bo.getState());

        // Αν η κατάσταση δεν είναι BACKORDER, η ακύρωση δεν αλλάζει
        bo.cancel();
        assertEquals(OrderState.CANCELED, bo.getState());
    }
}
