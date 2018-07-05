package com.hb.mydietcoach.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.adapter.WeightHistoryAdapter;
import com.hb.mydietcoach.model.WeightChangeHistory;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;
import com.hb.mydietcoach.utils.MyUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class WeightLoggingActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = WeightLoggingActivity.class.getSimpleName();

    private static final int NOT_SCROLL = 0;
    private static final int SCROLL_UP = 1;
    private static final int SCROOL_DOWN = 2;

    private static final long LINE_CHANGING_LENGTH = 10;

    private DrawerLayout drawer;

    private PreferenceManager pm;

    private Realm databaseRealm;

    private NumberFormat numberFormat;

    //For target weight
    private float targetWeight;
    private EditText edtTargetWeight;
    private RelativeLayout rlWeightChange;

    //Init weight
    private float previousWeight;
    private int diffDay;

    //Current weight
    private EditText edtCurrWeight;
    private float currWeight;

    //For change weight by ruller
    private RullerListener rulerListener;
    private float currRotation = 0;
    private ImageView ivWeightWheel;
    //Array line on ruler
    private int[] arrLine;
    private int linePosition; //Line position
    private ImageView[] ivRulers;

    //Weight container and dumbbells
    private FrameLayout flWeightContainer;
    private ImageView[] ivDumbbells;
    private AnimationSet dumbbellAnimation;
    private ScaleDumbbellTask scaleDumbbellTask;

    //Tracking handle
    private FrameLayout flSwitchWeight;
    private ImageView ivTrackingHandle;
    //Rotate tracking handle, hide a view
    private Animation trackingHandleAnimation, alphaAnimation;
    private Animation translateTop;
    //Last weight before save weight
    private float lastWeight;

    //Detail weight change
    private LinearLayout llDetailWeightChange;
    private TextView tvAlertWeightChange, tvAmountWeightChange, tvAmountDayChange;
    private RecyclerView recyclerView;
    private List<WeightChangeHistory> weightHistories;


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
        databaseRealm = Realm.getDefaultInstance();
        RealmResults<WeightChangeHistory> historyResults = databaseRealm.where(WeightChangeHistory.class).findAll();
        weightHistories = new ArrayList<>();
        weightHistories.addAll(historyResults);

        //Number format
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);

        //Get target weight
        pm = new PreferenceManager(this);
        targetWeight = pm.getFloat(PreferenceManager.TARGET_WEIGHT, -1);
        targetWeight = MyUtils.roundFloat(numberFormat, targetWeight);

        previousWeight = pm.getFloat(PreferenceManager.CURRENT_WEIGHT, -1);
        previousWeight = MyUtils.roundFloat(numberFormat, previousWeight);
        currWeight = previousWeight;

        long previousDay = pm.getLong(PreferenceManager.LAST_WEIGHT_LOGGING_DAY,
                Calendar.getInstance().getTimeInMillis());

        diffDay = MyUtils.getDifferentDay(previousDay, Calendar.getInstance().getTimeInMillis());

        //Change weight by ruler
        rulerListener = new RullerListener();
        arrLine = new int[]{
                R.drawable.track_animation_1, R.drawable.track_animation_2,
                R.drawable.track_animation_3, R.drawable.track_animation_4,
                R.drawable.track_animation_5, R.drawable.track_animation_6,
                R.drawable.track_animation_7, R.drawable.track_animation_8,
                R.drawable.track_animation_9, R.drawable.track_animation_10,
                R.drawable.track_animation_11
        };
        linePosition = 0;

        //Tracking handle
        trackingHandleAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_tracking_handle);
        trackingHandleAnimation.setDuration(Constants.TRACKING_HANDLE_ANIM_LENGTH);
        //Listener
        trackingHandleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (flSwitchWeight != null) flSwitchWeight.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        //Rotate weight container
        translateTop = AnimationUtils.loadAnimation(WeightLoggingActivity.this, R.anim.translate_top);
        translateTop.setDuration(Constants.TRACKING_HANDLE_ANIM_LENGTH);
        translateTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showRecyclerView();
                ivDumbbells[0].clearAnimation();
                flWeightContainer.setRotation(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaAnimation.setDuration(Constants.ANIMATION_LENGTH);
        alphaAnimation.setRepeatCount(3);

        //Scale Dumbbell Task
        scaleDumbbellTask = new ScaleDumbbellTask();
        dumbbellAnimation = new AnimationSet(true);
        //Dumbbell animation
        //Scale smaller
        ScaleAnimation sa = new ScaleAnimation(1f, 1f, .8f, .8f);
        sa.setDuration(600);
        dumbbellAnimation.addAnimation(sa);
        //Alpha anim
        AlphaAnimation aa = new AlphaAnimation(1f, 0f);
        aa.setDuration(400);
        dumbbellAnimation.addAnimation(aa);
        //Scale bigger
        sa = new ScaleAnimation(.8f, .8f, 1.2f, 1.2f);
        dumbbellAnimation.addAnimation(sa);
        //Scale to normal
        sa = new ScaleAnimation(1.2f, 1.2f, 1f, 1f);
        dumbbellAnimation.addAnimation(sa);
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

        //First set goal
        TextView tvSetGoal = findViewById(R.id.tvSetGoal);
        rlWeightChange = findViewById(R.id.rlChangeWeight);

        //Weight goal/target
        edtTargetWeight = findViewById(R.id.edtWeightGoal);
        if (targetWeight > 0) edtTargetWeight.setText(numberFormat.format(targetWeight));

        //Weight wheel
        ivWeightWheel = findViewById(R.id.ivWeightWheel);

        //Ruler
        LinearLayout llRuler = findViewById(R.id.llRuler);
        llRuler.setOnTouchListener(rulerListener);

        //Array of image view line on ruler
        ivRulers = getImageViewLinesOnRuler();

        //Current weight
        edtCurrWeight = findViewById(R.id.edtCurrentWeight);
        TextView tvCurrentWeight = findViewById(R.id.tvCurrentWeight);

        //Detail weight change
        llDetailWeightChange = findViewById(R.id.llWeightChangeDetail);
        tvAlertWeightChange = findViewById(R.id.tvAlertChangeWeight);
        tvAmountWeightChange = findViewById(R.id.tvAmountWeightChange);
        tvAmountDayChange = findViewById(R.id.tvAmountDayChange);

        //Track handle
        flSwitchWeight = findViewById(R.id.flSwitchWeight);
        ivTrackingHandle = findViewById(R.id.ivTrackingHandle);

        // Weight Container and dumbbell
        flWeightContainer = findViewById(R.id.flWeightContainer);
        ivDumbbells = getImageViewDumbbells();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(WeightLoggingActivity.this,
                LinearLayoutManager.VERTICAL, false));

        if (currWeight > 0) {
            updateCurrentWeight(currWeight);
        } else {
            tvSetGoal.setVisibility(View.VISIBLE);
            tvCurrentWeight.setText(R.string.start_weight);

            currWeight = pm.getFloat(PreferenceManager.START_WEIGHT, 60);
            previousWeight = currWeight;
            updateCurrentWeight(currWeight);
        }

        //last weight
        lastWeight = currWeight;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        showRecyclerView();
    }

    @SuppressLint("StaticFieldLeak")
    private void showRecyclerView() {
        WeightHistoryAdapter adapter = new WeightHistoryAdapter(WeightLoggingActivity.this,
                weightHistories, recyclerView.getWidth());
        recyclerView.setAdapter(adapter);
    }

    boolean isShowRecyclerView = false;

    @OnClick(R.id.ivShowMore)
    void clickShowRecyclerView() {
        if (isShowRecyclerView) {
            isShowRecyclerView = false;
            showNormalRecyclerView();
        } else {
            isShowRecyclerView = true;
            showFullRecyclerView();
        }
    }

    private void showNormalRecyclerView() {
        //Down arrow
        ImageView ivShowMore = findViewById(R.id.ivShowMore);
        ivShowMore.setRotation(180);

        RelativeLayout rlRecyclerView = findViewById(R.id.rlRecyclerView);

        //RecyclerView under flWeightContainer
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.flWeightContainer);

        rlRecyclerView.setLayoutParams(params);
    }

    private void showFullRecyclerView() {
        //Up arrow
        ImageView ivShowMore = findViewById(R.id.ivShowMore);
        ivShowMore.setRotation(0);

        RelativeLayout rlRecyclerView = findViewById(R.id.rlRecyclerView);

        //RecyclerView under flWeightContainer
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        rlRecyclerView.setLayoutParams(params);
    }


    private ImageView[] getImageViewDumbbells() {
        ImageView[] arr = new ImageView[5];

        ImageView imageView;
        imageView = findViewById(R.id.ivDumbbell1);
        arr[0] = imageView;

        imageView = findViewById(R.id.ivDumbbell2);
        arr[3] = imageView;

        imageView = findViewById(R.id.ivDumbbell3);
        arr[1] = imageView;

        imageView = findViewById(R.id.ivDumbbell4);
        arr[4] = imageView;

        imageView = findViewById(R.id.ivDumbbell5);
        arr[2] = imageView;

        return arr;
    }

    private ImageView[] getImageViewLinesOnRuler() {
        ImageView[] arr = new ImageView[9];

        ImageView imageView;
        imageView = findViewById(R.id.ivItemRuler1);
        arr[0] = imageView;

        imageView = findViewById(R.id.ivItemRuler2);
        arr[1] = imageView;

        imageView = findViewById(R.id.ivItemRuler3);
        arr[2] = imageView;

        imageView = findViewById(R.id.ivItemRuler4);
        arr[3] = imageView;

        imageView = findViewById(R.id.ivItemRuler5);
        arr[4] = imageView;

        imageView = findViewById(R.id.ivItemRuler6);
        arr[5] = imageView;

        imageView = findViewById(R.id.ivItemRuler7);
        arr[6] = imageView;

        imageView = findViewById(R.id.ivItemRuler8);
        arr[7] = imageView;

        imageView = findViewById(R.id.ivItemRuler9);
        arr[8] = imageView;

        return arr;
    }

    /**
     * Update current weight
     *
     * @param weight: current weight
     */
    private void updateCurrentWeight(float weight) {
        edtCurrWeight.setText(numberFormat.format(weight));
    }

    @SuppressLint("SetTextI18n")
    private void showDetailWeightChange(float weight) {
        //Detail weight change
        if (llDetailWeightChange.getVisibility() != View.VISIBLE) {
            llDetailWeightChange.setVisibility(View.VISIBLE);
        }

        float wchange = MyUtils.roundFloat(numberFormat, weight - previousWeight);

        if (wchange > 0) {
            tvAlertWeightChange.setText(R.string.you_gained);
        } else {
            tvAlertWeightChange.setText(R.string.you_lost);
        }

        String change;
        float absWeight = Math.abs(wchange);
        if (absWeight >= 1) {
            change = numberFormat.format(absWeight) + " " + getString(R.string.kgs);
        } else {
            change = Math.round(absWeight * 1000) + " " + getString(R.string.gr);
        }
        tvAmountWeightChange.setText(change);

        String dayChange = getString(R.string.in) + " " + diffDay + " " + getString(R.string.days);
        tvAmountDayChange.setText(dayChange);

        //Switch weight
        if (flSwitchWeight.getVisibility() != View.VISIBLE) {
            flSwitchWeight.setVisibility(View.VISIBLE);
        }

        flSwitchWeight.startAnimation(alphaAnimation);
    }

    private void showDumbbellAnimation() {
        if (scaleDumbbellTask.isAnimationEnd) {
            scaleDumbbellTask = new ScaleDumbbellTask();
            scaleDumbbellTask.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ScaleDumbbellTask extends AsyncTask<Void, Integer, Void> {
        boolean isAnimationEnd = true;
        int count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isAnimationEnd) {
                isAnimationEnd = false;
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (count = 0; count < ivDumbbells.length; count++) {
                publishProgress(count);
                SystemClock.sleep(dumbbellAnimation.getDuration());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ivDumbbells[values[0]].startAnimation(dumbbellAnimation);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isAnimationEnd = true;
        }
    }

    @OnClick(R.id.ivWeightGain)
    void clickWeightGain() {
        currWeight += 0.1;
        updateCurrentWeight(currWeight);
        showDetailWeightChange(currWeight);

        showDumbbellAnimation();
    }

    @OnClick(R.id.ivWeightLoss)
    void clickWeightLoss() {
        currWeight -= 0.1;
        updateCurrentWeight(currWeight);
        showDetailWeightChange(currWeight);

        showDumbbellAnimation();
    }

    @OnClick(R.id.flSwitchWeight)
    void clickTrackingHandle() {
        //Save to database
        saveWeightChange();

        //Update view
        ivTrackingHandle.startAnimation(trackingHandleAnimation);
        flWeightContainer.setRotation(180);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivDumbbells[0].startAnimation(translateTop);
            }
        }, 200);
    }

    private void saveWeightChange() {
        //Save preference
        if (currWeight > 0) pm.putFloat(PreferenceManager.CURRENT_WEIGHT, currWeight);

        float wchange = MyUtils.roundFloat(numberFormat, currWeight - lastWeight);
        WeightChangeHistory history = new WeightChangeHistory(Calendar.getInstance().getTimeInMillis(), currWeight, wchange);

        //Add to list
        weightHistories.add(history);

        //Save realm database
        databaseRealm.beginTransaction();
        databaseRealm.insertOrUpdate(history);
        databaseRealm.commitTransaction();

        //Update last weight
        lastWeight = currWeight;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            float target = Float.parseFloat(edtTargetWeight.getText().toString());
            if (target > 0) pm.putFloat(PreferenceManager.TARGET_WEIGHT, target);

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
        int scrollType;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                startPositionY = motionEvent.getY();
                scrollType = NOT_SCROLL;

                showDumbbellAnimation();
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                float change = motionEvent.getY() - startPositionY;

                //Rotation wheel
                rotation = currRotation + change;
                ivWeightWheel.setRotation(rotation);

                //Update weight
                weight = currWeight - change / 100;
                updateCurrentWeight(weight);
                showDetailWeightChange(weight);

                if (scrollType == NOT_SCROLL) {
                    scrollType = (change > 0) ? SCROOL_DOWN : SCROLL_UP;
                    changeLineInRuler();
                }

            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                currRotation = rotation;
                currWeight = weight;

                scrollType = NOT_SCROLL;
            }
            return true;
        }
    }

    Runnable linesRunnable = new Runnable() {
        @Override
        public void run() {
            if (ivRulers == null || rulerListener == null) return;

            for (ImageView ivRuler : ivRulers) ivRuler.setImageResource(arrLine[linePosition]);
            changeLineInRuler();
        }
    };

    /**
     * Change line in ruler like animation
     */
    private void changeLineInRuler() {
        if (rulerListener.scrollType == SCROLL_UP) {
            --linePosition;
            if (linePosition < 0) linePosition = arrLine.length - 1;

            new Handler().postDelayed(linesRunnable, LINE_CHANGING_LENGTH);
        } else if (rulerListener.scrollType == SCROOL_DOWN) {
            ++linePosition;
            if (linePosition > arrLine.length - 1) linePosition = 0;

            new Handler().postDelayed(linesRunnable, LINE_CHANGING_LENGTH);
        }
    }
}
