package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ChatLogActivity;
import rs.ac.uns.ftn.eventsapp.activities.UserDetailActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.firebase.notification.APIFirebaseNotificationService;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Client;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Response;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Token;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Data;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.NotificationTypeEnum;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Sender;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class UserSimpleItem extends Item<GroupieViewHolder>{
    public static String EXTRA_USER_NAME = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_NAME";
    public static String EXTRA_USER_IMAGE_PATH = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_IMAGE_PATH";
    public static String EXTRA_USER_EMAIL = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_EMAIL";
    public static String EXTRA_USER_FIREBASE_UID = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_FIREBASE_UID";
    public static String EXTRA_USER_ID = "rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem" +
            ".EXTRA_USER_ID";

    private User user;
    private Boolean setAddButton;
    private Boolean onClickGoToChatRoom;
    private Query emailQuery;
    private RecyclerView.ViewHolder viewHolder;
    private Button addButton;

    public UserSimpleItem(User user, Boolean setAddButton, Boolean onClickGoToChatRoom){

        this.user = user;
        this.setAddButton = setAddButton;
        this.onClickGoToChatRoom = onClickGoToChatRoom;

    }

    @Override
    public void bind(@NonNull final GroupieViewHolder viewHolder, int position) {
        TextView textUsername = viewHolder.itemView.findViewById(R.id.text_username_simple_item);
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_simple_item);
        Context itemViewContext = viewHolder.itemView.getContext();
        addButton = viewHolder.itemView.findViewById(R.id.btn_add_friend);
        this.viewHolder = viewHolder;
        if(!setAddButton){
            addButton.setVisibility(View.GONE);
        }
        else {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriendAction(viewHolder);
                }
            });
        }

        textUsername.setText(user.getName());

        Picasso.get().setLoggingEnabled(true);
        if (user.getImageUri().startsWith("http")){
            Picasso.get().load(Uri.parse(user.getImageUri())).placeholder(R.drawable.ic_user_icon).into(imageUser);
        } else {
            Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + user.getImageUri())).placeholder(R.drawable.ic_user_icon).into(imageUser);
        }

        textUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickGoToChatRoom){
                    findChatPartnerAndGoToChatRoom(viewHolder);
                }
                else{
                    goToUserDetails(viewHolder);
                }

            }
        });

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickGoToChatRoom){
                    findChatPartnerAndGoToChatRoom(viewHolder);
                }
                else{
                    goToUserDetails(viewHolder);
                }
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.item_user_simple;
    }

    private void goToUserDetails(GroupieViewHolder viewHolder) {
        // TODO: Do something when user clicks on row..
        Intent intent = new Intent(viewHolder.itemView.getContext(), UserDetailActivity.class);
        intent.putExtra(EXTRA_USER_NAME, user.getName());
        intent.putExtra(EXTRA_USER_IMAGE_PATH, user.getImageUri());
        intent.putExtra(EXTRA_USER_EMAIL, user.getEmail());
        intent.putExtra(EXTRA_USER_ID, user.getId());

        viewHolder.itemView.getContext().startActivity(intent);
    }

    private void findChatPartnerAndGoToChatRoom(final GroupieViewHolder viewHolder){

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
                FirebaseUserDTO foundUser = dataSnapshot.getValue(FirebaseUserDTO.class);

                Intent intent = new Intent(viewHolder.itemView.getContext(), ChatLogActivity.class);
                intent.putExtra(EXTRA_USER_FIREBASE_UID, foundUser.getUid());
                intent.putExtra(EXTRA_USER_NAME, user.getName());
                intent.putExtra(EXTRA_USER_IMAGE_PATH, user.getImageUri());
                intent.putExtra(EXTRA_USER_EMAIL, user.getEmail());

                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

    }

    private void addFriendAction(final GroupieViewHolder viewHolder) {
        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();

        FriendshipAppAPI friendshipApi = retrofit.create(FriendshipAppAPI.class);
        Call<FriendshipDTO> friendshipCall = friendshipApi.sendFriendRequest(loggedUser.getId(),
                user.getId());

        friendshipCall.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, retrofit2.Response<FriendshipDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.already_friends,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.friend_request_send,
                            Toast.LENGTH_SHORT).show();
                    addButton.setVisibility(View.GONE);
                    findFirebaseReceiverUserThenSendNotification();
                }
            }

            @Override
            public void onFailure(Call<FriendshipDTO> call, Throwable t) {
                Toast.makeText(viewHolder.itemView.getContext(), R.string.failed,
                        Toast.LENGTH_SHORT).show();
            }

        });
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
        if(FirebaseAuth.getInstance().getUid() == null)
            return;

        final String loggedUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(toId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token chatPartnerToken = ds.getValue(Token.class);
                    String notificationBody =
                            viewHolder.itemView.getContext().getResources().getString(R.string.friend_request_notification);
                    String notificationTitle =
                            viewHolder.itemView.getContext().getResources().getString(R.string.friend_requests);
                    Data data = new Data(loggedUserUid, notificationBody, notificationTitle,
                            toId, R.drawable.logo, NotificationTypeEnum.FRIEND_REQUEST);

                    assert chatPartnerToken != null;
                    Sender sender = new Sender(data, chatPartnerToken.getToken());
                    apiFirebaseService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public User getUser() {
        return user;
    }
}
