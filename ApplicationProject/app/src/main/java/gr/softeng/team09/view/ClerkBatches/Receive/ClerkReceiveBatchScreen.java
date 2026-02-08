package gr.softeng.team09.view.ClerkBatches.Receive;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import gr.softeng.team09.R;
import gr.softeng.team09.domain.Batch;
import gr.softeng.team09.domain.Inventory;
import gr.softeng.team09.domain.Product;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.memorydao.MemoryStore;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;
import gr.softeng.team09.view.ClerkShowStock.ClerkShowStockScreen;

/**
 * The type Clerk receive batch screen.
 */
public class ClerkReceiveBatchScreen extends AppCompatActivity implements ClerkReceiveBatchView {

    private EditText editEOFCode;
    private EditText editNameProduct;
    private EditText editNumberOfBatches;
    private EditText editNumberOfProducts;
    private EditText editPriceProduct;
    private MaterialAutoCompleteTextView editProductCategory;

    /**
     * The Inventory.
     */
    Inventory inventory = MemoryStore.getInventory();

    private ClerkReceiveBatchPresenter presenter;
    private static int batchNumberCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_receive_batch);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ClerkReceiveBatchViewModel vm =
                new ViewModelProvider(this).get(ClerkReceiveBatchViewModel.class);
        presenter = vm.getPresenter();
        presenter.setView(this);

        //--------Views-------------------------------------------
        Button ReceiveToBatchesGeneral = findViewById(R.id.buttonBackReceive);
        Button PressToReceive = findViewById(R.id.button4PressToreceive);

        editEOFCode = findViewById(R.id.eofinputReceive);
        editNameProduct = findViewById(R.id.nameInputReceive);
        editNumberOfBatches = findViewById(R.id.NumberOfBatches);
        editNumberOfProducts = findViewById(R.id.ProductsInBatches);
        editPriceProduct = findViewById(R.id.priceOfProduct);
        editProductCategory = findViewById(R.id.categoryDropdown);

        //--------Navigation---------------------------------------
        ReceiveToBatchesGeneral.setOnClickListener(v -> {
            Intent intent = new Intent(ClerkReceiveBatchScreen.this, ClerkBatchesGeneralScreen.class);
            startActivity(intent);
        });

        //--------Dropdown Logic----------------------------------
        android.widget.ArrayAdapter<ProductCategory> adapter = new android.widget.ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                ProductCategory.values()
        );
        editProductCategory.setAdapter(adapter);

        editProductCategory.setOnItemClickListener((parent, view, position, id) -> {
            editProductCategory.clearFocus();
        });

        //--------Submit Button------------------------------------
        PressToReceive.setOnClickListener(v -> {

            String codeStr = editEOFCode.getText().toString().trim();
            String name = editNameProduct.getText().toString().trim();
            String nBatchesStr = editNumberOfBatches.getText().toString().trim();
            String nProductsStr = editNumberOfProducts.getText().toString().trim();
            String priceStr = editPriceProduct.getText().toString().trim();
            String categoryStr = editProductCategory.getText().toString().trim();


            if (codeStr.isEmpty()) { editEOFCode.setError("Required"); return; }
            if (name.isEmpty()) { editNameProduct.setError("Required"); return; }
            if (nBatchesStr.isEmpty()) { editNumberOfBatches.setError("Required"); return; }
            if (nProductsStr.isEmpty()) { editNumberOfProducts.setError("Required"); return; }
            if (priceStr.isEmpty()) { editPriceProduct.setError("Required"); return; }
            if (categoryStr.isEmpty()) { editProductCategory.setError("Required"); return; }

            int eof, nBatches, nProducts;
            double price;

            try { eof = Integer.parseInt(codeStr); }
            catch (NumberFormatException e) { editEOFCode.setError("Invalid EOF"); return; }

            try { nBatches = Integer.parseInt(nBatchesStr); }
            catch (NumberFormatException e) { editNumberOfBatches.setError("Invalid number"); return; }

            try { nProducts = Integer.parseInt(nProductsStr); }
            catch (NumberFormatException e) { editNumberOfProducts.setError("Invalid number"); return; }

            try { price = Double.parseDouble(priceStr); }
            catch (NumberFormatException e) { editPriceProduct.setError("Invalid price"); return; }

            ProductCategory category;
            try { category = ProductCategory.valueOf(categoryStr); }
            catch (IllegalArgumentException e) { editProductCategory.setError("Pick from list"); return; }


            presenter.setEofInput(eof);
            presenter.setProductName(name);
            presenter.setNumberOfBatches(nBatches);
            presenter.setProductEachBatch(nProducts);
            presenter.setPriceProduct(price);
            presenter.setProductCategory(category);


            presenter.ReadyToReceive();
        });

        //Buttons little down there---------------------------------------------
        ImageView btnToStock = findViewById(R.id.imageViewStock1);
        btnToStock.setOnClickListener(v -> startActivity(new Intent(this, ClerkShowStockScreen.class)));

        ImageView btnToBatches = findViewById(R.id.imageViewOrders);
        btnToBatches.setOnClickListener(v -> startActivity(new Intent(this, ClerkOrdersGeneral.class)));

        ImageView btnToStats = findViewById(R.id.imageViewStats);
        btnToStats.setOnClickListener(v -> startActivity(new Intent(this, ClerkShowStatsScreen.class)));

        ImageView btnToOrders = findViewById(R.id.imageViewBatches);
        btnToOrders.setOnClickListener(v -> startActivity(new Intent(this, ClerkBatchesGeneralScreen.class)));
    }

    //======================= view interface===========================
    @Override
    public synchronized int generateBatchNumber() {
        return ++batchNumberCounter;
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
    public void receiveBatchCall(int eof, String productName, int nProducts,
                                 int nBatches, double price, ProductCategory productCategory) {

        for (int i = 0; i < nBatches; i++) {
            int batchId = inventory.nextBatchId();
            int batchNumber = generateBatchNumber();

            Product product = new Product(productName, productCategory, eof, price);

            Batch batch = new Batch(
                    batchId,
                    product,
                    batchNumber,
                    nProducts
            );

            inventory.receiveBatch(batch);
        }
    }
}
