package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rs.ac.uns.ftn.eventsapp.R;

public class FilterEventsActivity extends AppCompatActivity {

    final Calendar calendar = Calendar.getInstance();
    private EditText startingDateEditText;
    private EditText startingTimeEditText;
    private EditText endingDateEditText;
    private EditText endingTimeEditText;

    private TextView showProgress;
    private SeekBar seekbar;

    private Button forYouBtn;
    private Button recentBtn;
    private Button popularBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_events);

        showProgress = findViewById(R.id.kmFilterTextView);
        seekbar = findViewById(R.id.distanceFilterSeekBar);

        startingDateEditText = findViewById(R.id.startingDateFilterEditText);
        startingTimeEditText = findViewById(R.id.startingTimeFilterEditText);
        endingDateEditText = findViewById(R.id.endingDateFilterEditText);
        endingTimeEditText = findViewById(R.id.endingTimeFilterEditText);

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

        forYouBtn = findViewById(R.id.forYouFilterBtn);
        recentBtn = findViewById(R.id.recentFilterBtn);
        popularBtn = findViewById(R.id.popularFilterBtn);

        forYouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryBtn(forYouBtn.getId());
            }
        });

        recentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryBtn(recentBtn.getId());
            }
        });

        popularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryBtn(popularBtn.getId());
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

    }

    private void onClickCategoryBtn(int id) {
        switch (id) {
            case R.id.forYouFilterBtn:
                forYouBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                forYouBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                recentBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                recentBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                popularBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                popularBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.recentFilterBtn:
                recentBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                recentBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                forYouBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                forYouBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                popularBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                popularBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.popularFilterBtn:
                popularBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                popularBtn.setTextColor(getResources().getColor(R.color.colorWhite));
                recentBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                recentBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                forYouBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                forYouBtn.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
        }
    }
}
