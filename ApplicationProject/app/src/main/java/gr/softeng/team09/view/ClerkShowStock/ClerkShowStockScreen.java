package gr.softeng.team09.view.ClerkShowStock;

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
import gr.softeng.team09.domain.InventoryLine;
import gr.softeng.team09.domain.OrderState;
import gr.softeng.team09.domain.ProductCategory;
import gr.softeng.team09.view.ClerkBatches.ClerkBatchesGeneralScreen;
import gr.softeng.team09.view.ClerkMain.ClerkMainScreen;
import gr.softeng.team09.view.ClerkOrdersGeneral.ClerkOrdersGeneral;
import gr.softeng.team09.view.ClerkShowStats.ClerkShowStatsScreen;

/**
 * The type Clerk show stock screen.
 */
public class ClerkShowStockScreen extends AppCompatActivity implements ClerkShowStockView {

    private EditText editEofSearch;
    private TextView textViewInformationProduct;
    private ListView listViewStock;

    private ArrayAdapter<String> adapter;
    private final List<String> stockList = new ArrayList<>();

    private ClerkShowStockViewModel viewModel;
    private ClerkShowStockPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clerk_show_stock_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //---- ViewModel / Presenter --------
        viewModel = new ViewModelProvider(this).get(ClerkShowStockViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);





        //----- Views --------
        Button stockToHome = findViewById(R.id.button9homepage);
        Button searchSpecifique = findViewById(R.id.button2SearchProducts);
        Button showFullStock = findViewById(R.id.button2FullStock);

        listViewStock = findViewById(R.id.StockList);
        editEofSearch = findViewById(R.id.SearchProductEOFforStock);
        textViewInformationProduct = findViewById(R.id.textViewSpecifueProductInf);

        //----- List adapter --------
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stockList);
        listViewStock.setAdapter(adapter);
        //adapter.notifyDataSetChanged();


        //----- Load full stock --------
        presenter.loadFullStock();
        loadInformationToList();




        //buttons-------------------------------------------------------------------------

        //navigation----------------------------------
        stockToHome.setOnClickListener(v -> {
            Intent intent = new Intent(ClerkShowStockScreen.this, ClerkMainScreen.class);
            startActivity(intent);
        });

        //button full stock----------------------------------
        showFullStock.setOnClickListener(v -> {
            presenter.loadFullStock();
            loadInformationToList();
        });

        searchSpecifique.setOnClickListener(v->{
            String searched = editEofSearch.getText().toString().trim();
            if(!searched.isEmpty()){
                presenter.setEofToSearch(searched);
                presenter.loadEofStock();
                adapter.notifyDataSetChanged();
            }
        });


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


    }

    //================== ClerkShowStockView methods ==================

    @Override
    public void loadInformationToList() {
        stockList.clear();

        List<InventoryLine> inv = presenter.getStock();
        for (InventoryLine in : inv) {
            stockList.add(buildRow(in));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public String buildRow(InventoryLine i) {
        String name = i.getProduct().getName();
        int quantity = i.getQuantity();
        int eof = i.getProduct().getEofyCode();
        double price = i.getProduct().getPrice();
        ProductCategory category = i.getProduct().getCategory();
        return "Product name: " + name + "\nEOF code: " + eof + "\nProduct Category: " + category  +"\nQuantity: " + quantity +
                "\nPrice without VAT: " + price+ " €";
    }

    @Override
    public void showError(String msg) {
        editEofSearch.setError(msg);
    }

    @Override
    public void clearOrders(){
        stockList.clear();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showOrders(List<String> display){
        stockList.clear();
        stockList.addAll(display);
        adapter.notifyDataSetChanged();
        listViewStock.setVisibility(android.view.View.VISIBLE);
    }

}


