package gr.softeng.team09;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The type Inventory add or increase line test.
 */
public class InventoryAddOrIncreaseLineTest {

    /**
     * Test add or increase line increases quantity for existing product.
     */
    @Test
    public void testAddOrIncreaseLineIncreasesQuantityForExistingProduct() {

        Clerk clerk = new Clerk("Alice", "34ewsd", "sad", "dsd", "dsd");
        Inventory inv = new Inventory(clerk,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory());

        Pharmacist ph = new Pharmacist("John", "pwd", "mail", 1, "id");
        Pharmacy pharmacy = new Pharmacy("Pharmacy1", 123456789, ph, new Address("Street", 1, "City", "12345"), "pass");

        Order order = new Order(1, pharmacy);
        Product product = new Product("P1", ProductCategory.OTHER, 100, 120);


        OrderLine ol = new OrderLine(product, 2);
        order.addLine(ol);


        inv.addOrIncreaseLine(order, product, 3);


        assertEquals(1, order.getLines().size());
        assertEquals(5, order.getLines().get(0).getQuantity());
    }

    /**
     * Test add or increase line adds new line if product not present.
     */
    @Test
    public void testAddOrIncreaseLineAddsNewLineIfProductNotPresent() {
        Clerk clerk = new Clerk("Alice", "34ewsd", "sad", "dsd", "dsd");
        Inventory inv = new Inventory(clerk,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory());

        Pharmacist ph = new Pharmacist("John", "pwd", "mail", 1, "id");
        Pharmacy pharmacy = new Pharmacy("Pharmacy1", 123456789, ph, new Address("Street", 1, "City", "12345"), "pass");

        Order order = new Order(1, pharmacy);
        Product product = new Product("P1", ProductCategory.OTHER, 100, 120);


        inv.addOrIncreaseLine(order, product, 4);


        assertEquals(1, order.getLines().size());
        assertEquals(4, order.getLines().get(0).getQuantity());
    }
}
