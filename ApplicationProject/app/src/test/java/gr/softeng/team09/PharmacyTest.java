package gr.softeng.team09;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * The type Pharmacy test.
 */
public class PharmacyTest {

    private Pharmacy pharmacy;
    private Inventory inventory;
    private Product productA;
    private Product productB;
    private Pharmacist pro;
    
    private static int uniqueCounter = 0;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        pro = new Pharmacist("nikos", "p", "p3230267@aueb.gr", 765433, "dsd");
        Address addr = new Address("Οδός", 1, "Πόλη", "12345");

        pharmacy = new Pharmacy("Test Pharmacy", 1231313132, pro, addr, "password123");

        Clerk clerk = new Clerk("prodromos", "marakis", "32323", "sda", "pass");

        BatchDAO batchDAO = new BatchDAOMemory();
        InventoryLineDAO inventoryLineDAO = new InventoryLineDAOMemory();
        BackorderDAO backorderDAO = new BackorderDAOMemory();
        ReservationDAO reservationDAO = new ReservationDAOMemory();

        inventory = new Inventory(clerk, batchDAO, inventoryLineDAO, backorderDAO, reservationDAO);

        int idA = 50000 + (++uniqueCounter);
        int idB = 60000 + (++uniqueCounter);

        productA = new Product("Proda_" + uniqueCounter, ProductCategory.ANTIBIOTIC, idA, 10.0);
        productB = new Product("Prodb_" + uniqueCounter, ProductCategory.ANALGESIC, idB, 5.0);

        Batch batchA = new Batch(inventory.nextBatchId(), productA, 5, 111);
        inventory.receiveBatch(batchA);

        Batch batchB = new Batch(inventory.nextBatchId(), productB, 2, 222);
        inventory.receiveBatch(batchB);
    }

    /**
     * Test get name and afm.
     */
    @Test
    public void testGetNameAndAfm() {
        assertEquals("Test Pharmacy", pharmacy.getName());
        assertEquals(1231313132, pharmacy.getAfm());
    }

    /**
     * Test set and get password.
     */
    @Test
    public void testSetAndGetPassword() {
        assertEquals("password123", pharmacy.getPassword());
        pharmacy.setPassword("newpass");
        assertEquals("newpass", pharmacy.getPassword());
    }

    /**
     * Test submit order already in draft orders.
     */
    @Test
    public void testSubmitOrderAlreadyInDraftOrders() {

        Order o = new Order(500, pharmacy);
        o.addLine(new OrderLine(productA, 10));

        int smallBudget = 1;

        pharmacy.submitOrder(o, inventory, smallBudget, false);


        assertTrue(pharmacy.draftOrders.contains(o));


        pharmacy.submitOrder(o, inventory, smallBudget, false);


        int count = 0;
        for(Order or : pharmacy.draftOrders){
            if(or.getId() == o.getId()){
                count++;
            }
        }

        assertEquals(1, count);
    }


    /**
     * Test set owner null.
     */
    @Test
    public void testSetOwnerNull() {
        pharmacy.setOwner(null);
        assertNull(pharmacy.getOwner());
    }

    /**
     * Test get cart.
     */
    @Test
    public void testGetCart() {
        assertTrue(pharmacy.getCart().isEmpty());

        Order o = new Order(999, pharmacy);
        OrderLine line = new OrderLine(productA, 1);
        pharmacy.addToCart(o, line);

        assertEquals(1, pharmacy.getCart().size());
    }

    /**
     * Test add to cart increases quantity if found.
     */
    @Test
    public void testAddToCartIncreasesQuantityIfFound() {
        Order o = new Order(888, pharmacy);
        OrderLine line1 = new OrderLine(productA, 1);
        pharmacy.addToCart(o, line1);

        OrderLine line2 = new OrderLine(productA, 2);
        pharmacy.addToCart(o, line2);

        assertEquals(1, o.getLines().size());
        assertEquals(3, o.getLines().get(0).getQuantity());
    }

    /**
     * Test calculate order total.
     */
    @Test
    public void testCalculateOrderTotal() {
        Order o = new Order(103, pharmacy);
        o.addLine(new OrderLine(productA, 2));
        o.addLine(new OrderLine(productB, 1));

        double expected = 2 * productA.getPriceWithVAT()
                + 1 * productB.getPriceWithVAT();
        double actual = pharmacy.calculateOrderTotal(o);

        assertEquals(expected, actual, 0.0001);
    }

    /**
     * Test calculate order total null throws.
     */
    @Test(expected = NullPointerException.class)
    public void testCalculateOrderTotalNullThrows() {
        pharmacy.calculateOrderTotal(null);
    }

    /**
     * Test submit order payment rejected.
     */
    @Test
    public void testSubmitOrderPaymentRejected() {
        Order o = new Order(104, pharmacy);
        o.addLine(new OrderLine(productA, 2));

        pharmacy.submitOrder(o, inventory, 1, true);

        assertNotEquals(OrderState.PENDING, o.getState());
    }

    /**
     * Test submit order null order throws.
     */
    @Test(expected = NullPointerException.class)
    public void testSubmitOrderNullOrderThrows() {
        pharmacy.submitOrder(null, inventory, 100, true);
    }

    /**
     * Test submit order null inventory throws.
     */
    @Test(expected = NullPointerException.class)
    public void testSubmitOrderNullInventoryThrows() {
        Order o = new Order(1, pharmacy);
        pharmacy.submitOrder(o, null, 100, true);
    }

    /**
     * Test submit order payment accepted full stock backorders allowed.
     */
    @Test
    public void testSubmitOrderPaymentAccepted_FullStock_BackordersAllowed() {
        Order o = new Order(105, pharmacy);
        o.addLine(new OrderLine(productA, 2));

        int bigBudget = 1_000_000;
        pharmacy.submitOrder(o, inventory, bigBudget, true);

        assertEquals(OrderState.PENDING, o.getState());
        assertTrue(pharmacy.personalPendingOrders.contains(o));
    }

    /**
     * ΔΙΟΡΘΩΣΗ: Εξαναγκάζουμε το απόθεμα να είναι 5 πριν τον έλεγχο,
     * ώστε να μην επηρεάζεται από "σκουπίδια" προηγούμενων tests.
     */
    @Test
    public void testSubmitOrderPaymentAccepted_ShortStock_NoBackorders() {
        InventoryLine line = inventory.findLine(productA);
        if(line != null) line.setQuantity(5);

        Order o = new Order(106, pharmacy);
        o.addLine(new OrderLine(productA, 10));

        int bigBudget = 1_000_000;

        pharmacy.submitOrder(o, inventory, bigBudget, false);

        assertEquals(OrderState.PENDING, o.getState());

        assertEquals(5, o.getLines().get(0).getQuantity());
        assertTrue(pharmacy.personalPendingOrders.contains(o));
    }

    /**
     * Test submit order payment accepted short stock backorders allowed.
     */
    @Test
    public void testSubmitOrderPaymentAccepted_ShortStock_BackordersAllowed() {
        InventoryLine line = inventory.findLine(productA);
        if(line != null) line.setQuantity(5);

        Order o = new Order(107, pharmacy);
        o.addLine(new OrderLine(productA, 10));

        int bigBudget = 1_000_000;
        pharmacy.submitOrder(o, inventory, bigBudget, true);

        assertEquals(OrderState.PENDING, o.getState());
        assertEquals(10, o.getLines().get(0).getQuantity());
    }

    /**
     * Delete submitted order.
     */
    @Test
    public void deleteSubmittedOrder(){
        Clerk clerk = new Clerk("prodromos", "marakis", "32323", "sda", "pass");
        BatchDAO batchDAO = new BatchDAOMemory();
        InventoryLineDAO inventoryLineDAO = new InventoryLineDAOMemory();
        BackorderDAO backorderDAO = new BackorderDAOMemory();
        ReservationDAO reservationDAO = new ReservationDAOMemory();
        Inventory localInv = new Inventory(clerk, batchDAO, inventoryLineDAO, backorderDAO, reservationDAO);

        Product pA = new Product("Proda_Local", ProductCategory.ANTIBIOTIC, 999999, 10.0);
        Batch batchA = new Batch(1, pA, 5, 111);
        localInv.receiveBatch(batchA);

        Order o = new Order(104, pharmacy);
        o.addLine(new OrderLine(pA, 2));

        pharmacy.submitOrder(o, localInv, 12212, true);

        assertFalse(pharmacy.personalPendingOrders.isEmpty());

        pharmacy.deleteSubmittedOrders(o, localInv);
        assertTrue(pharmacy.personalPendingOrders.isEmpty());
    }

    /**
     * Test check availability for order product not in inventory.
     */
    @Test
    public void testCheckAvailabilityForOrderProductNotInInventory() {
        Product productC = new Product("Prodc", ProductCategory.OTHER, 99999, 7.0);

        Order o = new Order(200, pharmacy);
        o.addLine(new OrderLine(productC, 5));

        assertNull(inventory.findLine(productC));

        Map<Product, Integer> availability = pharmacy.checkAvailabilityForOrder(o, inventory);

        assertTrue(availability.containsKey(productC));
        assertEquals(0, (int) availability.get(productC));
    }

    /**
     * Test update order with availability removes unavailable products.
     */
    @Test
    public void testUpdateOrderWithAvailabilityRemovesUnavailableProducts() {
        Product productC = new Product("Prodc", ProductCategory.OTHER, 88888, 7.0);

        Order o = new Order(201, pharmacy);
        o.addLine(new OrderLine(productC, 5));

        int bigBudget = 1_000_000;
        pharmacy.submitOrder(o, inventory, bigBudget, false);

        boolean hasC = false;
        for (OrderLine l : o.getLines()) {
            if (l.getProduct().equals(productC)) {
                hasC = true;
                break;
            }
        }
        assertFalse(hasC);
    }

    /**
     * Test update order with availability null and non null can serve.
     */
    @Test
    public void testUpdateOrderWithAvailabilityNullAndNonNullCanServe() {
        Product productC = new Product("Prodc", ProductCategory.OTHER, 77777, 7.0);

        Order o = new Order(400, pharmacy);
        o.addLine(new OrderLine(productA, 5));
        o.addLine(new OrderLine(productC, 4));

        Map<Product, Integer> availability = new HashMap<>();
        availability.put(productA, 3);

        pharmacy.updateOrderWithAvailability(o, availability);

        OrderLine lineA = null;
        boolean hasC = false;
        for (OrderLine l : o.getLines()) {
            if (l.getProduct().equals(productA)) {
                lineA = l;
            }
            if (l.getProduct().equals(productC)) {
                hasC = true;
            }
        }

        assertNotNull(lineA);
        assertEquals(3, lineA.getQuantity());
        assertFalse(hasC);
    }

    /**
     * Test equals and hash code.
     */
    @Test
    public void testEqualsAndHashCode() {
        Address addr = new Address("A", 1, "B", "C");
        Pharmacy p1 = new Pharmacy("P1", 100, pro, addr, "pass");
        Pharmacy p2 = new Pharmacy("P2", 100, pro, addr, "pass");
        Pharmacy p3 = new Pharmacy("P3", 200, pro, addr, "pass");

        assertTrue(p1.equals(p1));
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(null));
        assertFalse(p1.equals(new Object()));

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }
}