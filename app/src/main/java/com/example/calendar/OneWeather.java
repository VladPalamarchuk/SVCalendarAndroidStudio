package com.example.calendar;

import android.graphics.Bitmap;

public class OneWeather {

    int time;
    Bitmap image;
    String pogoda;

    public OneWeather(int time, Bitmap image, String pogoda) {
        super();
        this.time = time;
        this.image = image;
        this.pogoda = pogoda;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPogoda() {
        return pogoda;
    }

    public void setPogoda(String pogoda) {
        this.pogoda = pogoda;
    }

}
