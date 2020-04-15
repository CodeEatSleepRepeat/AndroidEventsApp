package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventListRecyclerView;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class UserDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

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
        recyclerView.setAdapter(new EventListRecyclerView(items, this));
    }
}
