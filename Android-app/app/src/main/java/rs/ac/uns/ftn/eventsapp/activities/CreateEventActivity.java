package rs.ac.uns.ftn.eventsapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rs.ac.uns.ftn.eventsapp.R;

public class CreateEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY="MapViewBundleKey";

    final Calendar calendar = Calendar.getInstance();
    private EditText startingDateEditText;
    private EditText startingTimeEditText;
    private EditText endingDateEditText;
    private EditText endingTimeEditText;

    private CheckBox charityCB;
    private CheckBox educationalCB;
    private CheckBox talksCB;
    private CheckBox musicCB;
    private CheckBox partyCB;
    private CheckBox sportsCB;

    private static final int GALLERY_REQUEST=123;
    private ImageView imgView;
    private ImageView clearImage;
    private FrameLayout imageHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
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
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
                endingDateEditText.setText(sdf.format(calendar.getTime()));
            }
        };

        /*
         * Listener koji se odnosi na time picker
         * Postavlja izabrano zavrsno vreme u edit box
         * */
        final TimePickerDialog.OnTimeSetListener endingTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
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
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        endingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventActivity.this, endingDate,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                imgView.setImageURI(null);
            }
        });
    }

    /*
    * Rukuje se rezultatom gore poslatog intenta
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageData = data.getData();
            findViewById(R.id.cameraImageView).setVisibility(View.INVISIBLE);
            findViewById(R.id.addPhotoTextView).setVisibility(View.INVISIBLE);
            imgView.setImageURI(imageData);
            clearImage.bringToFront();
        }
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.createEventMapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions mo = new MarkerOptions();
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
        if(mapViewBundle==null){
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
