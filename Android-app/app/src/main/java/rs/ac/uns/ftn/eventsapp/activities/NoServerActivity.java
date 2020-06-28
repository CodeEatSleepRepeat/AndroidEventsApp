package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import pl.droidsonroids.gif.GifImageView;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.tools.InternetConnectionType;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class NoServerActivity extends Activity {

    private Button retryBtn;
    private GifImageView imageView;

    public static final String preferenceServerIpAddress = "preferenceServerIpAddress";

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

        imageView = findViewById(R.id.no_server_gif);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewIP();
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

    /**
     * Dijalog za setovanje nove IP adrese
     */
    private void setNewIP() {
        final AlertDialog.Builder notifyDialogBuilder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_update_ip_address, null);
        notifyDialogBuilder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText ipAddress = view.findViewById(R.id.server_ip_address);
                        if (!ipAddress.getText().toString().trim().equals("")) {
                            // set new ip
                            setNewIp(ipAddress.getText().toString().trim());
                            Toast.makeText(NoServerActivity.this, getString(R.string.server_ip_is) + AppDataSingleton.getInstance().SERVER_IP, Toast.LENGTH_SHORT).show();
                        } else {
                            //wrong ip
                            Toast.makeText(NoServerActivity.this, R.string.server_ip_not_correct, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //close dialog
                    }
                });

        notifyDialogBuilder.create().show();
    }

    private void setNewIp(String ip) {
        //set new ip in preferences folder
        SharedPreferences sharedPreferences = getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceServerIpAddress, ip);
        editor.commit();

        //set new ip in AppDataSingleton
        AppDataSingleton.getInstance().setContext(this);
    }
}
