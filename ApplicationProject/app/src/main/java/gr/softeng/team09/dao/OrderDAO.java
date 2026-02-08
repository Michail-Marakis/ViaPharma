package gr.softeng.team09.dao;

import java.util.List;

import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;

/**
 * The interface Order dao.
 */
public interface OrderDAO {

    /**
     * Save draft.
     *
     * @param order the order
     */
    void saveDraft(Order order);

    /**
     * Save pending.
     *
     * @param order the order
     */
    void savePending(Order order);

    /**
     * Save completed.
     *
     * @param order the order
     */
    void saveCompleted(Order order);

    /**
     * Delete draft.
     *
     * @param order the order
     */
    void deleteDraft(Order order);

    /**
     * Delete pending.
     *
     * @param order the order
     */
    void deletePending(Order order);

    /**
     * Delete completed.
     *
     * @param order the order
     */
    void deleteCompleted(Order order);

    /**
     * Find order.
     *
     * @param id the id
     * @return the order
     */
    Order find(int id);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Order> findAll();

    /**
     * Find by state list.
     *
     * @param state the state
     * @return the list
     */
    List<Order> findByState(OrderState state);

    /**
     * Find by pharmacy and state list.
     *
     * @param pharmacy the pharmacy
     * @param state    the state
     * @return the list
     */
    List<Order> findByPharmacyAndState(Pharmacy pharmacy, OrderState state);

    /**
     * Find cart order.
     *
     * @param pharmacy the pharmacy
     * @return the order
     */
    Order findCart(Pharmacy pharmacy); // DRAFT

    /**
     * Cancel.
     *
     * @param order the order
     */
    void cancel(Order order);          // CANCELED

    /**
     * Complete.
     *
     * @param order the order
     */
    void complete(Order order);        // COMPLETED

    /**
     * Next id int.
     *
     * @return the int
     */
    int nextId();
}