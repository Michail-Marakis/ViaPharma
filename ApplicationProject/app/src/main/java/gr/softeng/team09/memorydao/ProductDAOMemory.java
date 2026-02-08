package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gr.softeng.team09.dao.ProductDAO;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;

/**
 * The type Product dao memory.
 */
public class ProductDAOMemory implements ProductDAO {

    /**
     * The constant entities.
     */
    public static ArrayList<Product> entities = new ArrayList<>();

    @Override
    public void delete(Product entity) {
        entities.remove(entity);
    }

    @Override
    public List<Product> findAll() {
        return entities;
    }

    @Override
    public void save(Product entity) {
        for (Product p : entities) {
            if (p == entity) {
                return ;
            }
        }
        entities.add(entity);
    }

    @Override
    public Product findByName(String name) {
        for (Product p : entities) {
            if (p.getName()==name) {
                return p;
            }
        }
        return null;
    }


    @Override
    public Product findByEofyCode(int eofyCode) {
        for (Product p : entities) {
            if (p.getEofyCode() == eofyCode) {
                return p;
            }
        }
        return null;
    }
}
