package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.User;

public class UserSimpleItem extends Item<GroupieViewHolder>{
    private User user;

    public UserSimpleItem(User user){
        this.user = user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView textUsername = viewHolder.itemView.findViewById(R.id.text_username_user_item);
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_item);
        Context itemViewContext = viewHolder.itemView.getContext();

        textUsername.setText(user.getUserName());

        Picasso.get()
                .load(R.drawable.ic_veljko)
                .placeholder(R.drawable.ic_veljko)
                .error(R.drawable.ic_user_icon)
                .into(imageUser);
    }

    @Override
    public int getLayout() {
        return R.layout.item_user_simple;
    }
}
