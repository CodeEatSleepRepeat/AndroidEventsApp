package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.UserShareDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem;

public class SendInvitationsActivity extends AppCompatActivity implements Filterable {

    private Toolbar toolbar;
    private ArrayList<Long> checkedUsersIds = new ArrayList<>();
    private ArrayList<String> checkedUsersEmails = new ArrayList<>();
    private List<UserShareDTO> userList = new ArrayList<>();
    private List<UserShareDTO> userListAll = new ArrayList<>();
    private GroupAdapter adapter;
    private FloatingActionButton sendBtn;
    private TextView selectionText;
    private CheckBox selectionCheckbox;
    private Long eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        Intent intent = getIntent();
        eventId = intent.getLongExtra("eventId", -1);

        adapter = new GroupAdapter<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerview_list_of_users);
        recyclerView.setAdapter(adapter);

        toolbar = findViewById(R.id.toolbarShareFriends);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendBtn = findViewById(R.id.floating_send_btn);
        sendBtn.hide();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedUsersEmails.clear();
                for(UserShareDTO user : userListAll){
                    if(checkedUsersIds.contains(user.getUser().getId()))
                        checkedUsersEmails.add(user.getUser().getEmail());
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("SHARED_FRIENDS_IDS", checkedUsersIds);
                returnIntent.putExtra("SHARED_FRIENDS_EMAILS", checkedUsersEmails);
                setResult(Activity.RESULT_OK, returnIntent);

                Toast.makeText(getApplicationContext(), "Invitations send!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        selectionCheckbox = findViewById(R.id.selectionCheckbox);
        selectionCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    selectAllUsers();
                } else {
                    deselectAllUsers();
                }
            }
        });
        selectionText = findViewById(R.id.selectionText);
        selectionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionCheckbox.isChecked()) {
                    deselectAllUsers();
                } else {
                    selectAllUsers();
                }
            }
        });

        getAllFriendUsers();
    }

    private void deselectAllUsers() {
        for (UserShareDTO user : userListAll) {
            user.setChecked(false);
        }
        checkedUsersIds = new ArrayList<>();

        adapter.notifyDataSetChanged();

        selectionCheckbox.setChecked(false);
        selectionText.setText("Select all");
        sendBtn.hide();
    }

    private void selectAllUsers() {
        for (UserShareDTO user : userListAll) {
            user.setChecked(true);
            checkedUsersIds.add(user.getUser().getId());
        }

        adapter.notifyDataSetChanged();

        selectionCheckbox.setChecked(true);
        selectionText.setText("Deselect all");
        sendBtn.show();
    }

    public void checkedUserCountChanged(Boolean checked, Long userId) {
        if (checked) {
            checkedUsersIds.add(userId);
            sendBtn.show();

            if (checkedUsersIds.size() == userListAll.size()) {
                selectionCheckbox.setChecked(true);
                selectionText.setText("Deselect all");
            }
        } else {
            checkedUsersIds.remove(userId);
            selectionCheckbox.setChecked(false);
            selectionText.setText("Select all");

            if (checkedUsersIds.size() == 0) {
                sendBtn.hide();
            }
        }
    }

    private void getAllFriendUsers() {
        UserAppApi userAppi;
        userAppi = getUserApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<List<User>> userFriends =
                userAppi.getFriendsForInvitation(loggedUser.getId(),eventId);
        userFriends.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body() != null) {
                    adapter.clear();

                    for(User user : response.body()){
                        UserShareDTO newUserShare = new UserShareDTO(user, false);
                        userList.add(newUserShare);
                        userListAll.add(newUserShare);
                        adapter.add(new ShareUserSimpleItem(newUserShare,
                                SendInvitationsActivity.this));
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });

    }
//    {
//
//        adapter = new GroupAdapter<>();
//        RecyclerView recyclerView = findViewById(R.id.recyclerview_list_of_users);
//
//        ArrayList<User> users = TestMockup.users;
//        for (User user : users) {
//            UserShareDTO newUserShare = new UserShareDTO(user, false);
//            userList.add(newUserShare);
//            userListAll.add(newUserShare);
//            adapter.add(new ShareUserSimpleItem(newUserShare, this));
//        }
//
//        recyclerView.setAdapter(adapter);
//    }

    /*
     * Filter za filtriranje korisnika
     * po njihovom username-u
     * */
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        SendInvitationsActivity parentActivity = SendInvitationsActivity.this;

        //Ovo se pokrece u background niti...
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<UserShareDTO> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(userListAll);
            } else {
                for (UserShareDTO user : userListAll) {
                    if (user.getUser().getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            userList.addAll((Collection<? extends UserShareDTO>) results.values);
            adapter.clear();

            for (UserShareDTO user : userList) {
                adapter.add(new ShareUserSimpleItem(user, parentActivity));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(getApplicationContext(), "Invitation canceled!", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /**
     * Kod za pamcenje trenutnog stanja liste pri okretanju ekrana
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        long[] longList = new long[checkedUsersIds.size()];
        for (int i = 0; i < checkedUsersIds.size(); i++) {
            longList[i] = checkedUsersIds.get(i);
        }
        outState.putLongArray("checkedUsers", longList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        long[] checkedUsersOld = savedInstanceState.getLongArray("checkedUsers");
        if (checkedUsersOld.length != 0) {
            //toolbar.setNavigationIcon(R.drawable.ic_check_white);
            sendBtn.show();

            for (int i = 0; i < checkedUsersOld.length; i++) {
                checkedUsersIds.add(checkedUsersOld[i]);
                for (UserShareDTO user : userListAll) {
                    if (user.getUser().getId().equals(checkedUsersOld[i])) {
                        user.setChecked(true);
                    }
                }
                for (UserShareDTO user : userList) {
                    if (user.getUser().getId().equals(checkedUsersOld[i])) {
                        user.setChecked(true);
                    }
                }
            }
        } else {
            //toolbar.setNavigationIcon(R.drawable.ic_close_white);
            sendBtn.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_users, menu);
        MenuItem item = menu.findItem(R.id.action_search_users);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilter().filter(newText);
                return false;
            }
        });
        return true;
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
