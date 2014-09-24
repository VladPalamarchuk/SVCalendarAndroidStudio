package com.example.calendar;

public class OneColorPicker {

    String title;
    String key;
    int color;

    public OneColorPicker(String title, String key, int color) {
        super();
        this.title = title;
        this.key = key;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
