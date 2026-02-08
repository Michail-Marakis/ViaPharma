package gr.softeng.team09.view.ClerkEmails;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk emails presenter.
 */
public class ClerkEmailsPresenter {

    private ClerkEmailsView view;
    private final Inventory inventory = MemoryStore.getInventory();
    private List<StringBuilder> listWithEmails = new ArrayList<>();

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkEmailsView view) {
        this.view = view;
    }

    /**
     * Load emails list.
     */
    public void loadEmailsList() {
        listWithEmails = inventory.getOwner().getEmailsSent();

        List<String> titles = new ArrayList<>();

        for (int i = listWithEmails.size() - 1; i >= 0; i--) {
            int shownNumber = (listWithEmails.size() - i);
            titles.add("Email " + shownNumber);
        }

        view.showEmails(titles);
    }

    /**
     * On email clicked.
     *
     * @param position the position
     */
    public void onEmailClicked(int position) {
        if (listWithEmails == null || listWithEmails.isEmpty()) return;

        int realIndex = (listWithEmails.size() - 1) - position;

        if (realIndex < 0 || realIndex >= listWithEmails.size()) return;

        String emailText = listWithEmails.get(realIndex).toString();
        view.showEmailPopup("Email", emailText);
    }

}
