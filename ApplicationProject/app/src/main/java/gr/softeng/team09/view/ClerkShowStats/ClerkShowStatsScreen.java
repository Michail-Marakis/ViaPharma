package gr.softeng.team09.view.ClerkShowStats;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockScreen;

/**
 * The type Clerk show stats screen.
 */
public class ClerkShowStatsScreen extends AppCompatActivity implements ClerkShowStatsView {

    private EditText editAfmOrders;
    private EditText editStartTime;
    private EditText editEndTime;

    private ListView listViewOrders;
    private TextView textViewRevenue;

    private final List<String> listDisplay = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private ClerkShowStatsViewModel viewModel;
    private ClerkShowStatsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_show_stats_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // -------- ViewModel / Presenter --------
        viewModel = new ViewModelProvider(this).get(ClerkShowStatsViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        // -------- Views --------
        Button btnHome = findViewById(R.id.button6homepage);
        Button btnShowOrders = findViewById(R.id.buttonOrderPerPharmacy);
        Button btnShowRevenue = findViewById(R.id.buttonrevenueByClient);
        Button btnShowTotalRevenue = findViewById(R.id.buttontotalRevenue);
        Button btnShowSalesTimeStamp = findViewById(R.id.button5timestamp);

        editAfmOrders = findViewById(R.id.editTextAFM);
        editStartTime = findViewById(R.id.inputStartDate);
        editEndTime = findViewById(R.id.inputEndDate);

        listViewOrders = findViewById(R.id.statsList);
        textViewRevenue = findViewById(R.id.textViewtotalrevenue2);

        //---- One List adapter (για όλα) --------
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDisplay);
        listViewOrders.setAdapter(adapter);
        listViewOrders.setVisibility(android.view.View.VISIBLE);

        //------------------------------ Buttons---------------------------------------------------
        btnShowOrders.setOnClickListener(v -> {
            presenter.showOrdersByPharmacy(editAfmOrders.getText().toString());
        });

        btnShowRevenue.setOnClickListener(v -> {
            presenter.showRevenueByPharmacy(editAfmOrders.getText().toString());
        });

        btnShowSalesTimeStamp.setOnClickListener(v -> {
            presenter.showSalesBetween(
                    editStartTime.getText().toString(),
                    editEndTime.getText().toString()
            );
        });

        btnShowTotalRevenue.setOnClickListener(v -> presenter.showTotalRevenue());

        //Buttons little down there---------------------------------------------
        ImageView btnToStock = findViewById(R.id.imageViewStock1);
        btnToStock.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClerkShowStockScreen.class);
            startActivity(intent);
        });

        ImageView btnToBatches = findViewById(R.id.imageViewOrders);
        btnToBatches.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClerkOrdersGeneral.class);
            startActivity(intent);
        });
        ImageView btnToStats = findViewById(R.id.imageViewStats);
        btnToStats.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClerkShowStatsScreen.class);
            startActivity(intent);
        });

        ImageView btnToOrders = findViewById(R.id.imageViewBatches);
        btnToOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClerkBatchesGeneralScreen.class);
            startActivity(intent);
        });

        //navigation-----------------------------------------------------------------------
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, ClerkMainScreen.class)));

        listViewOrders.setOnItemClickListener((parent, view, position, id) -> {
            Order o = presenter.getOrderAt(position);
            if (o == null) return;

            showOrderDetailsDialog(o);
        });

    }

    // ================== ClerkShowStatsView ==================

    @Override
    public void showOrders(List<String> display) {
        listDisplay.clear();
        listDisplay.addAll(display);
        adapter.notifyDataSetChanged();
        listViewOrders.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void showRevenue(String msg) {
        textViewRevenue.setText(msg);
        textViewRevenue.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void showTotalRevenue(String msg) {
        textViewRevenue.setText(msg);
        textViewRevenue.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void showSalesBetween(List<String> display) {
        listDisplay.clear();
        listDisplay.addAll(display);
        adapter.notifyDataSetChanged();
        listViewOrders.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void showErrorOnOrdersInput(String msg) {
        editAfmOrders.setError(msg);
    }

    @Override
    public void showErrorOnRevenueInput(String msg) {
        editAfmOrders.setError(msg);
    }

    @Override
    public void showErrorOnStartDateInput(String msg) {
        editStartTime.setError(msg);
    }

    @Override
    public void showErrorOnEndDateInput(String msg) {
        editEndTime.setError(msg);
    }

    @Override
    public void clearOrders() {
        listDisplay.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearSalesBetween() {
        listDisplay.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showOrderDetailsDialog(Order o) {

        StringBuilder sb = new StringBuilder();

        sb.append("Order ID: ").append(o.getId()).append("\n");
        sb.append("Pharmacy: ").append(o.getPharmacy().getName()).append("\n");
        sb.append("Type: ");

        if (o.getState() == OrderState.COMPLETEDBACKORDER) {
            sb.append("BACKORDER (shipment)\n");
        } else {
            sb.append("ORDER\n");
        }

        sb.append("State: ").append(o.getState()).append("\n\n");
        sb.append("Products:\n");

        double total = 0.0;
        for (OrderLine l : o.getLines()) {
            double lineTotal = l.getQuantity() * l.getProduct().getPriceWithVAT();
            total += lineTotal;

            sb.append("- ")
                    .append(l.getProduct().getName())
                    .append(" x")
                    .append(l.getQuantity())
                    .append(" = ")
                    .append(String.format("%.2f", lineTotal))
                    .append(" €\n");
        }

        sb.append("\nTotal: ").append(String.format("%.2f", total)).append(" €");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Order Details")
                .setMessage(sb.toString())
                .setPositiveButton("OK", null)
                .show();
    }

}
