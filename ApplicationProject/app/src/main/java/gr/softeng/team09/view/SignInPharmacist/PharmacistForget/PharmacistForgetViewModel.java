package gr.softeng.team09.view.SignInPharmacist.PharmacistForget;


import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacist forget view model.
 */
public class PharmacistForgetViewModel extends ViewModel {

    private final PharmacistForgetPresenter presenter;

    /**
     * Instantiates a new Pharmacist forget view model.
     */
    public PharmacistForgetViewModel() {
        presenter = new PharmacistForgetPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public PharmacistForgetPresenter getPresenter() {
        return presenter;
    }
}
