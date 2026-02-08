package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Backorder;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk back orders presenter.
 */
public class ClerkBackOrdersPresenter {

    private ClerkBackOrdersView view;
    private String pharmAFM = null;

    private final Inventory inventory = MemoryStore.getInventory();


    private final List<Backorder> lastShownBackorders = new ArrayList<>();

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkBackOrdersView view) {
        this.view = view;
    }

    /**
     * Sets pharm afm.
     *
     * @param afm the afm
     */
    public void setPharmAFM(String afm) {
        pharmAFM = afm;
    }


    private List<Backorder> freshBackorders() {
        return new ArrayList<>(inventory.getBackorders());
    }


    /**
     * Gets backorder at.
     *
     * @param position the position
     * @return the backorder at
     */
    public Backorder getBackorderAt(int position) {
        if (position < 0 || position >= lastShownBackorders.size()) return null;
        return lastShownBackorders.get(position);
    }


    /**
     * Gets last shown backorders.
     *
     * @return the last shown backorders
     */
    public List<Backorder> getLastShownBackorders() {
        return new ArrayList<>(lastShownBackorders);
    }

    /**
     * Load active backorders.
     */
// ---------------- ACTIVE (state BACKORDER) ----------------
    public void loadActiveBackorders() {
        List<Backorder> result = new ArrayList<>();
        for (Backorder b : freshBackorders()) {
            if (b.getState() == OrderState.BACKORDER) {
                result.add(b);
            }
        }
        lastShownBackorders.clear();
        lastShownBackorders.addAll(result);
    }

    /**
     * Load all backorders.
     */
// ---------------- ALL ----------------
    public void loadAllBackorders() {
        List<Backorder> result = freshBackorders();
        lastShownBackorders.clear();
        lastShownBackorders.addAll(result);
    }

    /**
     * Find all back orders from pharm.
     */
// ---------------- BY PHARMACY AFM ----------------
    public void findAllBackOrdersFromPharm() {
        int afm = parseAfm(pharmAFM);
        if (afm == -1) return;

        List<Backorder> result = inventory.getBackFromOnePharm(afm);
        lastShownBackorders.clear();
        lastShownBackorders.addAll(result);

        pharmAFM = null;
    }

    private int parseAfm(String idText) {
        if (idText == null || idText.trim().isEmpty()) {
            view.showError("AFM required");
            return -1;
        }
        try {
            return Integer.parseInt(idText.trim());
        } catch (NumberFormatException e) {
            view.showError("Invalid AFM");
            return -1;
        }
    }
}
