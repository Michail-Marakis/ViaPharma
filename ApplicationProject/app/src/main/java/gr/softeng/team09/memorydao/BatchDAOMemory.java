package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Product;

/**
 * The type Batch dao memory.
 */
public class BatchDAOMemory implements BatchDAO {


    private final Map<Product, List<Batch>> activeByProduct = new HashMap<>();

    /**
     * The Total batches.
     */
    public int totalBatches = 0;


    private final Map<Integer, Batch> byId = new HashMap<>();
    @Override
    public synchronized int nextId() {
        int max = 0;
        for (Integer id : byId.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }


    @Override
    public void save(Batch batch) {
        if (batch == null) return;

        //πάντα το κρατάμε στο byId (history)
        byId.put(batch.getId(), batch);

        //αν έχει stock, το κρατάμε και στα active
        if (batch.getQuantity() > 0) {
            Product p = batch.getProduct();
            List<Batch> list = activeByProduct.get(p);
            if (list == null) {
                list = new ArrayList<>();
                activeByProduct.put(p, list);
            }
            if (!list.contains(batch)) {
                list.add(batch);
            }
        } else {
            removeFromActiveIfEmpty(batch);
        }
    }

    @Override
    public Batch find(int batchId) {
        return byId.get(batchId);
    }

    @Override
    public List<Batch> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public List<Batch> findActiveByProduct(Product product) {
        if (product == null) return new ArrayList<>();
        List<Batch> list = activeByProduct.get(product);
        if (list == null) return new ArrayList<>();
        return new ArrayList<>(list);
    }

    @Override
    public void removeFromActiveIfEmpty(Batch batch) {
        if (batch == null) return;

        if (batch.getQuantity() > 0) return; //δεν είναι empty

        Product p = batch.getProduct();
        List<Batch> list = activeByProduct.get(p);
        if (list == null) return;

        list.remove(batch);

        if (list.isEmpty()) {
            activeByProduct.remove(p);
        }
    }
    @Override
    public void BatchesCounter(){
        totalBatches++;
    }

    @Override
    public int getTotalBatches(){
        return totalBatches;
    }
}
