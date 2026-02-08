package gr.softeng.team09.view.PharmacyShowProducts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.domain.Address;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.OrderDAOMemory;

/**
 * The type Pharmacy show products presenter.
 */
public class PharmacyShowProductsPresenter {

    private PharmacyShowProductsView view;
    private Inventory inventory;

    /**
     * The Ph.
     */
    public Pharmacy ph;
    private List<InventoryLine> allItems;
    private List<InventoryLine> displayedItems;


    private Order currentOrder;
    private OrderDAO orderDAO ;

    /**
     * Instantiates a new Pharmacy show products presenter.
     */
    public PharmacyShowProductsPresenter() {
        this.orderDAO = new OrderDAOMemory();
        ph = MemoryStore.getPharmacy();
        this.inventory = MemoryStore.getInventory();
        currentOrder = MemoryStore.getActiveOrder();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacyShowProductsView view) {
        this.view = view;
    }


    /**
     * Add product to cart.
     *
     * @param product  the product
     * @param quantity the quantity
     */
    public void addProductToCart(Product product, int quantity) {
        if (ph == null) {
            ph = MemoryStore.getPharmacy();
            if (ph == null) {
                view.showError("Error: No connected pharmacy. Please login again.");
                return;
            }
        }

        if (currentOrder == null) {
            currentOrder = MemoryStore.getActiveOrder();
        }

        if (currentOrder == null) {
            currentOrder = new Order(orderDAO.nextId(), ph);
            orderDAO.saveDraft(currentOrder);
        }

        MemoryStore.setActiveOrder(currentOrder);

        try {
            OrderLine ol = new OrderLine(product, quantity);
            ph.addToCart(currentOrder, ol);

            view.showError("Added: " + product.getName());
        } catch (Exception e) {
            view.showError("Error during addition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load current items.
     */
    public void loadCurrentItems(){
        if (MemoryStore.getInventory() != null) {
            this.allItems = MemoryStore.getInventory().getAvailableItems();
            displayedItems = new ArrayList<>(allItems);
        }
    }

    /**
     * Sort products.
     *
     * @param criteria the criteria
     */
    public void sortProducts(String criteria) {
        if (displayedItems == null || displayedItems.isEmpty()) return;

        switch (criteria) {
            case "NAME":
                Collections.sort(displayedItems, new Comparator<InventoryLine>() {
                    @Override
                    public int compare(InventoryLine o1, InventoryLine o2) {
                        return o1.getProduct().getName().compareToIgnoreCase(o2.getProduct().getName());
                    }
                });
                break;
            case "EOF":
                Collections.sort(displayedItems, new Comparator<InventoryLine>() {
                    @Override
                    public int compare(InventoryLine o1, InventoryLine o2) {
                        return Integer.compare(o1.getProduct().getEofyCode(), o2.getProduct().getEofyCode());
                    }
                });
                break;
        }
        view.showProducts(displayedItems);
    }

    /**
     * Search by eof.
     *
     * @param eof the eof
     */
    public void searchByEof(String eof) {
        if (allItems == null || allItems.isEmpty()) {
            view.showError("No more products");
            return;
        }
        if (eof == null || eof.trim().isEmpty()) {
            this.displayedItems = new ArrayList<>(this.allItems);
            view.showProducts(displayedItems);
            return;
        }
        List<InventoryLine> results = new ArrayList<>();
        int eofCode;
        try {
            eofCode = Integer.parseInt(eof.trim());
            for (InventoryLine line : allItems) {
                if (line.getProduct().getEofyCode() == eofCode) {
                    results.add(line);
                }
            }
            this.displayedItems = results;
            view.showProducts(displayedItems);
        } catch (NumberFormatException e) {
            view.showError("EOF code must be a number");
        }
    }

    /**
     * Get all items list.
     *
     * @return the list
     */
    public List<InventoryLine> getAllItems(){
        return allItems;
    }

}