package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.activities.EventDetailsActivity;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDetailsDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class EventListRecyclerView extends RecyclerView.Adapter<EventListRecyclerView.EventViewHolder> {

    private static final String IMAGE_URI = "http://10.0.2.2:8080/event/image/";
    private List<EventDTO> mItems;
    private Context context;
    private int listRowResourceLayout;
    private SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

    public EventListRecyclerView(List<EventDTO> items, Context context, @NonNull int listRowResourceLayout) {
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
        EventDTO item = mItems.get(i);
        viewHolder.eventNameTextView.setText(item.getName());
        viewHolder.eventAddressTextView.setText(item.getPlace());
        viewHolder.eventStartDate.setText(formatter.format(item.getStart_time()));
        String imageUri = item.getImageUri();
        if (imageUri!=null && !imageUri.equals("") && !imageUri.startsWith("http")){
            imageUri = IMAGE_URI + imageUri;
        }
        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.ic_missing_event_icon)
                .error(R.drawable.ic_missing_event_icon)
                .into(viewHolder.eventImage);


        //postavljanje listenera za going/interested/cancel dugmice
        setOnClickListeners(viewHolder, i, item);

        LinearLayout invitationBody = viewHolder.itemView.findViewById(R.id.event_row_body);
        invitationBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent detailsIntent = new Intent(context, EventDetailsActivity.class);

                final EventDTO e = mItems.get(i);
                String imageUri = e.getImageUri();
                if (imageUri!=null && !imageUri.equals("") && !imageUri.startsWith("http")){
                    imageUri = IMAGE_URI + imageUri;
                }
                EventDetailsDTO dto = new EventDetailsDTO(e.getId(), e.getName(), e.getDescription(), imageUri, e.getType(),
                        e.getPrivacy(), e.getStart_time(), e.getEnd_time(), e.getPlace(), e.getLongitude(), e.getLatitude(), e.getOwner());

                detailsIntent.putExtra("EVENT", dto);
                context.startActivity(detailsIntent);
            }
        });
    }

    /**
     * Setovanje onClickListenera za dugmeice (KOJI MOZDA NE POSTOJE NA SVAKOM LAYOUTU koji se ovde prosledi - zato je if null
     *
     * @param viewHolder
     */
    private void setOnClickListeners(@NonNull final EventViewHolder viewHolder, final int i, final EventDTO item) {
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
                    interestedInEvent(viewHolder, i, item);
                }
            });
        }

        if (imageGoingAction != null) {
            imageGoingAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goingToEvent(viewHolder, i, item);
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
        return mItems == null ? 0 : mItems.size();
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

    public void interestedInEvent(@NonNull final EventViewHolder viewHolder, final int i, final EventDTO item){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.interestedInEvent(item.getId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code()!=200) {
                    Toast.makeText(viewHolder.itemView.getContext(), "You are already interested in this event", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("TAG", response.body().getId().toString());
                    mItems.remove(item);
                    notifyDataSetChanged();
                    Toast.makeText(viewHolder.itemView.getContext(), "Added to Interested Events!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(viewHolder.itemView.getContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goingToEvent(@NonNull final EventViewHolder viewHolder, final int i, final EventDTO event){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.goingToEvent(event.getId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code()!=200) {
                    Toast.makeText(viewHolder.itemView.getContext(), "You are already going to this event", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("TAG", response.body().getId().toString());
                    mItems.remove(event);
                    notifyDataSetChanged();
                    Toast.makeText(viewHolder.itemView.getContext(), "Added to Going Events!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(viewHolder.itemView.getContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
    }

}

