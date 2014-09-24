package com.example.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SyncGoogleCalendar {

    Context context;
    ArrayList<Integer> calendarsID = new ArrayList<Integer>();

    public SyncGoogleCalendar(Context context) {
        this.context = context;
    }

    public void showSyncQuestion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(MainActivity.getInstance().language.SYNC_GOOGLE_CALENDAR_QUESTION);

        dialog.setPositiveButton(MainActivity.getInstance().language.YES,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        showSyncDialog();

                        MainActivity.getInstance().shared
                                .edit()
                                .putBoolean(
                                        MainActivity.SHARED_IS_SYNC_GOOGLE_CALENDAR,
                                        true).commit();

                    }
                });

        dialog.setNegativeButton(MainActivity.getInstance().language.NO,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        MainActivity.getInstance().shared
                                .edit()
                                .putBoolean(
                                        MainActivity.SHARED_IS_SYNC_GOOGLE_CALENDAR,
                                        false).commit();

                    }
                });

        dialog.show();

    }

    public void showSyncDialog() {

        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(MainActivity.getInstance().language.SYNC_GOOGLE_CALENDAR);
            final ListView list = new ListView(context);
            final ArrayList<String> calendars = new CalendarService(context)
                    .getNamesCalendar();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_multiple_choice,
                    calendars);

            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    calendarsID.clear();

                    SparseBooleanArray sbArray = list.getCheckedItemPositions();
                    for (int i = 0; i < sbArray.size(); i++) {
                        int key = sbArray.keyAt(i);
                        calendarsID.add(CalendarService
                                .getIdCalendarByName(calendars.get(key)));
                    }

                    MainActivity.getInstance().putCalendarsId(calendarsID);

                }
            });
            dialog.setView(list);

            dialog.setPositiveButton(MainActivity.getInstance().language.SYNC,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            MainActivity.getInstance().addGoogleCalendarEvents(
                                    calendarsID);
                        }
                    });

            dialog.setNegativeButton(
                    MainActivity.getInstance().language.CANCEL,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    });

            dialog.show();

        } catch (Exception e) {
            // TODO: handle exception
            // Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }
}
