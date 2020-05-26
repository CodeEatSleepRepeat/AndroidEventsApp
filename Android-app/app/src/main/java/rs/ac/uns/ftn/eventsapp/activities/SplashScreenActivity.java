package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.sync.InitTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncReceiverInitTask;

public class SplashScreenActivity extends Activity {

    private SyncReceiverInitTask syncReceiverInitTask;
    public static String SYNC_USER = "SYNC_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        this.syncReceiverInitTask = new SyncReceiverInitTask();
        //register broadcast listener
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_USER);
        registerReceiver(syncReceiverInitTask, filter);

        // uradi inicijalizaciju u pozadinskom threadu
        new InitTask(this).execute();
    }

    @Override
    protected void onDestroy() {
        //osloboditi resurse
        if (syncReceiverInitTask != null) {
            unregisterReceiver(syncReceiverInitTask);
        }
        super.onDestroy();
    }
}
