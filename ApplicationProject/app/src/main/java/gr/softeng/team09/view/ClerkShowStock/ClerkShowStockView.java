package gr.softeng.team09.view.ClerkShowStock;

import java.util.List;

import gr.softeng.team09.domain.InventoryLine;

/**
 * The interface Clerk show stock view.
 */
public interface ClerkShowStockView {

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);

    /**
     * Load information to list.
     */
    void loadInformationToList();

    /**
     * Build row string.
     *
     * @param i the
     * @return the string
     */
    String buildRow(InventoryLine i);

    /**
     * Clear orders.
     */
    void clearOrders();

    /**
     * Show orders.
     *
     * @param display the display
     */
    void showOrders(List<String> display);
}
