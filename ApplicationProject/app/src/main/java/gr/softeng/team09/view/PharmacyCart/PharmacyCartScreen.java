package gr.softeng.team09.view.PharmacyCart;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Order;
import gr.softeng.team09.domain.OrderLine;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;
import gr.softeng.team09.view.PharmacyShowProducts.PharmacyShowProductsScreen;
import gr.softeng.team09.view.PharmacyViewCompletedOrders.PharmacyViewCompletedOrdersScreen;
import gr.softeng.team09.view.PharmacyViewDraftOrders.PharmacyViewDraftOrdersScreen;
import gr.softeng.team09.view.PharmacyViewPendingOrders.PharmacyViewPendingOrdersScreen;

/**
 * The type Pharmacy cart screen.
 */
public class PharmacyCartScreen extends AppCompatActivity implements PharmacyCartView {

    private ListView list;
    private ArrayAdapter<OrderLine> adapter;
    private final ArrayList<OrderLine> cartLines = new ArrayList<>();

    private PharmacyCartPresenter presenter;
    private PharmacyCartViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_view_cart_screen);

        //adapater
        list = findViewById(R.id.cart_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartLines);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            OrderLine selectedLine = cartLines.get(position);
            showUpdateQuantityDialog(selectedLine);
        });

        //presenter
        viewModel = new ViewModelProvider(this).get(PharmacyCartViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        // load from DAO memory
        presenter.loadCart();

        // Buttons
        Button backButton = findViewById(R.id.BackButton);
        Button submitButton = findViewById(R.id.SubmitOrderButton);
        Button saveButton = findViewById(R.id.SaveDraftButton);

        backButton.setOnClickListener(v -> presenter.onBackPressed());
        submitButton.setOnClickListener(v -> presenter.onSubmitClicked());
        saveButton.setOnClickListener(v -> presenter.saveDraft());

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

    @Override
    public void showOrder(Order order) {
        cartLines.clear();
        if (order != null && order.getLines() != null) {
            cartLines.addAll(order.getLines());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void showTotal(double total) {
        TextView totalView = findViewById(R.id.TotalCartView);
        if (totalView != null) {
            totalView.setText(String.format("Total: %.2f €", total));
        }
    }

    @Override
    public void showBudgetDialog() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle("Step 1/2: Insert Budget")
                .setMessage("Available amount:")
                .setView(input)
                .setPositiveButton("Continue", (d, w) -> {
                    String text = input.getText().toString();
                    if (!text.isEmpty()) {
                        try {
                            int budget = Integer.parseInt(text);
                            showBackOrderDialog(budget);
                        } catch (NumberFormatException e) {
                            showError("Wrong number");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void showBackOrderDialog(int budget) {
        new AlertDialog.Builder(this)
                .setTitle("Step 2/2: Backorder")
                .setMessage("In a case of shortage, do you want backorder?")
                .setPositiveButton("YES", (d, w) -> {
                    presenter.submitOrder(budget, true);
                })
                .setNegativeButton("NO", (d, w) -> {
                    presenter.submitOrder(budget, false);
                })
                .show();
    }

    private void showUpdateQuantityDialog(OrderLine line) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_cart_update, null);
        builder.setView(dialogView);

        TextView nameView = dialogView.findViewById(R.id.dialogProductName);
        TextView quantityView = dialogView.findViewById(R.id.dialogQuantity);
        ImageView btnMinus = dialogView.findViewById(R.id.btnMinus);
        ImageView btnPlus = dialogView.findViewById(R.id.btnPlus);


        nameView.setText(line.getProduct().getName());
        final int[] currentQty = {line.getQuantity()};
        quantityView.setText(String.valueOf(currentQty[0]));


        btnPlus.setOnClickListener(v -> {
            currentQty[0]++;
            quantityView.setText(String.valueOf(currentQty[0]));
        });

        btnMinus.setOnClickListener(v -> {
            if (currentQty[0] > 0) {
                currentQty[0]--;
                quantityView.setText(String.valueOf(currentQty[0]));
            }
        });

        builder.setPositiveButton("Update", (dialog, which) -> {
            presenter.updateLineQuantity(line, currentQty[0]);
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
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
    public void navigateBack() {
        Intent intent = new Intent(this, PharmacyMainScreen.class);
        startActivity(intent);
    }
}