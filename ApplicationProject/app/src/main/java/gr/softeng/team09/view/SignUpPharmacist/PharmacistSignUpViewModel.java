package gr.softeng.team09.view.SignUpPharmacist;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacist sign up view model.
 */
public class PharmacistSignUpViewModel extends ViewModel {
    private final PharmacistSignUpPresenter presenter;

    /**
     * Instantiates a new Pharmacist sign up view model.
     */
    public PharmacistSignUpViewModel() {
        presenter = new PharmacistSignUpPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacistSignUpPresenter getPresenter() {
        return presenter;
    }
}