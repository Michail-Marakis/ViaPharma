package gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import gr.softeng.team09.R;
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;
import gr.softeng.team09.view.SignInPharmacist.PharmacistSignInScreen;
import gr.softeng.team09.view.SignUpClerk.SignUpClerkScreen;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.SignUpPharmacist.PharmacistSignUpScreen;

/**
 * The type Pharmacy sign up screen.
 */
public class PharmacySignUpScreen extends AppCompatActivity implements PharmacySignUpView {

    private PharmacySignUpPresenter presenter;
    private PharmacySignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_sign_up_screen);

        viewModel = new ViewModelProvider(this).get(PharmacySignUpViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        presenter.checkPharmacistLoggedIn();

        EditText nametxt = findViewById(R.id.signup_pharmacy_name_edit_text);
        EditText afmtxt = findViewById(R.id.sing_up_afm_edittext);
        EditText streettxt = findViewById(R.id.street_edit_text);
        EditText strnumbertxt = findViewById(R.id.street_number_edit_text);
        EditText regiontxt = findViewById(R.id.region_edit_text);
        EditText postaltxt = findViewById(R.id.postal_code_edit_text);
        EditText passwordtxt = findViewById(R.id.editTextTextPassword2);
        Button signupbtn = findViewById(R.id.sign_up_pharmacy_button);
        Button btnToBack = findViewById(R.id.buttonBackTomain);

        signupbtn.setOnClickListener(v -> presenter.onSignUp(
                nametxt.getText().toString(),
                afmtxt.getText().toString(),
                streettxt.getText().toString(),
                strnumbertxt.getText().toString(),
                regiontxt.getText().toString(),
                postaltxt.getText().toString(),
                passwordtxt.getText().toString()
        ));

        btnToBack.setOnClickListener(v-> {
            Intent intent = new Intent(PharmacySignUpScreen.this, PharmacistSignUpScreen.class);
            startActivity(intent);
        });
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishSignUp() {
        Intent intent = new Intent(this, PharmacyMainScreen.class);
        startActivity(intent);
    }

}