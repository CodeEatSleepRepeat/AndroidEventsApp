package rs.ac.uns.ftn.eventsapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.Date;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.models.EventType;


public class ShowEventDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ShowEventDetailsAct";

    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //load image from file system just for testing
        String uri = "content://media/external/downloads/24";

        ImageView imageView = findViewById(R.id.image_user_user_detail);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(uri).into(imageView);
        Log.d(TAG, "setViewPic2: " + imageView.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get all elements that are going to contain event information
        collapsingToolbar = findViewById(R.id.collapsing_toolbar_user_detail);

        setView(getIntent().getExtras());
    }

    private void setView(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            Long id = (Long) savedInstanceState.get("id");
            String name = (String) savedInstanceState.get("name");
            String description = (String) savedInstanceState.get("description");
            String imageURI = (String) savedInstanceState.get("imageURI");
            EventType type = (EventType) savedInstanceState.get("type");
            Boolean open = (Boolean) savedInstanceState.get("open");
            Date start = (Date) savedInstanceState.get("start");
            Date end = (Date) savedInstanceState.get("end");
            String location = (String) savedInstanceState.get("location");
            Long longitude = (Long) savedInstanceState.get("longitude");
            Long latitude = (Long) savedInstanceState.get("latitude");
            Long userId = (Long) savedInstanceState.get("userId");




            collapsingToolbar.setTitle(name);
/*
            //TODO: ovo treba da ide u dobavljac sadrzaja (u posebnu nit), a ne ovako
            ImageView imageView = findViewById(R.id.imageViewasdf);
            if (imageView == null) {
                Log.d(TAG, "setView: Image view was null");
                imageView = new ImageView(this);
            }

            Log.d(imageView.toString(), "setView1: " + imageURI);


            Picasso.with(this).setLoggingEnabled(true);
            Picasso.with(this).load(uri).into(imageView);
            Log.d(TAG, "setView2: " + imageView.toString());
*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_drawer_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
