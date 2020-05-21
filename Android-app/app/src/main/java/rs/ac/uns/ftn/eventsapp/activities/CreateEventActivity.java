package rs.ac.uns.ftn.eventsapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.apiCalls.EventsAppAPI;
import rs.ac.uns.ftn.eventsapp.dtos.CreateEventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.OwnerCreateEventDTO;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.Owner;
import rs.ac.uns.ftn.eventsapp.models.User;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;

public class CreateEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int GALLERY_REQUEST = 123;
    final Calendar calendar = Calendar.getInstance();
    final Calendar calendar2 = Calendar.getInstance();
    private Button createButton;
    private MapView mMapView;
    private EditText startingDateEditText;
    private EditText startingTimeEditText;
    private EditText endingDateEditText;
    private EditText endingTimeEditText;
    private EditText placeEditText;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Double lat = null;
    private Double lng = null;
    private String startDateTime = null;
    private String endDateTime = null;
    private EventType eventType;
    private FacebookPrivacy facebookPrivacy = FacebookPrivacy.PUBLIC;
    private String imgUri = null;
    private CheckBox charityCB;
    private CheckBox educationalCB;
    private CheckBox talksCB;
    private CheckBox musicCB;
    private CheckBox partyCB;
    private CheckBox sportsCB;
    private CheckBox privateCB;
    private ImageView imgView;
    private ImageView clearImage;
    private FrameLayout imageHolder;
    private Retrofit retrofit;
    private Bitmap bitmap;
    private MediaType mediaType;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        createButton = findViewById(R.id.createEventBtn);
        placeEditText = findViewById(R.id.placeCreateEventEditText);
        nameEditText = findViewById(R.id.eventNameEditText);
        descriptionEditText = findViewById(R.id.eventDescriptionEditText);
        privateCB = findViewById(R.id.privateEventCreateEventCB);

        Toolbar toolbar = findViewById(R.id.toolbarCreateEvent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMapView = findViewById(R.id.createEventMapView);
        if (savedInstanceState != null) {
            lat = savedInstanceState.getDouble("lat");
            lng = savedInstanceState.getDouble("lng");
            imgUri = savedInstanceState.getString("uri");
        }
        initGoogleMap(savedInstanceState);

        startingDateEditText = findViewById(R.id.startingDateCreateEventEditText);
        startingTimeEditText = findViewById(R.id.startingTimeCreateEventEditText);
        endingDateEditText = findViewById(R.id.endingDateCreateEventEditText);
        endingTimeEditText = findViewById(R.id.endingTimeCreateEventEditText);

        charityCB = findViewById(R.id.charityCreateEventRB);
        educationalCB = findViewById(R.id.educationalCreateEventRB);
        talksCB = findViewById(R.id.talksCreateEventRB);
        musicCB = findViewById(R.id.musicCreateEventRB);
        partyCB = findViewById(R.id.partyCreateEventRB);
        sportsCB = findViewById(R.id.sportsCreateEventRB);

        charityCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationalCB.setChecked(false);
                talksCB.setChecked(false);
                musicCB.setChecked(false);
                partyCB.setChecked(false);
                sportsCB.setChecked(false);
            }
        });
        educationalCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charityCB.setChecked(false);
                talksCB.setChecked(false);
                musicCB.setChecked(false);
                partyCB.setChecked(false);
                sportsCB.setChecked(false);
            }
        });
        talksCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationalCB.setChecked(false);
                charityCB.setChecked(false);
                musicCB.setChecked(false);
                partyCB.setChecked(false);
                sportsCB.setChecked(false);
            }
        });
        musicCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationalCB.setChecked(false);
                talksCB.setChecked(false);
                charityCB.setChecked(false);
                partyCB.setChecked(false);
                sportsCB.setChecked(false);
            }
        });
        partyCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationalCB.setChecked(false);
                talksCB.setChecked(false);
                musicCB.setChecked(false);
                charityCB.setChecked(false);
                sportsCB.setChecked(false);
            }
        });
        sportsCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationalCB.setChecked(false);
                talksCB.setChecked(false);
                musicCB.setChecked(false);
                partyCB.setChecked(false);
                charityCB.setChecked(false);
            }
        });

        /*
         * Listener koji se odnosi na kalendar
         * Postavlja izabrani pocetni datum u edit text
         * */
        final DatePickerDialog.OnDateSetListener startingDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
                startingDateEditText.setError(null);
                startingDateEditText.setText(sdf.format(calendar.getTime()));
            }
        };

        /*
         * Listener koji se odnosi na time picker
         * Postavlja izabrano pocetno vreme u edit box
         * */
        final TimePickerDialog.OnTimeSetListener startingTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startingTimeEditText.setError(null);
                if (minute < 10) {
                    startingTimeEditText.setText(hourOfDay + ":0" + minute);
                } else {
                    startingTimeEditText.setText(hourOfDay + ":" + minute);
                }
            }
        };

        startingTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateEventActivity.this, startingTime,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        startingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, startingDate,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /*
         * Listener koji se odnosi na kalendar
         * Postavlja izabrani zavrsni datum u edit text
         * */
        final DatePickerDialog.OnDateSetListener endingDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar2.set(Calendar.YEAR, year);
                calendar2.set(Calendar.MONTH, month);
                calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
                endingDateEditText.setError(null);
                endingDateEditText.setText(sdf.format(calendar2.getTime()));
            }
        };

        /*
         * Listener koji se odnosi na time picker
         * Postavlja izabrano zavrsno vreme u edit box
         * */
        final TimePickerDialog.OnTimeSetListener endingTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar2.set(Calendar.MINUTE, minute);
                endingTimeEditText.setError(null);
                if (minute < 10) {
                    endingTimeEditText.setText(hourOfDay + ":0" + minute);
                } else {
                    endingTimeEditText.setText(hourOfDay + ":" + minute);
                }
            }
        };

        endingTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateEventActivity.this, endingTime,
                        calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), true).show();
            }
        });

        endingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, endingDate,
                        calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        /*
         * Klikom na sliku se salje intent(zahtev) za izbor slike
         * */
        imgView = findViewById(R.id.eventImageCreateEventImgView);

        imageHolder = findViewById(R.id.imageHolderCreateEvent);
        imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select an image"), GALLERY_REQUEST);
            }
        });

        clearImage = findViewById(R.id.clearImageCreateEvent);
        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.cameraImageView).setVisibility(View.VISIBLE);
                findViewById(R.id.addPhotoTextView).setVisibility(View.VISIBLE);
                imgUri = null;
                bitmap = null;
                mediaType = null;
                imgView.setImageURI(null);
            }
        });

        if (savedInstanceState != null) {
            String s = savedInstanceState.getString("uri");
            if (s != null) {
                imgView.setImageURI(Uri.parse(s));
                findViewById(R.id.cameraImageView).setVisibility(View.INVISIBLE);
                findViewById(R.id.addPhotoTextView).setVisibility(View.INVISIBLE);
            }
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationSuccess()) {
                    sendRequest();
                }
            }
        });
    }

    private Boolean validationSuccess() {
        if ((lat == null && lng == null) || (lat == 0.0 && lng == 0.0)) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation1, Toast.LENGTH_LONG).show();
            return false;
        }
        if (placeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation2, Toast.LENGTH_LONG).show();
            placeEditText.setError("This field cannot be blank.");
            return false;
        }
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation3, Toast.LENGTH_LONG).show();
            nameEditText.setError("This field cannot be blank.");
            return false;
        }
        if (descriptionEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation4, Toast.LENGTH_LONG).show();
            descriptionEditText.setError("This field cannot be blank.");
            return false;
        }
        if (!charityCB.isChecked() && !educationalCB.isChecked() && !musicCB.isChecked() && !partyCB.isChecked()
                && !sportsCB.isChecked() && !talksCB.isChecked()) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation5, Toast.LENGTH_LONG).show();
            return false;
        }
        if (startingDateEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation6, Toast.LENGTH_LONG).show();
            startingDateEditText.setError("This field cannot be blank.");
            return false;
        }
        if (startingTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation7, Toast.LENGTH_LONG).show();
            startingTimeEditText.setError("This field cannot be blank.");
            return false;
        }
        if (endingDateEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation8, Toast.LENGTH_LONG).show();
            endingDateEditText.setError("This field cannot be blank.");
            return false;
        }
        if (endingTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation9, Toast.LENGTH_LONG).show();
            endingTimeEditText.setError("This field cannot be blank.");
            return false;
        }
        if (calendar.getTimeInMillis() >= calendar2.getTimeInMillis()) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation10, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void sendRequest() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        startDateTime = sdf.format(calendar.getTime());
        endDateTime = sdf.format(calendar2.getTime());
        if (charityCB.isChecked()) {
            eventType = EventType.CHARITY;
        } else if (educationalCB.isChecked()) {
            eventType = EventType.EDUCATIONAL;
        } else if (talksCB.isChecked()) {
            eventType = EventType.TALKS;
        } else if (musicCB.isChecked()) {
            eventType = EventType.MUSIC;
        } else if (partyCB.isChecked()) {
            eventType = EventType.PARTY;
        } else if (sportsCB.isChecked()) {
            eventType = EventType.SPORTS;
        }

        if (privateCB.isChecked()) {
            facebookPrivacy = FacebookPrivacy.PRIVATE;
        }

        OwnerCreateEventDTO owner = new OwnerCreateEventDTO();
        User user = AppDataSingleton.getInstance().getLoggedUser();
        owner.setUserId(user.getId());
        if(user.getFacebookId()!=null) {
            owner.setFacebookId(user.getFacebookId());
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EventsAppAPI e = retrofit.create(EventsAppAPI.class);
        Call<EventDTO> s = e.createEvent(new CreateEventDTO(lat, lng, nameEditText.getText().toString(), placeEditText.getText().toString(),
                descriptionEditText.getText().toString(), eventType, startDateTime,
                endDateTime, facebookPrivacy, owner));
        s.enqueue(new Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                }
                Log.d("TAG", response.body().getId().toString());
                if(imgUri!=null) {
                    try {

                        uploadImage(response.body().getId());
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage(Long id) throws URISyntaxException {
        if(bitmap!=null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            EventsAppAPI e = retrofit.create(EventsAppAPI.class);
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            builder.addFormDataPart("image", "image", RequestBody.create(mediaType, bos.toByteArray()));
            RequestBody requestBody = builder.build();
            Call<EventDTO> s = e.uploadEventImg(requestBody, id);
            s.enqueue(new Callback<EventDTO>() {
                @Override
                public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.code() + " " + response.body(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(), R.string.eventCreated, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<EventDTO> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(imageData);
                bitmap = BitmapFactory.decodeStream(is);
                mediaType = MediaType.parse(getContentResolver().getType(imageData));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imgUri = imageData.getPath();
            findViewById(R.id.cameraImageView).setVisibility(View.INVISIBLE);
            findViewById(R.id.addPhotoTextView).setVisibility(View.INVISIBLE);
            imgView.setImageURI(imageData);
            clearImage.bringToFront();
        }
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if ((lat != null && lng != null) && (!lat.equals(0.0) && !lng.equals(0.0))) {
            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(lat, lng));
            mo.title(lat + " : " + lng);
            googleMap.addMarker(mo);
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions mo = new MarkerOptions();
                lat = latLng.latitude;
                lng = latLng.longitude;
                mo.position(latLng);
                mo.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.clear();
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                googleMap.addMarker(mo);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        if (lat != null && lng != null) {
            outState.putDouble("lat", lat);
            outState.putDouble("lng", lng);
        }
        if (imgUri != null) {
            outState.putString("uri", imgUri);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
