package rs.ac.uns.ftn.eventsapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import rs.ac.uns.ftn.eventsapp.tools.InternetConnectionType;

public class SyncUserService extends Service {

    /**
     * Start on command (when user refreshes page) or periodically every n minutes
     *
     * @param intent  - holds flag whether user asked for refresh it will perform task on any internet connection, or if periodical call, it will only perform on wifi
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Boolean forcedCall = intent.getBooleanExtra("CALL_TYPE", false);
        int networkStatus = InternetConnectionType.getConnectivityStatus(getApplicationContext());

        if (networkStatus == InternetConnectionType.TYPE_WIFI || (networkStatus == InternetConnectionType.TYPE_MOBILE && forcedCall)) {
            new SyncUserTask(getApplicationContext()).execute();
        }

        stopSelf();

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
