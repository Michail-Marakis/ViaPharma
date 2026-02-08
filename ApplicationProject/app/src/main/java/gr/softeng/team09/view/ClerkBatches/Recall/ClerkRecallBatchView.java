package gr.softeng.team09.view.ClerkBatches.Recall;

import gr.softeng.team09.domain.Batch;

/**
 * The interface Clerk recall batch view.
 */
public interface ClerkRecallBatchView {

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
     * Recall batch call.
     *
     * @param id the id
     */
    void recallBatchCall(int id);

    /**
     * Load batches to list.
     */
    void loadBatchesToList();

    /**
     * Build row string.
     *
     * @param b the b
     * @return the string
     */
    String buildRow(Batch b);
}
