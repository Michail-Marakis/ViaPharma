package gr.softeng.team09.view.SignInPharmacist;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacist sign in view model.
 */
public class PharmacistSignInViewModel extends ViewModel {
    private final PharmacistSignInPresenter presenter;

    /**
     * Instantiates a new Pharmacist sign in view model.
     */
    public PharmacistSignInViewModel() {
        presenter = new PharmacistSignInPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacistSignInPresenter getPresenter() {
        return presenter;
    }
}