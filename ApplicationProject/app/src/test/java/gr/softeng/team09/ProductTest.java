package gr.softeng.team09;

import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Product test.
 */
public class ProductTest {

    private Product product;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        product = new Product("Proda", ProductCategory.ANTIBIOTIC, 12345, 10.0);
    }

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals("Proda", product.getName());
        assertEquals(ProductCategory.ANTIBIOTIC, product.getCategory());
        assertEquals(12345, product.getEofyCode());
        assertEquals(10.0, product.getPrice(), 0.0001);
    }

    /**
     * Test setters.
     */
    @Test
    public void testSetters() {
        product.setCategory(ProductCategory.ANALGESIC);
        product.setEofyCode(99999);
        product.setPrice(15.5);

        assertEquals(ProductCategory.ANALGESIC, product.getCategory());
        assertEquals(99999, product.getEofyCode());
        assertEquals(15.5, product.getPrice(), 0.0001);
    }

    /**
     * Test get price with vat.
     */
    @Test
    public void testGetPriceWithVAT() {
        //10 * 1.24 = 12.4
        assertEquals(12.4, product.getPriceWithVAT(), 0.0001);
    }

    /**
     * Test equals and hash code same eofy code.
     */
    @Test
    public void testEqualsAndHashCodeSameEofyCode() {
        Product p2 = new Product("prodb", ProductCategory.ANALGESIC,
                12345,   20.0);

        assertEquals(product, p2);
        assertEquals(product.hashCode(), p2.hashCode());
    }

    /**
     * Test equals and hash code different eofy code.
     */
    @Test
    public void testEqualsAndHashCodeDifferentEofyCode() {
        Product p2 = new Product("Proda", ProductCategory.ANTIBIOTIC, 54321, 10.0);

        assertNotEquals(product, p2);
        assertNotEquals(p2, product);
    }

    /**
     * Test equals with itself and null and other type.
     */
    @Test
    public void testEqualsWithItselfAndNullAndOtherType() {
        assertTrue(product.equals(product));
        assertFalse(product.equals(null));
        assertFalse(product.equals("string"));
    }
}