package com.example.calendar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;

public class ShowDialog {
    AlertDialog.Builder ad;
    ArrayList<OneEvent> events = new ArrayList<OneEvent>();
    int milisec;
    boolean isDown;
    View v;
    int id_dialog = 0;
    int year;
    int mounth;
    int day;
    public static ShowDialog showDialog;
    int hour;
    int minute;
    public static final int TYPE_DELETE = 1;
    public static final int TYPE_COPY = 2;
    public static final int TYPE_CUT = 3;
    public static final int TYPE_CHANGE = 4;
    public boolean isShow = false;

    public ShowDialog() {
        showDialog = this;
    }

    public static ShowDialog getInstance() {
        return showDialog;
    }

    public void setDate(View v, int year, int mounth, int day, int hour,
                        int minute) {

        this.v = v;
        this.year = year;
        this.mounth = mounth;
        this.day = day;
        this.hour = hour;
        this.minute = minute;

        events = MainActivity.getInstance().getEventsIcludingDate(year, mounth,
                day, hour, minute);

        if (events.size() > 0) {

            if (MainActivity.getInstance().isBuffered) {
                id_dialog = 2;
                MainActivity.getInstance().dialog_actions = new String[]{
                        MainActivity.getInstance().language.DIALOG_ADD_EVENT,
                        MainActivity.getInstance().language.DIALOG_CHANGE_EVENT,
                        MainActivity.getInstance().language.DIALOG_PASTE_EVENT,
                        MainActivity.getInstance().language.DIALOG_COPY_EVENT,
                        MainActivity.getInstance().language.DIALOG_CUT_EVENT,
                        MainActivity.getInstance().language.DIALOG_DELETE_EVENT};
            } else {
                id_dialog = 1;
                MainActivity.getInstance().dialog_actions = new String[]{
                        MainActivity.getInstance().language.DIALOG_ADD_EVENT,
                        MainActivity.getInstance().language.DIALOG_CHANGE_EVENT,
                        MainActivity.getInstance().language.DIALOG_COPY_EVENT,
                        MainActivity.getInstance().language.DIALOG_CUT_EVENT,
                        MainActivity.getInstance().language.DIALOG_DELETE_EVENT};
            }

        } else {

            if (MainActivity.getInstance().isBuffered) {
                id_dialog = 3;

                MainActivity.getInstance().dialog_actions = new String[]{
                        MainActivity.getInstance().language.DIALOG_ADD_EVENT,
                        MainActivity.getInstance().language.DIALOG_PASTE_EVENT};
            } else {
                id_dialog = 0;
                MainActivity.getInstance().dialog_actions = new String[]{MainActivity
                        .getInstance().language.DIALOG_ADD_EVENT};
            }
        }

    }

    public void show() {

        MainActivity.getInstance().showDialog(id_dialog);

        isShow = true;
        // Log.e("", "Show dialog show  = " + isShow);
    }

    public boolean isShow() {
        return isShow;
    }

    public void ChoiseOfAction(String[] array, int id) {
        try {

            Intent i = new Intent(MainActivity.getInstance(), GetOneItem.class);
            i.putExtra("YEAR", year);
            i.putExtra("MOUNTH", mounth);
            i.putExtra("DAY", day);

            if (array[id]
                    .equals(MainActivity.getInstance().language.DIALOG_ADD_EVENT)) {

                MainActivity.getInstance().startAddEventFragment(false, false,
                        year, mounth, day, hour, minute);
            }

            if (array[id]
                    .equals(MainActivity.getInstance().language.DIALOG_COPY_EVENT)) {
                if (events.size() == 1) {
                    OneEvent e = events.get(0);
                    MainActivity.getInstance().CopyEvent(v, e, true);
                } else {

                    i.putExtra("TYPE", TYPE_COPY);
                    MainActivity.getInstance().startActivity(i);
                }

            }

            if (array[id]
                    .equals(MainActivity.getInstance().language.DIALOG_CUT_EVENT)) {
                if (events.size() == 1) {
                    OneEvent e = events.get(0);
                    MainActivity.getInstance().CutEvent(v, e, true);
                } else {

                    i.putExtra("TYPE", TYPE_CUT);
                    MainActivity.getInstance().startActivity(i);
                }
            }

            if (array[id]
                    .equals(MainActivity.getInstance().language.DIALOG_CHANGE_EVENT)) {
                if (events.size() == 1) {
                    OneEvent e = events.get(0);
                    MainActivity.getInstance().UpdateEvent(e);

                } else {

                    i.putExtra("TYPE", TYPE_CHANGE);
                    MainActivity.getInstance().startActivity(i);
                }
            }

            if (array[id]
                    .equals(MainActivity.getInstance().language.DIALOG_PASTE_EVENT)) {

                MainActivity.getInstance().PasteEvent(v, year, mounth, day,
                        hour, minute);

            }

            if (array[id]
                    .equals(MainActivity.getInstance().language.DIALOG_DELETE_EVENT)) {

                if (events.size() == 1) {
                    OneEvent e = events.get(0);
                    MainActivity.getInstance().DeleteEvent(v, e, true);

                } else {

                    i.putExtra("TYPE", TYPE_DELETE);
                    MainActivity.getInstance().startActivity(i);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception

            // Log.e("", "ChoiseOfAction catch: " + e);
        }

        // isShow = false;
    }

    public void StartTimer() {

        if (isDown) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    milisec += 10;

                    if (milisec < 2000) {
                        if (isDown)
                            isDown = true;
                        StartTimer();
                    } else {
                        StopTimer();
                        isShow = true;
                        show();
                    }

                }
            }, 1);
        }
    }

    public void StopTimer() {

        isShow = false;
        isDown = false;
        milisec = 0;
        // Log.e("", "StopTimer dialog show  = " + isShow);
    }

}
