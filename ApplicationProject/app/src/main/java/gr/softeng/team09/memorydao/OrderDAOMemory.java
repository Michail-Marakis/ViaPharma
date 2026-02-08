package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;

/**
 * The type Order dao memory.
 */
public class OrderDAOMemory implements OrderDAO {

    /**
     * The constant draft_entities.
     */
    public static final List<Order> draft_entities = new ArrayList<>();
    /**
     * The constant completed_entities.
     */
    public static final List<Order> completed_entities = new ArrayList<>();
    /**
     * The constant pending_entities.
     */
    public static final List<Order> pending_entities = new ArrayList<>();



    @Override
    public void saveDraft(Order order) {
        if (order != null && !draft_entities.contains(order)) {
            draft_entities.add(order);
        }
    }

    @Override
    public void savePending(Order order) {
        if (order != null && !pending_entities.contains(order)) {
            pending_entities.add(order);
        }
    }

    @Override
    public void saveCompleted(Order order) {
        if (order != null && !completed_entities.contains(order)) {
            completed_entities.add(order);
        }
    }

    @Override
    public void deleteDraft(Order order) {
        draft_entities.remove(order);
    }

    @Override
    public void deletePending(Order order) {
        pending_entities.remove(order);
    }

    @Override
    public void deleteCompleted(Order order) {
        completed_entities.remove(order);
    }

    @Override
    public Order find(int id) {
        for (Order o : draft_entities) {
            if (o.getId() == id) {
                return o;
            }
        }
        for (Order o : completed_entities) {
            if (o.getId() == id) {
                return o;
            }
        }
        for (Order o : pending_entities) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    @Override
    public List<Order> findAll() {
        List<Order> all = new ArrayList<>();
        all.addAll(draft_entities);
        all.addAll(completed_entities);
        all.addAll(pending_entities);
        return all;
    }

    @Override
    public List<Order> findByState(OrderState state) {
        List<Order> result = new ArrayList<>();
        if (state == OrderState.COMPLETED) {
            for (Order o : completed_entities) {
                if (o.getState() == state) {
                    result.add(o);
                }
            }
        } else if (state == OrderState.DRAFT){
            for (Order o : draft_entities) {
                if (o.getState() == state) {
                    result.add(o);
                }
            }
        } else {
            for (Order o : pending_entities) {
                if (o.getState() == state) {
                    result.add(o);
                }
            }
        }
        return result;
    }

    @Override
    public List<Order> findByPharmacyAndState(Pharmacy pharmacy, OrderState state) {
        List<Order> result = new ArrayList<>();
        if (state == OrderState.COMPLETED || state == OrderState.COMPLETEDBACKORDER) {
            for (Order o : completed_entities) {
                if (o.getPharmacy().equals(pharmacy) && o.getState() == state) {
                    result.add(o);
                }
            }
        } else if (state == OrderState.DRAFT){
            for (Order o : draft_entities) {
                if (o.getPharmacy().equals(pharmacy) && o.getState() == state) {
                    result.add(o);
                }
            }
        } else if (state == OrderState.PENDING){
            for (Order o : pending_entities) {
                if (o.getPharmacy().equals(pharmacy) && o.getState() == state) {
                    result.add(o);
                }
            }
        } else {
        for (Order o : pending_entities) {
            if (o.getPharmacy().equals(pharmacy) && o.getState() == state) {
                result.add(o);
            }
        }
    }

        return result;
    }

    @Override
    public Order findCart(Pharmacy pharmacy) {
        for (Order o : draft_entities) {
            if (o.getPharmacy().equals(pharmacy)
                    && o.getState() == OrderState.DRAFT) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void cancel(Order order) {
        draft_entities.remove(order);
    }

    @Override
    public void complete(Order order) {
        if (draft_entities.contains(order)) {
            order.setState(OrderState.PENDING);
            draft_entities.remove(order);
            pending_entities.add(order);
        } else if (pending_entities.contains(order)) {
            order.setState(OrderState.PENDING);
        }
    }

    @Override
    public int nextId() {
        int maxId = 0;
        for (Order o : draft_entities) {
            if (o.getId() > maxId) maxId = o.getId();
        }
        for (Order o : completed_entities) {
            if (o.getId() > maxId) maxId = o.getId();
        }
        for (Order o : pending_entities) {
            if (o.getId() > maxId) maxId = o.getId();
        }
        return maxId + 1;
    }
}