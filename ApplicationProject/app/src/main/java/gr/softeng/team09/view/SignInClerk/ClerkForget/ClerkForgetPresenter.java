package gr.softeng.team09.view.SignInClerk.ClerkForget;

import java.util.Objects;

import gr.softeng.team09.domain.Clerk;

import gr.softeng.team09.memorydao.ClerkDAOMemory;
import gr.softeng.team09.memorydao.MemoryStore;

/**
 * The type Clerk forget presenter.
 */
public class ClerkForgetPresenter {

    /**
     * The View.
     */
    ClerkForgetView view;
    private String NewPassword = null;
    private String NewPasswordConfirmation = null;

    private String username = null;

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(ClerkForgetView view) {
        this.view = view;
    }

    /**
     * Set username.
     *
     * @param username the username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Set new password.
     *
     * @param newPassword the new password
     */
    public void setNewPassword(String newPassword){
        this.NewPassword = newPassword;
    }

    /**
     * Set new password confirmation.
     *
     * @param Confirmation the confirmation
     */
    public void setNewPasswordConfirmation(String Confirmation){
        this.NewPasswordConfirmation = Confirmation;
    }

    /**
     * Authentication session.
     */
    public void authenticationSession(){
        if(username ==null || NewPassword == null || NewPasswordConfirmation == null){
            view.showError("Fill in the form");
        }

        Clerk tofound = MemoryStore.getByEmailClerk(username);
        if(ClerkDAOMemory.entitiesClerk.contains(tofound)){
            if(Objects.equals(NewPassword, NewPasswordConfirmation)){
                tofound.setPassword(NewPassword);
                view.showSuccess("Password changed successfully");
                resetValues();
                view.goToStartingScreen();
            }else{
                view.showError("Confirmation password is not correct");
            }
        }else{
            view.showError("User not identified");
        }

    }

    private void resetValues(){
        this.username = null;
        this.NewPassword = null;
        this.NewPasswordConfirmation = null;
    }
}
