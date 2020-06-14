package rs.ac.uns.ftn.eventsapp.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

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


        Boolean isMessageByMe = setPresentedText(viewHolder, textUserUsername, textMessage);

        bindDateAndTimeToUI(messageTime);

        highlightMessageIfUnseen(textUserUsername, textMessage, constraintLayoutLatestMessage, isMessageByMe);

        bindUserImageToUI(imageUser);
    }

    private void bindDateAndTimeToUI(TextView messageTime) {
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
                SimpleDateFormat.SHORT);
        messageTime.setText(dateFormat.format(new Date(chatMessage.getDate())));
    }

    @NotNull
    private Boolean setPresentedText(@NonNull GroupieViewHolder viewHolder, TextView textUserUsername, TextView textMessage) {
        textUserUsername.setText(chatPartnerUser.getUsername());
        int lenghtOfChatMessage = chatMessage.getText().length();
        String chatMessageText = getFirstNCharactersFromString(20, lenghtOfChatMessage);
        Boolean isMessageByMe = chatMessage.getFromId().equals(FirebaseAuth.getInstance().getUid());
        if(isMessageByMe)
            textMessage.setText(viewHolder.itemView.getContext().getString(R.string.latestMessageMessagePrefixInOurMessage) + " " + chatMessageText);
        else
            textMessage.setText(chatMessageText);
        return isMessageByMe;
    }

    @NotNull
    private String getFirstNCharactersFromString(int n, int lenghtOfChatMessage) {
        String message = chatMessage.getText().substring(0, Math.min(lenghtOfChatMessage,
                n));
        if(lenghtOfChatMessage > n)
            message += "...";
        return message;
    }

    private void highlightMessageIfUnseen(TextView textUserUsername, TextView textMessage, ConstraintLayout constraintLayoutLatestMessage, Boolean isMessageByMe) {
        Boolean isChatMessageForUsAndUnseen = !chatMessage.getSeen()
                        && !isMessageByMe;
        if(isChatMessageForUsAndUnseen){
            textUserUsername.setTypeface(null, Typeface.BOLD);
            textUserUsername.setTextColor(Color.rgb(0,0,0));
            textMessage.setTypeface(null, Typeface.BOLD);
            textMessage.setTextColor(Color.rgb(0,0,0));
            constraintLayoutLatestMessage.setBackgroundResource(R.drawable.rounded_new_message_background);
        }
    }

    private void bindUserImageToUI(ImageView imageUser) {
        String userImageUrl = chatPartnerUser.getProfileImageUrl();
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

    @Override
    public int getLayout() {
        return R.layout.item_latest_message;
    }

    public FirebaseUserDTO getChatPartnerUser() {
        return chatPartnerUser;
    }

}
