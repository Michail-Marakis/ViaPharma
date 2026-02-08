package gr.softeng.team09.dao;

import gr.softeng.team09.domain.Clerk;

/**
 * The interface Clerk dao.
 */
public interface ClerkDAO {

    /**
     * Save.
     *
     * @param clerk the clerk
     */
    void save(Clerk clerk);

    /**
     * Find by email and password clerk.
     *
     * @param email    the email
     * @param password the password
     * @return the clerk
     */
    Clerk findByEmailAndPassword(String email, String password);

    /**
     * Exists boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean exists(String email);
}
