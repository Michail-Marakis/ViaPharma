package gr.softeng.team09;

import gr.softeng.team09.domain.Clerk;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * The type Clerk test.
 */
public class ClerkTest {

    private Clerk clerk;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        clerk = new Clerk("Giorgos", "Papadopoulos", "2101234567", "test@example.com","pass");
    }

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        assertEquals("Giorgos", clerk.getName());
        assertEquals("Papadopoulos", clerk.getSurname());
        assertEquals("2101234567", clerk.getPhone());
        assertEquals("test@example.com", clerk.getEmail());
        assertEquals("pass", clerk.getPassword());
    }

    /**
     * Test setters.
     */
    @Test
    public void testSetters() {
        clerk.setName("Nikos");
        clerk.setSurname("Nikolaou");
        clerk.setPhone("2109999999");
        clerk.setEmail("nikos@example.com");
        clerk.setPassword("pass");

        assertEquals("Nikos", clerk.getName());
        assertEquals("Nikolaou", clerk.getSurname());
        assertEquals("2109999999", clerk.getPhone());
        assertEquals("nikos@example.com", clerk.getEmail());
        assertEquals("pass", clerk.getPassword());
    }

    /**
     * Test equals and hashCode.
     *
     */
    @Test
    public void testEqualsAndHashCode() {
        assertTrue(clerk.equals(clerk));

        assertFalse(clerk.equals(null));
        assertFalse(clerk.equals("Some String"));

        Clerk otherClerkDiffEmail = new Clerk("Giorgos", "Papadopoulos", "2101234567", "other@example.com", "pass");
        assertFalse(clerk.equals(otherClerkDiffEmail));
        assertNotEquals(clerk.hashCode(), otherClerkDiffEmail.hashCode());

        Clerk otherClerkSameEmail = new Clerk("OtherName", "OtherSurname", "0000000000", "test@example.com", "pass");
        assertTrue(clerk.equals(otherClerkSameEmail));
        assertEquals(clerk.hashCode(), otherClerkSameEmail.hashCode());
    }

    /**
     * Test email sending tracking and recipient list.
     * Checks setEmailsSent, getEmailsSent, and getSentTo.
     */
    @Test
    public void testEmailsSentAndRecipients() {
        gr.softeng.team09.domain.Pharmacist recipient = new gr.softeng.team09.domain.Pharmacist("Maria", "K", "maria@example.com", 690000000, "password");

        StringBuilder emailBody = new StringBuilder("Backorder Completed successfully.");

        clerk.setEmailsSent(emailBody, recipient);

        assertFalse(clerk.getEmailsSent().isEmpty());
        assertEquals(1, clerk.getEmailsSent().size());
        assertEquals("Backorder Completed successfully.", clerk.getEmailsSent().get(0).toString());

        assertFalse(clerk.getSentTo().isEmpty());
        assertEquals(1, clerk.getSentTo().size());
        assertEquals(recipient, clerk.getSentTo().get(0));
    }
}
