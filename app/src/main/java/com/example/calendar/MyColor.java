package com.example.calendar;

import android.graphics.Color;
import android.util.Log;

public class MyColor {

    public int getColorBacground() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_BACKGROUND, Color.WHITE);
    }

    public int getColorComponents() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_COMPONENTS, -2130706432);
    }

    public int getColorFont() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_FONT, Color.parseColor("#6a6a6a"));
    }

    public int getColorLabel() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_LABEL, Color.WHITE);
    }

    public int getColorEventYear() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_EVENT_YEAR, Color.RED);
    }

    public int getColorEventMounth() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_EVENT_MOUNTH, Color.RED);
    }

    public int getColorHoliday() {
        return MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_COLOR_HOLIDAY, Color.RED);
    }

    public int getColorNowDay() {
        try {

            return MainActivity.getInstance().shared.getInt(
                    MainActivity.SHARED_COLOR_NOW_DATE, Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
            return Color.RED;
        }
    }

}
