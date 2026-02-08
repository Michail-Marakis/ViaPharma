package gr.softeng.team09.view.ClerkShowStock;

import androidx.lifecycle.ViewModel;

/**
 * The type Clerk show stock view model.
 */
public class ClerkShowStockViewModel extends ViewModel {

    private final ClerkShowStockPresenter presenter;

    /**
     * Instantiates a new Clerk show stock view model.
     */
    public ClerkShowStockViewModel() {
        this.presenter = new ClerkShowStockPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public ClerkShowStockPresenter getPresenter() {
        return presenter;
    }
}
