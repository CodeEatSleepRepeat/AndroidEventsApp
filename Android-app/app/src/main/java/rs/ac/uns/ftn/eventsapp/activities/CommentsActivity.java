package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.CommentsArrayAdapter;
import rs.ac.uns.ftn.eventsapp.models.Comment;
import rs.ac.uns.ftn.eventsapp.models.User;

public class CommentsActivity extends AppCompatActivity {

    ArrayList<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ListView listView = findViewById(R.id.commentsListView);

        User u1 = new User(1l, "Borko Bakic", "baki9@example.com", "sdcsdc84sSA", "SD58s4dSD8s87sdfSf4sdf87sdf48SDFsd178");
        User u2 = new User(2l, "Martin Kovac", "kovac.m@example.com", "sdcsdcs8794", "1s4asSDF894FSDSDF478sd48fsdfsdf8s74ssd");
        User u3 = new User(3l, "Aleksej Stralic", "ftnstudent@example.com", "sdf984sd54", "aiushdASDOF#EP#EKC#K$Rc#R$cCWepkcweC44c");
        Comment c1 = new Comment(u1, "Bas je dobar dogadjaj, jedva cekam da odem!!!");
        Comment c2 = new Comment(u2, "WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOW");
        Comment c3 = new Comment(u3, "oh my god, I am never going to financially recover from this.");
        Comment c4 = new Comment(u1, "Bas je dobar dogadjaj, jedva cekam da odem!!!");
        Comment c5 = new Comment(u2, "WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOW");
        Comment c6 = new Comment(u3, "oh my god, I am never going to financially recover from this.");
        Comment c7 = new Comment(u1, "Bas je dobar dogadjaj, jedva cekam da odem!!!");
        Comment c8 = new Comment(u2, "WOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOW");
        Comment c9 = new Comment(u3, "oh my god, I am never going to financially recover from this.");

        comments.add(c1);
        comments.add(c2);
        comments.add(c3);
        comments.add(c4);
        comments.add(c5);
        comments.add(c6);
        comments.add(c7);
        comments.add(c8);
        comments.add(c9);

        CommentsArrayAdapter adapter = new CommentsArrayAdapter(this, R.layout.adapter_comments_activity, comments);
        listView.setAdapter(adapter);
    }
}
