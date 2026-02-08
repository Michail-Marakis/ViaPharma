package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders;

import androidx.lifecycle.ViewModel;

/**
 * The type Clerk back orders view model.
 */
public class ClerkBackOrdersViewModel extends ViewModel {

    private final ClerkBackOrdersPresenter presenter;

    /**
     * Instantiates a new Clerk back orders view model.
     */
    public ClerkBackOrdersViewModel(){presenter = new ClerkBackOrdersPresenter();}

    /**
     * Get presenter clerk back orders presenter.
     *
     * @return the clerk back orders presenter
     */
    public ClerkBackOrdersPresenter getPresenter(){return presenter;}

}
