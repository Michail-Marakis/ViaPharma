package gr.softeng.team09.view.ClerkBatches.Receive;

import gr.softeng.team09.domain.ProductCategory;

/**
 * The interface Clerk receive batch view.
 */
public interface ClerkReceiveBatchView {

    //======================= view interface===========================

    /**
     * Generate batch number int.
     *
     * @return the int
     */
//======================= view interface===========================
    int generateBatchNumber();

    /**
     * Show error.
     *
     * @param msg the msg
     */
    void showError(String msg);

    /**
     * Receive batch call.
     *
     * @param eof             the eof
     * @param productName     the product name
     * @param nProducts       the n products
     * @param nBatches        the n batches
     * @param price           the price
     * @param productCategory the product category
     */
    void receiveBatchCall(int eof, String productName, int nProducts, int nBatches, double price, ProductCategory productCategory);

    /**
     * Show success.
     *
     * @param msg the msg
     */
    void showSuccess(String msg);
}
