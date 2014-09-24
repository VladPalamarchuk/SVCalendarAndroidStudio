package com.example.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerAuthorization extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(MainActivity.getInstance(), this,
                1990, 1, 0);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());

        try {
            AutorizationActivity.getInstance().date_birthday = formattedDate;

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
