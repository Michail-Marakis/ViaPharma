package gr.softeng.team09.view.PharmacyCart;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy cart view model.
 */
public class PharmacyCartViewModel extends ViewModel {

    private final PharmacyCartPresenter presenter;

    /**
     * Instantiates a new Pharmacy cart view model.
     */
    public PharmacyCartViewModel() {
        presenter = new PharmacyCartPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacyCartPresenter getPresenter() {
        return presenter;
    }
}

