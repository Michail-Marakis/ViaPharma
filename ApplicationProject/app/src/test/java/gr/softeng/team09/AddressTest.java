package gr.softeng.team09;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team09.domain.Address;

/**
 * The type Address test.
 */
public class AddressTest {

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        Address address = new Address("Patision", 12, "Athens", "11251");

        assertEquals("Patision", address.getStreet());
        assertEquals(12, address.getStreetNo());
        assertEquals("Athens", address.getRegion());
        assertEquals("11251", address.getPostalCode());
    }

    /**
     * Test set street.
     */
    @Test
    public void testSetStreet() {
        Address address = new Address("Old", 1, "Athens", "11111");
        address.setStreet("New Street");
        assertEquals("New Street", address.getStreet());
    }

    /**
     * Test set street no.
     */
    @Test
    public void testSetStreetNo() {
        Address address = new Address("Test", 1, "Athens", "11111");
        address.setStreetNo(50);
        assertEquals(50, address.getStreetNo());
    }

    /**
     * Test set region.
     */
    @Test
    public void testSetRegion() {
        Address address = new Address("Test", 1, "Old Region", "11111");
        address.setRegion("New Region");
        assertEquals("New Region", address.getRegion());
    }

    /**
     * Test set postal code.
     */
    @Test
    public void testSetPostalCode() {
        Address address = new Address("Test", 1, "Region", "11111");
        address.setPostalCode("99999");
        assertEquals("99999", address.getPostalCode());
    }
}
