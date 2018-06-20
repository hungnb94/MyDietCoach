package com.hb.mydietcoach.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.model.Challenge;
import com.hb.mydietcoach.model.Exercise;
import com.hb.mydietcoach.model.Food;
import com.hb.mydietcoach.model.IItemDiary;
import com.hb.mydietcoach.model.NormalChallenge;
import com.hb.mydietcoach.model.Reminder;
import com.hb.mydietcoach.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private final String TAG = MyDatabase.class.getSimpleName();

    private Context context;

    private static final String DB_NAME = "fat_secret";
    private static final int DB_VERSION = 2;
    private static final String TABLE_FOOD_EXERCISE = "diary_activity";
    private static final String TABLE_REMINDER = "reminders";
    private static final String TABLE_MY_CHALLENGE = "challenges";

    //Type for table food & exercise
    public static final int TYPE_FOOD = 0;
    public static final int TYPE_EXERCISE = 1;

    //Comnon column name
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";

    //Table food & exercise column name
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_CALORIES = "calories";
    public static final String FIELD_WEIGHT = "weight";

    //Table Reminder column name
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_START_DATE = "start_date";
    public static final String FIELD_REPEAT_MILISECONDS = "repeat_miliseconds";

    //Table Challenge column name

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

    private static final String CREATE_TABLE_MY_CHALLENGE = "CREATE TABLE " + TABLE_MY_CHALLENGE + "("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_NAME + " TEXT"
            + ")";

    private static final String DROP_TABLE_FOOD_EXERCISE = "DROP TABLE IF EXISTS " + TABLE_FOOD_EXERCISE;
    private static final String DROP_TABLE_REMINDER = "DROP TABLE IF EXISTS " + TABLE_REMINDER;
    private static final String DROP_TABLE_MY_CHALLENGE = "DROP TABLE IF EXISTS " + TABLE_MY_CHALLENGE;

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
        List<Food> list = new ArrayList();
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

    public List<IItemDiary> findByDate(String date) {
        List list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_FOOD_EXERCISE + " WHERE "
                + FIELD_TIME + " LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{date + "%"});

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

    public int updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, food.getName());
        values.put(FIELD_TIME, food.getTime());
        values.put(FIELD_CALORIES, food.getCalories());
        values.put(FIELD_WEIGHT, food.getWeight());

        // updating row
        return db.update(TABLE_FOOD_EXERCISE, values, FIELD_ID + " = ?",
                new String[]{String.valueOf(food.getId())});
    }

    public int updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, exercise.getName());
        values.put(FIELD_TIME, exercise.getTime());
        values.put(FIELD_CALORIES, exercise.getCalories());

        // updating row
        return db.update(TABLE_FOOD_EXERCISE, values, FIELD_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }

    public void deleteItem(IItemDiary item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD_EXERCISE, FIELD_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
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

    public long insertMyChallenge(Challenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, challenge.getTitle());
        long id = db.insert(TABLE_MY_CHALLENGE, null, values);
        db.close();
        return id;
    }

    public List<Challenge> getAllMyChallenge() {
        ArrayList<Challenge> list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_MY_CHALLENGE;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                NormalChallenge challenge = new NormalChallenge();
                challenge.setImageId(R.drawable.challenges_general_after);
                challenge.setTitle(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                challenge.setStars(Constants.STARS_FOR_MY_CHALLENGE);
                challenge.setType(Constants.CHALLENGE_TYPE_OF_MY);
                challenge.setTotalCount(3);
                challenge.setCurrentPosition(0);
                challenge.setUnit(context.getString(R.string.events));
                list.add(challenge);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
}
