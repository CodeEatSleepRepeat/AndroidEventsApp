package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.adapters.EventDetailsSimilarEventsRecyclerView;
import rs.ac.uns.ftn.eventsapp.adapters.EventDetailsUserRecyclerView;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.apiCalls.InvitationAppApi;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.dtos.GoingInterestedEventsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.InvitationDTO;
import rs.ac.uns.ftn.eventsapp.dtos.RequestEventDetailsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.ResponseEventDetailsDTO;
import rs.ac.uns.ftn.eventsapp.dtos.firebase.FirebaseUserDTO;
import rs.ac.uns.ftn.eventsapp.firebase.notification.APIFirebaseNotificationService;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Client;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Token;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Data;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.NotificationTypeEnum;
import rs.ac.uns.ftn.eventsapp.firebase.notification.message.Sender;
import rs.ac.uns.ftn.eventsapp.models.GoingInterestedStatus;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ZonedGsonBuilder;


public class EventDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ShowEventDetailsAct";
    private static final int LAUNCH_SEND_INVITATIONS_ACTIVITY = 2001;
    private static Long idEvent;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MMM yyy HH:mm z");

    private CollapsingToolbarLayout collapsingToolbar;
    private EventDTO dto;
    private Long ownerId;

    private ResponseEventDetailsDTO res;
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
    private ImageView authorImg;
    private TextView authorName;
    private TextView organizedEvents;
    private TextView authorInfo;
    private Button goingBtn;
    private Button interestedBtn;
    private Query profileImageUrlQuery;


    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //mMapView = findViewById(R.id.mapViewEventDetails);
        //initGoogleMap(savedInstanceState);
        //get all elements that are going to contain event information
        authorImg = findViewById(R.id.authorEventDetailsImgView);
        authorName = findViewById(R.id.userNameEventDetailsTextView);
        authorInfo = findViewById(R.id.userInfoEventDetailsTextView);
        organizedEvents = findViewById(R.id.organizedEventsEventDetailsTextView);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar_user_detail);

        dto = (EventDTO) getIntent().getSerializableExtra("EVENT");
        getDetails();
        setView(dto);

    }

    private void setView(final EventDTO dto) {

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

        if (AppDataSingleton.getInstance().getLoggedUser()==null || AppDataSingleton.getInstance().getLoggedUser().getId().equals(dto.getOwner())) {
            goingBtn.setVisibility(View.INVISIBLE);
            interestedBtn.setVisibility(View.INVISIBLE);
        }

        idEvent = dto.getId();
        collapsingToolbar.setTitle(dto.getName());
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT); //added to hide title over image

        Picasso.get().setLoggingEnabled(true);
        if (dto.getImageUri() == null || dto.getImageUri().equals("")) {
            Picasso.get().load(R.drawable.ic_missing_event_icon_white).placeholder(R.drawable.ic_missing_event_icon_white).into(imageView); //for picasso to not crash if image is empty or null
        } else {
            Picasso.get().load(dto.getImageUri()).placeholder(R.drawable.ic_missing_event_icon_white).into(imageView);
        }
        imageView.setAlpha(0.9f);

        eventNameEventDetailsTextView.setText(dto.getName());
        eventStartEventDetailsView.setText(formatter.format(dto.getStart_time()));
        eventDescriptionEventDetailsView.setText(dto.getDescription());
        eventLocationEventDetailsTextView.setText(dto.getPlace());
        final EventForMapDTO mapDto = new EventForMapDTO(dto.getId(), dto.getName(), dto.getLatitude(), dto.getLongitude(), dto.getImageUri());
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
                intent2.putExtra("EventId", idEvent.toString());
                startActivity(intent2);
            }
        });

        final Intent intent3 = new Intent(this, GoingToEventActivity.class);
        seeAllGoingEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent3.putExtra("eventId", idEvent.toString());
                startActivity(intent3);
            }
        });

        final Intent intent4 = new Intent(this, InterestedInEventActivity.class);
        seeAllInterestedEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent4.putExtra("eventId", idEvent.toString());
                startActivity(intent4);
            }
        });

        final Intent intent5 = new Intent(this, AuthorsEventsActivity.class);
        seeAllAuthorsEventsEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OWNER", ownerId.toString());
                intent5.putExtra("ownerId", ownerId.toString());
                startActivity(intent5);
            }
        });


        final Intent intent6 = new Intent(this, SimilarEventsActivity.class);
        seeAllSimilarPostsEventDetailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ID", idEvent.toString());
                intent6.putExtra("eventId", idEvent.toString());
                startActivity(intent6);
            }
        });

        goingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goingBtn.getText().equals(getString(R.string.notGoing))) {
                    removeFromGoing();
                } else {
                    goingToEvent();
                }
            }
        });

        interestedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interestedBtn.getText().equals(getString(R.string.notInterested))){
                    removeFromInterested();
                } else {
                    interestedInEvent();
                }
            }
        });

        if (AppDataSingleton.getInstance().isUserGoingTo(dto.getId())) {
            //korisnik vec ide na ovaj event
            goingBtn.setText(R.string.notGoing);
        }
        if (AppDataSingleton.getInstance().isUserInterestedTo(dto.getId())) {
            //korisnik vec ide na ovaj event
            interestedBtn.setText(R.string.notInterested);
        }

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

    public void goingToEvent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.goingToEvent(dto.getId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "You are already going to this event", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", response.body().getId().toString());
                    AppDataSingleton.getInstance().deleteInterestedEventPhysical(dto.getId());
                    AppDataSingleton.getInstance().addGIEvent(new GoingInterestedEventsDTO(dto, GoingInterestedStatus.GOING));
                    goingBtn.setText(R.string.notGoing);
                    interestedBtn.setText(R.string.nav_item_interested);
                    Toast.makeText(getApplicationContext(), "Added to Going Events!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void interestedInEvent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.interestedInEvent(dto.getId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "You are already interested in this event", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", response.body().getId().toString());
                    AppDataSingleton.getInstance().deleteGoingEventPhysical(dto.getId());
                    AppDataSingleton.getInstance().addGIEvent(new GoingInterestedEventsDTO(dto, GoingInterestedStatus.INTERESTED));
                    interestedBtn.setText(R.string.notInterested);
                    goingBtn.setText(R.string.nav_item_going);
                    Toast.makeText(getApplicationContext(), "Added to Interested Events!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeFromInterested() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.removeInterestedEvent(dto.getId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Event not found", Toast.LENGTH_LONG).show();
                } else {
                    AppDataSingleton.getInstance().deleteInterestedEventPhysical(dto.getId());
                    Toast.makeText(getApplicationContext(), "Removed from interested!", Toast.LENGTH_LONG).show();
                    interestedBtn.setText(R.string.nav_item_interested);
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void removeFromGoing() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.removeGoingEvent(dto.getId(), AppDataSingleton.getInstance().getLoggedUser().getId());
        s.enqueue(new retrofit2.Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Event not found", Toast.LENGTH_LONG).show();
                } else {
                    AppDataSingleton.getInstance().deleteGoingEventPhysical(dto.getId());
                    Toast.makeText(getApplicationContext(), "Removed from going!", Toast.LENGTH_LONG).show();
                    goingBtn.setText(R.string.nav_item_going);
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
                List<Long> userIds = (ArrayList<Long>) data.getSerializableExtra(
                        "SHARED_FRIENDS_IDS");
                List<String> userEmails = data.getStringArrayListExtra("SHARED_FRIENDS_EMAILS");

                for(int i=0; i<userIds.size(); i++){
                    //sendInvitation
                    sendEventInvitation(userIds.get(i), userEmails.get(i));
                }


            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                //user je kliknuo na nesto drugo

            }
        }
    }

    private void sendEventInvitation(final Long userId, final String userEmail) {
        User loggedUser = AppDataSingleton.getInstance().getLoggedUser();

        InvitationAppApi invitationApi = getInvitationApi();
        Call<InvitationDTO> invitationCall = invitationApi.sendInvitation(loggedUser.getId(),
                userId, idEvent);

        invitationCall.enqueue(new Callback<InvitationDTO>() {
            @Override
            public void onResponse(Call<InvitationDTO> call, retrofit2.Response<InvitationDTO> response) {
                if (response.isSuccessful()){
                    //sendInvitationNotification(userEmail);
                    InvitationDTO createdInvitation = response.body();
                    findFirebaseReceiverUserThenSendNotification(createdInvitation.getReciever().getImgUri());
                }
            }

            @Override
            public void onFailure(Call<InvitationDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed,
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getDetails(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Long userId = null;
        if(AppDataSingleton.getInstance().getLoggedUser()!=null){
            userId = AppDataSingleton.getInstance().getLoggedUser().getId();
        }
        Call<ResponseEventDetailsDTO> s = e.getDetails(new RequestEventDetailsDTO(userId, dto.getId()));
        s.enqueue(new Callback<ResponseEventDetailsDTO>() {
            @Override
            public void onResponse(Call<ResponseEventDetailsDTO> call, Response<ResponseEventDetailsDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                    return;
                }
                res = response.body();
                String imageUri = res.getUserImage();
                if (!imageUri.equals("")) {
                    Picasso.get().setLoggingEnabled(true);
                    if (imageUri.startsWith("http")) {
                        Picasso.get().load(Uri.parse(imageUri)).placeholder(R.drawable.ic_user_icon_black).into(authorImg);
                    } else {
                        Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + imageUri)).placeholder(R.drawable.ic_user_icon_black).into(authorImg);
                    }
                } else {
                    authorImg.setImageResource(R.drawable.ic_user_icon_black);
                }
                ownerId = res.getUserId();
                authorName.setText(res.getUserName());
                authorInfo.setText(R.string.eventOrganizer);
                organizedEvents.setText(res.getOrganizedEventsNum() + " " + getString(R.string.eventsOrganized));
                initGoingRV(res);
                initInterestedRV(res);
                initSimilarRV(res);
            }

            @Override
            public void onFailure(Call<ResponseEventDetailsDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initGoingRV(ResponseEventDetailsDTO res){
        if(!res.getGoingImages().isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView rv = findViewById(R.id.goingToEventRV);
            rv.setLayoutManager(layoutManager);
            EventDetailsUserRecyclerView adapter = new EventDetailsUserRecyclerView(this, res.getGoingImages());
            rv.setAdapter(adapter);
        }else{
            seeAllGoingEventDetailsTextView.setText("No one");
            seeAllGoingEventDetailsTextView.setOnClickListener(null);
        }
    }

    private void initInterestedRV(ResponseEventDetailsDTO res){
        if(!res.getInterestedImages().isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView rv = findViewById(R.id.interestedInEventRV);
            rv.setLayoutManager(layoutManager);
            EventDetailsUserRecyclerView adapter = new EventDetailsUserRecyclerView(this, res.getInterestedImages());
            rv.setAdapter(adapter);
        }else{
            seeAllInterestedEventDetailsTextView.setText("No one");
            seeAllInterestedEventDetailsTextView.setOnClickListener(null);
        }
    }

    private void initSimilarRV(ResponseEventDetailsDTO res){
        if(!res.getEvents().isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView rv = findViewById(R.id.similarEventsRV);
            rv.setLayoutManager(layoutManager);
            EventDetailsSimilarEventsRecyclerView adapter = new EventDetailsSimilarEventsRecyclerView(this, res.getEvents());
            rv.setAdapter(adapter);
        }else{
            seeAllSimilarPostsEventDetailsTextView.setText("No similar events");
            seeAllSimilarPostsEventDetailsTextView.setOnClickListener(null);
        }
    }

    private void findFirebaseReceiverUserThenSendNotification(String invitedUserImageUri) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        profileImageUrlQuery = usersRef.orderByChild("profileImageUrl").equalTo(invitedUserImageUri);
        profileImageUrlQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //sendDataAndUnregister(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }

            private void sendDataAndUnregister(DataSnapshot dataSnapshot) {
                profileImageUrlQuery.removeEventListener(this);
                FirebaseUserDTO foundRequestReciever = dataSnapshot.getValue(FirebaseUserDTO.class);

                APIFirebaseNotificationService apiFirebaseService =
                        Client.getRetrofit("https://fcm.googleapis.com/").create(APIFirebaseNotificationService .class);

                sendEventInvitationNotification(apiFirebaseService, foundRequestReciever.getUid());
            }
        });

    }

    private void sendEventInvitationNotification(final APIFirebaseNotificationService apiFirebaseService, final String toId) {
        final String loggedUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(toId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token chatPartnerToken = ds.getValue(Token.class);
                    String notificationBody =
                            getResources().getString(R.string.event_invitation_notification);
                    String notificationTitle =
                            getResources().getString(R.string.invitations);
                    Data data = new Data(loggedUserUid, notificationBody, notificationTitle,
                            toId, R.drawable.logo, NotificationTypeEnum.INVITATION);

                    assert chatPartnerToken != null;
                    Sender sender = new Sender(data, chatPartnerToken.getToken());
                    apiFirebaseService.sendNotification(sender)
                            .enqueue(new Callback<rs.ac.uns.ftn.eventsapp.firebase.notification.Response>() {
                                @Override
                                public void onResponse(Call<rs.ac.uns.ftn.eventsapp.firebase.notification.Response> call, retrofit2.Response<rs.ac.uns.ftn.eventsapp.firebase.notification.Response> response) {

                                }

                                @Override
                                public void onFailure(Call<rs.ac.uns.ftn.eventsapp.firebase.notification.Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private InvitationAppApi getInvitationApi() {
        InvitationAppApi invitationApi;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.localhost_uri))
                .addConverterFactory(ZonedGsonBuilder.getZonedGsonFactory())
                .build();
        invitationApi = retrofit.create(InvitationAppApi.class);
        return invitationApi;
    }
}
