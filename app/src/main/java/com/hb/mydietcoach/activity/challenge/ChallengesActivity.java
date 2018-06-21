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

        //Funcion for Normal Challenge
        isFirstChallenges = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_CHALLENGES, true);
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        animScale.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animScale.setAnimationListener(animScaleListener);

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
            int total = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_TOTAL_ITEMS, 8);
            int currentPosition = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_CURRENT_POSITION, 0);
            challenge = new NormalChallenge(R.drawable.challenge_water_full,
                    getString(R.string.drink_more_water),
                    Constants.STARS_FOR_DRINK_WATER,
                    total,
                    currentPosition,
                    getString(R.string.glasses),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_PUSH_UP) {
            int total = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_TOTAL_ITEMS, 8);
            int currentPosition = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_CURRENT_POSITION, 0);
            challenge = new NormalChallenge(R.drawable.challenges_pushups01_sh,
                    getString(R.string.push_up_challenge_title),
                    Constants.STARS_FOR_PUSH_UP,
                    total,
                    currentPosition,
                    getString(R.string.sets),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_GYM) {
            int total = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_TOTAL_ITEMS, 8);
            int currentPosition = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_CURRENT_POSITION, 0);
            challenge = new NormalChallenge(R.drawable.challenges_gym1,
                    getString(R.string.gym_challenge_title),
                    Constants.STARS_FOR_GYM_EXERCISE,
                    total,
                    currentPosition,
                    getString(R.string.times),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_OF_MY) {
            int total = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_TOTAL_ITEMS, 3);
            int currentPosition = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_CURRENT_POSITION, 0);
            String title = pre.getString(PreferenceManager.MY_CHALLENGE_TITLE, getString(R.string.title));
            challenge = new NormalChallenge(R.drawable.challenges_general_before,
                    title,
                    Constants.STARS_FOR_MY_CHALLENGE,
                    total,
                    currentPosition,
                    getString(R.string.times),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_FILL_MY_PLATE) {
            int total = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_TOTAL_ITEMS, 8);
            int currentPosition = pre.getInt(PreferenceManager.NORMAL_CHALLENGE_CURRENT_POSITION, 0);
            challenge = new AnimationChallenge(R.drawable.challenges_portion2,
                    getString(R.string.fill_my_plate),
                    Constants.STARS_FOR_FILL_MY_PLATE,
                    total,
                    currentPosition,
                    getString(R.string.meals),
                    type);
        } else if (type == Constants.CHALLENGE_TYPE_WALK_A_MILE) {
            double total = pre.getFloat(PreferenceManager.RUN_CHALLENGE_TOTAL_LENGTH, 2);
            double currentPosition = pre.getFloat(PreferenceManager.RUN_CHALLENGE_CURRENT_POSITION, 0);
            challenge = new RunChallenge(R.drawable.challenges_walk1_sh,
                    getString(R.string.walk_2_miles),
                    Constants.STARS_FOR_WALK_A_MILE,
                    total,
                    currentPosition,
                    getString(R.string.miles),
                    0.01,
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
    }

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
            float seekBarPosition;

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
     * Update UI for challenge UI only
     */
    private void updateChallengeUI() {
        Log.e(TAG, "Challenge type: " + challenge.getClass().getSimpleName());

        tvTitleChallenge.setText(challenge.getTitle());

        if (challenge instanceof NormalChallenge) {
            //Show recycler view
            recyclerView.setVisibility(View.VISIBLE);

            //Hide human layout
            rlHumanPosition.setVisibility(View.GONE);
            initRecyclerView();
        } else if (challenge instanceof RunChallenge) {
            //Hide recycler view
            recyclerView.setVisibility(View.GONE);

            //Show human layout
            rlHumanPosition.setVisibility(View.VISIBLE);

            RunChallenge rc = (RunChallenge) challenge;
            //Update text view
            double current = rc.getCurrentPosition();
            double total = rc.getTotalLength();
            int progress = (int) (current / total * SEEKBAR_MAX_VALUE);
            seekBar.setProgress(progress);
        }

        updatePoint();
    }

    private void initRecyclerView() {
        if (challenge instanceof NormalChallenge) {
            NormalChallenge nmChallenge = (NormalChallenge) challenge;
            adapter = new ChallengesAdapter(this, challenge, nmChallenge.getTotalCount());
            adapter.setOnItemEventListener(this);

            Calendar lastDrinkedDate = new GregorianCalendar();
            long lastDate = pre.getLong(PreferenceManager.LAST_TIME_USING, Calendar.getInstance().getTimeInMillis());
            lastDrinkedDate.setTimeInMillis(lastDate);
            Calendar todayDate = Calendar.getInstance();
            if (lastDrinkedDate.get(Calendar.DAY_OF_MONTH) == todayDate.get(Calendar.DAY_OF_MONTH)
                    && lastDrinkedDate.get(Calendar.MONTH) == todayDate.get(Calendar.MONTH)
                    && lastDrinkedDate.get(Calendar.YEAR) == todayDate.get(Calendar.YEAR)) {
                adapter.setNumCurrentPosition(nmChallenge.getCurrentPosition());
            }

            recyclerView.setAdapter(adapter);
        }
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

            //Disable scroll seek bar when walk all length
//            if (rc.getCurrentPosition() == rc.getTotalLength()) seekBar.setEnabled(false);
//            else seekBar.setEnabled(true);
        }
        llPoint.startAnimation(animScale);
    }

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (challenge instanceof NormalChallenge) {
            pre.putInt(PreferenceManager.CHALLENGE_TYPE, challenge.getType());
            pre.putInt(PreferenceManager.NORMAL_CHALLENGE_TOTAL_ITEMS,
                    ((NormalChallenge) challenge).getTotalCount());
            pre.putInt(PreferenceManager.NORMAL_CHALLENGE_CURRENT_POSITION,
                    ((NormalChallenge) challenge).getCurrentPosition());
            pre.putLong(PreferenceManager.LAST_TIME_USING, Calendar.getInstance().getTimeInMillis());
        } else if (challenge instanceof RunChallenge) {
            pre.putInt(PreferenceManager.CHALLENGE_TYPE, challenge.getType());
            pre.putFloat(PreferenceManager.RUN_CHALLENGE_TOTAL_LENGTH,
                    (float) ((RunChallenge) challenge).getTotalLength());
            pre.putFloat(PreferenceManager.RUN_CHALLENGE_CURRENT_POSITION,
                    (float) ((RunChallenge) challenge).getCurrentPosition());
            pre.putLong(PreferenceManager.LAST_TIME_USING, Calendar.getInstance().getTimeInMillis());
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
    public void clickArrowOption(View view) {
        if (llGuideOption.getVisibility() == View.VISIBLE) {
            llGuideOption.setVisibility(View.GONE);
            ivArrowOption.setRotation(0);
        } else {
            llGuideOption.setVisibility(View.VISIBLE);
            ivArrowOption.setRotation(180);
        }
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

    @OnClick(R.id.btnUndo)
    public void clickUndo() {
        btnUndo.setVisibility(View.INVISIBLE);
        if (adapter.isAllItemDone())
            tvTitleChallenge.setPaintFlags(tvTitleChallenge.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        adapter.backToPreviousItem();
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

    @Override
    public void startClick() {
        btnUndo.setVisibility(View.INVISIBLE);
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

    Runnable undoRunnable = new Runnable() {
        @Override
        public void run() {
            btnUndo.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void itemViewDone() {
        btnUndo.setVisibility(View.VISIBLE);
        new Handler().postDelayed(undoRunnable, 3000);
        updatePoint();
    }

    @Override
    public void allItemDone() {
        tvTitleChallenge.setPaintFlags(tvTitleChallenge.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
}
