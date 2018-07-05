package com.hb.mydietcoach.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.Reminder;
import com.hb.mydietcoach.model.challenge.AnimationChallenge;
import com.hb.mydietcoach.model.challenge.Challenge;
import com.hb.mydietcoach.model.challenge.NormalChallenge;
import com.hb.mydietcoach.model.challenge.RunChallenge;
import com.hb.mydietcoach.model.challenge.SelfControlChallenge;
import com.hb.mydietcoach.model.diary.Exercise;
import com.hb.mydietcoach.model.diary.Food;
import com.hb.mydietcoach.model.diary.IItemDiary;
import com.hb.mydietcoach.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private final String TAG = MyDatabase.class.getSimpleName();

    private Context context;

    private static final String DB_NAME = "fat_secret";
    private static final int DB_VERSION = 2;
    private static final String TABLE_FOOD_EXERCISE = "diary_activity";
    private static final String TABLE_REMINDER = "reminders";
    private static final String TABLE_CHALLENGE = "challenges";

    //Type for table food & exercise
    private static final int TYPE_FOOD = 0;
    private static final int TYPE_EXERCISE = 1;

    //Comnon column name
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";

    //Table food & exercise column name
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_TIME = "time";
    private static final String FIELD_CALORIES = "calories";
    private static final String FIELD_WEIGHT = "weight";

    //Table Reminder column name
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_START_DATE = "start_date";
    private static final String FIELD_REPEAT_MILISECONDS = "repeat_miliseconds";

    //Table Challenge column name
    private static final String FIELD_IMAGE_ID = "image_id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_STARS = "stars";
    private static final String FIELD_OBJECT_TYPE = "object_type";
    private static final String FIELD_CHALLENGE_TYPE = "challenge_type";
    private static final String FIELD_LAST_TIME_USING = "last_time";
    private static final String FIELD_TOTAL = "total";
    private static final String FIELD_CURRENT = "current";
    private static final String FIELD_UNIT = "unit";

    private static final String CREATE_TABLE_FOOD_EXERCISE = "CREATE TABLE " + TABLE_FOOD_EXERCISE + "("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_TYPE + " INTEGER, "
            + FIELD_TIME + " TEXT, "
            + FIELD_NAME + " TEXT, "
            + FIELD_CALORIES + " TEXT, "
            + FIELD_WEIGHT + " TEXT"
            + ")";

    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + "("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_CONTENT + " TEXT, "
            + FIELD_START_DATE + " LONG, "
            + FIELD_REPEAT_MILISECONDS + " LONG"
            + ")";

    private static final String CREATE_TABLE_MY_CHALLENGE = "CREATE TABLE " + TABLE_CHALLENGE + "("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_IMAGE_ID + " INTEGER, "
            + FIELD_TITLE + " TEXT, "
            + FIELD_STARS + " INTEGER, "
            + FIELD_OBJECT_TYPE + " INTEGER, "
            + FIELD_CHALLENGE_TYPE + " INTEGER, "
            + FIELD_LAST_TIME_USING + " LONG, "
            + FIELD_TOTAL + " FLOAT, "
            + FIELD_CURRENT + " FLOAT, "
            + FIELD_UNIT + " TEXT"
            + ")";

    private static final String DROP_TABLE_FOOD_EXERCISE = "DROP TABLE IF EXISTS " + TABLE_FOOD_EXERCISE;
    private static final String DROP_TABLE_REMINDER = "DROP TABLE IF EXISTS " + TABLE_REMINDER;
    private static final String DROP_TABLE_MY_CHALLENGE = "DROP TABLE IF EXISTS " + TABLE_CHALLENGE;

    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_FOOD_EXERCISE);
        sqLiteDatabase.execSQL(CREATE_TABLE_REMINDER);
        sqLiteDatabase.execSQL(CREATE_TABLE_MY_CHALLENGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_FOOD_EXERCISE);
        sqLiteDatabase.execSQL(DROP_TABLE_REMINDER);
        sqLiteDatabase.execSQL(DROP_TABLE_MY_CHALLENGE);
        onCreate(sqLiteDatabase);
    }

    public long insertFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_TYPE, TYPE_FOOD);
        values.put(FIELD_TIME, food.getTime());
        values.put(FIELD_NAME, food.getName());
        values.put(FIELD_CALORIES, food.getCalories());
        values.put(FIELD_WEIGHT, food.getWeight());

        long id = db.insert(TABLE_FOOD_EXERCISE, null, values);
        db.close();
        return id;
    }

    public long insertExercise(Exercise exercise) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_TYPE, TYPE_EXERCISE);
        values.put(FIELD_TIME, exercise.getTime());
        values.put(FIELD_NAME, exercise.getName());
        values.put(FIELD_CALORIES, exercise.getCalories());

        // insert row
        long id = db.insert(TABLE_FOOD_EXERCISE, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List getAllFood() {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_FOOD_EXERCISE + " WHERE " + FIELD_TYPE + " = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(TYPE_FOOD)});

        if (cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
                if (type == TYPE_FOOD) {
                    Food food = new Food();
                    food.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                    food.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                    food.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                    food.setCalories(cursor.getString(cursor.getColumnIndex(FIELD_CALORIES)));
                    food.setWeight(cursor.getString(cursor.getColumnIndex(FIELD_WEIGHT)));
                    list.add(food);
                }
            } while (cursor.moveToNext());
        }
        db.close();

        // return list
        return list;
    }

    public List<IItemDiary> findDiaryItemByDate(String date) {
        List<IItemDiary> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_FOOD_EXERCISE + " WHERE "
                + FIELD_TIME + " LIKE ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, new String[]{date + "%"});

        if (cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
                if (type == TYPE_FOOD) {
                    Food food = new Food();
                    food.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                    food.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                    food.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                    food.setCalories(cursor.getString(cursor.getColumnIndex(FIELD_CALORIES)));
                    food.setWeight(cursor.getString(cursor.getColumnIndex(FIELD_WEIGHT)));
                    list.add(food);
                } else {
                    Exercise exercise = new Exercise();
                    exercise.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                    exercise.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                    exercise.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                    exercise.setCalories(cursor.getString(cursor.getColumnIndex(FIELD_CALORIES)));
                    list.add(exercise);
                }
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();

        // return list
        return list;
    }

    /**
     * Delete a food or exercise
     *
     * @param id: id of item (food or exercise)
     */
    public void deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD_EXERCISE, FIELD_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Reminder> getAllReminder() {
        List<Reminder> list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_REMINDER;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                reminder.setContent(cursor.getString(cursor.getColumnIndex(FIELD_CONTENT)));
                long miliseconds = cursor.getLong(cursor.getColumnIndex(FIELD_START_DATE));
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(miliseconds);
                reminder.setStartDate(calendar);
                reminder.setRepeatMilisecond(cursor.getLong(cursor.getColumnIndex(FIELD_REPEAT_MILISECONDS)));
                list.add(reminder);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public long insertReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_CONTENT, reminder.getContent());
        values.put(FIELD_START_DATE, reminder.getStartDate().getTime().getTime());
        values.put(FIELD_REPEAT_MILISECONDS, reminder.getRepeatMilisecond());
        long id = db.insert(TABLE_REMINDER, null, values);
        db.close();
        return id;
    }

    public boolean updateReminder(Reminder reminder) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_CONTENT, reminder.getContent());
        values.put(FIELD_START_DATE, reminder.getStartDate().getTime().getTime());
        values.put(FIELD_REPEAT_MILISECONDS, reminder.getRepeatMilisecond());
        int nums = database.update(TABLE_REMINDER, values, FIELD_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())});
        database.close();
        return nums > 0;
    }

    public boolean deleteReminder(long reminderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int num = db.delete(TABLE_REMINDER, FIELD_ID + " = ?",
                new String[]{String.valueOf(reminderId)});
        db.close();
        return num > 0;
    }

    public long insertChallenge(Challenge challenge) {
        if (challenge instanceof AnimationChallenge) {
            return insertAnimationChallenge((AnimationChallenge) challenge);
        } else if (challenge instanceof NormalChallenge) {
            return insertNormalChallenge((NormalChallenge) challenge);
        } else if (challenge instanceof RunChallenge) {
            return insertRunChallenge((RunChallenge) challenge);
        } else if (challenge instanceof SelfControlChallenge) {
            return insertSelfControlChallenge((SelfControlChallenge) challenge);
        }
        return -1;
    }

    /**
     * Insert AnimationChallenge to SQLite
     *
     * @param challenge: AnimationChallenge
     * @return: id if insert success, -1 if otherwise
     */
    private long insertAnimationChallenge(AnimationChallenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_IMAGE_ID, challenge.getImageId());
        values.put(FIELD_TITLE, challenge.getTitle());
        values.put(FIELD_STARS, challenge.getStars());
        values.put(FIELD_OBJECT_TYPE, Constants.OBJECT_ANIMATION_CHALLENGE);
        values.put(FIELD_CHALLENGE_TYPE, challenge.getType());
        values.put(FIELD_TOTAL, (float) challenge.getTotalCount());
        values.put(FIELD_CURRENT, (float) challenge.getCurrentPosition());
        values.put(FIELD_UNIT, challenge.getUnit());
        long id = db.insert(TABLE_CHALLENGE, null, values);
        db.close();
        return id;
    }

    /**
     * Insert NormalChallenge to SQLite
     *
     * @param challenge: NormalChallenge
     * @return: id if insert success, -1 if otherwise
     */
    private long insertNormalChallenge(NormalChallenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_IMAGE_ID, challenge.getImageId());
        values.put(FIELD_TITLE, challenge.getTitle());
        values.put(FIELD_STARS, challenge.getStars());
        values.put(FIELD_OBJECT_TYPE, Constants.OBJECT_NORMAL_CHALLENGE);
        values.put(FIELD_CHALLENGE_TYPE, challenge.getType());
        values.put(FIELD_TOTAL, (float) challenge.getTotalCount());
        values.put(FIELD_CURRENT, (float) challenge.getCurrentPosition());
        values.put(FIELD_UNIT, challenge.getUnit());
        long id = db.insert(TABLE_CHALLENGE, null, values);
        db.close();
        return id;
    }

    /**
     * Insert NormalChallenge to SQLite
     *
     * @param challenge: NormalChallenge
     * @return: id if insert success, -1 if otherwise
     */
    private long insertRunChallenge(RunChallenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_IMAGE_ID, challenge.getImageId());
        values.put(FIELD_TITLE, challenge.getTitle());
        values.put(FIELD_STARS, challenge.getStars());
        values.put(FIELD_OBJECT_TYPE, Constants.OBJECT_RUN_CHALLENGE);
        values.put(FIELD_CHALLENGE_TYPE, challenge.getType());
        values.put(FIELD_TOTAL, (float) challenge.getTotalLength());
        values.put(FIELD_CURRENT, (float) challenge.getCurrentPosition());
        values.put(FIELD_UNIT, challenge.getUnit());
        long id = db.insert(TABLE_CHALLENGE, null, values);
        db.close();
        return id;
    }

    /**
     * Insert NormalChallenge to SQLite
     *
     * @param challenge: NormalChallenge
     * @return: id if insert success, -1 if otherwise
     */
    private long insertSelfControlChallenge(SelfControlChallenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_IMAGE_ID, challenge.getImageId());
        values.put(FIELD_TITLE, challenge.getTitle());
        values.put(FIELD_STARS, challenge.getStars());
        values.put(FIELD_OBJECT_TYPE, Constants.OBJECT_SELF_CONTROL_CHALLENGE);
        values.put(FIELD_CHALLENGE_TYPE, challenge.getType());
        values.put(FIELD_CURRENT, challenge.getCurrentPosition());
        long id = db.insert(TABLE_CHALLENGE, null, values);
        db.close();
        return id;
    }

    /**
     * Get all exercise challenge from sqlite
     *
     * @return: List of exercise challenge
     */
    public List<Challenge> getExerciseChallenges() {
        //Challenge type need to find
        String[] types = {Constants.CHALLENGE_TYPE_GYM + "",
                Constants.CHALLENGE_TYPE_PUSH_UP + "",
                Constants.CHALLENGE_TYPE_WALK_A_MILE + ""};

        return getChallengeFromTypes(types);
    }

    /**
     * Get all exercise challenge from sqlite
     *
     * @return: List of exercise challenge
     */
    public List<Challenge> getEatingHabitChallenges() {
        //Challenge type need to find
        String[] types = {Constants.CHALLENGE_TYPE_DRINK_WATER + "",
                Constants.CHALLENGE_TYPE_FILL_MY_PLATE + ""};
        return getChallengeFromTypes(types);
    }

    /**
     * Get all exercise challenge from sqlite
     *
     * @return: List of exercise challenge
     */
    public List<Challenge> getSelfControlChallenges() {
        //Challenge type need to find
        String[] types = {Constants.CHALLENGE_TYPE_AVOID_JUNK_FOOD + "",
                Constants.CHALLENGE_TYPE_AVOID_SUGARY_DRINK + "",
                Constants.CHALLENGE_TYPE_AVOID_SNACKING + ""};
        return getChallengeFromTypes(types);
    }

    /**
     * Get all my challenge from sqlite
     *
     * @return: List of my challenge
     */
    public List<Challenge> getMyChallenges() {
        ArrayList<Challenge> list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CHALLENGE + " WHERE "
                + FIELD_CHALLENGE_TYPE + " = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constants.CHALLENGE_TYPE_OF_MY)});

        if (cursor.moveToFirst()) {
            do {
                NormalChallenge challenge = new NormalChallenge();
                challenge.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)))
                        .setImageId(R.drawable.challenges_general_after)
                        .setTitle(cursor.getString(cursor.getColumnIndex(FIELD_TITLE)))
                        .setStars(Constants.STARS_FOR_MY_CHALLENGE)
                        .setType(Constants.CHALLENGE_TYPE_OF_MY)
                        .setLastTime(cursor.getLong(cursor.getColumnIndex(FIELD_LAST_TIME_USING)));
                challenge.setTotalCount((int) cursor.getFloat(cursor.getColumnIndex(FIELD_TOTAL)))
                        .setCurrentPosition((int) cursor.getFloat(cursor.getColumnIndex(FIELD_CURRENT)))
                        .setUnit(context.getString(R.string.events));
                list.add(challenge);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /**
     * Get all challenges from types like DRINK-WATER, GYM-EXERCISE,...
     *
     * @param types: Types of challenge
     * @return: List challenge has type in types
     */
    private List<Challenge> getChallengeFromTypes(String[] types) {
        ArrayList<Challenge> list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        //InClauses example: ?,?,? (Amount of ? depend types.length)
        String inClauses = TextUtils.join(",", Collections.nCopies(types.length, "?"));

        String sql = "SELECT * FROM " + TABLE_CHALLENGE + " WHERE "
                + FIELD_CHALLENGE_TYPE + " IN (" + inClauses + ")";

        Cursor cursor = db.rawQuery(sql, types);

        if (cursor.moveToFirst()) {
            do {
                Challenge challenge = getChallengFromCursor(cursor);
                if (challenge != null) list.add(challenge);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    /**
     * Get a challenge from cursor
     *
     * @param cursor: input data from sqlite
     * @return: A challenge depend object type and challenge type
     */
    private Challenge getChallengFromCursor(Cursor cursor) {
        Challenge challenge;
        int objectType = cursor.getInt(cursor.getColumnIndex(FIELD_OBJECT_TYPE));
        if (objectType == Constants.OBJECT_ANIMATION_CHALLENGE) {
            AnimationChallenge ac = new AnimationChallenge();
            ac.setTotalCount((int) cursor.getFloat(cursor.getColumnIndex(FIELD_TOTAL)))
                    .setCurrentPosition((int) cursor.getFloat(cursor.getColumnIndex(FIELD_CURRENT)))
                    .setUnit(cursor.getString(cursor.getColumnIndex(FIELD_UNIT)));
            challenge = ac;
        } else if (objectType == Constants.OBJECT_NORMAL_CHALLENGE) {
            NormalChallenge nc = new NormalChallenge();
            nc.setTotalCount((int) cursor.getFloat(cursor.getColumnIndex(FIELD_TOTAL)))
                    .setCurrentPosition((int) cursor.getFloat(cursor.getColumnIndex(FIELD_CURRENT)))
                    .setUnit(cursor.getString(cursor.getColumnIndex(FIELD_UNIT)));
            challenge = nc;
        } else if (objectType == Constants.OBJECT_RUN_CHALLENGE) {
            RunChallenge rc = new RunChallenge();
            rc.setTotalLength(cursor.getFloat(cursor.getColumnIndex(FIELD_TOTAL)))
                    .setCurrentPosition(cursor.getFloat(cursor.getColumnIndex(FIELD_CURRENT)))
                    .setUnit(cursor.getString(cursor.getColumnIndex(FIELD_UNIT)));
            challenge = rc;
        } else if (objectType == Constants.OBJECT_SELF_CONTROL_CHALLENGE) {
            SelfControlChallenge scc = new SelfControlChallenge();
            scc.setCurrentPosition((int) cursor.getFloat(cursor.getColumnIndex(FIELD_CURRENT)));
            challenge = scc;
        } else {
            return null;
        }
        challenge.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)))
                .setImageId(cursor.getInt(cursor.getColumnIndex(FIELD_IMAGE_ID)))
                .setTitle(cursor.getString(cursor.getColumnIndex(FIELD_TITLE)))
                .setStars(Constants.STARS_FOR_MY_CHALLENGE)
                .setType(cursor.getInt(cursor.getColumnIndex(FIELD_CHALLENGE_TYPE)))
                .setLastTime(cursor.getLong(cursor.getColumnIndex(FIELD_LAST_TIME_USING)));
        return challenge;
    }

    /**
     * Get last challenge depend last time using this challenge
     *
     * @return: last challenge
     */
    public Challenge getLastChallenge() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT MAX(" + FIELD_LAST_TIME_USING + ") AS " + FIELD_LAST_TIME_USING + ", "
                + FIELD_ID + ", " + FIELD_IMAGE_ID + ", " + FIELD_TITLE + ", "
                + FIELD_STARS + ", " + FIELD_OBJECT_TYPE + ", " + FIELD_CHALLENGE_TYPE + ", "
                + FIELD_TOTAL + ", " + FIELD_CURRENT + ", " + FIELD_UNIT
                + " FROM " + TABLE_CHALLENGE + " WHERE "
                + FIELD_LAST_TIME_USING + " > 0";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            return getChallengFromCursor(cursor);
        }
        // close db connection
        db.close();

        // return list
        return null;
    }

    public Challenge getDrinkWaterChallenge() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CHALLENGE + " WHERE "
                + FIELD_CHALLENGE_TYPE + " = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constants.CHALLENGE_TYPE_DRINK_WATER)});

        if (cursor.moveToFirst()) {
            do {
                NormalChallenge challenge = new NormalChallenge();
                challenge.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)))
                        .setImageId(R.drawable.challenge_water_full)
                        .setTitle(cursor.getString(cursor.getColumnIndex(FIELD_TITLE)))
                        .setStars(cursor.getInt(cursor.getColumnIndex(FIELD_STARS)))
                        .setType(cursor.getInt(cursor.getColumnIndex(FIELD_CHALLENGE_TYPE)))
                        .setLastTime(cursor.getLong(cursor.getColumnIndex(FIELD_LAST_TIME_USING)));
                challenge.setTotalCount((int) cursor.getFloat(cursor.getColumnIndex(FIELD_TOTAL)))
                        .setCurrentPosition((int) cursor.getFloat(cursor.getColumnIndex(FIELD_CURRENT)))
                        .setUnit(context.getString(R.string.events));
                return challenge;
            } while (cursor.moveToNext());
        }
        db.close();
        return null;
    }

    /**
     * Update last using challenge
     *
     * @param challenge: last challenge
     * @return true if success, false if failed
     */
    public boolean updateLastChallenge(Challenge challenge) {
        if (challenge instanceof NormalChallenge) {
            return updateNormalChallenge((NormalChallenge) challenge);
        } else if (challenge instanceof RunChallenge) {
            return updateRunChallenge((RunChallenge) challenge);
        } else if (challenge instanceof SelfControlChallenge) {
            return updateSelfControlChallenge((SelfControlChallenge) challenge);
        }
        return false;
    }

    private boolean updateNormalChallenge(NormalChallenge challenge) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_TOTAL, (float) challenge.getTotalCount());
        values.put(FIELD_CURRENT, (float) challenge.getCurrentPosition());
        values.put(FIELD_LAST_TIME_USING, challenge.getLastTime());

        int num = db.update(TABLE_CHALLENGE, values,
                FIELD_ID + "= ?", new String[]{String.valueOf(challenge.getId())});

        db.close();

        return num > 0;
    }

    private boolean updateRunChallenge(RunChallenge challenge) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_TOTAL, (float) challenge.getTotalLength());
        values.put(FIELD_CURRENT, (float) challenge.getCurrentPosition());
        values.put(FIELD_LAST_TIME_USING, challenge.getLastTime());

        int num = db.update(TABLE_CHALLENGE, values,
                FIELD_ID + "= ?", new String[]{String.valueOf(challenge.getId())});

        db.close();

        return num > 0;
    }

    private boolean updateSelfControlChallenge(SelfControlChallenge challenge) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_CURRENT, (float) challenge.getCurrentPosition());
        values.put(FIELD_LAST_TIME_USING, challenge.getLastTime());

        int num = db.update(TABLE_CHALLENGE, values,
                FIELD_ID + "= ?", new String[]{String.valueOf(challenge.getId())});

        db.close();

        return num > 0;
    }

    public void showLogListChallenge(String nameList, List<Challenge> list) {
        Log.e(TAG, "List name: " + nameList);
        for (Challenge challenge : list) {
            showLogChallenge(challenge);
        }
    }

    public void showLogChallenge(Challenge challenge) {
        Log.e(TAG, "ID: " + challenge.getId());
        Log.e(TAG, "Name: " + challenge.getTitle());
        Log.e(TAG, "Star: " + challenge.getStars());
        Log.e(TAG, "Type: " + challenge.getType());
        Log.e(TAG, "Last time: " + challenge.getLastTime());
        Log.e(TAG, "");
    }
}
