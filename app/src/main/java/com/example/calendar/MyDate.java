package com.example.calendar;

public class MyDate {

    String date;

    public MyDate(String yyyy_mm_dd_hh_mm) {
        this.date = yyyy_mm_dd_hh_mm;
    }

    public String getDate() {

        return getYear() + "-" + getMounth() + "-" + getDay() + " " + getHour()
                + ":" + getMinute();

    }

    public int getYear() {

        return Integer.parseInt(date.substring(0, 4));
    }

    public int getMounth() {
        return Integer.parseInt(date.substring(5, 7));
    }

    public int getDay() {
        return Integer.parseInt(date.substring(8, 10));
    }

    public int getHour() {
        return Integer.parseInt(date.substring(11, 13));
    }

    public int getMinute() {
        return Integer.parseInt(date.substring(14, 16));
    }

}
