package gr.softeng.team09.view.PharmacyViewCompletedOrders;


import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.OrderDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.ReservationDAOMemory;

/**
 * The type Pharmacy view completed orders presenter.
 */
public class PharmacyViewCompletedOrdersPresenter {

    private PharmacyViewCompletedOrdersView view;
    private Pharmacy pharmacy;
    private OrderDAO orderDAO;
    private Inventory inv;
    private List<Order> orders;

    /**
     * Instantiates a new Pharmacy view completed orders presenter.
     */
    public PharmacyViewCompletedOrdersPresenter() {
        this.orderDAO = new OrderDAOMemory();
        this.pharmacy = MemoryStore.getPharmacy();
        this.inv = MemoryStore.getInventory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacyViewCompletedOrdersView view) {
        this.view = view;
    }

    /**
     * Load completed orders.
     */
    public void loadCompletedOrders() {
        if (pharmacy == null || orderDAO == null || view == null) return;

        List<Order> completed =
                orderDAO.findByPharmacyAndState(pharmacy, OrderState.COMPLETED);

        List<Order> completedBack =
                orderDAO.findByPharmacyAndState(pharmacy, OrderState.COMPLETEDBACKORDER);

        orders = new ArrayList<>();

        if (completed != null) {
            orders.addAll(completed);
        }

        if (completedBack != null) {
            orders.addAll(completedBack);
        }

        view.showCompletedOrders(orders);
    }


    /**
     * Gets orders.
     *
     * @return the orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Calculate total double.
     *
     * @param order the order
     * @return the double
     */
    public double calculateTotal(Order order) {
        double sum = 0;
        for (OrderLine l : order.getLines()) {
            sum += l.getProduct().getPriceWithVAT() * l.getQuantity();
        }
        return sum;
    }

    /**
     * Όταν ο χρήστης επιλέγει μια παραγγελία από τη λίστα
     *
     * @param pos Η θέση στη λίστα
     */
    public void onSelected(int pos) {
        if (pos >= 0 && pos < orders.size()) {
            Order selectedOrder = orders.get(pos);
            view.showOrderDetails(selectedOrder);
        }
    }
}