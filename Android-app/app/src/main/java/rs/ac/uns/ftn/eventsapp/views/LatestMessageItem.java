package rs.ac.uns.ftn.eventsapp.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;

public class LatestMessageItem extends Item<GroupieViewHolder> {

    private ChatMessage chatMessage;
    private FirebaseUserDTO chatPartnerUser;

    public LatestMessageItem(ChatMessage chatMessage,FirebaseUserDTO chatPartnerUser){
        this.chatMessage = chatMessage;
        this.chatPartnerUser = chatPartnerUser;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        bindDataToView(viewHolder);
    }

    private void bindDataToView(@NonNull GroupieViewHolder viewHolder) {
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_latest_message_item);
        TextView textUserUsername =
                viewHolder.itemView.findViewById(R.id.text_username_latest_message_item);
        TextView textMessage = viewHolder.itemView.findViewById(R.id.text_latest_message_latest_message_item);
        ConstraintLayout constraintLayoutLatestMessage =
                viewHolder.itemView.findViewById(R.id.constraint_layout_latest_message_item);
        TextView messageTime =
                viewHolder.itemView.findViewById(R.id.text_view_date_latest_message_item);


        textUserUsername.setText(chatPartnerUser.getUsername());
        Boolean isMessageByMe = chatMessage.getFromId().equals(FirebaseAuth.getInstance().getUid());
        if(isMessageByMe)
            textMessage.setText(viewHolder.itemView.getContext().getString(R.string.latestMessageMessagePrefixInOurMessage) + " " + chatMessage.getText());
        else
            textMessage.setText(chatMessage.getText());

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
                SimpleDateFormat.SHORT);
        messageTime.setText(dateFormat.format(new Date(chatMessage.getDate())));

        Boolean isChatMessageForUsAndUnseen = !chatMessage.getSeen()
                        && !isMessageByMe;
        if(isChatMessageForUsAndUnseen){
            textUserUsername.setTypeface(null, Typeface.BOLD);
            textUserUsername.setTextColor(Color.rgb(0,0,0));
            textMessage.setTypeface(null, Typeface.BOLD);
            textMessage.setTextColor(Color.rgb(0,0,0));
            constraintLayoutLatestMessage.setBackgroundResource(R.drawable.rounded_new_message_background);
        }

        Picasso.get()
                .load(chatPartnerUser.getProfileImageUrl())
                .placeholder(R.drawable.ic_veljko)
                .error(R.drawable.ic_veljko)
                .into(imageUser);
    }

    @Override
    public int getLayout() {
        return R.layout.item_latest_message;
    }

}
