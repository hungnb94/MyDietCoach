package com.hb.mydietcoach.activity.contact_faq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.BaseActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.RewardActivity;
import com.hb.mydietcoach.activity.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactFAQActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int FAQ_HOW_SUBCRIPTION_BE_CHARGED = 1;
    public static final int FAQ_HOW_CANCEL_SUBCRIPTION_RENEWAL = 2;
    public static final int FAQ_HOW_EDIT_DIARY_ITEM = 3;


    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_faq);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.contact_feedback);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_contact);
    }

    @OnClick(R.id.llContact)
    void clickContactFeedback() {
        Intent contactIntent = new Intent(Intent.ACTION_SEND);
        contactIntent.setType("text/plain");

        contactIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.support_email));

        String subject = getString(R.string.app_name)
                + " - " + getString(R.string.android)
                + " - " + getString(R.string.customers_support);
        contactIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        startActivity(contactIntent);
    }

    @OnClick(R.id.llSubcriptionCharged)
    void clickHowSubcriptionChargedSubcriptionChareged() {
        Intent intent = new Intent(this, FrequentAskQuestionActivity.class);
        intent.putExtra(FrequentAskQuestionActivity.FREQUENT_ASK_QUESTION,
                FrequentAskQuestionActivity.FQ_HOW_SUBCRIPTION_BE_CHARGED);
        startActivity(intent);
    }

    @OnClick(R.id.llCancelSubcriptionRenewal)
    void clickHowCancelSubcriptionRenewal() {
        Intent intent = new Intent(this, FrequentAskQuestionActivity.class);
        intent.putExtra(FrequentAskQuestionActivity.FREQUENT_ASK_QUESTION,
                FrequentAskQuestionActivity.FQ_HOW_CANCEL_SUBCRIPTION_RENEWAL);
        startActivity(intent);
    }

    @OnClick(R.id.llEditItemDiary)
    void clickHowEditItemDiary() {
        Intent intent = new Intent(this, FrequentAskQuestionActivity.class);
        intent.putExtra(FrequentAskQuestionActivity.FREQUENT_ASK_QUESTION,
                FrequentAskQuestionActivity.FQ_HOW_DO_DELETE_OR_EDIT_ITEM_FROM_DIARY);
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
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
