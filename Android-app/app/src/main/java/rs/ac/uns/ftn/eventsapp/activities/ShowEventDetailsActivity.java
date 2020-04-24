package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.dtos.EventDetailsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.models.EventType;


public class ShowEventDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ShowEventDetailsAct";
    private static final int LAUNCH_SEND_INVITATIONS_ACTIVITY = 2001;
    private static Long idEvent;

    private CollapsingToolbarLayout collapsingToolbar;
    private EventDetailsDTO dto;

    private ImageView imageView;
    private TextView eventNameEventDetailsTextView;
    private TextView eventStartEventDetailsView;
    private TextView eventDescriptionEventDetailsView;
    private TextView eventLocationEventDetailsTextView;
    private TextView seeOnMapEventDetailsTextView;

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //load image from file system just for testing
        /*String uri = "content://media/external/downloads/24";

        ImageView imageView = findViewById(R.id.image_user_user_detail);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(uri).into(imageView);
        Log.d(TAG, "setViewPic2: " + imageView.toString());*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //mMapView = findViewById(R.id.mapViewEventDetails);
        //initGoogleMap(savedInstanceState);
        //get all elements that are going to contain event information
        collapsingToolbar = findViewById(R.id.collapsing_toolbar_user_detail);

        dto = (EventDetailsDTO) getIntent().getSerializableExtra("EVENT");
        setView(dto);

    }

    /*private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.mapViewEventDetails);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MarkerOptions mo = new MarkerOptions();
        mo.position(new LatLng(dto.getLatitude(), dto.getLongitude()));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(mo);
    }*/

    private void setView(EventDetailsDTO dto) {

        imageView = findViewById(R.id.image_user_user_detail);
        eventNameEventDetailsTextView = findViewById(R.id.eventNameEventDetailsTextView);
        eventStartEventDetailsView = findViewById(R.id.eventStartEventDetailsTextView);
        eventDescriptionEventDetailsView = findViewById(R.id.eventDescriptionEventDetailsTextView);
        eventLocationEventDetailsTextView = findViewById(R.id.eventLocationEventDetailsTextView);
        seeOnMapEventDetailsTextView = findViewById(R.id.seeOnMapEventDetailsTextView);

        idEvent = dto.getEventId();
        collapsingToolbar.setTitle(dto.getEventName());
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(Uri.parse(dto.getEventImageURI())).into(imageView);
        eventNameEventDetailsTextView.setText(dto.getEventName());
        eventStartEventDetailsView.setText(dto.getStartTime() + "");
        eventDescriptionEventDetailsView.setText(dto.getEventDescription());
        eventLocationEventDetailsTextView.setText(dto.getLocation());
        final EventForMapDTO mapDto = new EventForMapDTO(dto.getEventId(), dto.getEventName(), dto.getLongitude(), dto.getLatitude(), dto.getEventImageURI());
        final Intent intent = new Intent(this, MapActivity.class);
        seeOnMapEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<EventForMapDTO> list = new ArrayList<>();
                list.add(mapDto);
                intent.putExtra("EVENTS", list);
                startActivity(intent);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_even_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.inviteEventDetails) {
            Intent intent = new Intent(this, SendInvitationsActivity.class);
            intent.putExtra("eventId", idEvent);
            startActivityForResult(intent, LAUNCH_SEND_INVITATIONS_ACTIVITY);

        } else if (id == R.id.shareEventDetails) {
            Toast.makeText(getApplicationContext(), "Share event", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.reportEventDetails) {
            Toast.makeText(getApplicationContext(), "Event reported!", Toast.LENGTH_SHORT).show();

        } else {
            //it's go home button, what else
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: ako zatreba nesto od ovog, vecina se radi u SendInvitationsActivity klasi
        if (requestCode == LAUNCH_SEND_INVITATIONS_ACTIVITY) {
            //send invitations vraca listu prijatelja, mozda
            if (resultCode == Activity.RESULT_OK) {
                //user je dodao neke prijatelje

            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                //user je kliknuo na nesto drugo

            }
        }
    }



}
