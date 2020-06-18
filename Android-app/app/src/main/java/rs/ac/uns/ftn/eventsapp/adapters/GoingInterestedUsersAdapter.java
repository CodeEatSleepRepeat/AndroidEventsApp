package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.net.Uri;
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
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

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

        String imageUri = users.get(position).getImgUri();
        if (!imageUri.equals("")) {
            Picasso.get().setLoggingEnabled(true);
            if (imageUri.startsWith("http")) {
                Picasso.get().load(Uri.parse(imageUri)).placeholder(R.drawable.ic_user_icon_black).into(holder.img);
            } else {
                Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + imageUri)).placeholder(R.drawable.ic_user_icon_black).into(holder.img);
            }
        } else {
            holder.img.setImageResource(R.drawable.ic_user_icon_black);
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
