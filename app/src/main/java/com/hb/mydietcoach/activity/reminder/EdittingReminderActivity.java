package com.hb.mydietcoach.activity.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.model.Reminder;
import com.hb.mydietcoach.notification.NotificationManager;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EdittingReminderActivity extends AppCompatActivity {

    public static final String IS_EDIT = "is_edit";
    public static final String IS_MOTIVATIONAL_REMINDER = "is_motivational";
    private boolean isEdit = false;
    private boolean isMotivational = false;

    private Reminder reminder;
    private EditText editText;
    private TextView tvDate, tvTime;
    private Spinner spinner;

    MyDatabase database;

    private SimpleDateFormat formatDate, formatTime;

    String arr[];
    private ArrayAdapter<String> adapter;
    public static final int HOUR_MINISECOND = 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editting_reminder);

        database = new MyDatabase(this);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.edit_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        editText = findViewById(R.id.editText);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        spinner = findViewById(R.id.spinner);

        formatDate = new SimpleDateFormat(Constants.DATE_FORMAT);
        formatTime = new SimpleDateFormat(Constants.FORMAT_HOUR);
        initSpinner();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean(IS_EDIT, false);
            isMotivational = bundle.getBoolean(IS_MOTIVATIONAL_REMINDER, false);
        }

        if (isEdit) {
            reminder = (Reminder) bundle.getSerializable(Constants.DATA_SERIALIZABLE);
            editText.setText(reminder.getContent());
            Calendar calendar = reminder.getStartDate();
            tvDate.setText(formatDate.format(calendar.getTime()));
            tvTime.setText(formatTime.format(calendar.getTime()));
            setSpinnerSelection(reminder.getRepeatMilisecond());
        } else if (isMotivational) {
            reminder = (Reminder) bundle.getSerializable(Constants.DATA_SERIALIZABLE);
            editText.setText(reminder.getContent());
            Calendar calendar = reminder.getStartDate();
            tvDate.setText(formatDate.format(calendar.getTime()));
            tvTime.setText(formatTime.format(calendar.getTime()));
            setSpinnerSelection(reminder.getRepeatMilisecond());
        } else {
            reminder = new Reminder();
            Calendar calendar = Calendar.getInstance();
            reminder.setStartDate(calendar);
            tvDate.setText(formatDate.format(calendar.getTime()));
            tvTime.setText(formatTime.format(calendar.getTime()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.action_delete) {
            database.deleteReminder(reminder.getId());
            setResult(Constants.RESULT_DELETE_REMINDER);
            Toast.makeText(this, R.string.reminder_was_deleted, Toast.LENGTH_SHORT).show();
            NotificationManager manager = new NotificationManager(this);
            manager.cancelAlarm((int) reminder.getId());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Init spinner
     */
    private void initSpinner() {
        arr = getResources().getStringArray(R.array.spinner_editting_reminder);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, arr);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    //Listener handle event click an item on spinner
    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                reminder.setRepeatMilisecond(0);
            } else if (i == 1) {
                reminder.setRepeatMilisecond(HOUR_MINISECOND);
            } else if (i == 2) {
                reminder.setRepeatMilisecond(2 * HOUR_MINISECOND);
            } else if (i == 3) {
                reminder.setRepeatMilisecond(3 * HOUR_MINISECOND);
            } else if (i == 4) {
                reminder.setRepeatMilisecond(4 * HOUR_MINISECOND);
            } else if (i == 5) {
                reminder.setRepeatMilisecond(24 * HOUR_MINISECOND);
            } else {
                reminder.setRepeatMilisecond(24 * 7 * HOUR_MINISECOND);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    //Listener handle event change date
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            reminder.getStartDate().set(year, monthOfYear, dayOfMonth);
            String strDate = formatDate.format(reminder.getStartDate().getTime());
            tvDate.setText(strDate);
        }
    };

    //Listener for change hour
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            reminder.getStartDate().set(Calendar.HOUR_OF_DAY, hourOfDay);
            reminder.getStartDate().set(Calendar.MINUTE, minute);
            String strTime = formatTime.format(reminder.getStartDate().getTime());
            tvTime.setText(strTime);
        }
    };

    /**
     * Click on tvDate
     * Show DatePickerDialog
     *
     * @param view
     */
    @OnClick(R.id.tvDate)
    public void setDate(View view) {
        DatePickerDialog dialog = DatePickerDialog.newInstance(dateSetListener, reminder.getStartDate());
        dialog.show(getFragmentManager(), null);
    }

    /**
     * Click on tvTime
     * Show TimePickerDialog
     *
     * @param view
     */
    @OnClick(R.id.tvTime)
    public void setTime(View view) {
        Calendar calendar = reminder.getStartDate();
        TimePickerDialog dialog = TimePickerDialog.newInstance(timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        dialog.show(getFragmentManager(), null);
    }

    /**
     * Click save button
     * Save to database and finish activity
     */
    @OnClick(R.id.button)
    public void clickSave(View view) {
        String strContent = editText.getText().toString();
        if (TextUtils.isEmpty(strContent)) {
            Toast.makeText(this, R.string.input_reminder_text, Toast.LENGTH_SHORT).show();
            return;
        }
        reminder.setContent(strContent);
        if (isEdit) {
            database.updateReminder(reminder);
        } else {
            database.insertReminder(reminder);
            if (isMotivational){
                PreferenceManager pre = new PreferenceManager(this);
                pre.putBoolean(PreferenceManager.IS_HAS_MOTIVATIONAL_PHOTO, true);
            }
        }
        if (reminder.getRepeatMilisecond() > 0) {
            setNotification();
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.DATA_SERIALIZABLE, reminder);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Setting new notification
     */
    private void setNotification() {
        NotificationManager manager = new NotificationManager(this);
        manager.setRepeatAlarm((int) reminder.getId(),
                reminder.getContent(),
                reminder.getStartDate().getTimeInMillis(),
                reminder.getRepeatMilisecond());
    }

    /**
     * Set selected position for spinner
     *
     * @param repeatMilisecond: Repeat time in miliseconds
     */
    private void setSpinnerSelection(long repeatMilisecond) {
        if (repeatMilisecond <= 0) {
            spinner.setSelection(0);
        } else if (repeatMilisecond == HOUR_MINISECOND) {
            spinner.setSelection(1);
        } else if (repeatMilisecond == 2 * HOUR_MINISECOND) {
            spinner.setSelection(2);
        } else if (repeatMilisecond == 3 * HOUR_MINISECOND) {
            spinner.setSelection(3);
        } else if (repeatMilisecond == 4 * HOUR_MINISECOND) {
            spinner.setSelection(4);
        } else if (repeatMilisecond == 24 * HOUR_MINISECOND) {
            spinner.setSelection(5);
        } else if (repeatMilisecond == 24 * 7 * HOUR_MINISECOND) {
            spinner.setSelection(6);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.editting_reminder, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }
}
