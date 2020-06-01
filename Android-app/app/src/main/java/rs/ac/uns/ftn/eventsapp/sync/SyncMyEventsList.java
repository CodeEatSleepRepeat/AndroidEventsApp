package rs.ac.uns.ftn.eventsapp.sync;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.fragments.GoingEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.MyEventsListFragment;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SyncMyEventsList extends AsyncTask<Void, Void, Void> {

    private MyEventsListFragment fragment;

    public SyncMyEventsList(MyEventsListFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.getPullToRefresh().setRefreshing(true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(fragment.getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getMyEvents(AppDataSingleton.getInstance().getLoggedUser().getId(), 0, new SearchFilterEventsDTO());
        events.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR", response.message());
                    fragment.getPullToRefresh().setRefreshing(false);
                    return;
                }
                fragment.setItems(response.body());
                fragment.getPullToRefresh().setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                Log.d("ERROR", t.toString());
                fragment.getPullToRefresh().setRefreshing(false);
            }
        });
        return null;
    }
}
