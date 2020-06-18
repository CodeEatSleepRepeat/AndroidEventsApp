package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.net.Uri;
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
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

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
        String imageUri = mItems.get(position);
        if (!imageUri.equals("")) {
            Picasso.get().setLoggingEnabled(true);
            if (imageUri.startsWith("http")) {
                Picasso.get().load(Uri.parse(imageUri)).placeholder(R.drawable.ic_user_icon_black).into(holder.image);
            } else {
                Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + imageUri)).placeholder(R.drawable.ic_user_icon_black).into(holder.image);
            }
        } else {
            holder.image.setImageResource(R.drawable.ic_user_icon_black);
        }
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
