package gr.softeng.team09.view.ClerkBatches.Recall;

import androidx.lifecycle.ViewModel;

/**
 * The type Clerk recall batch view model.
 */
public class ClerkRecallBatchViewModel extends ViewModel {

    private final ClerkRecallBatchPresenter presenter;

    /**
     * Instantiates a new Clerk recall batch view model.
     */
    public ClerkRecallBatchViewModel(){presenter = new ClerkRecallBatchPresenter();}

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public ClerkRecallBatchPresenter getPresenter() {return presenter;}
}
