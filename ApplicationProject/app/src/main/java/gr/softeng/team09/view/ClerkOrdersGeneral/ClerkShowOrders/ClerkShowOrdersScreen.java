package gr.softeng.team09.view.ClerkOrdersGeneral.ClerkShowOrders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockScreen;

/**
 * The type Clerk show orders screen.
 */
public class ClerkShowOrdersScreen extends AppCompatActivity
        implements ClerkShowOrdersView {

    private EditText editIdExecute, editIdCancel, editIdPostpone;

    private ListView listViewOrders;
    private ArrayAdapter<String> adapter;
    private final List<String> data = new ArrayList<>();

    private ClerkShowOrdersPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_show_orders_screen);

        presenter = new ViewModelProvider(this)
                .get(ClerkShowOrdersViewModel.class)
                .getPresenter();
        presenter.setView(this);

        //Inputs------------------------------------------
        editIdExecute = findViewById(R.id.idExecute);
        editIdCancel = findViewById(R.id.idCancel);
        editIdPostpone = findViewById(R.id.idPostpone);

        //List-----------------------------------------------------------------------------
        listViewOrders = findViewById(R.id.OrdersList);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, data);
        listViewOrders.setAdapter(adapter);

        listViewOrders.setOnItemClickListener(
                (p, v, pos, id) -> presenter.onOrderSelected(pos)
        );

        //Buttons------------------------------------------------------------------------------
        findViewById(R.id.buttonShowAllOrders)
                .setOnClickListener(v -> presenter.loadAllOrders());

        findViewById(R.id.buttonCompletedOrders)
                .setOnClickListener(v -> presenter.loadCompletedOrders());

        findViewById(R.id.buttonExecute)
                .setOnClickListener(v ->
                        presenter.executeById(
                                editIdExecute.getText().toString(), true));

        findViewById(R.id.buttonPostpone)
                .setOnClickListener(v ->
                        presenter.postponeById(
                                editIdPostpone.getText().toString()));

        findViewById(R.id.button3CancelClientWish)
                .setOnClickListener(v ->
                        presenter.cancelById(
                                editIdCancel.getText().toString()));

        findViewById(R.id.buttonbACKFromOrders)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, ClerkOrdersGeneral.class)));

        //Bottom nav---------------------------------------------------------------------
        findViewById(R.id.imageViewStock1)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, ClerkShowStockScreen.class)));

        findViewById(R.id.imageViewBatches)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, ClerkBatchesGeneralScreen.class)));

        findViewById(R.id.imageViewStats)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, ClerkShowStatsScreen.class)));

        findViewById(R.id.imageViewOrders)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, ClerkOrdersGeneral.class)));

        presenter.loadAllOrders();
    }

    // ---------------- VIEW ----------------

    @Override
    public void showOrders(List<String> ordersDisplay) {
        data.clear();
        data.addAll(ordersDisplay);
        listViewOrders.setVisibility(ListView.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showOrderDetails(Order order) {
        ListView list = new ListView(this);
        list.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                order.getLines()
        ));

        double total = presenter.calculateTotal(order);

        new AlertDialog.Builder(this)
                .setTitle("Order Details")
                .setMessage("\nOrder #" + order.getId() +
                        "\nTotal: " + String.format("%.2f €", total) +
                        "\nState: " + order.getState().toString())
                .setView(list)
                .setPositiveButton("Close", null)
                .show();
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
