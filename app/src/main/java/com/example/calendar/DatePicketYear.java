package com.example.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePicketYear extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    boolean one = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(MainActivity.getInstance(), this,
                YearFragment.getInstance().number_year, 1, 1);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String formattedDate = sdf.format(c.getTime());
        YearFragment.getInstance().year_layout.removeAllViews();
        YearFragment.getInstance().view_events = new DrawYear(
                MainActivity.getInstance(), Integer.parseInt(formattedDate),
                MainActivity.getInstance().language.NAMES_MOUNTH, true);
        YearFragment.getInstance().year_layout.addView(YearFragment
                .getInstance().view_events);
        YearFragment.getInstance().number_year = Integer
                .parseInt(formattedDate);
    }

}
