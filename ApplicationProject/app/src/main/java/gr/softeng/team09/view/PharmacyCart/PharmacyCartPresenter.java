package gr.softeng.team09.view.PharmacyCart;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.dao.BatchDAO;
import gr.softeng.team09.dao.InventoryLineDAO;
import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.dao.ReservationDAO;
import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.ReservationDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.OrderDAOMemory;

/**
 * The type Pharmacy cart presenter.
 */
public class PharmacyCartPresenter {

    private PharmacyCartView view;
    private Pharmacy pharmacy ;
    private Order currentOrder;
    private OrderDAO orderDAO ;
    private Inventory inventory;

    /**
     * Instantiates a new Pharmacy cart presenter.
     */
    public PharmacyCartPresenter() {
        this.orderDAO = new OrderDAOMemory();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacyCartView view) {
        this.view = view;
    }

    /**
     * Load cart.
     */
    public void loadCart() {
        this.pharmacy = MemoryStore.getPharmacy();
        this.inventory = MemoryStore.getInventory();

        if (pharmacy == null) return;

        this.currentOrder = MemoryStore.getActiveOrder();

        if (currentOrder == null || currentOrder.getLines().isEmpty()) {
            view.showError("Cart is empty");
            view.showTotal(0.0);
            view.showOrder(null);
            return;
        }

        view.showOrder(currentOrder);
        view.showTotal(pharmacy.calculateOrderTotal(currentOrder));
    }

    /**
     * On submit clicked.
     */
    public void onSubmitClicked() {
        currentOrder = MemoryStore.getActiveOrder();

        if (currentOrder == null || currentOrder.getLines().isEmpty()) {
            view.showError("Cart is empty.");
            return;
        }
        view.showBudgetDialog();
    }


    /**
     * Submit order.
     *
     * @param budget          the budget
     * @param allowBackorders the allow backorders
     */
    public void submitOrder(int budget, boolean allowBackorders) {
        if (currentOrder == null) return;

        this.pharmacy = MemoryStore.getPharmacy();
        this.inventory = MemoryStore.getInventory();

        if (pharmacy.payment(budget, currentOrder)) {
            pharmacy.submitOrder(currentOrder, inventory, budget, allowBackorders);
            orderDAO.complete(currentOrder);
            view.showSuccess("Order submitted");
            clearActiveSession();

            view.navigateBack();
        } else {
            view.showError("Balance not enough for the payment.");
        }
    }

    /**
     * Save draft.
     */
    public void saveDraft() {
        if (currentOrder != null) {
            orderDAO.saveDraft(currentOrder);

            clearActiveSession();

            view.showError("Saved to Drafts.");
        }
        view.navigateBack();
    }

    /**
     * On back pressed.
     */
    public void onBackPressed() {
        view.navigateBack();
    }

    /**
     * Update line quantity.
     *
     * @param line        the line
     * @param newQuantity the new quantity
     */
    public void updateLineQuantity(OrderLine line, int newQuantity) {
        if (newQuantity <= 0) {
            currentOrder.removeLine(line);
        } else {
            line.setQuantity(newQuantity);
        }

        view.showOrder(currentOrder);
        view.showTotal(pharmacy.calculateOrderTotal(currentOrder));
    }

    private void clearActiveSession() {
        MemoryStore.clearActiveOrder();

        if (pharmacy != null && pharmacy.cart != null) {
            pharmacy.cart.clear();
        }

        this.currentOrder = null;
    }
}