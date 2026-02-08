package gr.softeng.team09.view.ClerkShowStats;

import androidx.lifecycle.ViewModel;

/**
 * The type Clerk show stats view model.
 */
public class ClerkShowStatsViewModel extends ViewModel {

    private final ClerkShowStatsPresenter presenter;

    /**
     * Instantiates a new Clerk show stats view model.
     */
    public ClerkShowStatsViewModel() {
        presenter = new ClerkShowStatsPresenter();
    }

    /**
     * Gets presenter.
     *
     * @return the presenter
     */
    public ClerkShowStatsPresenter getPresenter() {
        return presenter;
    }
}
