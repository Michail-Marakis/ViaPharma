package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;

/**
 * The type Memory store.
 */
public class MemoryStore {

    private static Pharmacy activePharmacy;
    private static Pharmacist loggedInPharmacist;

    private static Clerk loggedInClerk;

    private static Inventory inventory;
    private static Order activeOrder;

    /**
     * The constant Allpharmacist.
     */
    public static List<Pharmacist> Allpharmacist = new ArrayList<>();
    /**
     * The constant Allpharmacy.
     */
    public static List<Pharmacy> Allpharmacy = new ArrayList<>();
    /**
     * The constant AllClerk.
     */
    public static List<Clerk> AllClerk = new ArrayList<>();


    /**
     * Sets pharmacy.
     *
     * @param pharmacy the pharmacy
     */
// ---------------- Pharmacist / Pharmacy ----------------
    public static void setPharmacy(Pharmacy pharmacy) {
        activePharmacy = pharmacy;
    }

    /**
     * Gets pharmacy.
     *
     * @return the pharmacy
     */
    public static Pharmacy getPharmacy() {
        return activePharmacy;
    }

    /**
     * Add pharmacy.
     *
     * @param p the p
     */
    public static void addPharmacy(Pharmacy p){
        Allpharmacy.add(p);
    }

    /**
     * Sets pharmacist.
     *
     * @param pharmacist the pharmacist
     */
    public static void setPharmacist(Pharmacist pharmacist) {
        loggedInPharmacist = pharmacist;
    }

    /**
     * Gets pharmacist.
     *
     * @return the pharmacist
     */
    public static Pharmacist getPharmacist() {
        return loggedInPharmacist;
    }

    /**
     * Add pharmacist.
     *
     * @param p the p
     */
    public static void addPharmacist(Pharmacist p){
        Allpharmacist.add(p);
    }

    /**
     * Get by email pharmacist.
     *
     * @param email the email
     * @return the pharmacist
     */
    public static Pharmacist getByEmail(String email){
        for(Pharmacist p: Allpharmacist){
            if(p.getEmail().equals(email)){
                return p;
            }
        }
        return null;
    }

    /**
     * Get by afm pharmacy.
     *
     * @param AFM the afm
     * @return the pharmacy
     */
    public static Pharmacy getByAFM(int AFM){
        for(Pharmacy p: Allpharmacy){
            if(p.getAfm() == AFM){
                return p;
            }
        }
        return null;
    }

    /**
     * Sets clerk.
     *
     * @param clerk the clerk
     */
// ---------------- Clerk ----------------
    public static void setClerk(Clerk clerk) {
        loggedInClerk = clerk;

        if (inventory != null) {
            inventory.setOwner(clerk);
        }
    }

    /**
     * Gets clerk.
     *
     * @return the clerk
     */
    public static Clerk getClerk() {
        return loggedInClerk;
    }

    /**
     * Get by email clerk clerk.
     *
     * @param email the email
     * @return the clerk
     */
    public static Clerk getByEmailClerk(String email){
        for(Clerk c: AllClerk){
            if(c.getEmail().equals(email)){
                return c;
            }
        }
        return null;
    }

    /**
     * Add clerk.
     *
     * @param c the c
     */
    public static void addClerk(Clerk c){
        AllClerk.add(c);
    }


    /**
     * Init inventory if needed.
     *
     * @param owner            the owner
     * @param batchDAO         the batch dao
     * @param inventoryLineDAO the inventory line dao
     * @param backorderDAO     the backorder dao
     * @param reservationDAO   the reservation dao
     */
// ---------------- Inventory ----------------
    public static void initInventoryIfNeeded(Clerk owner,
                                             BatchDAO batchDAO,
                                             InventoryLineDAO inventoryLineDAO,
                                             BackorderDAO backorderDAO,
                                             ReservationDAO reservationDAO) {

        if (inventory == null) {
            inventory = new Inventory(owner, batchDAO, inventoryLineDAO, backorderDAO, reservationDAO);
        } else {
            inventory.setOwner(owner);
        }
    }

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public static Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets active order.
     *
     * @param order the order
     */
// ----------------order--------------------------------
    public static void setActiveOrder(Order order) {
        activeOrder = order;
    }

    /**
     * Gets active order.
     *
     * @return the active order
     */
    public static Order getActiveOrder() {
        return activeOrder;
    }

    /**
     * Clear active order.
     */
    public static void clearActiveOrder() {
        activeOrder = null;
    }

    /**
     * Clear session pharma.
     */
// ---------------- Logout / clear ----------------
    public static void clearSessionPharma(){
        activePharmacy = null;
        loggedInPharmacist = null;
    }

    /**
     * Clear session clerk.
     */
    public static void clearSessionClerk(){
        loggedInClerk = null;
    }
}
