package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.MemoryInitializer;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.OrderDAOMemory;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;
import gr.softeng.team09.memorydao.ProductDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

import static org.junit.Assert.*;

/**
 * The type Memory initializer test.
 */
public class MemoryInitializerTest {

    private MemoryInitializer initializer;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        initializer = new MemoryInitializer();
    }

    /**
     * Gets da os return correct instances.
     */
    @Test
    public void getDAOs_ReturnCorrectInstances() {
        assertTrue(initializer.getProductDAO() instanceof ProductDAOMemory);
        assertTrue(initializer.getPharmacistDAO() instanceof PharmacistDAOMemory);
        assertTrue(initializer.getPharmacyDAO() instanceof PharmacyDAOMemory);
        assertTrue(initializer.getClerkDAO() instanceof ClerkDAOMemory);
        assertTrue(initializer.getBatchDAO() instanceof BatchDAOMemory);
        assertTrue(initializer.getOrderDAO() instanceof OrderDAOMemory);
        assertTrue(initializer.getInventoryLineDAO() instanceof InventoryLineDAOMemory);
        assertTrue(initializer.getBackorderDAO() instanceof BackorderDAOMemory);
        assertTrue(initializer.getReservationDAO() instanceof ReservationDAOMemory);
    }

    /**
     * Erase data clears all memory da os.
     */
    @Test
    public void eraseData_ClearsAllMemoryDAOs() {

        ProductDAOMemory.entities.add(new Product("Test", ProductCategory.OTHER, 1, 1));

        PharmacistDAOMemory.entities.add(new Pharmacist("P","b", "p@mail.gr", 69, "p"));


        PharmacyDAOMemory.entitiesPharmacy.add(new Pharmacy("Ph", 123, null, null, "p"));


        ClerkDAOMemory.entitiesClerk.add(new Clerk("C", "c","69","c@gmail.gr", "c"));

        MemoryStore.setPharmacist(new Pharmacist("P","b", "p@mail.gr", 69, "p"));
        MemoryStore.setClerk(new Clerk("C", "c","69","c@gmail.gr", "c"));

        assertFalse(ProductDAOMemory.entities.isEmpty());
        assertFalse(PharmacistDAOMemory.entities.isEmpty());
        assertFalse(PharmacyDAOMemory.entitiesPharmacy.isEmpty());
        assertFalse(ClerkDAOMemory.entitiesClerk.isEmpty());
        assertNotNull(MemoryStore.getPharmacist());


        initializer.eraseData();

        assertTrue("Products should be empty", ProductDAOMemory.entities.isEmpty());
        assertTrue("Pharmacists should be empty", PharmacistDAOMemory.entities.isEmpty());
        assertTrue("Pharmacies should be empty", PharmacyDAOMemory.entitiesPharmacy.isEmpty());
        assertTrue("Clerks should be empty", ClerkDAOMemory.entitiesClerk.isEmpty());

        assertNull("Session Pharmacist should be cleared", MemoryStore.getPharmacist());
        assertNull("Session Clerk should be cleared", MemoryStore.getClerk());
    }
}
