package rs.ac.uns.ftn.eventsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import rs.ac.uns.ftn.eventsapp.R;

public class FilterEventsActivity extends AppCompatActivity {

    private TextView showProgress;
    private Button forYouBtn;
    private Button recentBtn;
    private Button popularBtn;
    private SeekBar seekbar;

    public FilterEventsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_events);
        showProgress = findViewById(R.id.kmFilterTextView);
        seekbar = findViewById(R.id.distanceFilterSeekBar);

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
