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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ChatLogActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.LatestMessageItem;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;


public class LatestMessagesFragment extends Fragment implements Filterable {
    public static String EXTRA_USER_NAME = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_NAME";
    public static String EXTRA_USER_IMAGE_PATH = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_IMAGE_PATH";
    public static String EXTRA_USER_EMAIL = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_EMAIL";
    public static String EXTRA_USER_FIREBASE_UID = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_FIREBASE_UID";
    public static String SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES = "rs.ac.uns.ftn.eventsapp" +
            ".fragments" +
            ".LatestMessagesFragment.SEARCH_TEXT_LIST_OF_USERS_LATEST_MESSAGES";

    private List<User> userList = new ArrayList<User>();
    private List<User> userListAll = new ArrayList<User>();
    private String searchText ="";
    private GroupAdapter adapter;
    private Map<String, ChatMessage> latestMessagesMap =new HashMap<String, ChatMessage>();
    private Map<String, FirebaseUserDTO> latestMessageToChatPartner = new HashMap<String, FirebaseUserDTO>();
    private FirebaseUserDTO chatPartnerUser;
    private Query emailQuery;

    public LatestMessagesFragment() {
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

        View v = inflater.inflate(R.layout.fragment_latest_messages, container, false);
        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.pullToRefresh);
        if(pullToRefresh != null)
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

        listenForLatestMessages();

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

    @Override
    public void onResume() {
        super.onResume();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.nav_item_messages);
    }

    private void listenForLatestMessages(){
        adapter = new GroupAdapter();
        latestMessagesMap.clear();
        RecyclerView recyclerViewLatestMessages =
                getActivity().findViewById(R.id.recyclerview_list_of_latest_messages);

        String currentUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference usersLatestMessagesRef = FirebaseDatabase.getInstance()
                .getReference("/latest-messages/" + currentUserUid);

        usersLatestMessagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                if(chatMessage == null)
                    return;
                latestMessagesMap.put(dataSnapshot.getKey(),chatMessage);
                refreshRecyclerViewMessages();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                if(chatMessage == null)
                    return;
                latestMessagesMap.put(dataSnapshot.getKey(),chatMessage);
                refreshRecyclerViewMessages();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {

                LatestMessageItem latestMessageItem = (LatestMessageItem) item;
                onAdapterItemClick(latestMessageItem.getChatPartnerUser());
            }
        });

        recyclerViewLatestMessages.setAdapter(adapter);

    }

    private void refreshRecyclerViewMessages(){
        for(Map.Entry<String, ChatMessage> entry : latestMessagesMap.entrySet()){
            String chatPartnerUid = "";
            if(entry.getValue().getFromId().equals(FirebaseAuth.getInstance().getUid())){
                chatPartnerUid = entry.getValue().getToId();
            }
            else{
                chatPartnerUid = entry.getValue().getFromId();
            }
            findChatPartnerInfo(chatPartnerUid, entry.getValue(), entry.getKey());
        }
    }

    private void getAllFriendUsers(){

        UserAppApi userAppi;
        userAppi = getUserApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<List<User>> userFriends =
                userAppi.getFriendsOfUser(loggedUser.getId());
        userFriends.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.failed,
                            Toast.LENGTH_LONG).show();
                }
                if(response.body() != null) {
                    userList.addAll(response.body());
                    userListAll.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });


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
                listenForLatestMessages();
            }
            else{
                userList.addAll((Collection<? extends User>) results.values);
                for(User user : userList){
                    adapter.add(new UserSimpleItem(user, false, true));
                }
            }

        }
    };

    private void findChatPartnerInfo(String chatPartnerId, final ChatMessage message,
                                     final String messageKey){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        emailQuery = usersRef.orderByChild("uid").equalTo(chatPartnerId);
        emailQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            private void sendDataAndUnregister(DataSnapshot dataSnapshot) {
                emailQuery.removeEventListener(this);
                FirebaseUserDTO chatPartnerUser = dataSnapshot.getValue(FirebaseUserDTO.class);
                latestMessageToChatPartner.put(messageKey, chatPartnerUser);
                addLatestMessagesItemViewToView();
            }
        });

    }

    private void addLatestMessagesItemViewToView() {
        adapter.clear();
        for(Map.Entry<String, FirebaseUserDTO> entry : latestMessageToChatPartner.entrySet()){
            adapter.add(new LatestMessageItem(latestMessagesMap.get(entry.getKey()), entry.getValue()));
        }
        adapter.notifyDataSetChanged();
    }

    private void onAdapterItemClick(FirebaseUserDTO chatPartnerUser){
        Intent intent = new Intent(getActivity(), ChatLogActivity.class);
        intent.putExtra(EXTRA_USER_FIREBASE_UID, chatPartnerUser.getUid());
        intent.putExtra(EXTRA_USER_NAME, chatPartnerUser.getUsername());
        intent.putExtra(EXTRA_USER_IMAGE_PATH, chatPartnerUser.getProfileImageUrl());
        intent.putExtra(EXTRA_USER_EMAIL, chatPartnerUser.getEmail());
        getActivity().startActivity(intent);
    }

    /**
     * Refresh data from server
     */
    private void refreshData() {
        //TODO: pozovi refresh data sa servera, osvezi bazu i ponovo iscrtaj listu u ovom fragmentu
        refreshRecyclerViewMessages();
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
