package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.ac.uns.ftn.eventsapp.R;

public class EventDetailsUserRecyclerView extends RecyclerView.Adapter<EventDetailsUserRecyclerView.ViewHolder>{

    private List<String> mItems = new ArrayList<>();
    private Context mContext;

    public EventDetailsUserRecyclerView(Context mContext, List<String> uris) {
        this.mItems = uris;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EventDetailsUserRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventdetails_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventDetailsUserRecyclerView.ViewHolder holder, int position) {
        Picasso.get()
                .load(mItems.get(position))
                .placeholder(R.drawable.ic_missing_event_icon)
                .error(R.drawable.ic_missing_event_icon)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        Log.d("SIZE", "" + mItems.size());
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.userImgEventDetailsRV);
        }
    }

}
