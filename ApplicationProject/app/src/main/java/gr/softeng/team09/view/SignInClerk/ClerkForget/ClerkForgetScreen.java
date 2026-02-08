package gr.softeng.team09.view.SignInClerk.ClerkForget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import gr.softeng.team09.R;
import gr.softeng.team09.view.SignInPharmacist.PharmacistSignInScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Clerk forget screen.
 */
public class ClerkForgetScreen extends AppCompatActivity implements ClerkForgetView{

    /**
     * The Presenter.
     */
    ClerkForgetPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_forget_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ClerkForgetViewModel viewModel =
                new ViewModelProvider(this).get(ClerkForgetViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        Button btnToBack = findViewById(R.id.backfromforgetclerk);

        btnToBack.setOnClickListener(v-> {
            Intent intent = new Intent(this, PharmacistSignInScreen.class);
            startActivity(intent);
        });

        EditText editTextUsername = findViewById(R.id.inputusernameForgetClerk);
        EditText editTextNewPass = findViewById(R.id.inputNewPassClerk);
        EditText editTextConfirmPass = findViewById(R.id.ConfirmpassClerkforget);

        editTextUsername.setOnFocusChangeListener((v, hasfocus)->{
            if(!hasfocus){
                String user = editTextUsername.getText().toString().trim();
                if(!user.isEmpty()){
                    presenter.setUsername(user);
                }
            }
        });
        editTextNewPass.setOnFocusChangeListener((v, hasfocus)->{
            if(!hasfocus){
                String newpass = editTextNewPass.getText().toString().trim();
                if(!newpass.isEmpty()){
                    presenter.setNewPassword(newpass);
                }
            }
        });
        editTextConfirmPass.setOnFocusChangeListener((v, hasfocus)->{
            if(!hasfocus){
                String conf = editTextConfirmPass.getText().toString().trim();
                if(!conf.isEmpty()){
                    presenter.setNewPasswordConfirmation(conf);
                }
            }
        });

        //button to save----------------------------------------------------------------------

        Button saveChanges = findViewById(R.id.saveChangesForgetClerk);
        saveChanges.setOnClickListener(v->{
            presenter.authenticationSession();
        });


    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void goToStartingScreen(){
        Intent intent = new Intent(this, StartingScreenScreen.class);
        startActivity(intent);
    }
}