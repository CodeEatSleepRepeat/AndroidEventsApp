package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;

public class SplashScreenActivity extends Activity
{
    private static int SPLASH_TIME_OUT = 2500; // splash ce biti vidljiv minimum SPLASH_TIME_OUT milisekundi

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // uradi inicijalizaciju u pozadinskom threadu
        new InitTask().execute();
    }

    private class InitTask extends AsyncTask<Void, Void, Void>
    {
        private long startTime;

        @Override
        protected void onPreExecute()
        {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            checkInternetConnection();
            return null;
        }

        private void continueLogin()
        {
            // sacekaj tako da splash bude vidljiv minimum SPLASH_TIME_OUT milisekundi
            long timeLeft = SPLASH_TIME_OUT - (System.currentTimeMillis() - startTime);
            if(timeLeft < 0) timeLeft = 0;
            SystemClock.sleep(timeLeft);

            //pokreni glavni ekran
            startMainActivity();
        }

        private void checkInternetConnection() {
            NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                continueLogin();
            } else {
                startNoInternetActivity();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

    }

    private void startMainActivity()
    {
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish(); // da nebi mogao da ode back na splash
    }

    private void startNoInternetActivity()
    {
        startActivity(new Intent(SplashScreenActivity.this, NoInternetActivity.class));
        finish(); // da nebi mogao da ode back na splash
    }
}
