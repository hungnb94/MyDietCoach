package com.hb.mydietcoach.activity;

import android.content.Intent;
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

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.preference.PreferenceManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = SettingsActivity.class.getSimpleName();

    private DrawerLayout drawer;

    private PreferenceManager pre;
    //Gender
    private Spinner spinnerGender;

    //Weight
    private EditText edtStartWeight;

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
        getSupportActionBar().setTitle(R.string.settings);
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
    }

    @OnClick(R.id.btnSave)
    void clickSave(){
        saveGender();
        saveWeight();

        finish();
    }

    /**
     * Save gender to SharedPreference
     */
    private void saveGender() {
        boolean isFemale = (spinnerGender.getSelectedItemPosition()==0);
        pre.putBoolean(PreferenceManager.IS_GENDER_FEMALE, isFemale);
    }

    /**
     * Save user's weight to SharedPreference
     */
    private void saveWeight(){
        String strWeight = edtStartWeight.getText().toString();
        if (!TextUtils.isEmpty(strWeight)){
            try {
                float weight = Float.parseFloat(strWeight);
                if (weight > 0) pre.putFloat(PreferenceManager.START_WEIGHT, weight);
            } catch (Exception e){
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

        } else if (id == R.id.nav_settings) {
            //Blank
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactFAQActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
