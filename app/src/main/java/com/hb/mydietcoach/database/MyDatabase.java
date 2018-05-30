package com.hb.mydietcoach.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hb.mydietcoach.model.Exercise;
import com.hb.mydietcoach.model.Food;
import com.hb.mydietcoach.model.IItemDiary;
import com.hb.mydietcoach.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.hb.mydietcoach.utils.Constants.FIELD_CALORIES;
import static com.hb.mydietcoach.utils.Constants.FIELD_ID;
import static com.hb.mydietcoach.utils.Constants.FIELD_NAME;
import static com.hb.mydietcoach.utils.Constants.FIELD_TIME;
import static com.hb.mydietcoach.utils.Constants.FIELD_TYPE;
import static com.hb.mydietcoach.utils.Constants.FIELD_WEIGHT;
import static com.hb.mydietcoach.utils.Constants.TYPE_FOOD;

public class MyDatabase extends SQLiteOpenHelper {
    private final String TAG = MyDatabase.class.getSimpleName();

    private static final String DB_NAME = "fat_secret";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "diary_activity";

    private static final String CREATE_DATABASE = "CREATE TABLE " + TABLE_NAME + "("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_TYPE + " INTEGER, "
            + FIELD_TIME + " TEXT, "
            + FIELD_NAME + " TEXT, "
            + FIELD_CALORIES + " TEXT, "
            + FIELD_WEIGHT + " TEXT"
            + ")";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_NAME;

    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public long insertFood(Food food) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_TYPE, Constants.TYPE_FOOD);
        values.put(FIELD_TIME, food.getTime());
        values.put(FIELD_NAME, food.getName());
        values.put(FIELD_CALORIES, food.getCalories());
        values.put(FIELD_WEIGHT, food.getWeight());

        // insert row
        long id = db.insert(TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public long insertExercise(Exercise exercise) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_TYPE, Constants.TYPE_EXERCISE);
        values.put(FIELD_TIME, exercise.getTime());
        values.put(FIELD_NAME, exercise.getName());
        values.put(FIELD_CALORIES, exercise.getCalories());

        // insert row
        long id = db.insert(TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public List getAll() {
        List list = new ArrayList();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
                if (type == Constants.TYPE_FOOD) {
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

    public List getAllFood() {
        List list = new ArrayList();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{FIELD_ID, FIELD_TYPE, FIELD_NAME, FIELD_TIME, FIELD_CALORIES, FIELD_WEIGHT},
                FIELD_TYPE + " = ?",
                new String[]{String.valueOf(TYPE_FOOD)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
                if (type == Constants.TYPE_FOOD) {
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
        // close db connection
        db.close();

        // return list
        return list;
    }

    public List findByDate(String date) {
        List list = new ArrayList();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{FIELD_ID, FIELD_TYPE, FIELD_NAME, FIELD_TIME, FIELD_CALORIES, FIELD_WEIGHT},
                FIELD_TIME + " LIKE ?",
                new String[]{date + "%"}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(cursor.getColumnIndex(FIELD_TYPE));
                if (type == Constants.TYPE_FOOD) {
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
        return db.update(TABLE_NAME, values, FIELD_ID + " = ?",
                new String[]{String.valueOf(food.getId())});
    }

    public int updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_NAME, exercise.getName());
        values.put(FIELD_TIME, exercise.getTime());
        values.put(FIELD_CALORIES, exercise.getCalories());

        // updating row
        return db.update(TABLE_NAME, values, FIELD_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }

    public void deleteItem(IItemDiary item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, FIELD_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
    }
}
