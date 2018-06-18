package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        navigationView.setCheckedItem(R.id.nav_home);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_diary) {
            openDiaryActivity();
        } else if (id == R.id.nav_log_weight) {

        } else if (id == R.id.nav_reminder) {
            openReminderActivity();
        } else if (id == R.id.nav_photos) {
            openPhotosActivity();
        } else if (id == R.id.nav_tips) {
            openTipsActivity();
        } else if (id == R.id.nav_challenges) {
            openChallengesActivity();
        } else if (id == R.id.nav_rewards) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_contact) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Click diary layout
    @OnClick(R.id.llDiary)
    void clickDiary(View view) {
        openDiaryActivity();
    }

    //Open DiaryActivity
    private void openDiaryActivity() {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    //Click reminder layout
    @OnClick(R.id.llReminder)
    void clickReminder(View view) {
        openReminderActivity();
    }

    //Open ReminderActivity
    private void openReminderActivity() {
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }

    //Click challenges layout
    @OnClick(R.id.llChallenges)
    void clickChallenges(View view){
        openChallengesActivity();
    }

    //Open ChallengesActivity
    private void openChallengesActivity(){
        Intent intent = new Intent(this, ChallengesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llPhotos)
    void clickPhotos(View view){
        openPhotosActivity();
    }

    void openPhotosActivity(){
        Intent intent = new Intent(this, PhotosActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.llTips)
    void clickTips(View view){
        openTipsActivity();
    }

    void openTipsActivity(){
        Intent intent = new Intent(this, TipsActivity.class);
        startActivity(intent);
    }
}
