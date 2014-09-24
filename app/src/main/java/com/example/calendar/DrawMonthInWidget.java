package com.example.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class DrawMonthInWidget extends View {
    String[] names_mounth;
    public float height = 0;
    public float width = 0;
    Context context;
    int year;
    DBStorage dbStorage;
    SQLiteDatabase DB;
    Canvas canvas;
    Paint paint;
    ArrayList<OneDay> arrayOneDay = new ArrayList<OneDay>();
    ArrayList<ArrayList<OneEvent>> arrayOneEventByMounth = new ArrayList<ArrayList<OneEvent>>();
    int mounth;
    float y = 0;
    Handler popup_handler;
    float x = 0;
    float f = 0;
    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    Timer timer;
    View view;
    float SwipeX, SwipeY;
    int row = 0;
    int milisec = 0;
    Languages languages;
    SharedPreferences shared;

    public DrawMonthInWidget(Context context, final int year, final int mounth) {

        super(context);
        this.year = year;
        this.names_mounth = names_mounth;
        this.context = context;
        this.mounth = mounth;
        shared = context.getSharedPreferences("shared", context.MODE_PRIVATE);
        languages = new Languages(
                shared.getInt(MainActivity.SHARED_LANGUAGE, 1));

        shared = context.getSharedPreferences("shared", context.MODE_PRIVATE);
        String name_db = shared.getString("db", "qDB.db");
        dbStorage = new DBStorage(context, name_db);
        DB = dbStorage.getWritableDatabase();
        int id_lang = shared.getInt(MainActivity.SHARED_LANGUAGE, 0);
        names_mounth = new Languages(id_lang).NAMES_MOUNTH;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = canvas.getHeight();
        width = canvas.getWidth();
        paint = new Paint();

        paint.setColor(shared.getInt(MainActivity.SHARED_COLOR_BACKGROUND,
                Color.WHITE));

        paint.setAlpha(150);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setAlpha(255);
        y = ((float) canvas.getHeight()) / 100;
        x = ((float) canvas.getWidth()) / 100;
        f = (((float) canvas.getHeight()) / 15);

        paint.setStyle(Style.FILL);
        paint.setTextSize(f);
        // paint.setColor(Color.RED);
        int index = 0;
        int first = 0;

        arrayOneDay = new GetOneMounth(context, new OneMount(year, mounth))
                .Get(true);
        getEventsByMounth();
        for (int i = 2; i < 98; i += 16) {
            for (int j = 1; j < 98; j += 14) {

                String day = "";
                if (arrayOneDay.get(index).getNumberOfDay() != 0) {
                    day = arrayOneDay.get(index).getNumberOfDay() + "";
                }

                if (Now(year, mounth, arrayOneDay.get(index).getNumberOfDay())) {
                    paint.setColor(new MyColor().getColorNowDay());
                    drawCircle(canvas, j, i);

                    Calendar c = Calendar.getInstance();
                    c.set(arrayOneDay.get(index).getNumberOfYear(), arrayOneDay
                            .get(index).getNumberOfMounth() - 1, Integer
                            .parseInt(day) - 1);

                    // MounthFragment.getInstance().selected_date
                    // .setText(languages.NAMES_DAYS_WEEK[c
                    // .get(Calendar.DAY_OF_WEEK) - 1]
                    // + ", "
                    // + names_mounth[mounth - 1] + " " + day);

                }

                if (arrayOneDay.get(index).isHaveEvent()) {

                    float xs;
                    if (day.length() == 1)
                        xs = (j * x) + 8 * x;
                    else
                        xs = (float) ((j * x) + (9.5) * x);
                    DrawEventRect(canvas, xs, (i + 2) * y, xs + (6 * x),
                            (i + 14) * y, arrayOneEventByMounth.get(index));

                }
                paint.setColor(shared.getInt(MainActivity.SHARED_COLOR_FONT,
                        Color.BLACK));

                if (arrayOneDay.get(index).getNumberOfDay() < 10)
                    canvas.drawText(day, (float) (j + 5.3) * x, y * (i + 10),
                            paint);
                else
                    canvas.drawText(day, (j + 4) * x, y * (i + 10), paint);
                index++;

                if (first >= 1) {
                    canvas.drawLine(0, i * y, 100 * x, i * y, paint);

                }
            }
            first++;
        }
        this.canvas = canvas;

    }

    public void Click(Canvas canvas, int index) {
        int k = 0;
        for (int i = 2; i < 98; i += 16) {
            for (int j = 1; j < 98; j += 14) {
                k++;
                if (k == index) {
                    if (arrayOneDay.get(index - 1).getNumberOfDay() != 0)
                        drawCircle(canvas, j, i);
                }

            }
        }

    }

    public void drawCircle(Canvas canvas, float j, float i) {

        paint.setStyle(Style.FILL);
        canvas.drawCircle((float) (j + 6.5) * x, (float) (y * (i + 7.5)), f,
                paint);

    }

    public void drawCircleEvent(Canvas canvas, float j, float i) {
        paint.setColor(shared.getInt(MainActivity.SHARED_COLOR_COMPONENTS,
                Color.BLUE));

        paint.setStyle(Style.STROKE);
        canvas.drawCircle((float) (j + 6.5) * x, (float) (y * (i + 7.5)), f,
                paint);

    }

    public int getIndex(float x, float y) {
        float Xk = x / width;
        float Yk = y / height;
        int c = 0;

        if (Yk < 0.18) {

            c = 0;
            row = 1;
        } else if (Yk > 0.18 && Yk < 0.34) {
            c = 1;
            row = 2;
        } else if (Yk > 0.34 && Yk < 0.50) {
            c = 2;
            row = 3;
        } else if (Yk > 0.50 && Yk < 0.66) {
            c = 3;
            row = 4;
        } else if (Yk > 0.66 && Yk < 0.82) {
            c = 4;
            row = 5;
        } else if (Yk > 0.82 && Yk < 1) {
            c = 5;
            row = 6;
        }

        if (Xk < 0.15) {
            return c * 7 + 1;
        } else if (Xk > 0.15 && Xk < 0.29) {
            return c * 7 + 2;
        } else if (Xk > 0.29 && Xk < 0.43) {
            return c * 7 + 3;
        } else if (Xk > 0.43 && Xk < 0.57) {
            return c * 7 + 4;
        } else if (Xk > 0.57 && Xk < 0.71) {
            return c * 7 + 5;
        } else if (Xk > 0.71 && Xk < 0.85) {

            return c * 7 + 6;
        } else if (Xk > 0.85 && Xk < 1) {

            return c * 7 + 7;
        }

        return 0;

    }

    public boolean Now(int year, int mounth, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String now = sdf.format(new Date());

        int y = Integer.parseInt(now.substring(0, 4));
        int m = Integer.parseInt(now.substring(5, 7));
        int d = Integer.parseInt(now.substring(8));

        if (year == y && mounth == m && day == d) {
            return true;
        }
        return false;
    }

    public OneDay getDay(int index) {
        index--;
        return arrayOneDay.get(index);
    }

    public int getYear() {
        return year;
    }

    public int getFirstDayInRow(float x, float y) {

        int index = getIndex(x, y);
        int i = 7 * (row - 1);
        int res = arrayOneDay.get(i).getNumberOfDay();
        if (res > 0)
            return res;
        else {
            while (res == 0) {
                i++;
                res = arrayOneDay.get(i).getNumberOfDay();
            }
            return res;
        }

    }

    public String[] getNamesMounth() {
        return names_mounth;
    }

    public int getMounth() {
        return mounth;
    }

    public void getEventsByMounth() {
        arrayOneEventByMounth.clear();
        for (int i = 0; i < arrayOneDay.size(); i++) {
            if (arrayOneDay.get(i).isHaveEvent()) {
                arrayOneEventByMounth.add(getEventsByDate(arrayOneDay.get(i)
                        .getNumberOfYear(), arrayOneDay.get(i)
                        .getNumberOfMounth(), arrayOneDay.get(i)
                        .getNumberOfDay()));
            } else {
                arrayOneEventByMounth.add(null);
            }
        }
    }

    public void DrawEventRect(Canvas canvas, float xs, float ys, float xf,
                              float yf, ArrayList<OneEvent> arrayOneEvent) {
        float height_rect;
        if (arrayOneEvent.size() < 8)
            height_rect = (yf - ys) / 8;
        else
            height_rect = (yf - ys) / arrayOneEvent.size();
        for (int i = 0; i < arrayOneEvent.size(); i++) {

            paint.setColor(arrayOneEvent.get(i).getColor());
            canvas.drawRect(xs, ys + 2, xf, ys + height_rect, paint);
            ys += height_rect;

        }
    }

    public int getColumn(int index) {
        if (index % 7 != 0)
            return index % 7;
        else
            return 7;
    }

    public ArrayList<OneEvent> getEventsByDate(int year, int mounth, int day) {
        ArrayList<OneEvent> result = new ArrayList<OneEvent>();
        Cursor c = DB.rawQuery("select * from " + dbStorage.EVENT_TABLE
                + " where " + "start_date_year = " + year
                + " AND start_date_mounth = " + mounth
                + "  AND start_date_day = " + day + " ;", null);

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
            return result;
        } else {
            c.close();
            return result;
        }

    }

}