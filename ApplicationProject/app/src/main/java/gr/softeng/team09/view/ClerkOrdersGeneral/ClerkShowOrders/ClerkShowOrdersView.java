package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders;

import java.util.List;

import gr.softeng.team09.domain.Order;

/**
 * The interface Clerk show orders view.
 */
public interface ClerkShowOrdersView {
    /**
     * Show orders.
     *
     * @param ordersDisplay the orders display
     */
    void showOrders(List<String> ordersDisplay);

    /**
     * Show order details.
     *
     * @param order the order
     */
    void showOrderDetails(Order order);

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);

    /**
     * Show message.
     *
     * @param msg the msg
     */
    void showMessage(String msg);

}

