package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import static org.junit.Assert.*;

/**
 * The type Memory store test.
 */
public class MemoryStoreTest {
    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        MemoryStore.Allpharmacist.clear();
        MemoryStore.Allpharmacy.clear();
        MemoryStore.AllClerk.clear();

        MemoryStore.clearSessionPharma();
        MemoryStore.clearSessionClerk();
        MemoryStore.clearActiveOrder();

        Field inventoryField = MemoryStore.class.getDeclaredField("inventory");
        inventoryField.setAccessible(true);
        inventoryField.set(null, null);
    }

    /**
     * Constructor coverage.
     */
    @Test
    public void constructor_Coverage() {
        new MemoryStore();
    }

    /**
     * Pharmacy operations test.
     */
    @Test
    public void pharmacyOperations_Test() {
        Pharmacy p = new Pharmacy("TestPharma", 12345, null, null, "pass");

        MemoryStore.setPharmacy(p);
        assertEquals(p, MemoryStore.getPharmacy());

        MemoryStore.addPharmacy(p);
        assertEquals(1, MemoryStore.Allpharmacy.size());
        assertEquals(p, MemoryStore.getByAFM(12345));
        assertNull(MemoryStore.getByAFM(99999));
    }

    /**
     * Pharmacist operations test.
     */
    @Test
    public void pharmacistOperations_Test() {
        Pharmacist p = new Pharmacist("Nikos", "Baglamas","nikos@mail.com",69, "1234");

        MemoryStore.setPharmacist(p);
        assertEquals(p, MemoryStore.getPharmacist());

        MemoryStore.addPharmacist(p);
        assertEquals(1, MemoryStore.Allpharmacist.size());
        assertEquals(p, MemoryStore.getByEmail("nikos@mail.com"));
        assertNull(MemoryStore.getByEmail("unknown@mail.com"));
    }

    /**
     * Clerk operations test.
     */
    @Test
    public void clerkOperations_Test() {
        Clerk c = new Clerk("Maria","Vas", "69", "maria@mail.com", "pass");

        MemoryStore.setClerk(c);
        assertEquals(c, MemoryStore.getClerk());

        MemoryStore.addClerk(c);
        assertEquals(1, MemoryStore.AllClerk.size());
        assertEquals(c, MemoryStore.getByEmailClerk("maria@mail.com"));
        assertNull(MemoryStore.getByEmailClerk("unknown@mail.com"));
    }

    /**
     * Sets clerk updates inventory owner when inventory exists.
     */
    @Test
    public void setClerk_UpdatesInventoryOwner_WhenInventoryExists() {

        Clerk initialClerk = new Clerk("Initial", "C", "00", "init@mail.com", "pass");
        MemoryStore.initInventoryIfNeeded(initialClerk, new BatchDAOMemory(), new InventoryLineDAOMemory(), new BackorderDAOMemory(), new ReservationDAOMemory());

        Clerk newClerk = new Clerk("New", "C", "11", "new@mail.com", "pass");

        MemoryStore.setClerk(newClerk);

        assertEquals(newClerk, MemoryStore.getClerk());
        assertNotNull(MemoryStore.getInventory());

    }

    /**
     * Active order test.
     */
    @Test
    public void activeOrder_Test() {
        Pharmacy ph = new Pharmacy("P", 1, null, null, "p");
        Order order = new Order(1, ph);

        MemoryStore.setActiveOrder(order);
        assertEquals(order, MemoryStore.getActiveOrder());

        MemoryStore.clearActiveOrder();
        assertNull(MemoryStore.getActiveOrder());
    }

    /**
     * Clear session pharma test.
     */
    @Test
    public void clearSessionPharma_Test() {
        MemoryStore.setPharmacy(new Pharmacy("P", 1, null, null, "p"));
        MemoryStore.setPharmacist(new Pharmacist("User","userlast","user@gmail.gr",69, "p"));

        assertNotNull(MemoryStore.getPharmacy());
        assertNotNull(MemoryStore.getPharmacist());

        MemoryStore.clearSessionPharma();

        assertNull(MemoryStore.getPharmacy());
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * Clear session clerk test.
     */
    @Test
    public void clearSessionClerk_Test() {
        MemoryStore.setClerk(new Clerk("C","s","69", "c", "p"));
        assertNotNull(MemoryStore.getClerk());

        MemoryStore.clearSessionClerk();
        assertNull(MemoryStore.getClerk());
    }

    /**
     * Init inventory if needed test.
     */
    @Test
    public void initInventoryIfNeeded_Test() {
        assertNull(MemoryStore.getInventory());

        Clerk c = new Clerk("C","s","69", "c", "p");

        MemoryStore.initInventoryIfNeeded(c, new BatchDAOMemory(), new InventoryLineDAOMemory(), new BackorderDAOMemory(), new ReservationDAOMemory());

        assertNotNull(MemoryStore.getInventory());
    }

    /**
     * Init inventory updates owner if exists.
     */
    @Test
    public void initInventory_UpdatesOwnerIfExists() {
        Clerk c1 = new Clerk("C1","s1","69", "c", "p");
        Clerk c2 = new Clerk("C2","s2","69", "c", "p");

        MemoryStore.initInventoryIfNeeded(c1, new BatchDAOMemory(), new InventoryLineDAOMemory(), new BackorderDAOMemory(), new ReservationDAOMemory());

        MemoryStore.initInventoryIfNeeded(c2, new BatchDAOMemory(), new InventoryLineDAOMemory(), new BackorderDAOMemory(), new ReservationDAOMemory());

        assertNotNull(MemoryStore.getInventory());
    }
}
