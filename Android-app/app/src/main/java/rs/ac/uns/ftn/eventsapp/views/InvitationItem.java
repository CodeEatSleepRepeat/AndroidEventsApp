package rs.ac.uns.ftn.eventsapp.views;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.Invitation;

public class InvitationItem extends Item<GroupieViewHolder> {

    private Invitation invitation;

    // TODO: Na ulaz treba da ide invitacija objekat
    public InvitationItem(){

    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        ImageView eventImage= viewHolder.itemView.findViewById(R.id.image_event_item_invitation);
        TextView eventName = viewHolder.itemView.findViewById(R.id.text_event_name_item_invitation);
        TextView userUsername =
                viewHolder.itemView.findViewById(R.id.text_invited_user_name_item_invitation);
        TextView eventDate = viewHolder.itemView.findViewById(R.id.text_event_date_item_invitation);

        // TODO: Ovde postaviti pravi url lol :D
        Picasso.get()
                .load(R.drawable.ic_facebook_logo)
                .placeholder(R.drawable.ic_facebook_logo)
                .error(R.drawable.ic_facebook_logo)
                .into(eventImage);

        eventName.setText("Kobasicijada u Turiji");
        userUsername.setText("Invited by: Nesa Sibirski Tigar");
        eventDate.setText("29.02.2020");
    }

    @Override
    public int getLayout() {
        return R.layout.item_invitation;
    }
}
