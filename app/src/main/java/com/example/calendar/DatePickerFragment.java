package com.example.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    boolean one = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        MyDate myDate;
        if (AddEventFragment.getInstance().isStartDate)
            myDate = new MyDate(
                    AddEventFragment.getInstance().open_start_date_dialog
                            .getText().toString());
        else
            myDate = new MyDate(
                    AddEventFragment.getInstance().open_end_date_dialog
                            .getText().toString());
        int year = myDate.getYear();
        int month = myDate.getMounth() - 1;
        int day = myDate.getDay();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(MainActivity.getInstance(), this, year,
                month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());

        if (AddEventFragment.getInstance().push_time) {
            AddEventFragment.getInstance().push_date = formattedDate;
        } else {

            if (AddEventFragment.getInstance().isStartDate) {
                AddEventFragment.getInstance().start_date = formattedDate;

            } else {
                AddEventFragment.getInstance().end_date = formattedDate;

            }
        }
        if (one) {
            DialogFragment picker = new TimePickerFragment();
            picker.show(getFragmentManager(), "datePicker");
            one = false;
        }

    }
}
