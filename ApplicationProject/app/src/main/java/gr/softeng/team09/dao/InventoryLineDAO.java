package gr.softeng.team09.dao;

import java.util.List;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Product;

/**
 * The interface Inventory line dao.
 */
public interface InventoryLineDAO {
    /**
     * Save.
     *
     * @param line the line
     */
    void save(InventoryLine line);

    /**
     * Find by product inventory line.
     *
     * @param product the product
     * @return the inventory line
     */
    InventoryLine findByProduct(Product product);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<InventoryLine> findAll();

    /**
     * Delete by product.
     *
     * @param product the product
     */
    void deleteByProduct(Product product);
}
