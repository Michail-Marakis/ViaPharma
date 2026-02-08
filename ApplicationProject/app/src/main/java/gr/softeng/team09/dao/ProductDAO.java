package gr.softeng.team09.dao;

import java.util.List;

import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;

/**
 * The interface Product dao.
 */
public interface ProductDAO {

    /**
     * Delete.
     *
     * @param entity the entity
     */
    void delete(Product entity);


    /**
     * Find all list.
     *
     * @return the list
     */
    List<Product> findAll();


    /**
     * Save.
     *
     * @param entity the entity
     */
    void save(Product entity);


    /**
     * Find by name product.
     *
     * @param name the name
     * @return the product
     */
    Product findByName(String name);

    /**
     * Find by eofy code product.
     *
     * @param eofyCode the eofy code
     * @return the product
     */
    Product findByEofyCode(int eofyCode);
}