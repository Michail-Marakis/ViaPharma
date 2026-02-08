package gr.softeng.team09.view.SignInPharmacist.PharmacistForget;

/**
 * The interface Pharmacist forget view.
 */
public interface PharmacistForgetView {
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
