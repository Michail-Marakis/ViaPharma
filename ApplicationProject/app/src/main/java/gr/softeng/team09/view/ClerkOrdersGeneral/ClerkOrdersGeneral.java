package gr.softeng.team09.view.ClerkOrdersGeneral;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team09.R;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders.ClerkBackOrdersScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders.ClerkShowOrdersScreen;

/**
 * The type Clerk orders general.
 */
public class ClerkOrdersGeneral extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_orders_general);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button OrdersToHome = findViewById(R.id.button7homepage);
        OrdersToHome.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkOrdersGeneral.this, ClerkMainScreen.class);
            startActivity(intent);
        });

        Button buttonAllOrders = findViewById(R.id.buttonALLord);
        buttonAllOrders.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkOrdersGeneral.this, ClerkShowOrdersScreen.class);
            startActivity(intent);
        });

        Button buttonBackOrders = findViewById(R.id.buttonbackord);
        buttonBackOrders.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkOrdersGeneral.this, ClerkBackOrdersScreen.class);
            startActivity(intent);
        });
    }
}