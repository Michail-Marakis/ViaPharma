package gr.softeng.team09.dao;

import java.util.Map;
import gr.softeng.team09.domain.Product;

/**
 * The interface Reservation dao.
 */
public interface ReservationDAO {
    /**
     * Put.
     *
     * @param orderId  the order id
     * @param reserved the reserved
     */
    void put(int orderId, Map<Product, Integer> reserved);

    /**
     * Get map.
     *
     * @param orderId the order id
     * @return the map
     */
    Map<Product, Integer> get(int orderId);

    /**
     * Remove.
     *
     * @param orderId the order id
     */
    void remove(int orderId);

    /**
     * Contains boolean.
     *
     * @param orderId the order id
     * @return the boolean
     */
    boolean contains(int orderId);
}
