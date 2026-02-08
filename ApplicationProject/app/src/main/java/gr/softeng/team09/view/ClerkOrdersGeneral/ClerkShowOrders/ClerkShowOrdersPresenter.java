package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.dao.BackorderDAO;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk show orders presenter.
 */
public class ClerkShowOrdersPresenter {

    private ClerkShowOrdersView view;

    private final Inventory inventory = MemoryStore.getInventory();
    private final BackorderDAO backorderDAO = inventory.backorderDAO;

    private List<Order> lastShownOrders = new ArrayList<>();

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkShowOrdersView view) {
        this.view = view;
    }


    /**
     * Load all orders.
     */
    public void loadAllOrders() {
        lastShownOrders = inventory.getAllOrders();
        view.showOrders(toDisplayList(lastShownOrders));
    }

    /**
     * Load completed orders.
     */
    public void loadCompletedOrders() {
        lastShownOrders = inventory.getCompletedOrders();
        view.showOrders(toDisplayList(lastShownOrders));
    }


    /**
     * Execute by id.
     *
     * @param idText          the id text
     * @param allowBackorders the allow backorders
     */
    public void executeById(String idText, boolean allowBackorders) {
        Integer id = parseId(idText);
        if (id == null) return;

        boolean ok = inventory.executeOrderById(id, allowBackorders);
        if (!ok) {
            view.showError("Cannot execute order #" + id);
            return;
        }
        
        if (backorderDAO.getFlag()) {
            view.showMessage("Order #" + id + " executed. BackOrder created.");
            backorderDAO.setFlag(false);
        } else {
            view.showMessage("Order #" + id + " executed. NO BackOrder created.");
        }

        loadAllOrders();
    }

    /**
     * Cancel by id.
     *
     * @param idText the id text
     */
    public void cancelById(String idText) {
        Integer id = parseId(idText);
        if (id == null) return;

        boolean ok = inventory.cancelOrderById(id);
        if (!ok) {
            view.showError("Cannot cancel order #" + id);
            return;
        }

        view.showMessage("Cancelled order #" + id);
        loadAllOrders();
    }

    /**
     * Postpone by id.
     *
     * @param idText the id text
     */
    public void postponeById(String idText) {
        Integer id = parseId(idText);
        if (id == null) return;

        for (Order o : inventory.getAllOrders()) {
            if (o.getId() == id && o.getState() == OrderState.PENDING) {
                inventory.postponeOrderOnPurpose(o);
                view.showMessage("Order #" + id + " postponed");
                loadAllOrders();
                return;
            }
        }

        view.showError("Cannot postpone order #" + id);
    }

    //-------------- LIST CLICK ----------------

    /**
     * On order selected.
     *
     * @param position the position
     */
    public void onOrderSelected(int position) {
        if (position < 0 || position >= lastShownOrders.size()) return;
        view.showOrderDetails(lastShownOrders.get(position));
    }

    //------------ HELPERS ----------------

    private Integer parseId(String idText) {
        if (idText == null || idText.trim().isEmpty()) {
            view.showError("Order ID required");
            return null;
        }
        try {
            return Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            view.showError("Invalid Order ID");
            return null;
        }
    }

    private List<String> toDisplayList(List<Order> orders) {
        List<String> display = new ArrayList<>();
        for (Order o : orders) {
            display.add(
                    "Order #" + o.getId() +
                            "\nPharmacy: " + o.getPharmacy().getName() +
                            "\nState: " + o.getState()
            );
        }
        return display;
    }

    /**
     * Calculate total double.
     *
     * @param order the order
     * @return the double
     */
    public double calculateTotal(Order order) {
        double sum = 0;
        for (OrderLine l : order.getLines()) {
            sum += l.getProduct().getPriceWithVAT() * l.getQuantity();
        }
        return sum;
    }
}
