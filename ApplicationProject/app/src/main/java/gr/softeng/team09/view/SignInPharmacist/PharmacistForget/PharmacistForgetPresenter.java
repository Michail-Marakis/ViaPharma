package gr.softeng.team09.view.SignInPharmacist.PharmacistForget;

import java.util.Objects;

import gr.softeng.team09.domain.Pharmacist;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.PharmacistDAOMemory;

/**
 * The type Pharmacist forget presenter.
 */
public class PharmacistForgetPresenter {
    /**
     * The View.
     */
    PharmacistForgetView view;
    private String NewPassword = null;
    private String NewPasswordConfirmation = null;

    private String username = null;

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(PharmacistForgetView view) {
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

        Pharmacist tofound = MemoryStore.getByEmail(username);
        if(PharmacistDAOMemory.entities.contains(tofound)){
            if(Objects.equals(NewPassword, NewPasswordConfirmation)){
                MemoryStore.getPharmacist().SetCode(NewPassword);
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
