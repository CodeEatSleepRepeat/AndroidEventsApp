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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.UserAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;
import rs.ac.uns.ftn.eventsapp.dtos.SearchFilterEventsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.firebase.notification.APIFirebaseNotificationService;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Client;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Token;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Data;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.NotificationTypeEnum;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Sender;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.PaginationScrollListener;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class UserDetailActivity extends AppCompatActivity {
    public static String EXTRA_USER_NAME = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_NAME";
    public static String EXTRA_USER_IMAGE_PATH = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_IMAGE_PATH";
    public static String EXTRA_USER_EMAIL = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_EMAIL";
    public static String EXTRA_USER_FIREBASE_UID = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_FIREBASE_UID";

    private User user;
    private FriendshipDTO friendship;
    private Long userId;
    private Menu menu;
    private Query emailQuery;
    private static final int PAGE_START = 0;
    private static List<EventDTO> items = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private static int currentPage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_details);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventListRecyclerView(items, this, R.layout.event_list_row);
        recyclerView.setAdapter(adapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setting username
        String intentExtraUsername = getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_NAME);
        if(intentExtraUsername != null){
            TextView textUserEvents = findViewById(R.id.text_user_events_user_detail);
            textUserEvents.setText(intentExtraUsername+ " " +getString(R.string.app_name));
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

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager, null, null) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                getEventsPage(currentPage);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

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
                    return;
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

        if (loggedUser.getId()==userId){
            //this is me
            return;
        }

        Call<FriendshipDTO> friendshipRequest =
                friendshipAppAPI.getFriendshipOfTwoUsers(loggedUser.getId(), userId);
        friendshipRequest.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, Response<FriendshipDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
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

    private void setViewAsMyself() {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_of_myself_user_details, menu);
        ImageView imageMessage = findViewById(R.id.image_message_user_detail);
        imageMessage.setVisibility(View.GONE);
    }

    private void goToChatLog(){

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        emailQuery = usersRef.orderByChild("email").equalTo(user.getEmail());
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
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }

            private void sendDataAndUnregister(DataSnapshot dataSnapshot) {
                emailQuery.removeEventListener(this);
                FirebaseUserDTO foundRequestReciever = dataSnapshot.getValue(FirebaseUserDTO.class);

                Intent intent = new Intent(UserDetailActivity.this, ChatLogActivity.class);
                intent.putExtra(EXTRA_USER_FIREBASE_UID, foundRequestReciever.getUid());
                intent.putExtra(EXTRA_USER_NAME, foundRequestReciever.getUsername());
                intent.putExtra(EXTRA_USER_IMAGE_PATH, foundRequestReciever.getProfileImageUrl());
                intent.putExtra(EXTRA_USER_EMAIL, foundRequestReciever.getEmail());
                startActivity(intent);
        }
        });
    }

    private void fillUserEvents(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_of_non_friend_user_details, menu);

        if (AppDataSingleton.getInstance().getLoggedUser().getId()==userId){
            //this is me
            setViewAsMyself();
        }

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
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
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
                    findFirebaseReceiverUserThenSendNotification();
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

    private void findFirebaseReceiverUserThenSendNotification() {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        emailQuery = usersRef.orderByChild("email").equalTo(user.getEmail());
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
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }

            private void sendDataAndUnregister(DataSnapshot dataSnapshot) {
                emailQuery.removeEventListener(this);
                FirebaseUserDTO foundRequestReciever = dataSnapshot.getValue(FirebaseUserDTO.class);

                APIFirebaseNotificationService apiFirebaseService =
                        Client.getRetrofit("https://fcm.googleapis.com/").create(APIFirebaseNotificationService .class);

                sendFriendRequestNotification(apiFirebaseService, foundRequestReciever.getUid());
            }
        });

    }

    private void sendFriendRequestNotification(final APIFirebaseNotificationService apiFirebaseService,
                                               final String toId) {
        final String loggedUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(toId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token chatPartnerToken = ds.getValue(Token.class);
                    String notificationBody =
                            getResources().getString(R.string.friend_request_notification);
                    String notificationTitle =
                            getResources().getString(R.string.friend_requests);
                    Data data = new Data(loggedUserUid, notificationBody, notificationTitle,
                            toId, R.drawable.logo, NotificationTypeEnum.FRIEND_REQUEST);

                    assert chatPartnerToken != null;
                    Sender sender = new Sender(data, chatPartnerToken.getToken());
                    apiFirebaseService.sendNotification(sender)
                            .enqueue(new Callback<rs.ac.uns.ftn.eventsapp.firebase.notification.Response>() {
                                @Override
                                public void onResponse(Call<rs.ac.uns.ftn.eventsapp.firebase.notification.Response> call, retrofit2.Response<rs.ac.uns.ftn.eventsapp.firebase.notification.Response> response) {

                                }

                                @Override
                                public void onFailure(Call<rs.ac.uns.ftn.eventsapp.firebase.notification.Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getEventsPage(int num) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<List<EventDTO>> events = e.getMyEvents(userId, num, new SearchFilterEventsDTO());
        events.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                isLoading = false;
                items.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        currentPage = PAGE_START;
        getEventsPage(PAGE_START);
    }

}
