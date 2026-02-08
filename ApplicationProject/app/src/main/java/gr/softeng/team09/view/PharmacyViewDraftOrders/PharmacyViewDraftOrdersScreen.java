package gr.softeng.team09.view.PharmacyViewDraftOrders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartScreen;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsScreen;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersScreen;
import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersScreen;

/**
 * The type Pharmacy view draft orders screen.
 */
public class PharmacyViewDraftOrdersScreen extends AppCompatActivity implements PharmacyViewDraftOrdersView {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private PharmacyViewDraftOrdersPresenter presenter;
    private PharmacyViewDraftOrdersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_view_draft_orders_screen);

        list = findViewById(R.id.draft_orders_list);
        viewModel = new ViewModelProvider(this).get(PharmacyViewDraftOrdersViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        presenter.loadDraftOrders();

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

    private void goBack() {
        Intent intent = new Intent(this, PharmacyMainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDraftOrders(List<Order> draftOrders) {

        List<String> l = builderString(draftOrders);

        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, l);
            list.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(l);
            adapter.notifyDataSetChanged();
        }

        list.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < draftOrders.size()) {
                Order selected = draftOrders.get(position);
                presenter.onDraftOrderSelected(selected);
            }
        });
    }

    /**
     * Builder string list.
     *
     * @param draftOrders the draft orders
     * @return the list
     */
    public List<String> builderString(List<Order> draftOrders){
        List<String> list = new ArrayList<>();
        for (Order o : draftOrders){
            String x = "Order ID: " + o.getId() + " - State: " + o.getState();
            list.add(x);
        }
        return list;
    }


    @Override
    public void navigateToCart() {
        Intent intent = new Intent(this, PharmacyCartScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String msg) { }
}