package gr.softeng.team09.androidTest.LogicViewsTests.SignInPharmacist.PharmacistForget;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;
import gr.softeng.team09.view.SignInPharmacist.PharmacistForget.PharmacistForgetPresenter;
import gr.softeng.team09.view.SignInPharmacist.PharmacistForget.PharmacistForgetView;

import static org.junit.Assert.*;

/**
 * The type Pharmacist forget presenter test.
 */
public class PharmacistForgetPresenterTest {

    private PharmacistForgetPresenter presenter;

    private List<String> errors;
    private String lastSuccess;
    private int goStartCount;

    private Pharmacist pharmacist;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset static state
        PharmacistDAOMemory.entities.clear();
        MemoryStore.Allpharmacist.clear();
        MemoryStore.clearSessionPharma();

        errors = new ArrayList<>();
        lastSuccess = null;
        goStartCount = 0;

        presenter = new PharmacistForgetPresenter();
        presenter.setView(new PharmacistForgetView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void goToStartingScreen() { goStartCount++; }
        });

        // create + register pharmacist
        pharmacist = new Pharmacist("A", "B", "pharm@mail.com", 6900, "oldpw");
        PharmacistDAOMemory.entities.add(pharmacist);
        MemoryStore.addPharmacist(pharmacist);

        // IMPORTANT: στο success path ο κώδικας αλλάζει password με MemoryStore.getPharmacist().SetCode(...)
        // άρα πρέπει να είναι logged in
        MemoryStore.setPharmacist(pharmacist);
    }

    /**
     * Authentication session when user not identified shows user not identified.
     */
    @Test
    public void authenticationSession_whenUserNotIdentified_showsUserNotIdentified() {
        presenter.setUsername("unknown@mail.com");
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
        presenter.setUsername("pharm@mail.com");
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("DIFFERENT");

        presenter.authenticationSession();

        assertTrue(errors.contains("Confirmation password is not correct"));
        assertNull(lastSuccess);
        assertEquals(0, goStartCount);

        // password unchanged
        assertEquals("oldpw", pharmacist.getPassword());
    }

    /**
     * Authentication session when all ok changes password and navigates.
     */
    @Test
    public void authenticationSession_whenAllOk_changesPassword_andNavigates() {
        presenter.setUsername("pharm@mail.com");
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("newpw");

        presenter.authenticationSession();

        assertEquals("Password changed successfully", lastSuccess);
        assertEquals(1, goStartCount);

        // άλλαξε το SignInCode μέσω SetCode
        assertEquals("newpw", pharmacist.getPassword());
    }

    /**
     * Authentication session when missing fields shows fill in the form.
     */
    @Test
    public void authenticationSession_whenMissingFields_showsFillInTheForm() {
        presenter.setUsername("pharm@mail.com");
        presenter.setNewPassword(null);
        presenter.setNewPasswordConfirmation(null);

        presenter.authenticationSession();

        assertTrue(errors.contains("Fill in the form"));
    }
}
