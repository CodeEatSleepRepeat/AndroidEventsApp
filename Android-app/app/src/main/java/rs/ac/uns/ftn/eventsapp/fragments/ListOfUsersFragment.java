package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.AddFriendActivity;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class ListOfUsersFragment extends Fragment implements Filterable {

    public static String SEARCH_TEXT_LIST_OF_USERS = "rs.ac.uns.ftn.eventsapp.fragments" +
            ".ListOfUsersFragment.SEARCH_TEXT_LIST_OF_USERS";

    private List<User> userList = new ArrayList<User>();
    private List<User> userListAll = new ArrayList<User>();
    private String searchText;
    private GroupAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        // ukoliko korisnik npr rotira telefon, prehodno stanje mu ucitavam
        if(savedInstanceState != null){
            if(savedInstanceState.getString(SEARCH_TEXT_LIST_OF_USERS) != null){
                searchText = savedInstanceState.getString(SEARCH_TEXT_LIST_OF_USERS);
                getFilter().filter(searchText);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddFriendActivity();
            }
        });

        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.hide();
    }

    @Override
    public void onStop() {
        super.onStop();

        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_TEXT_LIST_OF_USERS, searchText);
    }

    private void getAllFriendUsers(){

        adapter = new GroupAdapter<>();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview_list_of_users);

        userList.addAll(TestMockup.users);
        userListAll.addAll(TestMockup.users);
        for(User user : userList){
            adapter.add(new UserSimpleItem(user, false, false));
        }

        recyclerView.setAdapter(adapter);

    }

    private void goToAddFriendActivity(){
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_search_users, menu);
        MenuItem item = menu.findItem(R.id.action_search_users);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    /*
    * Filter za filtriranje korisnika
    * po njihovom username-u
    * */
    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_friends);
    }

    private Filter filter = new Filter() {
        //Ovo se pokrece u background niti...
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<User> filteredList = new ArrayList<User>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(userListAll);
            }
            else {
                for(User user: userListAll){
                    if(user.getUserName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(user);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //ovo se pokrece u glavnoj UI niti...
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((Collection<? extends User>) results.values);
            adapter.clear();
            for(User user : userList){
                adapter.add(new UserSimpleItem(user, false, false));
            }
        }
    };
}
