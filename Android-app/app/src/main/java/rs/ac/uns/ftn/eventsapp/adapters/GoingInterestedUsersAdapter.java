package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.UserDTO;

public class GoingInterestedUsersAdapter extends RecyclerView.Adapter<GoingInterestedUsersAdapter.ViewHolder>{

    private List<UserDTO> users = new ArrayList<>();
    private Context mContext;

    public GoingInterestedUsersAdapter(Context mContext, List<UserDTO> users) {
        this.users = users;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_going_interested_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (users.get(position).getImgUri()==null || users.get(position).getImgUri().equals("")){
            Picasso.get()
                    .load(R.drawable.ic_missing_event_icon)
                    .placeholder(R.drawable.ic_missing_event_icon)
                    .error(R.drawable.ic_missing_event_icon)
                    .into(holder.img);
        } else {
            Picasso.get()
                    .load(users.get(position).getImgUri())
                    .placeholder(R.drawable.ic_missing_event_icon)
                    .error(R.drawable.ic_missing_event_icon)
                    .into(holder.img);
        }

        holder.name.setText(users.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name;
        RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.goingInterestedUser);
            name = itemView.findViewById(R.id.goingInterestedUserName);
            parent = itemView.findViewById(R.id.goingInterestedUserParent);
        }
    }

}
