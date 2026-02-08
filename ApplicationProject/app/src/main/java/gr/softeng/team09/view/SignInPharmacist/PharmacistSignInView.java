package gr.softeng.team09.view.SignInPharmacist;

/**
 * The interface Pharmacist sign in view.
 */
public interface PharmacistSignInView {
    /**
     * Show error.
     *
     * @param message the message
     */
    void showError(String message);

    /**
     * Navigate to pharmacy.
     */
    void navigateToPharmacy();

    /**
     * Show forgot password dialog.
     */
    void showForgotPasswordDialog();
}