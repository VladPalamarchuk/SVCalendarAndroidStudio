/**
 * iCal Import/Export Plus - import ical-Files on Android
 *
 * Copyright (C) 2013 Gilles Baatz <baatzgilles@gmail.com>
 * Copyright (C) 2013 Dominik Schürmann <dominik@dominikschuermann.de>
 * Copyright (C) 2010-2011 Lukas Aichbauer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.calendar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ImportIcal {
    private static final String TAG = ImportIcal.class.getSimpleName();

    /*
     * Views
     */
    private Spinner calendarSpinner;
    private Spinner fileSpinner;
    private Button calendarInformation;
    private Button searchButton;
    private Button loadButton;
    private Button insertButton;
    private Button deleteButton;
    private Button setUrlButton;
    private Button dumpCalendar;
    private TextView icalInformation;
    // private Controller controller;
    private CheckBox chbDuplicates;
    private Calendar calendarObj;
    /*
     * Values
     */
    private List<BasicInputAdapter> urls;
    private List<GoogleCalendar> calendars;
    private SharedPreferences preferences;
    private int startYear = 0;
    private int startMonth = 0;
    private int startDay = 0;
    private int endYear = 0;
    private int endMonth = 0;
    private int endDay = 0;
    private String startData = null;
    private String endData = null;
    private String description = null;
    private String location = null;
    private String title = null;
    private int startHours = 0;
    private int startMinuts = 0;
    private int endHours = 0;
    private int endMinuts = 0;

    public static String getTag() {
        return TAG;
    }

    public int getStartHours() {
        return startHours;
    }

    public void setStartHours(int startHours) {
        this.startHours = startHours;
    }

    public int getStartMinuts() {
        return startMinuts;
    }

    public void setStartMinuts(int startMinuts) {
        this.startMinuts = startMinuts;
    }

    public int getEndHours() {
        return endHours;
    }

    public void setEndHours(int endHours) {
        this.endHours = endHours;
    }

    public int getEndMinuts() {
        return endMinuts;
    }

    public void setEndMinuts(int endMinuts) {
        this.endMinuts = endMinuts;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getTitles() {
        return title;
    }

    public void setTitles(String title) {
        this.title = title;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndMonth() {

        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getStartYear() {

        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public OneEvent[] readFile(String path, String nameFile) {
        OneEvent[] oneEventsArray = null;
        FileInputStream fin = null;
        try {
            File file = new File(path, nameFile);
            fin = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CalendarBuilder builder = new CalendarBuilder();

        try {
            calendarObj = builder.build(fin);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        ComponentList listEvent = calendarObj.getComponents(Component.VEVENT);

        int size = listEvent.size();
        oneEventsArray = new OneEvent[size];
        int i = 0;
        VEvent elem = null;
        for (Object ele : listEvent) {
            elem = (VEvent) ele;

            if (elem.getDescription() == null) {
                description = "";
            } else {

                description = "" + elem.getDescription().getValue();

            }
            if (elem.getLocation() == null) {
                location = "";
            } else {
                location = "" + elem.getLocation().getValue();

            }
            if (elem.getSummary() == null) {
                title = "";
            } else {
                title = "" + elem.getSummary().getValue();

            }
            if (elem.getEndDate() == null) {

            } else {
                endData = "" + elem.getEndDate().getDate();
                parseEndData(endData);
                endHours = elem.getEndDate().getDate().getHours();
                endMinuts = elem.getEndDate().getDate().getMinutes();

            }
            if (elem.getStartDate() == null) {

            } else {
                startData = "" + elem.getStartDate().getDate();
                parseStartData(startData);
                startHours = elem.getStartDate().getDate().getHours();
                startMinuts = elem.getStartDate().getDate().getMinutes();
            }

            OneEvent

                    event = new OneEvent(0, title, Color.RED, getStartYear(),
                    getStartMonth(), getStartDay(), getStartHours(),
                    getStartMinuts(), getEndYear(), getEndMonth(), getEndDay(),
                    getEndHours(), getEndMinuts(), "1", "33345", description,
                    0, "", "", "", 0);

            oneEventsArray[i] = event;

            i++;
        }

        Log.d(TAG, "________________________" + elem.getStartDate().getDate());

        for (int k = 0; k < oneEventsArray.length; k++) {
            Log.d(TAG, "Year = " + oneEventsArray[k].getStart_date_year()
                    + "Month =  " + oneEventsArray[k].getStart_date_mounth()
                    + "day = " + oneEventsArray[k].getStart_date_day()
                    + "hours = " + oneEventsArray[k].getStart_date_hour()
                    + "minutes  " + oneEventsArray[k].getStart_date_minute()
                    + " title " + oneEventsArray[k].getTitle() + "description "
                    + oneEventsArray[k].getInfo() + " location "
                    + oneEventsArray[k].getLocation() + " lastYear "
                    + oneEventsArray[k].getEnd_date_year() + "lastMonth "
                    + oneEventsArray[k].getEnd_date_mounth() + "lastDay "
                    + oneEventsArray[k].getEnd_date_day() + "lastHours"
                    + oneEventsArray[k].getEnd_date_hour() + "lastMinutes"
                    + oneEventsArray[k].getEnd_date_minute());

        }

        return oneEventsArray;
    }

    public void parseStartData(String startData) {
        startYear = Integer.parseInt(startData.substring(0, 4));
        startMonth = Integer.parseInt(startData.substring(4, 6));
        startDay = Integer.parseInt(startData.substring(6, 8));

    }

    public void parseEndData(String endData) {
        endYear = Integer.parseInt(endData.substring(0, 4));
        endMonth = Integer.parseInt(endData.substring(4, 6));
        endDay = Integer.parseInt(endData.substring(6, 8));

    }

}
