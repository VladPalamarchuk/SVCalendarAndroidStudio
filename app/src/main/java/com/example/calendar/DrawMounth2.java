package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;

public class DrawMounth2 extends View {

    public float height = 0;
    public float width = 0;
    Context context;
    int year;
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
    int tap_year;
    int tap_mounth;
    int day;

    public DrawMounth2(Context context, final int year, final int mounth,
                       int day) {

        super(context);
        this.year = year;
        this.day = day;
        this.context = context;
        this.mounth = mounth;
        tap_year = year;
        tap_mounth = mounth;
        WeekFragment2.getInstance().name
                .setText(MainActivity.getInstance().language.NAMES_MOUNTH[mounth - 1]
                        + " " + year);

        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(final View v, final MotionEvent event) {

                getIndex(event.getX(), event.getY());

                setPlashkaView(row);

                final int day = getFirstDayInRow(event.getX(), event.getY());
                //
                // Log.e("", "click");
                // Log.e("", "tap_year = " + tap_year);
                // Log.e("", "tap_mounth = " + tap_mounth);
                // Log.e("", " tap day = " + day);

                WeekFragment2.getInstance().setContent(tap_year, tap_mounth,
                        day);

                Log.e("", "----");
                Log.e("", "tap_year = " + tap_year);
                Log.e("", "tap_mounth = " + tap_mounth);
                Log.e("", " tap day = " + day);

                return false;

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = canvas.getHeight();
        width = canvas.getWidth();
        paint = new Paint();

        // paint.setColor(MainActivity.getInstance().shared.getInt(
        // MainActivity.SHARED_COLOR_BACKGROUND, Color.WHITE));
        // canvas.drawRect(0, 0, width, height, paint);

        y = ((float) canvas.getHeight()) / 100;
        x = ((float) canvas.getWidth()) / 100;
        f = (((float) canvas.getHeight()) / 15);

        paint.setStyle(Style.FILL);
        paint.setTextSize(f);
        // paint.setColor(Color.RED);
        int index = 0;
        int first = 0;

        arrayOneDay = new GetOneMounth(MainActivity.getInstance(),
                new OneMount(year, mounth)).Get(true);
        setPlashkaView(getRowByDay(day));
        int first_index = 0;

        for (int i = 0; i < arrayOneDay.size(); i++) {
            if (arrayOneDay.get(i).getNumberOfDay() != 0) {
                first_index = i;
                break;
            }
        }

        for (int i = 2; i < 98; i += 16) {
            for (int j = 1; j < 98; j += 14) {

                String day = "";
                if (arrayOneDay.get(index).getNumberOfDay() != 0) {
                    day = arrayOneDay.get(index).getNumberOfDay() + "";
                }

                paint.setColor(MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_FONT, Color.BLACK));

                if (arrayOneDay.get(index).getNumberOfDay() < 10)
                    canvas.drawText(day, (float) (j + 5.3) * x, y * (i + 10),
                            paint);
                else
                    canvas.drawText(day, (j + 4) * x, y * (i + 10), paint);
                index++;

            }
            first++;
        }
        this.canvas = canvas;
    }

    public void drawRectWeek(Canvas canvas, float event_y) {

        paint.setColor(new MyColor().getColorComponents());
        paint.setAlpha(150);
        float height_row = (float) (height / 6.25);
        int row = (int) (((int) event_y / height_row));
        if (row < 6) {
            canvas.drawRect(3 * x, ((row * height_row) + 5), 97 * x,
                    (float) ((row * height_row) + f * 2.5), paint);
        }
        paint.setAlpha(255);
    }

    public int getFirstDayInRow(float x, float y) {

        int res = 0;

        int index = getIndex(x, y);
        int i = 7 * (row - 1);
        res = arrayOneDay.get(i).getNumberOfDay();

        if (res > 0) {
            tap_mounth = mounth;
            tap_year = year;

            return res;

        } else {
            while (res == 0) {
                i++;

                try {

                    res = arrayOneDay.get(i).getNumberOfDay();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("class:" + getClass().toString(), "catch:" + e);
                }

                if (i > 42)
                    break;
            }

            int prev_mounth = mounth;
            int prev_year = year;
            if (mounth == 1) {
                prev_mounth = 12;
                prev_year = year - 1;
            } else
                prev_mounth = mounth - 1;

            tap_mounth = prev_mounth;
            tap_year = prev_year;
            res = new GetCountDay().getCountDay(prev_year, prev_mounth) - i + 1;
            return res;

        }

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

    public void setPlashkaView(int row) {
        switch (row) {
            case 1:
                WeekFragment2.getInstance().root.findViewById(R.id.first_row)
                        .setVisibility(View.VISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.second_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.three_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.four_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.five_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.six_row)
                        .setVisibility(View.INVISIBLE);

                break;

            case 2:
                WeekFragment2.getInstance().root.findViewById(R.id.first_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.second_row)
                        .setVisibility(View.VISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.three_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.four_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.five_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.six_row)
                        .setVisibility(View.INVISIBLE);

                break;

            case 3:
                WeekFragment2.getInstance().root.findViewById(R.id.first_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.second_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.three_row)
                        .setVisibility(View.VISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.four_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.five_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.six_row)
                        .setVisibility(View.INVISIBLE);

                break;

            case 4:
                WeekFragment2.getInstance().root.findViewById(R.id.first_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.second_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.three_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.four_row)
                        .setVisibility(View.VISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.five_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.six_row)
                        .setVisibility(View.INVISIBLE);

                break;

            case 5:
                WeekFragment2.getInstance().root.findViewById(R.id.first_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.second_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.three_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.four_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.five_row)
                        .setVisibility(View.VISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.six_row)
                        .setVisibility(View.INVISIBLE);

                break;

            case 6:
                WeekFragment2.getInstance().root.findViewById(R.id.first_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.second_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.three_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.four_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.five_row)
                        .setVisibility(View.INVISIBLE);
                WeekFragment2.getInstance().root.findViewById(R.id.six_row)
                        .setVisibility(View.VISIBLE);

                break;

        }
    }

    public int getRowByDay(int day) {
        for (int i = 0; i < arrayOneDay.size(); i++) {
            if (arrayOneDay.get(i).getNumberOfDay() == day) {
                return i / 7 + 1;
            }
        }
        return 0;
    }
}