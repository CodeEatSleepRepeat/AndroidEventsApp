package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.EventDetailsActivity;
import rs.ac.uns.ftn.eventsapp.dtos.EventDetailsDTO;
import rs.ac.uns.ftn.eventsapp.models.Event;

public class EventListRecyclerView extends RecyclerView.Adapter<EventListRecyclerView.EventViewHolder> {

    private List<Event> mItems;
    private Context context;
    private int listRowResourceLayout;
    private SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

    public EventListRecyclerView(List<Event> items, Context context, @NonNull int listRowResourceLayout) {
        mItems = items;
        this.context = context;
        this.listRowResourceLayout = listRowResourceLayout;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(listRowResourceLayout, viewGroup, false);

        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, final int i) {
        Event item = mItems.get(i);
        viewHolder.eventNameTextView.setText(item.getEventName());
        viewHolder.eventAddressTextView.setText(item.getLocation());
        viewHolder.eventStartDate.setText(formatter.format(item.getStartTime()));
        try {
            Picasso.get().
                    load(item.getEventImageURI()).
                    placeholder(R.drawable.ic_missing_event_icon).
                    error(R.drawable.ic_missing_event_icon).
                    into(viewHolder.eventImage);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "onBindViewHolder: Picasso error\n" + e.getMessage());
        }

        //postavljanje listenera za going/interested/cancel dugmice
        setOnClickListeners(viewHolder);

        LinearLayout invitationBody = viewHolder.itemView.findViewById(R.id.event_row_body);
        invitationBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent detailsIntent = new Intent(context, EventDetailsActivity.class);

                final Event e = mItems.get(i);
                EventDetailsDTO dto = new EventDetailsDTO(e.getEventId(), e.getEventName(), e.getEventDescription(), e.getEventImageURI(), e.getEventType(),
                        e.getOpenForAll(), e.getStartTime(), e.getEndTime(), e.getLocation(), e.getLongitude(), e.getLatitude(), e.getAuthor().getUserId());
                detailsIntent.putExtra("EVENT", dto);
                context.startActivity(detailsIntent);
            }
        });
    }

    /**
     * Setovanje onClickListenera za dugmeice (KOJI MOZDA NE POSTOJE NA SVAKOM LAYOUTU koji se ovde prosledi - zato je if null
     *  
     */
    private void setOnClickListeners() {
        setOnClickListeners();
    }

    /**
     * Setovanje onClickListenera za dugmeice (KOJI MOZDA NE POSTOJE NA SVAKOM LAYOUTU koji se ovde prosledi - zato je if null
     *
     * @param viewHolder
     */
    private void setOnClickListeners(@NonNull final EventViewHolder viewHolder) {
        ImageView imageInterestedAction =
                viewHolder.itemView.findViewById(R.id.image_interested_item_invitation);
        ImageView imageGoingAction =
                viewHolder.itemView.findViewById(R.id.image_goind_item_invitation);
        ImageView imageDeclineAction =
                viewHolder.itemView.findViewById(R.id.image_not_interested_item_invitation);

        if (imageInterestedAction != null) {
            imageInterestedAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewHolder.itemView.getContext(), "Zainteresovan si",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (imageGoingAction != null) {
            imageGoingAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewHolder.itemView.getContext(), "Oho ides a?",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (imageDeclineAction != null) {
            imageDeclineAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(viewHolder.itemView.getContext(), "A jbga, nista onda drugi put",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventNameTextView;
        private final TextView eventAddressTextView;
        private final TextView eventStartDate;
        private final ImageView eventImage;

        EventViewHolder(View v) {
            super(v);
            eventNameTextView = v.findViewById(R.id.event_list_name);
            eventAddressTextView = v.findViewById(R.id.event_list_address);
            eventStartDate = v.findViewById(R.id.event_list_start_date);
            eventImage = v.findViewById(R.id.event_list_image);
        }
    }

}

