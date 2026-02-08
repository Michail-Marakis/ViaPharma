package gr.softeng.team09.androidTest.LogicViewsTests.SignInPharmacy.PharmacyForget;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget.PharmacyForgetPresenter;
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget.PharmacyForgetView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy forget presenter test.
 */
public class PharmacyForgetPresenterTest {

    private PharmacyForgetPresenter presenter;

    private List<String> errors;
    private String lastSuccess;
    private int goStartCount;

    private Pharmacy pharmacy;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset static state
        PharmacyDAOMemory.entitiesPharmacy.clear();
        MemoryStore.Allpharmacy.clear();
        MemoryStore.clearSessionPharma();

        errors = new ArrayList<>();
        lastSuccess = null;
        goStartCount = 0;

        presenter = new PharmacyForgetPresenter();
        presenter.setView(new PharmacyForgetView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void goToStartingScreen() { goStartCount++; }
        });

        // create + register a pharmacy
        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 6900, "pw");
        pharmacy = new Pharmacy("PharmA", 111, owner, null, "oldpw");

        PharmacyDAOMemory.entitiesPharmacy.add(pharmacy);
        MemoryStore.addPharmacy(pharmacy);
    }

    /**
     * Authentication session when user not identified shows user not identified.
     */
    @Test
    public void authenticationSession_whenUserNotIdentified_showsUserNotIdentified() {
        presenter.setUsername("999"); // δεν υπάρχει
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("newpw");

        presenter.authenticationSession();

        assertTrue(errors.contains("User not identified"));
        assertNull(lastSuccess);
        assertEquals(0, goStartCount);
    }

    /**
     * Authentication session when confirmation mismatch shows error and does not navigate.
     */
    @Test
    public void authenticationSession_whenConfirmationMismatch_showsErrorAndDoesNotNavigate() {
        presenter.setUsername("111");
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("DIFFERENT");

        presenter.authenticationSession();

        assertTrue(errors.contains("Confirmation password is not correct"));
        assertNull(lastSuccess);
        assertEquals(0, goStartCount);

        assertEquals("oldpw", pharmacy.getPassword());
    }

    /**
     * Authentication session when all ok changes password and navigates.
     */
    @Test
    public void authenticationSession_whenAllOk_changesPassword_andNavigates() {
        presenter.setUsername("111");
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("newpw");

        presenter.authenticationSession();

        assertEquals("Password changed successfully", lastSuccess);
        assertEquals(1, goStartCount);
        assertEquals("newpw", pharmacy.getPassword());
    }

    /**
     * Authentication session when missing fields shows fill in the form.
     */
    @Test
    public void authenticationSession_whenMissingFields_showsFillInTheForm() {
        // ασφαλές: βάζουμε username για να μη μείνει default, αφήνουμε pass null
        presenter.setUsername("111");
        presenter.setNewPassword(null);
        presenter.setNewPasswordConfirmation(null);

        presenter.authenticationSession();

        assertTrue(errors.contains("Fill in the form"));
    }

    /**
     * Sets username when not number throws number format exception.
     */
    @Test(expected = NumberFormatException.class)
    public void setUsername_whenNotNumber_throwsNumberFormatException() {
        presenter.setUsername("abc");
    }
}
