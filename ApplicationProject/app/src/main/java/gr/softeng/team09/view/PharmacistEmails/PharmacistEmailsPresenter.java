package gr.softeng.team09.view.PharmacistEmails;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gr.softeng.team09.domain.Clerk;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Pharmacist emails presenter.
 */
public class PharmacistEmailsPresenter {

    private PharmacistEmailsView view;
    private final Inventory inventory = MemoryStore.getInventory();
    private List<StringBuilder> listWithEmails = new ArrayList<>();

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacistEmailsView view) {
        this.view = view;
    }


    /**
     * Gets emails.
     *
     * @return the emails
     */
    public List<StringBuilder> getEmails() {
        List<StringBuilder> PersonalEmails = new ArrayList<>();


        String myEmail = MemoryStore.getPharmacist().getEmail();

        for (Clerk own : inventory.getOwners()) {
            for (int i = 0; i < own.getSentTo().size(); i++) {
                if (Objects.equals(own.getSentTo().get(i).getEmail(), myEmail)) {
                    StringBuilder copy = getStringBuilder(own, i);
                    PersonalEmails.add(copy);
                }
            }
        }
        return new ArrayList<>(PersonalEmails);
    }

    @NonNull
    private static StringBuilder getStringBuilder(Clerk own, int i) {
        StringBuilder original = own.getEmailsSent().get(i);
        StringBuilder copy = new StringBuilder(original.toString());
        int idx2 = copy.indexOf("sent");
        if (idx2 != -1) {
            copy.replace(idx2, idx2 + "sent".length(), "received");
        }
        return copy;
    }

    /**
     * Load emails list.
     */
    public void loadEmailsList() {
        List<String> OutSideEmails = new ArrayList<>();
        listWithEmails = getEmails();
        for (int i = listWithEmails.size() -1; i >= 0; i--) {
            String info = "Email " + (listWithEmails.size() - i);
            OutSideEmails.add(info);
        }
        view.showEmails(OutSideEmails);
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

        String message = listWithEmails.get(realIndex).toString();
        view.showEmailPopup("Email", message);
    }
}
