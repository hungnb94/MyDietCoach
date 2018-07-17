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
    public static final String IS_FIRST_TIME_OPEN_DIARY = "diary";
    public static final String IS_FIRST_TIME_DELETE_MEAL = "delete_meal";
    public static final String IS_FIRST_TIME_ADD_MEAL = "add_meal";
    public static final String IS_FIRST_TIME_CHALLENGES = "first_challenge";

    //Reward
    public static final String LAST_POINTS_FOR = "last_point_for";
    public static final String POINTS = "points";
    public static final String LEVEL = "level";
    public static final String TOTAL_POINTS = "total_point";
    public static final String IS_LIKE_FANPAGE_FACEBOOK = "like_fanpage";
    public static final String IS_SHARE_GOOGLE_PLUS = "share_google_plus";
    public static final String IS_FOLLOW_TWITTER = "follow_twitter";

    //Profile of user
    public static final String DAILY_CALORIES_GOAL = "daily_calories_goal";
    public static final String CALORIES_LEFT = "calories_left";
    public static final String LAST_USING_DAY = "last_using_day";
    public static final String USER_BIRTH_YEAR = "birth_year";
    public static final String USER_HEIGHT = "user_height";
    public static final String ACTIVITY_LEVEL = "activity_level";
    public static final String WEEKY_WEIGHT_LOSS_GOAL = "weeky_weight_loss_goal";
    public static final String UNIT_MEASUREMENT = "unit_measurement";
    public static final String WEIGHT_HEIGHT_UNIT = "weight_height_unit";

    //Setting activity
    public static final String SETTING_IS_PLAY_SOUND = "play_sound";
    public static final String SETTING_IS_MAKE_VIBRATE = "make_vibrate";
    public static final String SETTING_IS_SHOW_CUSTOMIZED_AVATAR = "customized_avatar";
    public static final String SETTING_URI_NOTIFICATION_SOUND = "uri_sound";
    public static final String SETTING_START_SLEEPING_HOUR = "start_sleeping_hour";
    public static final String SETTING_END_SLEEPING_HOUR = "end_sleeping_hour";
    public static final String SETTING_START_SLEEPING_MINUTE = "start_sleeping_minute";
    public static final String SETTING_END_SLEEPING_MINUTE = "end_sleeping_minute";

    public static final String START_WEIGHT = "weight";
    public static final String TARGET_WEIGHT = "target_weight";
    public static final String CURRENT_WEIGHT = "current_weight";
    public static final String IS_GENDER_FEMALE = "gender";
    public static final String LAST_WEIGHT_LOGGING_DAY = "day_weight_log";

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
