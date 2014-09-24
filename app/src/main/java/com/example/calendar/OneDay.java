package com.example.calendar;

public class OneDay {
    private int number_day;
    private int year;
    private int mounth;
    private boolean isHoliday;
    private boolean isHaveEvent;

    public OneDay(int year, int mounth, int number_day, boolean isHoliday, boolean isHaveEvent) {
        super();
        this.isHaveEvent = isHaveEvent;
        this.number_day = number_day;
        this.year = year;
        this.mounth = mounth;
        this.isHoliday = isHoliday;
    }

    public int getNumberOfDay() {
        return number_day;
    }

    public void setNumberOfDay(int number_day) {
        this.number_day = number_day;
    }

    int getNumberOfYear() {
        return year;
    }

    void setNumberOfYear(int year) {
        this.year = year;
    }

    int getNumberOfMounth() {
        return mounth;
    }

    void setNumberOfMounth(int mounth) {
        this.mounth = mounth;
    }

    boolean isHoliday() {
        return isHoliday;
    }

    void setHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean isHaveEvent() {
        return isHaveEvent;
    }

    public void setHaveEvent(boolean isHaveEvent) {
        this.isHaveEvent = isHaveEvent;
    }

}
