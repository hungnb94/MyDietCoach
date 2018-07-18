package com.hb.mydietcoach.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.RewardActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.contact_faq.ContactFAQActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hb.mydietcoach.utils.Constants.RC_CHOOSE_NOTIFICATION_SOUND;

public class SettingsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = SettingsActivity.class.getSimpleName();

    private DrawerLayout drawer;

    private PreferenceManager pre;
    //Gender
    private Spinner spinnerGender;

    //Weight
    private EditText edtStartWeight;

    private Switch swPlaySound, swMakeVibrate, swCustomizedAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pre = new PreferenceManager(this);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.settings);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_settings);

        spinnerGender = findViewById(R.id.spinnerGender);
        String[] genderTypes = getResources().getStringArray(R.array.gender_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, genderTypes);
        spinnerGender.setAdapter(adapter);

        boolean isFemale = pre.getBoolean(PreferenceManager.IS_GENDER_FEMALE, true);
        if (isFemale) spinnerGender.setSelection(0);
        else spinnerGender.setSelection(0);

        edtStartWeight = findViewById(R.id.edtWeight);

        float weight = pre.getFloat(PreferenceManager.START_WEIGHT, 80);
        edtStartWeight.setText(String.valueOf(weight));

        boolean isPlaySound = pre.getBoolean(PreferenceManager.SETTING_IS_PLAY_SOUND, false);
        swPlaySound = findViewById(R.id.swPlaySound);
        swPlaySound.setChecked(isPlaySound);

        boolean isMakeVibrate = pre.getBoolean(PreferenceManager.SETTING_IS_MAKE_VIBRATE, false);
        swMakeVibrate = findViewById(R.id.swMakeVibrate);
        swMakeVibrate.setChecked(isMakeVibrate);

        boolean isCustomizedAvatar = pre.getBoolean(PreferenceManager.SETTING_IS_SHOW_CUSTOMIZED_AVATAR, false);
        swCustomizedAvatar = findViewById(R.id.swCustomizedAvatar);
        swCustomizedAvatar.setChecked(isCustomizedAvatar);
    }

    @OnClick(R.id.llSetSleepingHour)
    void clickSetSleepingHour() {
        Intent intent = new Intent(this, SettingSleepingHourActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llChooseNotificationSound)
    void clickChooseNotificationSound() {
        PreferenceManager pre = new PreferenceManager(this);
        String strSound = pre.getString(PreferenceManager.SETTING_URI_NOTIFICATION_SOUND, null);

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.choose_notification_sound));
        if (strSound != null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(strSound));
        }
        this.startActivityForResult(intent, RC_CHOOSE_NOTIFICATION_SOUND);
    }

    private String chosenRingtone;
    private boolean isChangeRingtone;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RC_CHOOSE_NOTIFICATION_SOUND) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                isChangeRingtone = true;
                this.chosenRingtone = uri.toString();
            } else {
                this.chosenRingtone = null;
            }
        }
    }

    @OnClick(R.id.btnSave)
    void clickSave() {
        saveGender();
        saveWeight();

        pre.putBoolean(PreferenceManager.SETTING_IS_PLAY_SOUND, swPlaySound.isChecked());
        pre.putBoolean(PreferenceManager.SETTING_IS_MAKE_VIBRATE, swMakeVibrate.isChecked());
        pre.putBoolean(PreferenceManager.SETTING_IS_SHOW_CUSTOMIZED_AVATAR, swCustomizedAvatar.isChecked());
        if (isChangeRingtone) {
            pre.putString(PreferenceManager.SETTING_URI_NOTIFICATION_SOUND, chosenRingtone);
        }

        finish();
    }

    @OnClick(R.id.llPrivacyPolicy)
    void clickPrivacyPolicy() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_PRIVACY_POLICY));
        startActivity(intent);
    }

    /**
     * Save gender to SharedPreference
     */
    private void saveGender() {
        boolean isFemale = (spinnerGender.getSelectedItemPosition() == 0);
        pre.putBoolean(PreferenceManager.IS_GENDER_FEMALE, isFemale);
    }

    /**
     * Save user's weight to SharedPreference
     */
    private void saveWeight() {
        String strWeight = edtStartWeight.getText().toString();
        if (!TextUtils.isEmpty(strWeight)) {
            try {
                float weight = Float.parseFloat(strWeight);
                if (weight > 0) pre.putFloat(PreferenceManager.START_WEIGHT, weight);
            } catch (Exception e) {
                Log.e(TAG, "Weight not valid: " + strWeight);
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_diary) {
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_log_weight) {
            Intent intent = new Intent(this, WeightLoggingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_reminder) {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_photos) {
            Intent intent = new Intent(this, PhotosActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_tips) {
            Intent intent = new Intent(this, TipsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_challenges) {
            Intent intent = new Intent(this, ChallengesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(this, RewardActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactFAQActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
