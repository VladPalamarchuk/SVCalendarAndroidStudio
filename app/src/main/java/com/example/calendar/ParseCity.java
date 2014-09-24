package com.example.calendar;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ParseCity {

    int id;
    int region;
    String country;
    String name;
    ArrayList<OneCity> result = new ArrayList<OneCity>();
    boolean city;

    public ArrayList<OneCity> Parsing_data() {

        try {

            // 1 - russian
            // 2 - english
            // 3 - ukranian
            // 4 - spain

            InputStream stream;// = conn.getInputStream();

            int lang = 1;

            lang = MainActivity.getInstance().shared.getInt(
                    MainActivity.SHARED_LANGUAGE, 1);

            if (lang > 4 || lang < 1)
                lang = 2;

            stream = MainActivity.getInstance().getResources().getAssets()
                    .open(lang + ".txt");

            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory
                    .newInstance();
            XmlPullParser xpp = xmlFactoryObject.newPullParser();
            xpp.setInput(stream, null);

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {

                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (xpp.getName().equalsIgnoreCase("city")) {
                            city = true;

                            for (int i = 0; i < xpp.getAttributeCount(); i++) {

                                if (xpp.getAttributeName(i).equalsIgnoreCase("id")) {
                                    id = Integer.parseInt(xpp.getAttributeValue(i));

                                }

                                if (xpp.getAttributeName(i).equalsIgnoreCase(
                                        "region")) {
                                    region = Integer.parseInt(xpp
                                            .getAttributeValue(i));

                                }

                                if (xpp.getAttributeName(i).equalsIgnoreCase(
                                        "country")) {
                                    country = xpp.getAttributeValue(i);

                                }

                            }

                        }

                        break;
                    case XmlPullParser.END_TAG:

                        if (xpp.getName().equalsIgnoreCase("city")) {
                            city = false;
                        }

                        break;
                    case XmlPullParser.TEXT:
                        if (city) {
                            name = xpp.getText();
                            result.add(new OneCity(id, region, country, name));
                        }

                        break;

                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("", "catch 1");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "catch 2");
        }

        return result;
    }

}
