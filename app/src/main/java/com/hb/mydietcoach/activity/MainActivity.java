package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.contact_faq.ContactFAQActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.setting.SettingsActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.preference.PreferenceManager;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private ImageView ivFatPerson, ivSlimPerson;

    private PreferenceManager pre;

    public static int nCaloriesLeft = -1;
    private TextView tvCaloriesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pre = new PreferenceManager(this);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ivFatPerson = findViewById(R.id.ivFatPerson);
        ivSlimPerson = findViewById(R.id.ivSlimPerson);

        tvCaloriesLeft = findViewById(R.id.tvCaloriesLeft);

        //Get data to update by onResume
        int nDay = pre.getInt(PreferenceManager.LAST_USING_DAY, -1);
        if (nDay > 0) {
            if (nDay == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                nCaloriesLeft = pre.getInt(PreferenceManager.CALORIES_LEFT, -1);
                if (nCaloriesLeft < 0)
                    nCaloriesLeft = pre.getInt(PreferenceManager.DAILY_CALORIES_GOAL, -1);
            } else {
                nCaloriesLeft = pre.getInt(PreferenceManager.DAILY_CALORIES_GOAL, -1);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);

        boolean isFemale = pre.getBoolean(PreferenceManager.IS_GENDER_FEMALE, true);
        if (isFemale) {
            ivFatPerson.setImageResource(R.drawable.fat_avatar_default);
            ivSlimPerson.setImageResource(R.drawable.img_slim_avatar);
        } else {
            ivFatPerson.setImageResource(R.drawable.fat_man_avatar_default);
            ivSlimPerson.setImageResource(R.drawable.slim_man_avatar_default);
        }

        //Calories left
        if (nCaloriesLeft > 0) {
            tvCaloriesLeft.setText(String.valueOf(nCaloriesLeft));
        } else if (!tvCaloriesLeft.getText().toString().equalsIgnoreCase(getString(R.string.calories))) {
            tvCaloriesLeft.setText(String.valueOf(nCaloriesLeft));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Blank
        } else if (id == R.id.nav_diary) {
            openDiaryActivity();
        } else if (id == R.id.nav_log_weight) {
            openWeightLoggingActivity();
        } else if (id == R.id.nav_reminder) {
            openReminderActivity();
        } else if (id == R.id.nav_photos) {
            openPhotosActivity();
        } else if (id == R.id.nav_tips) {
            openTipsActivity();
        } else if (id == R.id.nav_challenges) {
            openChallengesActivity();
        } else if (id == R.id.nav_rewards) {
            openRewardActivity();
        } else if (id == R.id.nav_settings) {
            openSettingsActivity();
        } else if (id == R.id.nav_contact) {
            openContactFAQActivity();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.ivCaloriesLeft)
    void clickCaloriesLeft() {
        openDiaryActivity();
    }

    //Click diary layout
    @OnClick(R.id.llDiary)
    void clickDiary() {
        openDiaryActivity();
    }

    /**
     * Open DiaryActivity
     */
    private void openDiaryActivity() {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    /**
     * Open WeightLoggingActivity
     */
    private void openWeightLoggingActivity() {
        Intent intent = new Intent(this, WeightLoggingActivity.class);
        startActivity(intent);
    }

    //Click reminder layout
    @OnClick(R.id.llReminder)
    void clickReminder() {
        openReminderActivity();
    }

    //Open ReminderActivity
    private void openReminderActivity() {
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }

    //Click challenges layout
    @OnClick(R.id.llChallenges)
    void clickChallenges() {
        openChallengesActivity();
    }

    /**
     * Open ChallengesActivity
     */
    private void openChallengesActivity() {
        Intent intent = new Intent(this, ChallengesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llRewards)
    void clickRewards() {
        openRewardActivity();
    }

    @OnClick(R.id.llPhotos)
    void clickPhotos() {
        openPhotosActivity();
    }

    /**
     * Open PhotosActivity
     */
    private void openPhotosActivity() {
        Intent intent = new Intent(this, PhotosActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llTips)
    void clickTips() {
        openTipsActivity();
    }

    /**
     * Open TipsActivity
     */
    private void openTipsActivity() {
        Intent intent = new Intent(this, TipsActivity.class);
        startActivity(intent);
    }

    private void openRewardActivity() {
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
    }

    /**
     * Open SettingsActivity
     */
    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Open ContacFAQActivity
     */
    private void openContactFAQActivity() {
        Intent intent = new Intent(this, ContactFAQActivity.class);
        startActivity(intent);
    }
}
