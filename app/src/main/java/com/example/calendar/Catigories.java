package com.example.calendar;

import android.graphics.Color;
import android.widget.ImageView;

public class Catigories {

    public int id;
    public ImageView image;
    public String title;

    public Catigories(int id, ImageView image) {
        this.id = id;
        this.image = image;
    }

    public void setImage() {
        switch (id) {

            case 1:
                image.setImageResource(R.drawable.family);

                break;
            case 2:
                image.setImageResource(R.drawable.travel);
                break;
            case 3:
                image.setImageResource(R.drawable.work);
                break;

            case 4:
                image.setImageResource(R.drawable.otdih);
                break;
            case 5:
                image.setImageResource(R.drawable.razvl);
                break;

        }
    }

    public String getName() {
        switch (id) {

            case 1:
                return MainActivity.getInstance().language.CATEGORY2_FAMILY;

            case 2:
                return MainActivity.getInstance().language.CATEGORY1_TRAVEL;

            case 3:
                return MainActivity.getInstance().language.CATEGORY3_WORK;

            case 4:
                return MainActivity.getInstance().language.CATEGORY4_OTDIX;

            case 5:
                return MainActivity.getInstance().language.CATEGORY5_RAZVL;
        }

        return "";
    }

    public int getColor() {
        switch (id) {

            case 1:

                return Color.parseColor("#ff8400");

            case 2:
                return Color.parseColor("#1eabff");

            case 3:

                return Color.parseColor("#8a117f");

            case 4:

                return Color.parseColor("#ff7a50");

            case 5:

                return Color.parseColor("#6fba00");
        }

        return Color.RED;
    }
}
