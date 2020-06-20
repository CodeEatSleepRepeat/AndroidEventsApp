package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.SendInvitationsActivity;
import rs.ac.uns.ftn.eventsapp.activities.UserDetailActivity;
import rs.ac.uns.ftn.eventsapp.dtos.UserShareDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class ShareUserSimpleItem extends Item<GroupieViewHolder> {
    public static String EXTRA_USER_NAME = "rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem" +
            ".EXTRA_USER_NAME";
    public static String EXTRA_USER_IMAGE_PATH = "rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem" +
            ".EXTRA_USER_IMAGE_PATH";
    public static String EXTRA_USER_EMAIL = "rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem" +
            ".EXTRA_USER_EMAIL";
    public static String EXTRA_USER_ID = "rs.ac.uns.ftn.eventsapp.views.ShareUserSimpleItem" +
            ".EXTRA_USER_ID";

    private UserShareDTO user;
    private SendInvitationsActivity parentActivity;

    public ShareUserSimpleItem(UserShareDTO user, SendInvitationsActivity parentActivity) {
        this.user = user;
        this.parentActivity = parentActivity;
    }

    @Override
    public void bind(@NonNull final GroupieViewHolder viewHolder, int position) {
        TextView textUsername = viewHolder.itemView.findViewById(R.id.text_username_simple_item);
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_simple_item);
        Context itemViewContext = viewHolder.itemView.getContext();
        CheckBox selectedCB = viewHolder.itemView.findViewById(R.id.selectFriendForShareCB);

        textUsername.setText(user.getUser().getName());
        selectedCB.setChecked(user.getChecked());

        Picasso.get().setLoggingEnabled(true);
        if (user.getUser().getImageUri().startsWith("http")){
            Picasso.get().load(Uri.parse(user.getUser().getImageUri())).placeholder(R.drawable.ic_user_icon).into(imageUser);
        } else {
            Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + user.getUser().getImageUri())).placeholder(R.drawable.ic_user_icon).into(imageUser);
        }
//        Picasso.get()
//                .load(user.getUser().getImageUri())
//                .placeholder(R.drawable.ic_veljko)
//                .error(R.drawable.ic_user_icon)
//                .into(imageUser);

        ConstraintLayout userRow = viewHolder.itemView.findViewById(R.id.share_user_item_row);
        userRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = viewHolder.itemView.findViewById(R.id.selectFriendForShareCB);
                cb.setChecked(!cb.isChecked());
                user.setChecked(cb.isChecked());
                parentActivity.checkedUserCountChanged(user.getChecked(), user.getUser().getId());
            }
        });

        selectedCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                user.setChecked(cb.isChecked());
                parentActivity.checkedUserCountChanged(user.getChecked(), user.getUser().getId());
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.item_share_user_simple;
    }


    private void goToUserDetails(GroupieViewHolder viewHolder) {
        // TODO: Do something when user clicks on row..
        Intent intent = new Intent(viewHolder.itemView.getContext(), UserDetailActivity.class);
        intent.putExtra(EXTRA_USER_NAME, user.getUser().getName());
        intent.putExtra(EXTRA_USER_IMAGE_PATH, user.getUser().getImageUri());
        intent.putExtra(EXTRA_USER_EMAIL, user.getUser().getEmail());
        intent.putExtra(EXTRA_USER_ID, user.getUser().getId());
        viewHolder.itemView.getContext().startActivity(intent);
    }

    public UserShareDTO getUser() {
        return user;
    }
}
