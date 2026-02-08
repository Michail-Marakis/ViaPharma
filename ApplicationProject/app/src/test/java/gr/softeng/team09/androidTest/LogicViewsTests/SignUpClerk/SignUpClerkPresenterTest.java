package gr.softeng.team09.androidTest.LogicViewsTests.SignUpClerk;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.SignUpClerk.SignUpClerkPresenter;
import gr.softeng.team09.view.SignUpClerk.SignUpClerkView;

import static org.junit.Assert.*;

/**
 * The type Sign up clerk presenter test.
 */
public class SignUpClerkPresenterTest {

    private SignUpClerkPresenter presenter;

    private List<String> errors;
    private String lastSuccess;
    private int finishCount;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // reset static state
        ClerkDAOMemory.entitiesClerk.clear();
        MemoryStore.AllClerk.clear();
        MemoryStore.clearSessionClerk();

        errors = new ArrayList<>();
        lastSuccess = null;
        finishCount = 0;

        presenter = new SignUpClerkPresenter();
        presenter.setView(new SignUpClerkView() {
            @Override public void showError(String msg) { errors.add(msg); }
            @Override public void showSuccess(String msg) { lastSuccess = msg; }
            @Override public void finishSignUp() { finishCount++; }
        });
    }

    /**
     * On sign up when any field empty shows error and returns.
     */
    @Test
    public void onSignUp_whenAnyFieldEmpty_showsErrorAndReturns() {
        presenter.onSignUp("", "Doe", "123", "a@b.com", "pw");

        assertTrue(errors.contains("Fill all fields"));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);
        assertNull(MemoryStore.getClerk());
    }

    /**
     * On sign up when email exists shows error and returns.
     */
    @Test
    public void onSignUp_whenEmailExists_showsErrorAndReturns() {
        // Arrange: βάζουμε ήδη clerk με ίδιο email στο DAO memory store
        Clerk existing = new Clerk("John", "Doe", "123", "a@b.com", "pw");
        ClerkDAOMemory.entitiesClerk.add(existing);
        MemoryStore.addClerk(existing);

        presenter.onSignUp("Jane", "Doe", "999", "a@b.com", "pw2");

        assertTrue(errors.contains("Email already exists"));
        assertNull(lastSuccess);
        assertEquals(0, finishCount);

        // δεν πρέπει να άλλαξε session clerk σε νέο χρήστη
        // (μένει null γιατί στο setUp κάναμε clearSessionClerk και στο error path δεν κάνει setClerk)
        assertNull(MemoryStore.getClerk());
    }

    /**
     * On sign up when valid and email new saves sets session shows success and finishes.
     */
    @Test
    public void onSignUp_whenValidAndEmailNew_saves_setsSession_showsSuccess_andFinishes() {
        presenter.onSignUp("John", "Doe", "123", "a@b.com", "pw");

        assertTrue(errors.isEmpty());
        assertEquals("Clerk registered successfully", lastSuccess);
        assertEquals(1, finishCount);

        assertNotNull(MemoryStore.getClerk());
        assertEquals("a@b.com", MemoryStore.getClerk().getEmail());

        // optional: επιβεβαίωση ότι όντως σώθηκε στο memory DAO storage
        assertTrue(ClerkDAOMemory.entitiesClerk.contains(MemoryStore.getClerk()));
    }
}
