package com.hb.mydietcoach.utils;

public class Constants {
    public static final long SPLASH_LENGTH = 1500;
    public static final long ANIMATION_LENGTH = 500;

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd hh:mm a";
    public static final String FORMAT_TODAY_DATE = "MMM dd";
    public static final String FORMAT_NORMAL_DATE = "EEEE, MMM dd";
    public static final String FORMAT_HOUR = "hh:mm a";
    public static final String DATA_SERIALIZABLE = "data_serializable";

    public static final String NOTIFICATION_CONTENT_TEXT = "content_notification";
    public static final long ALERT_DIALOG_LENGTH = 1000;
    public static final int RESULT_DELETE_REMINDER = 94;
    public static final String IS_REPEAT_NOTIFICATION = "repeate_nofification";
    public static final String NOTIFICATION_REPEATE_TIME_INTERVAL = "interval_time";
    public static final int COLUMN_GLASSES_COUNT = 4;

    //Challenge type
    public static final int CHALLENGE_TYPE_DRINK_WATER = 0;
    public static final int CHALLENGE_TYPE_PUSH_UP = 1;
    public static final int CHALLENGE_TYPE_GYM = 2;
    public static final int CHALLENGE_TYPE_FILL_MY_PLATE = 3;
    public static final int CHALLENGE_TYPE_AVOID_JUNK_FOOD = 4;
    public static final int CHALLENGE_TYPE_AVOID_SUGARY_DRINK = 5;
    public static final int CHALLENGE_TYPE_AVOID_SNACKING = 6;
    public static final int CHALLENGE_TYPE_OF_MY = 7;
    public static final int CHALLENGE_TYPE_WALK_A_MILE = 8;

    //Challenge type
    public static final int DEFAULT_DRINK_WATER = 8;
    public static final int DEFAULT_PUSH_UP = 2;
    public static final int DEFAULT_GYM = 2;
    public static final int DEFAULT_FILL_MY_PLATE = 5;
    public static final int DEFAULT_MY_CHALLENGE = 4;
    public static final float DEFAULT_WALK_A_MILE_TOTAL = 2;
    public static final double DEFAULT_WALK_A_MILE_LENGTH_UNIT = 0.01;

    //Exercise challenges
    public static final int STARS_FOR_GYM_EXERCISE = 40;
    public static final int STARS_FOR_PUSH_UP = 15;
    public static final int STARS_FOR_WALK_A_MILE = 20;
    //Eating habit challenges
    public static final int STARS_FOR_DRINK_WATER = 3;
    public static final int STARS_FOR_FILL_MY_PLATE = 9;
    //Self control challenges
    public static final int STARS_FOR_AVOID_JUNK_FOOD = 30;
    public static final int STARS_FOR_AVOID_SURGARY_DRINKS = 20;
    public static final int STARS_FOR_AVOID_SNACKING = 80;
    //My challenges
    public static final int STARS_FOR_MY_CHALLENGE = 5;

    //SELF CONTROL TOTAL ITEMS
    public static final int SELF_CONTROL_CHALLENGE_TOTAL_ITEMS = 10;

    //Challenge object type
    public static final int OBJECT_NORMAL_CHALLENGE = 1;
    public static final int OBJECT_ANIMATION_CHALLENGE = 2;
    public static final int OBJECT_RUN_CHALLENGE = 3;
    public static final int OBJECT_SELF_CONTROL_CHALLENGE = 4;

    public static final String MY_FOLDER = "mydietcoach";

    //Request code for project
    public static final int RC_MOTIVATIONAL_PHOTO = 0;
    public static final int RC_PICK_IMAGE = 1;
    public static final int RC_TAKE_PHOTO = 2;
    public static final int RC_CAMERA_PERMISSION = 3;
    public static final int RC_EXTERNAL_STORAGE = 4;
    public static final int RC_NEW_MY_CHALLENGE = 5;
    public static final int RC_EXERCISE_NOTIFICATION = 6;
    public static final int RC_EDITTING_CHALLENGE = 7;
    public static final int RC_ADD_CHALLENGE = 8;
    public static final int RC_ADD_EXERCISE = 9;
    public static final int RC_EDIT_MEAL_HISTORY = 10;
    public static final int RC_ADD_REMINDER = 11;
    public static final int RC_EDIT_REMINDER = 12;

    //Change unit
    public static final double LB_TO_KG = 0.45359237;
    public static final double KG_TO_LB = 2.204622622;
    public static final double CM_TO_FT = 0.03280839895;
    public static final double FT_TO_CM = 30.48;
    public static final double CM_TO_IN = 0.3937007874;
    public static final double IN_TO_CM = 2.54;

    public static final String IS_MOTIVATIONAL_NOTIFICATION = "motivational";

    //TIPS CATEGORY ID
    public static final int ID_FOOD_CRAVINGS = 1;
    public static final int ID_EMOTIONAL_EATING = 2;
    public static final int ID_EATING_OUT = 3;
    public static final int ID_FOOD_TEMPTATIOIN = 4;
    public static final int ID_FAMILY_MEAL = 5;

    public static final long TRACKING_HANDLE_ANIM_LENGTH = 1500;
    public static final String URL_CHANGE_SUBCRIPTION = "https://support.google.com/googleplay/answer/7018481";

    //Point on reward
    public static final int POINT_FOR_LIKE_FANPAGE_FACEBOOK = 60;
    public static final int POINT_FOR_SHARE_GOOGLE_PLUS = 60;
    public static final int POINT_FOR_FOLLOW_TWITTER = 60;
}
