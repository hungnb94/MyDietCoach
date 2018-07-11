package com.hb.mydietcoach.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.contact_faq.ContactFAQActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.dialog.TargetArchiveDialog;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RewardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final int TARGET_DISTANT = 26;

    private DrawerLayout drawer;

    private String sLastPointFor;
    private int nPoints, nLevel, nTarget, nTotalPoints;
    private TextView tvLevel, tvLastPointFor, tvTargetPoint, tvTotalPoint;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        initView();

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.my_progress);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_rewards);

        tvLastPointFor = findViewById(R.id.tvLastPointFor);
        tvLevel = findViewById(R.id.tvLevel);
        progressBar = findViewById(R.id.progressBar);
        tvTargetPoint = findViewById(R.id.tvTargetPoint);
        tvTotalPoint = findViewById(R.id.tvTotalPoints);

        PreferenceManager pre = new PreferenceManager(this);

        sLastPointFor = pre.getString(PreferenceManager.LAST_POINTS_FOR, getString(R.string.starting_with));

        nPoints = pre.getInt(PreferenceManager.POINTS, 40);
        nLevel = pre.getInt(PreferenceManager.LEVEL, 1);
        nTotalPoints = pre.getInt(PreferenceManager.TOTAL_POINTS, nPoints);
        nTarget = 100 + (nLevel - 1) * TARGET_DISTANT;

        updateView();
    }

    private void updateView() {
        tvLastPointFor.setText(sLastPointFor);

        String sLevel = getString(R.string.level) + " " + nLevel +
                getString(R.string._target) + " " + nTarget + " " + getString(R.string.points);
        tvLevel.setText(sLevel);

        tvTargetPoint.setText(String.valueOf(nTarget));
        progressBar.setProgress(nPoints * 100 / nTarget);

        String sTotalPoint = getString(R.string.you_earned) + " " + nTotalPoints + " " + getString(R.string.point_so_far);
        tvTotalPoint.setText(sTotalPoint);
    }

    @OnClick(R.id.flLike)
    void clickLike() {
        PreferenceManager pre = new PreferenceManager(this);
        boolean isLike = pre.getBoolean(PreferenceManager.IS_LIKE_FANPAGE_FACEBOOK, false);
        if (!isLike) {
            pre.putBoolean(PreferenceManager.IS_LIKE_FANPAGE_FACEBOOK, true);

            nPoints += Constants.POINT_FOR_LIKE_FANPAGE_FACEBOOK;
            nTotalPoints += Constants.POINT_FOR_LIKE_FANPAGE_FACEBOOK;
            sLastPointFor = getString(R.string.like_fanpage);

            checkLevel();

            updateView();
        }

        openWeb(getString(R.string.link_facebook));
    }

    @OnClick(R.id.flShare)
    void clickShare() {
        PreferenceManager pre = new PreferenceManager(this);
        boolean isLike = pre.getBoolean(PreferenceManager.IS_SHARE_GOOGLE_PLUS, false);
        if (!isLike) {
            pre.putBoolean(PreferenceManager.IS_SHARE_GOOGLE_PLUS, true);

            nPoints += Constants.POINT_FOR_SHARE_GOOGLE_PLUS;
            nTotalPoints += Constants.POINT_FOR_SHARE_GOOGLE_PLUS;
            sLastPointFor = getString(R.string.share_app);

            checkLevel();

            updateView();
        }

        //Share
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String text = getString(R.string.share_text);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_using)));
    }

    @OnClick(R.id.flFollow)
    void clickFollow() {
        PreferenceManager pre = new PreferenceManager(this);
        boolean isLike = pre.getBoolean(PreferenceManager.IS_FOLLOW_TWITTER, false);
        if (!isLike) {
            pre.putBoolean(PreferenceManager.IS_FOLLOW_TWITTER, true);

            nPoints += Constants.POINT_FOR_FOLLOW_TWITTER;
            nTotalPoints += Constants.POINT_FOR_FOLLOW_TWITTER;
            sLastPointFor = getString(R.string.follow_twitter);

            checkLevel();
            updateView();
        }

        openWeb(getString(R.string.link_twitter));
    }

    private void checkLevel() {
        if (nPoints >= nTarget) {
            Dialog dialog = new TargetArchiveDialog(this, nTarget, nLevel, nTarget + TARGET_DISTANT);
            dialog.show();
            nPoints = 0;
            ++nLevel;
            nTarget += TARGET_DISTANT;
        }
    }

    /**
     * Open a link
     *
     * @param url: url
     */
    private void openWeb(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager pre = new PreferenceManager(this);
        pre.putString(PreferenceManager.LAST_POINTS_FOR, sLastPointFor);
        pre.putInt(PreferenceManager.POINTS, nPoints);
        pre.putInt(PreferenceManager.LEVEL, nLevel);
        pre.putInt(PreferenceManager.TOTAL_POINTS, nTotalPoints);
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
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactFAQActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
