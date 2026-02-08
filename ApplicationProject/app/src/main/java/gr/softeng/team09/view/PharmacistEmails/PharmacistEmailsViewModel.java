package gr.softeng.team09.view.PharmacistEmails;

import androidx.lifecycle.ViewModel;


/**
 * The type Clerk emails view model.
 */
public class PharmacistEmailsViewModel extends ViewModel {

    private PharmacistEmailsPresenter presenter;


    /**
     * Instantiates a new Clerk emails view model.
     */
    public PharmacistEmailsViewModel(){
        this.presenter = new PharmacistEmailsPresenter();
    }


    /**
     * Get presenter clerk emails presenter.
     *
     * @return the clerk emails presenter
     */
    public PharmacistEmailsPresenter getPresenter(){
        return presenter;
    }

}
