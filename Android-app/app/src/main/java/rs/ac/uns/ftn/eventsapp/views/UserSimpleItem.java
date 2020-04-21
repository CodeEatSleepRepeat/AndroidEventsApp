package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import rs.ac.uns.ftn.eventsapp.R;
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

    public UserSimpleItem(User user){
        this.user = user;
    }

    @Override
    public void bind(@NonNull final GroupieViewHolder viewHolder, int position) {
        TextView textUsername = viewHolder.itemView.findViewById(R.id.text_username_user_item);
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_item);
        Context itemViewContext = viewHolder.itemView.getContext();

        textUsername.setText(user.getUserName());

        Picasso.get()
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.ic_veljko)
                .error(R.drawable.ic_user_icon)
                .into(imageUser);

        textUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserDetails(viewHolder);
            }
        });

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserDetails(viewHolder);
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
        intent.putExtra(EXTRA_USER_NAME, user.getUserName());
        intent.putExtra(EXTRA_USER_IMAGE_PATH, user.getProfileImageUrl());
        intent.putExtra(EXTRA_USER_EMAIL, user.getEmail());
        viewHolder.itemView.getContext().startActivity(intent);
    }

    public User getUser() {
        return user;
    }
}
