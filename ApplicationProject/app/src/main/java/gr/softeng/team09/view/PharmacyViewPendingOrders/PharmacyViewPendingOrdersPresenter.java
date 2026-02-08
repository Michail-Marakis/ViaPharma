package gr.softeng.team09.view.PharmacyViewPendingOrders;

import java.util.List;

import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.OrderDAOMemory;

/**
 * The type Pharmacy view pending orders presenter.
 */
public class PharmacyViewPendingOrdersPresenter {

    private PharmacyViewPendingOrdersView view;
    private Pharmacy pharmacy;
    private OrderDAO orderDAO;
    private Inventory inv;
    private Order orderPendingCancellation;
    private List<Order> orders;

    /**
     * Instantiates a new Pharmacy view pending orders presenter.
     */
    public PharmacyViewPendingOrdersPresenter() {
        this.orderDAO = new OrderDAOMemory();
        this.pharmacy = MemoryStore.getPharmacy();
        this.inv = MemoryStore.getInventory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacyViewPendingOrdersView view) {
        this.view = view;
    }

    /**
     * Load pending orders.
     */
    public void loadPendingOrders() {
        if (pharmacy == null) return;

        this.orders = orderDAO.findByPharmacyAndState(pharmacy, OrderState.PENDING);
        view.showPendingOrders(this.orders);
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
     * Initiate cancellation.
     *
     * @param order the order
     */
    public void initiateCancellation(Order order) {
        this.orderPendingCancellation = order;
        view.showCancellationDialog();
    }

    /**
     * Submit order.
     *
     * @param confirmed the confirmed
     */
    public void submitOrder(boolean confirmed) {
        if (!confirmed || orderPendingCancellation == null) {
            orderPendingCancellation = null;
            return;
        }
        view.showError("Order #"+ orderPendingCancellation.getId()+ " asked to get canceled");
        pharmacy.deleteSubmittedOrders(orderPendingCancellation, inv);
        orderPendingCancellation.setState(OrderState.TOCANCEL);
        orderDAO.cancel(orderPendingCancellation);
        loadPendingOrders();


        orderPendingCancellation = null;
    }
}