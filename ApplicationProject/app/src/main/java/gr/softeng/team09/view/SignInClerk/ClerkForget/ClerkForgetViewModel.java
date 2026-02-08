package gr.softeng.team09.view.SignInClerk.ClerkForget;

import androidx.lifecycle.ViewModel;


/**
 * The type Clerk forget view model.
 */
public class ClerkForgetViewModel extends ViewModel {

    private final ClerkForgetPresenter presenter;

    /**
     * Instantiates a new Clerk forget view model.
     */
    public ClerkForgetViewModel() {
        presenter = new ClerkForgetPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public ClerkForgetPresenter getPresenter() {
        return presenter;
    }

}
