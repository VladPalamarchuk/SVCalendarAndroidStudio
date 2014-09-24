package com.example.calendar;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class NewCurrency {
    private String src = "http://www.getexchangerates.com/api/latest.json";
    public Context context;
    OnLoadComplete complete;
    HashMap<String, Float> MAP = new HashMap<String, Float>();

    public void setOnLoadCompleteListener(OnLoadComplete complete) {
        this.complete = complete;
    }

    public NewCurrency(Context context) {

        this.context = context;

    }

    public void parse() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Looper.prepare();
                try {
                    URL url = new URL(src);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(url.openStream()));
                    String strTemp = "";
                    String response = "";
                    while (null != (strTemp = br.readLine())) {
                        response = strTemp;
                    }
                    JSONArray array = new JSONArray(response);
                    JSONObject jsonObject = (JSONObject) array.get(0);
                    Iterator keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String currentDynamicKey = (String) keys.next();
                        String data = jsonObject.getString(currentDynamicKey);

                        if (!currentDynamicKey.equalsIgnoreCase("DateTime")) {
                            MAP.put(currentDynamicKey, Float.parseFloat(data));

                        }
                    }

                } catch (Exception e) {
                    Log.e(getClass().toString(), "catch new currency: " + e);
                }

                complete.Complete();
            }
        }).start();
    }

    public HashMap<String, Float> getMapCur() {
        return MAP;
    }

    public HashMap<String, Float> sortByNameCurr(HashMap<String, Float> map,
                                                 String nameCurr) {
        HashMap<String, Float> res = new HashMap<String, Float>();
        float valueCurr = 1;
        for (String key : map.keySet()) {
            if (key.equalsIgnoreCase(nameCurr)) {

                valueCurr = map.get(key);
            }
        }
        for (String key : map.keySet()) {
            res.put(key, valueCurr / map.get(key));
        }
        return res;
    }

    public ArrayList<String> getNamesCurr(HashMap<String, Float> map) {
        ArrayList<String> res = new ArrayList<String>();
        for (String key : map.keySet()) {
            res.add(key);
        }

        Collections.sort(res);
        return res;
    }

    public interface OnLoadComplete {
        public void Complete();
    }

    public ArrayList<OneCurrency> getCurrencyByNameAndListNameCurrency(
            HashMap<String, Float> map, String nameYourCurrency,
            ArrayList<String> namesCurrency) {
        ArrayList<OneCurrency> res = new ArrayList<OneCurrency>();

        map = sortByNameCurr(map, nameYourCurrency);

        for (int i = 0; i < namesCurrency.size(); i++) {
            String name = namesCurrency.get(i);
            float value = map.get(name);

            float d = (float) value / 50;
//
//			while (d > value/10) {
//				d -= 0.001;
//			} 

            if (name.equalsIgnoreCase(nameYourCurrency))
                d = 0;

            float sale = value - d;
            float bay = value + d;

            res.add(new OneCurrency(String.valueOf(bay), String.valueOf(sale),
                    name));
        }
        return res;
    }

}
