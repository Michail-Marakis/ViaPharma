package gr.softeng.team09.androidTest.MemoryDAO;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;

import static org.junit.Assert.*;

/**
 * The type Pharmacy dao memory test.
 */
public class PharmacyDAOMemoryTest {

    private PharmacyDAOMemory dao;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dao = new PharmacyDAOMemory();

        PharmacyDAOMemory.entitiesPharmacy.clear();
        MemoryStore.Allpharmacy.clear();
    }

    /**
     * Save adds pharmacy to memory.
     */
    @Test
    public void save_AddsPharmacyToMemory() {

        Pharmacist owner = new Pharmacist("Nikos", "Papadopoulos", "nikos@mail.com", 697123456, "1234");


        Pharmacy p = new Pharmacy("MyPharma", 123456789, owner, null, "pass123");


        dao.save(p);

        assertEquals(1, PharmacyDAOMemory.entitiesPharmacy.size());
        assertTrue(PharmacyDAOMemory.entitiesPharmacy.contains(p));

        assertTrue(MemoryStore.Allpharmacy.contains(p));
    }

    /**
     * Save existing pharmacy does not add duplicate.
     */
    @Test
    public void save_ExistingPharmacy_DoesNotAddDuplicate() {
        Pharmacist owner = new Pharmacist("Nikos", "Pap", "n@m.com", 123, "1");
        Pharmacy p = new Pharmacy("MyPharma", 1000, owner, null, "pass");

        dao.save(p);
        dao.save(p);

        assertEquals("Should contain only 1 pharmacy", 1, PharmacyDAOMemory.entitiesPharmacy.size());
    }

    /**
     * Find by afm returns correct pharmacy.
     */
    @Test
    public void findByAfm_ReturnsCorrectPharmacy() {
        Pharmacist owner = new Pharmacist("Maria", "Geo", "m@m.com", 987, "2");
        Pharmacy p = new Pharmacy("MariaPharma", 999999999, owner, null, "pass");
        dao.save(p);

        Pharmacy found = dao.findByAfm("999999999");

        assertNotNull("Pharmacy should be found", found);
        assertEquals(p, found);
    }

    /**
     * Find by afm returns null if not found.
     */
    @Test
    public void findByAfm_ReturnsNullIfNotFound() {
        assertNull(dao.findByAfm("000000000"));
    }

    /**
     * Find by name returns correct pharmacy.
     */
    @Test
    public void findByName_ReturnsCorrectPharmacy() {
        Pharmacist owner = new Pharmacist("Giannis", "Ant", "g@m.com", 555, "3");
        Pharmacy p = new Pharmacy("UniqueNamePharma", 111222333, owner, null, "pass");
        dao.save(p);

        Pharmacy found = dao.findByName("UniqueNamePharma");

        assertNotNull(found);
        assertEquals(p, found);
    }

    /**
     * Find by name returns null if not found.
     */
    @Test
    public void findByName_ReturnsNullIfNotFound() {
        assertNull(dao.findByName("GhostPharma"));
    }

    /**
     * Contains returns true if afm exists.
     */
    @Test
    public void contains_ReturnsTrueIfAfmExists() {
        Pharmacist owner = new Pharmacist("Costas", "X", "c@m.com", 111, "4");
        Pharmacy p = new Pharmacy("Test", 55555, owner, null, "pass");
        dao.save(p);

        assertTrue(dao.contains("55555"));
        assertFalse(dao.contains("123"));
    }

    /**
     * Save null does nothing.
     */
    @Test
    public void save_Null_DoesNothing() {
        dao.save(null);
        assertEquals(0, PharmacyDAOMemory.entitiesPharmacy.size());
    }
}
