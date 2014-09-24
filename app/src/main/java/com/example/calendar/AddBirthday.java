package com.example.calendar;

import java.util.ArrayList;

public class AddBirthday {
    ArrayList<OneFriend> array;

    public AddBirthday(ArrayList<OneFriend> array) {
        this.array = array;
    }

    public void Add() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                    for (int i = 0; i < array.size(); i++) {

                        if (!MainActivity.getInstance().isFriendSaved(
                                array.get(i))) {

                            MainActivity.getInstance().AddFriendToDb(
                                    array.get(i));
                        }

                        if (array.get(i).getDate_birthday() != "") {

                            for (int j = MainActivity.getInstance()
                                    .getNowYear(); j < MainActivity
                                    .getInstance().getNowYear() + 5; j++) {
                                String b = array.get(i).getDate_birthday();

                                String day = (String) b.substring(0,
                                        b.indexOf("."));
                                String mounth = "";

                                if (b.length() < 6) {
                                    mounth = b.substring(b.indexOf(".") + 1,
                                            b.length());
                                } else {
                                    b = b.substring(b.indexOf(".") + 1,
                                            b.length());
                                    mounth = (String) b.substring(0,
                                            b.indexOf("."));
                                }

                                if (day.length() == 1)
                                    day = "0" + day;

                                if (mounth.length() == 1)
                                    mounth = "0" + mounth;

                                String start_date = j + "-" + mounth + "-"
                                        + day + " 00:00";

                                String push_time = j + "-" + mounth + "-" + day
                                        + " 10:00";
                                String end_date = j + "-" + mounth + "-" + day
                                        + " 23:59";

                                MyDate MyDateStart = new MyDate(start_date);
                                MyDate MyDateEnd = new MyDate(end_date);
                                OneEvent e = new OneEvent(5, MainActivity
                                        .getInstance().language.HAPPY_BIRTHDAY
                                        + " "
                                        + array.get(i).getFirst_name()
                                        + " " + array.get(i).getLast_name(),
                                        new MyColor().getColorEventMounth(),
                                        MyDateStart.getYear(), MyDateStart
                                        .getMounth(), MyDateStart
                                        .getDay(), MyDateStart
                                        .getHour(), MyDateStart
                                        .getMinute(), MyDateEnd
                                        .getYear(), MyDateEnd
                                        .getMounth(), MyDateEnd
                                        .getDay(), MyDateEnd.getHour(),
                                        MyDateEnd.getMinute(), "", "", "", 0,
                                        "", push_time, "0", 0);
                                if (!MainActivity.getInstance().isEventSaved(e)) {
                                    String loc = "1";
                                    if (array.get(i).getLocation().length() > 0)
                                        loc = array.get(i).getLocation();


                                    ArrayList<Integer> arrayCalendarsId = MainActivity.getInstance().getSavedCalendarsID();
                                    int calId = -1;
                                    if (arrayCalendarsId.size() > 0)
                                        calId = arrayCalendarsId.get(0);

                                    MainActivity
                                            .getInstance()
                                            .AddEventToDB(calId,
                                                    MainActivity.getInstance().language.HAPPY_BIRTHDAY
                                                            + " "
                                                            + array.get(i)
                                                            .getFirst_name()
                                                            + " "
                                                            + array.get(i)
                                                            .getLast_name(),
                                                    new MyColor()
                                                            .getColorEventMounth(),
                                                    start_date, end_date,
                                                    1 + "", loc + "", "", "",
                                                    push_time, "0", 0);

                                }
                            }
                        }
                    }

                } catch (Exception e) {

                }
            }
        }).start();
    }

}
