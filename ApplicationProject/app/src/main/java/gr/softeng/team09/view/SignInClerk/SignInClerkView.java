package gr.softeng.team09.view.SignInClerk;

/**
 * The interface Sign in clerk view.
 */
public interface SignInClerkView {

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
     * Go to clerk main.
     */
    void goToClerkMain();

    /**
     * Go to forgot.
     */
    void goToForgot();

    /**
     * Go to start.
     */
    void goToStart();
}
