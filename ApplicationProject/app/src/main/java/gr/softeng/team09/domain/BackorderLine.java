package gr.softeng.team09.domain;

/**
 * The type Backorder line.
 */
public class BackorderLine {
    private final Product product;


    private final int quantityRequested;


    private int quantityPending;

    /**
     * Instantiates a new Backorder line.
     *
     * @param product         the product
     * @param quantityPending the quantity pending
     */
    public BackorderLine(Product product, int quantityPending) {
        this.product = product;
        int q = Math.max(0, quantityPending);

        this.quantityRequested = q;
        this.quantityPending = q;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public Product getProduct() { return product; }

    /**
     * Gets quantity pending.
     *
     * @return the quantity pending
     */
    public int getQuantityPending() { return quantityPending; }

    /**
     * Gets quantity requested.
     *
     * @return the quantity requested
     */
    public int getQuantityRequested() { return quantityRequested; }

    /**
     * Gets quantity fulfilled.
     *
     * @return the quantity fulfilled
     */
    public int getQuantityFulfilled() { return quantityRequested - quantityPending; }

    /**
     * Decrease pending.
     *
     * @param delta the delta
     */
    public void decreasePending(int delta) {
        if (delta <= 0) return;
        quantityPending = Math.max(0, quantityPending - delta);
    }

    /**
     * Is fulfilled boolean.
     *
     * @return the boolean
     */
    public boolean isFulfilled() { return quantityPending == 0; }


}
