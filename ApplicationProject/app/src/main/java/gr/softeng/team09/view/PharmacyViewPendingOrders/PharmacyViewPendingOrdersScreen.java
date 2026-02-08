package gr.softeng.team09.view.PharmacyViewPendingOrders;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartScreen;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;

import gr.softeng.team09.R;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsScreen;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersScreen;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersScreen;

/**
 * The type Pharmacy view pending orders screen.
 */
public class PharmacyViewPendingOrdersScreen extends AppCompatActivity implements PharmacyViewPendingOrdersView{

    private ListView list;
    private ArrayAdapter<String> adapter;
    private PharmacyViewPendingOrdersPresenter presenter;
    private PharmacyViewPendingOrdersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pharmacy_view_pending_orders_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        list = findViewById(R.id.pending_orders_list);


        viewModel = new ViewModelProvider(this).get(PharmacyViewPendingOrdersViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Order selectedOrder = presenter.getOrders().get(position);
            presenter.initiateCancellation(selectedOrder);
        });

        presenter.loadPendingOrders();

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
            Intent intent = new Intent(this,PharmacyViewPendingOrdersScreen.class);
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
    public void showPendingOrders(List<Order> orders) {
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

    @Override
    public void showCancellationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancellation")
                .setMessage("Do you want to cancel this pending order?")
                .setPositiveButton("YES", (d, w) -> {
                    presenter.submitOrder(true);

                })
                .setNegativeButton("NO", (d, w) -> {
                    presenter.submitOrder(false);
                })
                .show();
    }

    private List<String> builderString(List<Order> orders) {
        List<String> list = new ArrayList<>();
        for (Order o : orders){
            list.add("Pending Order ID: " + o.getId());
        }
        return list;
    }

    @Override
    public void refreshOrders() {
        presenter.loadPendingOrders();
    }



    @Override public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
