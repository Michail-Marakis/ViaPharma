package gr.softeng.team09.androidTest.LogicViewsTests.SignInClerk;

import org.junit.Before;
import org.junit.Test;

import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.SignInClerk.SignInClerkPresenter;
import gr.softeng.team09.view.SignInClerk.SignInClerkView;

import static org.junit.Assert.*;

/**
 * The type Sign in clerk presenter test.
 */
public class SignInClerkPresenterTest {

    private SignInClerkPresenter presenter;

    // view captures
    private String lastError;
    private String lastSuccess;
    private int goMainCount;
    private int goForgotCount;
    private int goStartCount;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset MemoryStore clerk session
        MemoryStore.clearSessionClerk();

        presenter = new SignInClerkPresenter();

        lastError = null;
        lastSuccess = null;
        goMainCount = 0;
        goForgotCount = 0;
        goStartCount = 0;

        presenter.setView(new SignInClerkView() {
            @Override public void showError(String msg) { lastError = msg; }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void goToClerkMain() { goMainCount++; }
            @Override public void goToForgot() { goForgotCount++; }
            @Override public void goToStart() { goStartCount++; }
        });
    }

    /**
     * On sign in when username missing shows error.
     */
    @Test
    public void onSignIn_whenUsernameMissing_showsError() {
        presenter.onSignIn("   ", "pw");

        assertEquals("Username is required", lastError);
        assertNull(lastSuccess);
        assertEquals(0, goMainCount);
        assertNull(MemoryStore.getClerk());
    }

    /**
     * On sign in when password missing shows error.
     */
    @Test
    public void onSignIn_whenPasswordMissing_showsError() {
        presenter.onSignIn("user@mail.com", "   ");

        assertEquals("Password is required", lastError);
        assertNull(lastSuccess);
        assertEquals(0, goMainCount);
        assertNull(MemoryStore.getClerk());
    }

    /**
     * On sign in when wrong credentials shows error.
     */
    @Test
    public void onSignIn_whenWrongCredentials_showsError() {
        presenter.onSignIn("user@mail.com", "wrong");

        assertEquals("Wrong credentials", lastError);
        assertNull(lastSuccess);
        assertEquals(0, goMainCount);
        assertNull(MemoryStore.getClerk());
    }

    /**
     * On sign in when correct credentials sets memory store and navigates.
     */
    @Test
    public void onSignIn_whenCorrectCredentials_setsMemoryStoreAndNavigates() {
        // Arrange: αποθηκεύουμε clerk στο DAO που χρησιμοποιεί ο presenter
        // IMPORTANT: προσαρμόζεις τα arguments του Clerk constructor σύμφωνα με το δικό σου Clerk class.
        Clerk clerk = makeClerk("clerk@mail.com", "pw");

        ClerkDAOMemory dao = new ClerkDAOMemory();
        dao.save(clerk);

        // Act
        presenter.onSignIn("  clerk@mail.com  ", "  pw ");

        // Assert
        assertNull(lastError);
        assertEquals("Signed in", lastSuccess);
        assertEquals(1, goMainCount);
        assertNotNull(MemoryStore.getClerk());
        assertEquals("clerk@mail.com", MemoryStore.getClerk().getEmail());
    }

    /**
     * On forgot clicked navigates.
     */
    @Test
    public void onForgotClicked_navigates() {
        presenter.onForgotClicked();
        assertEquals(1, goForgotCount);
    }

    /**
     * On back clicked navigates.
     */
    @Test
    public void onBackClicked_navigates() {
        presenter.onBackClicked();
        assertEquals(1, goStartCount);
    }

    // ---- helper ----
    private Clerk makeClerk(String email, String password) {
        // TODO: άλλαξε αυτό ώστε να ταιριάζει στο constructor του Clerk σου.
        // π.χ. new Clerk(first,last,email,phone,password) κλπ.
        return new Clerk("John", "Doe", "123133", email, password);
    }
}
