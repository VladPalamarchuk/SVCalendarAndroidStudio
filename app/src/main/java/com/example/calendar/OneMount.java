package com.example.calendar;

public class OneMount {

    private int number;
    private int year;

    public OneMount(int year, int number_of_mounth) {
        super();
        this.setNumberOfMounth(number_of_mounth);
        this.setNumberOfYear(year);
    }

    public int getNumberOfMounth() {
        return number;
    }

    public void setNumberOfMounth(int number) {
        this.number = number;
    }

    public int getNumberOfYear() {
        return year;
    }

    public void setNumberOfYear(int year) {
        this.year = year;
    }

}
