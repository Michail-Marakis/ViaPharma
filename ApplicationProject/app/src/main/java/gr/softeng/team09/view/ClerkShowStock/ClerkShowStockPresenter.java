package gr.softeng.team09.view.ClerkShowStock;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk show stock presenter.
 */
public class ClerkShowStockPresenter {

    private ClerkShowStockView view;
    /**
     * The Inventory.
     */
    Inventory inventory = MemoryStore.getInventory();
    private int eofToSearch = -1;
    /**
     * The Stock.
     */
    List<InventoryLine> stock = new ArrayList<>();


    /**
     * Get stock list.
     *
     * @return the list
     */
    public List<InventoryLine> getStock(){
        return stock;
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkShowStockView view) {
        this.view = view;
    }

    /**
     * Set eof to search.
     *
     * @param eof the eof
     */
    public void setEofToSearch(String eof){

        this.eofToSearch = parseId(eof);
    }

    /**
     * Load full stock.
     */
//-----------------helpers--------
    public void loadFullStock() {
        stock.clear();
        stock = inventory.getAvailableItems();
    }

    /**
     * Load eof stock.
     */
    public void loadEofStock(){
        List<String> specifiqueStock = new ArrayList<>();
        for(InventoryLine line : inventory.getAvailableItems()){
            if(line.getProduct() !=null && line.getProduct().getEofyCode() == eofToSearch){
                String info =
                        "Name: " + line.getProduct().getName() +
                                "\n EOF code: " + line.getProduct().getEofyCode() +
                                "\nQuantity: " + line.getQuantity() +
                                "\nPrice without VAT: " + line.getProduct().getPrice() + " €";
                specifiqueStock.add(info);
                break;
            }
        }
        view.showOrders(specifiqueStock);
    }


    private Integer parseId(String eofText) {
        if (eofText == null || eofText.trim().isEmpty()) {
            view.showError("EOF code required");
            return -1;
        }
        try {
            return Integer.parseInt(eofText.trim());
        } catch (NumberFormatException e) {
            view.showError("Invalid EOF code");
            return -1;
        }
    }
}
