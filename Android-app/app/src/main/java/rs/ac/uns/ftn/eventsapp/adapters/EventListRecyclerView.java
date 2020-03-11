package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.ShowEventDetailsActivity;
import rs.ac.uns.ftn.eventsapp.models.Event;

public class EventListRecyclerView extends RecyclerView.Adapter<EventListRecyclerView.EventViewHolder> {

    private List<Event> mItems;

    public EventListRecyclerView(List<Event> items) {
        mItems = items;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_row, viewGroup, false);

        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, final int i) {
        Event item = mItems.get(i);
        viewHolder.eventNameTextView.setText(item.getEventName());

        viewHolder.eventNameTextView.setOnClickListener(new View.OnClickListener() {
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

        EventViewHolder(View v) {
            super(v);
            eventNameTextView = v.findViewById(R.id.list_item);
        }
    }

}

