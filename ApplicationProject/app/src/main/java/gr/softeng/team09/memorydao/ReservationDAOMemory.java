package gr.softeng.team09.memorydao;

import java.util.HashMap;
import java.util.Map;

import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Product;

/**
 * The type Reservation dao memory.
 */
public class ReservationDAOMemory implements ReservationDAO {

    private static final Map<Integer, Map<Product, Integer>> byOrderId = new HashMap<>();

    @Override
    public void put(int orderId, Map<Product, Integer> reserved) {
        if (reserved == null) reserved = new HashMap<>();
        byOrderId.put(orderId, new HashMap<>(reserved));
    }

    @Override
    public Map<Product, Integer> get(int orderId) {
        Map<Product, Integer> res = byOrderId.get(orderId);
        return res == null ? null : new HashMap<>(res);
    }

    @Override
    public void remove(int orderId) {
        byOrderId.remove(orderId);
    }

    @Override
    public boolean contains(int orderId) {
        return byOrderId.containsKey(orderId);
    }
}
