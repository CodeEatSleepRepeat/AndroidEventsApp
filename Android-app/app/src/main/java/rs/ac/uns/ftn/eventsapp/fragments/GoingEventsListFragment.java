package rs.ac.uns.ftn.eventsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;

public class GoingEventsListFragment extends Fragment {

    private ArrayList<Event> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: data goes here
        items = TestMockup.getInstance().events;
        ArrayList<Event> temp = new ArrayList<>();
        for (Event e : items){
            if (e.getAuthor().getUserId()!=1l){
                temp.add(e);
            }
        }
        items = temp;

        View v =  inflater.inflate(R.layout.fragment_list_of_events, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new EventListRecyclerView(items, this.getContext(), R.layout.going_event_list_row));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_going);
    }

    public ArrayList<Event> getItems(){
        return items;
    }
}
