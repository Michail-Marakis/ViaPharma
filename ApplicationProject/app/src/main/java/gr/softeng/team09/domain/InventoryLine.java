package gr.softeng.team09.domain;

import androidx.annotation.NonNull;

/**
 * The type Inventory line.
 */
public class InventoryLine {
    private final Product product;
    private int quantity;

    /**
     * Instantiates a new Inventory line.
     *
     * @param product  the product
     * @param quantity the quantity
     */
    public InventoryLine(Product product, int quantity) {
        this.product = product;
        this.quantity = Math.max(0, quantity);
    }

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
    public int getQuantity() { return quantity; }

    /**
     * Sets quantity.
     *
     * @param q the q
     */
    public void setQuantity(int q) { this.quantity = Math.max(0, q); }

    /**
     * Increase.
     *
     * @param delta the delta
     */
    public void increase(int delta) {
        if(delta >= 0) {
            setQuantity(this.quantity + delta);
        }else{
            decrease(-delta);
        }
    }

    /**
     * Decrease.
     *
     * @param delta the delta
     */
    public void decrease(int delta) {
        if(delta >=0) {
            setQuantity(this.quantity - delta);
        }else{
            increase(-delta);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return product.getName() + "\nPrice: " + product.getPriceWithVAT()+"\nEOF code:"+product.getEofyCode();
    }

}
