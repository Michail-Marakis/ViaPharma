package gr.softeng.team09.view.SignUpPharmacist;

/**
 * The interface Pharmacist sign up view.
 */
public interface PharmacistSignUpView {
    /**
     * Show success.
     *
     * @param message the message
     */
    void showSuccess(String message);

    /**
     * Show error.
     *
     * @param message the message
     */
    void showError(String message);

    /**
     * Finish sign up.
     */
    void finishSignUp();

}