package com.example.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class GetOneMounth {

    OneMount rowItem;

    Context context;
    SharedPreferences shared;

    public GetOneMounth(Context context, OneMount rowItem) {
        this.rowItem = rowItem;
        this.context = context;
    }

    public ArrayList<OneDay> Get(boolean isLoadEvent) {
        int count = new GetCountDay().getCountDay(rowItem.getNumberOfYear(),
                rowItem.getNumberOfMounth());

        Calendar cal = Calendar.getInstance();
        cal.set(rowItem.getNumberOfYear(), rowItem.getNumberOfMounth() - 1, 0);
        int firstday = cal.get(Calendar.DAY_OF_WEEK);
        ArrayList<OneDay> ArrayOneDay = new ArrayList<OneDay>();

        ArrayList<Integer> days_event = new ArrayList<Integer>();
        if (isLoadEvent)
            days_event = getEventsByMounth(rowItem.getNumberOfYear(),
                    rowItem.getNumberOfMounth());

        int k = 0;
        for (int i = 1; i < count + firstday; i++) {
            if (i < firstday) {

                ArrayOneDay.add(new OneDay(rowItem.getNumberOfYear(), rowItem
                        .getNumberOfMounth(), 0, false, false));

            } else {
                k++;

                Calendar c = Calendar.getInstance();
                c.set(rowItem.getNumberOfYear(),
                        rowItem.getNumberOfMounth() - 1, k - 1);

                boolean holiday = false;

                ArrayList<Integer> arrayHoliday = MainActivity.getHolidays();

                if (arrayHoliday.contains(c.get(Calendar.DAY_OF_WEEK) - 1))
                    holiday = true;

                boolean haveEvent = false;
                if (days_event != null && days_event.contains(k))
                    haveEvent = true;

                ArrayOneDay.add(new OneDay(rowItem.getNumberOfYear(), rowItem
                        .getNumberOfMounth(), k, holiday, haveEvent));
            }
        }

        while (ArrayOneDay.size() != 42) {
            ArrayOneDay.add(new OneDay(rowItem.getNumberOfYear(), rowItem
                    .getNumberOfMounth(), 0, false, false));

        }

        return ArrayOneDay;
    }

    public ArrayList<Integer> getEventsByMounth(int year, int mounth) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        try {

            Cursor c = MainActivity.DataBase.rawQuery("select * from "
                    + MainActivity.dbStorage.EVENT_TABLE, null);

            if (c.getCount() > 0) {

                for (int i = 1; i <= new GetCountDay()
                        .getCountDay(year, mounth); i++) {
                    String date = "";
                    if (i < 10)
                        date = year + "-" + mounth + "-0" + i;
                    else
                        date = year + "-" + mounth + "-" + i;

                    long date_milisec = MainActivity.StringSmallToMilisec(date);

                    try {

                        c = MainActivity.DataBase.rawQuery("select * from "
                                + MainActivity.dbStorage.EVENT_TABLE
                                + " where start_milisec<=" + date_milisec
                                + " and end_milisec>=" + date_milisec, null);

                        if (c.getCount() > 0) {
                            result.add(i);
                        }
                        c.close();

                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e(getClass().toString()
                                + "line = "
                                + Thread.currentThread().getStackTrace()[2]
                                .getLineNumber(), "catch:" + e);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }

        return result;
    }
}
