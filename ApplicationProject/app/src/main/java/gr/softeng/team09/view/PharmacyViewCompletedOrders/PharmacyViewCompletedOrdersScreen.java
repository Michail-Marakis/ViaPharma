package gr.softeng.team09.view.PharmacyViewCompletedOrders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartScreen;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsScreen;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersScreen;
import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersScreen;

/**
 * The type Pharmacy view completed orders screen.
 */
public class PharmacyViewCompletedOrdersScreen extends AppCompatActivity implements PharmacyViewCompletedOrdersView {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private PharmacyViewCompletedOrdersPresenter presenter;
    private PharmacyViewCompletedOrdersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_view_completed_orders_screen);

        list = findViewById(R.id.pending_orders_list);
        list.setOnItemClickListener((parent, view, position, id) -> {
            presenter.onSelected(position);
        });

        viewModel = new ViewModelProvider(this).get(PharmacyViewCompletedOrdersViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);


        presenter.loadCompletedOrders();

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(v -> goBack());

        //Buttons little down there---------------------------------------------
        ImageView btnToProducts = findViewById(R.id.imageViewStats);
        btnToProducts.setOnClickListener(v -> {
            Intent intent = new Intent(this, PharmacyShowProductsScreen.class);
            startActivity(intent);
        });

        ImageView btnToCart = findViewById(R.id.imageViewBatches);
        btnToCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, PharmacyCartScreen.class);
            startActivity(intent);
        });
        ImageView btnToDraft = findViewById(R.id.imageViewOrders);
        btnToDraft.setOnClickListener(v -> {
            Intent intent = new Intent(this, PharmacyViewDraftOrdersScreen.class);
            startActivity(intent);
        });

        ImageView btnToPending = findViewById(R.id.imageViewStock1);
        btnToPending.setOnClickListener(v -> {
            Intent intent = new Intent(this, PharmacyViewPendingOrdersScreen.class);
            startActivity(intent);
        });

        ImageView btnToCompleted = findViewById(R.id.imageViewStock5);
        btnToCompleted.setOnClickListener(v -> {
            Intent intent = new Intent(this, PharmacyViewCompletedOrdersScreen.class);
            startActivity(intent);
        });
    }

    private void goBack(){
        Intent intent = new Intent(this, PharmacyMainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showCompletedOrders(List<Order> orders) {
        List<String> l = builderString(orders);
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, l);
            list.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(l);
            adapter.notifyDataSetChanged();
        }

    }



    private List<String> builderString(List<Order> orders) {
        List<String> list = new ArrayList<>();
        for (Order o : orders){
            if(o.getState() == OrderState.COMPLETED) list.add("Completed Order ID: " + o.getId());
            if(o.getState() == OrderState.COMPLETEDBACKORDER) list.add("Completed BackOrder ID: " + o.getId());
        }
        return list;
    }

    @Override public void showError(String msg) {}

    @Override
    public void showOrderDetails(Order order) {
        ListView orderLinesList = new ListView(this);

        ArrayAdapter<Object> linesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>(order.getLines())
        );
        orderLinesList.setAdapter(linesAdapter);


        double total = presenter.calculateTotal(order);
        new AlertDialog.Builder(this)
                .setTitle("Order Details - ID: " + order.getId())
                .setMessage("State: " + order.getState() + "\n" +
                        "Total Cost: " + String.format("%.2f €", total) + "\n\n" +
                        "Products:")
                .setView(orderLinesList)
                .setPositiveButton("OK", null)
                .show();
    }
}