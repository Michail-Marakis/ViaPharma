package gr.softeng.team09.view.SignUpClerk;

import gr.softeng.team09.dao.ClerkDAO;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Sign up clerk presenter.
 */
public class SignUpClerkPresenter {

    private SignUpClerkView view;
    private final ClerkDAO dao;

    /**
     * Instantiates a new Sign up clerk presenter.
     */
    public SignUpClerkPresenter() {
        dao = new ClerkDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(SignUpClerkView view) {
        this.view = view;
    }

    /**
     * On sign up.
     *
     * @param name     the name
     * @param surname  the surname
     * @param phone    the phone
     * @param email    the email
     * @param password the password
     */
    public void onSignUp(String name, String surname, String phone,
                         String email, String password) {

        if (name.isEmpty() || surname.isEmpty() ||
                phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.showError("Fill all fields");
            return;
        }

        if (dao.exists(email)) {
            view.showError("Email already exists");
            return;
        }

        Clerk clerk = new Clerk(name, surname, phone, email, password);
        MemoryStore.setClerk(clerk);

        dao.save(clerk);

        view.showSuccess("Clerk registered successfully");
        view.finishSignUp();
    }
}
