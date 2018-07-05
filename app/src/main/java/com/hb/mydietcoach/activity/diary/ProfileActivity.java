package com.hb.mydietcoach.activity.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.activity.ContactFAQActivity;
import com.hb.mydietcoach.activity.MainActivity;
import com.hb.mydietcoach.activity.SettingsActivity;
import com.hb.mydietcoach.activity.WeightLoggingActivity;
import com.hb.mydietcoach.activity.challenge.ChallengesActivity;
import com.hb.mydietcoach.activity.photo.PhotosActivity;
import com.hb.mydietcoach.activity.reminder.ReminderActivity;
import com.hb.mydietcoach.activity.tip.TipsActivity;
import com.hb.mydietcoach.adapter.diary.ActivityLevelAdapter;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.BMIUtils;
import com.hb.mydietcoach.utils.Constants;

import java.text.NumberFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private DrawerLayout drawer;

    private boolean isFirstTime;    //First time open diary activity

    private EditText edtCaloriesGoal;
    private TextView tvBmi;
    private Spinner spWeightHeightUnit, spGender;
    private EditText edtAge, edtHeight, edtCurrentWeight, edtTargetWeight;
    private Spinner spActivityLevel;
    private EditText edtWeekyWeightLossGoal;
    private Spinner spUnitMeasurement;
    //WeightHeightUnit
    private int weightHeightUnit;
    private EditText edtHeightFt, edtHeightIn;
    private NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
        addEvent();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);

        edtCaloriesGoal = findViewById(R.id.edtCaloriesGoal);
        tvBmi = findViewById(R.id.tvBMI);

        spWeightHeightUnit = findViewById(R.id.spWeightHeightUnit);
        spWeightHeightUnit.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.weight_height_units)));

        spGender = findViewById(R.id.spGender);
        spGender.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.gender_types)));

        edtAge = findViewById(R.id.edtAge);
        edtHeight = findViewById(R.id.edtHeight);
        edtHeightFt = findViewById(R.id.edtHeightFt);
        edtHeightIn = findViewById(R.id.edtHeightIn);
        edtCurrentWeight = findViewById(R.id.edtCurrentWeight);
        edtTargetWeight = findViewById(R.id.edtWeightGoal);

        spActivityLevel = findViewById(R.id.spActivityLevel);
        spActivityLevel.setAdapter(new ActivityLevelAdapter(this,
                getResources().getStringArray(R.array.activity_level_titles),
                getResources().getStringArray(R.array.activity_level_details)));
        edtWeekyWeightLossGoal = findViewById(R.id.edtWeekyWeightLossGoal);

        spUnitMeasurement = findViewById(R.id.spUnitMeasurement);
        spUnitMeasurement.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.unit_measurements)));

        getDataAndUpdateView();
    }

    private void getDataAndUpdateView() {
        //First time open app
        isFirstTime = getIntent().getBooleanExtra(PreferenceManager.IS_FIRST_TIME_OPEN_DIARY, false);

        PreferenceManager pre = new PreferenceManager(this);
        float height = pre.getFloat(PreferenceManager.USER_HEIGHT, 160);
        float weight = pre.getFloat(PreferenceManager.CURRENT_WEIGHT, 88);

        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);

        //BMI index
        float bmi = (float) BMIUtils.getBMI(weight, height / 100);
        tvBmi.setText(numberFormat.format(bmi));

        //Gender
        boolean isFemale = pre.getBoolean(PreferenceManager.IS_GENDER_FEMALE, true);
        if (!isFemale) spGender.setSelection(1);

        if (isFirstTime) {
            findViewById(R.id.llWeightHeightUnit).setVisibility(View.GONE);
            findViewById(R.id.llGender).setVisibility(View.GONE);
            findViewById(R.id.llWeightGoal).setVisibility(View.GONE);
            findViewById(R.id.llUnitMeasurement).setVisibility(View.GONE);
        } else {
            int dailyCaloriesGoal = pre.getInt(PreferenceManager.DAILY_CALORIES_GOAL, 1550);
            if (dailyCaloriesGoal > 0) edtCaloriesGoal.setText(String.valueOf(dailyCaloriesGoal));

            weightHeightUnit = pre.getInt(PreferenceManager.WEIGHT_HEIGHT_UNIT, 0);
            spWeightHeightUnit.setSelection(weightHeightUnit);

            int birthYear = pre.getInt(PreferenceManager.USER_BIRTH_YEAR, -1);
            int age = Calendar.getInstance().get(Calendar.YEAR) - birthYear;
            if (ageIsValid(age)) {
                edtAge.setText(String.valueOf(age));
            }

            int unitMeasurement = pre.getInt(PreferenceManager.UNIT_MEASUREMENT, 0);
            spUnitMeasurement.setSelection(unitMeasurement);

            int activityLevel = pre.getInt(PreferenceManager.ACTIVITY_LEVEL, 0);
            spActivityLevel.setSelection(activityLevel);

            float weightGoal = pre.getFloat(PreferenceManager.TARGET_WEIGHT, height - 100);

            //Convert data to kg and inch if select type is lbs/in

            if (weightHeightUnit == 1) {
                weight = (float) (weight * Constants.KG_TO_LB);
                edtCurrentWeight.setText(numberFormat.format(weight));

                weightGoal = (float) (weightGoal * Constants.KG_TO_LB);
                edtTargetWeight.setText(numberFormat.format(weightGoal));

                edtHeight.setVisibility(View.GONE);
                findViewById(R.id.llHeightIn).setVisibility(View.VISIBLE);

                int foot = (int) (height * Constants.CM_TO_FT);
                edtHeightFt.setText(String.valueOf(foot));

                height = (float) (height - foot * Constants.FT_TO_CM);
                float inch = (float) (height * Constants.CM_TO_IN);

                edtHeightIn.setText(numberFormat.format(inch));
            } else {
                edtHeight.setText(numberFormat.format(height));

                edtCurrentWeight.setText(numberFormat.format(weight));

                edtTargetWeight.setText(numberFormat.format(weightGoal));

                float weightLossGoal = pre.getFloat(PreferenceManager.WEEKY_WEIGHT_LOSS_GOAL, 0.3f);
                edtWeekyWeightLossGoal.setText(numberFormat.format(weightLossGoal));
            }
        }
    }

    private void addEvent() {
        spWeightHeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != weightHeightUnit) {
                    weightHeightUnit = position;

                    //Select kg and cm
                    if (position == 0) {
                        findViewById(R.id.llHeightIn).setVisibility(View.GONE);
                        edtHeight.setVisibility(View.VISIBLE);
                        float heightFt = getFloatFromEdittext(edtHeightFt);
                        float heightIn = getFloatFromEdittext(edtHeightIn);
                        float height = (float) (heightFt * Constants.FT_TO_CM + heightIn * Constants.IN_TO_CM);
                        edtHeight.setText(numberFormat.format(height));

                        float currWeight = getFloatFromEdittext(edtCurrentWeight);
                        currWeight = (float) (currWeight * Constants.LB_TO_KG);
                        edtCurrentWeight.setText(numberFormat.format(currWeight));

                        float targetWeight = getFloatFromEdittext(edtTargetWeight);
                        targetWeight = (float) (targetWeight * Constants.LB_TO_KG);
                        edtTargetWeight.setText(numberFormat.format(targetWeight));

                        float weekyWeightLossGoal = getFloatFromEdittext(edtWeekyWeightLossGoal);
                        weekyWeightLossGoal *= Constants.LB_TO_KG;
                        edtWeekyWeightLossGoal.setText(numberFormat.format(weekyWeightLossGoal));
                    } else {
                        findViewById(R.id.llHeightIn).setVisibility(View.VISIBLE);
                        edtHeight.setVisibility(View.GONE);

                        float height = getFloatFromEdittext(edtHeight);
                        int heightFt = (int) (height * Constants.CM_TO_FT);
                        edtHeightFt.setText(String.valueOf(heightFt));

                        float heightIn = (float) ((height - heightFt * Constants.FT_TO_CM) * Constants.CM_TO_IN);
                        edtHeightIn.setText(numberFormat.format(heightIn));

                        float currWeight = getFloatFromEdittext(edtCurrentWeight);
                        currWeight = (float) (currWeight * Constants.KG_TO_LB);
                        edtCurrentWeight.setText(numberFormat.format(currWeight));

                        float targetWeight = getFloatFromEdittext(edtTargetWeight);
                        targetWeight = (float) (targetWeight * Constants.KG_TO_LB);
                        edtTargetWeight.setText(numberFormat.format(targetWeight));

                        float weekyWeightLossGoal = getFloatFromEdittext(edtWeekyWeightLossGoal);
                        weekyWeightLossGoal *= Constants.KG_TO_LB;
                        edtWeekyWeightLossGoal.setText(numberFormat.format(weekyWeightLossGoal));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager pre = new PreferenceManager(this);

        //Save all field to preferences
        if (isFirstTime) {
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_OPEN_DIARY, false);

            int dailyCalories = (int) getFloatFromEdittext(edtCaloriesGoal);
            if (dailyCalories > 0) pre.putInt(PreferenceManager.DAILY_CALORIES_GOAL, dailyCalories);

            int age = (int) getFloatFromEdittext(edtAge);
            if (ageIsValid(age)) {
                int year = Calendar.getInstance().get(Calendar.YEAR) - age;
                pre.putInt(PreferenceManager.USER_BIRTH_YEAR, year);
            }

            float height = getFloatFromEdittext(edtHeight);
            if (height > 0) pre.putFloat(PreferenceManager.USER_HEIGHT, height);

            float weight = getFloatFromEdittext(edtCurrentWeight);
            if (weight > 0) pre.putFloat(PreferenceManager.CURRENT_WEIGHT, weight);

            int activityLevel = spActivityLevel.getSelectedItemPosition();
            pre.putInt(PreferenceManager.ACTIVITY_LEVEL, activityLevel);

            float weekyWeightLossGoal = getFloatFromEdittext(edtWeekyWeightLossGoal);
            if (weekyWeightLossGoal >= 0)
                pre.putFloat(PreferenceManager.WEEKY_WEIGHT_LOSS_GOAL, weekyWeightLossGoal);
        } else {
            int dailyCalories = (int) getFloatFromEdittext(edtCaloriesGoal);
            if (dailyCalories > 0) pre.putInt(PreferenceManager.DAILY_CALORIES_GOAL, dailyCalories);

            pre.putInt(PreferenceManager.WEIGHT_HEIGHT_UNIT, spWeightHeightUnit.getSelectedItemPosition());
            pre.putBoolean(PreferenceManager.IS_GENDER_FEMALE, spGender.getSelectedItemPosition() == 0);

            int age = (int) getFloatFromEdittext(edtAge);
            if (ageIsValid(age)) {
                int year = Calendar.getInstance().get(Calendar.YEAR) - age;
                pre.putInt(PreferenceManager.USER_BIRTH_YEAR, year);
            }

            pre.putInt(PreferenceManager.ACTIVITY_LEVEL, spActivityLevel.getSelectedItemPosition());
            pre.putInt(PreferenceManager.UNIT_MEASUREMENT, spUnitMeasurement.getSelectedItemPosition());

            if (spWeightHeightUnit.getSelectedItemPosition() == 0) {
                float height = getFloatFromEdittext(edtHeight);
                if (height > 0) pre.putFloat(PreferenceManager.USER_HEIGHT, height);

                float curWeight = getFloatFromEdittext(edtCurrentWeight);
                if (curWeight > 0) pre.putFloat(PreferenceManager.CURRENT_WEIGHT, curWeight);

                float tagertWeight = getFloatFromEdittext(edtTargetWeight);
                if (weightGoalIsValild(tagertWeight, height))
                    pre.putFloat(PreferenceManager.TARGET_WEIGHT, tagertWeight);

                float weightLossGoal = getFloatFromEdittext(edtWeekyWeightLossGoal);
                if (weightLossGoal >= 0)
                    pre.putFloat(PreferenceManager.WEEKY_WEIGHT_LOSS_GOAL, weightLossGoal);
            } else {
                float heightFt = getFloatFromEdittext(edtHeightFt);
                float heightIn = getFloatFromEdittext(edtHeightIn);
                float height = 1;
                if (heightFt >= 0 && heightIn >= 0) {
                    height = (float) (heightFt * Constants.FT_TO_CM + heightIn * Constants.IN_TO_CM);
                    if (height > 0) pre.putFloat(PreferenceManager.USER_HEIGHT, height);
                }

                float curWeight = getFloatFromEdittext(edtCurrentWeight);
                curWeight *= Constants.LB_TO_KG;
                if (curWeight > 0) pre.putFloat(PreferenceManager.CURRENT_WEIGHT, curWeight);

                float tagertWeight = getFloatFromEdittext(edtTargetWeight);
                tagertWeight *= Constants.LB_TO_KG;
                if (weightGoalIsValild(tagertWeight, height))
                    pre.putFloat(PreferenceManager.TARGET_WEIGHT, tagertWeight);

                float weightLossGoal = getFloatFromEdittext(edtWeekyWeightLossGoal);
                weightLossGoal *= Constants.LB_TO_KG;
                if (weightLossGoal >= 0)
                    pre.putFloat(PreferenceManager.WEEKY_WEIGHT_LOSS_GOAL, weightLossGoal);
            }
        }
    }

    @OnClick(R.id.button)
    void clickContinue() {
        finish();
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

    private boolean ageIsValid(int age) {
        return (age > 0 && age < 100);
    }

    /**
     * Check weight is valid
     *
     * @param weight: weight kgs
     * @param height: height m
     * @return: true if good, false if otherwise
     */
    private boolean weightGoalIsValild(float weight, float height) {
        float bmi = (float) BMIUtils.getBMI(weight, height / 100);
        return (bmi >= BMIUtils.NORMAL_WEIGHT_LOWER && bmi <= BMIUtils.NORMAL_WEIGHT_UPPER);
    }

    private float getFloatFromEdittext(EditText editText) {
        try {
            float value = Float.parseFloat(editText.getText().toString());
            return value > 0 ? value : 0;
        } catch (Exception ex) {
            Log.e(TAG, "Cannot get number from edittext");
            ex.printStackTrace();
            return 0;
        }
    }
}
