package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.FriendRequestItem;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.xwray.groupie.GroupAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendRequestsActivity extends AppCompatActivity {

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private int currentPage = PAGE_START;
    private GroupAdapter adapter = new GroupAdapter();
    private List<FriendshipDTO> friendRequests = new ArrayList<>();
    private RecyclerView recyclerViewFriendRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.friend_requests);

        recyclerViewFriendRequests = findViewById(R.id.recycler_view_friend_requestss);
        recyclerViewFriendRequests.setAdapter(adapter);

//        List<User> dumpUsers = TestMockup.users;
//        for(User user : dumpUsers){
//            adapter.add(new FriendRequestItem(user, 1l));
//            adapter.notifyDataSetChanged();
//        }

        getFriendRequests();

        addPaginationScrollListenerToRecyclerView();
    }

    private void addPaginationScrollListenerToRecyclerView() {
        recyclerViewFriendRequests.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerViewFriendRequests.getLayoutManager(), null, null) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                Log.d("salepare", "usao ovde opet");
                currentPage += 1;
                getFriendRequests();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void getFriendRequests(){
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<List<FriendshipDTO>> friendshipRequests =
                friendshipAppAPI.getFriendRequests(loggedUser.getId(), currentPage);
        friendshipRequests.enqueue(new Callback<List<FriendshipDTO>>() {
            @Override
            public void onResponse(Call<List<FriendshipDTO>> call, Response<List<FriendshipDTO>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
                if(response.body() != null) {
                    friendRequests.clear();
                    friendRequests.addAll(response.body());
                    refreshRequestViews();
                }
            }

            @Override
            public void onFailure(Call<List<FriendshipDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });

    }

    private void refreshRequestViews() {
        for(int i = 0; i < friendRequests.size(); i++){
            boolean isItTheEnd;
            isItTheEnd = i == friendRequests.size() - 1;

            getUserInfoFromApiAndCreateItemForAdapter(friendRequests.get(i), isItTheEnd);
        }
    }

    private void getUserInfoFromApiAndCreateItemForAdapter(final FriendshipDTO friendshipDTO, final boolean isItTheEnd) {
        UserAppApi userAppi = getUserApi();

        Call<User> userFromFriendRequest =
                userAppi.getUser(friendshipDTO.getSenderId());
        userFromFriendRequest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
                if(response.body() != null) {
                    User requestSender = response.body();
                    adapter.add(new FriendRequestItem(requestSender, friendshipDTO.getId()));
                    adapter.notifyDataSetChanged();
                    if(isItTheEnd)
                        isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
