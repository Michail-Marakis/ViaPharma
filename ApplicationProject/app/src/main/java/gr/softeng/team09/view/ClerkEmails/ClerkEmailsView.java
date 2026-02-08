package gr.softeng.team09.view.ClerkEmails;

import java.util.List;

/**
 * The interface Clerk emails view.
 */
public interface ClerkEmailsView {
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
