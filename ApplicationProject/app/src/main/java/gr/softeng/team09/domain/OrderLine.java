package gr.softeng.team09.domain;

/**
 * The type Order line.
 */
public class OrderLine {

    private Product product;
    private int quantity;

    /**
     * Instantiates a new Order line.
     *
     * @param product  the product
     * @param quantity the quantity
     */
    public OrderLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets product.
     *
     * @param product the product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " (" + String.format("%.2f", (product.getPriceWithVAT() * quantity)) + "€)";
    }

}
