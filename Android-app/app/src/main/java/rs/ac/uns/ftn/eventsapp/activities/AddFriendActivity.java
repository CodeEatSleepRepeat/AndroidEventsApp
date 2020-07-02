package rs.ac.uns.ftn.eventsapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class AddFriendActivity extends AppCompatActivity {

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    private GroupAdapter adapter = new GroupAdapter();
    private List<User> foundUsers = new ArrayList<>();
    private RecyclerView recyclerViewFoundUsers;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        recyclerViewFoundUsers = findViewById(R.id.recycler_view_list_user_add_friend);
        recyclerViewFoundUsers.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_add_friend);

        Button btnSearchUsers = findViewById(R.id.btn_search_add_friend);
        btnSearchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = 0;
                foundUsers.clear();
                adapter.clear();
                searchUsersByUsername();
            }
        });

        addPaginationScrollListenerToRecyclerView();

        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("RESTORE")){
                searchUsersByUsername();
            }
        }

        addNoInternetListener();
    }

    /**
     * Ovo je za proveru konekcije interneta i odmah reaguje na promene, ali ne radi bas ako se vracam iz aktivnosti bez interneta
     */
    private void addNoInternetListener() {
        noInternetDialog = new NoInternetDialog.Builder(this)
                .setBgGradientStart(getResources().getColor(R.color.colorPrimary)) // Start color for background gradient
                .setBgGradientCenter(getResources().getColor(R.color.colorPrimaryDark)) // Center color for background gradient
                .setWifiLoaderColor(R.color.colorBlack) // Set custom color for wifi loader
                .setCancelable(true)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noInternetDialog != null)
            noInternetDialog.onDestroy();
    }

    private void addPaginationScrollListenerToRecyclerView() {
        recyclerViewFoundUsers.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerViewFoundUsers.getLayoutManager(), null, null) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                searchUsersByUsername();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //ovde cu cuvati vrednosti searcha...
        outState.putBoolean("RESTORE", true);
    }

    private void searchUsersByUsername() {
        EditText searchInput = findViewById(R.id.editText_username_search_add_friend);

        UserAppApi userAppi;
        userAppi = getUserApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<List<User>> userFriends =
                userAppi.getUsersWhichContainsUsername(loggedUser.getId(), currentPage,
                        searchInput.getText().toString());
        userFriends.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                if(response.body() != null) {
                    foundUsers.clear();
                    foundUsers.addAll(response.body());
                    refreshFoundUserList();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });

    }

    private void refreshFoundUserList() {
        //adapter.clear();
        for(int i = 0; i < foundUsers.size(); i++){
            boolean isEndingUser = i == foundUsers.size()-1;
            getFriendshipStatus(foundUsers.get(i), isEndingUser);
        }
    }

    private void getFriendshipStatus(final User user, final boolean isEndingUser) {
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<FriendshipDTO> friendshipRequest =
                friendshipAppAPI.getFriendshipOfTwoUsers(loggedUser.getId(), user.getId());
        friendshipRequest.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, Response<FriendshipDTO> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                if(response.body() != null){
                    FriendshipDTO friendship = response.body();
                    boolean isPending = friendship.getStatus().equals("PENDING");
                    boolean isNonExisting = friendship.getStatus().equals("NOT EXISTS");

                    if(isPending)
                        adapter.add(new UserSimpleItem(user, false, false));
                    else if(isNonExisting)
                        adapter.add(new UserSimpleItem(user, true, false));

                    adapter.notifyDataSetChanged();
                    if(isEndingUser)
                        isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<FriendshipDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed,
                        Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
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

    private FriendshipAppAPI getFriendshipApi() {
        FriendshipAppAPI friendshipAppAPI;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        friendshipAppAPI = retrofit.create(FriendshipAppAPI.class);
        return friendshipAppAPI;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
