package com.example.calendar;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class OneWeatherDay {

    ArrayList<OneWeather> weatersDay;
    int day;
    int year;
    int mounth;

    boolean isDate(int year, int mounth, int day) {
        if (year == this.year && mounth == this.mounth && day == this.day)
            return true;
        return false;
    }

    public Bitmap getImageByWeek() {
        if (weatersDay.size() != 0)
            return weatersDay.get(0).getImage();
        return null;
    }

    public String getPogodaTextByWeek() {
        int min = 100;
        int max = -100;
        int p1 = 0;
        int p2 = 0;
        try {

            for (int i = 0; i < 4; i++) {

                // response text : +13,+14
                String text = weatersDay.get(i).getPogoda();

                p1 = Integer.parseInt(text.substring(1, 3));
                p2 = Integer.parseInt(text.substring(5, 7));

                if (p1 < min)
                    min = p1;
                if (p1 > max)
                    max = p1;

                if (p2 < min)
                    min = p2;
                if (p2 > max)
                    max = p2;

            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }
        return max + "/" + min;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMounth() {
        return mounth;
    }

    public void setMounth(int mounth) {
        this.mounth = mounth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public OneWeatherDay(ArrayList<OneWeather> weatersDay, int year,
                         int mounth, int day) {
        super();
        this.weatersDay = weatersDay;
        this.mounth = mounth;
        this.year = year;
        this.day = day;
    }

    public ArrayList<OneWeather> getWeatersDay() {
        return weatersDay;
    }

    public void setWeatersDay(ArrayList<OneWeather> weatersDay) {
        this.weatersDay = weatersDay;
    }

}
