package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import java.util.ArrayList;
import java.util.Objects;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.SettingsActivity;
import rs.ac.uns.ftn.eventsapp.models.Invitation;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.InvitationItem;


public class InvitationsFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitations, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getAllInvitations();
    }

    @Override
    public void onStart() {
        super.onStart();

        FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        fab.hide();
    }

    @Override
    public void onStop() {
        super.onStop();

        FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        fab.show();
    }

    private void getAllInvitations() {
        GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<GroupieViewHolder>();
        RecyclerView recyclerView =
                getActivity().findViewById(R.id.recyclerview_fragment_invitations);

        ArrayList<Invitation> invitations = TestMockup.invitations;
        for(Invitation invitation : invitations){
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
}
