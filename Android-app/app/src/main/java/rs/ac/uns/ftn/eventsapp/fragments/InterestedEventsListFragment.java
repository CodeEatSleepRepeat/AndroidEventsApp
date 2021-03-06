package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.sync.SyncHomeList;
import rs.ac.uns.ftn.eventsapp.sync.SyncInterestedList;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class InterestedEventsListFragment extends Fragment {

    private static final int PAGE_START = 0;
    private List<EventDTO> items = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout pullToRefresh;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    private SearchFilterEventsDTO dto = new SearchFilterEventsDTO();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.filterEventsBtn).setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_of_events, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventListRecyclerView(items, this.getContext(), R.layout.interested_event_list_row);
        recyclerView.setAdapter(adapter);

        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(); // code for updating list
            }
        });

        final FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        final FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, fab, fabMap) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                getEventsPage(currentPage, dto);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_interested);
        items.clear();
        currentPage = PAGE_START;
        getEventsPage(PAGE_START, dto);
    }

    public List<EventDTO> getItems() {
        return items;
    }

    /**
     * Refresh data from server
     */
    private void refreshData() {
        new SyncInterestedList(this).execute();
    }

    private void getEventsPage(int num, SearchFilterEventsDTO dto) {
        dto.setEventStart(ZonedDateTime.now());
        dto.setEventEnd(ZonedDateTime.now());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getInterestedEvents(AppDataSingleton.getInstance().getLoggedUser().getId(), num, dto);
        events.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    isLoading = false;
                    return;
                }
                isLoading = false;
                items.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                //Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
                setItems(AppDataSingleton.getInstance().getInterestedEventsDTO());
            }
        });
    }

    public void setItems(List<EventDTO> newItems) {
        isLoading = false;
        items.clear();
        items.addAll(newItems);
        adapter.notifyDataSetChanged();
    }

    public SwipeRefreshLayout getPullToRefresh() {
        return pullToRefresh;
    }

    private void setItUp(Bundle bundle){
        if(bundle.getString("SEARCH")!=null && !bundle.getString("SEARCH").equals("")){
            dto.setSearch(bundle.getString("SEARCH"));
            dto.setEventStart(ZonedDateTime.now());
            dto.setEventEnd(ZonedDateTime.now());
        }
    }

    public void onSearch(Bundle bundle){
        setItUp(bundle);
        items.clear();
        currentPage = PAGE_START;
        getEventsPage(PAGE_START, dto);
    }
}
