package com.hb.mydietcoach.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.hb.mydietcoach.R;
import com.hb.mydietcoach.database.MyDatabase;
import com.hb.mydietcoach.dialog.MyAlertDialog;
import com.hb.mydietcoach.model.challenge.AnimationChallenge;
import com.hb.mydietcoach.model.challenge.Challenge;
import com.hb.mydietcoach.model.challenge.NormalChallenge;
import com.hb.mydietcoach.model.challenge.RunChallenge;
import com.hb.mydietcoach.model.challenge.SelfControlChallenge;
import com.hb.mydietcoach.model.diary.asset.FoodAssets;
import com.hb.mydietcoach.model.tip.TipCategory;
import com.hb.mydietcoach.model.tip.TipDetail;
import com.hb.mydietcoach.preference.PreferenceManager;
import com.hb.mydietcoach.utils.Constants;
import com.hb.mydietcoach.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class TermConditionActivity extends BaseActivity {

    private static final String TAG = TermConditionActivity.class.getSimpleName();

    private EditText edtWeight;
    private Spinner spinnerWeightType, spinnerGender;
    private CheckBox checkBox;

    private String weightTypes[];
    private String genderTypes[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);

        weightTypes = getResources().getStringArray(R.array.weight_types);
        genderTypes = getResources().getStringArray(R.array.gender_types);

        PreferenceManager pre = new PreferenceManager(this);
        boolean isFirstInsertDatabase = pre.getBoolean(PreferenceManager.IS_FIRST_TIME_INSERT_SQLITE, true);
        //First time run app, insert default challenges to sqlite

        if (isFirstInsertDatabase) {
            insertDefaultData();
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_INSERT_SQLITE, false);
        }

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));

        ButterKnife.bind(this);
        edtWeight = findViewById(R.id.edtWeight);
        spinnerWeightType = findViewById(R.id.spinnerWeightType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, weightTypes);
        spinnerWeightType.setAdapter(adapter);

        spinnerGender = findViewById(R.id.spinnerGender);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, genderTypes);
        spinnerGender.setAdapter(adapter);

        checkBox = findViewById(R.id.checkbox);
    }

    //Click cancel
    @OnClick(R.id.btnCancel)
    void clickCancel() {
        finish();
    }

    //Click agree with terms and conditions
    @OnClick(R.id.btnAgree)
    void clickAgree() {
        if (allParametersAvailable()) {
            int position = spinnerGender.getSelectedItemPosition();
            boolean isFemale = (position == 0);
            String strWeight = edtWeight.getText().toString();
            float weight = Float.valueOf(strWeight);
            if (spinnerWeightType.getSelectedItemPosition()==1){
                weight = changeLbToKg(weight);
            }

            PreferenceManager pre = new PreferenceManager(this);
            pre.putBoolean(PreferenceManager.IS_FIRST_TIME_LAUNCH, false);
            pre.putFloat(PreferenceManager.START_WEIGHT, weight);
            pre.putBoolean(PreferenceManager.IS_GENDER_FEMALE, isFemale);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean allParametersAvailable() {
        try {
            String strWeight = edtWeight.getText().toString();
            if (TextUtils.isEmpty(strWeight)) {
                MyAlertDialog dialog = new MyAlertDialog(this, getString(R.string.enter_your_weight));
                dialog.show();
                return false;
            }
            float weight = Float.valueOf(strWeight);
            if (weight <= 0) {
                MyAlertDialog dialog = new MyAlertDialog(this, getString(R.string.bigger_than_zero));
                dialog.show();
                return false;
            }
            if (!checkBox.isChecked()) {
                MyAlertDialog dialog = new MyAlertDialog(this, getString(R.string.must_accept_terms_conditions));
                dialog.show();
                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private float changeLbToKg(float lbWeight){
        return (float) (lbWeight * Constants.LB_TO_KG);
    }

    private void insertDefaultData() {
        DatabaseTask task = new DatabaseTask();
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        Realm realm;

        @Override
        protected Void doInBackground(Void... voids) {
            realm = Realm.getDefaultInstance();

            insertDefaultChallenges();

            insertDefaultTips();

            insertDefaultFoods();

            realm.close();

            return null;
        }

        private void insertDefaultChallenges() {
            MyDatabase database = new MyDatabase(getApplicationContext());

            //Default challenges
            List<Challenge> list = generateExerciseChallenges();
            list.addAll(generateEatingHabitChallenges());
            list.addAll(generateSelfControlChallenges());

            for (Challenge challenge : list) {
                long id = database.insertChallenge(challenge);
                challenge.setId(id);
            }
        }

        private void insertDefaultTips() {
            List<TipCategory> categories = getDefaultCategories();

            //Insert to realm database
            realm.beginTransaction();
            for (TipCategory tipCategory : categories) {
                realm.insertOrUpdate(tipCategory.getAdvices());
            }
            realm.insertOrUpdate(categories);
            realm.commitTransaction();
        }

        private void insertDefaultFoods() {
            ArrayList<FoodAssets> foodAssets = getDefaultFoods();

            realm.beginTransaction();
            for (FoodAssets asset : foodAssets) {
                realm.insertOrUpdate(asset);
            }
            realm.commitTransaction();
        }

        private ArrayList<FoodAssets> getDefaultFoods() {
            ArrayList<FoodAssets> foodAssets = new ArrayList<>();
            String content = FileUtils.readFileFromAssets(TermConditionActivity.this, "new_food_db_1.txt");
            StringTokenizer tokenizer = new StringTokenizer(content, "\n");
            while (tokenizer.hasMoreTokens()) {
                try {
                    String mLine = tokenizer.nextToken();
                    mLine = mLine.substring(8, mLine.length() - 1);

                    //Convert text to object
                    Gson gson = new Gson();
                    FoodAssets food = gson.fromJson(mLine, FoodAssets.class);

                    //Add to list
                    foodAssets.add(food);
                } catch (Exception e) {
                    Log.e(TAG, "Exception");
                    e.printStackTrace();
                }
            }
            return foodAssets;
        }

        /**
         * Generate default Exercise challenge list
         *
         * @return: default list
         */
        private List<Challenge> generateExerciseChallenges() {
            List<Challenge> list = new ArrayList<>();

            NormalChallenge nc = new NormalChallenge();
            nc.setTotalCount(Constants.DEFAULT_GYM)
                    .setUnit(getString(R.string.times))
                    .setImageId(R.drawable.challenges_gym1)
                    .setTitle(getString(R.string.go_to_the_gym))
                    .setStars(Constants.STARS_FOR_GYM_EXERCISE)
                    .setType(Constants.CHALLENGE_TYPE_GYM);
            list.add(nc);

            nc = new NormalChallenge();
            nc.setTotalCount(Constants.DEFAULT_PUSH_UP)
                    .setUnit(getString(R.string.sets))
                    .setImageId(R.drawable.challenges_pushups01_sh)
                    .setTitle(getString(R.string.push_up_challenge_title))
                    .setStars(Constants.STARS_FOR_PUSH_UP)
                    .setType(Constants.CHALLENGE_TYPE_PUSH_UP);
            list.add(nc);

            RunChallenge rc = new RunChallenge();
            rc.setTotalLength(Constants.DEFAULT_WALK_A_MILE_TOTAL)
                    .setLengthUnit(Constants.DEFAULT_WALK_A_MILE_LENGTH_UNIT)
                    .setUnit(getString(R.string.miles))
                    .setImageId(R.drawable.challenges_walk1_sh)
                    .setTitle(getString(R.string.walk_2_miles))
                    .setStars(Constants.STARS_FOR_WALK_A_MILE)
                    .setType(Constants.CHALLENGE_TYPE_WALK_A_MILE);
            list.add(rc);

            return list;
        }

        /**
         * Generate default Eating Habit challenge list
         *
         * @return: default list
         */
        private List<Challenge> generateEatingHabitChallenges() {
            List<Challenge> list = new ArrayList<>();

            NormalChallenge nc = new NormalChallenge();
            nc.setTotalCount(Constants.DEFAULT_DRINK_WATER)
                    .setUnit(getString(R.string.glasses))
                    .setImageId(R.drawable.challenge_water_full)
                    .setTitle(getString(R.string.drink_more_water))
                    .setStars(Constants.STARS_FOR_DRINK_WATER)
                    .setType(Constants.CHALLENGE_TYPE_DRINK_WATER);
            list.add(nc);

            AnimationChallenge ac = new AnimationChallenge();
            ac.setTotalCount(Constants.DEFAULT_FILL_MY_PLATE)
                    .setUnit(getString(R.string.meals))
                    .setImageId(R.drawable.challenges_table_plate)
                    .setTitle(getString(R.string.fill_my_plate))
                    .setStars(Constants.STARS_FOR_FILL_MY_PLATE)
                    .setType(Constants.CHALLENGE_TYPE_FILL_MY_PLATE);
            list.add(ac);

            return list;
        }

        /**
         * Generate default Self Control challenge list
         *
         * @return: default list
         */
        private List<Challenge> generateSelfControlChallenges() {
            List<Challenge> list = new ArrayList<>();

            SelfControlChallenge scc = new SelfControlChallenge();
            scc.setImageId(R.drawable.junk_food_avoid)
                    .setTitle(getString(R.string.avoid_junk_food))
                    .setStars(Constants.STARS_FOR_AVOID_JUNK_FOOD)
                    .setType(Constants.CHALLENGE_TYPE_AVOID_JUNK_FOOD);
            list.add(scc);

            scc = new SelfControlChallenge();
            scc.setImageId(R.drawable.sugray_drink)
                    .setTitle(getString(R.string.avoid_sugary_drinks))
                    .setStars(Constants.STARS_FOR_AVOID_SURGARY_DRINKS)
                    .setType(Constants.CHALLENGE_TYPE_AVOID_SUGARY_DRINK);
            list.add(scc);

            scc = new SelfControlChallenge();
            scc.setImageId(R.drawable.snack)
                    .setTitle(getString(R.string.avoid_snacking))
                    .setStars(Constants.STARS_FOR_AVOID_SNACKING)
                    .setType(Constants.CHALLENGE_TYPE_AVOID_SNACKING);
            list.add(scc);

            return list;
        }

        private List<TipCategory> getDefaultCategories() {
            List<TipCategory> list = new ArrayList<>();

            String content = FileUtils.readFileFromAssets(getApplicationContext(), "tips.txt");

            //Parse json
            try {
                JSONArray jsonArray = new JSONArray(content);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    TipCategory tip = new TipCategory();
                    tip.setId(object.getInt("id"));
                    tip.setTitle(object.getString("title"));

                    JSONArray array = object.getJSONArray("advices");
                    List<TipDetail> details = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        TipDetail td = new TipDetail();
                        td.setMessage(array.getJSONObject(j).getString("message"));
                        details.add(td);
                    }
                    tip.setAdvices(details);
                    list.add(tip);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
