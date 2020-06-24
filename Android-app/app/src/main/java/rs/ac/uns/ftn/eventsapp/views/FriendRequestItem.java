package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.FriendshipAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.FriendshipDTO;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class FriendRequestItem extends Item<GroupieViewHolder> {
    private User user;
    private Long friendRequestId;
    private RecyclerView.ViewHolder viewHolder;
    private Button acceptButton;
    private Button declineButton;

    public FriendRequestItem(User user, Long friendRequestId){
        this.user = user;
        this.friendRequestId = friendRequestId;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView textUsername = viewHolder.itemView.findViewById(R.id.text_username_friend_request_item);
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_friend_request_item);
        Context itemViewContext = viewHolder.itemView.getContext();
        acceptButton = viewHolder.itemView.findViewById(R.id.btn_accept_friend);
        declineButton = viewHolder.itemView.findViewById(R.id.btn_decline_friend);

        this.viewHolder = viewHolder;

        textUsername.setText(user.getName());

        Picasso.get().setLoggingEnabled(true);
        if (user.getImageUri().startsWith("http")){
            Picasso.get().load(Uri.parse(user.getImageUri())).placeholder(R.drawable.ic_user_icon).into(imageUser);
        } else {
            Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + user.getImageUri())).placeholder(R.drawable.ic_user_icon).into(imageUser);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriendRequest();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineFriendRequest();
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.item_friend_request;
    }

    private void acceptFriendRequest() {
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();
        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        Call<FriendshipDTO> friendshipRequests =
                friendshipAppAPI.acceptRequest(loggedUser.getId(), friendRequestId);

        friendshipRequests.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, Response<FriendshipDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                if(response.body() != null) {
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(),
                            R.string.request_accepted,
                            Toast.LENGTH_LONG).show();
                    declineButton.setVisibility(View.GONE);
                    acceptButton.setOnClickListener(null);
                }
            }

            @Override
            public void onFailure(Call<FriendshipDTO> call, Throwable t) {
                Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(), R.string.failed,
                        Toast.LENGTH_LONG).show();
                Log.d("ERROR", t.toString());
            }
        });
    }

    private void declineFriendRequest() {
        FriendshipAppAPI friendshipAppAPI = getFriendshipApi();

        Call<FriendshipDTO> friendshipRequests =
                friendshipAppAPI.deleteRequest(friendRequestId);

        friendshipRequests.enqueue(new Callback<FriendshipDTO>() {
            @Override
            public void onResponse(Call<FriendshipDTO> call, Response<FriendshipDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                    return;
                }
                if(response.body() != null) {
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(),
                            R.string.request_declined,
                            Toast.LENGTH_LONG).show();
                    acceptButton.setVisibility(View.GONE);
                    declineButton.setOnClickListener(null);
                }
            }

            @Override
            public void onFailure(Call<FriendshipDTO> call, Throwable t) {
                Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(), R.string.failed,
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
}
