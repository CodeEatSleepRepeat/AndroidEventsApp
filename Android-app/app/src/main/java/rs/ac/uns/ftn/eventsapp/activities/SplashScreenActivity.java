package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.sync.InitTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncReceiverInitTask;

public class SplashScreenActivity extends Activity {

    private SyncReceiverInitTask syncReceiverInitTask;
    public static final String SYNC_USER = "SYNC_USER";
    public static final String SYNC_MY_EVENTS = "SYNC_MY_EVENTS";
    public static final String SYNC_USER_RESULT = "SYNC_USER_RESULT";
    public static final String SYNC_MY_EVENT_RESULT = "SYNC_MY_EVENT_RESULT";
    public static final int SYNC_OK = 0;
    public static final int SYNC_SERVER_NA = 1;
    public static final int SYNC_BAD_USER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        this.syncReceiverInitTask = new SyncReceiverInitTask();
        //register broadcast listener
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_USER);
        filter.addAction(SYNC_MY_EVENTS);
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
