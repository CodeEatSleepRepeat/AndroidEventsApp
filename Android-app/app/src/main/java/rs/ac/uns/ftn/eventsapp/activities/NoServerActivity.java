package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.tools.InternetConnectionType;

public class NoServerActivity extends Activity {

    private Button retryBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_server);

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
            startActivity(new Intent(NoServerActivity.this, NoServerActivity.class));
            finish(); // da ne bi mogao da ode back na ovaj ekran
        }
    }

    private void startSplashScreen() {
        startActivity(new Intent(NoServerActivity.this, SplashScreenActivity.class));
        finish(); // da ne bi mogao da ode back na splash
    }
}
