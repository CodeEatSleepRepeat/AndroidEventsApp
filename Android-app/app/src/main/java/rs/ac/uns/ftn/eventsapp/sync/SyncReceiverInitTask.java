package rs.ac.uns.ftn.eventsapp.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.activities.NoInternetActivity;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;

public class SyncReceiverInitTask extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SplashScreenActivity.SYNC_USER)) {
            boolean resultStatus = intent.getExtras().getBoolean("SYNC_USER_RESULT");
            if (resultStatus) {
                startMainActivity(context);
            } else {
                startNoInternetActivity(context);
            }
        }
    }

    private void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void startNoInternetActivity(Context context) {
        Intent intent = new Intent(context, NoInternetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
