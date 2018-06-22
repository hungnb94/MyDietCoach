package com.hb.mydietcoach.activity.challenge;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.ContactFAQActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.diary.DiaryActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.adapter.ChallengesAdapter;
import com.hb.mydietcoach.custom_view.DrawingView;
import com.hb.mydietcoach.model.AnimationChallenge;
import com.hb.mydietcoach.model.Challenge;
import com.hb.mydietcoach.model.NormalChallenge;
import com.hb.mydietcoach.model.RunChallenge;
import com.hb.mydietcoach.model.SelfControlChallenge;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hb.mydietcoach.utils.Constants.CHALLENGE_TYPE_DRINK_WATER;
import static com.hb.mydietcoach.utils.Constants.RC_EDITTING_CHALLENGE;

public class ChallengesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChallengesAdapter.ItemEventListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = ChallengesActivity.class.getSimpleName();
    private static final long ANIMATION_CHALLENGE_LENGTH = 2000;
    private static final long ANIMATION_TEMP_LENGTH = 1000;

    private static final int SEEKBAR_MAX_VALUE = 1000;

    private DrawerLayout drawer;

    //Function guide
    private DrawingView drawingView;
    private FrameLayout flGuide;
    private LinearLayout llGuideTop, llGuideBottom;
    private Animation animTranslateTop, animTranslateBottom;
    AnimationSet animComeBack;
    private ImageView ivArrowOption;
    private LinearLayout llGuideOption;
    private boolean isFirstChallenges;

    private PreferenceManager pre;

    //Function for normal challenges
    private RecyclerView recyclerView;
    private ChallengesAdapter adapter;
    private Button btnUndo;
    private TextView tvPoint, tvTitleChallenge;
    private LinearLayout llPoint;
    private Animation animScale;
    //Function for run challenge
    private NumberFormat format;
    private RelativeLayout rlHumanPosition;
    private LinearLayout llHumanPosition;
    private TextView tvHumanPosition;
    private ImageView ivHumanPosition;
    private SeekBar seekBar;
    //Self Control Challenge
    private LinearLayout llSelfControlChallenge, llHumanShadow;
    private ImageView ivAvoidHuman, ivAvoidFoodIcon, ivAvoidFoodAnimation, ivSelfControlSign;
    //Data from last challenge
    private Challenge challenge;

    //Function show tooltip
    private boolean isShowToolTip;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        pre = new PreferenceManager(this);

        initParam();
        initView();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
            //Blank
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

    //Common funcion
    private void initParam() {
        //Funcion guideline
        animTranslateTop = AnimationUtils.loadAnimation(this, R.anim.translate_top);
        animTranslateTop.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animTranslateTop.setFillAfter(true);
        animTranslateBottom = AnimationUtils.loadAnimation(this, R.anim.translate_bottom);
        animTranslateBottom.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animTranslateBottom.setFillAfter(true);
        animComeBack = new AnimationSet(false);
        Animation animationTemp1 = AnimationUtils.loadAnimation(this, R.anim.translate_top_back);
        animationTemp1.setDuration(ANIMATION_TEMP_LENGTH);
        Animation animationTemp2 = AnimationUtils.loadAnimation(this, R.anim.translate_bottom_back);
        animationTemp2.setDuration(ANIMATION_TEMP_LENGTH);
        animComeBack.addAnimation(animationTemp1);
        animComeBack.addAnimation(animationTemp2);

        //Using for all challenge
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        animScale.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animScale.setAnimationListener(animScaleListener);

        //Funcion for Normal Challenge
        isFirstChallenges = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_CHALLENGES, true);

        //Function for Run Challenge
        format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);

        //Tooltip
        isShowToolTip = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_TOOLTIP_CHALLENGE, true);

        initChallenge();
    }

    private void initChallenge() {
        int type = pre.getInt(PreferenceManager.CHALLENGE_TYPE, CHALLENGE_TYPE_DRINK_WATER);
        if (type == CHALLENGE_TYPE_DRINK_WATER) {
            int total = pre.getInt(PreferenceManager.DRINK_WATER_TOTAL_ITEMS, 8);
            int currentPosition = 0;
            long time = pre.getLong(PreferenceManager.DRINK_WATER_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.DRINK_WATER_CURRENT_POSITION, 0);
            }
            challenge = new NormalChallenge(R.drawable.challenge_water_full,
                    getString(R.string.drink_more_water),
                    Constants.STARS_FOR_DRINK_WATER,
                    total,
                    currentPosition,
                    getString(R.string.glasses),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_PUSH_UP) {
            int total = pre.getInt(PreferenceManager.PUSH_UP_TOTAL_ITEMS, 8);
            long time = pre.getLong(PreferenceManager.PUSH_UP_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.PUSH_UP_CURRENT_POSITION, 0);
            }
            challenge = new NormalChallenge(R.drawable.challenges_pushups01_sh,
                    getString(R.string.push_up_challenge_title),
                    Constants.STARS_FOR_PUSH_UP,
                    total,
                    currentPosition,
                    getString(R.string.sets),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_GYM) {
            int total = pre.getInt(PreferenceManager.GYM_EXERCISE_TOTAL_ITEMS, 8);
            long time = pre.getLong(PreferenceManager.GYM_EXERCISE_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.GYM_EXERCISE_CURRENT_POSITION, 0);
            }
            challenge = new NormalChallenge(R.drawable.challenges_gym1,
                    getString(R.string.gym_challenge_title),
                    Constants.STARS_FOR_GYM_EXERCISE,
                    total,
                    currentPosition,
                    getString(R.string.times),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_OF_MY) {
            int total = pre.getInt(PreferenceManager.MY_CHALLENGE_TOTAL_ITEMS, 3);
            long time = pre.getLong(PreferenceManager.MY_CHALLENGE_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.MY_CHALLENGE_CURRENT_POSITION, 0);
            }
            String title = pre.getString(PreferenceManager.MY_CHALLENGE_TITLE, getString(R.string.title));
            challenge = new NormalChallenge(R.drawable.challenges_general_before,
                    title,
                    Constants.STARS_FOR_MY_CHALLENGE,
                    total,
                    currentPosition,
                    getString(R.string.times),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_FILL_MY_PLATE) {
            int total = pre.getInt(PreferenceManager.FILL_MY_PLATE_TOTAL_ITEMS, 8);
            long time = pre.getLong(PreferenceManager.FILL_MY_PLATE_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.FILL_MY_PLATE_CURRENT_POSITION, 0);
            }
            challenge = new AnimationChallenge(R.drawable.challenges_portion2,
                    getString(R.string.fill_my_plate),
                    Constants.STARS_FOR_FILL_MY_PLATE,
                    total,
                    currentPosition,
                    getString(R.string.meals),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_WALK_A_MILE) {
            double total = pre.getFloat(PreferenceManager.RUN_CHALLENGE_TOTAL_LENGTH, 2);
            long time = pre.getLong(PreferenceManager.RUN_CHALLENGE_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            double currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getFloat(PreferenceManager.RUN_CHALLENGE_CURRENT_POSITION, 0);
            }
            challenge = new RunChallenge(R.drawable.challenges_walk1_sh,
                    getString(R.string.walk_2_miles),
                    Constants.STARS_FOR_WALK_A_MILE,
                    total,
                    currentPosition,
                    getString(R.string.miles),
                    0.01,
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_AVOID_JUNK_FOOD) {
            long time = pre.getLong(PreferenceManager.AVOID_JUNK_FOOD_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.AVOID_JUNK_FOOD_CURRENT_POSITION, 0);
            }
            challenge = new SelfControlChallenge(R.drawable.junk_food_avoid,
                    getString(R.string.avoid_junk_food),
                    currentPosition,
                    Constants.STARS_FOR_AVOID_JUNK_FOOD,
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_AVOID_SUGARY_DRINK) {
            long time = pre.getLong(PreferenceManager.AVOID_SUGARY_DRINK_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.AVOID_SUGARY_DRINK_CURRENT_POSITION, 0);
            }
            challenge = new SelfControlChallenge(R.drawable.sugray_drink,
                    getString(R.string.avoid_sugary_drinks),
                    currentPosition,
                    Constants.STARS_FOR_AVOID_SURGARY_DRINKS,
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_AVOID_SNACKING) {
            long time = pre.getLong(PreferenceManager.AVOID_SNACKING_LAST_TIME, 0);
            Calendar lastTime = new GregorianCalendar();
            lastTime.setTimeInMillis(time);
            boolean isToday = lastTimeUsingisToday(lastTime);
            int currentPosition = 0;
            if (isToday) {
                currentPosition = pre.getInt(PreferenceManager.AVOID_SNACKING_CURRENT_POSITION, 0);
            }
            challenge = new SelfControlChallenge(R.drawable.snack,
                    getString(R.string.avoid_snacking),
                    currentPosition,
                    Constants.STARS_FOR_AVOID_SNACKING,
                    type);
        }
    }

    /**
     * Init view
     */
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.track_challenges);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_challenges);

        ButterKnife.bind(this);

        //DrawingView
        drawingView = findViewById(R.id.drawingView);
        drawingView.setDrawingListener(listener);

        //Guide layout
        flGuide = findViewById(R.id.flGuide);
        llGuideTop = findViewById(R.id.llGuideTop);
        llGuideBottom = findViewById(R.id.llGuideBottom);
        ivArrowOption = findViewById(R.id.ivArrowOption);
        llGuideOption = findViewById(R.id.llGuideOption);
        if (isFirstChallenges) flGuide.setVisibility(View.VISIBLE);

        //Guide tooltip
        rootLayout = findViewById(R.id.rootLayout);


        btnUndo = findViewById(R.id.btnUndo);
        tvPoint = findViewById(R.id.tvPoint);
        llPoint = findViewById(R.id.llPoint);
        tvTitleChallenge = findViewById(R.id.tvTitleChallenge);

        //Funcion for Normal Challenge
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, Constants.COLUMN_GLASSES_COUNT);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Funcion for Run Challenge
        rlHumanPosition = findViewById(R.id.rlHumanPosition);
        llHumanPosition = findViewById(R.id.llHumanPosition);
        tvHumanPosition = findViewById(R.id.tvHumanPosision);
        ivHumanPosition = findViewById(R.id.ivHumanPosition);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(SEEKBAR_MAX_VALUE);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setOnTouchListener(seekbarListener);

        //Self control challenge
        llSelfControlChallenge = findViewById(R.id.llSelfControlChallenge);
        llHumanShadow = findViewById(R.id.llHumanShadow);
        ivAvoidHuman = findViewById(R.id.ivAvoidHuman);
        ivAvoidFoodIcon = findViewById(R.id.ivAvoidFoodIcon);
        ivAvoidFoodAnimation = findViewById(R.id.ivAvoidFoodAnimation);
        ivSelfControlSign = findViewById(R.id.ivSelfControlSign);
    }

    /**
     * Click stop button
     * After click show guideline layout
     */
    @OnClick(R.id.btnStop)
    public void clickButtonStop() {
        ivArrowOption.setRotation(0);
        llGuideOption.setVisibility(View.GONE);
        flGuide.setVisibility(View.VISIBLE);
        flGuide.startAnimation(animComeBack);
    }

    /**
     * Check last time using this challenge is today?
     *
     * @return true if last time is today, false if otherwise
     */
    private boolean lastTimeUsingisToday(Calendar lastDay) {
        Calendar today = Calendar.getInstance();
        return (lastDay.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
                && lastDay.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && lastDay.get(Calendar.YEAR) == today.get(Calendar.YEAR));
    }

    /**
     * Update UI for challenge
     */
    private void updateChallengeUI() {
        Log.e(TAG, "Challenge type: " + challenge.getClass().getSimpleName());

        tvTitleChallenge.setText(challenge.getTitle());

        if (challenge instanceof NormalChallenge) {
            //Show recycler view
            recyclerView.setVisibility(View.VISIBLE);

            //Hide human layout, self control layout
            rlHumanPosition.setVisibility(View.GONE);
            llSelfControlChallenge.setVisibility(View.GONE);

            updateNormalChallenge((NormalChallenge) challenge);
        } else if (challenge instanceof RunChallenge) {
            //Hide recycler view, self control layout
            recyclerView.setVisibility(View.GONE);
            llSelfControlChallenge.setVisibility(View.GONE);

            //Show human layout
            rlHumanPosition.setVisibility(View.VISIBLE);

            RunChallenge rc = (RunChallenge) challenge;
            //Update text view
            double current = rc.getCurrentPosition();
            double total = rc.getTotalLength();
            int progress = (int) (current / total * SEEKBAR_MAX_VALUE);
            seekBar.setProgress(progress);
        } else if (challenge instanceof SelfControlChallenge) {
            //Hide recycler view, human layout
            recyclerView.setVisibility(View.GONE);
            rlHumanPosition.setVisibility(View.GONE);

            //Show self control layout
            llSelfControlChallenge.setVisibility(View.VISIBLE);

            ivAvoidFoodIcon.setImageResource(challenge.getImageId());
            ivAvoidFoodAnimation.setImageResource(challenge.getImageId());

            updateSelfControlChallenge((SelfControlChallenge) challenge);
        }

        updatePoint();
    }

    /**
     * Update point of user
     */
    private void updatePoint() {
        if (challenge instanceof NormalChallenge) {
            int numItems = adapter.getItemCount();
            int numCurrentPosition = adapter.getCurentPosition();
            int numLeft = numItems - numCurrentPosition;
            String string = getString(R.string.earned) + " " + numCurrentPosition * challenge.getStars()
                    + " " + getString(R.string.hh_points) + " " + numLeft
                    + " " + ((NormalChallenge) challenge).getUnit() + " " + getString(R.string.left);
            tvPoint.setText(string);
        } else if (challenge instanceof RunChallenge) {
            RunChallenge rc = (RunChallenge) challenge;
            int stars = (int) (rc.getCurrentPosition() / rc.getTotalLength() * rc.getStars());
            double lengthLeft = rc.getTotalLength() - rc.getCurrentPosition();
            String string = getString(R.string.earned) + " " + stars
                    + " " + getString(R.string.hh_points) + " " + format.format(lengthLeft)
                    + " " + rc.getUnit() + " " + getString(R.string.left);
            tvPoint.setText(string);
        } else if (challenge instanceof SelfControlChallenge) {
            SelfControlChallenge scc = (SelfControlChallenge) challenge;

            //Set icon for self control sign
            if (scc.getCurrentPosition() == Constants.SELF_CONTROL_CHALLENGE_TOTAL_ITEMS) {
                ivSelfControlSign.setImageResource(R.drawable.victory_sign);
                ivAvoidHuman.setEnabled(false);
            } else {
                ivSelfControlSign.setImageResource(R.drawable.stop_sign);
                ivAvoidHuman.setEnabled(true);
            }

            int stars = scc.getCurrentPosition() * scc.getStars() / Constants.SELF_CONTROL_CHALLENGE_TOTAL_ITEMS;
            double left = Constants.SELF_CONTROL_CHALLENGE_TOTAL_ITEMS - scc.getCurrentPosition();
            String string = getString(R.string.earned) + " " + stars
                    + " " + getString(R.string.hh_points) + " " + left
                    + " " + getString(R.string.hours) + " " + getString(R.string.left);
            tvPoint.setText(string);
        }
        llPoint.startAnimation(animScale);
    }

    private Animation.AnimationListener animScaleListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            llPoint.clearAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    //Listener when first click ribbon (ruy băng)
    DrawingView.OnDrawingListener listener = new DrawingView.OnDrawingListener() {
        @Override
        public void onDrawing() {
            drawingView.clearAll();
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_CHALLENGES, false);
            llGuideTop.startAnimation(animTranslateTop);
            llGuideBottom.startAnimation(animTranslateBottom);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    llGuideTop.clearAnimation();
                    llGuideBottom.clearAnimation();
                    flGuide.clearAnimation();
                    flGuide.setVisibility(View.GONE);
                    showToolTip();

                }
            }, ANIMATION_CHALLENGE_LENGTH);
        }
    };

    /**
     * Show tool tip if necessary
     */
    private void showToolTip() {
        if (isShowToolTip) {
            int backgroundColor = ContextCompat.getColor(this, R.color.colorGuideline);
            ToolTipsManager toolTipsManager = new ToolTipsManager();

            View menuItem = findViewById(R.id.action_edit);
            ToolTip.Builder builderMenuItem = new ToolTip.Builder(this, menuItem, rootLayout,
                    getString(R.string.edit_challenges), ToolTip.POSITION_BELOW);
            builderMenuItem.setBackgroundColor(backgroundColor);
            toolTipsManager.show(builderMenuItem.build());

            View itemListView = recyclerView.getChildAt(0);
            ToolTip.Builder builderItemListView = new ToolTip.Builder(this, itemListView, rootLayout,
                    getString(R.string.guide_tap_challenge_icon), ToolTip.POSITION_RIGHT_TO);
            builderItemListView.setBackgroundColor(backgroundColor);
            toolTipsManager.show(builderItemListView.build());

            isShowToolTip = false;
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_TOOLTIP_CHALLENGE, false);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EDITTING_CHALLENGE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                challenge = (Challenge) bundle.getSerializable(Constants.DATA_SERIALIZABLE);
                updateChallengeUI();
            }
        }
    }

    @Override
    protected void onStop() {
        int type = challenge.getType();
        pre.putInt(PreferenceManager.CHALLENGE_TYPE, type);
        if (challenge instanceof AnimationChallenge) {
            AnimationChallenge ac = (AnimationChallenge) challenge;
            if (type == Constants.CHALLENGE_TYPE_FILL_MY_PLATE) {
                pre.putInt(PreferenceManager.FILL_MY_PLATE_TOTAL_ITEMS, ac.getTotalCount());
                pre.putInt(PreferenceManager.FILL_MY_PLATE_CURRENT_POSITION, ac.getCurrentPosition());
                pre.putLong(PreferenceManager.FILL_MY_PLATE_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            }
        } else if (challenge instanceof NormalChallenge) {
            NormalChallenge nc = (NormalChallenge) challenge;
            if (type == Constants.CHALLENGE_TYPE_DRINK_WATER) {
                pre.putInt(PreferenceManager.DRINK_WATER_TOTAL_ITEMS, nc.getTotalCount());
                pre.putInt(PreferenceManager.DRINK_WATER_CURRENT_POSITION, nc.getCurrentPosition());
                pre.putLong(PreferenceManager.DRINK_WATER_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            } else if (type == Constants.CHALLENGE_TYPE_GYM) {
                pre.putInt(PreferenceManager.GYM_EXERCISE_TOTAL_ITEMS, nc.getTotalCount());
                pre.putInt(PreferenceManager.GYM_EXERCISE_CURRENT_POSITION, nc.getCurrentPosition());
                pre.putLong(PreferenceManager.GYM_EXERCISE_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            } else if (type == Constants.CHALLENGE_TYPE_PUSH_UP) {
                pre.putInt(PreferenceManager.PUSH_UP_TOTAL_ITEMS, nc.getTotalCount());
                pre.putInt(PreferenceManager.PUSH_UP_CURRENT_POSITION, nc.getCurrentPosition());
                pre.putLong(PreferenceManager.PUSH_UP_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            } else if (type == Constants.CHALLENGE_TYPE_OF_MY) {
                pre.putString(PreferenceManager.MY_CHALLENGE_TITLE, nc.getTitle());
                pre.putInt(PreferenceManager.MY_CHALLENGE_TOTAL_ITEMS, nc.getTotalCount());
                pre.putInt(PreferenceManager.MY_CHALLENGE_CURRENT_POSITION, nc.getCurrentPosition());
                pre.putLong(PreferenceManager.MY_CHALLENGE_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            }
        } else if (challenge instanceof RunChallenge) {
            if (type == Constants.CHALLENGE_TYPE_WALK_A_MILE) {
                pre.putInt(PreferenceManager.CHALLENGE_TYPE, challenge.getType());
                pre.putFloat(PreferenceManager.RUN_CHALLENGE_TOTAL_LENGTH,
                        (float) ((RunChallenge) challenge).getTotalLength());
                pre.putFloat(PreferenceManager.RUN_CHALLENGE_CURRENT_POSITION,
                        (float) ((RunChallenge) challenge).getCurrentPosition());
                pre.putLong(PreferenceManager.RUN_CHALLENGE_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            }
        } else if (challenge instanceof SelfControlChallenge) {
            SelfControlChallenge scc = (SelfControlChallenge) challenge;
            if (type == Constants.CHALLENGE_TYPE_AVOID_JUNK_FOOD) {
                pre.putInt(PreferenceManager.AVOID_JUNK_FOOD_CURRENT_POSITION, scc.getCurrentPosition());
                pre.putLong(PreferenceManager.AVOID_JUNK_FOOD_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            } else if (type == Constants.CHALLENGE_TYPE_AVOID_SUGARY_DRINK) {
                pre.putInt(PreferenceManager.AVOID_SUGARY_DRINK_CURRENT_POSITION, scc.getCurrentPosition());
                pre.putLong(PreferenceManager.AVOID_SUGARY_DRINK_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            } else if (type == Constants.CHALLENGE_TYPE_AVOID_SNACKING) {
                pre.putInt(PreferenceManager.AVOID_SNACKING_CURRENT_POSITION, scc.getCurrentPosition());
                pre.putLong(PreferenceManager.AVOID_SNACKING_LAST_TIME, Calendar.getInstance().getTimeInMillis());
            }
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.challenges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, EdittingChallengeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.DATA_SERIALIZABLE, challenge);
            if (adapter != null && challenge instanceof NormalChallenge) {
                NormalChallenge normalChallenge = (NormalChallenge) challenge;
                normalChallenge.setTotalCount(adapter.getItemCount());
            }
            intent.putExtras(bundle);
            startActivityForResult(intent, RC_EDITTING_CHALLENGE);
        }

        return super.onOptionsItemSelected(item);
    }

    //Show/hiden guideline option
    @OnClick(R.id.ivArrowOption)
    public void clickArrowOption() {
        if (llGuideOption.getVisibility() == View.VISIBLE) {
            llGuideOption.setVisibility(View.GONE);
            ivArrowOption.setRotation(0);
        } else {
            llGuideOption.setVisibility(View.VISIBLE);
            ivArrowOption.setRotation(180);
        }
    }

    /**
     * Normal challenge
     */

    /**
     * Update UI for Normal Challenge
     *
     * @param challenge: Normal Challenge
     */
    private void updateNormalChallenge(NormalChallenge challenge) {
        adapter = new ChallengesAdapter(this, challenge, challenge.getTotalCount());
        adapter.setOnItemEventListener(this);
        adapter.setNumCurrentPosition(challenge.getCurrentPosition());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void startClick() {
        btnUndo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void itemViewDone() {
        btnUndo.setVisibility(View.VISIBLE);
        new Handler().postDelayed(undoRunnable, 3000);
        updatePoint();
    }

    Runnable undoRunnable = new Runnable() {
        @Override
        public void run() {
            btnUndo.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void allItemDone() {
        tvTitleChallenge.setPaintFlags(tvTitleChallenge.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * Back to previous item from recycler view
     */
    @OnClick(R.id.btnUndo)
    public void clickUndo() {
        btnUndo.setVisibility(View.INVISIBLE);
        if (adapter.isAllItemDone())
            tvTitleChallenge.setPaintFlags(tvTitleChallenge.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        adapter.backToPreviousItem();
    }

    /**
     * RunChallenge
     */

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) updateChallengeUI();
    }

    /**
     * SeekBar TouchListener
     * Does not allow change progress when it already finish
     */
    View.OnTouchListener seekbarListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (challenge instanceof RunChallenge) {
                    RunChallenge rc = (RunChallenge) challenge;
                    return rc.getTotalLength() == rc.getCurrentPosition();
                }
            }
            return false;
        }
    };

    /**
     * Change image continuous like walking
     */
    private void changeImageLikeWalking() {
        if (isContinue) {
            if (currentImageId == R.drawable.challenges_walk1_sh) {
                currentImageId = R.drawable.challenges_walk2_sh;
            } else {
                currentImageId = R.drawable.challenges_walk1_sh;
            }
            ivHumanPosition.setImageResource(currentImageId);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeImageLikeWalking();
                }
            }, 200);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (challenge instanceof RunChallenge) {
            double percent = (double) i / SEEKBAR_MAX_VALUE;
            double current = percent * ((RunChallenge) challenge).getTotalLength();
            ((RunChallenge) challenge).setCurrentPosition(current);
            updateHumanPosition();
        }
    }

    /**
     * Update current position for human
     */
    private void updateHumanPosition() {
        if (challenge instanceof RunChallenge) {
            RunChallenge rc = (RunChallenge) challenge;
            tvHumanPosition.setText(format.format(rc.getCurrentPosition()));
            double percent = rc.getCurrentPosition() / rc.getTotalLength();
            Log.e(TAG, "Current position: " + rc.getCurrentPosition());
            Log.e(TAG, "Percent of seekbar: " + percent);
            int left = seekBar.getLeft();
            int right = seekBar.getRight();
            Log.e(TAG, "Seek bar left: " + left);
            Log.e(TAG, "Seek bar right: " + right);
            float x = (float) (left + percent * (right - left));
            Log.e(TAG, "Human layout new position: " + x);

            llHumanPosition.setTranslationX(x);
        }
    }

    //Continuous change image like animation
    boolean isContinue;
    int currentImageId = R.drawable.challenges_walk1_sh;

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isContinue = true;
        changeImageLikeWalking();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isContinue = false;
        updatePoint();
    }

    /*
      SelfControlChallenge
     */


    /**
     * Update UI for SelfControlChallenge
     *
     * @param challenge: SelfControlChallenge
     */
    private void updateSelfControlChallenge(SelfControlChallenge challenge) {
        //Set width for shaddow
        int width = ivAvoidHuman.getWidth();
        int height = ivAvoidHuman.getHeight();
        int left = Constants.SELF_CONTROL_CHALLENGE_TOTAL_ITEMS - challenge.getCurrentPosition();
        //New width for shaddow
        int nwidth = width * left / Constants.SELF_CONTROL_CHALLENGE_TOTAL_ITEMS;
        llHumanShadow.setLayoutParams(new FrameLayout.LayoutParams(nwidth, height));

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_top);
        animation.setDuration(Constants.ANIMATION_LENGTH);
        ivAvoidFoodAnimation.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updatePoint();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Click on Avoid Human
     */
    @OnClick(R.id.ivAvoidHuman)
    void clickAvoidHuman() {
        if (challenge instanceof SelfControlChallenge) {
            SelfControlChallenge scc = (SelfControlChallenge) challenge;
            int currentPosition = scc.getCurrentPosition();
            scc.setCurrentPosition(currentPosition + 1);
            updateSelfControlChallenge((SelfControlChallenge) challenge);
        }
    }


}
