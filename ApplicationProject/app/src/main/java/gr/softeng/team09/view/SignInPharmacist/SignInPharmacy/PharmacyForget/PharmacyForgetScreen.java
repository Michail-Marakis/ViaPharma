package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget;

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
 * The type Pharmacy forget screen.
 */
public class PharmacyForgetScreen extends AppCompatActivity implements PharmacyForgetView{
    /**
     * The Presenter.
     */
    PharmacyForgetPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pharmacy_forget_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PharmacyForgetViewModel viewModel =
                new ViewModelProvider(this).get(PharmacyForgetViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        Button btnToBack = findViewById(R.id.buttonbackToSignInpharmacy);

        btnToBack.setOnClickListener(v-> {
            Intent intent = new Intent(PharmacyForgetScreen.this, PharmacistSignInScreen.class);
            startActivity(intent);
        });

        EditText editTextUsername = findViewById(R.id.putTelephonePharmacy);
        EditText editTextNewPass = findViewById(R.id.NewPassPharmacy);
        EditText editTextConfirmPass = findViewById(R.id.ConfirmPharmacy);

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

        Button saveChanges = findViewById(R.id.button2tSaveForgetpharmacy);
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