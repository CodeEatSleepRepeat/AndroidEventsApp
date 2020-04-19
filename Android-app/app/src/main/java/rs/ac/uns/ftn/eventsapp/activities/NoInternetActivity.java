package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;

public class NoInternetActivity extends Activity {

    private Button retryBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        retryBtn = findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
            }
        });
    }

    private void checkInternetConnection() {
        NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            startMainActivity();
        } else {
            // nema interneta
            startActivity(new Intent(NoInternetActivity.this, NoInternetActivity.class));
            finish(); // da ne bi mogao da ode back na ovaj ekran
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(NoInternetActivity.this, MainActivity.class));
        finish(); // da ne bi mogao da ode back na splash
    }
}
