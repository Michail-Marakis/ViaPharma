package gr.softeng.team09.view.SignUpClerk;

import androidx.lifecycle.ViewModel;

/**
 * The type Sign up clerk view model.
 */
public class SignUpClerkViewModel extends ViewModel {

    private final SignUpClerkPresenter presenter;

    /**
     * Instantiates a new Sign up clerk view model.
     */
    public SignUpClerkViewModel() {
        presenter = new SignUpClerkPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public SignUpClerkPresenter getPresenter() {
        return presenter;
    }
}
