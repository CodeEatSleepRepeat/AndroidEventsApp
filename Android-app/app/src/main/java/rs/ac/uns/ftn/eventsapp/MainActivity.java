package rs.ac.uns.ftn.eventsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.FilterEventsActivity;
import rs.ac.uns.ftn.eventsapp.activities.MapActivity;
import rs.ac.uns.ftn.eventsapp.activities.SignInActivity;
import rs.ac.uns.ftn.eventsapp.dtos.EventForMapDTO;
import rs.ac.uns.ftn.eventsapp.fragments.GoingEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.HomeEventListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.InterestedEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.InvitationsFragment;
import rs.ac.uns.ftn.eventsapp.fragments.LatestMessagesFragment;
import rs.ac.uns.ftn.eventsapp.fragments.ListOfUsersFragment;
import rs.ac.uns.ftn.eventsapp.fragments.MyEventsListFragment;
import rs.ac.uns.ftn.eventsapp.models.Event;
import rs.ac.uns.ftn.eventsapp.tools.FragmentTransition;


public class MainActivity extends AppCompatActivity {

    private static final int LAUNCH_FILTER_ACTIVITY = 1001;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ChipGroup chipGroup;
    //private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

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

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        if (getIntent().getBooleanExtra(SignInActivity.IS_ANONYMOUS, false)) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_drawer_unauthorized_user);
            navigationView.getHeaderView(0).setVisibility(View.GONE);
            setNavigationListenerUnauthorizedUser(navigationView, toolbar);
        } else {
            setNavigationListenerAuthorizedUser(navigationView, toolbar);
        }

        FloatingActionButton fab = findViewById(R.id.floating_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateEvent();
            }
        });

        /*
        EventListPagerAdapter adapter = new EventListPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.fragment_view);
        viewPager.setAdapter(adapter);*/
        if (savedInstanceState == null) {
            FragmentTransition.to(HomeEventListFragment.newInstance(), this, false);

            try {
                FrameLayout frameLayout = findViewById(R.id.fragment_view);
                View fragmentView = frameLayout.getChildAt(0);
                chipGroup = fragmentView.findViewById(R.id.chipsGroup);
                chipGroup.setVisibility(View.GONE);
            } catch (Exception e) {
            }

        }

        FloatingActionButton fabMap = findViewById(R.id.floating_map_btn);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMap();
            }
        });
    }

    private void setNavigationListenerUnauthorizedUser(NavigationView navigationView,
                                                       final Toolbar toolbar) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_settings_unauth:
                        Toast.makeText(MainActivity.this, "Ojsa", Toast.LENGTH_SHORT).show();
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
    }

    private void setNavigationListenerAuthorizedUser(NavigationView navigationView,
                                                     final Toolbar toolbar) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_create:
                        onClickCreateEvent();
                        break;
                    case R.id.navigation_item_myEvents:
                        onClickNavItem(MyEventsListFragment.class);
                        toolbar.setTitle(R.string.nav_item_myEvents);
                        break;
                    case R.id.navigation_item_going:
                        onClickNavItem(GoingEventsListFragment.class);
                        toolbar.setTitle(R.string.nav_item_going);
                        break;
                    case R.id.navigation_item_interested:
                        onClickNavItem(InterestedEventsListFragment.class);
                        toolbar.setTitle(R.string.nav_item_interested);
                        break;
                    case R.id.navigation_item_friends:
                        onClickNavItem(ListOfUsersFragment.class);
                        toolbar.setTitle("Veljko's friends");
                        break;
                    case R.id.navigation_item_messages:
                        onClickNavItem(LatestMessagesFragment.class);
                        toolbar.setTitle("Messages");
                        break;
                    case R.id.navigation_item_logout:
                        logout();
                        break;
                    case R.id.navigation_item_invitations:
                        onClickNavItem(InvitationsFragment.class);
                        toolbar.setTitle("Invitations");
                        break;

                    default:
                        Toast.makeText(MainActivity.this, menuItem.getItemId(), Toast.LENGTH_LONG).show();
                        return false;
                }

                return true;
            }
        });
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
            case R.id.action_settings:
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
        } else {
            super.onBackPressed();
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
        ArrayList<Event> items = new ArrayList<>();
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
        for (Event item : items) {
            eventsForMap.add(new EventForMapDTO(item.getEventId(), item.getEventName(), item.getLatitude(), item.getLongitude(), item.getEventImageURI()));
        }
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("EVENTS", eventsForMap);
        startActivity(intent);

    }

    private void onClickCreateEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    private void onClickNavItem(Class<?> intentClass) {
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, intentClass).commit();
        try {
            FragmentTransition.to((Fragment) intentClass.newInstance(), this, true);
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "onClickNavItem: ");
            e.printStackTrace();
        }
    }

    private void logout() {
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
                Boolean privateEvents = data.getBooleanExtra("PRIVATE", true);

                //priprema teksta za chips
                ArrayList<String> chipTexts = new ArrayList<>();

                if (dist > 0) {
                    chipTexts.add(Integer.toString(dist) + "km");
                }

                for (String categoryItem : category) {
                    chipTexts.add(categoryItem);
                }

                if (privateEvents) {
                    chipTexts.add("Private events");
                }

                //setovanje sort by
                switch (sort) {
                    case "For you":
                        ((TextView) findViewById(R.id.sortFilterTextView)).setText("Events for you");
                        break;
                    case "Recent":
                        ((TextView) findViewById(R.id.sortFilterTextView)).setText("Recent events");
                        break;
                    default:
                        ((TextView) findViewById(R.id.sortFilterTextView)).setText("Popular events");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //noInternetDialog.onDestroy();
    }
}


