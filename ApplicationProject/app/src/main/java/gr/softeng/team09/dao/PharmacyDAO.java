package gr.softeng.team09.dao;

import gr.softeng.team09.domain.Pharmacy;

/**
 * The interface Pharmacy dao.
 */
public interface PharmacyDAO {
    /**
     * Save.
     *
     * @param pharmacy the pharmacy
     */
    void save(Pharmacy pharmacy);

    /**
     * Find by afm pharmacy.
     *
     * @param afm the afm
     * @return the pharmacy
     */
    Pharmacy findByAfm(String afm);

    /**
     * Find by name pharmacy.
     *
     * @param name the name
     * @return the pharmacy
     */
    Pharmacy findByName(String name);

    /**
     * Contains boolean.
     *
     * @param afm the afm
     * @return the boolean
     */
    boolean contains(String afm);
}