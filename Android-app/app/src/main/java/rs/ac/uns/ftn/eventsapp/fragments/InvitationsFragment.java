package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.Query;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.GoogleMapActivity;
import rs.ac.uns.ftn.eventsapp.activities.SettingsActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.InvitationAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.dtos.InvitationDTO;
import rs.ac.uns.ftn.eventsapp.models.Invitation;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.InvitationItem;


public class InvitationsFragment extends Fragment {

    private ArrayList<Invitation> invitations;
    private RecyclerView recyclerView;
    private GroupAdapter<GroupieViewHolder> adapter;

    public InvitationsFragment() {
        // Required empty public constructor
    }

    public static InvitationsFragment newInstance(String param1, String param2) {
        InvitationsFragment fragment = new InvitationsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_invitations, container, false);
        recyclerView = v.findViewById(R.id.recyclerview_fragment_invitations);
        adapter = new GroupAdapter<GroupieViewHolder>();
        recyclerView.setAdapter(adapter);

        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();  //TODO: implement refresh list from backend
                pullToRefresh.setRefreshing(false);
            }
        });

        return v;
    }

    /**
     * Refresh data from server
     */
    private void refreshData() {
        getAllInvitations();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getAllInvitations();
    }

    @Override
    public void onStart() {
        super.onStart();

        //sakrij map dugme
        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.hide();

        //pretvori add dugme u map dugme
        final FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);

        fab.setImageResource(android.R.drawable.ic_dialog_map);
        fab.hide(); //ovo je zbog baga googla: https://stackoverflow.com/questions/54506295/icon-not-showing-in-floatingactionbutton-after-changed-programmatically
        fab.show(); //ovo je zbog baga googla: https://stackoverflow.com/questions/54506295/icon-not-showing-in-floatingactionbutton-after-changed-programmatically
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMap();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void onClickCreateEvent() {
        Intent intent = new Intent(getContext(), CreateEventActivity.class);
        startActivity(intent);
    }

    private void onClickMap() {
        ArrayList<EventForMapDTO> eventsForMap = new ArrayList<>();
        for (Invitation item : invitations) {
            eventsForMap.add(new EventForMapDTO(item.getEvent().getId(), item.getEvent().getName(), item.getEvent().getLatitude(), item.getEvent().getLongitude(), item.getEvent().getImageUri()));
        }
        Intent intent = new Intent(getContext(), GoogleMapActivity.class);
        intent.putExtra("EVENTS", eventsForMap);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();

        //prikazi map dugme
        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.show();

        //vrati bivse add (trenutno map dugme) u add dugme
        FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);

        fab.setImageResource(android.R.drawable.ic_input_add);
        fab.hide(); //ovo je zbog baga googla: https://stackoverflow.com/questions/54506295/icon-not-showing-in-floatingactionbutton-after-changed-programmatically
        fab.show(); //ovo je zbog baga googla: https://stackoverflow.com/questions/54506295/icon-not-showing-in-floatingactionbutton-after-changed-programmatically
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateEvent();
            }
        });
    }

    private void getAllInvitations() {
        adapter.clear();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        InvitationAppApi invitationApi = getInvitationApi();
        Call<List<InvitationDTO>> invitationCall =
                invitationApi.getUserInvitations(loggedUser.getId());

        invitationCall.enqueue(new Callback<List<InvitationDTO>>() {
            @Override
            public void onResponse(Call<List<InvitationDTO>> call, retrofit2.Response<List<InvitationDTO>> response) {
                if (response.isSuccessful()){
                    adapter.clear();
                    for (InvitationDTO invitationDTO : response.body()) {
                        adapter.add(new InvitationItem(invitationDTO));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<InvitationDTO>> call, Throwable t) {

            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.menu_settings_item_only, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings_only_menu_item) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_invitations);
    }


    public ArrayList<Invitation> getItems() {
        return invitations;
    }

    private InvitationAppApi getInvitationApi() {
        InvitationAppApi invitationApi;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        invitationApi = retrofit.create(InvitationAppApi.class);
        return invitationApi;
    }
}
