package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawYear extends View {
    String[] names_mounth;
    public float height = 0;
    public float width = 0;
    Handler handler;
    Context context;
    int year;
    Thread t;
    ArrayList<ArrayList<OneDay>> root_array = new ArrayList<ArrayList<OneDay>>();
    Paint paint;
    boolean isLoadEvent;
    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    int cb = MainActivity.getInstance().shared.getInt(
            MainActivity.getInstance().SHARED_COLOR_BACKGROUND, Color.WHITE);
    int cc = MainActivity.getInstance().shared.getInt(
            MainActivity.getInstance().SHARED_COLOR_COMPONENTS, Color.BLUE);
    int cf = MainActivity.getInstance().shared.getInt(
            MainActivity.getInstance().SHARED_COLOR_FONT, Color.BLACK);

    public DrawYear(Context context, final int year, String[] names_mounth,
                    final boolean isLoadEvent) {
        super(context);
        this.year = year;
        this.names_mounth = names_mounth;
        this.context = context;
        this.isLoadEvent = isLoadEvent;
        this.setOnTouchListener(new OnSwipeTouchListener(context) {

            public void onSwipeTop() {

                YearFragment.getInstance().NextYear();
            }

            public void onSwipeRight() {

            }

            public void onSwipeLeft() {

            }

            public void onSwipeBottom() {

                YearFragment.getInstance().PrevYear();
            }

            public boolean onTouch(final View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    x1 = event.getX();
                    y1 = event.getY();

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    x2 = event.getX();
                    y2 = event.getY();
                }

                if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {

                    MainActivity.getInstance().startMounthFragment(year,
                            getNumberMounth(event.getX(), event.getY()), 1);
                    new PlaySoundButton();
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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                YearFragment.getInstance().year_name.setText(year + "");

                t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                        for (int i = 1; i <= 12; i++) {

                            root_array.add(new GetOneMounth(MainActivity
                                    .getInstance(), new OneMount(year, i))
                                    .Get(isLoadEvent));
                            handler.sendMessage(handler.obtainMessage(1, 1));
                        }

                    }
                });
                t.start();

            }
        }, 500);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = canvas.getHeight();
        width = canvas.getWidth();
        paint = new Paint();
        paint.setColor(cb);
        paint.setAlpha(127);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setAlpha(255);

        final float y = ((float) canvas.getHeight()) / 100;
        final float x = ((float) canvas.getWidth()) / 100;
        final float f = ((float) canvas.getHeight()) / 60;
        paint.setColor(cc);
        int mounth = 0;
        for (float J = 0; J < 100; J += 25) {
            for (float I = 0; I < 99; I += 33) {

                paint.setTextSize(f + 1);
                paint.setColor(cc);
                paint.setStyle(Style.FILL);
                paint.setColor(new MyColor().getColorComponents());
                canvas.drawRect((I + 2) * x, (float) (0.2 + J) * y, (I + 31)
                        * x, (float) (J + 2.5) * y, paint);

                paint.setColor(new MyColor().getColorLabel());
                paint.setTypeface(Typeface.create(Typeface.DEFAULT,
                        Typeface.BOLD));
                canvas.drawText(names_mounth[mounth], (float) (I + 16.5) * x
                        - paint.measureText(names_mounth[mounth]) / 2, y
                        * (J + 2), paint);
                mounth++;

                paint.setTypeface(Typeface.create(Typeface.DEFAULT,
                        Typeface.NORMAL));

                ArrayList<OneDay> arrayOneDay = new ArrayList<OneDay>();
                try {
                    arrayOneDay = root_array.get(mounth - 1);

                } catch (Exception e) {

                    arrayOneDay = new GetOneMounth(MainActivity.getInstance(),
                            new OneMount(year, mounth)).Get(false);
                }

                int index = 0;
                for (float j = 5 + J; j < 23 + J; j += 3) {
                    for (float i = I + 3; i < I + 31; i += 4) {
                        paint.setTextSize(f);

                        String day = "";
                        try {

                            if (arrayOneDay.get(index).getNumberOfDay() != 0) {
                                day = arrayOneDay.get(index).getNumberOfDay()
                                        + "";

                                if (Integer.parseInt(day) == MainActivity
                                        .getInstance().getNowDay()
                                        && year == MainActivity.getInstance()
                                        .getNowYear()
                                        && mounth == MainActivity.getInstance()
                                        .getNowMounth()) {
                                    paint.setColor(new MyColor()
                                            .getColorNowDay());
                                    paint.setStyle(Style.FILL);
                                    canvas.drawCircle((float) (i + 1.45) * x,
                                            (float) (j - 0.75) * y,
                                            (float) (0.75 * f), paint);
                                }
                            }

                            if (arrayOneDay.get(index).isHaveEvent()) {
                                paint.setStyle(Style.STROKE);
                                paint.setColor(new MyColor()
                                        .getColorEventYear());
                                canvas.drawCircle((float) (i + 1.45) * x,
                                        (float) (j - 0.75) * y,
                                        (float) (0.75 * f), paint);
                            }
                        } catch (Exception e) {

                        }

                        paint.setColor(cf);

                        if (arrayOneDay.get(index).isHoliday())
                            paint.setColor(new MyColor().getColorHoliday());

                        if (day.length() > 1)
                            canvas.drawText(day, i * x, y * j, paint);
                        else {
                            canvas.drawText(day, (float) (i + 0.6) * x, y * j,
                                    paint);
                        }

                        index++;
                    }
                }

            }

        }

    }

    public int getYear() {
        return year;
    }

    public int getNumberMounth(float x, float y) {
        float Xk = x / width;
        float Yk = y / height;

        if (Xk < 0.33) {
            if (Yk < 0.25)
                return 1;

            if (Yk > 0.25 && Yk < 0.5)
                return 4;
            if (Yk > 0.5 && Yk < 0.75)
                return 7;
            if (Yk > 0.75 && Yk < 2)
                return 10;
        } else if (Xk > 0.33 && Xk < 0.66) {
            if (Yk < 0.25)
                return 2;

            if (Yk > 0.25 && Yk < 0.5)
                return 5;
            if (Yk > 0.5 && Yk < 0.75)
                return 8;
            if (Yk > 0.75 && Yk < 2)
                return 11;
        } else if (Xk > 0.066 && Xk < 1) {
            if (Yk < 0.25)
                return 3;

            if (Yk > 0.25 && Yk < 0.5)
                return 6;
            if (Yk > 0.5 && Yk < 0.75)
                return 9;
            if (Yk > 0.75 && Yk < 2)
                return 12;
        }
        return 0;
    }

    public void onDestroy() {

    }

}