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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventsSyncDTO;
import rs.ac.uns.ftn.eventsapp.dtos.UpdateEventDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SyncMyEventsTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private long lastSyncTime;
    private Retrofit retrofit;
    public static final String preferenceSyncMyEvents = "preferenceSyncMyEvents";

    public SyncMyEventsTask(Context applicationContext) {
        this.context = applicationContext;
        AndroidThreeTen.init(context);
    }

    @Override
    protected void onPreExecute() {
        //procitaj kada je poslednje vreme azuriranja korisnickih eventova
        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
        lastSyncTime = sharedPreferences.getLong(preferenceSyncMyEvents, 0l);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<UpdateEventDTO> eventsForUpdate = AppDataSingleton.getInstance().getUserEventsForUpdate(lastSyncTime);

        //call backend method for synchronization
        retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI api = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> call = api.syncUserEvents(new EventsSyncDTO(AppDataSingleton.getInstance().getLoggedUser().getEmail(), AppDataSingleton.getInstance().getLoggedUser().getPassword(), lastSyncTime, eventsForUpdate));
        call.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AppDataSingleton.getInstance().updateUserEvents((ArrayList<EventDTO>) response.body());
                    }

                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(preferenceSyncMyEvents, lastSyncTime);
                    editor.commit();

                    broadcastOk();
                } else if (response.code() == 404) {
                    //response is 404 - user psw have changed or user is deleted on another device
                    AppDataSingleton.getInstance().deleteAllPhysical();
                    broadcastBadUser();
                } else {
                    Log.d("xxs", "onFailure: SyncMyEventsTask -> server returned bad code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                if (t instanceof EOFException) {
                    //idiots who made gson didn't think what if null response with code 200 is valid
                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(preferenceSyncMyEvents, lastSyncTime);
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
        Intent ints = new Intent(SplashScreenActivity.SYNC_MY_EVENTS);
        ints.putExtra(SplashScreenActivity.SYNC_MY_EVENT_RESULT, SplashScreenActivity.SYNC_SERVER_NA);
        context.sendBroadcast(ints);
    }

    private void broadcastBadUser() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_MY_EVENTS);
        ints.putExtra(SplashScreenActivity.SYNC_MY_EVENT_RESULT, SplashScreenActivity.SYNC_BAD_USER);
        context.sendBroadcast(ints);
    }

    private void broadcastOk() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_MY_EVENTS);
        ints.putExtra(SplashScreenActivity.SYNC_MY_EVENT_RESULT, SplashScreenActivity.SYNC_OK);
        context.sendBroadcast(ints);
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
