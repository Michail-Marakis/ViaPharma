package gr.softeng.team09.view.SignUpClerk;

/**
 * The interface Sign up clerk view.
 */
public interface SignUpClerkView {

    /**
     * Show success.
     *
     * @param msg the msg
     */
    void showSuccess(String msg);

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);

    /**
     * Finish sign up.
     */
    void finishSignUp();
}
