package gr.softeng.team09.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Batch.
 */
public class Batch {

    private final int id;
    private final Product product;
    private final int batchNumber;

    private final int initialQuantity;
    private int quantity; // remaining

    private final Map<Pharmacy, Integer> distributedQty = new HashMap<>();

    /**
     * Instantiates a new Batch.
     *
     * @param id          the id
     * @param product     the product
     * @param batchNumber the batch number
     * @param quantity    the quantity
     */
    public Batch(int id, Product product, int batchNumber, int quantity) {
        this.id = id;
        this.product = Objects.requireNonNull(product);
        this.batchNumber = batchNumber;
        this.initialQuantity = Math.max(0, quantity);
        this.quantity = this.initialQuantity;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() { return id; }

    /**
     * Gets product.
     *
     * @return the product
     */
    public Product getProduct() { return product; }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() { return quantity; } // remaining

    /**
     * Get batch number int.
     *
     * @return the int
     */
    public int getBatchNumber(){
        return batchNumber;
    }

    /**
     * Gets distributed qty.
     *
     * @return the distributed qty
     */
    public Map<Pharmacy, Integer> getDistributedQty() {
        return new HashMap<>(distributedQty);
    }

    /**
     * Gets distributed total.
     *
     * @return the distributed total
     */
    public int getDistributedTotal() {
        int sum = 0;
        for (int q : distributedQty.values()) sum += q;
        return sum;
    }

    /**
     * Gets distributed to.
     *
     * @return the distributed to
     */
    public List<Pharmacy> getDistributedTo() {
        return new ArrayList<>(distributedQty.keySet());
    }

    /**
     * Consume int.
     *
     * @param amount   the amount
     * @param pharmacy the pharmacy
     * @return the int
     */
    public int consume(int amount, Pharmacy pharmacy) {
        if (amount <= 0 || quantity <= 0) return 0;

        int consumed = Math.min(quantity, amount);
        quantity -= consumed;

        if (pharmacy != null) {
            distributedQty.put(
                    pharmacy,
                    distributedQty.getOrDefault(pharmacy, 0) + consumed
            );
        }

        return consumed;
    }

    /**
     * Recall.
     */
    public void recall() {
        for (Pharmacy p : distributedQty.keySet()) {
            System.out.println(
                    "RECALL NOTICE -> Pharmacy: " + p.getName()
                            + " batch " + batchNumber
                            + " of product " + product.getName()
            );
        }
        quantity = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Batch batch = (Batch) o;
        return id == batch.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}