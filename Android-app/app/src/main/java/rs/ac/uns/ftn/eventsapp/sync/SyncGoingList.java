package rs.ac.uns.ftn.eventsapp.sync;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.threeten.bp.ZonedDateTime;

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
import rs.ac.uns.ftn.eventsapp.fragments.HomeEventListFragment;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SyncGoingList extends AsyncTask<Void, Void, Void> {

    private GoingEventsListFragment fragment;

    public SyncGoingList(GoingEventsListFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.getPullToRefresh().setRefreshing(true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SearchFilterEventsDTO dto = new SearchFilterEventsDTO();
        dto.setEventStart(ZonedDateTime.now());
        dto.setEventEnd(ZonedDateTime.now());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getGoingEvents(AppDataSingleton.getInstance().getLoggedUser().getId(), 0, dto);
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
