package com.example.calendar;

public class OneStatus {

    int id;
    int year;
    int mounth;
    int day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public OneStatus(int id, int year, int mounth, int day) {
        super();
        this.id = id;
        this.year = year;
        this.mounth = mounth;
        this.day = day;
    }

    public OneStatus(int id, int position) {
        super();
        this.id = id;
        this.setPosition(position);

    }

    int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    private int position;

}
