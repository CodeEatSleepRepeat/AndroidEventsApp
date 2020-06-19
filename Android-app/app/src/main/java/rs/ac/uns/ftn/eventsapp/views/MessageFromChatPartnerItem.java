package rs.ac.uns.ftn.eventsapp.views;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class MessageFromChatPartnerItem extends Item<GroupieViewHolder> {
    private ChatMessage chatMessage;
    private FirebaseUserDTO user;

    public MessageFromChatPartnerItem(ChatMessage chatMessage, FirebaseUserDTO user){
        this.chatMessage = chatMessage;
        this.user = user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_message_from_user_item);
        TextView textMessage = viewHolder.itemView.findViewById(R.id.text_message_from_user_item);
        final TextView messageTime =
                viewHolder.itemView.findViewById(R.id.text_view_message_date_time_item_message_from_chat_partner);

        textMessage.setText(chatMessage.getText());

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
                SimpleDateFormat.SHORT);
        messageTime.setText(dateFormat.format(new Date(chatMessage.getDate())));
        messageTime.setVisibility(View.INVISIBLE);

        // TODO: Ovde treba sliak usera da se izvuce
        bindUserImageToUI(imageUser);

        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageTime.getVisibility() == View.INVISIBLE)
                    messageTime.setVisibility(View.VISIBLE);
                else
                    messageTime.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.item_message_from_chat_partner;
    }

    private void bindUserImageToUI(ImageView imageUser) {
        String userImageUrl = user.getProfileImageUrl();
        if (userImageUrl != null && !userImageUrl.equals("")) {
            try {
                if (userImageUrl.startsWith("http")){
                    Picasso.get().load(Uri.parse(userImageUrl)).placeholder(R.drawable.ic_user_icon).into(imageUser);
                } else {
                    Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + userImageUrl)).placeholder(R.drawable.ic_user_icon).into(imageUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageUser.setImageResource(R.drawable.ic_user_icon);
            }
        } else {
            imageUser.setImageResource(R.drawable.ic_user_icon);
        }
    }
}