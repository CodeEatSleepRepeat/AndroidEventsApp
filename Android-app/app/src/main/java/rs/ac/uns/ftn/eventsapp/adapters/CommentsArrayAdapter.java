package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.CommentDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.models.Comment;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class CommentsArrayAdapter extends RecyclerView.Adapter<CommentsArrayAdapter.ViewHolder>{

    private List<CommentDTO> mItems;
    private Context context;

    public CommentsArrayAdapter(List<CommentDTO> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comments_activity, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentDTO dto = mItems.get(position);
        String imageUri = dto.getImgUri();
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

        holder.comment.setText(dto.getText());
        holder.name.setText(dto.getImePrezime());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name;
        TextView comment;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.commentsActivityUserImg);
            name = itemView.findViewById(R.id.userNameCommentsActivityTextView);
            comment = itemView.findViewById(R.id.commentCommentsActivityTextView);
            parentLayout = itemView.findViewById(R.id.parent_comment_layout);
        }
    }


}
