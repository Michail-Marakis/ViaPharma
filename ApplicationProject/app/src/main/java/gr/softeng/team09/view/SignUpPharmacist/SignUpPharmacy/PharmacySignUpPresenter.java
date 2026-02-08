package gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy;

import gr.softeng.team09.dao.PharmacyDAO;
import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacyDAOMemory;

/**
 * The type Pharmacy sign up presenter.
 */
public class PharmacySignUpPresenter {

    private PharmacySignUpView view;
    private final PharmacyDAO pharmacyDAO;

    /**
     * Instantiates a new Pharmacy sign up presenter.
     */
    public PharmacySignUpPresenter() {
        this.pharmacyDAO = new PharmacyDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacySignUpView view) {
        this.view = view;
    }

    /**
     * Check pharmacist logged in.
     */
    public void checkPharmacistLoggedIn() {
        if (view == null) return;

        if (MemoryStore.getPharmacist() == null) {
            view.showError("Session Error. Please log in again as a pharmacist.");
        }
    }

    /**
     * On sign up.
     *
     * @param name      the name
     * @param afmStr    the afm str
     * @param street    the street
     * @param numberStr the number str
     * @param region    the region
     * @param postal    the postal
     * @param password  the password
     */
    public void onSignUp(String name,
                         String afmStr,
                         String street,
                         String numberStr,
                         String region,
                         String postal,
                         String password) {

        if (view == null) return;

        Pharmacist owner = MemoryStore.getPharmacist();
        if (owner == null) {
            view.showError("No connected pharmacist.");
            return;
        }

        name = safeTrim(name);
        afmStr = safeTrim(afmStr);
        street = safeTrim(street);
        numberStr = safeTrim(numberStr);
        region = safeTrim(region);
        postal = safeTrim(postal);
        password = safeTrim(password);

        if (name.isEmpty() || afmStr.isEmpty() || street.isEmpty() ||
                numberStr.isEmpty() || region.isEmpty() || postal.isEmpty() || password.isEmpty()) {
            view.showError("Complete all fields.");
            return;
        }

        if (pharmacyDAO.contains(afmStr)) {
            view.showError("Already a pharmacy with this AFM.");
            return;
        }

        try {
            int afm = Integer.parseInt(afmStr);
            int number = Integer.parseInt(numberStr);

            if (afm <= 0) {
                view.showError("AFM must be a positive number.");
                return;
            }
            if (number <= 0) {
                view.showError("Street number must be a positive number");
                return;
            }

            Address address = new Address(street, number, region, postal);

            Pharmacy newPharmacy = new Pharmacy(name, afm, owner, address, password);

            pharmacyDAO.save(newPharmacy);


            MemoryStore.setPharmacy(newPharmacy);

            view.showSuccess("Successful pharmacy sign up!");
            view.finishSignUp();

        } catch (NumberFormatException e) {
            view.showError("AFM and street number must be numbers.");
        } catch (Exception e) {
            view.showError("Error during sign up: " + e.getMessage());
        }
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
