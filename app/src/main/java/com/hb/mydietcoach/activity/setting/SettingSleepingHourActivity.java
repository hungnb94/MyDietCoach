package com.hb.mydietcoach.activity.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingSleepingHourActivity extends AppCompatActivity {
    private TextView tvStartHour, tvEndHour;

    private int nStartHour, nStartMinute, nEndHour, nEndMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sleeping_hour);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.set_sleeping_hours);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        PreferenceManager pre = new PreferenceManager(this);
        nStartHour = pre.getInt(PreferenceManager.SETTING_START_SLEEPING_HOUR, 22);
        nEndHour = pre.getInt(PreferenceManager.SETTING_END_SLEEPING_HOUR, 6);
        nStartMinute = pre.getInt(PreferenceManager.SETTING_START_SLEEPING_MINUTE, 0);
        nEndMinute = pre.getInt(PreferenceManager.SETTING_END_SLEEPING_MINUTE, 0);

        tvStartHour = findViewById(R.id.tvStartHour);
        tvEndHour = findViewById(R.id.tvEndHour);

        updateUIStartHour();
        updateUIEndHour();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUIStartHour() {
        String text = (nStartHour < 10 ? "0" + nStartHour : "" + nStartHour);
        text += ":" + (nStartMinute < 10 ? "0" + nStartMinute : "" + nStartMinute);
        tvStartHour.setText(text);
    }

    private void updateUIEndHour() {
        String text = (nEndHour < 10 ? "0" + nEndHour : "" + nEndHour);
        text += ":" + (nEndMinute < 10 ? "0" + nEndMinute : "" + nEndMinute);
        tvEndHour.setText(text);
    }

    @OnClick(R.id.llStartTime)
    void clickStartTime() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                nStartHour = hourOfDay;
                nStartMinute = minute;
                updateUIStartHour();
            }
        };
        TimePickerDialog tpd = TimePickerDialog.newInstance(timeSetListener,
                nStartHour, nStartMinute, true);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.show(getFragmentManager(), "set start hour");
    }

    @OnClick(R.id.llEndTime)
    void clickEndTime() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                nEndHour = hourOfDay;
                nEndMinute = minute;
                updateUIEndHour();
            }
        };
        TimePickerDialog tpd = TimePickerDialog.newInstance(timeSetListener,
                nEndHour, nEndMinute, true);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.show(getFragmentManager(), "set end hour");
    }

    @OnClick(R.id.tvCancel)
    void clickCancel() {
        finish();
    }

    @OnClick(R.id.tvContinue)
    void clickContinue() {
        PreferenceManager pre = new PreferenceManager(this);
        pre.putInt(PreferenceManager.SETTING_START_SLEEPING_HOUR, nStartHour);
        pre.putInt(PreferenceManager.SETTING_START_SLEEPING_MINUTE, nStartMinute);
        pre.putInt(PreferenceManager.SETTING_END_SLEEPING_HOUR, nEndHour);
        pre.putInt(PreferenceManager.SETTING_END_SLEEPING_MINUTE, nEndMinute);

        finish();
    }
}
