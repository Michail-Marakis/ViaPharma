package gr.softeng.team09.androidTest.LogicViewsTests.SignInPharmacy;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.PharmacyDAO;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacySignInPresenter;
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacySignInView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy sign in presenter test.
 */
public class PharmacySignInPresenterTest {

    private PharmacySignInPresenter presenter;

    private List<String> errors;
    private int goMainCount;
    private int goSignUpCount;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset statics
        PharmacyDAOMemory.entitiesPharmacy.clear();
        MemoryStore.Allpharmacy.clear();
        MemoryStore.clearSessionPharma();

        errors = new ArrayList<>();
        goMainCount = 0;
        goSignUpCount = 0;

        presenter = new PharmacySignInPresenter();
        presenter.setView(new PharmacySignInView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void navigateToMainScreen() { goMainCount++; }
            @Override public void navigateToSignUp() { goSignUpCount++; }
        });
    }

    /**
     * On sign in when afm empty shows insert afm and returns.
     */
    @Test
    public void onSignIn_whenAfmEmpty_showsInsertAfmAndReturns() {
        presenter.onSignIn("AnyName", "", "pw");

        assertTrue(errors.contains("Insert AFM."));
        assertEquals(0, goMainCount);
        assertNull(MemoryStore.getPharmacy());
    }

    /**
     * On sign in when no pharmacy found shows no pharmacy found.
     */
    @Test
    public void onSignIn_whenNoPharmacyFound_showsNoPharmacyFound() {
        presenter.onSignIn("AnyName", "111", "pw");

        assertTrue(errors.contains("No pharmacy found with this AFM."));
        assertEquals(0, goMainCount);
        assertNull(MemoryStore.getPharmacy());
    }

    /**
     * On sign in when found sets session and navigates.
     */
    @Test
    public void onSignIn_whenFound_setsSession_andNavigates() {
        // Arrange: βάλε pharmacy στο memory dao storage
        Pharmacist owner = new Pharmacist("A", "B", "a@b.com", 6900, "pw");
        Pharmacy ph = new Pharmacy("PharmA", 111, owner, null, "secret");

        PharmacyDAOMemory.entitiesPharmacy.add(ph);
        MemoryStore.addPharmacy(ph);

        presenter.onSignIn("doesntMatter", "111", "whatever");

        assertTrue(errors.isEmpty());
        assertEquals(1, goMainCount);
        assertNotNull(MemoryStore.getPharmacy());
        assertEquals(111, MemoryStore.getPharmacy().getAfm());
    }

    /**
     * On sign in when dao throws exception shows generic login error.
     *
     * @throws Exception the exception
     */
    @Test
    public void onSignIn_whenDaoThrowsException_showsGenericLoginError() throws Exception {
        // inject PharmacyDAO that throws
        Field f = PharmacySignInPresenter.class.getDeclaredField("pharmacyDAO");
        f.setAccessible(true);

        f.set(presenter, new PharmacyDAO() {
            @Override public void save(Pharmacy pharmacy) { throw new RuntimeException("no"); }
            @Override public Pharmacy findByAfm(String afm) { throw new RuntimeException("boom"); }
            @Override public Pharmacy findByName(String name) { return null; }
            @Override public boolean contains(String afm) { return false; }
        });

        presenter.onSignIn("Any", "111", "pw");

        assertTrue(errors.contains("Error during log in."));
        assertEquals(0, goMainCount);
    }

    /**
     * On sign up clicked navigates to sign up.
     */
    @Test
    public void onSignUpClicked_navigatesToSignUp() {
        presenter.onSignUpClicked();
        assertEquals(1, goSignUpCount);
    }
}
