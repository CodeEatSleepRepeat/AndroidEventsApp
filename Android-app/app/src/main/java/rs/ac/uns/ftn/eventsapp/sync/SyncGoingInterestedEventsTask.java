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
import rs.ac.uns.ftn.eventsapp.dtos.GIEventsSyncDTO;
import rs.ac.uns.ftn.eventsapp.dtos.GoingInterestedEventsDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SyncGoingInterestedEventsTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private long lastSyncTime;
    private Retrofit retrofit;
    public static final String preferenceSyncGIEvents = "preferenceSyncGIEvents";

    public SyncGoingInterestedEventsTask(Context applicationContext) {
        this.context = applicationContext;
        AndroidThreeTen.init(context);
    }

    @Override
    protected void onPreExecute() {
        //procitaj kada je poslednje vreme azuriranja korisnickih eventova
        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
        lastSyncTime = sharedPreferences.getLong(preferenceSyncGIEvents, 0l);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<GoingInterestedEventsDTO> eventsForUpdate = AppDataSingleton.getInstance().getGoingInterestedEventsForUpdate(lastSyncTime);

        //call backend method for synchronization
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI api = retrofit.create(EventsAppAPI.class);
        Call<List<GoingInterestedEventsDTO>> call = api.syncGoingInterestedEvents(new GIEventsSyncDTO(AppDataSingleton.getInstance().getLoggedUser().getEmail(), AppDataSingleton.getInstance().getLoggedUser().getPassword(), eventsForUpdate));
        call.enqueue(new Callback<List<GoingInterestedEventsDTO>>() {
            @Override
            public void onResponse(Call<List<GoingInterestedEventsDTO>> call, Response<List<GoingInterestedEventsDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AppDataSingleton.getInstance().updateGIEvents((ArrayList<GoingInterestedEventsDTO>) response.body());
                    }

                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(preferenceSyncGIEvents, lastSyncTime);
                    editor.commit();

                    broadcastOk();
                } else if (response.code() == 404) {
                    //response is 404 - user psw have changed or user is deleted on another device
                    AppDataSingleton.getInstance().deleteAllPhysical();
                    broadcastBadUser();
                } else {
                    Log.d("xxs", "onFailure: SyncGoingInterestedEventsTask -> server returned bad code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GoingInterestedEventsDTO>> call, Throwable t) {
                if (t instanceof EOFException) {
                    //idiots who made gson didn't think what if null response with code 200 is valid
                    lastSyncTime = ZonedDateTime.now().toInstant().toEpochMilli();

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(preferenceSyncGIEvents, lastSyncTime);
                    editor.commit();

                    broadcastOk();
                    return;
                }
                if (t instanceof SocketTimeoutException) {
                    //server is probably dead
                    Log.d("xxs", "onFailure: SyncGoingInterestedEventsTask -> server is not responding, maybe dead?");
                    broadcastNoServer();
                    return;
                }

                Log.d("xxs", "onFailure: SyncGoingInterestedEventsTask: " + t.getMessage());
                broadcastNoServer();
                return;
            }
        });

        return null;
    }

    private void broadcastNoServer() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_GI_EVENTS);
        ints.putExtra(SplashScreenActivity.SYNC_GI_EVENT_RESULT, SplashScreenActivity.SYNC_SERVER_NA);
        context.sendBroadcast(ints);
    }

    private void broadcastBadUser() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_GI_EVENTS);
        ints.putExtra(SplashScreenActivity.SYNC_GI_EVENT_RESULT, SplashScreenActivity.SYNC_BAD_USER);
        context.sendBroadcast(ints);
    }

    private void broadcastOk() {
        Intent ints = new Intent(SplashScreenActivity.SYNC_GI_EVENTS);
        ints.putExtra(SplashScreenActivity.SYNC_GI_EVENT_RESULT, SplashScreenActivity.SYNC_OK);
        context.sendBroadcast(ints);
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
