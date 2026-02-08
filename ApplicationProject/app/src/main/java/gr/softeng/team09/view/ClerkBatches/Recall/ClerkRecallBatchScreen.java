package gr.softeng.team09.view.ClerkBatches.Recall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Pharmacy;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockScreen;

/**
 * The type Clerk recall batch screen.
 */
public class ClerkRecallBatchScreen extends AppCompatActivity implements ClerkRecallBatchView {

    private EditText editIdToRecall;
    private ListView listViewRecall;

    private ClerkRecallBatchPresenter presenter;

    private ArrayAdapter<String> adapter;
    private final List<String> rows = new ArrayList<>();

    /**
     * The Inventory.
     */
    Inventory inventory = MemoryStore.getInventory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_recall_batch);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ClerkRecallBatchViewModel viewModel =
                new ViewModelProvider(this).get(ClerkRecallBatchViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        // Views
        Button backBtn = findViewById(R.id.buttonBackRecall);
        Button recallBtn = findViewById(R.id.buttonRecallNow);

        editIdToRecall = findViewById(R.id.editBatchId);
        listViewRecall = findViewById(R.id.listViewBatches);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        listViewRecall.setAdapter(adapter);

        loadBatchesToList();

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ClerkRecallBatchScreen.this, ClerkBatchesGeneralScreen.class);
            startActivity(intent);
        });

        recallBtn.setOnClickListener(v -> {
            String txt = editIdToRecall.getText().toString().trim();
            if (txt.isEmpty()) {
                showError("Give batch id");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                showError("Invalid batch id");
                return;
            }

            presenter.setIdToRecall(id);
            presenter.readyToRecall();

            // refresh list
            loadBatchesToList();
            listViewRecall.setVisibility(View.VISIBLE);
        });

        // bottom navigation
        ImageView btnToStock = findViewById(R.id.imageViewStock1);
        btnToStock.setOnClickListener(v -> startActivity(new Intent(this, ClerkShowStockScreen.class)));

        ImageView btnToBatches = findViewById(R.id.imageViewOrders);
        btnToBatches.setOnClickListener(v -> startActivity(new Intent(this, ClerkOrdersGeneral.class)));

        ImageView btnToStats = findViewById(R.id.imageViewStats);
        btnToStats.setOnClickListener(v -> startActivity(new Intent(this, ClerkShowStatsScreen.class)));

        ImageView btnToOrders = findViewById(R.id.imageViewBatches);
        btnToOrders.setOnClickListener(v -> startActivity(new Intent(this, ClerkBatchesGeneralScreen.class)));
    }

    // ======================== view interface ========================

    @Override
    public void loadBatchesToList() {
        rows.clear();

        List<Batch> batches = inventory.getAllBatches();
        for (Batch b : batches) {
            if (b == null) continue;

            rows.add(buildRow(b));
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public String buildRow(Batch b) {
        String productName = "Unknown";
        if (b.getProduct() != null && b.getProduct().getName() != null) {
            productName = b.getProduct().getName();
        }

        int distributedTotal = b.getDistributedTotal();

        Map<Pharmacy, Integer> distMap = b.getDistributedQty();

        int count = 0;
        String pharmaciesText = "none";

        if (distMap != null && !distMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Pharmacy, Integer> e : distMap.entrySet()) {
                Pharmacy ph = e.getKey();
                Integer qty = e.getValue();
                if (ph == null) continue;

                if (sb.length() > 0) sb.append(", ");
                sb.append(ph.getName()).append(": ").append(qty == null ? 0 : qty);
                count++;
            }
            pharmaciesText = sb.toString();
        }

        return "Batch ID: " + b.getId()
                + "\nProduct: " + productName
                + "\nRemaining: " + b.getQuantity()
                + "\nDistributed total: " + distributedTotal
                + "\nDistributed to (" + count + "): " + pharmaciesText;
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recallBatchCall(int id) {
        inventory.recallBatch(id);
    }
}
