package gr.softeng.team09.view.SignInPharmacist;

import gr.softeng.team09.dao.PharmacistDAO;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;

/**
 * The type Pharmacist sign in presenter.
 */
public class PharmacistSignInPresenter {
    private PharmacistSignInView view;
    private PharmacistDAO dao;

    /**
     * Instantiates a new Pharmacist sign in presenter.
     */
    public PharmacistSignInPresenter() {
        dao = new PharmacistDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacistSignInView view) {
        this.view = view;
    }

    /**
     * On sign in.
     *
     * @param email    the email
     * @param password the password
     */
    public void onSignIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showError("Username is required");
            view.showError("Password is required");
            return;
        }

        Pharmacist found = dao.findByEmailAndPassword(email, password);

        if (found != null) {
            MemoryStore.setPharmacist(found);
            view.navigateToPharmacy();
        } else {
            view.showError("Wrond email or password.");
        }
    }

    /**
     * On forgot password.
     */
    public void onForgotPassword() {
        view.showForgotPasswordDialog();
    }
}