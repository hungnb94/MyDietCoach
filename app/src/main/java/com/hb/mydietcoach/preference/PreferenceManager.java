package com.hb.mydietcoach.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "my_diet_coach";

    public static final String IS_FIRST_TIME_LAUNCH = "lauch";
    public static final String IS_FIRST_TIME_DELETE_MEAL = "delete_meal";
    public static final String IS_FIRST_TIME_ADD_MEAL = "add_meal";
    public static final String IS_FIRST_TIME_CHALLENGES = "first_challenge";

    public static final String USER_WEIGHT = "weight";
    public static final String IS_GENDER_FEMALE = "gender";
    public static final String IS_FIRST_TIME_TOOLTIP_CHALLENGE = "tooltip_challenge";
    public static final String IS_HAS_MOTIVATIONAL_PHOTO = "motivational_photo";

    //Commond field
    public static final String CHALLENGE_TYPE = "challenge_type";
    //Normal challenge
    public static final String GYM_EXERCISE_TOTAL_ITEMS = "gym_total";
    public static final String GYM_EXERCISE_CURRENT_POSITION = "gym_current";
    public static final String GYM_EXERCISE_LAST_TIME = "gym_time";

    public static final String PUSH_UP_TOTAL_ITEMS = "push_uptotal";
    public static final String PUSH_UP_CURRENT_POSITION = "push_upcurrent";
    public static final String PUSH_UP_LAST_TIME = "push_time";

    public static final String DRINK_WATER_TOTAL_ITEMS = "water_total";
    public static final String DRINK_WATER_CURRENT_POSITION = "water_current";
    public static final String DRINK_WATER_LAST_TIME = "water_time";
    //My challenge
    public static final String MY_CHALLENGE_TITLE = "my_challenge_title";
    public static final String MY_CHALLENGE_TOTAL_ITEMS = "my_challenge_total";
    public static final String MY_CHALLENGE_CURRENT_POSITION = "my_challenge_current";
    public static final String MY_CHALLENGE_LAST_TIME = "my_challenge_time";
    //Anim challenge
    public static final String FILL_MY_PLATE_TOTAL_ITEMS = "fill_plate_total";
    public static final String FILL_MY_PLATE_CURRENT_POSITION = "fill_plate_current";
    public static final String FILL_MY_PLATE_LAST_TIME = "fill_plate_time";
    //Run challenge
    public static final String RUN_CHALLENGE_TOTAL_LENGTH = "run_total";
    public static final String RUN_CHALLENGE_CURRENT_POSITION = "run_current";
    public static final String RUN_CHALLENGE_LAST_TIME = "run_time";
    //Self control challenge
    public static final String AVOID_JUNK_FOOD_CURRENT_POSITION = "junk_food_current";
    public static final String AVOID_JUNK_FOOD_LAST_TIME = "junk_food_time";

    public static final String AVOID_SUGARY_DRINK_CURRENT_POSITION = "sugary_drink_current";
    public static final String AVOID_SUGARY_DRINK_LAST_TIME = "sugary_drink_time";

    public static final String AVOID_SNACKING_CURRENT_POSITION = "snacking_current";
    public static final String AVOID_SNACKING_LAST_TIME = "snacking_time";


    public PreferenceManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public boolean putFloat(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float getFloat(String key, float defaultValue){
        return pref.getFloat(key, defaultValue);
    }

    public void remove(String key) {
        editor.remove(key);
    }

    public int getInt(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public boolean putInt(String key, int defaultValue) {
        editor.putInt(key, defaultValue);
        return editor.commit();
    }

    public long getLong(String key, long defaultValue){
        return pref.getLong(key, defaultValue);
    }

    public boolean putLong(String key, long value){
        editor.putLong(key, value);
        return editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public boolean putString(String key, String value){
        editor.putString(key, value);
        return editor.commit();
    }
}
