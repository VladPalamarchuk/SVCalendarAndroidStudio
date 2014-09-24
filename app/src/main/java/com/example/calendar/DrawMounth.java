package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class DrawMounth extends View {

    public static final int OLD_THEME = 1;
    public static final int BASE_THEME = 2;

    String[] names_mounth;
    public float height = 0;
    public float width = 0;
    Context context;
    boolean drawNumberWeek = false;
    int year;
    Handler handler;
    Canvas canvas;
    Calendar calendar;
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
    int number_week;
    int row = 0;
    int milisec = 0;
    int theme = 1;
    boolean onClick = false;
    int number_day = 0;
    int firstDraw = 0;

    float circleX = 0;

    float circleY = 0;
    int selected_day = 1;

    public DrawMounth(final Context context, final int year, final int mounth,
                      final String[] names_mounth, int theme, int selected_day) {

        super(context);

        this.theme = theme;
        this.selected_day = selected_day;
        this.year = year;
        this.names_mounth = names_mounth;
        this.context = context;
        this.mounth = mounth;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                MounthFragment.getInstance().name_mounth
                        .setText(names_mounth[mounth - 1] + " " + year);
            }
        }, 50);
        this.setOnTouchListener(new OnSwipeTouchListener(context) {

            public void onSwipeTop() {
                MounthFragment.getInstance().NextMounth();
                MainActivity.getInstance().showDialog.StopTimer();
                new PlaySoundButton();
            }

            public void onSwipeRight() {
                MainActivity.getInstance().startWeekFragment(7, 15,
                        getFirstDayInRow(SwipeX, SwipeY), mounth, year, false,
                        MainActivity.getInstance().language.NAMES_DAYS_WEEK);
                MainActivity.getInstance().showDialog.StopTimer();
                new PlaySoundButton();
            }

            public void onSwipeLeft() {
                MainActivity.getInstance().startWeekFragment(7, 15,
                        getFirstDayInRow(SwipeX, SwipeY), mounth, year, false,
                        MainActivity.getInstance().language.NAMES_DAYS_WEEK);
                MainActivity.getInstance().showDialog.StopTimer();
                new PlaySoundButton();
            }

            public void onSwipeBottom() {
                new PlaySoundButton();
                MounthFragment.getInstance().PrevMounth();
                MainActivity.getInstance().showDialog.StopTimer();
            }

            public boolean onTouch(final View v, final MotionEvent event) {
                view = v;
                boolean onTouch = false;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onTouch = true;
                    SwipeX = event.getX();
                    SwipeY = event.getY();
                    x1 = event.getX();
                    y1 = event.getY();

                    MainActivity.getInstance().showDialog.isDown = true;
                    MainActivity.getInstance().showDialog.setDate(
                            DrawMounth.this,
                            year,
                            mounth,
                            arrayOneDay.get(
                                    getIndex(event.getX(), event.getY()) - 1)
                                    .getNumberOfDay(), -1, -1);

                    MainActivity.getInstance().showDialog.StartTimer();

                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    onTouch = false;
                    x2 = event.getX();
                    y2 = event.getY();

                    if (Math.abs(x1 - x2) > 10 && Math.abs(y1 - y2) > 10)
                        MainActivity.getInstance().showDialog.StopTimer();

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    onTouch = false;
                    x2 = event.getX();
                    y2 = event.getY();
                    onTouch = true;
                    MainActivity.getInstance().showDialog.StopTimer();

                }

                if (x1 != 0 && x2 != 0 && y1 != 0 && y2 != 0 && onTouch)
                    if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {
                        onTouch = false;
                        new PlaySoundButton();

                        MounthFragment.getInstance().day = arrayOneDay.get(
                                getIndex(event.getX(), event.getY()) - 1)
                                .getNumberOfDay();

                        if (arrayOneDay.get(
                                getIndex(event.getX(), event.getY()) - 1)
                                .getNumberOfDay() != 0) {

                            MounthFragment.getInstance().selected_date.setText(MainActivity
                                    .getInstance().language.NAMES_DAYS_WEEK[getColumn(getIndex(
                                    event.getX(), event.getY())) - 1]
                                    + ", "
                                    + names_mounth[mounth - 1]
                                    + " "
                                    + +arrayOneDay
                                    .get(getIndex(event.getX(),
                                            event.getY()) - 1)
                                    .getNumberOfDay());

                            MounthFragment.getInstance().selected_date
                                    .startAnimation(AnimationUtils
                                            .loadAnimation(context,
                                                    R.anim.scaletext));
                        }

                        try {
                            if (MainActivity.getInstance().TYPE_TIP == MainActivity
                                    .getInstance().TYPE_TIP_ADD_EVENT) {
                                MainActivity.getInstance().root
                                        .removeView(MainActivity.getInstance().createEventView);

                                MainActivity.getInstance().TYPE_TIP = -1;

                            }
                        } catch (Exception e) {

                        }

                        MounthFragment.getInstance().addHeader(
                                year,
                                mounth,
                                arrayOneDay
                                        .get(getIndex(event.getX(),
                                                event.getY()) - 1)
                                        .getNumberOfDay());
                        MainActivity.getInstance().STATUSES.add(new OneStatus(
                                MainActivity.STATUS_MOUNTH, year, mounth,
                                arrayOneDay
                                        .get(getIndex(event.getX(),
                                                event.getY()) - 1)
                                        .getNumberOfDay()));
                        MounthFragment.getInstance().arrayOneEvent.clear();
                        MounthFragment.getInstance().arrayOneEventAndWeaters
                                .clear();
                        MounthFragment.getInstance().arrayOneEvent = MainActivity
                                .getInstance().getEventsByDate(
                                        year,
                                        mounth,
                                        arrayOneDay.get(
                                                getIndex(event.getX(),
                                                        event.getY()) - 1)
                                                .getNumberOfDay());

                        for (int i = 0; i < MounthFragment.getInstance().arrayOneEvent
                                .size(); i++) {
                            MounthFragment.getInstance().arrayOneEventAndWeaters.add(new OneEventAndWeater(
                                    MounthFragment.getInstance().arrayOneEvent
                                            .get(i)));
                        }

                        MounthFragment.getInstance().adapter
                                .notifyDataSetChanged();

                        onClick = true;
                        circleX = event.getRawX();
                        circleY = event.getRawY();
                        invalidate();
                        number_day = arrayOneDay.get(
                                getIndex(event.getX(), event.getY()) - 1)
                                .getNumberOfDay();
                        return false;
                    }

                return gestureDetector.onTouchEvent(event);

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                invalidate();
            }
        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                arrayOneDay = new GetOneMounth(MainActivity.getInstance(),
                        new OneMount(year, mounth)).Get(true);
                getEventsByMounth();
                handler.sendMessage(handler.obtainMessage(1, 1));
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e(getClass().toString(), "drawmounth");
        this.canvas = canvas;
        height = canvas.getHeight();
        width = canvas.getWidth();
        paint = new Paint();

        paint.setColor(MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_BACKGROUND, Color.WHITE));

        y = ((float) canvas.getHeight()) / 100;
        x = ((float) canvas.getWidth()) / 100;
        f = (((float) canvas.getHeight()) / 15);

        paint.setStyle(Style.FILL);
        paint.setTextSize(f);

        int index = 0;
        int first = 0;

        if (arrayOneDay.size() == 0) {
            arrayOneDay = new GetOneMounth(MainActivity.getInstance(),
                    new OneMount(year, mounth)).Get(false);
        }

        for (int i = 2; i < 98; i += 16) {
            for (int j = 1; j < 98; j += 14) {

                if (onClick) {

                    drawCircleTouch();
                    onClick = false;
                }

                String day = "";
                if (arrayOneDay.get(index).getNumberOfDay() != 0) {
                    day = arrayOneDay.get(index).getNumberOfDay() + "";
                }

                if (firstDraw == 1) {
                    if (arrayOneDay.get(index).getNumberOfDay() == selected_day) {
                        number_day = selected_day;
                        drawCircleTouch();

                        number_day = 0;
                    }
                }

                if (Now(year, mounth, arrayOneDay.get(index).getNumberOfDay())) {
                    paint.setColor(new MyColor().getColorComponents());
                    drawCircle(canvas, (float) (j), i,
                            new MyColor().getColorComponents(), Style.FILL);

                    Calendar c = Calendar.getInstance();
                    c.set(arrayOneDay.get(index).getNumberOfYear(), arrayOneDay
                            .get(index).getNumberOfMounth() - 1, Integer
                            .parseInt(day) - 1);

                    // MounthFragment.getInstance().selected_date
                    // .setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[c
                    // .get(Calendar.DAY_OF_WEEK) - 1]
                    // + ", "
                    // + names_mounth[mounth - 1] + " " + day);

                }
                try {

                    if (day.length() > 0 && Integer.parseInt(day) > 0) {
                        float xs;
                        if (day.length() == 1)
                            xs = (j * x) + 8 * x;
                        else
                            xs = (float) ((j * x) + (9.5) * x);

                        if (theme == OLD_THEME)
                            DrawEventRectOldTheme(canvas, xs, (i + 2) * y, xs
                                            + (6 * x), (i + 14) * y,
                                    arrayOneEventByMounth.get(index));

                        if (theme == BASE_THEME) {
                            xs = (j * x) + 8 * x;

                            DrawEventRectBaseTheme(canvas,
                                    (float) (xs - x * 3.5), (i - 2) * y,
                                    (float) (xs + 1.5 * x), (i + 4) * y,
                                    arrayOneEventByMounth.get(index));
                        }

                    }

                } catch (Exception e) {
                    // TODO: handle exception

                }
                paint.setColor(MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_FONT,
                        Color.parseColor("#959595")));

                if (Now(year, mounth, arrayOneDay.get(index).getNumberOfDay())
                        && theme == BASE_THEME)
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(new MyColor().getColorFont());

                if (arrayOneDay.get(index).isHoliday())
                    paint.setColor(MainActivity.getInstance().shared.getInt(
                            MainActivity.SHARED_COLOR_HOLIDAY, Color.RED));

                if (arrayOneDay.get(index).getNumberOfDay() < 10)
                    canvas.drawText(day, (float) (j + 5.3) * x, y * (i + 10),
                            paint);
                else
                    canvas.drawText(day, (j + 4) * x, y * (i + 10), paint);

                paint.setColor(MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_COMPONENTS, Color.BLUE));

                index++;

                if (first >= 1 && theme == OLD_THEME) {
                    canvas.drawLine(0, i * y, 100 * x, i * y, paint);

                }

                try {

                    calendar = Calendar.getInstance();
                    calendar.set(arrayOneDay.get(index).getNumberOfYear(),
                            arrayOneDay.get(index).getNumberOfMounth() - 1,
                            Integer.parseInt(day) - 1);

                    number_week = calendar.get(Calendar.WEEK_OF_YEAR);

                    drawNumberWeek = true;

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            try {
                if (drawNumberWeek) {
                    paint.setTextSize(f / 2);
                    paint.setColor(new MyColor().getColorFont());
                    canvas.drawText(number_week + "", (float) (1.5 * x), y
                            * (i + 12), paint);
                    paint.setTextSize(f);

                    drawNumberWeek = false;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

            first++;
        }

        firstDraw++;
    }

    // public void Click(Canvas canvas, int index) {
    // int k = 0;
    // for (int i = 2; i < 98; i += 16) {
    // for (int j = 1; j < 98; j += 14) {
    // k++;
    // if (k == index) {
    // if (arrayOneDay.get(index - 1).getNumberOfDay() != 0)
    // drawCircle(canvas, j, i);
    // }
    //
    // }
    // }

    // }

    public void drawCircle(Canvas canvas, float j, float i, int color,
                           Style style) {

        paint.setStyle(style);
        paint.setColor(color);
        canvas.drawCircle((float) (j + 6.65) * x, (float) (y * (i + 7.5)), f,
                paint);
        paint.setStyle(style.FILL);

    }

    public void drawCircleTouch() {

        int index = 0;
        for (int i = 2; i < 98; i += 16) {
            for (int j = 1; j < 98; j += 14) {

                String day = "";
                if (arrayOneDay.get(index).getNumberOfDay() != 0) {
                    day = arrayOneDay.get(index).getNumberOfDay() + "";
                }

                if (day.length() > 0) {

                    if (number_day == Integer.parseInt(day)) {
                        drawCircle(canvas, j, i, Color.GRAY, Style.STROKE);
                    }
                }
                index++;

            }
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

            arrayOneEventByMounth.add(MainActivity.getInstance()
                    .getEventsByDate(arrayOneDay.get(i).getNumberOfYear(),
                            arrayOneDay.get(i).getNumberOfMounth(),
                            arrayOneDay.get(i).getNumberOfDay()));

        }

    }

    public void DrawEventRectOldTheme(Canvas canvas, float xs, float ys,
                                      float xf, float yf, ArrayList<OneEvent> arrayOneEvent) {

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

    public void DrawEventRectBaseTheme(Canvas canvas, float xs, float ys,
                                       float xf, float yf, ArrayList<OneEvent> arrayOneEvent) {

        float yc = ys + (yf - ys) / 2;
        float xc = xs + (xf - xs) / 2;

        if (arrayOneEvent.size() == 1) {
            paint.setColor(arrayOneEvent.get(0).getColor());

            canvas.drawRect(xs + 2, ys + 2, xc - 2, yc - 2, paint);
        }

        if (arrayOneEvent.size() == 2) {
            paint.setColor(arrayOneEvent.get(0).getColor());

            canvas.drawRect(xs + 2, ys + 2, xc - 2, yc - 2, paint);
            paint.setColor(arrayOneEvent.get(1).getColor());

            canvas.drawRect(xc + 2, ys + 2, xf - 2, yc - 2, paint);

        }

        if (arrayOneEvent.size() == 3) {
            paint.setColor(arrayOneEvent.get(0).getColor());

            canvas.drawRect(xs + 2, ys + 2, xc - 2, yc - 2, paint);

            paint.setColor(arrayOneEvent.get(1).getColor());

            canvas.drawRect(xc + 2, ys + 2, xf - 2, yc - 2, paint);

            paint.setColor(arrayOneEvent.get(2).getColor());
            canvas.drawRect(xs + 2, yc + 2, xc - 2, yf - 2, paint);

        }

        if (arrayOneEvent.size() >= 4) {
            paint.setColor(arrayOneEvent.get(0).getColor());

            canvas.drawRect(xs + 2, ys + 2, xc - 2, yc - 2, paint);
            paint.setColor(arrayOneEvent.get(1).getColor());

            canvas.drawRect(xc + 2, ys + 2, xf - 2, yc - 2, paint);

            paint.setColor(arrayOneEvent.get(2).getColor());

            canvas.drawRect(xs + 2, yc + 2, xc - 2, yf - 2, paint);

            paint.setColor(arrayOneEvent.get(3).getColor());

            canvas.drawRect(xc + 2, yc + 2, xf - 2, yf - 2, paint);
        }

    }

    public int getColumn(int index) {
        if (index % 7 != 0)
            return index % 7;
        else
            return 7;
    }

}