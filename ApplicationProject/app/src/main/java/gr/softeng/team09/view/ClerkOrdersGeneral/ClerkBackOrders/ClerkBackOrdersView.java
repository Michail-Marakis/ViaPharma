package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders;


import gr.softeng.team09.domain.Backorder;

/**
 * The interface Clerk back orders view.
 */
public interface ClerkBackOrdersView {


    /**
     * Build row string.
     *
     * @param back the back
     * @return the string
     */
    String buildRow(Backorder back);

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
