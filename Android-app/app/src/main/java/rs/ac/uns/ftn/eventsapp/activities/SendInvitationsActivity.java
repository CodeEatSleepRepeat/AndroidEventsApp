package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.UserShareDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem;

public class SendInvitationsActivity extends AppCompatActivity implements Filterable {

    private Toolbar toolbar;
    private ArrayList<Long> checkedUsers = new ArrayList<>();
    private List<UserShareDTO> userList = new ArrayList<>();
    private List<UserShareDTO> userListAll = new ArrayList<>();
    private GroupAdapter adapter;
    private FloatingActionButton sendBtn;
    private TextView selectionText;
    private CheckBox selectionCheckbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

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
                Intent returnIntent = new Intent();
                returnIntent.putExtra("SHARED_FRIENDS", checkedUsers);
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
        checkedUsers = new ArrayList<>();

        adapter.notifyDataSetChanged();

        selectionCheckbox.setChecked(false);
        selectionText.setText("Select all");
        sendBtn.hide();
    }

    private void selectAllUsers() {
        for (UserShareDTO user : userListAll) {
            user.setChecked(true);
            checkedUsers.add(user.getUser().getUserId());
        }

        adapter.notifyDataSetChanged();

        selectionCheckbox.setChecked(true);
        selectionText.setText("Deselect all");
        sendBtn.show();
    }

    public void checkedUserCountChanged(Boolean checked, Long userId) {
        if (checked) {
            checkedUsers.add(userId);
            sendBtn.show();

            if (checkedUsers.size() == userListAll.size()) {
                selectionCheckbox.setChecked(true);
                selectionText.setText("Deselect all");
            }
        } else {
            checkedUsers.remove(userId);
            selectionCheckbox.setChecked(false);
            selectionText.setText("Select all");

            if (checkedUsers.size() == 0) {
                sendBtn.hide();
            }
        }
    }

    private void getAllFriendUsers() {

        adapter = new GroupAdapter<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerview_list_of_users);

        ArrayList<User> users = TestMockup.users;
        for (User user : users) {
            UserShareDTO newUserShare = new UserShareDTO(user, false);
            userList.add(newUserShare);
            userListAll.add(newUserShare);
            adapter.add(new ShareUserSimpleItem(newUserShare, this));
        }

        recyclerView.setAdapter(adapter);
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
        SendInvitationsActivity parentActivity = SendInvitationsActivity.this;

        //Ovo se pokrece u background niti...
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<UserShareDTO> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(userListAll);
            } else {
                for (UserShareDTO user : userListAll) {
                    if (user.getUser().getUserName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
        long[] longList = new long[checkedUsers.size()];
        for (int i = 0; i < checkedUsers.size(); i++) {
            longList[i] = checkedUsers.get(i);
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
                checkedUsers.add(checkedUsersOld[i]);
                for (UserShareDTO user : userListAll) {
                    if (user.getUser().getUserId().equals(checkedUsersOld[i])) {
                        user.setChecked(true);
                    }
                }
                for (UserShareDTO user : userList) {
                    if (user.getUser().getUserId().equals(checkedUsersOld[i])) {
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

}
