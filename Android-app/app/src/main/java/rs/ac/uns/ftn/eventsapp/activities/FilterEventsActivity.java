package rs.ac.uns.ftn.eventsapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import rs.ac.uns.ftn.eventsapp.models.SortType;

public class FilterEventsActivity extends AppCompatActivity {

    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private Toolbar toolbar;
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
    private ImageButton resetStartDateTime;
    private ImageButton resetEndingDateTime;

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

        resetStartDateTime = findViewById(R.id.resetStartDateTimeFilterImgBtn);
        resetEndingDateTime = findViewById(R.id.resetEndDateTimeFilterImgBtn);

        privateEventFilterCheckBox = findViewById(R.id.privateEventFilterCheckBox);
        privateEventFilterCheckBox.setVisibility(View.INVISIBLE);

        resetStartDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingDateEditText.setText("");
                startingTimeEditText.setText("");
            }
        });

        resetEndingDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endingDateEditText.setText("");
                endingTimeEditText.setText("");
            }
        });

        /*SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(pref.getString("pref_default_event_sort", "2")!=null){
            if(pref.getString("pref_default_event_sort", "2").equals("1")){
                forYouFilterBtn.setChecked(true);
                recentFilterBtn.setChecked(false);
                popularFilterBtn.setChecked(false);
            }else if(pref.getString("pref_default_event_sort", "2").equals("2")){
                forYouFilterBtn.setChecked(false);
                recentFilterBtn.setChecked(true);
                popularFilterBtn.setChecked(false);
            }else if(pref.getString("pref_default_event_sort", "2").equals("3")){
                forYouFilterBtn.setChecked(false);
                recentFilterBtn.setChecked(false);
                popularFilterBtn.setChecked(true);
            }
        }else{
            forYouFilterBtn.setChecked(true);
            recentFilterBtn.setChecked(false);
            popularFilterBtn.setChecked(false);
        }*/

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
                startingDateEditText.setError(null);
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
                startingTimeEditText.setError(null);
            }
        };

        startingTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startingTimeEditText.getText().toString().equals("")) {
                    new TimePickerDialog(FilterEventsActivity.this, startingTime,
                            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                } else {
                    //postavi ga na osnovu vrednosti iz polja
                    final int date = Integer.parseInt(startingTimeEditText.getText().toString().split(":")[0]);
                    final int time = Integer.parseInt(startingTimeEditText.getText().toString().split(":")[1]);
                    new TimePickerDialog(FilterEventsActivity.this, startingTime,
                            date, time, true).show();
                }
            }
        });

        startingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startingDateEditText.getText().toString().equals("")) {
                    new DatePickerDialog(FilterEventsActivity.this, startingDate,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                } else {
                    //postavi ga na osnovu vrednosti iz polja
                    final int day = Integer.parseInt(startingDateEditText.getText().toString().split("/")[0]);
                    final int month = Integer.parseInt(startingDateEditText.getText().toString().split("/")[1]) - 1;
                    final int year = Integer.parseInt(startingDateEditText.getText().toString().split("/")[2]);
                    new DatePickerDialog(FilterEventsActivity.this, startingDate, year, month, day).show();
                }
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
                endingDateEditText.setText(sdf.format(calendar2.getTime()));
                endingDateEditText.setError(null);
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
                if (minute < 10) {
                    endingTimeEditText.setText(hourOfDay + ":0" + minute);
                } else {
                    endingTimeEditText.setText(hourOfDay + ":" + minute);
                }
                endingTimeEditText.setError(null);
            }
        };

        endingTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endingTimeEditText.getText().toString().equals("")) {
                    new TimePickerDialog(FilterEventsActivity.this, endingTime,
                            calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), true).show();
                } else {
                    //postavi ga na osnovu vrednosti iz polja
                    final int date = Integer.parseInt(endingTimeEditText.getText().toString().split(":")[0]);
                    final int time = Integer.parseInt(endingTimeEditText.getText().toString().split(":")[1]);
                    new TimePickerDialog(FilterEventsActivity.this, endingTime,
                            date, time, true).show();
                }
            }
        });

        endingDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endingDateEditText.getText().toString().equals("")) {
                    new DatePickerDialog(FilterEventsActivity.this, endingDate,
                            calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
                            calendar2.get(Calendar.DAY_OF_MONTH)).show();
                } else {
                    //postavi ga na osnovu vrednosti iz polja
                    final int day = Integer.parseInt(endingDateEditText.getText().toString().split("/")[0]);
                    final int month = Integer.parseInt(endingDateEditText.getText().toString().split("/")[1]) - 1;
                    final int year = Integer.parseInt(endingDateEditText.getText().toString().split("/")[2]);
                    new DatePickerDialog(FilterEventsActivity.this, endingDate, year, month, day).show();
                }
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

                Intent returnIntent = new Intent();
                returnIntent.putExtra("DISTANCE", seekbar.getProgress());
                returnIntent.putExtra("SORT", getSortValue());
                returnIntent.putExtra("CATEGORY", getCategory());
                returnIntent.putExtra("START_DATE", startingDateEditText.getText().toString());
                returnIntent.putExtra("START_TIME", startingTimeEditText.getText().toString());
                if(!startingDateEditText.getText().toString().equals("") && !startingTimeEditText.getText().toString().equals("")) {
                    int hour = Integer.parseInt(startingTimeEditText.getText().toString().split(":")[0]);
                    int min = Integer.parseInt(startingTimeEditText.getText().toString().split(":")[1]);
                    int day = Integer.parseInt(startingDateEditText.getText().toString().split("/")[0]);
                    int month = Integer.parseInt(startingDateEditText.getText().toString().split("/")[1]) - 1;
                    int year = Integer.parseInt(startingDateEditText.getText().toString().split("/")[2]);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                    returnIntent.putExtra("START", calendar.getTime().toString());
                }
                returnIntent.putExtra("END_DATE", endingDateEditText.getText().toString());
                returnIntent.putExtra("END_TIME", endingTimeEditText.getText().toString());
                if(!endingDateEditText.getText().toString().equals("") && !endingTimeEditText.getText().toString().equals("")){
                    int hour = Integer.parseInt(endingTimeEditText.getText().toString().split(":")[0]);
                    int min = Integer.parseInt(endingTimeEditText.getText().toString().split(":")[1]);
                    int day = Integer.parseInt(endingDateEditText.getText().toString().split("/")[0]);
                    int month = Integer.parseInt(endingDateEditText.getText().toString().split("/")[1]) - 1;
                    int year = Integer.parseInt(endingDateEditText.getText().toString().split("/")[2]);
                    calendar2.set(Calendar.YEAR, year);
                    calendar2.set(Calendar.MONTH, month);
                    calendar2.set(Calendar.DAY_OF_MONTH, day);
                    calendar2.set(Calendar.HOUR_OF_DAY, hour);
                    calendar2.set(Calendar.MINUTE, min);
                    returnIntent.putExtra("END", calendar2.getTime().toString());
                }
                returnIntent.putExtra("PRIVATE", privateEventFilterCheckBox.isChecked());

                if(validationSuccess()) {
                    Toast.makeText(getApplicationContext(), "Apply Filters", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        if (getIntent() != null)
            setStateViaChips(getIntent());

    }

    /**
     * Sets Filter View with data collected from chips on Home screen
     *
     * @param savedIntent
     */
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
            } else if (chipText.startsWith("Starts")) {
                startingDateEditText.setText(chipText.split(" ")[1]);
                startingTimeEditText.setText(chipText.split(" ")[2]);
            } else if (chipText.startsWith("Ends")) {
                endingDateEditText.setText(chipText.split(" ")[1]);
                endingTimeEditText.setText(chipText.split(" ")[2]);
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

    private Boolean validationSuccess() {
        if (startingDateEditText.getText().toString().trim().equals("") && !startingTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Both start date and start time need to be filled in!", Toast.LENGTH_LONG).show();
            startingDateEditText.setError(Integer.toString(R.string.blankFieldError));
            return false;
        }
        if (!startingDateEditText.getText().toString().trim().equals("") && startingTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Both start date and start time need to be filled in!", Toast.LENGTH_LONG).show();
            startingTimeEditText.setError(Integer.toString(R.string.blankFieldError));
            return false;
        }
        if (endingDateEditText.getText().toString().trim().equals("") && !endingTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Both end date and end time need to be filled in!", Toast.LENGTH_LONG).show();
            endingDateEditText.setError(Integer.toString(R.string.blankFieldError));
            return false;
        }
        if (!endingDateEditText.getText().toString().trim().equals("") && endingTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Both end date and end time need to be filled in!", Toast.LENGTH_LONG).show();
            endingTimeEditText.setError(Integer.toString(R.string.blankFieldError));
            return false;
        }
        if (calendar.getTimeInMillis() >= calendar2.getTimeInMillis()) {
            Toast.makeText(getApplicationContext(), R.string.createEventValidation10, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
