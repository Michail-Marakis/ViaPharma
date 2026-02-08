package gr.softeng.team09.view.PharmacyShowProducts;

import java.util.List;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.Product;

/**
 * The interface Pharmacy show products view.
 */
public interface PharmacyShowProductsView {
    /**
     * Select quantity dialog.
     *
     * @param o the o
     * @param p the p
     */
    void selectQuantityDialog(Order o, Product p);


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
     * Show products.
     *
     * @param items the items
     */
    void showProducts(List<InventoryLine> items);

}