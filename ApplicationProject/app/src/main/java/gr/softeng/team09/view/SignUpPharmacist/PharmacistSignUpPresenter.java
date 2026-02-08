package gr.softeng.team09.view.SignUpPharmacist;

import gr.softeng.team09.dao.PharmacistDAO;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;

/**
 * The type Pharmacist sign up presenter.
 */
public class PharmacistSignUpPresenter {

    private PharmacistSignUpView view;
    private final PharmacistDAO dao;

    /**
     * Instantiates a new Pharmacist sign up presenter.
     */
    public PharmacistSignUpPresenter() {
        this.dao = new PharmacistDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacistSignUpView view) {
        this.view = view;
    }

    /**
     * On sign up.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param phoneStr  the phone str
     * @param password  the password
     */
    public void onSignUp(String firstName,
                         String lastName,
                         String email,
                         String phoneStr,
                         String password) {

        if (view == null) return;

        // trim + null safe
        firstName = safeTrim(firstName);
        lastName = safeTrim(lastName);
        email = safeTrim(email);
        phoneStr = safeTrim(phoneStr);
        password = safeTrim(password);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || phoneStr.isEmpty() || password.isEmpty()) {
            view.showError("Fill the form.");
            return;
        }


        if (!email.contains("@") || !email.contains(".")) {
            view.showError("Invalid email.");
            return;
        }

        if (dao.exists(email)) {
            view.showError("Email used already.");
            return;
        }

        try {
            int phone = Integer.parseInt(phoneStr);
            if (phone <= 0) {
                view.showError("Phone number is not valid.");
                return;
            }

            Pharmacist newPharmacist = new Pharmacist(firstName, lastName, email, phone, password);

            dao.save(newPharmacist);

            MemoryStore.setPharmacist(newPharmacist);

            view.showSuccess("Sign up succeeded!");
            view.finishSignUp();

        } catch (NumberFormatException e) {
            view.showError("Phone number must contain only numbers.");
        } catch (Exception e) {
            view.showError("Sign up error: " + e.getMessage());
        }
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
