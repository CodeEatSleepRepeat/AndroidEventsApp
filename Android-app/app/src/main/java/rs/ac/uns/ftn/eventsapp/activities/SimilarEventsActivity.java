package rs.ac.uns.ftn.eventsapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class SimilarEventsActivity extends AppCompatActivity {

    private static final int PAGE_START = 0;
    private static List<EventDTO> items = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    //private Long l;
    private boolean isLoading = false;
    private static int currentPage = -1;

    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_events);

        //l = Long.valueOf(getIntent().getStringExtra("eventId"));

        /*if(currentPage==-1){
            currentPage = PAGE_START;
        }
        if(items==null) {
            items = new ArrayList<>();
            getEventsPage(PAGE_START);
        }*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.similarEventsRecyclerView);
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

        addNoInternetListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ovo je za proveru konekcije interneta i odmah reaguje na promene, ali ne radi bas ako se vracam iz aktivnosti bez interneta
     */
    private void addNoInternetListener() {
        noInternetDialog = new NoInternetDialog.Builder(this)
                .setBgGradientStart(getResources().getColor(R.color.colorPrimary)) // Start color for background gradient
                .setBgGradientCenter(getResources().getColor(R.color.colorPrimaryDark)) // Center color for background gradient
                .setWifiLoaderColor(R.color.colorBlack) // Set custom color for wifi loader
                .setCancelable(true)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noInternetDialog != null)
            noInternetDialog.onDestroy();
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
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.similarEvents(num, Long.valueOf(getIntent().getStringExtra("eventId")));
        events.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    isLoading = false;
                    return;
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

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        currentPage = PAGE_START;
        getEventsPage(PAGE_START);
    }
}
