package rs.ac.uns.ftn.eventsapp.sync;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.fragments.HomeEventListFragment;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SyncHomeList extends AsyncTask<Void, Void, Void> {

    private HomeEventListFragment fragment;
    private SearchFilterEventsDTO dto;

    public SyncHomeList(HomeEventListFragment fragment) {
        this.fragment = fragment;
        this.dto = new SearchFilterEventsDTO(fragment.getDto().getSearch(), fragment.getDto().getDistance(), fragment.getDto().getLat(), fragment.getDto().getLng(), fragment.getDto().getSortType(), fragment.getDto().getEventTypes(), fragment.getDto().getEventStart(), fragment.getDto().getEventEnd(), fragment.getDto().getFacebookPrivacy());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.getPullToRefresh().setRefreshing(true);
        if (dto.getDistance() == 0) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(fragment.getContext().getApplicationContext());
            dto.setDistance(Integer.parseInt(sharedPreferences.getString("pref_default_distance2", "100")));
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getInitialEvents(HomeEventListFragment.PAGE_START, dto);
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
