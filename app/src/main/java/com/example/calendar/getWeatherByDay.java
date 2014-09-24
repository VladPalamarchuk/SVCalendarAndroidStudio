package com.example.calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class getWeatherByDay {
    int id_city;
    String date;
    //String src = "http://calendar.studiovision.com.ua/calendar/weater_test.php?id_city=";


    String src = "http://beestore.com.ua/calendar/weater_test.php?id_city=";
    ArrayList<OneWeather> weahters = new ArrayList<OneWeather>();
    int year;
    int day;
    int mounth;
    boolean isDate;
    ArrayList<String> arrayTime = new ArrayList<String>();
    ArrayList<String> arraySrc = new ArrayList<String>();
    ArrayList<String> arrayPogoda = new ArrayList<String>();
    OneWeatherDay result;
    boolean isStrong;
    boolean isTime;
    boolean isSrc;
    boolean isPogoda;

    boolean isHavePrognosis = false;
    OnCompleteListener onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public getWeatherByDay(int id_city, int year, int mounth, int day) {

        String day_s = "";
        if (day < 10)
            day_s = "0" + day;
        else
            day_s = String.valueOf(day);

        String mounth_s = "";
        if (mounth < 10)
            mounth_s = "0" + mounth;
        else
            mounth_s = String.valueOf(mounth);

        this.date = day_s + "." + mounth_s + "." + year;

        src += String.valueOf(id_city);
        this.day = day;
        this.year = year;
        this.mounth = mounth;
    }

    public void Parse() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    URL url = new URL(src);

                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    InputStream stream = conn.getInputStream();
                    XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory
                            .newInstance();
                    XmlPullParser xpp = xmlFactoryObject.newPullParser();

                    xpp.setInput(stream, null);

                    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                        switch (xpp.getEventType()) {

                            case XmlPullParser.START_DOCUMENT:

                                break;

                            case XmlPullParser.START_TAG:

                                if (xpp.getName().equalsIgnoreCase("strong"))
                                    isStrong = true;

                                if (xpp.getName().equalsIgnoreCase("time"))
                                    isTime = true;

                                if (xpp.getName().equalsIgnoreCase("pogoda"))
                                    isPogoda = true;

                                if (isDate)
                                    if (xpp.getName().equalsIgnoreCase("img")) {

                                        for (int i = 0; i < xpp.getAttributeCount(); i++) {

                                            if (xpp.getAttributeName(i).equals(
                                                    "src")) {

                                                arraySrc.add(xpp
                                                        .getAttributeValue(i));
                                                break;

                                            }

                                        }
                                    }

                                break;
                            case XmlPullParser.END_TAG:

                                if (xpp.getName().equalsIgnoreCase("strong"))
                                    isStrong = false;

                                if (xpp.getName().equalsIgnoreCase("time"))
                                    isTime = false;

                                if (xpp.getName().equalsIgnoreCase("pogoda"))
                                    isPogoda = false;

                                if (xpp.getName().equalsIgnoreCase("weather"))
                                    isDate = false;

                                break;
                            case XmlPullParser.TEXT:
                                if (isStrong) {

                                    if (xpp.getText().substring(2).equals(date)) {

                                        isDate = true;

                                    }

                                }
                                if (isDate) {

                                    if (isTime) {
                                        arrayTime.add(xpp.getText());
                                    }

                                    if (isPogoda) {
                                        arrayPogoda.add(xpp.getText());

                                    }

                                }
                                break;

                            default:
                                break;
                        }
                        xpp.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }

                ArrayList<OneWeather> arrayOneWeater = new ArrayList<OneWeather>();

                if (arrayPogoda.size() > 0) {

                    isHavePrognosis = true;

                    for (int i = 0; i < arrayPogoda.size(); i++) {

                        Bitmap image = getBitmapFromURL(arraySrc.get(i));

                        arrayOneWeater.add(new OneWeather(Integer
                                .parseInt(arrayTime.get(i).substring(2)),
                                image, arrayPogoda.get(i).substring(2)));

                    }

                } else
                    isHavePrognosis = false;

                result = new OneWeatherDay(arrayOneWeater, year, mounth, day);

                try {
                    Thread.interrupted();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }

                if (onCompleteListener != null) {
                    onCompleteListener.Complete();
                }
            }

        }).start();

    }

    public OneWeatherDay GetOneWeatherDay() {

        return result;
    }

    public Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static interface OnCompleteListener {
        public void Complete();

    }

    ;
}
