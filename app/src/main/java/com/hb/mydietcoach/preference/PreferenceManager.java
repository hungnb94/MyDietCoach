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

    public static final String NUMBLE_GLASSES = "number_glasses";

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
}
