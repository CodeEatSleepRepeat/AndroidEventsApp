package rs.ac.uns.ftn.eventsapp.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.ZonedDateTime;

import java.io.EOFException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.UserProfileSyncDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SyncUserTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    long lastSyncTime;
    private Retrofit retrofit;
    public static final String preferenceSyncUser = "preferenceSyncUser";

    public SyncUserTask(Context applicationContext) {
        this.context = applicationContext;
        AndroidThreeTen.init(context);
    }

    @Override
    protected void onPreExecute() {
        //procitaj kada je poslednje vreme azuriranja korisnika
        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
        lastSyncTime = sharedPreferences.getLong(preferenceSyncUser, 0l);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //call backend method for synchronization
        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        UserAppApi api = retrofit.create(UserAppApi.class);
        Call<User> call = api.syncUser(new UserProfileSyncDTO(AppDataSingleton.getInstance().getLoggedUser().getEmail(), AppDataSingleton.getInstance().getLoggedUser().getPassword(), lastSyncTime));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AppDataSingleton.getInstance().updateUser(response.body());
                    }

                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();
                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(preferenceSyncUser, lastSyncTime);
                    editor.commit();

                    broadcastOk();
                } else {
                    //response is 404 - user psw have changed or user is deleted on another device
                    AppDataSingleton.getInstance().deleteAllPhysical();
                    broadcastBadUser();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof EOFException) {
                    //idiots who made gson didn't think what if null response with code 200 is valid
                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(preferenceSyncUser, lastSyncTime);
                    editor.commit();

                    broadcastOk();
                    return;
                }
                if (t instanceof SocketTimeoutException) {
                    //server is probably dead
                    Log.d("xxs", "onFailure: SyncUserTask -> server is not responding, maybe dead?");
                    broadcastNoServer();
                    return;
                }

                Log.d("xxs", "onFailure: SyncUserTask: " + t.getMessage());
                broadcastNoServer();
                return;
            }
        });

        return null;
    }

    private void broadcastNoServer() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
        ints.putExtra(SplashScreenActivity.SYNC_USER_RESULT, SplashScreenActivity.SYNC_SERVER_NA);
        context.sendBroadcast(ints);
    }

    private void broadcastBadUser() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
        ints.putExtra(SplashScreenActivity.SYNC_USER_RESULT, SplashScreenActivity.SYNC_BAD_USER);
        context.sendBroadcast(ints);
    }

    private void broadcastOk() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
        ints.putExtra(SplashScreenActivity.SYNC_USER_RESULT, SplashScreenActivity.SYNC_OK);
        context.sendBroadcast(ints);
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
