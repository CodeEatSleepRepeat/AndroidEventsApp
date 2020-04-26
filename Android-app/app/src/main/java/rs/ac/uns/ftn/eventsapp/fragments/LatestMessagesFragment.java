package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ChatLogActivity;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.views.LatestMessageItem;


public class LatestMessagesFragment extends Fragment {


    public LatestMessagesFragment() {
        // Required empty public constructor
    }


    public static LatestMessagesFragment newInstance(String param1, String param2) {
        LatestMessagesFragment fragment = new LatestMessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_messages, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fillLatestMessages();
    }

    private void fillLatestMessages(){
        GroupAdapter adapter = new GroupAdapter();
        RecyclerView recyclerViewLatestMessages =
                getActivity().findViewById(R.id.recyclerview_list_of_latest_messages);

        recyclerViewLatestMessages.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        adapter.add(new LatestMessageItem(new ChatMessage(
                1l,
                "Pa ja ovo prvi put vidim",
                2l,
                4l,
                System.currentTimeMillis()/1000
                )));
        adapter.add(new LatestMessageItem(new ChatMessage(
                2l,
                "Pa ja ovo prvi put vidim",
                54l,
                43l,
                System.currentTimeMillis()/1000
        )));
        adapter.add(new LatestMessageItem(new ChatMessage(
                4l,
                "Pa ja ovo prvi put vidim",
                22l,
                43l,
                System.currentTimeMillis()/1000
        )));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                onAdapterItemClick();
            }
        });

        recyclerViewLatestMessages.setAdapter(adapter);

    }

    private void onAdapterItemClick(){
        // TODO: Preci na prozor sa porukama sa izabranim korisnikom
        Intent intent = new Intent(getActivity(), ChatLogActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_messages);
    }
}
