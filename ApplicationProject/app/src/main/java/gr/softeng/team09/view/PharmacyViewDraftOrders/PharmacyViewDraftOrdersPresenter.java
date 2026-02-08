package gr.softeng.team09.view.PharmacyViewDraftOrders;

import java.util.List;

import gr.softeng.team09.dao.OrderDAO;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.OrderDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Pharmacy view draft orders presenter.
 */
public class PharmacyViewDraftOrdersPresenter {

    private PharmacyViewDraftOrdersView view;
    private Pharmacy pharmacy;
    private OrderDAO orderDAO;


    /**
     * Instantiates a new Pharmacy view draft orders presenter.
     */
    public PharmacyViewDraftOrdersPresenter() {
        this.orderDAO = new OrderDAOMemory();
        this.pharmacy = MemoryStore.getPharmacy();
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacyViewDraftOrdersView view) {
        this.view = view;
    }

    /**
     * Load draft orders.
     */
    public void loadDraftOrders() {
        if (pharmacy == null) {
            view.showError("No pharmacy found");
            return;
        }
        List<Order> drafts = orderDAO.findByPharmacyAndState(pharmacy, OrderState.DRAFT);
        view.showDraftOrders(drafts);
    }

    /**
     * On draft order selected.
     *
     * @param selectedDraft the selected draft
     */
    public void onDraftOrderSelected(Order selectedDraft) {
        Order activeOrder = MemoryStore.getActiveOrder();
        if (activeOrder != null && !activeOrder.equals(selectedDraft)) {
            orderDAO.saveDraft(activeOrder);
        }

        MemoryStore.setActiveOrder(selectedDraft);

        Pharmacy ph = MemoryStore.getPharmacy();
        if (ph != null) {
            ph.cart.clear();
            ph.cart.addAll(selectedDraft.getLines());
        }

        view.navigateToCart();
    }
}