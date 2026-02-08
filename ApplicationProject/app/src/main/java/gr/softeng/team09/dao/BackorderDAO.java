package gr.softeng.team09.dao;

import java.util.List;
import gr.softeng.team09.domain.Backorder;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;

/**
 * The interface Backorder dao.
 */
public interface BackorderDAO {

    /**
     * Enqueue.
     *
     * @param backorder the backorder
     */
    void enqueue(Backorder backorder);     // FIFO insert

    /**
     * Poll backorder.
     *
     * @return the backorder
     */
    Backorder poll();                      // FIFO remove

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Backorder> findAll();             // history

    /**
     * Find by state list.
     *
     * @param state the state
     * @return the list
     */
    List<Backorder> findByState(OrderState state);

    /**
     * Find by pharmacy list.
     *
     * @param pharmacy the pharmacy
     * @return the list
     */
    List<Backorder> findByPharmacy(Pharmacy pharmacy);

    /**
     * Find backorder.
     *
     * @param id the id
     * @return the backorder
     */
    Backorder find(int id);

    /**
     * Save.
     *
     * @param backorder the backorder
     */
    void save(Backorder backorder);        // upsert/update

    /**
     * Sets flag.
     *
     * @param flag the flag
     */
    void setFlag(boolean flag);

    /**
     * Gets flag.
     *
     * @return the flag
     */
    boolean getFlag();
}
