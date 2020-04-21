package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ShowEventDetailsActivity;
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
        try{
            Picasso.get().
                    load(item.getEventImageURI()).
                    placeholder(R.drawable.ic_missing_event_icon).
                    error(R.drawable.ic_missing_event_icon).
                    into(viewHolder.eventImage);
        } catch (Exception e){
            Log.d(this.getClass().getName(), "onBindViewHolder: Picasso error\n" + e.getMessage());
        }

        //viewHolder.eventImage.setImageURI(Uri.parse(item.getEventImageURI()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent detailsIntent = new Intent(context, ShowEventDetailsActivity.class);

                final Event e = mItems.get(i);
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
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

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

