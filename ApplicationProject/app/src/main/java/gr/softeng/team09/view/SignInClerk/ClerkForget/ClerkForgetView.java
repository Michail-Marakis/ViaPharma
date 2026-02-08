package gr.softeng.team09.view.SignInClerk.ClerkForget;

/**
 * The interface Clerk forget view.
 */
public interface ClerkForgetView {
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
