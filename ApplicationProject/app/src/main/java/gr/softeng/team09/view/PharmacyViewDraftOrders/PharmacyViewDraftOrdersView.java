package gr.softeng.team09.view.PharmacyViewDraftOrders;

import java.util.List;
import gr.softeng.team09.domain.Order;

/**
 * The interface Pharmacy view draft orders view.
 */
public interface PharmacyViewDraftOrdersView {

    /**
     * Show draft orders.
     *
     * @param orders the orders
     */
    void showDraftOrders(List<Order> orders);

    /**
     * Navigate to cart.
     */
    void navigateToCart();

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);
}
