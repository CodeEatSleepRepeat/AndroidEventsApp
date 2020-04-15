package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.Random;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.UserDetailActivity;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class ListOfUsersFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getAllFriendUsers();
    }

    private void getAllFriendUsers(){

        GroupAdapter adapter = new GroupAdapter<>();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview_list_of_users);

        adapter.add(new UserSimpleItem(new User(
                1l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                2l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                3l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                4l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                5l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                6l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                7l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                8l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                9l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                11l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                12l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));
        adapter.add(new UserSimpleItem(new User(
                13l,
                "Veljko number" + new Random().nextInt(100),
                "nemaJos")));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                onAdapterItemClick();
            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void onAdapterItemClick(){
        // TODO: Do something when user clicks on row..
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        startActivity(intent);
    }
}
