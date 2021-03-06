package rs.ac.uns.ftn.eventsapp.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import org.threeten.bp.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.EventDetailsActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.InvitationAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.GoingInterestedEventsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.InvitationDTO;
import rs.ac.uns.ftn.eventsapp.fragments.InvitationsFragment;
import rs.ac.uns.ftn.eventsapp.models.GoingInterestedStatus;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;

public class InvitationItem extends Item<GroupieViewHolder> {
    private InvitationDTO invitation;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MMM yyy HH:mm z");
    private GroupieViewHolder viewHolder;
    private InvitationsFragment fragment;

    public InvitationItem(InvitationDTO invitation, InvitationsFragment fragment) {
        this.invitation = invitation;
        this.fragment = fragment;
    }

    @Override
    public void bind(@NonNull final GroupieViewHolder viewHolder, int position) {
        ImageView eventImage = viewHolder.itemView.findViewById(R.id.image_event_item_invitation);
        TextView eventName = viewHolder.itemView.findViewById(R.id.text_event_name_item_invitation);
        TextView userUsername =
                viewHolder.itemView.findViewById(R.id.text_invited_user_name_item_invitation);
        TextView eventDate = viewHolder.itemView.findViewById(R.id.text_event_date_item_invitation);
        this.viewHolder = viewHolder;

        setOnClickListeners(viewHolder);
//        // TODO: Ovde postaviti pravi url lol :D
//        Picasso.get()
//                .load(invitation.getEvent().getImageUri())
//                .placeholder(R.drawable.ic_facebook_logo)
//                .error(R.drawable.ic_facebook_logo)
//                .into(eventImage);
        Picasso.get().setLoggingEnabled(true);
        if (invitation.getEvent().getImageUri().startsWith("http")) {
            Picasso.get().load(Uri.parse(invitation.getEvent().getImageUri())).placeholder(R.drawable.ic_user_icon).into(eventImage);
        } else {
            Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + invitation.getEvent().getImageUri())).placeholder(R.drawable.ic_user_icon).into(eventImage);
        }

        eventName.setText(invitation.getEvent().getName());
        userUsername.setText(viewHolder.itemView.getContext().getString(R.string.invited_by) + " " + invitation.getSender().getName());
        eventDate.setText(formatter.format(invitation.getEvent().getStart_time()));

    }

    @Override
    public int getLayout() {
        return R.layout.item_invitation;
    }

    private void setOnClickListeners(@NonNull final GroupieViewHolder viewHolder) {
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
                userInterestedAction();
            }
        });

        imageGoingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userGoingAction();
            }
        });

        imageDeclineAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDeclinedInvitationAction();
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

    private void userGoingAction() {
        EventsAppAPI eventsAppAPI = getEventApi();
        Call<EventDTO> eventDTOCall =
                eventsAppAPI.goingToEvent(invitation.getEvent().getId(),
                        invitation.getReciever().getId());

        eventDTOCall.enqueue(new Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, retrofit2.Response<EventDTO> response) {
                if (response.isSuccessful()) {
                    AppDataSingleton.getInstance().addGIEvent(new GoingInterestedEventsDTO(response.body(), GoingInterestedStatus.GOING));
                    hideInvitationView();
                    fragment.getAllInvitations();
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(),
                            R.string.joined_event_invitations, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
            }

        });
    }

    private void userInterestedAction() {
        EventsAppAPI eventsAppAPI = getEventApi();
        Call<EventDTO> eventDTOCall =
                eventsAppAPI.interestedInEvent(invitation.getEvent().getId(),
                        invitation.getReciever().getId());

        eventDTOCall.enqueue(new Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, retrofit2.Response<EventDTO> response) {
                if (response.isSuccessful()) {
                    AppDataSingleton.getInstance().addGIEvent(new GoingInterestedEventsDTO(response.body(), GoingInterestedStatus.INTERESTED));
                    hideInvitationView();
                    fragment.getAllInvitations();
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(),
                            R.string.interested_event_invitations, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
            }

        });
    }

    private void userDeclinedInvitationAction() {
        deleteInvitation();
    }

    private void deleteInvitation() {
        InvitationAppApi invitationApi = getInvitationApi();
        Call<InvitationDTO> invitationDeleteCall =
                invitationApi.deleteInvitation(invitation.getId());

        invitationDeleteCall.enqueue(new Callback<InvitationDTO>() {
            @Override
            public void onResponse(Call<InvitationDTO> call, retrofit2.Response<InvitationDTO> response) {
                if (response.isSuccessful()) {
                    //hideInvitationView();
                    fragment.getAllInvitations();
                    Toast.makeText(viewHolder.itemView.getContext().getApplicationContext(),
                            R.string.declined_event_invitations, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InvitationDTO> call, Throwable t) {
            }

        });
    }

    private void hideInvitationView() {
        ImageView imageInterestedAction =
                viewHolder.itemView.findViewById(R.id.image_interested_item_invitation);
        ImageView imageGoingAction =
                viewHolder.itemView.findViewById(R.id.image_goind_item_invitation);
        ImageView imageDeclineAction =
                viewHolder.itemView.findViewById(R.id.image_not_interested_item_invitation);
        imageInterestedAction.setVisibility(View.INVISIBLE);
        imageGoingAction.setVisibility(View.INVISIBLE);
        imageDeclineAction.setVisibility(View.INVISIBLE);
    }

    private void goToEventDetail(Context context) {
        Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
        EventDTO transferObject = invitation.getEvent();

        detailsIntent.putExtra("EVENT", transferObject);

        context.startActivity(detailsIntent);
    }

    private InvitationAppApi getInvitationApi() {
        InvitationAppApi invitationApi;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        invitationApi = retrofit.create(InvitationAppApi.class);
        return invitationApi;
    }

    private EventsAppAPI getEventApi() {
        EventsAppAPI eventApi;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppDataSingleton.getInstance().SERVER_IP)
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        eventApi = retrofit.create(EventsAppAPI.class);
        return eventApi;
    }
}
