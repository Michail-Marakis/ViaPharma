package gr.softeng.team09.androidTest.LogicViewsTests.SignUpPharmacist;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;
import gr.softeng.team09.view.SignUpPharmacist.PharmacistSignUpPresenter;
import gr.softeng.team09.view.SignUpPharmacist.PharmacistSignUpView;

import static org.junit.Assert.*;

/**
 * The type Pharmacist sign up presenter test.
 */
public class PharmacistSignUpPresenterTest {

    private PharmacistSignUpPresenter presenter;

    private List<String> errors;
    private String lastSuccess;
    private int finishCount;

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
        finishCount = 0;

        presenter = new PharmacistSignUpPresenter();
        presenter.setView(new PharmacistSignUpView() {
            @Override public void showSuccess(String message) { lastSuccess = message; }
            @Override public void showError(String message) { errors.add(message); }
            @Override public void finishSignUp() { finishCount++; }
        });
    }

    /**
     * On sign up when any field empty shows fill the form.
     */
    @Test
    public void onSignUp_whenAnyFieldEmpty_showsFillTheForm() {
        presenter.onSignUp("", "Doe", "a@b.com", "6900", "pw");

        assertTrue(errors.contains("Fill the form."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * On sign up when invalid email shows invalid email.
     */
    @Test
    public void onSignUp_whenInvalidEmail_showsInvalidEmail() {
        presenter.onSignUp("John", "Doe", "invalidEmail", "6900", "pw");

        assertTrue(errors.contains("Invalid email."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * On sign up when email already used shows email used already.
     */
    @Test
    public void onSignUp_whenEmailAlreadyUsed_showsEmailUsedAlready() {
        // Arrange: υπάρχει ήδη pharmacist με ίδιο email στο DAO
        Pharmacist existing = new Pharmacist("A", "B", "a@b.com", 6900, "pw");
        PharmacistDAOMemory.entities.add(existing);
        MemoryStore.addPharmacist(existing);

        presenter.onSignUp("John", "Doe", "a@b.com", "6900", "pw2");

        assertTrue(errors.contains("Email used already."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
        assertNull(MemoryStore.getPharmacist()); // στο error path δεν γίνεται setPharmacist
    }

    /**
     * On sign up when phone not number shows phone numbers only.
     */
    @Test
    public void onSignUp_whenPhoneNotNumber_showsPhoneNumbersOnly() {
        presenter.onSignUp("John", "Doe", "a@b.com", "abc", "pw");

        assertTrue(errors.contains("Phone number must contain only numbers."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * On sign up when phone not positive shows phone not valid.
     */
    @Test
    public void onSignUp_whenPhoneNotPositive_showsPhoneNotValid() {
        presenter.onSignUp("John", "Doe", "a@b.com", "0", "pw");

        assertTrue(errors.contains("Phone number is not valid."));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
        assertNull(MemoryStore.getPharmacist());
    }

    /**
     * On sign up when valid success sets session and finishes.
     */
    @Test
    public void onSignUp_whenValid_success_setsSession_andFinishes() {
        presenter.onSignUp("  John ", " Doe  ", "  a@b.com  ", " 6900 ", " pw ");

        assertTrue(errors.isEmpty());
        assertEquals("Sign up succeeded!", lastSuccess);
        assertEquals(1, finishCount);

        assertNotNull(MemoryStore.getPharmacist());
        assertEquals("a@b.com", MemoryStore.getPharmacist().getEmail());
    }

    /**
     * On sign up when dao throws exception shows generic sign up error.
     *
     * @throws Exception the exception
     */
    @Test
    public void onSignUp_whenDaoThrowsException_showsGenericSignUpError() throws Exception {
        // inject DAO that throws runtime exception on save
        Field f = PharmacistSignUpPresenter.class.getDeclaredField("dao");
        f.setAccessible(true);

        f.set(presenter, new PharmacistDAOMemory() {
            @Override
            public void save(Pharmacist pharmacist) {
                throw new RuntimeException("boom");
            }
        });

        presenter.onSignUp("John", "Doe", "john@doe.com", "6900", "pw");

        assertEquals(1, errors.size());
        assertEquals("Sign up error: boom", errors.get(0));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
    }
}
