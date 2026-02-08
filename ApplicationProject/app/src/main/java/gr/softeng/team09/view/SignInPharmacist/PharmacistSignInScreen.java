package gr.softeng.team09.view.SignInPharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import gr.softeng.team09.R;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.SignInPharmacist.PharmacistForget.PharmacistForgetScreen;
import gr.softeng.team09.view.SignInPharmacist.SignInPharmacy.PharmacySignInScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Pharmacist sign in screen.
 */
public class PharmacistSignInScreen extends AppCompatActivity implements PharmacistSignInView {

    private PharmacistSignInPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_pharmacist);

        PharmacistSignInViewModel viewModel = new ViewModelProvider(this).get(PharmacistSignInViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        EditText emailTxt = findViewById(R.id.username_sign_in_pharmacist);
        EditText passwordTxt = findViewById(R.id.password_sign_in_pharmacist);
        Button backBtn = findViewById(R.id.buttonToback);
        Button signBtn = findViewById(R.id.sign_in_button);
        Button forgotBtn = findViewById(R.id.forgot_button);

        backBtn.setOnClickListener(v-> goBack());

        signBtn.setOnClickListener(v ->
                presenter.onSignIn(
                        emailTxt.getText().toString().trim(),
                        passwordTxt.getText().toString().trim()
                )
        );

        forgotBtn.setOnClickListener(v -> presenter.onForgotPassword());
    }

    private void goBack() {
        Intent intent = new Intent(this, StartingScreenScreen.class);
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToPharmacy() {
        startActivity(new Intent(this, PharmacySignInScreen.class));
    }

    @Override
    public void showForgotPasswordDialog() {
        startActivity(new Intent(this, PharmacistForgetScreen.class));
        Toast.makeText(this, "Password recovery...", Toast.LENGTH_SHORT).show();
    }
}