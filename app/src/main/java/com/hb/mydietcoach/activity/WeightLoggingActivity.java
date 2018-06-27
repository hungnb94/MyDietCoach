package com.hb.mydietcoach.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.preference.PreferenceManager;

import java.text.NumberFormat;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeightLoggingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = WeightLoggingActivity.class.getSimpleName();

    private DrawerLayout drawer;

    private PreferenceManager pre;

    private NumberFormat numberFormat;

    //For target weight
    private float targetWeight;
    private EditText edtTargetWeight;

    //Current weight
    private EditText edtCurrWeight;
    private float currWeight;

    //For change weight by ruller
    private RullerListener rulerListener;
    private float currRotation = 0;
    private ImageView ivWeightWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_logging);

        init();
    }

    private void init() {
        initParam();
        initView();
    }

    private void initParam() {
        //Get target weight
        pre = new PreferenceManager(this);
        targetWeight = pre.getFloat(PreferenceManager.TARGET_WEIGHT, -1);

        //Number format
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        //Change weight by ruler
        rulerListener = new RullerListener();

        currWeight = pre.getFloat(PreferenceManager.CURRENT_WEIGHT, -1);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.my_motivational_photo);
        ButterKnife.bind(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_log_weight);

        //Weight goal/target
        edtTargetWeight = findViewById(R.id.edtWeightGoal);
        if (targetWeight > 0) edtTargetWeight.setText(numberFormat.format(targetWeight));

        //Weight wheel
        ivWeightWheel = findViewById(R.id.ivWeightWheel);

        //Ruler
        LinearLayout llRuler = findViewById(R.id.llRuler);
        llRuler.setOnTouchListener(rulerListener);

        //Current weight
        edtCurrWeight = findViewById(R.id.edtCurrentWeight);
        TextView tvCurrentWeight = findViewById(R.id.tvCurrentWeight);

        if (currWeight > 0) {
            updateCurrentWeight(currWeight);
        } else {
            tvCurrentWeight.setText(R.string.start_weight);
            currWeight = pre.getFloat(PreferenceManager.USER_WEIGHT, 60);
            updateCurrentWeight(currWeight);
        }
    }

    private void updateCurrentWeight(float weight) {
        edtCurrWeight.setText(numberFormat.format(weight));
    }

    @OnClick(R.id.ivWeightGain)
    void clickWeightGain() {
        currWeight += 0.1;
        updateCurrentWeight(currWeight);
    }

    @OnClick(R.id.ivWeightLoss)
    void clickWeightLoss() {
        currWeight -= 0.1;
        updateCurrentWeight(currWeight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (targetWeight > 0) getMenuInflater().inflate(R.menu.tip_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            float target = Float.parseFloat(edtTargetWeight.getText().toString());
            if (target > 0) pre.putFloat(PreferenceManager.TARGET_WEIGHT, target);
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.getMessage());
            e.printStackTrace();
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

    class RullerListener implements View.OnTouchListener {
        float startPositionY;
        float rotation;
        float weight;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                startPositionY = motionEvent.getY();
            else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                float change = motionEvent.getY() - startPositionY;

                //Rotation wheel
                rotation = currRotation + change;
                ivWeightWheel.setRotation(rotation);

                //Update weight
                weight = currWeight + change / 100;
                updateCurrentWeight(weight);

            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                currRotation = rotation;
                currWeight = weight;
            }
            return true;
        }
    }
}
