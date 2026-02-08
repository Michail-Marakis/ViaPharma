package gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy sign up view model.
 */
public class PharmacySignUpViewModel extends ViewModel {
    private final PharmacySignUpPresenter presenter;

    /**
     * Instantiates a new Pharmacy sign up view model.
     */
    public PharmacySignUpViewModel() {
        presenter = new PharmacySignUpPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacySignUpPresenter getPresenter() {
        return presenter;
    }
}