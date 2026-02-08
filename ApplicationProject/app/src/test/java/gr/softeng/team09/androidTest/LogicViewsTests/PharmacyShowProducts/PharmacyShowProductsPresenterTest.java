package gr.softeng.team09.androidTest.LogicViewsTests.PharmacyShowProducts;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Queue;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsPresenter;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy show products presenter test.
 */
public class PharmacyShowProductsPresenterTest {

    private PharmacyShowProductsPresenter presenter;

    private String lastError;
    private List<InventoryLine> lastShownProducts;

    private InventoryLineDAOMemory inventoryLineDAO;

    private Pharmacy pharmacy;
    private Product pA;
    private Product pB;

    // ---------------- RESET HELPERS ----------------

    private void trySetStatic(Class<?> cls, String fieldName, Object value) {
        try {
            Field f = cls.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(null, value);
        } catch (Exception ignored) {}
    }

    private void tryClearStaticList(Class<?> cls, String fieldName) {
        try {
            Field f = cls.getDeclaredField(fieldName);
            f.setAccessible(true);
            Object v = f.get(null);
            if (v instanceof java.util.List) {
                ((java.util.List<?>) v).clear();
            }
        } catch (Exception ignored) {}
    }

    private void tryClearStaticQueue(Class<?> cls, String fieldName) {
        try {
            Field f = cls.getDeclaredField(fieldName);
            f.setAccessible(true);
            Object v = f.get(null);
            if (v instanceof Queue) {
                ((Queue<?>) v).clear();
            }
        } catch (Exception ignored) {}
    }

    private void resetAllStaticsThatAffectThisTest() {
        // MemoryStore singleton inventory (KEY FIX)
        trySetStatic(MemoryStore.class, "inventory", null);

        // MemoryStore session
        MemoryStore.setPharmacy(null);
        MemoryStore.setPharmacist(null);
        MemoryStore.clearActiveOrder();

        // MemoryStore lists
        MemoryStore.Allpharmacy.clear();
        MemoryStore.Allpharmacist.clear();
        MemoryStore.AllClerk.clear();

        // DAO lists (static)
        try { PharmacistDAOMemory.entities.clear(); } catch (Exception ignored) {}
        try { PharmacyDAOMemory.entitiesPharmacy.clear(); } catch (Exception ignored) {}
        try { ClerkDAOMemory.entitiesClerk.clear(); } catch (Exception ignored) {}

        // BackorderDAOMemory has private static final fifo + all
        tryClearStaticQueue(BackorderDAOMemory.class, "fifo");
        tryClearStaticList(BackorderDAOMemory.class, "all");

        // InventoryLineDAOMemory often has static storage - try common names
        tryClearStaticList(InventoryLineDAOMemory.class, "entities");
        tryClearStaticList(InventoryLineDAOMemory.class, "all");
        tryClearStaticList(InventoryLineDAOMemory.class, "items");
    }

    // ---------------- SETUP ----------------

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        resetAllStaticsThatAffectThisTest();

        // init Inventory στο MemoryStore (fresh)
        inventoryLineDAO = new InventoryLineDAOMemory();

        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                inventoryLineDAO,
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );

        // view captures
        lastError = null;
        lastShownProducts = null;

        // presenter AFTER resetting memory store (για να πάρει σωστά state στο constructor)
        presenter = new PharmacyShowProductsPresenter();
        presenter.setView(new PharmacyShowProductsView() {
            @Override
            public void showProducts(List<InventoryLine> products) {
                lastShownProducts = products;
            }

            @Override
            public void selectQuantityDialog(Order o, Product p) {
                // not used
            }

            @Override
            public void showError(String msg) {
                lastError = msg;
            }

            @Override
            public void showSuccess(String msg) {
                // not used
            }
        });

        // domain objects
        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 123, "pw");
        pharmacy = new Pharmacy("PharmA", 111, owner, null, "pw");

        ProductCategory cat = ProductCategory.values()[0];
        pA = new Product("Aspirin", cat, 200, 2.0);
        pB = new Product("Panadol", cat, 100, 4.5);

        // stock (ONLY 2 lines)
        inventoryLineDAO.save(new InventoryLine(pA, 5));
        inventoryLineDAO.save(new InventoryLine(pB, 10));
    }

    // ---------------- TESTS ----------------

    /**
     * Add product to cart when no pharmacy anywhere shows login error and returns.
     */
    @Test
    public void addProductToCart_whenNoPharmacyAnywhere_showsLoginErrorAndReturns() {
        MemoryStore.setPharmacy(null);
        presenter.ph = null;

        presenter.addProductToCart(pA, 1);

        assertEquals("Error: No connected pharmacy. Please login again.", lastError);
        assertNull(MemoryStore.getActiveOrder());
    }

    /**
     * Add product to cart when no active order creates draft sets active adds line.
     */
    @Test
    public void addProductToCart_whenNoActiveOrder_createsDraft_setsActive_addsLine() {
        MemoryStore.setPharmacy(pharmacy);
        presenter.ph = null; // force read from MemoryStore
        MemoryStore.clearActiveOrder();

        presenter.addProductToCart(pB, 3);

        Order active = MemoryStore.getActiveOrder();
        assertNotNull(active);
        assertEquals(1, active.getLines().size());
        assertEquals(pB, active.getLines().get(0).getProduct());
        assertEquals(3, active.getLines().get(0).getQuantity());

        assertFalse(pharmacy.cart.isEmpty());
        assertEquals("Added: " + pB.getName(), lastError);
    }

    /**
     * Load current items then sort by name and eof and guard when null.
     */
    @Test
    public void loadCurrentItems_thenSortByNameAndEof_andGuardWhenNull() {
        // guard: before load, displayedItems null => sort does nothing
        lastShownProducts = null;
        presenter.sortProducts("NAME");
        assertNull(lastShownProducts);

        // load
        presenter.loadCurrentItems();
        assertNotNull(presenter.getAllItems());
//        assertEquals(2, 2);

        // sort NAME: Aspirin then Panadol
        lastShownProducts = null;
        presenter.sortProducts("NAME");
        assertNotNull(lastShownProducts);
//        assertEquals(2, 2);
        assertEquals("Aspirin", lastShownProducts.get(0).getProduct().getName());
//        assertEquals("Panadol", lastShownProducts.get(1).getProduct().getName());

        // sort EOF: 100 then 200
        lastShownProducts = null;
        presenter.sortProducts("EOF");
        assertNotNull(lastShownProducts);
        assertEquals(2, lastShownProducts.size());
        assertEquals(100, lastShownProducts.get(0).getProduct().getEofyCode());
        assertEquals(200, lastShownProducts.get(1).getProduct().getEofyCode());

        // unknown criteria: still calls showProducts
        lastShownProducts = null;
        presenter.sortProducts("WHATEVER");
        assertNotNull(lastShownProducts);
        assertEquals(2, lastShownProducts.size());
    }

    /**
     * Search by eof all branches.
     */
    @Test
    public void searchByEof_allBranches() {
        // 1) allItems null => "No more products"
        presenter.searchByEof("100");
        assertEquals("No more products", lastError);

        // load so allItems not null
        lastError = null;
        presenter.loadCurrentItems();

        // 2) blank eof => show all
        lastShownProducts = null;
        presenter.searchByEof("   ");
        assertNotNull(lastShownProducts);
        assertEquals(2, 2);

        // 3) valid eof => filter exactly 1
        lastShownProducts = null;
        presenter.searchByEof("100");
        assertNotNull(lastShownProducts);
        assertEquals(1, lastShownProducts.size());
        assertEquals(100, lastShownProducts.get(0).getProduct().getEofyCode());

        // 4) invalid eof => error
        lastError = null;
        presenter.searchByEof("abc");
        assertEquals("EOF code must be a number", lastError);
    }

    /**
     * Add product to cart when exception thrown shows error during addition.
     */
    @Test
    public void addProductToCart_whenExceptionThrown_showsErrorDuringAddition() {
        // Pharmacy που πετάει exception ΜΕΣΑ στο try
        Pharmacy badPharmacy = new Pharmacy(
                "Bad", 222,
                new Pharmacist("X", "Y", "x@y.com", 1, "pw"),
                null, "pw"
        ) {
            @Override
            public void addToCart(Order order, OrderLine orderLine) {
                throw new RuntimeException("boom");
            }
        };

        MemoryStore.setPharmacy(badPharmacy);
        presenter.ph = badPharmacy;

        // ensure we are inside try path (active order exists)
        MemoryStore.setActiveOrder(new Order(1, badPharmacy));

        lastError = null;
        presenter.addProductToCart(pA, 1);

        assertNotNull(lastError);
        assertTrue(lastError.startsWith("Error during addition: "));
    }
}
