package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import rs.ac.uns.ftn.eventsapp.R;

public class FilterEventsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private final Calendar calendar = Calendar.getInstance();

    //distance
    private TextView showProgress;
    private SeekBar seekbar;

    //sort
    private RadioButton forYouFilterBtn;
    private RadioButton recentFilterBtn;
    private RadioButton popularFilterBtn;

    //category
    private CheckBox charityFilterRB;
    private CheckBox educationalFilterRB;
    private CheckBox talksFilterRB;
    private CheckBox musicFilterRB;
    private CheckBox partyFilterRB;
    private CheckBox sportsFilterRB;

    private CheckBox privateEventFilterCheckBox;

    //calendar
    private EditText startingDateEditText;
    private EditText startingTimeEditText;
    private EditText endingDateEditText;
    private EditText endingTimeEditText;

    private Button saveFilterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_events);

        toolbar = findViewById(R.id.toolbarFilter);
        toolbar.setTitle(getString(R.string.filter_events));
        toolbar.inflateMenu(R.menu.menu_filter);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.resetFilter) {
                    //TODO: ovde resetuj filtere na pocetne vrednosti
                    Toast.makeText(getApplicationContext(), "Reset All Filters", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_FIRST_USER);
                    finish();
                }
                return false;
            }
        });

        showProgress = findViewById(R.id.kmFilterTextView);
        seekbar = findViewById(R.id.distanceFilterSeekBar);

        forYouFilterBtn = findViewById(R.id.forYouFilterBtn);
        recentFilterBtn = findViewById(R.id.recentFilterBtn);
        popularFilterBtn = findViewById(R.id.popularFilterBtn);

        charityFilterRB = findViewById(R.id.charityFilterRB);
        educationalFilterRB = findViewById(R.id.educationalFilterRB);
        talksFilterRB = findViewById(R.id.talksFilterRB);
        musicFilterRB = findViewById(R.id.musicFilterRB);
        partyFilterRB = findViewById(R.id.partyFilterRB);
        sportsFilterRB = findViewById(R.id.sportsFilterRB);

        startingDateEditText = findViewById(R.id.startingDateFilterEditText);
        startingTimeEditText = findViewById(R.id.startingTimeFilterEditText);
        endingDateEditText = findViewById(R.id.endingDateFilterEditText);
        endingTimeEditText = findViewById(R.id.endingTimeFilterEditText);

        privateEventFilterCheckBox = findViewById(R.id.privateEventFilterCheckBox);

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
                new TimePickerDialog(FilterEventsActivity.this, startingTime,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        startingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FilterEventsActivity.this, startingDate,
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
                new TimePickerDialog(FilterEventsActivity.this, endingTime,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        endingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(FilterEventsActivity.this, endingDate,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showProgress.setText(progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveFilterBtn = findViewById(R.id.saveFilterBtn);
        saveFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Ovde se vade podaci koje je korisnik izabrao i proverava se validnost unetih podataka
                Toast.makeText(getApplicationContext(), "Apply Filters", Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("DISTANCE", seekbar.getProgress());
                returnIntent.putExtra("SORT", getSortValue());
                returnIntent.putExtra("CATEGORY", getCategory());
                //TODO: dodati i za datume isto ovo

                returnIntent.putExtra("PRIVATE", privateEventFilterCheckBox.isChecked());

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        if (getIntent() != null)
            setStateViaChips(getIntent());

    }

    private void setStateViaChips(Intent savedIntent) {
        String[] chips = savedIntent.getStringArrayExtra("currentChips");

        charityFilterRB.setChecked(false);
        educationalFilterRB.setChecked(false);
        talksFilterRB.setChecked(false);
        musicFilterRB.setChecked(false);
        partyFilterRB.setChecked(false);
        sportsFilterRB.setChecked(false);
        privateEventFilterCheckBox.setChecked(false);

        //TODO: dodati i za kalendar
        for (String chipText : chips) {
            if (chipText.endsWith("km")) {
                //distance
                seekbar.setProgress(Integer.parseInt(chipText.substring(0, chipText.length() - 2)));
                continue;
            } else {
                switch (chipText) {
                    case "Events for you":
                        forYouFilterBtn.setChecked(true);
                        recentFilterBtn.setChecked(false);
                        popularFilterBtn.setChecked(false);
                        break;
                    case "Recent events":
                        forYouFilterBtn.setChecked(false);
                        recentFilterBtn.setChecked(true);
                        popularFilterBtn.setChecked(false);
                        break;
                    case "Popular events":
                        forYouFilterBtn.setChecked(false);
                        recentFilterBtn.setChecked(false);
                        popularFilterBtn.setChecked(true);
                        break;
                    case "Charity":
                        charityFilterRB.setChecked(true);
                        break;
                    case "Educational":
                        educationalFilterRB.setChecked(true);
                        break;
                    case "Talks":
                        talksFilterRB.setChecked(true);
                        break;
                    case "Music":
                        musicFilterRB.setChecked(true);
                        break;
                    case "Party":
                        partyFilterRB.setChecked(true);
                        break;
                    case "Sports":
                        sportsFilterRB.setChecked(true);
                        break;
                    case "Private events":
                        privateEventFilterCheckBox.setChecked(true);
                    default:
                        break;
                }//switch-case
            }//if-else
        }//for

    }

    private String getSortValue() {
        if (forYouFilterBtn.isChecked()) {
            return "For you";
        }
        if (recentFilterBtn.isChecked()) {
            return "Recent";
        }
        return "Popular";
    }

    private String[] getCategory() {
        ArrayList<String> array = new ArrayList<>();

        if (charityFilterRB.isChecked()) array.add("Charity");
        if (educationalFilterRB.isChecked()) array.add("Educational");
        if (talksFilterRB.isChecked()) array.add("Talks");
        if (musicFilterRB.isChecked()) array.add("Music");
        if (partyFilterRB.isChecked()) array.add("Party");
        if (sportsFilterRB.isChecked()) array.add("Sports");

        String[] list = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = array.get(i);
        }
        return list;
    }
}
