package gr.softeng.team09.view.PharmacyMain;

import static gr.softeng.team09.util.LocaleHelper.setLocale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team09.R;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.ClerkEmails.ClerkEmailScreen;
import gr.softeng.team09.view.PharmacistEmails.PharmacistEmailsScreen;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartScreen;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsScreen;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersScreen;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersScreen;
import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersScreen;
import gr.softeng.team09.view.StartingScreen.StartingScreenScreen;


/**
 * The type Pharmacy main screen.
 */
public class PharmacyMainScreen extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pharmacy_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {  finish(); });

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

        ImageView buttontoInbox = findViewById(R.id.imageView2EmailsPharma);
        buttontoInbox.setOnClickListener(v->{
            Intent intent = new Intent(this, PharmacistEmailsScreen.class);
            startActivity(intent);
        });
        TextView textViewName = findViewById(R.id.textViewname);
        TextView textViewAFM = findViewById(R.id.textView10afm);
        TextView textViewEmail = findViewById(R.id.textView9email);
        TextView textPharmName = findViewById(R.id.textView12namepharm);

        ImageView imageView= findViewById(R.id.pharmacy_account);
        imageView.setOnClickListener(v->{
            if (textViewName.getVisibility()==TextView.VISIBLE){
                textViewName.setVisibility(TextView.INVISIBLE);
                textViewAFM.setVisibility(TextView.INVISIBLE);
                textViewEmail.setVisibility(TextView.INVISIBLE);
                textPharmName.setVisibility(TextView.INVISIBLE);
            } else {
                textViewName.setVisibility(TextView.VISIBLE);
                textViewAFM.setVisibility(TextView.VISIBLE);
                textViewEmail.setVisibility(TextView.VISIBLE);
                textPharmName.setVisibility(TextView.VISIBLE);
            }
        });

        Button cartButton = findViewById(R.id.CartButton);
        cartButton.setOnClickListener(v -> goToCart());

        Button productsButton = findViewById(R.id.ProductsButton);
        productsButton.setOnClickListener(v-> goToProducts());

        Button draftOrdersButton = findViewById(R.id.DraftOrdersButton);
        draftOrdersButton.setOnClickListener(v-> goToDraftOrders());

        Button pendingOrdersButton = findViewById(R.id.pending_button);
        pendingOrdersButton.setOnClickListener(v -> goToPendingOrders());

        Button completedOrdersButton = findViewById(R.id.CompletedOrdersButton);
        completedOrdersButton.setOnClickListener(v-> goToCompletedOrders());

        Button logoutButton = findViewById(R.id.logout_pharmacy_button);
        logoutButton.setOnClickListener(v->{
            MemoryStore.clearSessionPharma();
            logOut();
        });

        if (MemoryStore.getPharmacist() == null || MemoryStore.getPharmacy() == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
            logOut();
            return;
        }

        //logged in information top corner----------------------------------------------------------------------------------

        String textNamePharmacist = MemoryStore.getPharmacist().getFirstName() + " " + MemoryStore.getPharmacist().getLastName();
        textViewName.setText(textNamePharmacist);
        textViewEmail.setText(MemoryStore.getPharmacist().getEmail());
        textPharmName.setText(MemoryStore.getPharmacy().getName());
        textViewAFM.setText(String.valueOf(MemoryStore.getPharmacy().getAfm()));

    }

    /**
     * Log out.
     */
    protected void logOut() {
        Intent intent1 = new Intent(this, StartingScreenScreen.class);
        launcher.launch(intent1);
    }

    /**
     * Go to cart.
     */
    protected void goToCart(){
        Intent intent1 = new Intent(this, PharmacyCartScreen.class);
        launcher.launch(intent1);
    }

    /**
     * Go to products.
     */
    protected void goToProducts(){
        Intent intent2 = new Intent(this, PharmacyShowProductsScreen.class);
        launcher.launch(intent2);
    }

    /**
     * Go to draft orders.
     */
    protected void goToDraftOrders(){
        Intent intent3 = new Intent(this, PharmacyViewDraftOrdersScreen.class);
        launcher.launch(intent3);
    }

    private void goToPendingOrders() {
        Intent intent1 = new Intent(this, PharmacyViewPendingOrdersScreen.class);
        launcher.launch(intent1);
    }

    /**
     * Go to completed orders.
     */
    protected void goToCompletedOrders(){
        Intent intent4 = new Intent(this, PharmacyViewCompletedOrdersScreen.class);
        launcher.launch(intent4);
    }

}