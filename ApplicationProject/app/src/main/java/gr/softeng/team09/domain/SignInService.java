package gr.softeng.team09.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Sign in service.
 */
public class SignInService {
    private List<Pharmacist> users = new ArrayList<>();
    private Clerk InventoryOwner;
    /**
     * The Clerk code.
     */
    int ClerkCode;

    /**
     * Get users list.
     *
     * @return the list
     */
    public List<Pharmacist> getUsers(){
        return users;
    }

    /**
     * Sign up pharmacist.
     *
     * @param pharmacist the pharmacist
     */
//Εγγραφή φαρμακοποιού χωρίς να απαιτείται φαρμακείο
    public void signUpPharmacist(Pharmacist pharmacist) {
        if (!users.contains(pharmacist)) {
            users.add(pharmacist);
        }
    }

    /**
     * Add pharmacy to pharmacist.
     *
     * @param pharmacist the pharmacist
     * @param pharmacy   the pharmacy
     */
//Προσθήκη φαρμακείου σε ήδη εγγεγραμμένο φαρμακοποιό
    public void addPharmacyToPharmacist(Pharmacist pharmacist, Pharmacy pharmacy) {
        if (pharmacist == null || pharmacy == null) return;
        pharmacist.addPharmacy(pharmacy);
    }

    /**
     * Sign in pharmacist pharmacist.
     *
     * @param email    the email
     * @param password the password
     * @return the pharmacist
     */
//Sign-in φαρμακοποιού
    public Pharmacist signInPharmacist(String email, String password) {
        for (Pharmacist p : users) {
            if (p.getEmail().equals(email) &&
                    p.getPassword().equals(password)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Select pharmacy pharmacy.
     *
     * @param pharmacist   the pharmacist
     * @param pharmacyName the pharmacy name
     * @param pharmacyVat  the pharmacy vat
     * @return the pharmacy
     */
//Επιλογή φαρμακείου sign in
    public Pharmacy selectPharmacy(Pharmacist pharmacist, String pharmacyName, int pharmacyVat) {
        if (pharmacist == null) return null;
        for (Pharmacy ph : pharmacist.getPharmacies()) {
            if (ph.getAfm() == pharmacyVat && ph.getName().equals(pharmacyName)) {
                return ph;
            }
        }
        return null;
    }

    /**
     * Get clerk code int.
     *
     * @return the int
     */
    public int getClerkCode(){
        return ClerkCode;
    }

    /**
     * Clerk sign in clerk.
     *
     * @param code the code
     * @return the clerk
     */
    public Clerk ClerkSignIn(int code){
        if(code == ClerkCode) return InventoryOwner;
        return null;
    }

    /**
     * Sign up owner.
     *
     * @param clerk     the clerk
     * @param code      the code
     * @param inventory the inventory
     */
    public void SignUpOwner(Clerk clerk, int code, Inventory inventory){
        if(InventoryOwner == null) InventoryOwner = clerk;
        ClerkCode = code;
        //if(inventory.getOwner() == null) inventory.owner = InventoryOwner;

    }

    /**
     * Get inventory owner clerk.
     *
     * @return the clerk
     */
    public Clerk getInventoryOwner(){
        return InventoryOwner;
    }
}

