package gr.softeng.team09;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Order test.
 */
public class OrderTest {

    private Pharmacy pharmacy;
    private Pharmacist pharmacist;
    private Order order;
    private Product productA;

    /**
     * Sets up.
     */
    @Before
        public void setUp() {
            pharmacist = new Pharmacist("Nikos", "P", "p3230267@aueb.gr", 65434, "d");
            Address addr = new Address("Οδός", 1, "Πόλη", "12345");
            pharmacy = new Pharmacy("Test Pharmacy", 1 , pharmacist,addr ,"1231313132");

            order = new Order(200, pharmacy);

            productA = new Product("Proda", ProductCategory.ANTIBIOTIC, 11111, 10.0);
        }

    /**
     * Test constructor and basic getters.
     */
    @Test
        public void testConstructorAndBasicGetters() {
            assertEquals(200, order.getId());
            assertEquals(pharmacy, order.getPharmacy());
            assertEquals(pharmacist, order.getPharmacist());
            assertEquals(OrderState.DRAFT, order.getState());
            assertNotNull(order.getLines());
            assertTrue(order.getLines().isEmpty());
            assertTrue(order.getCreationDate() > 0);
        }

    /**
     * Test pharmacist getter.
     */
    @Test
        public void testPharmacistGetter() {
            assertEquals(pharmacist, order.getPharmacist());
        }

    /**
     * Test add line.
     */
    @Test
        public void testAddLine() {
            OrderLine line = new OrderLine(productA, 3);
            order.addLine(line);

            List<OrderLine> lines = order.getLines();
            assertEquals(1, lines.size());
            assertEquals(line, lines.get(0));
        }

    /**
     * Test add null line does nothing.
     */
    @Test
        public void testAddNullLineDoesNothing() {
            order.addLine(null);
            assertTrue(order.getLines().isEmpty());
        }

    /**
     * Test remove line.
     */
    @Test
        public void testRemoveLine() {
            OrderLine line = new OrderLine(productA, 3);
            order.addLine(line);
            assertEquals(1, order.getLines().size());

            order.removeLine(line);
            assertTrue(order.getLines().isEmpty());
        }

    /**
     * Test remove null line.
     */
    @Test
        public void testRemoveNullLine() {
            OrderLine line = new OrderLine(productA, 3);
            order.addLine(line);
            // Should not throw or remove anything
            order.removeLine(null);
            assertEquals(1, order.getLines().size());
        }

    /**
     * Test set state and is completed.
     */
    @Test
        public void testSetStateAndIsCompleted() {
            Clerk clerk = new Clerk("prodromos", "marakis", "32323", "sda","pass");
            BatchDAO batchDAO = new BatchDAOMemory();
            InventoryLineDAO inventoryLineDAO = new InventoryLineDAOMemory();
            BackorderDAO backorderDAO = new BackorderDAOMemory();
            ReservationDAO reservationDAO = new ReservationDAOMemory();
            Inventory inventory = new Inventory(clerk,batchDAO,inventoryLineDAO, backorderDAO,reservationDAO);

            Batch batch = new Batch(12, productA, 32, 54333);
            inventory.receiveBatch(batch);

            pharmacy.submitOrder(order, inventory, 1123, true);

            assertEquals(OrderState.PENDING, order.getState());
            assertFalse(order.isCompleted());

            order.setState(OrderState.COMPLETED);
            assertEquals(OrderState.COMPLETED, order.getState());
            assertTrue(order.isCompleted());

            // Test COMPLETEDBACKORDER which also counts as completed
            order.setState(OrderState.COMPLETEDBACKORDER);
            assertTrue(order.isCompleted());

            order.setState(OrderState.CANCELED);
            assertEquals(OrderState.CANCELED, order.getState());
            assertFalse(order.isCompleted());
        }
}
