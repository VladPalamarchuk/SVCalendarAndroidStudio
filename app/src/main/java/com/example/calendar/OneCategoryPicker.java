package com.example.calendar;

import android.widget.ImageView;

public class OneCategoryPicker {

    int id;
    ImageView image;
    String title;

    public OneCategoryPicker(int id, ImageView image, String title) {
        super();
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
