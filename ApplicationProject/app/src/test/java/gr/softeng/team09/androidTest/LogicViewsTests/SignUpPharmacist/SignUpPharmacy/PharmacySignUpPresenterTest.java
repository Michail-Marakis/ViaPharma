package gr.softeng.team09.androidTest.LogicViewsTests.SignUpPharmacist.SignUpPharmacy;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;
import gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy.PharmacySignUpPresenter;
import gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy.PharmacySignUpView;

import static org.junit.Assert.*;

/**
 * The type Pharmacy sign up presenter test.
 */
public class PharmacySignUpPresenterTest {

    private PharmacySignUpPresenter presenter;

    private List<String> errors;
    private String lastSuccess;
    private int finishCount;

    private Pharmacist pharmacist;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset static memory state
        PharmacyDAOMemory.entitiesPharmacy.clear();
        MemoryStore.Allpharmacy.clear();
        MemoryStore.clearSessionPharma(); // clears pharmacist+pharmacy in MemoryStore

        errors = new ArrayList<>();
        lastSuccess = null;
        finishCount = 0;

        presenter = new PharmacySignUpPresenter();
        presenter.setView(new PharmacySignUpView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void finishSignUp() { finishCount++; }
        });

        pharmacist = new Pharmacist("A", "B", "pharm@mail.com", 690000000, "pw");
        MemoryStore.setPharmacist(pharmacist);
    }

    /**
     * Check pharmacist logged in when no pharmacist shows session error.
     */
    @Test
    public void checkPharmacistLoggedIn_whenNoPharmacist_showsSessionError() {
        MemoryStore.setPharmacist(null);

        presenter.checkPharmacistLoggedIn();

        assertTrue(errors.contains("Session Error. Please log in again as a pharmacist."));
    }

    /**
     * On sign up when owner null shows no connected pharmacist.
     */
    @Test
    public void onSignUp_whenOwnerNull_showsNoConnectedPharmacist() {
        MemoryStore.setPharmacist(null);

        presenter.onSignUp("Pharm", "123", "Street", "10", "Athens", "11111", "pw");

        assertTrue(errors.contains("No connected pharmacist."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
    }

    /**
     * On sign up when any field empty shows complete all fields.
     */
    @Test
    public void onSignUp_whenAnyFieldEmpty_showsCompleteAllFields() {
        presenter.onSignUp(null, "123", "Street", "10", "Athens", "11111", "pw");

        assertTrue(errors.contains("Complete all fields."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
    }

    /**
     * On sign up when afm already exists shows already pharmacy with this afm.
     */
    @Test
    public void onSignUp_whenAfmAlreadyExists_showsAlreadyPharmacyWithThisAFM() {
        // Arrange: βάζουμε ήδη pharmacy με AFM 123
        Pharmacy existing = new Pharmacy("Old", 123, pharmacist, new Address("S", 1, "R", "P"), "pw");
        PharmacyDAOMemory.entitiesPharmacy.add(existing);
        MemoryStore.addPharmacy(existing);

        presenter.onSignUp("New", "123", "Street", "10", "Athens", "11111", "pw");

        assertTrue(errors.contains("Already a pharmacy with this AFM."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
    }

    /**
     * On sign up when afm or number not numbers shows number format error.
     */
    @Test
    public void onSignUp_whenAfmOrNumberNotNumbers_showsNumberFormatError() {
        presenter.onSignUp("Pharm", "abc", "Street", "10", "Athens", "11111", "pw");
        assertTrue(errors.contains("AFM and street number must be numbers."));
    }

    /**
     * On sign up when afm not positive shows afm positive error.
     */
    @Test
    public void onSignUp_whenAfmNotPositive_showsAfmPositiveError() {
        presenter.onSignUp("Pharm", "0", "Street", "10", "Athens", "11111", "pw");
        assertTrue(errors.contains("AFM must be a positive number."));
    }

    /**
     * On sign up when street number not positive shows street number positive error.
     */
    @Test
    public void onSignUp_whenStreetNumberNotPositive_showsStreetNumberPositiveError() {
        presenter.onSignUp("Pharm", "123", "Street", "0", "Athens", "11111", "pw");
        assertTrue(errors.contains("Street number must be a positive number"));
    }

    /**
     * On sign up when valid success sets memory store and finishes.
     */
    @Test
    public void onSignUp_whenValid_success_setsMemoryStoreAndFinishes() {
        presenter.onSignUp("  MyPharmacy  ", " 123 ", " Main ", " 10 ", " Athens ", " 11111 ", " pw ");

        assertTrue(errors.isEmpty());
        assertEquals("Successful pharmacy sign up!", lastSuccess);
        assertEquals(1, finishCount);

        assertNotNull(MemoryStore.getPharmacy());
        assertEquals(123, MemoryStore.getPharmacy().getAfm());
        assertEquals("MyPharmacy", MemoryStore.getPharmacy().getName());
    }

    /**
     * On sign up when dao throws exception shows generic error.
     *
     * @throws Exception the exception
     */
    @Test
    public void onSignUp_whenDaoThrowsException_showsGenericError() throws Exception {
        // Arrange
        PharmacySignUpPresenter presenter = new PharmacySignUpPresenter();

        List<String> errors = new ArrayList<>();

        presenter.setView(new PharmacySignUpView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void showSuccess(String msg) {}
            @Override public void finishSignUp() {}
        });

        Pharmacist pharmacist = new Pharmacist(
                "A", "B", "pharm@mail.com", 690000000, "pw"
        );
        MemoryStore.setPharmacist(pharmacist);

        // ---- reflection injection ----
        java.lang.reflect.Field daoField =
                PharmacySignUpPresenter.class.getDeclaredField("pharmacyDAO");
        daoField.setAccessible(true);

        daoField.set(presenter, new PharmacyDAOMemory() {
            @Override
            public void save(Pharmacy pharmacy) {
                throw new RuntimeException("boom");
            }
        });

        // Act
        presenter.onSignUp(
                "Pharm", "123", "Street", "10", "Athens", "11111", "pw"
        );

        // Assert
        assertEquals(1, errors.size());
        assertEquals("Error during sign up: boom", errors.get(0));
    }

}
