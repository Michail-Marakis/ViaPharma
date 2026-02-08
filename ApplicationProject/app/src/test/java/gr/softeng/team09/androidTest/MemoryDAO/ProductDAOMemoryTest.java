package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.ProductDAOMemory;

import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Product dao memory test.
 */
public class ProductDAOMemoryTest {

    private ProductDAOMemory dao;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dao = new ProductDAOMemory();
        ProductDAOMemory.entities.clear();
    }

    /**
     * Save adds product.
     */
    @Test
    public void save_AddsProduct() {
        Product p = new Product("Aspirin", ProductCategory.OTHER, 555, 1.5);
        dao.save(p);

        assertEquals(1, dao.findAll().size());
        assertEquals(p, dao.findByName("Aspirin"));
    }

    /**
     * Save ignores duplicate object.
     */
    @Test
    public void save_IgnoresDuplicateObject() {
        Product p = new Product("Aspirin", ProductCategory.OTHER, 555, 1.5);
        dao.save(p);
        dao.save(p);

        assertEquals("Should not add duplicate object reference", 1, dao.findAll().size());
    }

    /**
     * Delete removes product.
     */
    @Test
    public void delete_RemovesProduct() {
        Product p = new Product("Depon", ProductCategory.OTHER, 1001, 2.5);
        dao.save(p);

        dao.delete(p);
        assertEquals(0, dao.findAll().size());
    }

    /**
     * Find by eofy code returns correct product.
     */
    @Test
    public void findByEofyCode_ReturnsCorrectProduct() {
        Product p = new Product("TestDrug", ProductCategory.OTHER, 9999, 10.0);
        dao.save(p);

        assertEquals(p, dao.findByEofyCode(9999));
        assertNull(dao.findByEofyCode(1234));
    }

    /**
     * Find by name returns correct product.
     */
    @Test
    public void findByName_ReturnsCorrectProduct() {
        Product p = new Product("Panadol", ProductCategory.OTHER, 1001, 2.0);
        dao.save(p);

        assertEquals(p, dao.findByName("Panadol"));
        assertNull(dao.findByName("Unknown"));
    }
}
