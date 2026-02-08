package gr.softeng.team09.view.PharmacistEmails;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team09.R;
import gr.softeng.team09.view.PharmacyMain.PharmacyMainScreen;


/**
 * The type Pharmacist emails screen.
 */
public class PharmacistEmailsScreen extends AppCompatActivity implements PharmacistEmailsView {

    private ArrayAdapter<String> adapter;
    private final List<String> rows = new ArrayList<>();

    private PharmacistEmailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pharmacist_emails);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PharmacistEmailsViewModel viewModel =
                new ViewModelProvider(this).get(PharmacistEmailsViewModel.class);
        presenter = viewModel.getPresenter();
        presenter.setView(this);

        Button btnBack = findViewById(R.id.buttonBackfromEmails4);
        btnBack.setOnClickListener(v -> finish());


        ListView listViewPersonalpharmacistEmails = findViewById(R.id.listPharmacistEmails);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        listViewPersonalpharmacistEmails.setAdapter(adapter);


        listViewPersonalpharmacistEmails.setOnItemClickListener((parent, view, position, id) -> {
            presenter.onEmailClicked(position);
        });

        presenter.loadEmailsList();
    }

    // ---------------- View methods ----------------

    @Override
    public void showEmails(List<String> emailTitles) {
        rows.clear();
        rows.addAll(emailTitles);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmailPopup(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
