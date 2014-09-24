package com.example.calendar;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calendar.getWeatherByDay.OnCompleteListener;

public class OneEventAndWeater {

    OneEvent oneEvent;
    public Bitmap image = null;
    public String pogoda = "";
    Handler handler;
    private ImageView imageView;

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

    OneWeatherDay oneWeatherDay;
    boolean isHaveWeather;

    public OneEvent getOneEvent() {
        return oneEvent;
    }

    public void setOneEvent(OneEvent oneEvent) {
        this.oneEvent = oneEvent;
    }

    public OneWeatherDay getOneWeatherDay() {
        return oneWeatherDay;
    }

    public void setOneWeatherDay(OneWeatherDay oneWeatherDay) {
        this.oneWeatherDay = oneWeatherDay;
    }

    public boolean isHaveWeather() {
        return isHaveWeather;
    }

    public void setHaveWeather(boolean isHaveWeather) {
        this.isHaveWeather = isHaveWeather;
    }

    public OneEventAndWeater(final OneEvent oneEvent) {
        this.oneEvent = oneEvent;


    }

    public void run() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    final getWeatherByDay gwbd = new getWeatherByDay(
                            Integer.parseInt(oneEvent.getLocation()),
                            oneEvent.getStart_date_year(),
                            oneEvent.getStart_date_mounth(),
                            oneEvent.getStart_date_day());

                    gwbd.setOnCompleteListener(new OnCompleteListener() {

                        @Override
                        public void Complete() {

                            if (gwbd.isHavePrognosis) {

                                OneWeatherDay oneWeatherDay = gwbd
                                        .GetOneWeatherDay();

                                int index = 0;
                                int start_hour = oneEvent.getStart_date_hour();
                                int end_hour = oneEvent.getEnd_date_hour();
                                if (start_hour >= 7 && start_hour <= 12)
                                    index = 0;
                                if (start_hour > 12 && start_hour <= 18)
                                    index = 1;
                                if (start_hour > 18 && start_hour <= 21)
                                    index = 2;
                                if (start_hour > 21 || start_hour < 7)
                                    index = 3;

                                if (start_hour < 8 && end_hour > 22)
                                    index = 1;


                                pogoda = oneWeatherDay.getWeatersDay()
                                        .get(index).getPogoda();
                                image = oneWeatherDay.getWeatersDay()
                                        .get(index).getImage();

//                                imageView.setImageBitmap(image);

                            }

                        }
                    });

                    gwbd.Parse();

                } catch (Exception e) {
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }
            }
        }).start();
    }

    public void setImage(final ImageView imageView) {

//        this.imageView = imageView;

        if (image != null) {
            imageView.setImageBitmap(image);
            imageView.setVisibility(View.VISIBLE);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
//                    onImageLoaded.load(image);
                    setImage(imageView);
                }
            }, 1000);
        }
    }

    public void setText(final TextView textView) {

        if (pogoda.length() != 0) {
            textView.setText(pogoda);

            textView.setVisibility(View.VISIBLE);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    setText(textView);
                }
            }, 100);
        }
    }

    public OnImageLoaded onImageLoaded;

    public static interface OnImageLoaded {
        public void load(Bitmap b);
    }

    public static interface OnTextLoaded {
        public void load(String text);
    }
}
