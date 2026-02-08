package gr.softeng.team09.view.SignUpClerk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import gr.softeng.team09.R;
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Sign up clerk screen.
 */
public class SignUpClerkScreen extends AppCompatActivity
        implements SignUpClerkView {

    private SignUpClerkPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_clerk_screen);

        SignUpClerkViewModel vm =
                new ViewModelProvider(this).get(SignUpClerkViewModel.class);
        presenter = vm.getPresenter();
        presenter.setView(this);

        EditText name = findViewById(R.id.nameClerk);
        EditText surname = findViewById(R.id.SurnameClerk);
        EditText phone = findViewById(R.id.PhoneClerk);
        EditText email = findViewById(R.id.EmailClerk);
        EditText password = findViewById(R.id.idPASSOWRD);

        Button signup = findViewById(R.id.signupComplete);
        Button back = findViewById(R.id.buttonBackToMainSignUpClerk);

        signup.setOnClickListener(v ->
                presenter.onSignUp(
                        name.getText().toString(),
                        surname.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                )
        );

        back.setOnClickListener(v ->
                startActivity(new Intent(this, StartingScreenScreen.class))
        );
    }

    @Override
    public void showSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishSignUp() {
        startActivity(new Intent(this, ClerkMainScreen.class));
        finish();
    }
}
