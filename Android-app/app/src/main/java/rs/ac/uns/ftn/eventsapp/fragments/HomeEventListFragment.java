package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SortType;
import rs.ac.uns.ftn.eventsapp.sync.SyncHomeList;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class HomeEventListFragment extends Fragment {

    public static final int PAGE_START = 0;
    private List<EventDTO> items = new ArrayList<>();
    private SearchFilterEventsDTO dto = new SearchFilterEventsDTO();
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout pullToRefresh;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    private TextView sortTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*if(currentPage==-1){
            currentPage = PAGE_START;
        }
        if(items==null) {
            items = new ArrayList<>();
            getEventsPage(PAGE_START);
        }*/

        Log.d("CREATE_HOME", "da");
        //items = TestMockup.getInstance().events;

        View v = inflater.inflate(R.layout.fragment_list_of_events, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new EventListRecyclerView(items, this.getContext(), R.layout.event_list_row);

        pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(); // code for updating list
            }
        });

        sortTextView = pullToRefresh.findViewById(R.id.sortFilterTextView);

        final FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, fab, null) {
            @Override
            protected void loadMoreItems() {//TODO: da li ovde uposte ulazi?
                Log.d("LOCATIONSCROLL", dto.getLat() + " " + dto.getLng());
                isLoading = true;
                //getEventsPage(currentPage, dto);
                currentPage += 1;
                getEventsPage(currentPage, dto);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        if (AppDataSingleton.getInstance().isLoggedIn()) {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CreateEventActivity.class);
                    startActivity(intent);
                }
            });

            final FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
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
        }

        setItUp(getArguments());

        return v;
    }


    /**
     * Refresh data from server
     */
    private void refreshData() {
        new SyncHomeList(this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_home);
        Log.d("OnResumeHome", dto.getLat() + " " + dto.getLng());
        items.clear();
        currentPage = PAGE_START;
        getEventsPage(PAGE_START, dto);
    }

    public List<EventDTO> getItems() {
        return items;
    }

    private void getEventsPage(int num, SearchFilterEventsDTO dto) {
        SearchFilterEventsDTO tempDto = new SearchFilterEventsDTO(dto.getSearch(), dto.getDistance(), dto.getLat(), dto.getLng(), dto.getSortType(), dto.getEventTypes(), dto.getEventStart(), dto.getEventEnd(), dto.getFacebookPrivacy());
        if (tempDto.getDistance() == 0) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
            String distString = sharedPreferences.getString("pref_default_distance2", "100");
            if (distString == null || distString.equals("0")) {
                distString = "100";
            }
            tempDto.setDistance(Integer.parseInt(distString));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getInitialEvents(num, tempDto);
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
                Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
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

    public void setItUp(Bundle bundle) {

        Log.d("SORT", bundle.getString("SORT"));
        ArrayList<String> types = bundle.getStringArrayList("TYPES");
        List<EventType> eventTypes = new ArrayList<>();
        Log.d("START", bundle.getString("START"));
        Log.d("END", bundle.getString("END"));
        Log.d("LAT", bundle.getString("LAT"));
        Log.d("LNG", bundle.getString("LNG"));
        Log.d("DIST", bundle.getString("DIST"));
        if (bundle.getString("SEARCH") != null && !bundle.getString("SEARCH").equals("")) {
            dto.setSearch(bundle.getString("SEARCH"));
        }
        if (!types.isEmpty()) {
            for (String categoryItem : types) {
                Log.d("CATEGORYITEM", categoryItem);
                if (categoryItem.equals(getString(R.string.charity))) {
                    eventTypes.add(EventType.CHARITY);
                } else if (categoryItem.equals(getString(R.string.party))) {
                    eventTypes.add(EventType.PARTY);
                } else if (categoryItem.equals(getString(R.string.music))) {
                    eventTypes.add(EventType.MUSIC);
                } else if (categoryItem.equals(getString(R.string.educational))) {
                    eventTypes.add(EventType.EDUCATIONAL);
                } else if (categoryItem.equals(getString(R.string.talks))) {
                    eventTypes.add(EventType.TALKS);
                } else if (categoryItem.equals(getString(R.string.sports))) {
                    eventTypes.add(EventType.SPORTS);
                }
            }
            Log.d("TYPE", eventTypes.get(0) + "");
            while (eventTypes.size() < 7) {
                eventTypes.add(null);
            }

        } else {
            eventTypes.add(EventType.CHARITY);
            eventTypes.add(EventType.SPORTS);
            eventTypes.add(EventType.TALKS);
            eventTypes.add(EventType.EDUCATIONAL);
            eventTypes.add(EventType.MUSIC);
            eventTypes.add(EventType.PARTY);
            eventTypes.add(EventType.FACEBOOK);
        }

        dto.setEventTypes(eventTypes);

        String sort = bundle.getString("SORT");
        if (sort.equals("POPULAR")) {
            dto.setSortType(SortType.POPULAR);
            sortTextView.setText(getString(R.string.popular_events));
        } else if (sort.equals("RECENT")) {
            dto.setSortType(SortType.RECENT);
            sortTextView.setText(getString(R.string.recent_events));
        } else {
            dto.setSortType(SortType.FOR_YOU);
            sortTextView.setText(getString(R.string.events_for_you));
        }
        String start = bundle.getString("START");
        String end = bundle.getString("END");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss z uuuu").withLocale(new Locale("en", "US"));
        if (!start.equals("min")) {
            //TODO: change language to engl
            dto.setEventStart(ZonedDateTime.parse(start, formatter));
        } else {
            dto.setEventStart(ZonedDateTime.now().minusYears(100));
        }
        if (!end.equals("max")) {
            dto.setEventEnd(ZonedDateTime.parse(end, formatter));
        } else {
            dto.setEventEnd(ZonedDateTime.now().plusYears(100));
        }
        dto.setDistance(Integer.parseInt(bundle.getString("DIST")));
        dto.setFacebookPrivacy(FacebookPrivacy.PUBLIC);
        if (bundle.getString("LAT").equals("-300") && bundle.getString("LNG").equals("-300")) {
            dto.setLat(null);
            dto.setLng(null);
            //dto.setDistance(100);
        } else {
            dto.setLat(Double.parseDouble(bundle.getString("LAT")));
            dto.setLng(Double.parseDouble(bundle.getString("LNG")));
        }
    }

    public void afterChipRemoved(Bundle bundle) {
        setItUp(bundle);
        items.clear();
        currentPage = PAGE_START;
        getEventsPage(PAGE_START, dto);
    }

    public SearchFilterEventsDTO getDto() {
        return dto;
    }
}
