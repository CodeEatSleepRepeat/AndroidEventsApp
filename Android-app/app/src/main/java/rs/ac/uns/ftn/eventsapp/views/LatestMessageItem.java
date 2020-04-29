package rs.ac.uns.ftn.eventsapp.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.ChatMessage;
import rs.ac.uns.ftn.eventsapp.models.User;

public class LatestMessageItem extends Item<GroupieViewHolder> {
    private ChatMessage chatMessage;
    private User chatPartnerUser;

    public LatestMessageItem(ChatMessage chatMessage){
        this.chatMessage = chatMessage;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        ImageView imageUser = viewHolder.itemView.findViewById(R.id.image_user_latest_message_item);
        TextView textUserUsername =
                viewHolder.itemView.findViewById(R.id.text_username_latest_message_item);
        TextView textMessage = viewHolder.itemView.findViewById(R.id.text_latest_message_latest_message_item);
        ConstraintLayout constraintLayoutLatestMessage =
                viewHolder.itemView.findViewById(R.id.constraint_layout_latest_message_item);

        // TODO: Na osnovu ID ulogovanog korisnika prvo treba otkriti cije podatke trebamo da
        //  ispisemo jer poruka moze biti nasa, a trebamo uvek prikazivati informacije o drugom
        //  korisniku (znaci nekad je from Id nekad toId). Kada otkrijemo ko je, onda treba iz
        //  baze izvuci informaciju o korisniku i ispisati njegove vrednosti.. tako cemo mi sad
        //  napraviti nesto random skroz...
        chatPartnerUser = new User(20l, "Veljko Man", "nema");
        textUserUsername.setText(chatPartnerUser.getUserName());
        textMessage.setText(chatMessage.getText());

        //TODO: pored provere SEEN moramo gledati da li je poruka namenjena nama!
        if(!chatMessage.getSeen()){
            textUserUsername.setTypeface(null, Typeface.BOLD);
            textUserUsername.setTextColor(Color.rgb(0,0,0));
            textMessage.setTypeface(null, Typeface.BOLD);
            textMessage.setTextColor(Color.rgb(0,0,0));
            constraintLayoutLatestMessage.setBackgroundResource(R.drawable.rounded_new_message_background);
        }

        Picasso.get()
                .load(R.drawable.ic_veljko)
                .placeholder(R.drawable.ic_veljko)
                .error(R.drawable.ic_veljko)
                .into(imageUser);


    }

    @Override
    public int getLayout() {
        return R.layout.item_latest_message;
    }
}
