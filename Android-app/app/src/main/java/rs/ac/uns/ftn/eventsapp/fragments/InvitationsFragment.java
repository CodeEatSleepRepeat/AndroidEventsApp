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
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import java.util.ArrayList;
import java.util.Objects;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.GoogleMapActivity;
import rs.ac.uns.ftn.eventsapp.activities.SettingsActivity;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.models.Invitation;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.InvitationItem;


public class InvitationsFragment extends Fragment {

    private ArrayList<Invitation> invitations;
    private RecyclerView recyclerView;

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
        //TODO: pozovi refresh data sa servera, osvezi bazu i ponovo iscrtaj listu u ovom fragmentu
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
        GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<GroupieViewHolder>();
        RecyclerView recyclerView =
                getActivity().findViewById(R.id.recyclerview_fragment_invitations);

        invitations = TestMockup.invitations;
        for (Invitation invitation : invitations) {
            adapter.add(new InvitationItem(invitation));
        }

        recyclerView.setAdapter(adapter);
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
}
