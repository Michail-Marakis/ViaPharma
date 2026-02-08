package gr.softeng.team09.view.SignInPharmacist.SignInPharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import gr.softeng.team09.R;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.SignInPharmacist.PharmacistSignInScreen;
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacyForget.PharmacyForgetScreen;
import gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy.PharmacySignUpScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Pharmacy sign in screen.
 */
public class PharmacySignInScreen extends AppCompatActivity implements PharmacySignInView {

    private PharmacySignInPresenter presenter;
    private PharmacySignInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_sign_in_screen);

        viewModel = new ViewModelProvider(this).get(PharmacySignInViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        EditText nametxt = findViewById(R.id.name_pharmacy_sign_in);
        EditText afmtxt = findViewById(R.id.afm_edit_text);
        EditText passwordtxt = findViewById(R.id.editTextTextPassword);
        Button signbtn = findViewById(R.id.prosignin_button);
        Button gotosignupbtn = findViewById(R.id.go_to_signup_button);
        Button btnToBackpharma = findViewById(R.id.buttonTopharma);
        Button forgBtn = findViewById(R.id.button2ForgetPharmacyPass);

        signbtn.setOnClickListener(v -> presenter.onSignIn(
                nametxt.getText().toString(),
                afmtxt.getText().toString(),
                passwordtxt.getText().toString()
        ));

        gotosignupbtn.setOnClickListener(v -> presenter.onSignUpClicked());

        btnToBackpharma.setOnClickListener(v-> {
            Intent intent = new Intent(PharmacySignInScreen.this, PharmacistSignInScreen.class);
            startActivity(intent);
        });

        forgBtn.setOnClickListener(v-> {
            startActivity(new Intent(this, PharmacyForgetScreen.class));
        });

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, PharmacyMainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(this, PharmacySignUpScreen.class);
        startActivity(intent);
    }
}

