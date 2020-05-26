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
import rs.ac.uns.ftn.eventsapp.tools.InternetConnectionType;

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
        int networkStatus = InternetConnectionType.getConnectivityStatus(this);

        if (networkStatus != InternetConnectionType.TYPE_NOT_CONNECTED) {
            startSplashScreen();
        } else {
            // nema interneta
            startActivity(new Intent(NoInternetActivity.this, NoInternetActivity.class));
            finish(); // da ne bi mogao da ode back na ovaj ekran
        }
    }

    private void startSplashScreen() {
        startActivity(new Intent(NoInternetActivity.this, SplashScreenActivity.class));
        finish(); // da ne bi mogao da ode back na splash
    }
}
