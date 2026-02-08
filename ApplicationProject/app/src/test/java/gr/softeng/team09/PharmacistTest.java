package gr.softeng.team09;

import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Pharmacist test.
 */
public class PharmacistTest {

    private Pharmacist pharmacist;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        pharmacist = new Pharmacist("Giannis", "Papadopoulos", "p3230267@aueb.gr", 75643, "d");
    }

    /**
     * Test initial state.
     */
    @Test
    public void testInitialState() {
        assertEquals("Giannis", pharmacist.getFirstName());
        assertEquals("Papadopoulos", pharmacist.getLastName());
        assertEquals(75643, pharmacist.getPhoneNumber());
        assertNotNull(pharmacist.getPharmacies());
        assertTrue(pharmacist.getPharmacies().isEmpty());
        assertEquals("p3230267@aueb.gr", pharmacist.getEmail());
        assertEquals("d", pharmacist.getPassword());
    }

    /**
     * Test add pharmacy directly.
     */
    @Test
    public void testAddPharmacyDirectly() {
        Address addr = new Address("Οδός", 1, "Πόλη", "12345");
        Pharmacy ph = new Pharmacy("Pharmacy A", 1212, pharmacist, addr,"pass");

        pharmacist.addPharmacy(ph);

        List<Pharmacy> list = pharmacist.getPharmacies();
        assertEquals(1, list.size());
        assertTrue(list.contains(ph));

        assertEquals(pharmacist, ph.getOwner());
    }

    /**
     * Test set owner from pharmacy side.
     */
    @Test
    public void testSetOwnerFromPharmacySide() {
        Address addr = new Address("Οδός", 1, "Πόλη", "12345");
        Pharmacy ph = new Pharmacy("Pharmacy B",121212, pharmacist, addr,"pass");

        ph.setOwner(pharmacist);

        assertEquals(pharmacist, ph.getOwner());
        assertTrue(pharmacist.getPharmacies().contains(ph));
    }

    /**
     * Test no duplicate pharmacies.
     */
    @Test
    public void testNoDuplicatePharmacies() {
        Address addr = new Address("Οδός", 1, "Πόλη", "12345");
        Pharmacy ph = new Pharmacy("Pharmacy C",212, pharmacist, addr,"pass");

        pharmacist.addPharmacy(ph);
        pharmacist.addPharmacy(ph);

        assertEquals(1, pharmacist.getPharmacies().size());
    }

    /**
     * Test SetCode and getPassword methods to reach 100% coverage.
     *
     */
    @Test
    public void testSetAndGetCode() {
        assertEquals("d", pharmacist.getPassword());

        pharmacist.SetCode("newPass123");
        assertEquals("newPass123", pharmacist.getPassword());
    }
}