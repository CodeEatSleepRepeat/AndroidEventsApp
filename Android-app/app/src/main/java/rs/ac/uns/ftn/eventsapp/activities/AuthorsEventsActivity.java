package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;

public class AuthorsEventsActivity extends AppCompatActivity {

    private static final int PAGE_START = 0;
    private static List<EventDTO> items = null;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private static int currentPage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authors_events);

        if(currentPage==-1){
            currentPage = PAGE_START;
        }
        if(items==null) {
            items = new ArrayList<>();
            getEventsPage(PAGE_START);
        }

        RecyclerView recyclerView = findViewById(R.id.authorsEventsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventListRecyclerView(items, this, R.layout.event_list_row);
        recyclerView.setAdapter(adapter);

        /*final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });*/

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, null, null) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                getEventsPage(currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    /**
     * Refresh data from server
     */
    private void refreshData() {
        //TODO: pozovi refresh data sa servera, osvezi bazu i ponovo iscrtaj listu u ovom fragmentu

    }

    public List<EventDTO> getItems() {
        return items;
    }

    private void getEventsPage(int num) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getInitialEvents(num, new SearchFilterEventsDTO());
        events.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
                isLoading = false;
                items.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }
}

