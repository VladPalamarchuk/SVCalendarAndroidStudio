package com.example.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBStorage extends SQLiteOpenHelper {
    public static String DB_NAME = "";
    private static final int DB_VERSION = 1;
    public static final String EVENT_TABLE = "EVENT";
    public static final String FRIENDS_TABLE = "FRIENDS_TABLE";
    public static final String FRIENDS_IN_THE_EVENT = "FRIENDS_IN_THE_EVENT";

    public static final String SETTINGS_TABLE = "SETTINGS_TABLE";
    public static final String RSS_TABLE = "RSS_TABLE";

    public static final String TEMPLATE_TABLE = "TEMPLATE_TABLE";
    public static final String STOCKS_TABLE = "STOCKS_TABLE";
    public static final String CURRENS_TABLE = "CURRENS_TABLE";

    DBStorage dbStorage;

    public DBStorage(Context context, String DB_NAME) {
        super(context, DB_NAME, null, DB_VERSION);
        this.DB_NAME = DB_NAME;

        dbStorage = this;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("create table " + EVENT_TABLE
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + " title text," + " color integer,"
                + " start_date_year integer," + "start_date_mounth integer,"
                + " start_date_day integr," + "start_date_hour integer,"
                + " start_date_minute integer," + "  end_date_year integer,"
                + "end_date_mounth integer, " + "end_date_day integr,"
                + "end_date_hour integer, " + "end_date_minute integer, "
                + "category text, " + "location text, " + "info text,"
                + "  status integer ," + " file_path text,"
                + " time_push text," + "sound text, " + "isDone integer, "
                + "start_milisec text," + " end_milisec text,"
                + "start_milisec_long text," + " end_milisec_long text);");

        db.execSQL("create table "
                + FRIENDS_TABLE
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,first_name text, last_name text, date_birthday text, type integer, contact text,location text,photo text);");

        db.execSQL("create table " + FRIENDS_IN_THE_EVENT
                + " (first_name text ,last_name text,id_event INTEGER);");

        db.execSQL("create table "
                + TEMPLATE_TABLE
                + " (id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,text text);");

        db.execSQL("create table "
                + RSS_TABLE
                + " (id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name text,src text);");

        db.execSQL("create table "
                + SETTINGS_TABLE
                + " (language integer, location integer, sound_key integer, is_am_pm integer, days_holiday text);");

        db.execSQL("create table " + STOCKS_TABLE + " (id integer);");

        db.execSQL("create table " + CURRENS_TABLE
                + " (your_currency text, interesing_currency text);");

        Log.e("", "Create database succes");
    }

    public DBStorage getInstance() {
        return dbStorage;
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        //
    }
}
