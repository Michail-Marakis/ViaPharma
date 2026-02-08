package gr.softeng.team09.view.PharmacistEmails;

import java.util.List;

/**
 * The interface Pharmacist emails view.
 */
public interface PharmacistEmailsView {
    /**
     * Show emails.
     *
     * @param emailTitles the email titles
     */
    void showEmails(List<String> emailTitles);

    /**
     * Show email popup.
     *
     * @param title   the title
     * @param message the message
     */
    void showEmailPopup(String title, String message);
}
