package com.example.hetkataria.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hetkataria.alarmapp.model.Alarm;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "AlarmData";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARM_TABLE = "CREATE TABLE alarm ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "time TEXT, "+
                "am_pm TEXT, "+ "onOrOff INTEGER )";

        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alarm");
        this.onCreate(db);
    }

    private static final String TABLE_ALARMS = "alarm";

    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_AM_PM = "am_pm";
    private static final String KEY_ON = "onOrOff";


    private static final String[] COLUMNS = {KEY_ID,KEY_TIME,KEY_AM_PM,KEY_ON};

    public void addAlarm(Alarm alarm){

        Log.d("addAlarm", alarm.getTime());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, alarm.getTime());
        values.put(KEY_AM_PM, alarm.getAm_or_pm());
        values.put(KEY_ON, alarm.getOn());


        db.insert(TABLE_ALARMS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        db.close();
    }

    public int updateAlarm(Alarm alarm) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, alarm.getTime());
        values.put(KEY_AM_PM, alarm.getAm_or_pm());
        values.put(KEY_ON, alarm.getOn());

        int i = db.update(TABLE_ALARMS, //table
                values, // column/value
                KEY_TIME+" = ?", // selections
                new String[] { String.valueOf(alarm.getTime()) }); //selection args

        db.close();

        return i;
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new LinkedList<Alarm>();

        String query = "SELECT  * FROM " + TABLE_ALARMS + " ORDER BY " + COLUMNS[1];

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Alarm alarm = null;
        if (cursor.moveToFirst()) {
            do {
                alarm = new Alarm("1","default");
                alarm.setId(Integer.parseInt(cursor.getString(0)));
                alarm.setTime(cursor.getString(1));
                alarm.setAm_or_pm(cursor.getString(2));
                alarm.setOn(cursor.getInt(3));


                alarms.add(alarm);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAlarms()", alarms.toString());

        return alarms;
    }

    public void deleteAlarm(String time) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ALARMS, //table name
                KEY_TIME+" = ?",  // selections
                new String[] { time }); //selections args
        db.close();
        Log.d("deleteBook", time);

    }

}
