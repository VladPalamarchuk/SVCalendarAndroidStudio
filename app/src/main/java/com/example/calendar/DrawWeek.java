package com.example.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class DrawWeek extends View {
    int count_row;
    View view;
    int count_col;
    Paint paint;
    int height;
    int width;
    float y, x, f;
    Canvas canvas;
    int first_day;
    int number_mounth;
    int count_day;
    int year;
    float x1, x2, y1, y2;
    int StartNext = 0;
    int static_mounth;
    int static_year;
    boolean prev = false;
    boolean not = false;
    int prev_mounth;
    int static_count_day;
    boolean timer;
    int milisec;
    int day_for_event;
    int mounth_for_event;
    int hour_for_event;
    RelativeLayout root;
    OneEvent event;
    String[] names_days;
    String[] names_days_sokr;

    public DrawWeek(Context context, final int count_col, int first_day,
                    final int number_mounth, final int year, boolean prev,
                    String[] names_days, String[] names_days_sokr) {
        super(context);
        // TODO Auto-generated constructor stub
        this.count_col = count_col;
        this.count_row = 15;
        this.first_day = first_day;
        this.names_days = names_days;
        this.number_mounth = number_mounth;
        this.year = year;
        this.names_days_sokr = names_days_sokr;
        view = this;
        this.static_mounth = number_mounth;
        this.prev = prev;
        static_year = year;
        count_day = new GetCountDay().getCountDay(year, number_mounth);
        static_count_day = count_day;

        this.setOnTouchListener(new OnSwipeTouchListener(context) {

            public void onSwipeTop() {
                MainActivity.getInstance().showDialog.StopTimer();
            }

            public void onSwipeRight() {
                WeekFragment.getInstance().PrevWeek();
                MainActivity.getInstance().showDialog.StopTimer();
            }

            public void onSwipeLeft() {
                WeekFragment.getInstance().NextWeek();
                MainActivity.getInstance().showDialog.StopTimer();
            }

            public void onSwipeBottom() {
                MainActivity.getInstance().showDialog.StopTimer();
            }

            public boolean onTouch(final View v, MotionEvent event) {
                boolean isOpen = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    x1 = event.getX();
                    y1 = event.getY();

                    MainActivity.getInstance().showDialog.isDown = true;
                    MainActivity.getInstance().showDialog.setDate(
                            DrawWeek.this, year, number_mounth,
                            getDay(event.getX(), event.getY()),
                            getHourByRow(event.getX(), event.getY()), 0);

                    MainActivity.getInstance().showDialog.StartTimer();

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    x2 = event.getX();
                    y2 = event.getY();
                    if (MainActivity.getInstance().showDialog.isShow())
                        isOpen = false;
                    else
                        isOpen = true;
                    MainActivity.getInstance().showDialog.StopTimer();

                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    x2 = event.getX();
                    y2 = event.getY();

                    if (Math.abs(x1 - x2) > 10 && Math.abs(y1 - y2) > 10)
                        MainActivity.getInstance().showDialog.StopTimer();
                }

                if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {
                    if (isOpen)
                        getIndex(event.getX(), event.getY());

                    new PlaySoundButton();

                    return false;
                }

                return gestureDetector.onTouchEvent(event);

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.canvas = canvas;

        paint = new Paint();

        height = canvas.getHeight();
        width = canvas.getWidth();
        paint.setColor(new MyColor().getColorBacground());
        paint.setAlpha(127);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setAlpha(255);
        count_row = 17;
        y = ((float) canvas.getHeight()) / 100;
        x = ((float) canvas.getWidth()) / 100;
        f = (((float) canvas.getHeight()) / 30);

        float lastX = 0;
        float secondX = 0;
        int r = 0;
        paint.setColor(new MyColor().getColorComponents());

        for (float i = 0; i < 99; i += 100 / count_row) {
            r++;

            if (r == 3) {
                secondX = i * y;
            }
            if (r != 1 && r != 2)
                canvas.drawLine(0, i * y, width, i * y, paint);
            lastX = i * y;
        }

        for (float i = 0; i < 99; i += 100 / count_col) {
            canvas.drawLine(i * x, secondX, i * x, lastX, paint);

        }
        paint.setColor(new MyColor().getColorFont());
        int k = 0;
        boolean d = false;
        int text = first_day;
        int t = 0;
        for (float i = 0; i < 99; i += 100 / count_col) {
            paint.setTextSize(f);
            t++;

            Calendar c = Calendar.getInstance();
            c.set(year, number_mounth - 1, text - 1);

            String text_date = String.valueOf(text);
            text_date = setWidhTextday(c.get(Calendar.DAY_OF_WEEK) - 1,
                    text_date);

            ArrayList<Integer> a = MainActivity.getHolidays();

            if (a.contains(c.get(Calendar.DAY_OF_WEEK) - 1))
                paint.setColor(new MyColor().getColorHoliday());

            canvas.drawText(
                    text_date,
                    (i + 100 / count_col / 2) * x
                            - (paint.measureText(text_date) / 2),
                    (100 / count_row / 2) * y + (float) (f / 1.5), paint);
            try {

                for (int w = 0; w < MainActivity.getInstance().WEATHERS.size(); w++) {
                    if (MainActivity.getInstance().WEATHERS.get(w).isDate(year,
                            number_mounth, text)) {
                        if (MainActivity.getInstance().WEATHERS.get(w)
                                .getImageByWeek() != null)
                            canvas.drawBitmap(
                                    MainActivity.getInstance().WEATHERS.get(w)
                                            .getImageByWeek(), i * x,

                                    (100 / count_row / 2) * y + f,

                                    paint);
                        int width_image = MainActivity.getInstance().WEATHERS
                                .get(w).getImageByWeek().getWidth();

                        if (count_col < 8) {
                            paint.setTextSize((float) (f / 1.5));
                            canvas.drawText(
                                    MainActivity.getInstance().WEATHERS.get(w)
                                            .getPogodaTextByWeek(),
                                    (float) (i * x + width_image + 1),
                                    (float) ((100 / count_row / 2) * y + 1.9 * f),
                                    paint);
                            paint.setTextSize(f);
                        }
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e(getClass().toString()
                        + "line = "
                        + Thread.currentThread().getStackTrace()[2]
                        .getLineNumber(), "catch:" + e);
            }

            if (text == count_day) {
                text = 0;
                d = true;
                if (t == count_col)
                    not = true;
                if (number_mounth == 12) {
                    number_mounth = 1;
                    year++;
                } else
                    number_mounth++;
                count_day = new GetCountDay().getCountDay(year, number_mounth);

            }

            try {
                ArrayList<OneEvent> arrayOneEvent = new ArrayList<OneEvent>();

                arrayOneEvent = MainActivity.getInstance().getEventsByDate(
                        year, number_mounth, text);
                if (arrayOneEvent != null) {

                    boolean isFullday = false;
                    int number_full_day = 0;

                    int full_day_count = 0;
                    int event_day_count = 0;

                    for (int l = 0; l < arrayOneEvent.size(); l++) {
                        event = arrayOneEvent.get(l);

                        if (event.getStart_date_hour() < 8
                                && event.getEnd_date_hour() == 23)
                            full_day_count++;
                        else
                            event_day_count++;
                    }

                    for (int l = 0; l < arrayOneEvent.size(); l++) {

                        event = arrayOneEvent.get(l);
                        isFullday = false;

                        if (event.getStart_date_hour() < 8
                                && event.getEnd_date_hour() == 23)
                            isFullday = true;

                        if (isFullday)
                            number_full_day++;

                        DrawEvent(
                                event.getStart_date_day(),
                                event_day_count,
                                event.getStart_date_hour(),
                                event.getEnd_date_hour()
                                        - event.getStart_date_hour(),
                                event.getTitle(), event.getColor(), isFullday,
                                l, number_full_day, full_day_count);
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

            // }

            paint.setColor(new MyColor().getColorFont());

            text++;

            if (d)
                k++;

        }
        if (d) {
            if (k != 1)
                StartNext = k - 1;
            else
                StartNext = k;
        } else
            StartNext = first_day + count_col;

    }

    public int getDay(float x, float y) {
        float Xk = x / width;
        float Yk = y / height;
        ArrayList<Float> Xarray = new ArrayList<Float>();
        ArrayList<Float> Yarray = new ArrayList<Float>();

        float I = 100 / count_col;
        float J = 100 / count_row;

        for (float i = 0; i <= I * count_col; i += I) {
            Xarray.add(i);

        }

        for (float i = 0; i <= 100; i += J) {
            Yarray.add(i);
        }

        int i = getIndexInArray(Xarray, Xk);
        return GetDayByColumn(i);
    }

    public int getHourByRow(float x, float y) {
        float Xk = x / width;
        float Yk = y / height;
        ArrayList<Float> Xarray = new ArrayList<Float>();
        ArrayList<Float> Yarray = new ArrayList<Float>();

        float I = 100 / count_col;
        float J = 100 / count_row;

        for (float i = 0; i <= I * count_col; i += I) {
            Xarray.add(i);

        }

        for (float i = 0; i <= 100; i += J) {
            Yarray.add(i);
        }

        int j = getIndexInArray(Yarray, Yk);

        return getHour(j);
    }

    public void getIndex(float x, float y) {
        float Xk = x / width;
        float Yk = y / height;
        ArrayList<Float> Xarray = new ArrayList<Float>();
        ArrayList<Float> Yarray = new ArrayList<Float>();

        float I = 100 / count_col;
        float J = 100 / count_row;

        for (float i = 0; i <= I * count_col; i += I) {
            Xarray.add(i);

        }

        for (float i = 0; i <= 100; i += J) {
            Yarray.add(i);
        }

        int i = getIndexInArray(Xarray, Xk);
        int j = getIndexInArray(Yarray, Yk);

        int mounth = number_mounth;
        if (GetDayByColumn(i) < first_day) {
            mounth++;
        }

        ArrayList<OneEvent> arrayOneEvent = MainActivity.getInstance()
                .getCountEventDayHour(year, mounth, GetDayByColumn(i),
                        getHour(j));

        if (arrayOneEvent.size() == 1) {

            OneEventAndWeater oneWeatherDay = new OneEventAndWeater(
                    arrayOneEvent.get(0));

            // TODO Auto-generated method stub
            MounthFragment.getInstance().arrayOneEventAndWeaters.clear();
            MounthFragment.getInstance().arrayOneEventAndWeaters
                    .add(oneWeatherDay);

            if (MounthFragment.getInstance().arrayOneEventAndWeaters.size() > 0)
                MainActivity.getInstance().startOneEventFragment(0);
        } else if (arrayOneEvent.size() > 1) {

            MainActivity.getInstance().startListEventOfDay(year, mounth,
                    GetDayByColumn(i));

        }

    }

    ContentValues getRowsEvent(double start, float duration) {
        ContentValues cv = new ContentValues();
        start = Math.round(start);
        float sum = (float) (start + duration);
        if (sum > 22)
            sum = 24;

        if (start < 8)
            start = 8;

        float ys = (float) ((start - 5) * (100 / count_row));
        float yf = (float) (((sum) - 5) * (100 / count_row));

        cv.put("YS", ys);
        cv.put("YF", yf);
        return cv;
    }

    public int getHour(int j) {
        return j + 5;

    }

    public int getIndexInArray(ArrayList<Float> array, float value) {
        int res = 0;
        value = value * 100;

        for (int i = 1; i < array.size(); i++) {
            if (value < array.get(i) && value > array.get(i - 1)) {
                res = i - 1;
            }
        }

        return res;
    }

    public int GetDayByColumn(int i) {
        int res = 0;
        int pr_m = number_mounth;
        int pr_y = year;
        int pr_d = count_day;
        number_mounth = static_mounth;
        year = static_year;
        count_day = static_count_day;

        res = first_day + i;
        if (res > static_count_day)
            res = i - static_count_day + first_day;
        number_mounth = pr_m;
        year = pr_y;
        count_day = pr_d;
        return res;
    }

    public int GetColumnByDay(int NumberDay) {
        int res = 0;

        int pr_m = number_mounth;
        int pr_y = year;
        int pr_d = count_day;
        number_mounth = static_mounth;
        year = static_year;
        count_day = static_count_day;

        res = NumberDay - first_day;
        if (res < 0) {
            res = count_day + res;
        }

        number_mounth = pr_m;
        year = pr_y;
        count_day = pr_d;
        return res;

    }

    public int getMounth() {
        return number_mounth;
    }

    public int getStartNext() {
        return StartNext;
    }

    public int getYear() {
        return year;
    }

    public int getCountCol() {
        return count_col;
    }

    public int getCountRow() {
        return count_row;
    }

    public int getFirstDay() {
        return first_day;
    }

    public int getStaticMounth() {
        return static_mounth;
    }

    public int getStaticYear() {
        return static_year;
    }

    public int getStartPrev() {

        if (first_day <= count_col) {

            ChangeDate();
            count_day = new GetCountDay().getCountDay(year, number_mounth);
            first_day = count_day - count_col + first_day;
        } else
            first_day = first_day - count_col;

        return first_day;
    }

    String getNameMounts() {

        if (static_mounth == number_mounth || not) {
            return first_day
                    + " - "
                    + String.valueOf(first_day + count_col - 1)
                    + " "
                    + MainActivity.getInstance().language.NAMES_MOUNTH[static_mounth - 1];
        } else {
            if (static_mounth > number_mounth) {
                return MainActivity.getInstance().language.NAMES_MOUNTH[number_mounth - 1]
                        + " - "
                        + MainActivity.getInstance().language.NAMES_MOUNTH[static_mounth - 1];
            } else {

                return first_day
                        + " "
                        + MainActivity.getInstance().language.NAMES_MOUNTH[static_mounth - 1]
                        + " - "
                        + String.valueOf(count_col
                        - (static_count_day - first_day) - 1)
                        + " "
                        + MainActivity.getInstance().language.NAMES_MOUNTH[number_mounth - 1];

            }
        }
    }

    public void ChangeDate() {
        if (prev) {
            if (number_mounth == 1) {
                number_mounth = 12;
                year--;
            } else {
                number_mounth--;
            }
        } else {
            if (number_mounth == 12) {
                number_mounth = 1;
                year++;
            } else
                number_mounth++;
        }

    }

    public int getMounthPrev() {
        return 0;
    }

    public void DrawEvent(int number_day, int status, double start,
                          float duration, String title, int color, boolean isFullDay,
                          int number, int number_full_day, int full_day_count) {
        float width_col = 100 / count_col;
        float height_row = 100 / count_row;
        float width_event = (width_col / status);

        int col = GetColumnByDay(number_day);

        if (!isFullDay) {

            float xs = 0;
            float xf = 0;
            if (status == 1) {
                xs = (col * width_col);
                xf = (xs + width_col);
            } else {

                xs = (col * width_col + number * width_event);

                xf = (xs + width_event);

            }
            ContentValues cv = getRowsEvent(start, duration);
            float ys = (Float) cv.get("YS");
            float yf = (Float) cv.get("YF");
            paint.setColor(color);
            paint.setAlpha(200);
            canvas.drawRect(xs * x, ys * y, xf * x, yf * y, paint);

            paint.setColor(color);

            paint.setAlpha(255);

            canvas.drawRect(xs * x, ys * y, (xs) * x + 5, yf * y, paint);

            paint.setTextSize((float) (f / 2.5));
            paint.setColor(new MyColor().getColorFont());

            String text = MainActivity.getInstance().FormatDate(
                    event.getStart_date_hour())
                    + ":"
                    + MainActivity.getInstance().FormatDate(
                    event.getStart_date_minute());

            float center = (xs + ((xf - xs) / 2)) * x - paint.measureText(text)
                    / 2;

            canvas.drawText(text, center, (float) (ys + 1.5) * y, paint);

            paint.setTextSize((float) (f / 1.5));

            drawTitle(title, x * (xf - xs), (float) (xs + 1.5) * x,
                    (float) (ys + 4) * y, yf);
        } else {

            paint.setColor(color);
            paint.setAlpha(200);
            float height_event = (height_row / full_day_count);
            float xs = (col) * width_col;
            float xf = xs + width_col;

            float ys = (100 / count_row) + number_full_day * height_event;

            if (full_day_count > 1)
                ys = 2 * (100 / count_row) + (number_full_day - 1)
                        * height_event;

            float yf = ys + height_event;

            Log.e(getClass().toString(), "Nfd = " + number_full_day + "ys = "
                    + ys + " yf = " + yf);

            canvas.drawRect(xs * x, ys * y, xf * x, yf * y, paint);

            paint.setColor(new MyColor().getColorFont());

            paint.setTextSize((float) (f / 1.5));
            if (height_event * y - 2 < f / 1.5)
                paint.setTextSize(height_event * y - 2);

            float yc = y * (ys + (yf - ys) / 2) + paint.getTextSize() / 2;
            canvas.drawText(setWidthText(title, xf * x - xs * x), xs * x + 5,
                    yc, paint);

        }
    }

    public void onDestroy() {

        invalidate();
        canvas.restore();
        view = null;
        clearAnimation();
        clearFocus();

    }

    public void DrawRow(int number_column, boolean top) {
        float W = 100 / count_col;
        float H = 100 / count_row;
        float X = W / 100;
        float Y = H / 100;
        if (top) {

        } else {
        }
    }

    public void drawTitle(String text, float width, float xs, float ys, float yf) {
        if (paint.measureText(text) < width - 1)
            canvas.drawText(text, xs, ys, paint);
        String draw_text = "";
        int index = 0;

        paint.setTextSize((float) (f / 1.5));

        while (index < text.length()) {

            while (paint.measureText(draw_text) < width - 3 * x) {

                if (index < text.length()) {
                    draw_text += text.charAt(index);
                    index++;

                } else {
                    break;
                }
            }
            if (ys < yf * y) {
                canvas.drawText(draw_text, xs, ys, paint);
                ys += f / 1.5;
                draw_text = "";
            } else
                break;

        }

    }

    boolean sokr;

    public String setWidhTextday(int index, String text) {


        if (count_col <= 2) {
            return text + " " + names_days[index];
        } else if (count_col > 2 && count_col <= 7) {
            return text + " " + names_days_sokr[index];
        } else
            return text;
    }

    public String setWidthText(String text, float width) {

        paint.setColor(new MyColor().getColorFont());
        if (paint.measureText(text) < width)
            return text;
        while (paint.measureText(text) > width - paint.measureText("...") - 5) {
            text = text.substring(0, text.length() - 1);

        }

        return text + "...";
    }

    public String[] getNameDays() {
        return names_days;
    }

    public int getPrevMounth() {

        if (number_mounth < static_mounth)
            return number_mounth;
        else
            return static_mounth;

    }

}
