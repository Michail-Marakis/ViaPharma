package gr.softeng.team09.domain;

import java.util.Objects;

/**
 * The type Product.
 */
public class Product {

    private final String name;
    private ProductCategory category;
    private int eofyCode;
    private double price;

    /**
     * Instantiates a new Product.
     *
     * @param name     the name
     * @param category the category
     * @param eofyCode the eofy code
     * @param price    the price
     */
    public Product(String name, ProductCategory category, int eofyCode, double price) {
        this.name = name;
        this.category = category;
        this.eofyCode = eofyCode;
        this.price = price;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets category.
     *
     * @return the category
     */
    public ProductCategory getCategory() { return category; }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(ProductCategory category) { this.category = category; }

    /**
     * Gets eofy code.
     *
     * @return the eofy code
     */
    public int getEofyCode() { return eofyCode; }

    /**
     * Sets eofy code.
     *
     * @param eofyCode the eofy code
     */
    public void setEofyCode(int eofyCode) { this.eofyCode = eofyCode; }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() { return price; }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Gets price with vat.
     *
     * @return the price with vat
     */
    public double getPriceWithVAT() { return price * 1.24; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return eofyCode == product.eofyCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eofyCode);
    }
}