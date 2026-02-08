package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget;


import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy forget view model.
 */
public class PharmacyForgetViewModel extends ViewModel {

    private final PharmacyForgetPresenter presenter;

    /**
     * Instantiates a new Pharmacy forget view model.
     */
    public PharmacyForgetViewModel() {
        presenter = new PharmacyForgetPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacyForgetPresenter getPresenter() {
        return presenter;
    }
}
