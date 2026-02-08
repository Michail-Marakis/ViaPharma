package gr.softeng.team09.androidTest.LogicViewsTests.ClerkShowStats;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsPresenter;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsView;

import static org.junit.Assert.*;

/**
 * The type Clerk show stats presenter test.
 */
public class ClerkShowStatsPresenterTest {

    private ClerkShowStatsPresenter presenter;

    private String errOrders;
    private String errRevenue;
    private String errStart;
    private String errEnd;

    private List<String> shownOrders;
    private String shownRevenue;
    private String shownTotalRevenue;
    private List<String> shownSalesBetween;

    private Inventory inventory;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        MemoryStore.initInventoryIfNeeded(
                null,
                new BatchDAOMemory(),
                new InventoryLineDAOMemory(),
                new BackorderDAOMemory(),
                new ReservationDAOMemory()
        );

        inventory = MemoryStore.getInventory();
        assertNotNull(inventory);


        clearInventoryOrders(inventory);

        presenter = new ClerkShowStatsPresenter();


        errOrders = errRevenue = errStart = errEnd = null;
        shownOrders = null;
        shownRevenue = null;
        shownTotalRevenue = null;
        shownSalesBetween = null;

        presenter.setView(new ClerkShowStatsView() {
            @Override public void showErrorOnOrdersInput(String msg) { errOrders = msg; }
            @Override public void showOrders(List<String> display) { shownOrders = display; }

            @Override public void showErrorOnRevenueInput(String msg) { errRevenue = msg; }
            @Override public void showRevenue(String msg) { shownRevenue = msg; }

            @Override public void showTotalRevenue(String msg) { shownTotalRevenue = msg; }

            @Override public void showErrorOnStartDateInput(String msg) { errStart = msg; }
            @Override public void showErrorOnEndDateInput(String msg) { errEnd = msg; }

            @Override
            public void clearOrders() {

            }

            @Override
            public void clearSalesBetween() {

            }

            @Override
            public void showOrderDetailsDialog(Order o) {

            }

            @Override public void showSalesBetween(List<String> list) { shownSalesBetween = list; }
        });
    }

    /**
     * Show orders by pharmacy covers loop totals and type and get order at.
     *
     * @throws Exception the exception
     */
    @Test
    public void showOrdersByPharmacy_coversLoopTotalsAndType_and_getOrderAt() throws Exception {

        int afm = 111;
        Pharmacy ph = new Pharmacy("PharmA", afm, null, null, "pw");

        ProductCategory cat = ProductCategory.values()[0];
        Product prod = new Product("Panadol", cat, 123, 4.5);

        Order completedOrder = new Order(1, ph, millisAthens(2025, 1, 10));
        completedOrder.addLine(new OrderLine(prod, 2));
        completedOrder.setState(OrderState.COMPLETED);

        Order completedBackorder = new Order(2, ph, millisAthens(2025, 1, 11));
        completedBackorder.addLine(new OrderLine(prod, 1));
        completedBackorder.setState(OrderState.COMPLETEDBACKORDER);

        addToInventoryOrders(inventory, completedOrder, true);
        addToInventoryOrders(inventory, completedBackorder, true);


        presenter.showOrdersByPharmacy("111");


        assertNull(errOrders);
        assertNotNull(shownOrders);
        assertEquals(2, shownOrders.size());


        boolean sawOrder = false;
        boolean sawBackorder = false;

        for (String row : shownOrders) {
            assertTrue(row.contains("ID: "));
            assertTrue(row.contains("Type: "));
            assertTrue(row.contains("Total: "));

            if (row.contains("Type: ORDER")) sawOrder = true;
            if (row.contains("Type: BACKORDER")) sawBackorder = true;
        }

        assertTrue(sawOrder);
        assertTrue(sawBackorder);

        assertNull(presenter.getOrderAt(-1));
        assertNull(presenter.getOrderAt(999));
        assertNotNull(presenter.getOrderAt(0));
    }

    /**
     * Validations and other methods cover edges.
     */
    @Test
    public void validations_and_other_methods_cover_edges() {
        presenter.showOrdersByPharmacy(null);
        assertEquals("AFM", errOrders);

        errOrders = null;
        presenter.showOrdersByPharmacy("abc");
        assertEquals("Invalid AFM", errOrders);

        presenter.showRevenueByPharmacy("");
        assertEquals("AFM", errRevenue);

        errRevenue = null;
        presenter.showRevenueByPharmacy("xx");
        assertEquals("Invalid AFM", errRevenue);

        presenter.showTotalRevenue();
        assertNotNull(shownTotalRevenue);

        presenter.showSalesBetween(null, "1/1/2025");
        assertEquals("Use d/M/yyyy", errStart);

        errStart = null;
        presenter.showSalesBetween("1/1/2025", "");
        assertEquals("Use d/M/yyyy", errEnd);

        errEnd = null;
        presenter.showSalesBetween("32/1/2025", "1/2/2025");
        assertEquals("Invalid date", errStart);

        errStart = null;
        presenter.showSalesBetween("1/1/2025", "31/2/2025");
        assertEquals("Invalid date", errEnd);

        errStart = errEnd = null;
        presenter.showSalesBetween("10/1/2025", "1/1/2025");
        assertNull(errStart);
        assertNull(errEnd);
        assertNotNull(shownSalesBetween);
    }

    // ---------- helpers ----------

    private static long millisAthens(int y, int m, int d) {
        return LocalDate.of(y, m, d)
                .atStartOfDay(ZoneId.of("Europe/Athens"))
                .toInstant()
                .toEpochMilli();
    }

    private static void clearInventoryOrders(Inventory inv) throws Exception {
        Field allOrdersF = Inventory.class.getDeclaredField("allOrders");
        allOrdersF.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Order> allOrders = (List<Order>) allOrdersF.get(inv);
        allOrders.clear();

        Field completedOrdersF = Inventory.class.getDeclaredField("completedOrders");
        completedOrdersF.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Order> completedOrders = (List<Order>) completedOrdersF.get(inv);
        completedOrders.clear();
    }

    private static void addToInventoryOrders(Inventory inv, Order order, boolean alsoCompleted) throws Exception {
        Field allOrdersF = Inventory.class.getDeclaredField("allOrders");
        allOrdersF.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Order> allOrders = (List<Order>) allOrdersF.get(inv);
        allOrders.add(order);

        if (alsoCompleted) {
            Field completedOrdersF = Inventory.class.getDeclaredField("completedOrders");
            completedOrdersF.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Order> completedOrders = (List<Order>) completedOrdersF.get(inv);
            completedOrders.add(order);
        }
    }

    /**
     * Show sales between when there are sales executes entry loop.
     *
     * @throws Exception the exception
     */
    @Test
    public void showSalesBetween_whenThereAreSales_executesEntryLoop() throws Exception {
        Inventory inv = MemoryStore.getInventory();
        clearInventoryOrders(inv);

        int afm = 111;
        Pharmacy ph = new Pharmacy("PharmA", afm, null, null, "pw");

        ProductCategory cat = ProductCategory.values()[0];
        Product prod = new Product("Panadol", cat, 123, 4.5);

        long ts = millisAthens(2025, 1, 1);
        Order completed = new Order(10, ph, ts);
        completed.addLine(new OrderLine(prod, 3));
        completed.setState(OrderState.COMPLETED);

        addToInventoryOrders(inv, completed, true);

        shownSalesBetween = null;
        presenter.showSalesBetween("1/1/2025", "1/1/2025");

        assertNotNull(shownSalesBetween);
        assertFalse("Expected non-empty sales list", shownSalesBetween.isEmpty());
    }

    /**
     * Show revenue by pharmacy when there is completed order shows non zero revenue.
     *
     * @throws Exception the exception
     */
    @Test
    public void showRevenueByPharmacy_whenThereIsCompletedOrder_showsNonZeroRevenue() throws Exception {
        Inventory inv = MemoryStore.getInventory();
        clearInventoryOrders(inv);

        int afm = 111;
        Pharmacy ph = new Pharmacy("PharmA", afm, null, null, "pw");

        ProductCategory cat = ProductCategory.values()[0];
        Product prod = new Product("Panadol", cat, 123, 4.5);

        Order completed = new Order(20, ph, millisAthens(2025, 1, 2));
        completed.addLine(new OrderLine(prod, 2));
        completed.setState(OrderState.COMPLETED);

        addToInventoryOrders(inv, completed, true);

        shownRevenue = null;
        errRevenue = null;

        presenter.showRevenueByPharmacy("111");

        assertNull(errRevenue);
        assertNotNull(shownRevenue);
        assertTrue(shownRevenue.startsWith("Revenue generated: "));
        assertTrue("Expected revenue to include 11.16", shownRevenue.contains("11.16"));
        assertTrue(shownRevenue.endsWith(" €"));
    }
}
