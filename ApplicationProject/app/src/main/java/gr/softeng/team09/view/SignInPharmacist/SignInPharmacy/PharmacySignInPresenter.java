package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy;

import gr.softeng.team09.dao.PharmacyDAO;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;

/**
 * The type Pharmacy sign in presenter.
 */
public class PharmacySignInPresenter {
    private PharmacySignInView view;
    private PharmacyDAO pharmacyDAO;

    /**
     * Instantiates a new Pharmacy sign in presenter.
     */
    public PharmacySignInPresenter() {
        this.pharmacyDAO = new PharmacyDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacySignInView view) {
        this.view = view;
    }

    /**
     * On sign in.
     *
     * @param name     the name
     * @param afmStr   the afm str
     * @param password the password
     */
    public void onSignIn(String name, String afmStr, String password) {
        if (afmStr.isEmpty()) {
            view.showError("Insert AFM.");
            return;
        }

        try {
            Pharmacy pharmacy = pharmacyDAO.findByAfm(afmStr);

            if (pharmacy == null) {
                view.showError("No pharmacy found with this AFM.");
                return;
            }


            MemoryStore.setPharmacy(pharmacy);

            view.navigateToMainScreen();

        } catch (Exception e) {
            view.showError("Error during log in.");
        }
    }

    /**
     * On sign up clicked.
     */
    public void onSignUpClicked() {
        view.navigateToSignUp();
    }
}