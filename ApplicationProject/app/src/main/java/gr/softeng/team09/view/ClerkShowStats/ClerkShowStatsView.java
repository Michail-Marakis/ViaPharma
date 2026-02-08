package gr.softeng.team09.view.ClerkShowStats;

import java.util.List;

import gr.softeng.team09.domain.Order;

/**
 * The interface Clerk show stats view.
 */
public interface ClerkShowStatsView {

    /**
     * Show orders.
     *
     * @param ordersDisplay the orders display
     */
//Orders list
    void showOrders(List<String> ordersDisplay);

    /**
     * Show revenue.
     *
     * @param msg the msg
     */
//Revenue outputs
    void showRevenue(String msg);

    /**
     * Show total revenue.
     *
     * @param msg the msg
     */
    void showTotalRevenue(String msg);

    /**
     * Show sales between.
     *
     * @param salesDisplay the sales display
     */
//Timestamp sales list
    void showSalesBetween(List<String> salesDisplay);

    /**
     * Show error on orders input.
     *
     * @param msg the msg
     */
//Errors / UI feedback
    void showErrorOnOrdersInput(String msg);

    /**
     * Show error on revenue input.
     *
     * @param msg the msg
     */
    void showErrorOnRevenueInput(String msg);

    /**
     * Show error on start date input.
     *
     * @param msg the msg
     */
    void showErrorOnStartDateInput(String msg);

    /**
     * Show error on end date input.
     *
     * @param msg the msg
     */
    void showErrorOnEndDateInput(String msg);

    /**
     * Clear orders.
     */
//clears
    void clearOrders();

    /**
     * Clear sales between.
     */
    void clearSalesBetween();

    /**
     * Show order details dialog.
     *
     * @param o the o
     */
    void showOrderDetailsDialog(Order o);
}
