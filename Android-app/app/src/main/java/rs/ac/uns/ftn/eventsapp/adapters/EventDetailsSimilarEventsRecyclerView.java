package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.SimilarEventDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class EventDetailsSimilarEventsRecyclerView extends RecyclerView.Adapter<EventDetailsSimilarEventsRecyclerView.ViewHolder>{

    private List<SimilarEventDTO> dtos;
    private Context mContext;

    public EventDetailsSimilarEventsRecyclerView(Context mContext, List<SimilarEventDTO> dtos) {
        this.dtos = dtos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventdetails_similarevent_item, parent, false);
        return new EventDetailsSimilarEventsRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimilarEventDTO dto = dtos.get(position);
        String uri = dto.getEventImg();
        if (uri != null && !uri.equals("") && !uri.startsWith("http")) {
            uri = AppDataSingleton.IMAGE_URI + uri;
        }else if (uri == null || uri.equals("")) {
            uri = "image"; //for picasso to not crash if image is empty or null
        }
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.ic_missing_event_icon)
                .error(R.drawable.ic_missing_event_icon)
                .into(holder.image);

        holder.eventName.setText(dto.getEventName());
    }

    @Override
    public int getItemCount() {
        return dtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView eventName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.eventImageViewRV);
            eventName = itemView.findViewById(R.id.eventNameEventDetailsRV);
        }
    }
}
