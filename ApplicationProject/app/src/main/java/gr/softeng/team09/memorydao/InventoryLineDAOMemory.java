package gr.softeng.team09.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Product;

/**
 * The type Inventory line dao memory.
 */
public class InventoryLineDAOMemory implements InventoryLineDAO {

    private static final List<InventoryLine> lines = new ArrayList<>();

    @Override
    public void save(InventoryLine line) {
        if (line == null) return;

        for (int i = 0; i < lines.size(); i++) {
            InventoryLine existing = lines.get(i);
            if (existing.getProduct().equals(line.getProduct())) { // Product equals = EOF
                lines.set(i, line);
                return;
            }
        }
        lines.add(line);
    }

    @Override
    public InventoryLine findByProduct(Product product) {
        if (product == null) return null;
        for (InventoryLine l : lines) {
            if (product.equals(l.getProduct())) return l;
        }
        return null;
    }

    @Override
    public List<InventoryLine> findAll() {
        return new ArrayList<>(lines);
    }

    @Override
    public void deleteByProduct(Product product) {
        if (product == null) return;
        lines.removeIf(l -> product.equals(l.getProduct()));
    }
}
