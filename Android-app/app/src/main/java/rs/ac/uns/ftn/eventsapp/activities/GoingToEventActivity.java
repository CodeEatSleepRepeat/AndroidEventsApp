package rs.ac.uns.ftn.eventsapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class GoingToEventActivity extends AppCompatActivity {

    private List<User> userList = new ArrayList<>();
    private List<User> userListAll = new ArrayList<>();
    private GroupAdapter adapter = new GroupAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_to_event);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_list_of_users_going);

        userList.addAll(TestMockup.users);
        userListAll.addAll(TestMockup.users);
        for(User user : userList){
            adapter.add(new UserSimpleItem(user, false, false));
        }

        recyclerView.setAdapter(adapter);
    }


}
