package com.hb.mydietcoach.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // Shared preferences file name
    private static final String PREF_NAME = "my_diet_coach";

    public static final String IS_FIRST_TIME_LAUNCH = "lauch";
    public static final String IS_FIRST_TIME_INSERT_SQLITE = "sqlite";
    public static final String IS_FIRST_TIME_DELETE_MEAL = "delete_meal";
    public static final String IS_FIRST_TIME_ADD_MEAL = "add_meal";
    public static final String IS_FIRST_TIME_CHALLENGES = "first_challenge";

    public static final String USER_WEIGHT = "weight";
    public static final String TARGET_WEIGHT = "target_weight";
    public static final String CURRENT_WEIGHT = "current_weight";
    public static final String IS_GENDER_FEMALE = "gender";

    public static final String IS_FIRST_TIME_TOOLTIP_CHALLENGE = "tooltip_challenge";
    public static final String IS_HAS_MOTIVATIONAL_PHOTO = "motivational_photo";


    public PreferenceManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
