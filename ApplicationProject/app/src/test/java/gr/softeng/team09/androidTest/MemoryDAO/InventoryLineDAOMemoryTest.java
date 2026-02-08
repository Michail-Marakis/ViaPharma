package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;

import java.lang.reflect.Field;
import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Inventory line dao memory test.
 */
public class InventoryLineDAOMemoryTest {

        private InventoryLineDAOMemory dao;
        private Product p1;
        private Product p2;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
        public void setUp() throws Exception {
            dao = new InventoryLineDAOMemory();

            Field field = InventoryLineDAOMemory.class.getDeclaredField("lines");
            field.setAccessible(true);
            ((List<?>) field.get(null)).clear();

            p1 = new Product("Depon", ProductCategory.OTHER, 1001, 2.50);
            p2 = new Product("Ponstan", ProductCategory.OTHER, 1002, 3.50);
        }

    /**
     * Τεστ για την save (έλεγχος ότι δουλεύει, για να στηρίξουμε τα επόμενα tests)
     */
    @Test
        public void save_AddsNewOrUpdatesExisting() {

            InventoryLine line1 = new InventoryLine(p1, 10);
            dao.save(line1);

            assertEquals("Should have 1 line", 1, dao.findAll().size());
            assertEquals("Quantity should be 10", 10, dao.findByProduct(p1).getQuantity());


            InventoryLine line1Update = new InventoryLine(p1, 50);
            dao.save(line1Update);

            assertEquals("Should still have 1 line (update)", 1, dao.findAll().size());
            assertEquals("Quantity should now be 50", 50, dao.findByProduct(p1).getQuantity());
        }

    /**
     * Τεστ για την findByProduct
     */
    @Test
        public void findByProduct_ReturnsCorrectResults() {

            InventoryLine line1 = new InventoryLine(p1, 10);
            InventoryLine line2 = new InventoryLine(p2, 20);
            dao.save(line1);
            dao.save(line2);

            InventoryLine found1 = dao.findByProduct(p1);
            assertNotNull("Should find product 1", found1);
            assertEquals("Should match product 1", p1, found1.getProduct());

            InventoryLine found2 = dao.findByProduct(p2);
            assertNotNull("Should find product 2", found2);
            assertEquals("Should match product 2", p2, found2.getProduct());


            Product unknownProduct = new Product("Unknown", ProductCategory.OTHER, 9999, 10.0);
            InventoryLine notFound = dao.findByProduct(unknownProduct);
            assertNull("Should return null for unknown product", notFound);

            assertNull("Should return null when searching for null", dao.findByProduct(null));
        }

    /**
     * Τεστ για την findAll
     */
    @Test
        public void findAll_ReturnsAllLines() {

            List<InventoryLine> emptyList = dao.findAll();
            assertNotNull(emptyList);
            assertEquals("Should be empty initially", 0, emptyList.size());


            dao.save(new InventoryLine(p1, 10));
            dao.save(new InventoryLine(p2, 20));


            List<InventoryLine> allLines = dao.findAll();
            assertEquals("Should return 2 lines", 2, allLines.size());


            boolean foundP1 = false;
            boolean foundP2 = false;

            for(InventoryLine line : allLines) {
                if (line.getProduct().equals(p1)) foundP1 = true;
                if (line.getProduct().equals(p2)) foundP2 = true;
            }

            assertTrue("List should contain product 1", foundP1);
            assertTrue("List should contain product 2", foundP2);
        }

    /**
     * Τεστ για delete (το είχαμε, αλλά καλό είναι να υπάρχει)
     */
    @Test
        public void deleteByProduct_RemovesLine() {
            dao.save(new InventoryLine(p1, 10));
            assertEquals(1, dao.findAll().size());

            dao.deleteByProduct(p1);
            assertEquals("List should be empty after delete", 0, dao.findAll().size());
            assertNull("Product should not be found", dao.findByProduct(p1));
        }
}
