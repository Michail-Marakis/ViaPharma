package gr.softeng.team09.view.SignInClerk;

import gr.softeng.team09.dao.ClerkDAO;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Sign in clerk presenter.
 */
public class SignInClerkPresenter {

    private SignInClerkView view;
    private final ClerkDAO dao;

    /**
     * Instantiates a new Sign in clerk presenter.
     */
    public SignInClerkPresenter() {
        dao = new ClerkDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(SignInClerkView view) {
        this.view = view;
    }

    /**
     * On sign in.
     *
     * @param usernameOrEmailOrPhone the username or email or phone
     * @param password               the password
     */
    public void onSignIn(String usernameOrEmailOrPhone, String password) {

        if (usernameOrEmailOrPhone == null || usernameOrEmailOrPhone.trim().isEmpty()) {
            view.showError("Username is required");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            view.showError("Password is required");
            return;
        }
        Clerk clerk = dao.findByEmailAndPassword(usernameOrEmailOrPhone.trim(), password.trim());


        if (clerk == null) {
            view.showError("Wrong credentials");
            return;
        }

        view.showSuccess("Signed in");
        MemoryStore.setClerk(clerk);
        view.goToClerkMain();
    }

    /**
     * On forgot clicked.
     */
    public void onForgotClicked() {
        view.goToForgot();
    }

    /**
     * On back clicked.
     */
    public void onBackClicked() {
        view.goToStart();
    }
}
