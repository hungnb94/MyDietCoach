package com.hb.mydietcoach.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.dialog.SettingReminderDialog;
import com.hb.mydietcoach.model.Exercise;
import com.hb.mydietcoach.notification.NotificationReceiver;
import com.hb.mydietcoach.utils.Constants;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddExerciseActivity extends AppCompatActivity
        implements SettingReminderDialog.ChangeReminderListener {

    private static final String TAG = AddExerciseActivity.class.getSimpleName();

    private EditText edtName, edtDuration, edtCalories;
    private TextView tvTime;
    private Calendar timer;

    //Function reminder
    private boolean isReminder = false;
    private int minutesFromEvent = -15;

    private SimpleDateFormat sdfHour, sdfFullTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        timer = Calendar.getInstance();
        sdfHour = new SimpleDateFormat(Constants.FORMAT_HOUR);
        sdfFullTime = new SimpleDateFormat(Constants.FULL_DATE_FORMAT);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.add_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        edtName = findViewById(R.id.edtExerciseName);
        edtDuration = findViewById(R.id.edtDuration);
        edtCalories = findViewById(R.id.edtCalories);

        //Set time
        tvTime = findViewById(R.id.tvTime);
        String strTime = sdfHour.format(timer.getTime());
        tvTime.setText(strTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnCancel)
    void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.btnSave)
    void saveExercise(View view) {
        try {
            String name = edtName.getText().toString();
            String strDuration = edtDuration.getText().toString();
            String strCalories = edtCalories.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(strDuration) || TextUtils.isEmpty(strCalories)) {
                Toast.makeText(this, getString(R.string.input_full_info), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            float duration = Float.parseFloat(strDuration);
            float calories = Float.parseFloat(strCalories);
            if (duration <= 0 || calories <= 0) {
                Toast.makeText(this, R.string.input_wrong_type, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            String strTime = sdfFullTime.format(timer.getTime());
            Exercise exercise = new Exercise(0, name, strCalories, strTime, isReminder, minutesFromEvent);
            MyDatabase db = new MyDatabase(this);
            long id = db.insertExercise(exercise);
            exercise.setId(id);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_SERIALIZABLE, exercise);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            setAlarmNotification();
            finish();
        } catch (Exception ex) {
            Log.e(TAG, "Exception " + ex.toString());
            ex.printStackTrace();
        }
    }

    /**
     * Set notification
     */
    private void setAlarmNotification() {
        if (!isReminder) return;
        Intent notifyIntent = new Intent(this, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NOTIFICATION_CONTENT_TEXT, edtName.getText().toString());
        notifyIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, Constants.RC_EXERCISE_NOTIFICATION, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar alertTime = Calendar.getInstance();
        alertTime.set(Calendar.HOUR_OF_DAY, timer.get(Calendar.HOUR_OF_DAY));
        alertTime.set(Calendar.MINUTE, timer.get(Calendar.MINUTE));
        alertTime.add(Calendar.MINUTE, minutesFromEvent);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime.getTimeInMillis(), pendingIntent);
    }

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            timer.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timer.set(Calendar.MINUTE, minute);
            String strTime = sdfHour.format(timer.getTime());
            tvTime.setText(strTime);
        }
    };

    @OnClick(R.id.llTimer)
    void changeTime(View view) {
        TimePickerDialog tpd = TimePickerDialog.newInstance(timeSetListener,
                timer.get(Calendar.HOUR_OF_DAY), timer.get(Calendar.MINUTE), false);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.show(getFragmentManager(), "set hour");
    }

    @OnClick(R.id.llReminder)
    void clickReminder(View view) {
        SettingReminderDialog dialog = new SettingReminderDialog(this);
        dialog.init(isReminder, minutesFromEvent);
        dialog.show();
    }

    @Override
    public void changeReminder(boolean isReminder, int minutesFromEvent) {
        this.isReminder = isReminder;
        this.minutesFromEvent = minutesFromEvent;
        Log.e(TAG, "Change reminder setting");
        Log.e(TAG, "isReminder " + isReminder + " minutes " + minutesFromEvent);
    }
}
