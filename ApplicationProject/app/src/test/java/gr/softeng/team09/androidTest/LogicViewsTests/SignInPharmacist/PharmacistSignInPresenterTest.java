package gr.softeng.team09.androidTest.LogicViewsTests.SignInPharmacist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.androidTest.LogicViewsTests.PresenterTestBase;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;
import gr.softeng.team09.view.SignInPharmacist.PharmacistSignInPresenter;
import gr.softeng.team09.view.SignInPharmacist.PharmacistSignInView;
import gr.softeng.team09.view.SignUpPharmacist.PharmacistSignUpPresenter;
import gr.softeng.team09.view.SignUpPharmacist.PharmacistSignUpView;

/**
 * The type Pharmacist sign in presenter test.
 */
public class PharmacistSignInPresenterTest extends PresenterTestBase {

    private static class FakeView implements PharmacistSignInView {
        /**
         * The Errors.
         */
        List<String> errors = new ArrayList<>();
        /**
         * The Go pharmacy.
         */
        int goPharmacy = 0;
        /**
         * The Forgot dialog.
         */
        int forgotDialog = 0;

        @Override public void showError(String message) { errors.add(message); }
        @Override public void navigateToPharmacy() { goPharmacy++; }
        @Override public void showForgotPasswordDialog() { forgotDialog++; }
    }

    private static class NoopSignUpView implements PharmacistSignUpView {
        @Override public void showSuccess(String message) {}
        @Override public void showError(String message) {}
        @Override public void finishSignUp() {}
    }

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // καθάρισμα static state
        PharmacistDAOMemory.entities.clear();
        MemoryStore.Allpharmacist.clear();
        MemoryStore.clearSessionPharma();
    }

    /**
     * On sign in empty fields shows errors and returns.
     */
    @Test
    public void onSignIn_emptyFields_showsErrors_andReturns() {
        PharmacistSignInPresenter presenter = new PharmacistSignInPresenter();
        FakeView view = new FakeView();
        presenter.setView(view);

        presenter.onSignIn("", "");

        assertTrue(view.errors.contains("Username is required"));
        assertTrue(view.errors.contains("Password is required"));
        assertEquals(0, view.goPharmacy);
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * On sign in wrong credentials shows error.
     */
    @Test
    public void onSignIn_wrongCredentials_showsError() {
        PharmacistSignInPresenter presenter = new PharmacistSignInPresenter();
        FakeView view = new FakeView();
        presenter.setView(view);

        presenter.onSignIn("wrong@mail.com", "wrong");

        assertTrue(view.errors.contains("Wrond email or password."));
        assertEquals(0, view.goPharmacy);
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * On sign in success sets session and navigates.
     */
    @Test
    public void onSignIn_success_setsSession_andNavigates() {
        // signup πρώτα
        PharmacistSignUpPresenter signUp = new PharmacistSignUpPresenter();
        signUp.setView(new NoopSignUpView());
        signUp.onSignUp("A", "B", "pharm@mail.com", "690000000", "pass");

        PharmacistSignInPresenter presenter = new PharmacistSignInPresenter();
        FakeView view = new FakeView();
        presenter.setView(view);

        presenter.onSignIn("pharm@mail.com", "pass");

        assertEquals(1, view.goPharmacy);
        assertNotNull(MemoryStore.getPharmacist());
        assertEquals("pharm@mail.com", MemoryStore.getPharmacist().getEmail());
    }

    /**
     * On forgot password opens dialog.
     */
    @Test
    public void onForgotPassword_opensDialog() {
        PharmacistSignInPresenter presenter = new PharmacistSignInPresenter();
        FakeView view = new FakeView();
        presenter.setView(view);

        presenter.onForgotPassword();

        assertEquals(1, view.forgotDialog);
    }
}
