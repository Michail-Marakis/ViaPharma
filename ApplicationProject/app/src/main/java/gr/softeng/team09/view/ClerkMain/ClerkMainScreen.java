package gr.softeng.team09.view.ClerkMain;

import static gr.softeng.team09.util.LocaleHelper.setLocale;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team09.R;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkEmails.ClerkEmailScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
//import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;

/**
 * The type Clerk main screen.
 */
public class ClerkMainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_main_screen);
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

        Button batchesCtrl = findViewById(R.id.buttonBatches);
        batchesCtrl.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkMainScreen.this, ClerkBatchesGeneralScreen.class);
            startActivity(intent);
        });

        Button buttonOrdersGeneral = findViewById(R.id.buttonOrdersGeneral);
        buttonOrdersGeneral.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkMainScreen.this, ClerkOrdersGeneral.class);
            startActivity(intent);
        });

        ImageView buttontoInbox = findViewById(R.id.imageViewInbox);
        buttontoInbox.setOnClickListener(v->{
            Intent intent = new Intent(this, ClerkEmailScreen.class);
            startActivity(intent);
        });

        Button buttonStock = findViewById(R.id.buttonStock);
        buttonStock.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkMainScreen.this, ClerkShowStockScreen.class);
            startActivity(intent);
        });

        Button buttonStats = findViewById(R.id.buttonStats);
        buttonStats.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkMainScreen.this, ClerkShowStatsScreen.class);
            startActivity(intent);
        });


        Button buttonLogout = findViewById(R.id.logout_clerk_button);
        buttonLogout.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkMainScreen.this, StartingScreenScreen.class);
            startActivity(intent);
            MemoryStore.clearSessionClerk();
        });

        //logged in information top right corner----------------------------------------------------------

        TextView textViewNameClerk = findViewById(R.id.textView11);
        TextView textViewEmailClerk = findViewById(R.id.textView14);

        ImageView imageView= findViewById(R.id.account_clerk);
        imageView.setOnClickListener(v->{
            if (textViewNameClerk.getVisibility()==TextView.VISIBLE){
                textViewNameClerk.setVisibility(TextView.INVISIBLE);
                textViewEmailClerk.setVisibility(TextView.INVISIBLE);
            } else {
                textViewNameClerk.setVisibility(TextView.VISIBLE);
                textViewEmailClerk.setVisibility(TextView.VISIBLE);
            }
        });

        textViewEmailClerk.setText(MemoryStore.getClerk().getEmail());
        textViewNameClerk.setText(MemoryStore.getClerk().getName());

    }
}