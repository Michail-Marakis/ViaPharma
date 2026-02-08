package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkBackOrders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Backorder;
import gr.softeng.team09.domain.BackorderLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockScreen;

/**
 * The type Clerk back orders screen.
 */
public class ClerkBackOrdersScreen extends AppCompatActivity implements ClerkBackOrdersView {

    private EditText editInputInforationBackOrder;
    private ListView listViewPersonalBackOrders;

    private ArrayAdapter<String> adapter;
    private final List<String> rows = new ArrayList<>();

    private ClerkBackOrdersPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_back_orders_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ClerkBackOrdersViewModel viewModel =
                new ViewModelProvider(this).get(ClerkBackOrdersViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        Button btnBack = findViewById(R.id.buttonbackBACKORDER);
        Button btnActive = findViewById(R.id.button2Back);
        Button btnAll = findViewById(R.id.Allbackorders);
        Button btnSearch = findViewById(R.id.button2PressToSeeBackOrder);

        editInputInforationBackOrder = findViewById(R.id.inputForBackOrderInformation);
        listViewPersonalBackOrders = findViewById(R.id.personalbackorders);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        listViewPersonalBackOrders.setAdapter(adapter);


        presenter.loadActiveBackorders();
        renderListFromPresenter();


        listViewPersonalBackOrders.setOnItemClickListener((parent, view, position, id) -> {
            Backorder selected = presenter.getBackorderAt(position);
            if (selected == null) return;
            showBackorderDetailsDialog(selected);
        });

        btnBack.setOnClickListener(v ->
                startActivity(new Intent(this, ClerkOrdersGeneral.class)));

        btnActive.setOnClickListener(v -> {
            presenter.loadActiveBackorders();
            renderListFromPresenter();
        });

        btnAll.setOnClickListener(v -> {
            presenter.loadAllBackorders();
            renderListFromPresenter();
        });

        btnSearch.setOnClickListener(v -> {
            String afm = editInputInforationBackOrder.getText().toString().trim();
            if (afm.isEmpty()) {
                showError("AFM required");
                return;
            }
            presenter.setPharmAFM(afm);
            presenter.findAllBackOrdersFromPharm();
            renderListFromPresenter();
        });

        // navigation
        ImageView btnToStock = findViewById(R.id.imageViewStock1);
        btnToStock.setOnClickListener(v ->
                startActivity(new Intent(this, ClerkShowStockScreen.class)));

        ImageView btnToOrders = findViewById(R.id.imageViewOrders);
        btnToOrders.setOnClickListener(v ->
                startActivity(new Intent(this, ClerkOrdersGeneral.class)));

        ImageView btnToStats = findViewById(R.id.imageViewStats);
        btnToStats.setOnClickListener(v ->
                startActivity(new Intent(this, ClerkShowStatsScreen.class)));

        ImageView btnToBatches = findViewById(R.id.imageViewBatches);
        btnToBatches.setOnClickListener(v ->
                startActivity(new Intent(this, ClerkBatchesGeneralScreen.class)));
    }


    private void renderListFromPresenter() {
        rows.clear();
        List<Backorder> list = presenter.getLastShownBackorders();
        for (Backorder b : list) {
            rows.add(buildRow(b));
        }
        adapter.notifyDataSetChanged();
    }

    // ---------------- DIALOG ----------------
    private void showBackorderDetailsDialog(Backorder b) {
        StringBuilder sb = new StringBuilder();

        sb.append("Type: BACKORDER\n\n");
        sb.append("Backorder ID: ").append(b.getId()).append("\n\n");
        sb.append("Pharmacy: ").append(b.getPharmacy().getName()).append("\n");
        sb.append("AFM: ").append(b.getPharmacy().getAfm()).append("\n\n");
        sb.append("Pharmacist: ")
                .append(b.getPharmacist().getFirstName())
                .append(" ")
                .append(b.getPharmacist().getLastName())
                .append("\n\n");
        sb.append("State: ").append(b.getState()).append("\n\n");
        sb.append("Order Lines:\n");

        for (BackorderLine ol : b.getLines()) {
            sb.append("- ")
                    .append(ol.getProduct().getName())
                    .append(" | requested: ")
                    .append(ol.getQuantityRequested())
                    .append(" | fulfilled: ")
                    .append(ol.getQuantityFulfilled())
                    .append(" | pending: ")
                    .append(ol.getQuantityPending())
                    .append("\n");
        }


        new AlertDialog.Builder(this)
                .setTitle("Backorder details")
                .setMessage(sb.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    // ---------------- View methods ----------------

    @Override
    public String buildRow(Backorder back) {
        String status;
        if (back.getState() == OrderState.BACKORDER) {
            status = "ACTIVE";
        } else if (back.getState() == OrderState.COMPLETEDBACKORDER) {
            status = "COMPLETED";
        } else {
            status = back.getState().toString();
        }

        return "Pharmacy: " + back.getPharmacy().getName()
                + "\nBackorder ID: " + back.getId()
                + "\nStatus: " + status;
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
