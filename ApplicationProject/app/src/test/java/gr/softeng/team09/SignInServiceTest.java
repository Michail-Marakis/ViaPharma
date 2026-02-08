package gr.softeng.team09;

import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.SignInService;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

/**
 * The type Sign in service test.
 */
public class SignInServiceTest {

    /**
     * Test success sign up.
     */
    @Test
    public void TestSuccessSignUp(){

        Clerk clerk = new Clerk("prodromos", "marakis", "32323", "sda","pass");
        Pharmacist pharmacist = new Pharmacist("prodromos", "serdaris", "proser@gmail.com", 3232323, "ds");
        SignInService sin = new SignInService();
        BatchDAO batchDAO = new BatchDAOMemory();
        InventoryLineDAO inventoryLineDAO = new InventoryLineDAOMemory();
        BackorderDAO backorderDAO = new BackorderDAOMemory();
        ReservationDAO reservationDAO = new ReservationDAOMemory();
        Inventory inventory = new Inventory(clerk,batchDAO,inventoryLineDAO, backorderDAO,reservationDAO);


        sin.signUpPharmacist(pharmacist);
        assertEquals(pharmacist, sin.getUsers().get(0));
        Address address = new Address("ZO", 32, "athens", "323");
        Pharmacy pharmacy = new Pharmacy("tralalelo", 323, pharmacist,address,"pass");

        sin.addPharmacyToPharmacist(pharmacist, pharmacy);
        assertEquals(pharmacy, pharmacist.getPharmacies().get(0));
        sin.SignUpOwner(clerk, 545, inventory);

        assertEquals(clerk, sin.getInventoryOwner());
        assertEquals(545, sin.getClerkCode());
        assertEquals(inventory.getOwner(), sin.getInventoryOwner());

        assertEquals(sin.ClerkSignIn(545), clerk);
        assertNull(sin.ClerkSignIn(1000));

    }

    /**
     * Test successful sign in pharmacist without pharmacy.
     */
    @Test
    public void testSuccessfulSignInPharmacistWithoutPharmacy() {
        Pharmacist pharmacist = new Pharmacist("mk", "P", "s@example.com", 123, "1234");
        SignInService sin = new SignInService();

        sin.signUpPharmacist(pharmacist);

        Pharmacist loggedIn = sin.signInPharmacist("s@example.com", "1234");
        assertNotNull(loggedIn);
        assertEquals(pharmacist, loggedIn);
    }

    /**
     * Test sign in fails with wrong password.
     */
    @Test
    public void testSignInFailsWithWrongPassword() {
        Pharmacist pharmacist = new Pharmacist("ee", "P", "ne@example.com", 123, "1234");
        SignInService sin = new SignInService();

        sin.signUpPharmacist(pharmacist);

        Pharmacist loggedIn = sin.signInPharmacist("ne@example.com", "wrong");
        assertNull(loggedIn);
    }

    /**
     * Test select pharmacy success and failure.
     */
    @Test
    public void testSelectPharmacySuccessAndFailure() {
        Pharmacist pharmacist = new Pharmacist("Prodromos", "S", "proser@gmail.com", 3232323, "ds");
        SignInService sin = new SignInService();
        sin.signUpPharmacist(pharmacist);

        Address address = new Address("ZO", 32, "athens", "323");
        Pharmacy ph = new Pharmacy("tralalelo", 323, pharmacist, address,"pass");
        sin.addPharmacyToPharmacist(pharmacist, ph);

        //επιτυχής επιλογή
        Pharmacy selected = sin.selectPharmacy(pharmacist, "tralalelo", 323);
        assertNotNull(selected);
        assertEquals(ph, selected);

        //αποτυχημένη επιλογή (λάθος όνομα)
        Pharmacy wrong = sin.selectPharmacy(pharmacist, "wrongName", 323);
        assertNull(wrong);
    }



}
