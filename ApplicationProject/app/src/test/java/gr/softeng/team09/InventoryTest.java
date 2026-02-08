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

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * The type Inventory test.
 */
public class InventoryTest {

    private Inventory inventory;
    private Pharmacy pharmacy;
    private Product productA;
    private Product productB;

    private static int uniqueCounter = 0;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        Clerk clerk = new Clerk("prodromos", "marakis", "32323", "sda", "1");
        BatchDAO batchDAO = new BatchDAOMemory();
        InventoryLineDAO inventoryLineDAO = new InventoryLineDAOMemory();
        BackorderDAO backorderDAO = new BackorderDAOMemory();
        ReservationDAO reservationDAO = new ReservationDAOMemory();

        inventory = new Inventory(clerk, batchDAO, inventoryLineDAO, backorderDAO, reservationDAO);

        Pharmacist ph = new Pharmacist("Nikos", "P", "p3230267@aueb.gr", 323,"32");
        Address addr = new Address("Οδός", 1, "Πόλη", "12345");
        pharmacy = new Pharmacy("Ph1", 123456789, ph, addr,"pass");

        int codeA = 10000 + (++uniqueCounter);
        int codeB = 20000 + (++uniqueCounter);

        productA = new Product("Proda_" + codeA, ProductCategory.ANTIBIOTIC, codeA, 10.0);
        productB = new Product("Prodb_" + codeB, ProductCategory.ANALGESIC, codeB, 5.0);
    }

    /**
     * Test set and get owner.
     */
    @Test
    public void testSetAndGetOwner() {
        Clerk newClerk = new Clerk("New", "Clerk", "000", "e", "p");
        inventory.setOwner(newClerk);
        assertEquals(newClerk, inventory.getOwner());
    }

    /**
     * Test get all batches.
     */
    @Test
    public void testGetAllBatches() {
        Batch b = new Batch(inventory.nextBatchId(), productA, 111, 10);
        inventory.receiveBatch(b);
        assertFalse(inventory.getAllBatches().isEmpty());
    }

    /**
     * Test unreserve stock for order and add or increase line.
     */
    @Test
    public void testUnreserveStockForOrderAndAddOrIncreaseLine() {
        InventoryLine line = new InventoryLine(productA, 0);
        inventory.getAvailableItems().add(line);

        Order o = new Order(inventory.nextBatchId() + 10000, pharmacy);
        o.addLine(new OrderLine(productA, 5));
        inventory.reservationDAO.put(o.getId(), Map.of(productA, 5));

        inventory.unreserveStockForOrder(o);

        InventoryLine updatedLine = inventory.findLine(productA);
        assertNotNull(updatedLine);
        assertEquals(5, updatedLine.getQuantity());

        assertNull(inventory.reservationDAO.get(o.getId()));

        Order testOrder = new Order(inventory.nextBatchId() + 10001, pharmacy);
        testOrder.addLine(new OrderLine(productA, 3));

        inventory.receiveBatch(new Batch(inventory.nextBatchId(), productA, 100, 0));
        inventory.executeOrder(testOrder, true);

        boolean found = false;
        for (OrderLine ol : testOrder.getLines()) {
            if (ol.getProduct().equals(productA) && ol.getQuantity() >= 3) {
                found = true;
            }
        }
        assertTrue(found);

        Backorder bo = new Backorder(inventory.nextBatchId() + 10002, pharmacy);
        BackorderLine bol = new BackorderLine(productB, 5);
        bo.addLine(bol);
        inventory.addBackorder(bo);

        Batch batch = new Batch(inventory.nextBatchId(), productB, 100, 5);
        inventory.receiveBatch(batch);

        assertTrue(bol.isFulfilled());
    }


    /**
     * Διορθωμένο test: Ψάχνει το συγκεκριμένο προϊόν αντί για το index 0.
     */
    @Test
    public void testGetAvailableItems() {
        Batch b = new Batch(inventory.nextBatchId(), productA, 111, 10);
        inventory.receiveBatch(b);

        List<InventoryLine> items = inventory.getAvailableItems();
        assertFalse(items.isEmpty());

        boolean found = false;
        for(InventoryLine line : items) {
            if(line.getProduct().equals(productA)) {
                found = true;
                break;
            }
        }
        assertTrue("Product A should be in available items", found);
    }

    /**
     * Test cancel order by id non existent.
     */
    @Test
    public void testCancelOrderByIdNonExistent() {
        assertFalse(inventory.cancelOrderById(999999));
    }

    /**
     * Test cancel order by id completed fails.
     */
    @Test
    public void testCancelOrderByIdCompletedFails() {
        Batch batch = new Batch(inventory.nextBatchId(), productA, 100, 341);
        inventory.receiveBatch(batch);

        Order order = new Order(inventory.nextBatchId() + 9000, pharmacy);
        order.addLine(new OrderLine(productA, 3));

        inventory.executeOrder(order, true);
        assertEquals(OrderState.COMPLETED, order.getState());

        boolean result = inventory.cancelOrderById(order.getId());
        assertFalse("Should not cancel COMPLETED order", result);
        assertEquals(OrderState.COMPLETED, order.getState());
    }

    /**
     * Test cancel order by id already canceled.
     */
    @Test
    public void testCancelOrderByIdAlreadyCanceled() {
        Order order = new Order(inventory.nextBatchId() + 1500, pharmacy);
        order.setState(OrderState.CANCELED);
        inventory.addToAll(order);

        assertFalse(inventory.cancelOrderById(order.getId()));
    }

    /**
     * Test cancel order by id pending or draft fails.
     */
    @Test
    public void testCancelOrderByIdPendingOrDraftFails() {
        int baseId = inventory.nextBatchId() + 2000;

        Order o1 = new Order(baseId + 1, pharmacy);
        o1.setState(OrderState.PENDING);
        inventory.addToAll(o1);
        assertFalse(inventory.cancelOrderById(o1.getId()));

        Order o2 = new Order(baseId + 2, pharmacy);
        o2.setState(OrderState.DRAFT);
        inventory.addToAll(o2);
        assertFalse(inventory.cancelOrderById(o2.getId()));

        Order o3 = new Order(baseId + 3, pharmacy);
        o3.setState(OrderState.BACKORDER);
        inventory.addToAll(o3);
        assertFalse(inventory.cancelOrderById(o3.getId()));
    }

    /**
     * Test cancel order by id to cancel state success.
     */
    @Test
    public void testCancelOrderByIdToCancelStateSuccess() {
        Order order = new Order(inventory.nextBatchId() + 3000, pharmacy);
        order.setState(OrderState.TOCANCEL);
        inventory.addToAll(order);

        assertTrue(inventory.cancelOrderById(order.getId()));
        assertEquals(OrderState.CANCELED, order.getState());
    }

    /**
     * Test execute order full completion.
     */
    @Test
    public void testExecuteOrderFullCompletion() {
        Batch batch = new Batch(inventory.nextBatchId(), productA, 100, 10);
        inventory.receiveBatch(batch);

        Order o = new Order(inventory.nextBatchId() + 4000, pharmacy);
        o.addLine(new OrderLine(productA, 5));

        inventory.executeOrder(o, true);

        assertEquals(OrderState.COMPLETED, o.getState());
        assertTrue(inventory.getCompletedOrders().contains(o));
        assertTrue(inventory.getAllOrders().contains(o));
    }

    /**
     * Test execute order cannot reserve stays pending or backorder.
     */
    @Test
    public void testExecuteOrderCannotReserveStaysPendingOrBackorder() {
        Order o = new Order(inventory.nextBatchId() + 5000, pharmacy);
        o.addLine(new OrderLine(productA, 5));

        inventory.executeOrder(o, true);

        assertEquals(OrderState.BACKORDER, o.getState());
        assertFalse(inventory.getCompletedOrders().contains(o));
    }

    /**
     * Test receive batch triggers backorder fulfillment.
     */
    @Test
    public void testReceiveBatchTriggersBackorderFulfillment() {
        Backorder bo = new Backorder(inventory.nextBatchId() + 6000, pharmacy);
        BackorderLine bol = new BackorderLine(productA, 5);
        bo.addLine(bol);
        inventory.addBackorder(bo);

        Batch batch = new Batch(inventory.nextBatchId(), productA, 300, 5);
        inventory.receiveBatch(batch);

        assertTrue(bol.isFulfilled());
    }

    /**
     * Test receive batch with null product.
     */
    @Test
    public void testReceiveBatchWithNullProduct() {
        Batch batch = new Batch(inventory.nextBatchId(), productA, 123, 10);
        inventory.receiveBatch(batch);

        InventoryLine line = inventory.findLine(productA);
        assertNotNull(line);
        assertTrue(line.getQuantity() >= 10);
    }

    /**
     * Test recall batch removes quantity.
     */
    @Test
    public void testRecallBatchRemovesQuantity() {
        int batchId = inventory.nextBatchId();
        Batch batch = new Batch(batchId, productA, 500, 8);
        inventory.receiveBatch(batch);

        Order o = new Order(inventory.nextBatchId() + 7000, pharmacy);
        o.addLine(new OrderLine(productA, 1));
        inventory.executeOrder(o, true);

        inventory.recallBatch(batchId);

        InventoryLine line = inventory.findLine(productA);
        assertEquals(0, line.getQuantity());
    }

    /**
     * Test recall batch invalid id.
     */
    @Test
    public void testRecallBatchInvalidId() {
        inventory.recallBatch(999999);
    }

    /**
     * Test fulfill order partial with backorder.
     */
    @Test
    public void testFulfillOrderPartialWithBackorder() {
        Batch batch = new Batch(inventory.nextBatchId(), productA, 100, 5);
        inventory.receiveBatch(batch);

        Order o = new Order(inventory.nextBatchId() + 8000, pharmacy);
        o.addLine(new OrderLine(productA, 8));

        inventory.executeOrder(o, true);

        assertEquals(OrderState.COMPLETED, o.getState());

        List<Backorder> backorders = inventory.getBackorders();
        boolean foundSpecific = false;
        for(Backorder bo : backorders) {
            for(BackorderLine bol : bo.getLines()) {
                if(bol.getProduct().equals(productA) && bol.getQuantityPending() == 3) {
                    foundSpecific = true;
                    break;
                }
            }
        }
        assertTrue("Should have a backorder for 3 units of productA", foundSpecific);
    }

    /**
     * Test execute order without backorders ships what exists.
     */
    @Test
    public void testExecuteOrderWithoutBackordersShipsWhatExists() {
        Batch batch = new Batch(inventory.nextBatchId(), productA, 100, 3);
        inventory.receiveBatch(batch);

        Order o = new Order(inventory.nextBatchId() + 9000, pharmacy);
        o.addLine(new OrderLine(productA, 5));

        inventory.executeOrder(o, false);

        assertEquals(OrderState.COMPLETED, o.getState());
        assertEquals(3, o.getLines().get(0).getQuantity());

        InventoryLine line = inventory.findLine(productA);
        assertEquals(0, line.getQuantity());
    }

    /**
     * Test execute order mixed availability.
     */
    @Test
    public void testExecuteOrderMixedAvailability() {
        inventory.receiveBatch(new Batch(inventory.nextBatchId(), productA, 1, 10));

        Order o = new Order(inventory.nextBatchId() + 9500, pharmacy);
        o.addLine(new OrderLine(productA, 5));
        o.addLine(new OrderLine(productB, 5));

        inventory.executeOrder(o, true);

        assertEquals(OrderState.COMPLETED, o.getState());
        assertEquals(1, o.getLines().size());
        assertEquals(productA, o.getLines().get(0).getProduct());

        boolean foundB = false;
        for(Backorder bo : inventory.getBackorders()) {
            for(BackorderLine bol : bo.getLines()) {
                if(bol.getProduct().equals(productB) && bol.getQuantityPending() == 5) {
                    foundB = true;
                }
            }
        }
        assertTrue("Should have backorder for productB", foundB);
    }

    /**
     * Test execute order all items unavailable no backorder.
     */
    @Test
    public void testExecuteOrderAllItemsUnavailableNoBackorder() {
        Order o = new Order(inventory.nextBatchId() + 9600, pharmacy);
        o.addLine(new OrderLine(productA, 5));

        inventory.executeOrder(o, false);

        assertEquals(OrderState.CANCELED, o.getState());
    }

    /**
     * Test execute order by id.
     */
    @Test
    public void testExecuteOrderById() {
        assertFalse(inventory.executeOrderById(999999, true));

        int id1 = inventory.nextBatchId() + 9700;
        Order o = new Order(id1, pharmacy);
        o.addLine(new OrderLine(productA, 5));
        o.setState(OrderState.PENDING);
        inventory.addToAll(o);

        assertTrue(inventory.executeOrderById(id1, true));
        assertEquals(OrderState.BACKORDER, o.getState());

        int id2 = inventory.nextBatchId() + 9701;
        Order o2 = new Order(id2, pharmacy);
        o2.setState(OrderState.DRAFT);
        inventory.addToAll(o2);
        assertFalse(inventory.executeOrderById(id2, true));
    }

    /**
     * Test backorders served in fifo order.
     */
    @Test
    public void testBackordersServedInFifoOrder() {
        Pharmacist ph2 = new Pharmacist("Maria", "K", "m@gmail.com", 323233, "dsd");
        Address addr2 = new Address("Οδός2", 2, "Πόλη2", "54321");
        Pharmacy pharmacy2 = new Pharmacy("Ph2", 987654321, ph2, addr2,"pass");

        Backorder bo1 = new Backorder(inventory.nextBatchId() + 9800, pharmacy);
        BackorderLine bol1 = new BackorderLine(productA, 5);
        bo1.addLine(bol1);
        inventory.addBackorder(bo1);

        Backorder bo2 = new Backorder(inventory.nextBatchId() + 9801, pharmacy2);
        BackorderLine bol2 = new BackorderLine(productA, 5);
        bo2.addLine(bol2);
        inventory.addBackorder(bo2);

        Batch batch = new Batch(inventory.nextBatchId(), productA, 100, 7);
        inventory.receiveBatch(batch);

        assertTrue(bol1.isFulfilled());

        assertFalse(bol2.isFulfilled());
        assertEquals(3, bol2.getQuantityPending());
    }

    /**
     * Test postpone order on purpose.
     */
    @Test
    public void testPostponeOrderOnPurpose() {
        Order o = new Order(inventory.nextBatchId() + 9900, pharmacy);
        o.addLine(new OrderLine(productA, 7));
        o.setState(OrderState.PENDING);
        inventory.addToAll(o);

        inventory.postponeOrderOnPurpose(o);
        assertEquals(OrderState.PENDING, o.getState());

        inventory.postponeOrderOnPurpose(null);
    }

    /**
     * Test get back from one pharm.
     */
    @Test
    public void testGetBackFromOnePharm() {
        Backorder bo1 = new Backorder(inventory.nextBatchId() + 9950, pharmacy);
        inventory.addBackorder(bo1);

        List<Backorder> list = inventory.getBackFromOnePharm(pharmacy.getAfm());
        assertTrue(list.contains(bo1));

        assertTrue(inventory.getBackFromOnePharm(-1).isEmpty());
        assertTrue(inventory.getBackFromOnePharm(9999999).isEmpty());
    }

    /**
     * Test add backorder null.
     */
    @Test
    public void testAddBackorderNull() {
        inventory.addBackorder(null);
    }

    /**
     * Test unreserve stock for order.
     */
    @Test
    public void testUnreserveStockForOrder() {
        inventory.unreserveStockForOrder(null);
    }

    /**
     * Test allocate from batches edge cases.
     */
    @Test
    public void testAllocateFromBatchesEdgeCases() {
        Batch b = new Batch(inventory.nextBatchId(), productA, 555, 0);
        inventory.receiveBatch(b);

        Order o = new Order(inventory.nextBatchId() + 9990, pharmacy);
        o.addLine(new OrderLine(productA, 5));

        inventory.executeOrder(o, true);
        assertEquals(OrderState.BACKORDER, o.getState());
    }

    /**
     * Test receive batch creates and then increases shipment line.
     */
    @Test
    public void testReceiveBatchCreatesAndThenIncreasesShipmentLine() {
        Backorder bo = new Backorder(inventory.nextBatchId() + 11000, pharmacy);
        bo.addLine(new BackorderLine(productA, 3));
        bo.addLine(new BackorderLine(productA, 2));
        inventory.addBackorder(bo);

        Batch batch = new Batch(inventory.nextBatchId(), productA, 999, 5);
        inventory.receiveBatch(batch);

        Order shipped = null;
        for (Order o : inventory.getCompletedOrders()) {
            if (o.getState() == OrderState.COMPLETEDBACKORDER) {
                shipped = o;
                break;
            }
        }

        assertNotNull(shipped);
        assertEquals(1, shipped.getLines().size());
        assertEquals(productA, shipped.getLines().get(0).getProduct());
        assertEquals(5, shipped.getLines().get(0).getQuantity());
    }

    /**
     * Test get owners list.
     * Verifies that the initial owner is added to the list.
     */
    @Test
    public void testGetOwners() {
        List<Clerk> owners = inventory.getOwners();
        assertNotNull(owners);
        assertFalse(owners.isEmpty());
        assertEquals(inventory.getOwner(), owners.get(0));
    }

    /**
     * Test add or increase line directly.
     * Checks the logic of grouping quantities for the same product in an order.
     */
    @Test
    public void testAddOrIncreaseLineDirectly() {
        Order order = new Order(100, pharmacy);

        inventory.addOrIncreaseLine(order, productA, 5);
        assertEquals(1, order.getLines().size());
        assertEquals(5, order.getLines().get(0).getQuantity());

        inventory.addOrIncreaseLine(order, productA, 3);
        assertEquals(1, order.getLines().size());
        assertEquals(8, order.getLines().get(0).getQuantity());

        inventory.addOrIncreaseLine(order, productB, 2);
        assertEquals(2, order.getLines().size());
    }

    /**
     * Test receive batch sends email on backorder completion.
     * Verifies that when a backorder is fulfilled via receiveBatch,
     * the Clerk's email list is updated (integration with Clerk logic).
     */
    @Test
    public void testReceiveBatchSendsEmailOnBackorderCompletion() {
        Backorder bo = new Backorder(inventory.nextBatchId(), pharmacy);
        BackorderLine bol = new BackorderLine(productA, 10);
        bo.addLine(bol);
        inventory.addBackorder(bo);

        Batch batch = new Batch(inventory.nextBatchId(), productA, 50, 10);
        inventory.receiveBatch(batch);

        assertTrue(bol.isFulfilled());

        Clerk owner = inventory.getOwner();
        List<StringBuilder> emails = owner.getEmailsSent();

        assertFalse("Clerk should have sent an email notification", emails.isEmpty());

        String emailContent = emails.get(0).toString();
        assertTrue(emailContent.contains("BACKORDER COMPLETED"));
        assertTrue(emailContent.contains(productA.getName()));
        assertTrue(emailContent.contains("Sent to: " + pharmacy.getName()));

        assertFalse(owner.getSentTo().isEmpty());
    }

}