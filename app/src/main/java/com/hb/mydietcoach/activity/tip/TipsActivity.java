package com.hb.mydietcoach.activity.tip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.activity.ContactFAQActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TipsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_tip:
                openAddingTipActicity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddingTipActicity() {
        Intent intent = new Intent(this, AddingTipActivity.class);
        startActivity(intent);
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_tips);
    }

    @OnClick(R.id.llFoodCravings)
    void clickFoodCravings() {
        openTipDetailActivity(Constants.ID_FOOD_CRAVINGS,
                getString(R.string.food_cravings),
                R.drawable.ic_cravings_tips);
    }

    @OnClick(R.id.llEmotionalEating)
    void clickEmotionalEating() {
        openTipDetailActivity(Constants.ID_EMOTIONAL_EATING,
                getString(R.string.emotional_eating),
                R.drawable.ic_emotional_tips);
    }

    @OnClick(R.id.llEatingOut)
    void clickEatingOut() {
        openTipDetailActivity(Constants.ID_EATING_OUT,
                getString(R.string.eating_out),
                R.drawable.ic_eating_out_tips);
    }

    @OnClick(R.id.llFoodTemptation)
    void clickFoodTemptation() {
        openTipDetailActivity(Constants.ID_FOOD_TEMPTATIOIN,
                getString(R.string.food_temptation),
                R.drawable.ic_temptation_tips);
    }

    @OnClick(R.id.llFamilyMeal)
    void clickFamilyMeal() {
        openTipDetailActivity(Constants.ID_FAMILY_MEAL,
                getString(R.string.family_meal),
                R.drawable.ic_family_meal_tips);
    }

    private void openTipDetailActivity(int tipID, String title, int imgResID) {
        Intent intent = new Intent(this, TipDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TipDetailActivity.TIP_CATEGORY_ID, tipID);
        bundle.putString(TipDetailActivity.TIP_TITLE, title);
        bundle.putInt(TipDetailActivity.TIP_IMAGE_RESOURCE, imgResID);
        intent.putExtras(bundle);
        startActivity(intent);

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
            //Blank
        } else if (id == R.id.nav_challenges) {
            Intent intent = new Intent(this, ChallengesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_rewards) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
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
