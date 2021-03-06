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
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.AddFriendActivity;
import rs.ac.uns.ftn.eventsapp.activities.FriendRequestsActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class ListOfUsersFragment extends Fragment implements Filterable {

    public static String SEARCH_TEXT_LIST_OF_USERS = "rs.ac.uns.ftn.eventsapp.fragments" +
            ".ListOfUsersFragment.SEARCH_TEXT_LIST_OF_USERS";

    private List<User> userList = new ArrayList<User>();
    private List<User> userListAll = new ArrayList<User>();
    private String searchText;
    private GroupAdapter adapter;
    private SearchView searchView;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_of_users, container, false);
        recyclerView = v.findViewById(R.id.recyclerview_list_of_users);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button friendRequestButton =
                getActivity().findViewById(R.id.button_go_to_friend_requests_list_of_users);
        friendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendRequestsActivity.class);
                startActivity(intent);
            }
        });
        adapter = new GroupAdapter<>();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerview_list_of_users);
        recyclerView.setAdapter(adapter);

        getAllUsersFriends();
        getNumberOfFriendRequests();

        // ukoliko korisnik npr rotira telefon, prehodno stanje mu ucitavam
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(SEARCH_TEXT_LIST_OF_USERS) != null) {
                searchText = savedInstanceState.getString(SEARCH_TEXT_LIST_OF_USERS);
                //getFilter().filter(searchText);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        changeFloatActionButtonForFriendsFragment();
    }

    private void changeFloatActionButtonForFriendsFragment() {
        final FloatingActionButton fab = getActivity().findViewById(R.id.floating_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddFriendActivity();
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
        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.hide();
    }


    @Override
    public void onStop() {
        super.onStop();

        returnFloatActionButtonOfEvents();
    }

    private void returnFloatActionButtonOfEvents() {
        FloatingActionButton fabMap = getActivity().findViewById(R.id.floating_map_btn);
        fabMap.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_TEXT_LIST_OF_USERS, searchText);
    }

    private void getAllUsersFriends() {
        UserAppApi userAppi;
        userAppi = getUserApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<List<User>> userFriends =
                userAppi.getFriendsOfUser(loggedUser.getId());
        userFriends.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body() != null) {
                    userList.clear();
                    userListAll.clear();
                    adapter.clear();
                    userList.addAll(response.body());
                    userListAll.addAll(response.body());

                    for (User user : userList) {
                        adapter.add(new UserSimpleItem(user, false, false));
                    }

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    private void goToAddFriendActivity() {
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        startActivity(intent);
    }

    /**
     * Refresh data from server
     */
    private void refreshData() {
        //TODO: pozovi refresh data sa servera, osvezi bazu i ponovo iscrtaj listu u ovom fragmentu
        getNumberOfFriendRequests();
        getAllUsersFriends();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_search_users, menu);
        MenuItem item = menu.findItem(R.id.action_search_users);
        searchView = (SearchView) item.getActionView();
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

        if (searchText != null && !searchText.equals("")) {
            final String s = searchText;
            item.expandActionView();
            searchView.setQuery(s, true);
            searchView.clearFocus();
        }

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
        //changeFloatActionButtonForFriendsFragment();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_friends);
    }

    private Filter filter = new Filter() {
        //Ovo se pokrece u background niti...
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<User> filteredList = new ArrayList<User>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(userListAll);
            } else {
                for (User user : userListAll) {
                    if (user.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            for (User user : userList) {
                adapter.add(new UserSimpleItem(user, false, false));
            }
        }
    };

    private void getNumberOfFriendRequests() {
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<Integer> friendshipRequests =
                friendshipAppAPI.getNumberOfUserFriendRequests(loggedUser.getId());
        friendshipRequests.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body() != null) {
                    Button goToFriendRequestsBtn =
                            getActivity().findViewById(R.id.button_go_to_friend_requests_list_of_users);
                    if (response.body() > 0) {
                        goToFriendRequestsBtn.setVisibility(View.VISIBLE);
                        String friendRequests = getString(R.string.friend_requests);
                        goToFriendRequestsBtn.setText(response.body() + " " + friendRequests);
                    } else {
                        goToFriendRequestsBtn.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.failed,
                        Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    private FriendshipAppAPI getFriendshipApi() {
        FriendshipAppAPI friendshipAppAPI;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        friendshipAppAPI = retrofit.create(FriendshipAppAPI.class);
        return friendshipAppAPI;
    }

    private UserAppApi getUserApi() {
        UserAppApi userAppi;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        userAppi = retrofit.create(UserAppApi.class);
        return userAppi;
    }

}
