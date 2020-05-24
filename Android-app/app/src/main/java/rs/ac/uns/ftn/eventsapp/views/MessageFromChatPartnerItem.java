package rs.ac.uns.ftn.eventsapp.views;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.models.User;

public class MessageFromChatPartnerItem extends Item<GroupieViewHolder> {
    private String chatMessage;
    private FirebaseUserDTO user;

    public MessageFromChatPartnerItem(String chatMessage, FirebaseUserDTO user){
        this.chatMessage = chatMessage;
        this.user = user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_message_from_user_item);
        TextView textMessage = viewHolder.itemView.findViewById(R.id.text_message_from_user_item);

        textMessage.setText(chatMessage);
        // TODO: Ovde treba sliak usera da se izvuce
        Picasso.get()
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.ic_veljko)
                .error(R.drawable.ic_veljko)
                .into(imageUser);
    }

    @Override
    public int getLayout() {
        return R.layout.item_message_from_chat_partner;
    }
}