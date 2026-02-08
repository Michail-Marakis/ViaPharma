package gr.softeng.team09.dao;

import gr.softeng.team09.domain.Pharmacist;

/**
 * The interface Pharmacist dao.
 */
public interface PharmacistDAO {
    /**
     * Save.
     *
     * @param pharmacist the pharmacist
     */
    void save(Pharmacist pharmacist);

    /**
     * Find by email and password pharmacist.
     *
     * @param email    the email
     * @param password the password
     * @return the pharmacist
     */
    Pharmacist findByEmailAndPassword(String email, String password);

    /**
     * Exists boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean exists(String email);
}