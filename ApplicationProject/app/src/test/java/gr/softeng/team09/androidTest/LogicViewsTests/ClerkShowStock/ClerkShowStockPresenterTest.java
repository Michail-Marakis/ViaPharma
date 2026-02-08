package gr.softeng.team09.androidTest.LogicViewsTests.ClerkShowStock;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;

import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockPresenter;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockView;

import static org.junit.Assert.*;

/**
 * The type Clerk show stock presenter test.
 */
public class ClerkShowStockPresenterTest {

    private ClerkShowStockPresenter presenter;

    private String lastError;
    private List<String> lastShownOrders;

    private Inventory inventory;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );

        inventory = MemoryStore.getInventory();
        assertNotNull(inventory);

        presenter = new ClerkShowStockPresenter();

        lastError = null;
        lastShownOrders = null;

        presenter.setView(new ClerkShowStockView() {
            @Override
            public void showError(String msg) {
                lastError = msg;
            }

            @Override
            public void loadInformationToList() {

            }

            @Override
            public String buildRow(InventoryLine i) {
                return "";
            }

            @Override
            public void clearOrders() {

            }

            @Override
            public void showOrders(List<String> orders) {
                lastShownOrders = orders;
            }
        });
    }

    /**
     * Sets eof to search parse id branches.
     */
    @Test
    public void setEofToSearch_parseIdBranches() {
        //null -> EOF code required
        lastError = null;
        presenter.setEofToSearch(null);
        assertEquals("EOF code required", lastError);

        //empty -> EOF code required
        lastError = null;
        presenter.setEofToSearch("   ");
        assertEquals("EOF code required", lastError);

        //invalid number -> Invalid EOF code
        lastError = null;
        presenter.setEofToSearch("abc");
        assertEquals("Invalid EOF code", lastError);

        //valid -> no error
        lastError = null;
        presenter.setEofToSearch("123");
        assertNull(lastError);
    }

    /**
     * Load full stock updates stock list.
     */
    @Test
    public void loadFullStock_updatesStockList() {
        //arrange: βάζουμε 2 inventory lines στο DAO
        ProductCategory cat = ProductCategory.values()[0];
        Product p1 = new Product("Panadol", cat, 111, 4.5);
        Product p2 = new Product("Aspirin", cat, 222, 3.0);

        InventoryLineDAOMemory invLineDAO = getInventoryLineDAOFromMemoryStore();

        invLineDAO.save(new InventoryLine(p1, 10));
        invLineDAO.save(new InventoryLine(p2, 5));

        presenter.loadFullStock();

        List<InventoryLine> stock = presenter.getStock();
        assertNotNull(stock);
        assertTrue(stock.size() >= 2);
    }

    /**
     * Load eof stock when found shows one formatted entry.
     */
    @Test
    public void loadEofStock_whenFound_showsOneFormattedEntry() {
        //arrange stock
        ProductCategory cat = ProductCategory.values()[0];
        Product p1 = new Product("Panadol", cat, 123, 4.5);
        Product p2 = new Product("Aspirin", cat, 999, 3.0);

        InventoryLineDAOMemory invLineDAO = getInventoryLineDAOFromMemoryStore();
        invLineDAO.save(new InventoryLine(p2, 1));
        invLineDAO.save(new InventoryLine(p1, 7)); // αυτό θέλουμε να βρεθεί

        presenter.setEofToSearch("123");
        lastShownOrders = null;
        presenter.loadEofStock();

        assertNotNull(lastShownOrders);
        assertEquals(1, lastShownOrders.size());

        String info = lastShownOrders.get(0);
        assertTrue(info.contains("Name: Panadol"));
        assertTrue(info.contains("EOF code: 123"));
        assertTrue(info.contains("Quantity: 7"));
        assertTrue(info.contains("Price without VAT: 4.5 €"));
    }

    /**
     * Load eof stock when not found shows empty list.
     */
    @Test
    public void loadEofStock_whenNotFound_showsEmptyList() {
        //arrange stock χωρίς το eof 555
        ProductCategory cat = ProductCategory.values()[0];
        Product p = new Product("Panadol", cat, 123, 4.5);

        InventoryLineDAOMemory invLineDAO = getInventoryLineDAOFromMemoryStore();
        invLineDAO.save(new InventoryLine(p, 7));

        presenter.setEofToSearch("555");
        lastShownOrders = null;
        presenter.loadEofStock();

        assertNotNull(lastShownOrders);
        assertTrue(lastShownOrders.isEmpty());
    }

    // -------- helper --------

    private InventoryLineDAOMemory getInventoryLineDAOFromMemoryStore() {

        try {
            java.lang.reflect.Field f = Inventory.class.getDeclaredField("inventoryLineDAO");
            f.setAccessible(true);
            return (InventoryLineDAOMemory) f.get(inventory);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access inventoryLineDAO for test", e);
        }
    }
}
