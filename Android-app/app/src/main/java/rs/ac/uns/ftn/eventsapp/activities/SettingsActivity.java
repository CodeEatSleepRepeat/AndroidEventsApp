package rs.ac.uns.ftn.eventsapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import rs.ac.uns.ftn.eventsapp.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static int MIN_DISTANCE = 1;
    public static int MAX_DISTANCE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(R.string.nav_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyPreferenceFragment()).commit(); 
    }

    public static class MyPreferenceFragment extends PreferenceFragment{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceManager().findPreference("pref_default_distance2").setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            String enteredValueString = (String) newValue;
                            int enteredValue = Integer.valueOf(enteredValueString);
                            if(enteredValue >= MIN_DISTANCE && enteredValue<= MAX_DISTANCE){
                                return true;
                            }
                            else{
                                Toast.makeText(getActivity(), "Value must be between " +
                                                + MIN_DISTANCE + " and " + MAX_DISTANCE + " !",
                                        Toast.LENGTH_SHORT).show();
                                return false;
                            }

                        }
                    }
            );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
