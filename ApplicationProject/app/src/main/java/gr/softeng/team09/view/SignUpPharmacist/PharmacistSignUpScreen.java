package gr.softeng.team09.view.SignUpPharmacist;

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
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacySignInScreen;
import gr.softeng.team09.view.SignUpPharmacist.SignUpPharmacy.PharmacySignUpScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Pharmacist sign up screen.
 */
public class PharmacistSignUpScreen extends AppCompatActivity implements PharmacistSignUpView {

    private PharmacistSignUpPresenter presenter;
    private PharmacistSignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_pharmacist);

        viewModel = new ViewModelProvider(this).get(PharmacistSignUpViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        EditText nametxt = findViewById(R.id.first_name_pharmacist_edit_text);
        EditText lastnametxt = findViewById(R.id.last_name_pharmacist_edit_text);
        EditText emailtxt = findViewById(R.id.email_pharmacist_edit_text);
        EditText phonetxt = findViewById(R.id.phone_edit_text);
        EditText passwordtxt = findViewById(R.id.password_sing_up_edit_text);
        Button signupbtn = findViewById(R.id.sign_up_pharmacist_buton);
        Button btnToHome = findViewById(R.id.buttonTomain);

        signupbtn.setOnClickListener(v -> presenter.onSignUp(
                nametxt.getText().toString(),
                lastnametxt.getText().toString(),
                emailtxt.getText().toString(),
                phonetxt.getText().toString(),
                passwordtxt.getText().toString()
        ));

        btnToHome.setOnClickListener(v-> {
            Intent intent = new Intent(PharmacistSignUpScreen.this, StartingScreenScreen.class);
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
        startActivity(new Intent(this, PharmacySignInScreen.class));
        finish();
    }
}