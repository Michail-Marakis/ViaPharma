package gr.softeng.team09.dao;

import java.util.List;

import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Product;

/**
 * The interface Batch dao.
 */
public interface BatchDAO {

    /**
     * Next id int.
     *
     * @return the int
     */
    int nextId();

    /**
     * Save.
     *
     * @param batch the batch
     */
//receive
    void save(Batch batch);

    /**
     * Find batch.
     *
     * @param batchId the batch id
     * @return the batch
     */
//recall by id
    Batch find(int batchId);

    /**
     * Find all list.
     *
     * @return the list
     */
//για UI list (όλα τα batches που ξέρει το σύστημα, active + history)
    List<Batch> findAll();

    /**
     * Find active by product list.
     *
     * @param product the product
     * @return the list
     */
//για allocate/fulfill: μόνο τα active batches για συγκεκριμένο προϊόν
    List<Batch> findActiveByProduct(Product product);

    /**
     * Remove from active if empty.
     *
     * @param batch the batch
     */
//όταν ένα batch αδειάσει, φύγε από active, αλλά ΜΗΝ χαθεί από history
    void removeFromActiveIfEmpty(Batch batch);

    /**
     * Batches counter.
     */
    void BatchesCounter();

    /**
     * Gets total batches.
     *
     * @return the total batches
     */
    int getTotalBatches();
}
