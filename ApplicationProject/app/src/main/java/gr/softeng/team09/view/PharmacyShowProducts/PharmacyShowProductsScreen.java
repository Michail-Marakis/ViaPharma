package gr.softeng.team09.view.PharmacyShowProducts;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast; // Import Toast

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList; // Import ArrayList
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.view.PharmacyCart.PharmacyCartScreen;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersScreen;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersScreen;
import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersScreen;

/**
 * The type Pharmacy show products screen.
 */
public class PharmacyShowProductsScreen extends AppCompatActivity implements PharmacyShowProductsView {

    private ListView list;
    private ArrayAdapter<InventoryLine> adapter;
    private PharmacyShowProductsPresenter presenter;
    private PharmacyShowProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_show_products_screen);


        list = findViewById(R.id.products_list);

        // presenter
        viewModel = new ViewModelProvider(this).get(PharmacyShowProductViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        list.setOnItemClickListener((parent, view, position, id) -> {
            InventoryLine selectedLine = (InventoryLine) parent.getItemAtPosition(position);
            selectQuantityDialog(null, selectedLine.getProduct());
        });

        presenter.loadCurrentItems();
        showProducts(presenter.getAllItems());


        // Buttons
        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(v -> goBack());

        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> showSortOptionsDialog());

        Button searchButton = findViewById(R.id.eofSearchButton);
        EditText searchEofEditText = findViewById(R.id.eofSearch);

        Button btlAllProducts = findViewById(R.id.buttonAllProducts);
        btlAllProducts.setOnClickListener(v ->{
            presenter.loadCurrentItems();
            showProducts(presenter.getAllItems());
        });

        searchButton.setOnClickListener(v -> {
            String eof = searchEofEditText.getText().toString();
            presenter.searchByEof(eof);
        });
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

    @Override
    public void selectQuantityDialog(Order o, Product p) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle("Quantity for " + p.getName())
                .setView(input)
                .setPositiveButton("Add", (d, w) -> {
                    String text = input.getText().toString();
                    if (!text.isEmpty()) {
                        int quant = Integer.parseInt(text);
                        presenter.addProductToCart(p, quant);
                    } else {
                        showError("Please select quantity");
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSortOptionsDialog() {
        String[] options = {"Alphabetically (name)", "EOF Code"};

        new AlertDialog.Builder(this)
                .setTitle("Sort by...")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        presenter.sortProducts("NAME");
                    } else if (which == 1) {
                        presenter.sortProducts("EOF");
                    }
                })
                .show();
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
    public void showProducts(List<InventoryLine> items) {

        if (items == null) items = new ArrayList<>();

        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            list.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    private void goBack(){
        Intent intent = new Intent(this, PharmacyMainScreen.class);
        startActivity(intent);
    }
}