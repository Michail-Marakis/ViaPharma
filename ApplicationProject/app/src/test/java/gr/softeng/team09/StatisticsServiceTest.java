package gr.softeng.team09;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.domain.StatisticsService;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * The type Statistics service test.
 */
public class StatisticsServiceTest {

    private Pharmacy pharmacy1;
    private Pharmacy pharmacy2;
    private Product productA;
    private Product productB;

    private List<Order> completedOrders;
    private List<Order> allOrders;
    private StatisticsService stats;
    private Inventory inventory;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        Pharmacist ph1 = new Pharmacist("Nikos", "P", "p3230267@aueb.gr", 42343, "dsd");
        Pharmacist ph2 = new Pharmacist("Giorgos", "K", "p3230267@aueb.gr", 8765, "dsd");
        Address addr1 = new Address("Οδός1", 1, "Πόλη1", "11111");
        Address addr2 = new Address("Οδός2", 2, "Πόλη2", "22222");

        pharmacy1 = new Pharmacy("Ph1", 123456789, ph1, addr1,"pass");
        pharmacy2 = new Pharmacy("Ph2", 987654321, ph2, addr2,"passs");

        productA = new Product("Proda", ProductCategory.ANTIBIOTIC, 11111, 10.0);
        productB = new Product("Prodb", ProductCategory.ANALGESIC, 22222, 5.0);

        completedOrders = new ArrayList<Order>();
        allOrders = new ArrayList<Order>();
        stats = new StatisticsService(completedOrders, allOrders);

        Clerk clerk = new Clerk("prodromos", "marakis", "32323", "sda","pass");
        BatchDAO batchDAO = new BatchDAOMemory();
        InventoryLineDAO inventoryLineDAO = new InventoryLineDAOMemory();
        BackorderDAO backorderDAO = new BackorderDAOMemory();
        ReservationDAO reservationDAO = new ReservationDAOMemory();

        inventory = new Inventory(clerk, batchDAO, inventoryLineDAO, backorderDAO, reservationDAO);
    }

    /**
     * Test constructor null lists throws.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullListsThrows() {
        new StatisticsService(null, new ArrayList<Order>());
    }

    /**
     * Test constructor null all orders throws.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAllOrdersThrows() {
        new StatisticsService(new ArrayList<Order>(), null);
    }

    /**
     * Test get orders by pharmacy sorted by date.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetOrdersByPharmacySortedByDate() throws Exception {
        Order o1 = new Order(1, pharmacy1);
        Thread.sleep(5);
        Order o2 = new Order(2, pharmacy1);

        o1.setState(OrderState.COMPLETED);
        o2.setState(OrderState.COMPLETED);

        completedOrders.add(o2);
        completedOrders.add(o1);

        List<Order> result = stats.getOrdersByPharmacy(pharmacy1.getAfm());
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    /**
     * Test get revenue by pharmacy.
     */
    @Test
    public void testGetRevenueByPharmacy() {
        Order o1 = new Order(1, pharmacy1);
        Order o2 = new Order(2, pharmacy1);
        Order o3 = new Order(3, pharmacy2);

        o1.setState(OrderState.COMPLETED);
        o2.setState(OrderState.COMPLETED);
        o3.setState(OrderState.COMPLETED);

        o1.addLine(new OrderLine(productA, 2));
        o2.addLine(new OrderLine(productB, 1));
        o3.addLine(new OrderLine(productA, 5));

        completedOrders.add(o1);
        completedOrders.add(o2);
        completedOrders.add(o3);

        double revenuePh1 = stats.getRevenueByPharmacy(pharmacy1.getAfm());
        double expected = 2 * productA.getPriceWithVAT()
                + 1 * productB.getPriceWithVAT();

        assertEquals(expected, revenuePh1, 0.0001);
    }


    /**
     * Test get product sales between filters correctly.
     */
    @Test
    public void testGetProductSalesBetweenFiltersCorrectly() {
        long now = System.currentTimeMillis();
        long start = now - 10000;
        long end   = now + 10000;

        Order o1 = new Order(1, pharmacy1, now);
        o1.setState(OrderState.PENDING);
        o1.addLine(new OrderLine(productA, 10));


        Order o2 = new Order(2, pharmacy1, start - 1000);
        o2.setState(OrderState.COMPLETED);
        o2.addLine(new OrderLine(productA, 20));


        Order o3 = new Order(3, pharmacy1, end + 1000);
        o3.setState(OrderState.COMPLETED);
        o3.addLine(new OrderLine(productA, 30));


        Order o4 = new Order(4, pharmacy1, now);
        o4.setState(OrderState.COMPLETED);
        o4.addLine(new OrderLine(productA, 5));


        Order o5 = new Order(5, pharmacy1, now);
        o5.setState(OrderState.COMPLETEDBACKORDER);
        o5.addLine(new OrderLine(productB, 3));

        completedOrders.add(o1);
        completedOrders.add(o2);
        completedOrders.add(o3);
        completedOrders.add(o4);
        completedOrders.add(o5);

        Map<Product, Integer> sales = stats.getProductSalesBetween(start, end);

        assertFalse(sales.isEmpty());
        assertEquals(Integer.valueOf(5), sales.get(productA));
        assertEquals(Integer.valueOf(3), sales.get(productB));
    }

    /**
     * Test get total revenue.
     */
    @Test
    public void testGetTotalRevenue() {
        Order o1 = new Order(1, pharmacy1);
        Order o2 = new Order(2, pharmacy1);
        Order o3 = new Order(3, pharmacy1);

        o1.setState(OrderState.COMPLETED);
        o2.setState(OrderState.COMPLETED);
        o3.setState(OrderState.PENDING); // Ignored

        o1.addLine(new OrderLine(productA, 1));
        o2.addLine(new OrderLine(productB, 2));
        o3.addLine(new OrderLine(productA, 100));

        completedOrders.add(o1);
        completedOrders.add(o2);
        completedOrders.add(o3);

        double expected = 1 * productA.getPriceWithVAT()
                + 2 * productB.getPriceWithVAT();

        double total = stats.getTotalRevenue();
        assertEquals(expected, total, 0.0001);
    }

    /**
     * Test null order in list is ignored.
     */
    @Test
    public void testNullOrderInListIsIgnored() {
        completedOrders.add(null);

        assertEquals(0.0, stats.getTotalRevenue(), 0.0001);
        assertTrue(stats.getProductSalesBetween(0, Long.MAX_VALUE).isEmpty());
        assertTrue(stats.getOrdersByPharmacy(pharmacy1.getAfm()).isEmpty());
        assertEquals(0.0, stats.getRevenueByPharmacy(pharmacy1.getAfm()), 0.0001);
    }
}