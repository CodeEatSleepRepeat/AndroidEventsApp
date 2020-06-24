package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import rs.ac.uns.ftn.eventsapp.activities.UserDetailActivity;
import rs.ac.uns.ftn.eventsapp.dtos.UserDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class GoingInterestedUsersAdapter extends RecyclerView.Adapter<GoingInterestedUsersAdapter.ViewHolder>{

    private List<UserDTO> users;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUserDetails(users.get(position).getName(), users.get(position).getImgUri(), users.get(position).getEmail(), users.get(position).getId());
            }
        });
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

    private void goToUserDetails(String name, String imageUri, String email, Long userId) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(UserSimpleItem.EXTRA_USER_NAME, name);
        intent.putExtra(UserSimpleItem.EXTRA_USER_IMAGE_PATH, imageUri);
        intent.putExtra(UserSimpleItem.EXTRA_USER_EMAIL, email);
        intent.putExtra(UserSimpleItem.EXTRA_USER_ID, userId);

        mContext.startActivity(intent);
    }

}
