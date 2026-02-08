package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget;

import java.util.Objects;

import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;


/**
 * The type Pharmacy forget presenter.
 */
public class PharmacyForgetPresenter {
    /**
     * The View.
     */
    PharmacyForgetView view;
    private String NewPassword = null;
    private String NewPasswordConfirmation = null;

    private int username = -1221212;

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacyForgetView view) {
        this.view = view;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = Integer.parseInt(username);
    }

    /**
     * Sets new password.
     *
     * @param newPassword the new password
     */
    public void setNewPassword(String newPassword) {
        this.NewPassword = newPassword;
    }

    /**
     * Sets new password confirmation.
     *
     * @param Confirmation the confirmation
     */
    public void setNewPasswordConfirmation(String Confirmation) {
        this.NewPasswordConfirmation = Confirmation;
    }

    /**
     * Authentication session.
     */
    public void authenticationSession() {
        if (username == -1221212 || NewPassword == null || NewPasswordConfirmation == null) {
            view.showError("Fill in the form");
        }

        Pharmacy tofound = MemoryStore.getByAFM(username);
        if (PharmacyDAOMemory.entitiesPharmacy.contains(tofound)) {
            if (Objects.equals(NewPassword, NewPasswordConfirmation)) {
                tofound.setPassword(NewPassword);
                view.showSuccess("Password changed successfully");
                resetValues();
                view.goToStartingScreen();
            } else {
                view.showError("Confirmation password is not correct");
            }
        } else {
            view.showError("User not identified");
        }

    }

    private void resetValues() {
        this.username = -1221212;
        this.NewPassword = null;
        this.NewPasswordConfirmation = null;
    }
}