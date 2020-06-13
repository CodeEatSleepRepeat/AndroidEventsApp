package rs.ac.uns.ftn.eventsapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class UserDetailActivity extends AppCompatActivity {

    private User user;
    private FriendshipDTO friendship;
    private String username;
    private String user_profile_picture_url;
    private Long userId;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setting username
        String intentExtraUsername = getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_NAME);
        if(intentExtraUsername != null){
            TextView textUserEvents = findViewById(R.id.text_user_events_user_detail);
            textUserEvents.setText(intentExtraUsername+ "'s Events");
            getSupportActionBar().setTitle(intentExtraUsername);
        }

        //setting user email
        String userEmail = getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_EMAIL);
        if(userEmail != null){
            TextView textUserEmail = findViewById(R.id.text_user_email_user_detail);
            textUserEmail.setText(userEmail);
        }

        //setting user image
        String intentExtraProfilePicUrl =
                getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_IMAGE_PATH);
        if(intentExtraProfilePicUrl != null){
            ImageView userProfilePicUrl = findViewById(R.id.image_user_user_detail);
            Picasso.get().setLoggingEnabled(true);
            if (intentExtraProfilePicUrl.startsWith("http")){
                Picasso.get().load(Uri.parse(intentExtraProfilePicUrl)).placeholder(R.drawable.ic_user_icon).into(userProfilePicUrl);
            } else {
                Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + intentExtraProfilePicUrl)).placeholder(R.drawable.ic_user_icon).into(userProfilePicUrl);
            }
        }

        userId = getIntent().getLongExtra(UserSimpleItem.EXTRA_USER_ID, 0);
        getUserFromApi();

        //adding listener to imageMessage...
        ImageView imageMessage = findViewById(R.id.image_message_user_detail);
        imageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatLog();
            }
        });

        fillUserEvents();
    }

    private void getUserFromApi() {
        UserAppApi userAppApi = getUserApi();

        Call<User> userRequest =
                userAppApi.getUser(userId);

        userRequest.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
                if(response.body() != null)
                    user = response.body();
                    getFriendshipStatus();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed,
                        Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    private void getFriendshipStatus() {
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();

        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<FriendshipDTO> friendshipRequest =
                friendshipAppAPI.getFriendshipOfTwoUsers(loggedUser.getId(), userId);
        friendshipRequest.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, Response<FriendshipDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
                if(response.body() != null){
                    friendship = response.body();
                    setViewByFriendshipStatus(response.body());
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

    private void setViewByFriendshipStatus(FriendshipDTO friendship) {
        boolean isPending = friendship.getStatus().equals("PENDING");
        boolean isAccepted = friendship.getStatus().equals("ACCEPTED");
        boolean isNonExisting = friendship.getStatus().equals("NOT EXISTS");

        if(isNonExisting){
            setViewAsNonFriend();
        }
        else if(isPending){
            setViewAsStillInPendingRequest();
        }
        else if(isAccepted){
            setViewAsFriend();
        }
    }

    private void setViewAsFriend() {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_of_friend_user_details, menu);
        ImageView imageMessage = findViewById(R.id.image_message_user_detail);
        imageMessage.setVisibility(View.VISIBLE);
    }

    private void setViewAsStillInPendingRequest() {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_of_user_who_is_still_on_pending_user_details, menu);
        ImageView imageMessage = findViewById(R.id.image_message_user_detail);
        imageMessage.setVisibility(View.GONE);
    }

    private void setViewAsNonFriend() {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_of_non_friend_user_details, menu);
        ImageView imageMessage = findViewById(R.id.image_message_user_detail);
        imageMessage.setVisibility(View.GONE);
    }

    private void goToChatLog(){
        Intent intent = new Intent(this, ChatLogActivity.class);
        startActivity(intent);
    }

    private void fillUserEvents(){
        List<EventDTO> items = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EventListRecyclerView(items, this, R.layout.event_list_row));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_of_non_friend_user_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_remove_friend:
                deleteFriendship();
                Toast.makeText(this, R.string.friend_removed, Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_friend_user_details:
                addFriendRequest();
                Toast.makeText(this, R.string.friend_request_send, Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_cancel_request:
                deleteFriendship();
                Toast.makeText(this, R.string.cancel_request, Toast.LENGTH_SHORT).show();
                break;
            default:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFriendRequest() {
        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();

        final FriendshipAppAPI friendshipApi = retrofit.create(FriendshipAppAPI.class);
        Call<FriendshipDTO> friendshipCall = friendshipApi.sendFriendRequest(loggedUser.getId(),
                user.getId());

        friendshipCall.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, retrofit2.Response<FriendshipDTO> response) {
                if (response.isSuccessful()){
                    setViewAsStillInPendingRequest();
                    friendship = response.body();
                }

            }

            @Override
            public void onFailure(Call<FriendshipDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "loool",
                        Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void deleteFriendship() {
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();

        Call<FriendshipDTO> friendshipRequests =
                friendshipAppAPI.deleteRequest(friendship.getId());

        friendshipRequests.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, Response<FriendshipDTO> response) {
                if (response.isSuccessful())
                    setViewAsNonFriend();
            }

            @Override
            public void onFailure(Call<FriendshipDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed,
                        Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    private FriendshipAppAPI getFriendshipApi() {
        FriendshipAppAPI friendshipAppAPI;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        friendshipAppAPI = retrofit.create(FriendshipAppAPI.class);
        return friendshipAppAPI;
    }

    private UserAppApi getUserApi() {
        UserAppApi userAppi;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        userAppi = retrofit.create(UserAppApi.class);
        return userAppi;
    }
}
