package gr.softeng.team09.view.ClerkEmails;

import androidx.lifecycle.ViewModel;


/**
 * The type Clerk emails view model.
 */
public class ClerkEmailsViewModel extends ViewModel {

    private ClerkEmailsPresenter presenter;


    /**
     * Instantiates a new Clerk emails view model.
     */
    public ClerkEmailsViewModel(){
        this.presenter = new ClerkEmailsPresenter();
    }


    /**
     * Get presenter clerk emails presenter.
     *
     * @return the clerk emails presenter
     */
    public ClerkEmailsPresenter getPresenter(){
        return presenter;
    }

}
