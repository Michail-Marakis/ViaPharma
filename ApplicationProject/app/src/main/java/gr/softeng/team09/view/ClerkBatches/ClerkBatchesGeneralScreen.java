package gr.softeng.team09.view.ClerkBatches;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gr.softeng.team09.R;
import gr.softeng.team09.view.ClerkBatches.Recall.ClerkRecallBatchScreen;
import gr.softeng.team09.view.ClerkBatches.Receive.ClerkReceiveBatchScreen;
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;

/**
 * The type Clerk batches general screen.
 */
public class ClerkBatchesGeneralScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_batches_general_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button batchesToHomePage = findViewById(R.id.button4HOMEPAGE);
        batchesToHomePage.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkBatchesGeneralScreen.this, ClerkMainScreen.class);
            startActivity(intent);
        });

        Button ButtonRecall  = findViewById(R.id.buttonRecall);
        ButtonRecall.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkBatchesGeneralScreen.this, ClerkRecallBatchScreen.class);
            startActivity(intent);
        });

        Button ButtonReceive = findViewById(R.id.buttonReceive);
        ButtonReceive.setOnClickListener(v-> {
            Intent intent = new Intent(ClerkBatchesGeneralScreen.this, ClerkReceiveBatchScreen.class);
            startActivity(intent);
        });
    }
}