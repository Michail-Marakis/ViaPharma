package gr.softeng.team09.view.PharmacyShowProducts;

import androidx.lifecycle.ViewModel;

/**
 * The type Pharmacy show product view model.
 */
public class PharmacyShowProductViewModel extends ViewModel {

    private PharmacyShowProductsPresenter presenter;

    /**
     * Instantiates a new Pharmacy show product view model.
     */
    public PharmacyShowProductViewModel(){
        this.presenter = new PharmacyShowProductsPresenter();
    }

    /**
     * Get presenter pharmacy show products presenter.
     *
     * @return the pharmacy show products presenter
     */
    public PharmacyShowProductsPresenter getPresenter(){
        return presenter;
    }

}
