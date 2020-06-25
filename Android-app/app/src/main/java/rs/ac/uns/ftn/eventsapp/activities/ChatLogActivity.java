package rs.ac.uns.ftn.eventsapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import am.appwise.components.ni.NoInternetDialog;
import rs.ac.uns.ftn.eventsapp.R;

public class ChatLogActivity extends AppCompatActivity {

    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_log);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addNoInternetListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ovo je za proveru konekcije interneta i odmah reaguje na promene, ali ne radi bas ako se vracam iz aktivnosti bez interneta
     */
    private void addNoInternetListener() {
        noInternetDialog = new NoInternetDialog.Builder(this)
                .setBgGradientStart(getResources().getColor(R.color.colorPrimary)) // Start color for background gradient
                .setBgGradientCenter(getResources().getColor(R.color.colorPrimaryDark)) // Center color for background gradient
                .setWifiLoaderColor(R.color.colorBlack) // Set custom color for wifi loader
                .setCancelable(true)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noInternetDialog != null)
            noInternetDialog.onDestroy();
    }
}
