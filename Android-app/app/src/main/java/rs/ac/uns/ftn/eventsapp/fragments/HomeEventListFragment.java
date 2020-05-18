package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.SignInActivity;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;

public class HomeEventListFragment extends Fragment {

    private List<EventDTO> items = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: data goes here
        getInitialEvents();
        Log.d("FINISH", "" + items);
        //items = TestMockup.getInstance().events;

        View v =  inflater.inflate(R.layout.fragment_list_of_events, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new EventListRecyclerView(items, this.getContext(), R.layout.event_list_row);
        recyclerView.setAdapter(adapter);

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

    public List<EventDTO> getItems(){
        return items;
    }

    private void getInitialEvents(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getInitialEvents();
        events.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
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
}
