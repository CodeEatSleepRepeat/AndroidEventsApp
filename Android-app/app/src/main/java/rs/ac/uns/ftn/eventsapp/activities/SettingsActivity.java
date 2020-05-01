package rs.ac.uns.ftn.eventsapp.activities;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rs.ac.uns.ftn.eventsapp.R;

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

    public static class MyPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            
            EditTextPreference pref = (EditTextPreference) findPreference("pref_default_distance2");
            pref.getEditText().setFilters(new InputFilter[]{
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                            try {
                                int enteredValue = Integer.parseInt(dest.toString() + source.toString());
                                if (enteredValue >= MIN_DISTANCE && enteredValue <= MAX_DISTANCE)
                                    return null;
                            } catch (NumberFormatException nfe) {
                            }
                            Toast.makeText(getActivity(), "Value must be between " +
                                            +MIN_DISTANCE + " and " + MAX_DISTANCE + " !",
                                    Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
