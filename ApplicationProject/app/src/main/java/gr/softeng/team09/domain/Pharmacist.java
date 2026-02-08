package gr.softeng.team09.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gr.softeng.team09.memorydao.MemoryStore;
import kotlin.contracts.Returns;

/**
 * The type Pharmacist.
 */
public class Pharmacist {

    private final String firstName;
    private final String lastName;

    //1 pharmacist can have many pharmacies
    private List<Pharmacy> pharmacies = new ArrayList<>();
    private final String email;
    private final int phoneNumber;
    /**
     * The Clerk.
     */
    Clerk clerk;
    private  String SignInCode;

    /**
     * Instantiates a new Pharmacist.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param email       the email
     * @param phoneNumber the phone number
     * @param SignInCode  the sign in code
     */
    public Pharmacist(String firstName, String lastName, String email, int phoneNumber, String SignInCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.SignInCode = SignInCode;
    }

    /**
     * Set code.
     *
     * @param code the code
     */
    public void SetCode(String code){
        this.SignInCode = code;
    }


    /**
     * Get password string.
     *
     * @return the string
     */
    public String getPassword(){return SignInCode;}

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() { return firstName; }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() { return lastName; }

    /**
     * Get phone number int.
     *
     * @return the int
     */
    public int getPhoneNumber(){return phoneNumber;}

    /**
     * Get email string.
     *
     * @return the string
     */
    public String getEmail(){return email;}

    /**
     * Gets pharmacies.
     *
     * @return the pharmacies
     */
    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }


    /**
     * Add pharmacy.
     *
     * @param pharmacy the pharmacy
     */
    public void addPharmacy(Pharmacy pharmacy) {
        if (!pharmacies.contains(pharmacy)) {
            pharmacies.add(pharmacy);
            pharmacy.setOwner(this);
        }
    }


}
