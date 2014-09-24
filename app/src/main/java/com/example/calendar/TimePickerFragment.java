package com.example.calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(MainActivity.getInstance(), this, 0, 0,
                true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub

        String time = "";
        if (hourOfDay < 10)
            time += "0" + hourOfDay;
        else
            time += hourOfDay;
        time += ":";
        if (minute < 10)
            time += "0" + minute;
        else
            time += minute;

        if (AddEventFragment.getInstance().push_time) {
            Long d1 = StringToMilisec((String) AddEventFragment.getInstance().open_start_date_dialog
                    .getText());
            Long d2 = StringToMilisec(AddEventFragment.getInstance().push_date
                    + " " + time);

            if (d1 > d2) {

                AddEventFragment.getInstance().add_event_psuh_date
                        .setText(AddEventFragment.getInstance().push_date + " "
                                + time);

            } else {

                AddEventFragment.getInstance().add_event_psuh_date
                        .setText(AddEventFragment.getInstance().open_start_date_dialog
                                .getText());
            }
        } else {
            if (AddEventFragment.getInstance().isStartDate) {
                AddEventFragment.getInstance().start_time = time;
                AddEventFragment.getInstance().open_start_date_dialog
                        .setText(AddEventFragment.getInstance().start_date
                                + " " + time);
                AddEventFragment.getInstance().open_end_date_dialog
                        .setText(AddEventFragment.getInstance().start_date
                                + " " + time);

                AddEventFragment.getInstance().add_event_psuh_date
                        .setText(AddEventFragment.getInstance().start_date
                                + " " + time);

            } else {

                Long d1 = StringToMilisec(AddEventFragment.getInstance().start_date
                        + " " + AddEventFragment.getInstance().start_time);
                AddEventFragment.getInstance().end_time = time;

                Long d2 = StringToMilisec(AddEventFragment.getInstance().end_date
                        + " " + time);

                if (d2 > d1) {
                    AddEventFragment.getInstance().open_end_date_dialog
                            .setText(AddEventFragment.getInstance().end_date
                                    + " " + time);
                } else {

                    AddEventFragment.getInstance().open_end_date_dialog
                            .setText(AddEventFragment.getInstance().open_start_date_dialog
                                    .getText());
                }
            }

        }
    }

    public long StringToMilisec(String DateInString) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long res = 0;
        String DateInMilisec = "";
        try {
            Date date;
            try {
                date = formatDate.parse(DateInString);
                DateInMilisec = "" + date.getTime();
                res = Long.parseLong(DateInMilisec);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

}
