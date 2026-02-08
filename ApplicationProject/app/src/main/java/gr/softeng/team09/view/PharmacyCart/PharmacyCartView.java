package gr.softeng.team09.view.PharmacyCart;

import gr.softeng.team09.domain.Order;

/**
 * The interface Pharmacy cart view.
 */
public interface PharmacyCartView {

    /**
     * Show order.
     *
     * @param order the order
     */
    void showOrder(Order order);

    /**
     * Show total.
     *
     * @param total the total
     */
    void showTotal(double total);

    /**
     * Show budget dialog.
     */
    void showBudgetDialog();

    /**
     * Show back order dialog.
     *
     * @param budget the budget
     */
    void showBackOrderDialog(int budget);

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);

    /**
     * Show success.
     *
     * @param msg the msg
     */
    void showSuccess(String msg);

    /**
     * Navigate back.
     */
    void navigateBack();
}
