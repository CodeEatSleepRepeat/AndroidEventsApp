package rs.ac.uns.ftn.eventsapp.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.UserProfileSyncDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class SyncUserTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    long lastSyncTime;
    private Retrofit retrofit;

    public SyncUserTask(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected void onPreExecute() {
        //procitaj kada je poslednje vreme azuriranja korisnika
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        lastSyncTime = sharedPreferences.getLong(context.getString(R.string.preferenceSyncUser), 0l);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //call backend method for synchronization
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.localhost_uri))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
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

                    lastSyncTime = new Timestamp(System.currentTimeMillis()).getTime();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(context.getString(R.string.preferenceSyncUser), lastSyncTime);
                    editor.commit();

                    Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
                    ints.putExtra("SYNC_USER_RESULT", AppDataSingleton.getInstance().isLoggedIn());
                    context.sendBroadcast(ints);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof EOFException) {
                    //idiots who made gson didn't think what if null response with code 200 is valid
                    lastSyncTime = new Timestamp(System.currentTimeMillis()).getTime();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong(context.getString(R.string.preferenceSyncUser), lastSyncTime);
                    editor.commit();

                    Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
                    ints.putExtra("SYNC_USER_RESULT", AppDataSingleton.getInstance().isLoggedIn());
                    context.sendBroadcast(ints);
                    return;
                }
                if (t instanceof SocketTimeoutException){
                    //server is probably dead
                    Log.d("xxs", "onFailure: SyncUserTask -> server is not responding, maby dead?");
                    Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
                    ints.putExtra("SYNC_USER_RESULT", false);
                    context.sendBroadcast(ints);
                    return;
                }

                Intent ints = new Intent(SplashScreenActivity.SYNC_USER);
                ints.putExtra("SYNC_USER_RESULT", AppDataSingleton.getInstance().isLoggedIn());
                context.sendBroadcast(ints);
                return;
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
