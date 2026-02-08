package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget;

/**
 * The interface Pharmacy forget view.
 */
public interface PharmacyForgetView {
    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);

    /**
     * Show success.
     *
     * @param msg the msg
     */
    void showSuccess(String msg);

    /**
     * Go to starting screen.
     */
    void goToStartingScreen();
}
