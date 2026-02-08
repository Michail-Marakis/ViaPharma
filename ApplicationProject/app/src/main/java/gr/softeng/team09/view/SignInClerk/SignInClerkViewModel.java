package gr.softeng.team09.view.SignInClerk;

import androidx.lifecycle.ViewModel;

/**
 * The type Sign in clerk view model.
 */
public class SignInClerkViewModel extends ViewModel {

    private final SignInClerkPresenter presenter;

    /**
     * Instantiates a new Sign in clerk view model.
     */
    public SignInClerkViewModel() {
        presenter = new SignInClerkPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public SignInClerkPresenter getPresenter() {
        return presenter;
    }
}
