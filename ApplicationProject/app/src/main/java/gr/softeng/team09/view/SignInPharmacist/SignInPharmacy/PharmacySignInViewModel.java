package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy sign in view model.
 */
public class PharmacySignInViewModel extends ViewModel {
    private final PharmacySignInPresenter presenter;

    /**
     * Instantiates a new Pharmacy sign in view model.
     */
    public PharmacySignInViewModel() {
        presenter = new PharmacySignInPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacySignInPresenter getPresenter() {
        return presenter;
    }
}