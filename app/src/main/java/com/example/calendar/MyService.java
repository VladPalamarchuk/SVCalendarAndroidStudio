package com.example.calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyService extends Service {

    public static String IS_PUSH_LOCALY = "IS_PUSH_LOCALY";
    Intent notificationIntent;
    SharedPreferences shared;
    String name;
    boolean isRun;
    // DBStorage dbStorage;
    SQLiteDatabase DB;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("", "onCreate");
        isRun = true;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("", "onStartCommand");

        isRun = true;
        Start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e("", "onDestroy service");
        Stop();
    }

    public void Start() {

        // name = shared.getString("db", "calendar.db");

        if (isRun) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            String y = FormatDate(getNowYear());
                            String m = FormatDate(getNowMounth());
                            String d = FormatDate(getNowDay());
                            String h = FormatDate(getNowHour());
                            String min = FormatDate(getNowMinute());
                            String time_push = y + "-" + m + "-" + d + " " + h
                                    + ":" + min;

                            shared = getSharedPreferences("shared",
                                    MODE_PRIVATE);
                            name = shared.getString("db", "default.db");

                            // Log.e("", "name  = " + name + " time = "
                            // + time_push);
                            ArrayList<OneEvent> arrayEvent = getEventsByPush(time_push);

                            if (arrayEvent.size() > 0) {
                                for (int i = 0; i < arrayEvent.size(); i++) {
                                    String mes =

                                            FormatDate(arrayEvent.get(i)
                                                    .getStart_date_day())
                                                    + ":"
                                                    + FormatDate(arrayEvent.get(i)
                                                    .getStart_date_mounth())
                                                    + "  "
                                                    +

                                                    FormatDate(arrayEvent.get(i)
                                                            .getStart_date_hour())
                                                    + ":"
                                                    + FormatDate(arrayEvent.get(i)
                                                    .getStart_date_minute())
                                                    + " "
                                                    + arrayEvent.get(i).getTitle();

                                    notificationIntent = new Intent(
                                            MyService.this, MainActivity.class);

                                    Log.e(getClass().toString(),
                                            "put isPush true");

                                    notificationIntent.putExtra(IS_PUSH_LOCALY,
                                            true);
                                    notificationIntent.putExtra("TITLE", mes);
                                    notificationIntent.putExtra("INFO", mes
                                            + "\n "
                                            + arrayEvent.get(i).getInfo());

                                    notificationIntent.putExtra("IDEVENT",
                                            arrayEvent.get(i).getId());

                                    notificationIntent
                                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                    Notify(mes, arrayEvent.get(i).getSound());
                                }

                            }

                        }

                    }).start();

                    Start();
                }
            }, 6 * 10000);
        }
    }

    public void Stop() {
        isRun = false;
    }

    private void Notify(String message, String sound) {
        Notification notification = new Notification(R.drawable.icon, message,
                System.currentTimeMillis());
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        try {

            notification.sound = Uri.parse(shared.getString(
                    MainActivity.SHARED_ALARM_SOUND, ""));

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            if (!sound.equals("1"))
                notification.sound = Uri.parse(sound);

        } catch (Exception e) {
            // TODO: handle exception
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "bCalendar", message,
                pendingIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(
                1, notification);
    }

    public String FormatDate(int zn) {
        if (zn < 10)
            return "0" + zn;
        return zn + "";
    }

    public int getNowYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public int getNowMounth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public int getNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public int getNowHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public int getNowMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public ArrayList<OneEvent> getEventsByPush(String push) {
        ArrayList<OneEvent> result = new ArrayList<OneEvent>();

        try {

            Cursor c = null;
            if (MainActivity.DataBase == null || MainActivity.dbStorage == null) {
                shared = getSharedPreferences("shared", MODE_PRIVATE);
                name = shared.getString("db", "default.db");
                DB = new DBStorage(this, name).getWritableDatabase();
                c = DB.rawQuery("select * from "
                        + MainActivity.dbStorage.EVENT_TABLE
                        + " where time_push = '" + push + "' ;", null);
                //
                // Log.e(getClass().toString(), "db=null name = " + name
                // + "   select * from "
                // + MainActivity.dbStorage.EVENT_TABLE
                // + " where time_push = '" + push + "' ;");

            } else {
                c = MainActivity.DataBase.rawQuery("select * from "
                        + MainActivity.dbStorage.EVENT_TABLE
                        + " where time_push = '" + push + "' ;", null);

                // LS
            }

            if (c.moveToFirst()) {
                do {

                    result.add(new OneEvent(c.getInt(c.getColumnIndex("id")), c
                            .getString(c.getColumnIndex("title")), c.getInt(c
                            .getColumnIndex("color")), c.getInt(c
                            .getColumnIndex("start_date_year")), c.getInt(c
                            .getColumnIndex("start_date_mounth")), c.getInt(c
                            .getColumnIndex("start_date_day")), c.getInt(c
                            .getColumnIndex("start_date_hour")), c.getInt(c
                            .getColumnIndex("start_date_minute")), c.getInt(c
                            .getColumnIndex("end_date_year")), c.getInt(c
                            .getColumnIndex("end_date_mounth")), c.getInt(c
                            .getColumnIndex("end_date_day")), c.getInt(c
                            .getColumnIndex("end_date_hour")), c.getInt(c
                            .getColumnIndex("end_date_minute")), c.getString(c
                            .getColumnIndex("category")), c.getString(c
                            .getColumnIndex("location")), c.getString(c
                            .getColumnIndex("info")), c.getInt(c
                            .getColumnIndex("status")), c.getString(c
                            .getColumnIndex("file_path")), c.getString(c
                            .getColumnIndex("time_push")), c.getString(c
                            .getColumnIndex("sound")), c.getInt(c
                            .getColumnIndex("isDone"))));
                } while (c.moveToNext());
                c.close();

            } else {
                c.close();

            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }
        return result;
    }

}
