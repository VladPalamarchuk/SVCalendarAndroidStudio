package com.example.calendar;

import android.annotation.SuppressLint;
import android.util.Log;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressLint("NewApi")
public class ImportExport {

    public void createFile(ArrayList<OneEvent> listOneEvent, String path) {

        try {

            net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();

            calendar.getProperties().add(new ProdId("-//habrahabr"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);


            //	TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = (TimeZone) TimeZone.getDefault();

            Calendar startDate = new GregorianCalendar();

            for (int i = 0; i < listOneEvent.size(); i++) {
                OneEvent event = listOneEvent.get(i);

                startDate.setTimeZone(TimeZone.getDefault());
                startDate.set(Calendar.MONTH,
                        event.getStart_date_mounth());
                startDate.set(Calendar.DAY_OF_MONTH,
                        event.getStart_date_day());
                startDate.set(Calendar.YEAR,
                        event.getStart_date_year());
                startDate.set(Calendar.HOUR_OF_DAY,
                        event.getStart_date_hour());
                startDate.set(Calendar.MINUTE,
                        event.getStart_date_minute());

                Calendar endDate = new GregorianCalendar();

                endDate.setTimeZone(TimeZone.getDefault());
                endDate.set(Calendar.MONTH,
                        event.getEnd_date_mounth());
                endDate.set(Calendar.DAY_OF_MONTH,
                        event.getEnd_date_day());
                endDate.set(Calendar.YEAR, event.getEnd_date_year());
                endDate.set(Calendar.HOUR_OF_DAY,
                        event.getEnd_date_hour());
                endDate.set(Calendar.MINUTE,
                        event.getEnd_date_minute());

                String eventName = event.getTitle();
                DateTime start = new DateTime(startDate.getTime());
                DateTime end = new DateTime(endDate.getTime());
                VTimeZone tz = timezone.getVTimeZone();


                VEvent meeting = new VEvent(start, end, eventName);


                meeting.getProperties().add(tz.getTimeZoneId());


                meeting.getProperties().add(new Description(event.getInfo()));


                //		meeting.getProperties().add();


                net.fortuna.ical4j.model.Calendar myCalendar = new net.fortuna.ical4j.model.Calendar();

                FileOutputStream fout = new FileOutputStream(path);
                CalendarOutputter out = new CalendarOutputter();

                try {

                    out.output(myCalendar, fout);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ValidationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ArrayList<OneEvent> readCaIFile(String path) {

        ArrayList<OneEvent> eventsArray = new ArrayList<OneEvent>();

        try {
            FileInputStream fin = new FileInputStream(path);
            CalendarBuilder builder = new CalendarBuilder();
            try {
                net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);
                ComponentList listEvents = calendar
                        .getComponents(Component.VEVENT);

                for (int i = 0; i < listEvents.size(); i++) {

                    OneEvent oneEvent = (OneEvent) listEvents.get(i);

                    Log.e(getClass().toString(),
                            "1 " + oneEvent.getStart_date_day());

                    eventsArray.add(oneEvent);

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return eventsArray;
    }

}
