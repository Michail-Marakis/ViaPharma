package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;

import static org.junit.Assert.*;

/**
 * The type Pharmacist dao memory test.
 */
public class PharmacistDAOMemoryTest {

    private PharmacistDAOMemory dao;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dao = new PharmacistDAOMemory();
        PharmacistDAOMemory.entities.clear();
        MemoryStore.Allpharmacist.clear();
    }

    /**
     * Save adds pharmacist.
     */
    @Test
    public void save_AddsPharmacist() {
        Pharmacist p = new Pharmacist("Nikos", "Baglamas","nikos@gmail.com",69, "1234");
        dao.save(p);

        assertTrue(dao.exists("nikos@gmail.com"));
        assertEquals(p, dao.findByEmailAndPassword("nikos@gmail.com", "1234"));
    }

    /**
     * Save does not add duplicate.
     */
    @Test
    public void save_DoesNotAddDuplicate() {
        Pharmacist p = new Pharmacist("Nikos", "Baglamas","nikos@gmail.com",69, "1234");
        dao.save(p);
        dao.save(p); // Ξανά

        assertEquals(1, PharmacistDAOMemory.entities.size());
    }

    /**
     * Find by email and password returns null for wrong creds.
     */
    @Test
    public void findByEmailAndPassword_ReturnsNullForWrongCreds() {
        dao.save(new Pharmacist("Nikos", "Baglamas","nikos@gmail.com",69, "1234"));

        assertNull(dao.findByEmailAndPassword("nikos@gmail.com", "wrongpass"));
        assertNull(dao.findByEmailAndPassword("wrong@gmail.com", "1234"));
    }

    /**
     * Exists returns false for unknown.
     */
    @Test
    public void exists_ReturnsFalseForUnknown() {
        assertFalse(dao.exists("unknown@gmail.com"));
    }
}
