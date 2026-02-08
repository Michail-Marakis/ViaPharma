package gr.softeng.team09.view.PharmacyViewCompletedOrders;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy view completed orders view model.
 */
public class PharmacyViewCompletedOrdersViewModel extends ViewModel {

    private final PharmacyViewCompletedOrdersPresenter presenter;

    /**
     * Instantiates a new Pharmacy view completed orders view model.
     */
    public PharmacyViewCompletedOrdersViewModel() {
        presenter = new PharmacyViewCompletedOrdersPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacyViewCompletedOrdersPresenter getPresenter() {
        return presenter;
    }
}
