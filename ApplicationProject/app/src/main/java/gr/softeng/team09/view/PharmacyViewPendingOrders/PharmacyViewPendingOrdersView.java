package gr.softeng.team09.view.PharmacyViewPendingOrders;

import java.util.List;
import gr.softeng.team09.domain.Order;

/**
 * The interface Pharmacy view pending orders view.
 */
public interface PharmacyViewPendingOrdersView {

    /**
     * Show pending orders.
     *
     * @param orders the orders
     */
    void showPendingOrders(List<Order> orders);

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);


    /**
     * Show cancellation dialog.
     */
    void showCancellationDialog();

    /**
     * Refresh orders.
     */
    void refreshOrders();
}