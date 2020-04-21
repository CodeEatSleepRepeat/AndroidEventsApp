package rs.ac.uns.ftn.eventsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
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

        ArrayList<User> users = TestMockup.users;
        for(User user : users){
            adapter.add(new UserSimpleItem(user));
        }

        recyclerView.setAdapter(adapter);

    }

}
