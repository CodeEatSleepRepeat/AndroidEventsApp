package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ChatLogActivity;
import rs.ac.uns.ftn.eventsapp.activities.UserDetailActivity;
import rs.ac.uns.ftn.eventsapp.models.User;

public class UserSimpleItem extends Item<GroupieViewHolder>{
    public static String EXTRA_USER_NAME = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_NAME";
    public static String EXTRA_USER_IMAGE_PATH = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_IMAGE_PATH";
    public static String EXTRA_USER_EMAIL = "rs.ac.uns.ftn.eventsapp.views.UserSimpleItem" +
            ".EXTRA_USER_EMAIL";

    private User user;
    private Boolean setAddButton;
    private Boolean onClickGoToChatRoom;

    public UserSimpleItem(User user, Boolean setAddButton, Boolean onClickGoToChatRoom){

        this.user = user;
        this.setAddButton = setAddButton;
        this.onClickGoToChatRoom = onClickGoToChatRoom;

    }

    @Override
    public void bind(@NonNull final GroupieViewHolder viewHolder, int position) {
        TextView textUsername = viewHolder.itemView.findViewById(R.id.text_username_user_item);
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_item);
        Context itemViewContext = viewHolder.itemView.getContext();
        Button addButton = viewHolder.itemView.findViewById(R.id.btn_add_simple_user_item);
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

        Picasso.get()
                .load(user.getImageUri())
                .placeholder(R.drawable.ic_veljko)
                .error(R.drawable.ic_user_icon)
                .into(imageUser);

        textUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickGoToChatRoom){
                    goToChatRoom(viewHolder);
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
                    goToChatRoom(viewHolder);
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
        viewHolder.itemView.getContext().startActivity(intent);
    }

    private void goToChatRoom(GroupieViewHolder viewHolder){
        Intent intent = new Intent(viewHolder.itemView.getContext(), ChatLogActivity.class);
        viewHolder.itemView.getContext().startActivity(intent);
    }

    private void addFriendAction(GroupieViewHolder viewHolder) {
        Toast.makeText(viewHolder.itemView.getContext(), "Hvala na prijateljstvu",
                Toast.LENGTH_SHORT).show();
    }

    public User getUser() {
        return user;
    }
}
