package rs.ac.uns.ftn.eventsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.Comment;
import rs.ac.uns.ftn.eventsapp.models.User;

public class CommentsArrayAdapter extends ArrayAdapter<Comment> {

    private Context mContext;
    private int mRes;

    public CommentsArrayAdapter(Context context, int resource, ArrayList<Comment> comments){
        super(context, resource, comments);
        mContext = context;
        mRes = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position).getUser();
        String comment = getItem(position).getComment();

        Comment c = new Comment(user, comment);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mRes, parent, false);

        TextView textViewUsername = convertView.findViewById(R.id.userNameCommentsActivityTextView);
        TextView textViewComment = convertView.findViewById(R.id.commentCommentsActivityTextView);

        textViewUsername.setText(c.getUser().getName());
        textViewComment.setText(c.getComment());

        return convertView;
    }
}
