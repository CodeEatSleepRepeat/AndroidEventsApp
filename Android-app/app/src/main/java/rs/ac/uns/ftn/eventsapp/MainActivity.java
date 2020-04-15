package rs.ac.uns.ftn.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import rs.ac.uns.ftn.eventsapp.activities.CreateEventActivity;
import rs.ac.uns.ftn.eventsapp.activities.FilterEventsActivity;
import rs.ac.uns.ftn.eventsapp.activities.SignInActivity;
import rs.ac.uns.ftn.eventsapp.fragments.HomeEventListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.GoingEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.InterestedEventsListFragment;
import rs.ac.uns.ftn.eventsapp.fragments.InvitationsFragment;
import rs.ac.uns.ftn.eventsapp.fragments.LatestMessagesFragment;
import rs.ac.uns.ftn.eventsapp.fragments.ListOfUsersFragment;
import rs.ac.uns.ftn.eventsapp.fragments.MyEventsListFragment;
import rs.ac.uns.ftn.eventsapp.tools.FragmentTransition;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
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

        FloatingActionButton fab = findViewById(R.id.floating_add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.coordinator), "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Snackbar Action", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
/*
        EventListPagerAdapter adapter = new EventListPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.fragment_view);
        viewPager.setAdapter(adapter);*/
        FragmentTransition.to(HomeEventListFragment.newInstance(), this, false);
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void onClickFilterImg() {
        Intent intent = new Intent(this, FilterEventsActivity.class);
        startActivity(intent);
    }

    private void onClickCreateEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    private void onClickNavItem(Class<?> intentClass){
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view, intentClass).commit();
        try {
            FragmentTransition.to((Fragment) intentClass.newInstance(), this, true);
        } catch (Exception e){
            Log.d(this.getClass().getName(), "onClickNavItem: ");
            e.printStackTrace();
        }
    }

    private void logout(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}
