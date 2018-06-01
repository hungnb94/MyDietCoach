package com.hb.mydietcoach.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.adapter.ChallengesAdapter;
import com.hb.mydietcoach.custom_view.DrawingView;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChallengesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = ChallengesActivity.class.getSimpleName();
    private static final long ANIMATION_CHALLENGE_LENGTH = 2000;

    private DrawerLayout drawer;
    private NavigationView navigationView;

    //Function guide
    private DrawingView drawingView;
    private FrameLayout flGuide;
    private LinearLayout llGuideTop, llGuideBottom;
    private Animation animTranslateTop, animTranslateBottom, animTranslateTopBack;
    AnimationSet animComeBack;
    private ImageView ivArrowOption;
    private LinearLayout llGuideOption;
    private boolean isFirstChallenges;

    //Function for challenges drink water
    private RecyclerView recyclerView;
    private ChallengesAdapter adapter;
    private int numGlasses;
    private PreferenceManager pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        //Funcion guideline
        animTranslateTop = AnimationUtils.loadAnimation(this, R.anim.translate_top);
        animTranslateTop.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animTranslateTop.setFillAfter(true);
        animTranslateBottom = AnimationUtils.loadAnimation(this, R.anim.translate_bottom);
        animTranslateBottom.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animTranslateBottom.setFillAfter(true);
        animTranslateTopBack = AnimationUtils.loadAnimation(this, R.anim.translate_top_back);
        animTranslateTopBack.setDuration(ANIMATION_CHALLENGE_LENGTH);
        animComeBack = new AnimationSet(false);
        animComeBack.addAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_top_back));
        animComeBack.addAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_bottom_back));

        //Funcion for challenge drink water
        pre = new PreferenceManager(this);
        numGlasses = pre.getInt(PreferenceManager.NUMBLE_GLASSES, Constants.DEFAULT_NUMBER_GLASSES);
        isFirstChallenges = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_CHALLENGES, true);

        initView();
    }

    /**
     * Init view
     */
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.track_challenges);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        if (isFirstChallenges) llGuideOption.setVisibility(View.VISIBLE);

        //Funcion for challenge drink water
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ChallengesAdapter(this, numGlasses);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, Constants.COLUMN_GLASSES_COUNT);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
                }
            }, ANIMATION_CHALLENGE_LENGTH);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Show/hiden guideline option
    @OnClick(R.id.ivArrowOption)
    void clickArrowOption(View view) {
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
     * @param view
     */
    @OnClick(R.id.btnStop)
    void clickButtonStop(View view) {
        ivArrowOption.setRotation(0);
        llGuideOption.setVisibility(View.GONE);
        flGuide.setVisibility(View.VISIBLE);
        flGuide.startAnimation(animComeBack);
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

        } else if (id == R.id.nav_reminder) {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_photos) {

        } else if (id == R.id.nav_tips) {

        } else if (id == R.id.nav_challenges) {
            Intent intent = new Intent(this, ChallengesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_rewards) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
