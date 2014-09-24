package com.example.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

public class CalendarService {

    public CalendarService(Context context) {
        this.context = context;
    }

    static Context context;
    HashMap<String, List<CalendarEvent>> eventMap = new HashMap<String, List<CalendarEvent>>();
    public CalendarEventLoadedListener loadedListener;

    public void readCalendar(Context context, ArrayList<Integer> calendarsID) {
        this.context = context;
        readCalendar(context, 5 * 365, 0, calendarsID);
    }

    public void setCalendarEventLoadedListener(
            CalendarEventLoadedListener loadedListener) {
        this.loadedListener = loadedListener;
    }

    public void readCalendar(Context context, int days, int hours,
                             ArrayList<Integer> calendarsID) {

        ContentResolver contentResolver = context.getContentResolver();

        final Cursor cursor = contentResolver.query(
                Uri.parse("content://com.android.calendar/calendars"),
                (new String[]{"_id", "calendar_displayName"}), null, null,
                null);

        HashSet<String> calendarIds = getCalenderIds(cursor);

        for (String id : calendarIds) {

            if (calendarsID.contains(Integer.parseInt(id))) {

                Uri.Builder builder = Uri.parse(
                        "content://com.android.calendar/instances/when")
                        .buildUpon();
                long now = new Date().getTime();

                ContentUris.appendId(builder, now
                        - (DateUtils.DAY_IN_MILLIS * days)
                        - (DateUtils.HOUR_IN_MILLIS * hours));

                ContentUris.appendId(builder, now
                        + (DateUtils.DAY_IN_MILLIS * days)
                        + (DateUtils.HOUR_IN_MILLIS * hours));

                Cursor eventCursor = contentResolver.query(builder.build(),
                        new String[]{"title", "begin", "end", "allDay",
                                "description", Calendars.CALENDAR_COLOR},
                        "calendar_id=" + id, null,
                        "startDay ASC, startMinute ASC");

                if (eventCursor.getCount() > 0) {

                    List<CalendarEvent> eventList = new ArrayList<CalendarEvent>();

                    eventCursor.moveToFirst();

                    CalendarEvent ce = loadEvent(eventCursor);

                    eventList.add(ce);

                    while (eventCursor.moveToNext()) {

                        ce = loadEvent(eventCursor);

                        // ce.getBegin().getm
                        eventList.add(ce);

                    }

                    Collections.sort(eventList);
                    eventMap.put(id, eventList);

                }
            }
        }

        if (loadedListener != null) {
            loadedListener.onCompleteLoaded();
        }
    }

    private static CalendarEvent loadEvent(Cursor csr) {
        return new CalendarEvent(csr.getString(0), new Date(csr.getLong(1)),
                new Date(csr.getLong(2)), !csr.getString(3).equals("0"),
                csr.getString(4), csr.getInt(5));
    }

    private static HashSet<String> getCalenderIds(Cursor cursor) {

        HashSet<String> calendarIds = new HashSet<String>();

        try {

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    String _id = cursor.getString(0);

                    calendarIds.add(_id);

                }
            }
        } catch (AssertionError ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calendarIds;

    }

    public ArrayList<OneEvent> getEvents() {
        ArrayList<OneEvent> res = new ArrayList<OneEvent>();

        for (String id : eventMap.keySet()) {

            List<CalendarEvent> events = eventMap.get(id);

            for (int j = 0; j < events.size(); j++) {
                String title = events.get(j).getTitle();
                Date begin = events.get(j).getBegin();
                Date end = events.get(j).getEnd();
                boolean allDay = events.get(j).isAllDay();

                int sh = begin.getHours();
                int sm = begin.getMinutes();
                int eh = end.getHours();
                int em = end.getMinutes();

                if (allDay) {
                    sh = 0;
                    sm = 0;
                    eh = 23;
                    em = 59;
                }

                String push = MainActivity.FormatDate(begin.getYear() + 1900)
                        + "-" + MainActivity.FormatDate(begin.getMonth() + 1)
                        + "-" + MainActivity.FormatDate(begin.getDate()) + " "
                        + MainActivity.FormatDate(begin.getHours()) + ":"
                        + MainActivity.FormatDate(begin.getMinutes());

                if (!arrayContainsTittle(res, title)) {

                    res.add(new OneEvent(0, title, events.get(j).getColor(),
                            begin.getYear() + 1900, begin.getMonth() + 1, begin
                            .getDate(), sh, sm, end.getYear() + 1900,
                            end.getMonth() + 1, end.getDate(), eh, em, "1",
                            "1", events.get(j).getInfo(), 0, "", push, "", 0));
                }

            }

        }

        return res;
    }

    public interface CalendarEventLoadedListener {
        public void onCompleteLoaded();
    }

    public ArrayList<String> getNamesCalendar() {
        ArrayList<String> res = new ArrayList<String>();
        ContentResolver contentResolver = context.getContentResolver();
        final Cursor cursor = contentResolver.query(
                Uri.parse("content://com.android.calendar/calendars"),
                (new String[]{"_id", "calendar_displayName"}), null, null,
                null);

        if (cursor.moveToFirst()) {
            do {

                res.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public ArrayList<String> getNamesCalendarContainsGMail() {
        ArrayList<String> res = new ArrayList<String>();
        ContentResolver contentResolver = context.getContentResolver();
        final Cursor cursor = contentResolver.query(
                Uri.parse("content://com.android.calendar/calendars"),
                (new String[]{"_id", "calendar_displayName"}), null, null,
                null);

        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(1);

                if (name.contains("@gmail.com"))

                    res.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return res;
    }

    public static int getIdCalendarByName(String nameCalendar) {
        String res = "-1";
        ContentResolver contentResolver = context.getContentResolver();
        final Cursor cursor = contentResolver.query(
                Uri.parse("content://com.android.calendar/calendars"),
                (new String[]{"_id", "calendar_displayName"}), null, null,
                null);

        if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(1);

                if (name.equalsIgnoreCase(nameCalendar))
                    res = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        try {

            cursor.close();
            return Integer.parseInt(res);
        } catch (Exception e) {
            // TODO: handle exception

            cursor.close();
            return -1;
        }

    }

    static public void sendEvent(OneEvent e, int calID) {
        String start_date = MainActivity.FormatDate(e.getStart_date_year())
                + "-" + MainActivity.FormatDate(e.getStart_date_mounth()) + "-"
                + MainActivity.FormatDate(e.getStart_date_day()) + " "
                + MainActivity.FormatDate(e.getStart_date_hour()) + ":"
                + MainActivity.FormatDate(e.getStart_date_minute());

        String end_date = MainActivity.FormatDate(e.getEnd_date_year()) + "-"
                + MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
                + MainActivity.FormatDate(e.getEnd_date_day()) + " "
                + MainActivity.FormatDate(e.getEnd_date_hour()) + ":"
                + MainActivity.FormatDate(e.getEnd_date_minute());

        long startMillisecond = MainActivity.StringToMilisec(start_date);
        long endMillisecond = MainActivity.StringToMilisec(end_date);
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Events.DTSTART, startMillisecond);
        values.put(Events.DTEND, endMillisecond);
        values.put(Events.TITLE, e.getTitle());
        values.put(Events.DESCRIPTION, e.getInfo());
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_COLOR, e.getColor());

        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault()
                .getDisplayName());

        Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
        syncCalendar(context, String.valueOf(calID));
    }

    public static void syncCalendar(Context context, String calendarId) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Calendars.SYNC_EVENTS, 1);
        values.put(Calendars.VISIBLE, 1);
        cr.update(ContentUris.withAppendedId(
                Uri.parse("content://com.android.calendar/calendars"),
                Long.parseLong(calendarId)), values, null, null);
    }

    public boolean arrayContainsTittle(ArrayList<OneEvent> array, String tittle) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getTitle().equalsIgnoreCase(tittle))
                return true;
        }
        return false;
    }
}
