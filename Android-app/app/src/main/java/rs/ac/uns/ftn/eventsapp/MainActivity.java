package rs.ac.uns.ftn.eventsapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.FilterEventsActivity;
import rs.ac.uns.ftn.eventsapp.activities.GoogleMapActivity;
import rs.ac.uns.ftn.eventsapp.activities.SettingsActivity;
import rs.ac.uns.ftn.eventsapp.activities.SettingsUnAuthUserActivity;
import rs.ac.uns.ftn.eventsapp.activities.SignInActivity;
import rs.ac.uns.ftn.eventsapp.activities.SplashScreenActivity;
import rs.ac.uns.ftn.eventsapp.activities.UserProfileActivity;
import rs.ac.uns.ftn.eventsapp.dtos.EventDTO;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.firebase.notification.Token;
import rs.ac.uns.ftn.eventsapp.fragments.GoingEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.HomeEventListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.InterestedEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.InvitationsFragment;
import rs.ac.uns.ftn.eventsapp.fragments.LatestMessagesFragment;
import rs.ac.uns.ftn.eventsapp.fragments.ListOfUsersFragment;
import rs.ac.uns.ftn.eventsapp.fragments.MyEventsListFragment;
import rs.ac.uns.ftn.eventsapp.models.EventType;
import rs.ac.uns.ftn.eventsapp.models.FacebookPrivacy;
import rs.ac.uns.ftn.eventsapp.models.SortType;
import rs.ac.uns.ftn.eventsapp.sync.SyncGoingInterestedEventsTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncMyEventsTask;
import rs.ac.uns.ftn.eventsapp.sync.SyncUserTask;
import rs.ac.uns.ftn.eventsapp.tools.FragmentTransition;
import rs.ac.uns.ftn.eventsapp.utils.AppDataSingleton;
import rs.ac.uns.ftn.eventsapp.utils.ClusterManagerRenderer;
import rs.ac.uns.ftn.eventsapp.utils.ClusterMarker;


public class MainActivity extends AppCompatActivity{

    private static final int LAUNCH_FILTER_ACTIVITY = 1001;
    private static final int LAUNCH_USER_PROFILE_ACTIVITY = 5001;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ChipGroup chipGroup;
    private boolean accepted = true;
    private static final int PERMISSION_REQUEST_ENABLE_GPS = 9002;

    private int distance = 100;
    private SortType sortType = SortType.RECENT;
    private ArrayList<String> eventTypes = new ArrayList<>();
    private String start = "min";// = Instant.ofEpochMilli(Long.MIN_VALUE).atZone(ZoneOffset.UTC);
    private String end = "max"; //= Instant.ofEpochMilli(Long.MAX_VALUE).atZone(ZoneOffset.UTC);
    private FacebookPrivacy facebookPrivacy;
    private Location location;

    //private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Log.d("SORTRF", sortType.toString());
        AppDataSingleton.getInstance().setContext(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ovo je super za proveru konekcije interneta i odmah reaguje na promene, ali ne radi bas ako se vracam iz aktivnosti bez interneta -> etapa 3
        /*noInternetDialog = new NoInternetDialog.Builder(this)
                .setBgGradientStart(getResources().getColor(R.color.colorPrimary)) // Start color for background gradient
                .setBgGradientCenter(getResources().getColor(R.color.colorPrimaryDark)) // Center color for background gradient
                //.setBgGradientEnd(getResources().getColor(R.color.colorBlack)) // End color for background gradient
                //.setButtonColor(R.color.colorWhite) // Set custom color for dialog buttons
                //.setButtonTextColor(R.color.colorWhite) // Set custom text color for dialog buttons
                //.setButtonIconsColor(R.color.colorWhite) // Set custom color for icons of dialog buttons
                .setWifiLoaderColor(R.color.colorBlack) // Set custom color for wifi loader
                .setCancelable(false)
                .build();*/

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        isMapsEnabled();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);

        if (savedInstanceState == null) {
            try {
                Bundle bundle = setUpSearchFilter();
                Fragment f = HomeEventListFragment.class.newInstance();
                f.setArguments(bundle);
                FragmentTransition.to(f, this, false);
                FrameLayout frameLayout = findViewById(R.id.fragment_view);
                View fragmentView = frameLayout.getChildAt(0);

                //check if there was chip-group
                if (chipGroup == null) {
                    chipGroup = fragmentView.findViewById(R.id.chipsGroup);
                    chipGroup.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }
        } else {
            int selectedNavItem = savedInstanceState.getInt("SELECTED_NAV_ITEM");
            if (selectedNavItem != -1) {
                clearCheckedItems(navigationView.getMenu());

                if (selectedNavItem / 10 > 0) {
                    navigationView.getMenu().getItem(selectedNavItem / 10).getSubMenu().getItem(selectedNavItem % 10).setChecked(true);
                } else {
                    navigationView.getMenu().getItem(selectedNavItem % 10).setChecked(true);
                }
            }
        }

        FloatingActionButton fab = findViewById(R.id.floating_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateEvent();
            }
        });

        FloatingActionButton fabMap = findViewById(R.id.floating_map_btn);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMap();
            }
        });

        if (!AppDataSingleton.getInstance().isLoggedIn()) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_drawer_unauthorized_user);
            setNavigationListenerUnauthorizedUser(navigationView, toolbar);
            changeNavBarUnauthorized();
        } else {
            setNavigationListenerAuthorizedUser(navigationView, toolbar);
            changeNavBarProfile(AppDataSingleton.getInstance().getLoggedUser().getName(), AppDataSingleton.getInstance().getLoggedUser().getImageUri());
        }

        boolean isUserLoggedToFirebase = FirebaseAuth.getInstance().getCurrentUser() != null;
        if(isUserLoggedToFirebase){
            updateFirebaseToken();
        }

    }

    public void updateFirebaseToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
                        Token mToken = new Token(newToken);
                        ref.child(FirebaseAuth.getInstance().getUid()).setValue(mToken);
                    }
                });
    }

    private void setNavigationListenerUnauthorizedUser(final NavigationView navigationView, final Toolbar toolbar) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_home:
                        onClickNavItem(HomeEventListFragment.class);
                        break;
                    case R.id.navigation_item_settings_unauth:
                        openSettingsUnauth();
                        break;
                    case R.id.navigation_item_sign_in:
                        logout();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, menuItem.getItemId(), Toast.LENGTH_LONG).show();
                        return false;
                }

                return true;
            }
        });

        //sakrij map dugme
        FloatingActionButton fabMap = findViewById(R.id.floating_map_btn);
        fabMap.hide();

        //pretvori add dugme u map dugme
        FloatingActionButton fab = findViewById(R.id.floating_add_btn);

        fab.setImageResource(android.R.drawable.ic_dialog_map);
        fab.hide(); //ovo je zbog baga googla: https://stackoverflow.com/questions/54506295/icon-not-showing-in-floatingactionbutton-after-changed-programmatically
        fab.show(); //ovo je zbog baga googla: https://stackoverflow.com/questions/54506295/icon-not-showing-in-floatingactionbutton-after-changed-programmatically
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMap();
            }
        });
    }

    private void setNavigationListenerAuthorizedUser(final NavigationView navigationView, final Toolbar toolbar) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_home:
                        onClickNavItem(HomeEventListFragment.class);
                        break;
                    case R.id.navigation_item_create:
                        onClickCreateEvent();
                        break;
                    case R.id.navigation_item_myEvents:
                        onClickNavItem(MyEventsListFragment.class);
                        break;
                    case R.id.navigation_item_going:
                        onClickNavItem(GoingEventsListFragment.class);
                        break;
                    case R.id.navigation_item_interested:
                        onClickNavItem(InterestedEventsListFragment.class);
                        break;
                    case R.id.navigation_item_friends:
                        onClickNavItem(ListOfUsersFragment.class);
                        break;
                    case R.id.navigation_item_messages:
                        Boolean isUserLoggedToFirebaseMessageService =
                                FirebaseAuth.getInstance().getCurrentUser() != null;
                        if (isUserLoggedToFirebaseMessageService)
                            onClickNavItem(LatestMessagesFragment.class);
                        else
                            Toast.makeText(MainActivity.this, "Message system is not avaible at the " +
                                            "moment",
                                    Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_logout:
                        logout();
                        break;
                    case R.id.navigation_item_invitations:
                        onClickNavItem(InvitationsFragment.class);
                        break;
                    case R.id.navigation_item_settings:
                        openSettings();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, menuItem.getItemId(), Toast.LENGTH_LONG).show();
                        return false;
                }

                return true;
            }
        });

        //set on picture and name click listener for user info activity
        View header = navigationView.getHeaderView(0);
        CircleImageView imageIcon = header.findViewById(R.id.circle_crop);
        TextView userNameTextView = header.findViewById(R.id.userNameNavDrawer);

        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUserProfile();
            }
        });

        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUserProfile();
            }
        });
    }


    /**
     * Deselect all menu items in navigation drawer
     *
     * @param menu
     */
    private void clearCheckedItems(@NonNull final Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                clearCheckedItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_drawer_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings_home:
                openSettings();
                return true;
            case R.id.filterEventsBtn:
                onClickFilterImg();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (chipGroup != null && chipGroup.getChildCount() != 0) {
            removeFilterChips();
        } else {
            super.onBackPressed();

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_view);
            onAttachFragment(currentFragment);
        }
    }

    private void onClickFilterImg() {
        Intent intent = new Intent(this, FilterEventsActivity.class);
        String[] chipTexts;

        if (chipGroup != null) {
            chipTexts = new String[chipGroup.getChildCount() + 1];

            //dodaj postojece chip-ove
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                chipTexts[i + 1] = String.valueOf(chip.getText());
            }
        } else {
            chipTexts = new String[1];
        }
        chipTexts[0] = String.valueOf(((TextView) findViewById(R.id.sortFilterTextView)).getText());

        intent.putExtra("currentChips", chipTexts);
        startActivityForResult(intent, LAUNCH_FILTER_ACTIVITY);
    }

    private void onClickMap() {
        List<EventDTO> items = new ArrayList<>();
        ArrayList<EventForMapDTO> eventsForMap = new ArrayList<>();
        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null && f.isVisible()) {
                    if (f instanceof GoingEventsListFragment) {
                        items = ((GoingEventsListFragment) f).getItems();
                    } else if (f instanceof HomeEventListFragment) {
                        items = ((HomeEventListFragment) f).getItems();
                    } else if (f instanceof InterestedEventsListFragment) {
                        items = ((InterestedEventsListFragment) f).getItems();
                    } else if (f instanceof MyEventsListFragment) {
                        items = ((MyEventsListFragment) f).getItems();
                    }
                }
            }
        }
        for (EventDTO item : items) {
            if (item.getImageUri() == null || item.getImageUri().equals("") || item.getImageUri().startsWith("http")) {
                eventsForMap.add(new EventForMapDTO(item.getId(), item.getName(), item.getLatitude(), item.getLongitude(), item.getImageUri()));
            } else {
                eventsForMap.add(new EventForMapDTO(item.getId(), item.getName(), item.getLatitude(), item.getLongitude(), AppDataSingleton.IMAGE_URI + item.getImageUri()));
            }
        }
        Intent intent = new Intent(this, GoogleMapActivity.class);
        intent.putExtra("EVENTS", eventsForMap);
        startActivity(intent);

    }

    private void onClickCreateEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    private void onClickUserProfile() {
        mDrawerLayout.closeDrawers();

        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("userId", AppDataSingleton.getInstance().getLoggedUser().getId());
        intent.putExtra("userName", AppDataSingleton.getInstance().getLoggedUser().getName());
        intent.putExtra("email", AppDataSingleton.getInstance().getLoggedUser().getEmail());
        intent.putExtra("password", AppDataSingleton.getInstance().getLoggedUser().getPassword());
        intent.putExtra("profileImageUrl", AppDataSingleton.getInstance().getLoggedUser().getImageUri());
        intent.putExtra("fbId", AppDataSingleton.getInstance().getLoggedUser().getFacebookId());
        startActivityForResult(intent, LAUNCH_USER_PROFILE_ACTIVITY);
    }

    private void onClickNavItem(Class<?> intentClass) {
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, intentClass).commit();
        try {
            Bundle bundle = setUpSearchFilter();
            Fragment f = (Fragment) intentClass.newInstance();
            f.setArguments(bundle);
            FragmentTransition.to(f, this, true);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "onClickNavItem: ");
            e.printStackTrace();
        }
    }

    private void logout() {
        //firebase sign out
        FirebaseAuth userFirebaseInstance = FirebaseAuth.getInstance();
        userFirebaseInstance.signOut();
        //DB deletion
        AppDataSingleton.getInstance().deleteAllPhysical();
        //delete last sync preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SplashScreenActivity.SYNC_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SyncUserTask.preferenceSyncUser);
        editor.remove(SyncMyEventsTask.preferenceSyncMyEvents);
        editor.remove(SyncGoingInterestedEventsTask.preferenceSyncGIEvents);
        editor.commit();

        //FB login btn reset
        LoginManager.getInstance().logOut();
        //goto sign in screen
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_FILTER_ACTIVITY) {
            //filter vraca odgovor
            if (resultCode == Activity.RESULT_OK) {
                //user je podesio neke filtere

                int dist = data.getIntExtra("DISTANCE", 100);
                String sort = data.getStringExtra("SORT");
                String[] category = data.getStringArrayExtra("CATEGORY");
                String startDate = data.getStringExtra("START_DATE");
                String startTime = data.getStringExtra("START_TIME");
                String endDate = data.getStringExtra("END_DATE");
                String endTime = data.getStringExtra("END_TIME");
                Boolean privateEvents = data.getBooleanExtra("PRIVATE", true);

                //priprema teksta za chips
                ArrayList<String> chipTexts = new ArrayList<>();

                if (dist > 0) {
                    chipTexts.add(dist + "km");
                    distance = dist;
                }else{
                    distance = 100;
                }

                if (!startDate.equals("") && !startTime.equals("")) {
                    Log.d("DATE", data.getStringExtra("START"));
                    chipTexts.add("Starts " + startDate + " " + startTime);
                    start =  data.getStringExtra("START");
                }else{
                    start = "min";
                }

                if (!endDate.equals("") && !endTime.equals("")) {
                    Log.d("DATE", data.getStringExtra("END"));
                    chipTexts.add("Ends " + endDate + " " + endTime);
                    end =  data.getStringExtra("END");
                }else{
                    end = "max";
                }
                Log.d("END", end + "");
                eventTypes.clear();
                for (String categoryItem : category) {
                    eventTypes.add(categoryItem);
                    chipTexts.add(categoryItem);
                }
                if (privateEvents) {
                    chipTexts.add("Private events");
                }

                //setovanje sort by
                switch (sort) {
                    case "For you":
                        ((TextView) findViewById(R.id.sortFilterTextView)).setText("Events for you");
                        sortType = SortType.FOR_YOU;
                        break;
                    case "Popular":
                        ((TextView) findViewById(R.id.sortFilterTextView)).setText("Popular events");
                        sortType = SortType.POPULAR;
                        break;
                    default:
                        ((TextView) findViewById(R.id.sortFilterTextView)).setText("Recent events");
                        sortType = SortType.RECENT;
                        break;
                }

                //uklanjanje prethodnih chips-a
                removeFilterChips();
                //dodavanje novih chips-a
                setFilterChips(chipTexts);
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                //user je kliknuo na brisanje svih filtera
                removeFilterChips();
            }

            FragmentManager fm = MainActivity.this.getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            if (fragments != null) {
                for (Fragment f : fragments) {
                    if (f != null && f.isVisible()) {
                        if (f instanceof GoingEventsListFragment) {
                            //items = ((GoingEventsListFragment) f).getItems();
                        } else if (f instanceof HomeEventListFragment) {
                            Log.d("USAOOO", distance+"");
                            FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
                            ftr.detach(f).commitNow();
                            ftr.attach(f).commitNow();
                        } else if (f instanceof InterestedEventsListFragment) {
                            //items = ((InterestedEventsListFragment) f).getItems();
                        } else if (f instanceof MyEventsListFragment) {
                            //items = ((MyEventsListFragment) f).getItems();
                        }
                    }
                }
            }

        } else if (requestCode == LAUNCH_USER_PROFILE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            //imamo novu sliku i user name za nav drawer
            changeNavBarProfile(AppDataSingleton.getInstance().getLoggedUser().getName(), AppDataSingleton.getInstance().getLoggedUser().getImageUri());
        }else if (requestCode == PERMISSION_REQUEST_ENABLE_GPS){
            return;
        }

    }

    private void setFilterChips(ArrayList<String> chipTexts) {
        try {
            FrameLayout frameLayout = findViewById(R.id.fragment_view);
            View fragmentView = frameLayout.getChildAt(0);
            chipGroup = fragmentView.findViewById(R.id.chipsGroup);

            for (String text : chipTexts) {
                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_item, null, false);
                mChip.setText(text);
                mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < chipGroup.getChildCount(); i++) {
                            Chip chip = (Chip) chipGroup.getChildAt(i);
                            if (chip.getText().equals(((Chip) v).getText())) {
                                removeChip(chip);
                                return;
                            }
                        }
                    }
                });
                mChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < chipGroup.getChildCount(); i++) {
                            Chip chip = (Chip) chipGroup.getChildAt(i);
                            if (chip.getText().equals(((Chip) v).getText())) {
                                removeChip(chip);
                                return;
                            }
                        }
                    }
                });

                //Ne prikazuj date i time chip, ali ih cuvaj zbog setovanja filtera
                if (text.startsWith("Starts") || text.startsWith("Ends")) {
                    mChip.setVisibility(View.GONE);
                }

                chipGroup.addView(mChip);
            }

            chipGroup.setVisibility(View.VISIBLE);

        } catch (Exception e) {
        }
    }

    private void removeChip(final Chip chip) {
        FrameLayout frameLayout = findViewById(R.id.fragment_view);
        View fragmentView = frameLayout.getChildAt(0);
        @SuppressWarnings("ConstantConditions") final ChipGroup optionsList = chipGroup = fragmentView.findViewById(R.id.chipsGroup);
        // Remove the chip with an animation
        if (chip == null) return;
        AlphaAnimation anim = new AlphaAnimation(1f, 0f);
        anim.setDuration(250);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                chipGroup.removeView(chip);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        chip.startAnimation(anim);
    }

    private void removeFilterChips() {
        try {
            chipGroup.setVisibility(View.GONE);
            chipGroup.removeAllViews();
        } catch (Exception e) {
        }
    }

    /**
     * Setovanje toolbar naslova i navigation drawer is
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        //TODO: proveriti korisnost ovog koda
        super.onAttachFragment(fragment);

        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);

        if (fragment == null) {
            return;
        }

        if (navigationView != null) clearCheckedItems(navigationView.getMenu());

        if (fragment instanceof HomeEventListFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_home);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_home).setChecked(true);
        } else if (fragment instanceof MyEventsListFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_myEvents);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_myEvents).setChecked(true);
        } else if (fragment instanceof InterestedEventsListFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_interested);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_interested).setChecked(true);
        } else if (fragment instanceof GoingEventsListFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_going);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_going).setChecked(true);
        } else if (fragment instanceof LatestMessagesFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_messages);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_messages).setChecked(true);
        } else if (fragment instanceof InvitationsFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_invitations);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_invitations).setChecked(true);
        } else if (fragment instanceof ListOfUsersFragment) {
            if (toolbar != null) toolbar.setTitle(R.string.nav_item_friends);
            if (navigationView != null)
                navigationView.getMenu().findItem(R.id.navigation_item_friends).setChecked(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SELECTED_NAV_ITEM", getCheckedItem((NavigationView) findViewById(R.id.navigation_view)));

        //setovanje sort by ako je na home fragmentu, ako nije puca
        if (findViewById(R.id.sortFilterTextView) != null) {
            outState.putString("SORT", (((TextView) findViewById(R.id.sortFilterTextView)).getText()).toString());
        }

        if (chipGroup != null) {
            ArrayList<String> chipsText = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                chipsText.add(chip.getText().toString());
            }
            outState.putStringArrayList("CHIPS", chipsText);
        }
    }

    private int getCheckedItem(NavigationView navigationView) {
        if (navigationView == null) {
            return -1;
        }

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                Menu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    if (subMenu.getItem(j).isChecked()) {
                        return i * 10 + j;
                    }
                }
            } else {
                if (item.isChecked()) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> chipsText = savedInstanceState.getStringArrayList("CHIPS");
        setFilterChips(chipsText);
        //set sort text
        String sort = savedInstanceState.getString("SORT");
        if (sort != null) {
            ((TextView) findViewById(R.id.sortFilterTextView)).setText(sort);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openSettingsUnauth() {
        Intent intent = new Intent(this, SettingsUnAuthUserActivity.class);
        startActivity(intent);
    }

    private void changeNavBarProfile(@NonNull String name, @NonNull String imgUri) {
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userNameNavDrawer);
        CircleImageView userImage = navigationView.getHeaderView(0).findViewById(R.id.circle_crop);

        userName.setText(name);
        if (!imgUri.equals("")) {

            Picasso.get().setLoggingEnabled(true);
            if (imgUri.startsWith("http")) {
                Picasso.get().load(Uri.parse(imgUri)).placeholder(R.drawable.ic_user_icon).into(userImage);
            } else {
                Picasso.get().load(Uri.parse(AppDataSingleton.PROFILE_IMAGE_URI + imgUri)).placeholder(R.drawable.ic_user_icon).into(userImage);
            }
        } else {
            userImage.setImageResource(R.drawable.ic_user_icon);
        }
    }

    private void changeNavBarUnauthorized() {
        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userNameNavDrawer);
        CircleImageView userImage = navigationView.getHeaderView(0).findViewById(R.id.circle_crop);

        userName.setText(getResources().getText(R.string.app_name));
        userImage.setImageResource(R.drawable.icon);
    }

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
        builder.setMessage(R.string.gps_dialog).setCancelable(false).setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accepted = false;
                Intent intentMA = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMA);
            }
        }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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
    protected void onResume() {
        super.onResume();
        Log.d("SORTRF", sortType.toString());
    }

    private Bundle setUpSearchFilter(){
        Bundle bundle = new Bundle();
        bundle.putString("SORT", sortType.name());
        bundle.putStringArrayList("TYPES", eventTypes);
        bundle.putString("START", start);
        bundle.putString("END", end);
        bundle.putString("LAT", "45.5484");
        bundle.putString("LNG", "19.7927");
        bundle.putString("DIST", distance+"");
        return bundle;
    }
}


