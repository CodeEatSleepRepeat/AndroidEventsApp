package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserDetailActivity extends AppCompatActivity {

    private String username;
    private String user_profile_picture_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        //setting username
        String intentExtraUsername = getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_NAME);
        if(intentExtraUsername != null){
            TextView textUsername = findViewById(R.id.text_user_username_user_detail);
            TextView textUserEvents = findViewById(R.id.text_user_events_user_detail);
            textUsername.setText(intentExtraUsername);
            textUserEvents.setText(intentExtraUsername+ "'s Events");
            getSupportActionBar().setTitle(intentExtraUsername);
        }

        //setting user email
        String userEmail = getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_EMAIL);
        if(userEmail != null){
            TextView textUserEmail = findViewById(R.id.text_user_email_user_detail);
            textUserEmail.setText(userEmail);
        }

        //setting user image
        String intentExtraProfilePicUrl =
                getIntent().getStringExtra(UserSimpleItem.EXTRA_USER_IMAGE_PATH);
        if(intentExtraProfilePicUrl != null){
            ImageView userProfilePicUrl = findViewById(R.id.image_user_user_detail);
            Picasso.get().load(intentExtraProfilePicUrl).error(R.drawable.ic_veljko).placeholder(R.drawable.ic_friends).into(userProfilePicUrl);
        }

        //adding listener to imageMessage...
        ImageView imageMessage = findViewById(R.id.image_message_user_detail);
        imageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatLog();
            }
        });

        fillUserEvents();
    }

    private void goToChatLog(){
        Intent intent = new Intent(this, ChatLogActivity.class);
        startActivity(intent);
    }

    private void fillUserEvents(){
        ArrayList<Event> items = TestMockup.getInstance().events;

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EventListRecyclerView(items, this, R.layout.event_list_row));
    }
}
