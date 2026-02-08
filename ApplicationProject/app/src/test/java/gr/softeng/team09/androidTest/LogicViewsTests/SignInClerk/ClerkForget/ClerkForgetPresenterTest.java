package gr.softeng.team09.androidTest.LogicViewsTests.SignInClerk.ClerkForget;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.SignInClerk.ClerkForget.ClerkForgetPresenter;
import gr.softeng.team09.view.SignInClerk.ClerkForget.ClerkForgetView;

import static org.junit.Assert.*;

/**
 * The type Clerk forget presenter test.
 */
public class ClerkForgetPresenterTest {

    private ClerkForgetPresenter presenter;

    private List<String> errors;
    private String lastSuccess;
    private int goStartCount;

    private Clerk clerk;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset static storage
        ClerkDAOMemory.entitiesClerk.clear();
        MemoryStore.AllClerk.clear();

        // init captures
        errors = new ArrayList<>();
        lastSuccess = null;
        goStartCount = 0;

        // create presenter FIRST
        presenter = new ClerkForgetPresenter();

        // set ONE view that stores history
        presenter.setView(new ClerkForgetView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void goToStartingScreen() { goStartCount++; }
        });

        // create + register a clerk (πρέπει να ταιριάζει στον constructor σου)
        clerk = makeClerk("clerk@mail.com", "oldpw");
        ClerkDAOMemory.entitiesClerk.add(clerk);
        MemoryStore.addClerk(clerk);
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
        presenter.setUsername("clerk@mail.com");
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("DIFFERENT");

        presenter.authenticationSession();

        assertTrue(errors.contains("Confirmation password is not correct"));
        assertNull(lastSuccess);
        assertEquals(0, goStartCount);

        assertEquals("oldpw", clerk.getPassword());
    }

    /**
     * Authentication session when all ok changes password and navigates.
     */
    @Test
    public void authenticationSession_whenAllOk_changesPassword_andNavigates() {
        presenter.setUsername("clerk@mail.com");
        presenter.setNewPassword("newpw");
        presenter.setNewPasswordConfirmation("newpw");

        presenter.authenticationSession();

        assertEquals("Password changed successfully", lastSuccess);
        assertEquals(1, goStartCount);
        assertEquals("newpw", clerk.getPassword());
    }

    /**
     * Authentication session when missing fields shows fill in the form.
     */
    @Test
    public void authenticationSession_whenMissingFields_showsFillInTheForm() {
        presenter.setUsername("clerk@mail.com");
        presenter.setNewPassword(null);
        presenter.setNewPasswordConfirmation(null);

        presenter.authenticationSession();

        assertTrue(errors.contains("Fill in the form"));
    }

    // ---- helper ----
    private Clerk makeClerk(String email, String password) {
        return new Clerk("John", "Doe", "123", email, password);
    }
}
