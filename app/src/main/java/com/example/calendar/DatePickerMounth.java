package com.example.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerMounth extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    boolean one = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(MainActivity.getInstance(), this,
                MounthFragment.getInstance().view.getYear(),
                MounthFragment.getInstance().view.getMounth(), 1);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());

        try {
            MyDate d = new MyDate(formattedDate);
            MounthFragment.getInstance().mounth.removeAllViews();
            MounthFragment.getInstance().view = new DrawMounth(
                    MainActivity.getInstance(), d.getYear(), d.getMounth(),
                    MainActivity.getInstance().language.NAMES_MOUNTH,
                    MainActivity.THEME_APPLICATION, day);
            MounthFragment.getInstance().mounth.addView(MounthFragment
                    .getInstance().view);

            MounthFragment.getInstance().drawClick(
                    MounthFragment.getInstance().view);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
