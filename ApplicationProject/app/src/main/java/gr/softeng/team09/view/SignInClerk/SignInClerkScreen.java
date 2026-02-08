package gr.softeng.team09.view.SignInClerk;

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
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;
import gr.softeng.team09.view.SignInClerk.ClerkForget.ClerkForgetScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Sign in clerk screen.
 */
public class SignInClerkScreen extends AppCompatActivity implements SignInClerkView {

    private EditText editUsername;
    private EditText editPassword;

    private SignInClerkPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_clerk_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SignInClerkViewModel vm =
                new ViewModelProvider(this).get(SignInClerkViewModel.class);
        presenter = vm.getPresenter();
        presenter.setView(this);

        //views--------------------------------------------------

        editUsername = findViewById(R.id.PhoneClerk);
        editPassword = findViewById(R.id.EmailClerk);

        Button btnSignIn = findViewById(R.id.signupComplete);
        Button btnForgot = findViewById(R.id.buttonForgot);
        Button btnBack = findViewById(R.id.buttonToStart);

        //button implementation---------------------------------
        btnSignIn.setOnClickListener(v -> presenter.onSignIn(
                editUsername.getText().toString(),
                editPassword.getText().toString()
        ));

        btnForgot.setOnClickListener(v -> presenter.onForgotClicked());

        btnBack.setOnClickListener(v -> presenter.onBackClicked());
    }

    //helpers--------------------------------------------------------------------------

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToClerkMain() {
        startActivity(new Intent(this, ClerkMainScreen.class));
        finish();
    }

    @Override
    public void goToForgot() {
        startActivity(new Intent(this, ClerkForgetScreen.class));
    }

    @Override
    public void goToStart() {
        startActivity(new Intent(this, StartingScreenScreen.class));
        finish();
    }
}
