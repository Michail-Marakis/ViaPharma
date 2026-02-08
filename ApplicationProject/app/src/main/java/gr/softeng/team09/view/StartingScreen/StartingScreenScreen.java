package gr.softeng.team09.view.StartingScreen;

import static gr.softeng.team09.util.LocaleHelper.setLocale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team09.R;

//import gr.softeng.team09.view.SignInClerk.SignInClerkScreen;
import gr.softeng.team09.memorydao.BackorderDAOMemory;
import gr.softeng.team09.memorydao.BatchDAOMemory;
import gr.softeng.team09.memorydao.InventoryLineDAOMemory;
import gr.softeng.team09.memorydao.MemoryInitializer;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.memorydao.ReservationDAOMemory;
import gr.softeng.team09.view.SignInClerk.SignInClerkScreen;
import gr.softeng.team09.view.SignInPharmacist.PharmacistSignInScreen;
//import gr.softeng.team09.view.SignUpClerk.SignUpClerkScreen;
import gr.softeng.team09.view.SignUpClerk.SignUpClerkScreen;
import gr.softeng.team09.view.SignUpPharmacist.PharmacistSignUpScreen;


/**
 * The type Starting screen screen.
 */
public class StartingScreenScreen extends AppCompatActivity {

    private static boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_starting_screen_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView gr= findViewById(R.id.greek_img);
        gr.setOnClickListener(v->{
            setLocale(this, "el");
        });
        ImageView en= findViewById(R.id.english_img);
        en.setOnClickListener(v->{
            setLocale(this, "en");
        });
        ImageView fr= findViewById(R.id.french_img);
        fr.setOnClickListener(v->{
            setLocale(this, "fr");
        });


        if (!initialized) {
            MemoryInitializer init = new MemoryInitializer();
            init.prepareData();
            initialized = true;
        }



        Button singInPharmacistbtn = findViewById(R.id.sign_in_pharmacist_button);
        Button singUpPharmacistbtn = findViewById(R.id.sing_up_pharmacist_button);
        Button singInClerkbtn = findViewById(R.id.sign_in_clerk_button);
        Button singUpClerkbtn = findViewById(R.id.sign_up_clerk_button);

        singInPharmacistbtn.setOnClickListener(v-> {Intent intent = new Intent(this, PharmacistSignInScreen.class);
                                                startActivity(intent);});
        singUpPharmacistbtn.setOnClickListener(v-> {Intent intent = new Intent(this,PharmacistSignUpScreen.class);
            startActivity(intent);});
        singInClerkbtn.setOnClickListener(v-> {Intent intent = new Intent(this, SignInClerkScreen.class);
            startActivity(intent);});
        singUpClerkbtn.setOnClickListener(v-> {Intent intent = new Intent(this, SignUpClerkScreen.class);
            startActivity(intent);});

    }
}