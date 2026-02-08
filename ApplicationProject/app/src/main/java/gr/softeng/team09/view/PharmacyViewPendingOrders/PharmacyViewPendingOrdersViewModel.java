package gr.softeng.team09.view.PharmacyViewPendingOrders;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy view pending orders view model.
 */
public class PharmacyViewPendingOrdersViewModel extends ViewModel {

    private final PharmacyViewPendingOrdersPresenter presenter;

    /**
     * Instantiates a new Pharmacy view pending orders view model.
     */
    public PharmacyViewPendingOrdersViewModel() {
        presenter = new PharmacyViewPendingOrdersPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacyViewPendingOrdersPresenter getPresenter() {
        return presenter;
    }
}