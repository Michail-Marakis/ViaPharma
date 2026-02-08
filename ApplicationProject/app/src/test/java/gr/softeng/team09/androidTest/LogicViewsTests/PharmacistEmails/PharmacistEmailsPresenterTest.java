package gr.softeng.team09.androidTest.LogicViewsTests.PharmacistEmails;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;
import gr.softeng.team09.view.PharmacistEmails.PharmacistEmailsPresenter;
import gr.softeng.team09.view.PharmacistEmails.PharmacistEmailsView;

import static org.junit.Assert.*;

/**
 * The type Pharmacist emails presenter test.
 */
public class PharmacistEmailsPresenterTest {

    private PharmacistEmailsPresenter presenter;
    private Inventory inventory;

    private List<String> shownEmails;
    private String popupTitle;
    private String popupMessage;

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
        assertNotNull("Inventory should not be null", inventory);

        clearInventoryOwners(inventory);
        Pharmacist myPharmacist = new Pharmacist("Pro", "Mak", "me@pharmacy.test", 69, "1");
        injectStaticFieldByType(MemoryStore.class, Pharmacist.class, myPharmacist);

        presenter = new PharmacistEmailsPresenter();
        shownEmails = null;
        popupTitle = null;
        popupMessage = null;

        presenter.setView(new PharmacistEmailsView() {
            @Override
            public void showEmails(List<String> emails) {
                shownEmails = emails;
            }

            @Override
            public void showEmailPopup(String title, String message) {
                popupTitle = title;
                popupMessage = message;
            }
        });
    }

    /**
     * Gets emails filters logic and replaces text.
     *
     * @throws Exception the exception
     */
    @Test
    public void getEmails_filtersLogic_and_replacesText() throws Exception {
        String myEmail = "me@pharmacy.test";
        String otherEmail = "other@pharmacy.test";

        Clerk clerk = new Clerk("nikos", "baglams", "69", "clerk@gmail.gr", "1");

        Pharmacist pMe = new Pharmacist("Pro", "Mak", myEmail, 69, "1");
        clerk.getSentTo().add(pMe);
        clerk.getEmailsSent().add(new StringBuilder("Order sent successfully"));

        Pharmacist pOther = new Pharmacist("Other", "User", otherEmail, 69, "1");
        clerk.getSentTo().add(pOther);
        clerk.getEmailsSent().add(new StringBuilder("Other order sent"));

        addOwnerToInventory(inventory, clerk);

        List<StringBuilder> result = presenter.getEmails();

        assertEquals("Should find exactly 1 email", 1, result.size());

        String emailContent = result.get(0).toString();
        assertTrue("Message should contain 'received'", emailContent.contains("received"));
        assertFalse("Message should NOT contain 'sent'", emailContent.contains("sent"));
    }

    /**
     * Load emails list prepares titles correctly in reverse order.
     *
     * @throws Exception the exception
     */
    @Test
    public void loadEmailsList_preparesTitlesCorrectly_inReverseOrder() throws Exception {
        String myEmail = "me@pharmacy.test";
        Clerk clerk = new Clerk("nikos", "baglams", "69", "clerk@gmail.gr", "1");
        Pharmacist pMe = new Pharmacist("Pro", "Mak", myEmail, 69, "1");

        clerk.getSentTo().add(pMe);
        clerk.getEmailsSent().add(new StringBuilder("Msg A (Old)"));
        clerk.getSentTo().add(pMe);
        clerk.getEmailsSent().add(new StringBuilder("Msg B (New)"));

        addOwnerToInventory(inventory, clerk);

        presenter.loadEmailsList();

        assertNotNull(shownEmails);
        assertEquals(2, shownEmails.size());

        assertEquals("Email 1", shownEmails.get(0));
        assertEquals("Email 2", shownEmails.get(1));
    }

    /**
     * On email clicked maps index correctly and shows popup.
     *
     * @throws Exception the exception
     */
    @Test
    public void onEmailClicked_mapsIndexCorrectly_and_showsPopup() throws Exception {
        String myEmail = "me@pharmacy.test";
        Clerk clerk = new Clerk("nikos", "baglams", "69", "clerk@gmail.gr", "1");
        Pharmacist pMe = new Pharmacist("Pro", "Mak", myEmail, 69, "1");

        clerk.getSentTo().add(pMe);
        clerk.getEmailsSent().add(new StringBuilder("Old Msg sent"));

        clerk.getSentTo().add(pMe);
        clerk.getEmailsSent().add(new StringBuilder("New Msg sent"));

        addOwnerToInventory(inventory, clerk);
        presenter.loadEmailsList();

        presenter.onEmailClicked(0);

        assertEquals("Email", popupTitle);
        assertEquals("New Msg received", popupMessage);
    }

    /**
     * On email clicked validations.
     *
     * @throws Exception the exception
     */
    @Test
    public void onEmailClicked_validations() throws Exception {
        presenter.loadEmailsList();
        presenter.onEmailClicked(0);
        assertNull(popupMessage);

        Clerk clerk = new Clerk("nikos", "baglams", "69", "clerk@gmail.gr", "1");
        Pharmacist pMe = new Pharmacist("Pro", "Mak", "me@pharmacy.test", 69, "1");

        clerk.getSentTo().add(pMe);
        clerk.getEmailsSent().add(new StringBuilder("Hello"));

        addOwnerToInventory(inventory, clerk);
        presenter.loadEmailsList();

        popupMessage = null;

        presenter.onEmailClicked(-1);
        assertNull(popupMessage);

        presenter.onEmailClicked(100);
        assertNull(popupMessage);
    }


    private void clearInventoryOwners(Inventory inv) throws Exception {
        Field ownersField = findField(Inventory.class, "owners");
        if (ownersField == null) {
            ownersField = findField(Inventory.class, "allOwners");
        }

        if (ownersField != null) {
            ownersField.setAccessible(true);
            List<?> list = (List<?>) ownersField.get(inv);
            if (list != null) {
                list.clear();
            }
        }
    }

    private void addOwnerToInventory(Inventory inv, Object owner) throws Exception {
        Field ownersField = findField(Inventory.class, "owners");
        if (ownersField == null) {
            ownersField = findField(Inventory.class, "allOwners");
        }

        if (ownersField != null) {
            ownersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) ownersField.get(inv);
            list.add(owner);
        } else {
            inv.getOwners().add((Clerk) owner);
        }
    }

    private Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private void injectStaticFieldByType(Class<?> targetClass, Class<?> fieldType, Object value) throws Exception {
        boolean found = false;
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(fieldType)) {
                field.setAccessible(true);
                field.set(null, value);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchFieldException("Could not find static field of type " + fieldType.getSimpleName() + " in " + targetClass.getSimpleName());
        }
    }
}