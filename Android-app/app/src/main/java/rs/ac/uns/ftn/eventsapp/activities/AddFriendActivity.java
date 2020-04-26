package rs.ac.uns.ftn.eventsapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;

import java.util.List;
import java.util.Objects;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.TestMockup;
import rs.ac.uns.ftn.eventsapp.views.UserSimpleItem;

public class AddFriendActivity extends AppCompatActivity {

    GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_add_friend);

        Button btnSearchUsers = findViewById(R.id.btn_search_add_friend);
        btnSearchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });
    }

    private void searchUsers() {
        EditText searchInput = findViewById(R.id.editText_username_search_add_friend);
        // dalje bi tekst odavde uzeli i ispisali usere.. ovako stavljamo mock objekte...
        RecyclerView recyclerViewFoundUsers = findViewById(R.id.recycler_view_list_user_add_friend);
        adapter = new GroupAdapter<>();
        List<User> foundUsers = TestMockup.users;
        for(User user : foundUsers){
            adapter.add(new UserSimpleItem(user, true));
        }

        recyclerViewFoundUsers.setAdapter(adapter);
    }
}
