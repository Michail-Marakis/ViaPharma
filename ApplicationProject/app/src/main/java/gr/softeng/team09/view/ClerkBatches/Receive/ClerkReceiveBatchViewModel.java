package gr.softeng.team09.view.ClerkBatches.Receive;
import android.view.View;

import androidx.lifecycle.ViewModel;

/**
 * The type Clerk receive batch view model.
 */
public class ClerkReceiveBatchViewModel extends ViewModel {

    private final ClerkReceiveBatchPresenter presenter;

    /**
     * Instantiates a new Clerk receive batch view model.
     */
    public ClerkReceiveBatchViewModel(){ presenter = new ClerkReceiveBatchPresenter();}

    /**
     * Get presenter clerk receive batch presenter.
     *
     * @return the clerk receive batch presenter
     */
    public ClerkReceiveBatchPresenter getPresenter(){ return presenter;}
}
