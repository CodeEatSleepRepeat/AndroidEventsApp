package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.SignInActivity;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;

public class HomeEventListFragment extends Fragment {

    ArrayList<Event> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: data goes here
        items = TestMockup.getInstance().events;

        View v =  inflater.inflate(R.layout.fragment_list_of_events, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new EventListRecyclerView(items, this.getContext(), R.layout.event_list_row));

        if (!getActivity().getIntent().getBooleanExtra(SignInActivity.IS_ANONYMOUS, false)) {
            FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CreateEventActivity.class);
                    startActivity(intent);
                }
            });
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_home);
    }

    public ArrayList<Event> getItems(){
        return items;
    }
}
