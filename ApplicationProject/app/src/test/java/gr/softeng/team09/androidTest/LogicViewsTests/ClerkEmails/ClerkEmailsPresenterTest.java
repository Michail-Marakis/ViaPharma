package gr.softeng.team09.androidTest.LogicViewsTests.ClerkEmails;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.*;
import gr.softeng.team09.memorydao.*;
import gr.softeng.team09.view.ClerkEmails.ClerkEmailsPresenter;
import gr.softeng.team09.view.ClerkEmails.ClerkEmailsView;

import static org.junit.Assert.*;

/**
 * The type Clerk emails presenter test.
 */
public class ClerkEmailsPresenterTest {

    private ClerkEmailsPresenter presenter;

    // View Captures
    private List<String> shownEmails;
    private String popupTitle;
    private String popupMessage;

    private Inventory inventory;

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
        assertNotNull(inventory);

        ensureOwnerAndClearEmails(inventory);

        presenter = new ClerkEmailsPresenter();

        shownEmails = null;
        popupTitle = null;
        popupMessage = null;

        presenter.setView(new ClerkEmailsView() {
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
     * Load emails list reverses order and formats titles.
     *
     * @throws Exception the exception
     */
    @Test
    public void loadEmailsList_reversesOrder_and_FormatsTitles() throws Exception {
        StringBuilder emailOld = new StringBuilder("Old Message Content");
        StringBuilder emailNew = new StringBuilder("New Message Content");

        addEmailToOwner(inventory, emailOld);
        addEmailToOwner(inventory, emailNew);

        presenter.loadEmailsList();

        assertNotNull(shownEmails);
        assertEquals(2, shownEmails.size());

        assertEquals("Email 1", shownEmails.get(0));
        assertEquals("Email 2", shownEmails.get(1));
    }

    /**
     * On email clicked maps ui index to real list index correctly.
     *
     * @throws Exception the exception
     */
    @Test
    public void onEmailClicked_mapsUiIndexToRealListIndex_correctly() throws Exception {
        StringBuilder msg1 = new StringBuilder("First Message");
        StringBuilder msg2 = new StringBuilder("Second Message");
        StringBuilder msg3 = new StringBuilder("Third Message"); // Το πιο πρόσφατο

        addEmailToOwner(inventory, msg1);
        addEmailToOwner(inventory, msg2);
        addEmailToOwner(inventory, msg3);

        presenter.loadEmailsList();
        presenter.onEmailClicked(0);

        assertEquals("Email", popupTitle);
        assertEquals("Third Message", popupMessage);

        presenter.onEmailClicked(2);

        assertEquals("First Message", popupMessage);
    }

    /**
     * On email clicked with empty list or invalid index does nothing.
     */
    @Test
    public void onEmailClicked_edgeCases() {
        presenter.loadEmailsList();
        presenter.onEmailClicked(0);

        assertNull(popupMessage);

        StringBuilder msg = new StringBuilder("Solo Message");
        try {
            addEmailToOwner(inventory, msg);
        } catch (Exception e) {
            fail("Setup failed");
        }
        presenter.loadEmailsList();

        presenter.onEmailClicked(0);
        assertNotNull(popupMessage);

        popupMessage = null;

        presenter.onEmailClicked(-1);
        assertNull(popupMessage);

        presenter.onEmailClicked(100);
        assertNull(popupMessage);
    }

    // ---------- helpers ----------

    private static void ensureOwnerAndClearEmails(Inventory inv) throws Exception {
        Clerk owner = inv.getOwner();
        if (owner == null) {
            owner = new Clerk("Nikos","Baglamas","69","clerk@gmail.gr","1");
            Field ownerField = Inventory.class.getDeclaredField("owner");
            ownerField.setAccessible(true);
            ownerField.set(inv, owner);
        }


        owner.getEmailsSent().clear();
    }

    private static void addEmailToOwner(Inventory inv, StringBuilder emailContent) throws Exception {
        Clerk owner = inv.getOwner();
        owner.getEmailsSent().add(emailContent);
    }
}