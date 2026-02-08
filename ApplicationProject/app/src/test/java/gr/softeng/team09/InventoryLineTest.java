package gr.softeng.team09;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;

/**
 * The type Inventory line test.
 */
public class InventoryLineTest {

    private Product product;
    private InventoryLine inventoryLine;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        product = new Product("ProdA", ProductCategory.OTHER, 124323, 23.9);
        inventoryLine = new InventoryLine(product, 323);
    }

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters(){
        assertEquals(product, inventoryLine.getProduct());
        assertEquals(323, inventoryLine.getQuantity());
    }

    /**
     * Test constructor with negative quantity to ensure Math.max(0, quantity) works.
     */
    @Test
    public void testConstructorWithNegativeQuantity() {
        InventoryLine negativeLine = new InventoryLine(product, -100);
        assertEquals(0, negativeLine.getQuantity());
    }

    /**
     * Test set quantity.
     */
    @Test
    public void testSetQuantity(){
        inventoryLine.setQuantity(3);
        assertEquals(3, inventoryLine.getQuantity());
    }

    /**
     * Test set quantity negative.
     */
    @Test
    public void testSetQuantityNegative(){
        inventoryLine.setQuantity(-50);
        assertEquals(0, inventoryLine.getQuantity());
    }

    /**
     * Test increase decrease normal cases.
     */
    @Test
    public void testIncreaseDecreaseNormalCases(){
        inventoryLine.setQuantity(43);
        inventoryLine.decrease(40);
        assertEquals(3, inventoryLine.getQuantity());

        inventoryLine.increase(2);
        assertEquals(5, inventoryLine.getQuantity());
    }

    /**
     * Tests the 'else' branches in increase/decrease logic
     * where a negative delta is passed, triggering the opposite method.
     */
    @Test
    public void testIncreaseDecreaseNegativeDeltaRecursion(){
        inventoryLine.setQuantity(50);

        inventoryLine.increase(-5);
        assertEquals(45, inventoryLine.getQuantity());

        inventoryLine.decrease(-5);
        assertEquals(50, inventoryLine.getQuantity());
    }

    /**
     * Test to string.
     */
    @Test
    public void testToString() {
        String expected = "ProdA" + "\nPrice: " + product.getPriceWithVAT() + "\nEOF code:" + 124323;
        String actual = inventoryLine.toString();

        assertEquals(expected, actual);
        assertTrue(actual.contains("ProdA"));
        assertTrue(actual.contains("124323"));
    }
}