package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy;

/**
 * The interface Pharmacy sign in view.
 */
public interface PharmacySignInView {
    /**
     * Show error.
     *
     * @param message the message
     */
    void showError(String message);

    /**
     * Navigate to main screen.
     */
    void navigateToMainScreen();

    /**
     * Navigate to sign up.
     */
    void navigateToSignUp();
}