package rs.ac.uns.ftn.eventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ChatLogActivity;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.LatestMessageItem;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;


public class LatestMessagesFragment extends Fragment implements Filterable {

    public static String SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES = "rs.ac.uns.ftn.eventsapp" +
            ".fragments" +
            ".LatestMessagesFragment.SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES";

    private List<User> userList = new ArrayList<User>();
    private List<User> userListAll = new ArrayList<User>();
    private String searchText ="";
    private GroupAdapter adapter;

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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest_messages, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fillLatestMessages();
        getAllFriendUsers();

        // ukoliko korisnik npr rotira telefon, prehodno stanje mu ucitavam
        if(savedInstanceState != null){
            if(savedInstanceState.getString(SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES) != null){
                searchText = savedInstanceState.getString(SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES);
                getFilter().filter(searchText);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        fab.hide();

        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        fab.show();

        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES, searchText);
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


    private void fillLatestMessages(){
        adapter = new GroupAdapter();
        RecyclerView recyclerViewLatestMessages =
                getActivity().findViewById(R.id.recyclerview_list_of_latest_messages);

//        recyclerViewLatestMessages.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL));

        adapter.add(new LatestMessageItem(new ChatMessage(
                1l,
                "Pa ja ovo prvi put vidim!",
                2l,
                4l,
                System.currentTimeMillis()/1000,
                false
                )));
        adapter.add(new LatestMessageItem(new ChatMessage(
                2l,
                "Pa ja ovo drugi put vidim!",
                54l,
                43l,
                System.currentTimeMillis()/1000,
                true
        )));
        adapter.add(new LatestMessageItem(new ChatMessage(
                4l,
                "Pa ja ovo treci put vidim!",
                22l,
                43l,
                System.currentTimeMillis()/1000,
                true
        )));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                onAdapterItemClick();
            }
        });

        recyclerViewLatestMessages.setAdapter(adapter);

    }

    private void getAllFriendUsers(){
        userList.addAll(TestMockup.users);
        userListAll.addAll(TestMockup.users);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_search_users, menu);
        MenuItem item = menu.findItem(R.id.action_search_users);
        SearchView searchView = (SearchView) item.getActionView();
        if(!searchText.equals("")){
            MenuItemCompat.expandActionView(item);
            searchView.setQuery(searchText, true);
        }

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


    private Filter filter = new Filter() {
        //Ovo se pokrece u background niti...
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<User> filteredList = new ArrayList<User>();

            if(charSequence.toString().isEmpty()){
                filteredList = null;
            }
            else {
                for(User user: userListAll){
                    if(user.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
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
            adapter.clear();
            if(((Collection<? extends User>) results.values) == null){
                fillLatestMessages();
            }
            else{
                userList.addAll((Collection<? extends User>) results.values);
                for(User user : userList){
                    adapter.add(new UserSimpleItem(user, false, true));
                }
            }

        }
    };
}
