package gr.softeng.team09.view.PharmacyViewDraftOrders;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy view draft orders view model.
 */
public class PharmacyViewDraftOrdersViewModel extends ViewModel {

    private final PharmacyViewDraftOrdersPresenter presenter;

    /**
     * Instantiates a new Pharmacy view draft orders view model.
     */
    public PharmacyViewDraftOrdersViewModel() {
        presenter = new PharmacyViewDraftOrdersPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacyViewDraftOrdersPresenter getPresenter() {

        return presenter;
    }
}
