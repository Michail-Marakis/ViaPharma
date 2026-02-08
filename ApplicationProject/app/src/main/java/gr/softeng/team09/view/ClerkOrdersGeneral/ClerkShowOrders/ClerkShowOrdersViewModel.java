package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders;

import androidx.lifecycle.ViewModel;

/**
 * The type Clerk show orders view model.
 */
public class ClerkShowOrdersViewModel extends ViewModel {

    private final ClerkShowOrdersPresenter presenter;

    /**
     * Instantiates a new Clerk show orders view model.
     */
    public ClerkShowOrdersViewModel() {
        presenter = new ClerkShowOrdersPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public ClerkShowOrdersPresenter getPresenter() {
        return presenter;
    }
}
