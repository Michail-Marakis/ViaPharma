package gr.softeng.team09;

import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Order line test.
 */
public class OrderLineTest {

    private Product product;
    private OrderLine orderLine;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        product = new Product("Proda", ProductCategory.ANTIBIOTIC, 12345, 10.0);
        orderLine = new OrderLine(product, 5);
    }

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals(product, orderLine.getProduct());
        assertEquals(5, orderLine.getQuantity());
    }

    /**
     * Test set product.
     */
    @Test
    public void testSetProduct() {
        Product newProduct = new Product("Prodb", ProductCategory.ANALGESIC, 67890, 7.5);
        orderLine.setProduct(newProduct);

        assertEquals(newProduct, orderLine.getProduct());
    }

    /**
     * Test set quantity.
     */
    @Test
    public void testSetQuantity() {
        orderLine.setQuantity(10);
        assertEquals(10, orderLine.getQuantity());
    }

    /**
     * Test to string.
     */
    @Test
    public void testToString() {
        double priceWithVat = product.getPriceWithVAT();
        double totalValue = priceWithVat * 5;

        String formattedPrice = String.format("%.2f", totalValue);

        String expected = "Proda x5 (" + formattedPrice + "€)";

        assertEquals(expected, orderLine.toString());
    }
}