package gr.softeng.team09.view.PharmacyViewCompletedOrders;

import java.util.List;
import gr.softeng.team09.domain.Order;

/**
 * The interface Pharmacy view completed orders view.
 */
public interface PharmacyViewCompletedOrdersView {

    /**
     * Show completed orders.
     *
     * @param orders the orders
     */
    void showCompletedOrders(List<Order> orders);

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);


    /**
     * Show order details.
     *
     * @param selectedOrder the selected order
     */
    void showOrderDetails(Order selectedOrder);
}
