package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDetailsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;


public class EventDetailsActivity extends AppCompatActivity {

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
    private TextView seeCommentsEventDetailsTextView;
    private TextView seeAllGoingEventDetailsTextView;
    private TextView seeAllInterestedEventDetailsTextView;
    private TextView seeAllAuthorsEventsEventDetailsTextView;
    private TextView seeAllSimilarPostsEventDetailsTextView;
    private Button goingBtn;
    private Button interestedBtn;

    @Override
    protected void onPostResume() {
        super.onPostResume();

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

    private void setView(EventDetailsDTO dto) {

        imageView = findViewById(R.id.image_user_user_detail);
        eventNameEventDetailsTextView = findViewById(R.id.eventNameEventDetailsTextView);
        eventStartEventDetailsView = findViewById(R.id.eventStartEventDetailsTextView);
        eventDescriptionEventDetailsView = findViewById(R.id.eventDescriptionEventDetailsTextView);
        eventLocationEventDetailsTextView = findViewById(R.id.eventLocationEventDetailsTextView);
        seeOnMapEventDetailsTextView = findViewById(R.id.seeOnMapEventDetailsTextView);
        seeCommentsEventDetailsTextView = findViewById(R.id.seeCommentsEventDetailsTextView);
        seeAllGoingEventDetailsTextView = findViewById(R.id.seeAllGoingEventDetailsTextView);
        seeAllInterestedEventDetailsTextView = findViewById(R.id.seeAllInterestedEventDetailsTextView);
        seeAllAuthorsEventsEventDetailsTextView = findViewById(R.id.seeAllOrganizedEventsEventDetailsTextView);
        seeAllSimilarPostsEventDetailsTextView = findViewById(R.id.seeAllSimilarPostsEventDetailsTextView);
        goingBtn = findViewById(R.id.goingEventDetailsBtn);
        interestedBtn = findViewById(R.id.interestedEventDetailsBtn);

        if(AppDataSingleton.getInstance().getLoggedUser().getId().equals(dto.getAuthor())){
            goingBtn.setVisibility(View.INVISIBLE);
            interestedBtn.setVisibility(View.INVISIBLE);
        }

        idEvent = dto.getEventId();
        collapsingToolbar.setTitle(dto.getEventName());
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(dto.getEventImageURI()).placeholder(R.drawable.ic_missing_event_icon_white).into(imageView, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                int dominantColor = getDominantColor(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                collapsingToolbar.setExpandedTitleColor(dominantColor);

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                collapsingToolbar.setExpandedTitleColor(Color.BLACK);
            }
        });

        imageView.setAlpha(0.9f);

        eventNameEventDetailsTextView.setText(dto.getEventName());
        eventStartEventDetailsView.setText(dto.getStartTime() + "");
        eventDescriptionEventDetailsView.setText(dto.getEventDescription());
        eventLocationEventDetailsTextView.setText(dto.getLocation());
        final EventForMapDTO mapDto = new EventForMapDTO(dto.getEventId(), dto.getEventName(), dto.getLongitude(), dto.getLatitude(), dto.getEventImageURI());
        final Intent intent = new Intent(this, GoogleMapActivity.class);
        seeOnMapEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<EventForMapDTO> list = new ArrayList<>();
                list.add(mapDto);
                intent.putExtra("EVENTS", list);
                startActivity(intent);
            }
        });

        final Intent intent2 = new Intent(this, CommentsActivity.class);
        seeCommentsEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });

        final Intent intent3 = new Intent(this, GoingToEventActivity.class);
        seeAllGoingEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent3);
            }
        });

        final Intent intent4 = new Intent(this, InterestedInEventActivity.class);
        seeAllInterestedEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent4);
            }
        });

        final Intent intent5 = new Intent(this, AuthorsEventsActivity.class);
        seeAllAuthorsEventsEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent5);
            }
        });

        final Intent intent6 = new Intent(this, SimilarEventsActivity.class);
        seeAllSimilarPostsEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent6);
            }
        });

        goingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goingToEvent();
            }
        });

        interestedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestedInEvent();
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

    public static int getDominantColor(Bitmap bitmap) {
        try {
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
            final int color = newBitmap.getPixel(0, 0);
            newBitmap.recycle();
            double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
            return y >= 128 ? Color.BLACK : Color.WHITE;
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

    public void goingToEvent(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.goingToEvent(dto.getEventId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                }
                Log.d("TAG", response.body().getId().toString());
                Toast.makeText(getApplicationContext(), "Added to Going Events!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void interestedInEvent(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.interestedInEvent(dto.getEventId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code()!=200) {
                    Toast.makeText(getApplicationContext(), "You are already interested in this event", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("TAG", response.body().getId().toString());
                    Toast.makeText(getApplicationContext(), "Added to Interested Events!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
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
