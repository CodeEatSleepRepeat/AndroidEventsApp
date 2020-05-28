package rs.ac.uns.ftn.eventsapp.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.activities.NoServerActivity;
import rs.ac.uns.ftn.eventsapp.activities.SignInActivity;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;

public class SyncReceiverInitTask extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SplashScreenActivity.SYNC_USER)) {
            int resultStatus = intent.getExtras().getInt(SplashScreenActivity.SYNC_USER_RESULT);
            switch (resultStatus) {
                case SplashScreenActivity.SYNC_OK:  //user is fetched, now sync all other user lists
                    //startMainActivity(context);
                    startSyncUserEventsService(context);
                    break;
                case SplashScreenActivity.SYNC_BAD_USER:    //go to login, user in DB was bad
                    startSignInActivity(context);
                    break;
                default:    // SplashScreenActivity.SYNC_SERVER_NA
                    startNoServerActivity(context);
            }
        } else if (intent.getAction().equals(SplashScreenActivity.SYNC_MY_EVENTS)) {
            int resultStatus = intent.getExtras().getInt(SplashScreenActivity.SYNC_MY_EVENT_RESULT);
            switch (resultStatus) {
                case SplashScreenActivity.SYNC_OK:  //user is fetched, now sync all other user lists
                    startMainActivity(context);
                    break;
                case SplashScreenActivity.SYNC_BAD_USER:    //go to login, user in DB was bad
                    startSignInActivity(context);
                    break;
                default:    // SplashScreenActivity.SYNC_SERVER_NA
                    startNoServerActivity(context);
            }
        }
    }

    private void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void startNoServerActivity(Context context) {
        Intent intent = new Intent(context, NoServerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void startSignInActivity(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void startSyncUserEventsService(Context context) {
        new SyncMyEventsTask(context).execute();
    }
}
