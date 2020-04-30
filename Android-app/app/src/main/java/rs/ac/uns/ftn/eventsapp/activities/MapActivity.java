package rs.ac.uns.ftn.eventsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import rs.ac.uns.ftn.eventsapp.MainActivity;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.R;
import rs.ac.uns.ftn.eventsapp.utils.ClusterManagerRenderer;
import rs.ac.uns.ftn.eventsapp.utils.ClusterMarker;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_ENABLE_GPS = 9002;
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private boolean mLocationPermissonGranted = false;
    private static final String MAPVIEW_BUNDLE_KEY="MapViewBundleKey";
    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final static long UPDATE_INTERVAL = 5000;
    private MapView mMapView;
    private ArrayList<EventForMapDTO> events = new ArrayList<>();
    private ClusterManager mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        events = (ArrayList<EventForMapDTO>) getIntent().getSerializableExtra("EVENTS");

        mMapView = findViewById(R.id.mainMapView);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
    }

    private void getLastKnownLocation(){
        Log.d("getLastKnownLocation", "called");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    currentLocation = task.getResult();
                    mMapView.getMapAsync(MapActivity.this);
                }
            }
        });
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(){
           @Override
           public void onLocationResult(LocationResult locationResult){
               currentLocation = locationResult.getLastLocation();
               if(currentLocation!=null){
                   Log.d("Trenutna lokacija:", " "+currentLocation.getLatitude() + " "+currentLocation.getLongitude());
                   mMapView.getMapAsync(MapActivity.this);
               }
           }
        }, Looper.myLooper());
    }

    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    /*
     * Provera da li je google play instaliran na uredjaju
     * */
    public boolean isServicesOK(){
        Log.d("isServiceOK", "I dont know");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d("isServiceOK", "It is");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d("isServiceOK", "An error occured");
            Toast.makeText(this, "Cant make map requests without google play", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Cant make map requests without google play", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /*
     * Provera da li je aplikaciji dozvoljeno da koristi gps
     * */
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?").setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentMA = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMA);
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, PERMISSION_REQUEST_ENABLE_GPS);
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "called");
        switch(requestCode){
            case PERMISSION_REQUEST_ENABLE_GPS:{
                if(mLocationPermissonGranted){
                    getLastKnownLocation();
                }else{
                    getLocationPermission();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        mLocationPermissonGranted = false;
        switch (requestCode){
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissonGranted = true;
                }
            }
        }
    }

    private void getLocationPermission(){
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mLocationPermissonGranted = true;
            getLastKnownLocation();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void addEventsOnMap(GoogleMap googleMap, ArrayList<EventForMapDTO> events){
        if(googleMap!=null){
            if(mClusterManager == null){
                mClusterManager = new ClusterManager<ClusterMarker>(getApplicationContext(), googleMap);
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new ClusterManagerRenderer(
                        this, googleMap, mClusterManager
                );
            }
            mClusterManager.setRenderer(mClusterManagerRenderer);
        }

        for(EventForMapDTO e : events){
            ClusterMarker newClusterMarker = new ClusterMarker(
                    new LatLng(e.getLatitude(), e.getLongitude()), e.getEventName(), e.getEventName(), e.getEventImageURI()
            );
            Log.d("Podaci: ", " " + newClusterMarker.getPosition());
            mClusterManager.addItem(newClusterMarker);
            mClusterMarkers.add(newClusterMarker);
        }
        mClusterManager.cluster();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onMapReady", "called" + currentLocation.getLatitude() + " : " + currentLocation.getLatitude());
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions currentLocationMarker = new MarkerOptions().position(latLng).title("You");
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(currentLocationMarker);

        addEventsOnMap(googleMap, events);
       /* MarkerOptions eventMarker = new MarkerOptions();
        for(EventForMapDTO e : events){
            eventMarker.position(new LatLng(e.getLatitude(), e.getLongitude())).title(e.getEventName());
            googleMap.addMarker(eventMarker);
        }*/
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
        if(checkMapServices()){
            if(mLocationPermissonGranted){
                getLastKnownLocation();
            }else{
                getLocationPermission();
            }
        }
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
