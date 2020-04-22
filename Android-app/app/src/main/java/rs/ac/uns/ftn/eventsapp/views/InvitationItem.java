package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.text.SimpleDateFormat;
import java.util.Date;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ShowEventDetailsActivity;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.Invitation;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;

public class InvitationItem extends Item<GroupieViewHolder> {

    private Invitation invitation;
    private SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");


    // TODO: Na ulaz treba da ide invitacija objekat
    public InvitationItem(Invitation invitation){
        //postavljanje mockap objekta
        this.invitation = invitation;
    }

    @Override
    public void bind(@NonNull final GroupieViewHolder viewHolder, int position) {
        ImageView eventImage= viewHolder.itemView.findViewById(R.id.image_event_item_invitation);
        TextView eventName = viewHolder.itemView.findViewById(R.id.text_event_name_item_invitation);
        TextView userUsername =
                viewHolder.itemView.findViewById(R.id.text_invited_user_name_item_invitation);
        TextView eventDate = viewHolder.itemView.findViewById(R.id.text_event_date_item_invitation);

        //postavljanje listenera
        setOnClickListeners(viewHolder);
        // TODO: Ovde postaviti pravi url lol :D
        Picasso.get()
                .load(invitation.getEvent().getEventImageURI())
                .placeholder(R.drawable.ic_facebook_logo)
                .error(R.drawable.ic_facebook_logo)
                .into(eventImage);

        eventName.setText(invitation.getEvent().getEventName());
        userUsername.setText("Invited by: " + invitation.getInvitationSender().getUserName());
        eventDate.setText(formatter.format(invitation.getEvent().getStartTime()));

    }

    @Override
    public int getLayout() {
        return R.layout.item_invitation;
    }

    private void setOnClickListeners(@NonNull final GroupieViewHolder viewHolder){
        ImageView imageInterestedAction =
                viewHolder.itemView.findViewById(R.id.image_interested_item_invitation);
        ImageView imageGoingAction =
                viewHolder.itemView.findViewById(R.id.image_goind_item_invitation);
        ImageView imageDeclineAction =
                viewHolder.itemView.findViewById(R.id.image_not_interested_item_invitation);
        ImageView imageEvent =
                viewHolder.itemView.findViewById(R.id.image_event_item_invitation);
        TextView textEventTitle =
                viewHolder.itemView.findViewById(R.id.text_event_name_item_invitation);

        imageInterestedAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewHolder.itemView.getContext(), "Zainteresovan si",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imageGoingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewHolder.itemView.getContext(), "Oho ides a?",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imageDeclineAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewHolder.itemView.getContext(), "A jbga, nista onda drugi put",
                        Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout invitationBody = viewHolder.itemView.findViewById(R.id.event_invitation_body);
        invitationBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEventDetail(viewHolder.itemView.getContext());
            }
        });

        textEventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEventDetail(viewHolder.itemView.getContext());
            }
        });
    }

    private void goToEventDetail(Context context){
        Intent detailsIntent = new Intent(context, ShowEventDetailsActivity.class);

        final Event e = invitation.getEvent();
        detailsIntent.putExtra("id", e.getEventId());
        detailsIntent.putExtra("name", e.getEventName());
        detailsIntent.putExtra("description", e.getEventDescription());
        detailsIntent.putExtra("imageURI", e.getEventImageURI());
        detailsIntent.putExtra("type", e.getEventType());
        detailsIntent.putExtra("open", e.getOpenForAll());
        detailsIntent.putExtra("start", e.getStartTime());
        detailsIntent.putExtra("end", e.getEndTime());
        detailsIntent.putExtra("location", e.getLocation());
        detailsIntent.putExtra("longitude", e.getLongitude());
        detailsIntent.putExtra("latitude", e.getLatitude());
        detailsIntent.putExtra("userId", e.getAuthor().getUserId());

        context.startActivity(detailsIntent);

    }
}
