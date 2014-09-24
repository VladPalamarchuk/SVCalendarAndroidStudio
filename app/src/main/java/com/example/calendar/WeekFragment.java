package com.example.calendar;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class WeekFragment extends android.support.v4.app.Fragment {

    static WeekFragment weekFragment;
    TextView name_mounth;
    DrawWeek view_week;
    SeekBar seek;
    RelativeLayout week_layout;
    RelativeLayout hours;
    ViewGroup root;
    ImageView add_event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        root = (ViewGroup) inflater.inflate(R.layout.week_fragment, null);

        int number_mounth = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT_NUMBER_MOUNTH);
        int count_col = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT_COUNT_COL);

        int count_row = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT_COUNT_ROW);
        int year = getArguments().getInt(MainActivity.WEEK_FRAGMENT_YEAR);
        int first_day = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT_FIRST_DAY);
        boolean prev = getArguments().getBoolean(
                MainActivity.WEEK_FRAGMENT_PREV);

        String[] names_days = getArguments().getStringArray(
                MainActivity.WEEK_FRAGMENT_NAME_DAYS);

        add_event = (ImageView) root.findViewById(R.id.week_fragment_add_event);

        add_event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().startAddEventFragment(false, false,
                        MainActivity.getNowYear(), MainActivity.getNowMounth(),
                        MainActivity.getNowDay(), MainActivity.getNowHour(),
                        MainActivity.getNowMinute());
            }
        });

        hours = (RelativeLayout) root.findViewById(R.id.hours);
        name_mounth = (TextView) root
                .findViewById(R.id.week_fragment_mounth_name);
        weekFragment = this;
        seek = (SeekBar) root.findViewById(R.id.week_fragment_seek);
        seek.setProgress(MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_WEEK_SHOW, 7) - 1);

        count_col = seek.getProgress();
        view_week = new DrawWeek(MainActivity.getInstance(), count_col,
                first_day, number_mounth, year, prev, names_days,
                MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR);

        week_layout = (RelativeLayout) root
                .findViewById(R.id.week_fragment_week);
        week_layout.addView(view_week);

        seek.setProgress(MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_WEEK_SHOW, 7) - 1);

        DrawHours drawHours = new DrawHours(MainActivity.getInstance(),
                MainActivity.getInstance().shared.getBoolean(
                        MainActivity.SHARED_IS_AMPM, false));
        hours.addView(drawHours);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                name_mounth.setText(view_week.getNameMounts());

            }
        }, 50);

        seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generate d method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                week_layout.removeAllViews();

                view_week = new DrawWeek(
                        MainActivity.getInstance(),
                        seekBar.getProgress() + 1,
                        view_week.getFirstDay(),
                        view_week.getStaticMounth(),
                        view_week.getStaticYear(),
                        false,
                        view_week.getNameDays(),
                        MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR);

                week_layout.addView(view_week);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        name_mounth.setText(view_week.getNameMounts());
                    }
                }, 50);

            }
        });

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.week_fragment_open_left_menu),
                        (Spinner) root.findViewById(R.id.week_fragment_spinner));
        setColor();
        return root;
    }

    public static WeekFragment getInstance() {
        return weekFragment;
    }

    public void PrevWeek() {

        Animation an1;

        an1 = new TranslateAnimation(0, week_layout.getWidth(), 0, 0);

        an1.setDuration(500);
        view_week.setAnimation(an1);

        view_week = new DrawWeek(MainActivity.getInstance(),
                view_week.getCountCol(), view_week.getStartPrev(),
                view_week.getPrevMounth(), view_week.getYear(), true,
                view_week.getNameDays(),
                MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR);

        week_layout.removeAllViews();

        an1 = new TranslateAnimation(-1 * week_layout.getWidth(), 0, 0, 0);

        an1.setDuration(500);
        view_week.setAnimation(an1);

        week_layout.addView(view_week);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                name_mounth.setText(view_week.getNameMounts());
            }
        }, 50);
    }

    public void NextWeek() {

        Animation an1;

        an1 = new TranslateAnimation(0, week_layout.getWidth() * -1, 0, 0);

        an1.setDuration(500);
        view_week.setAnimation(an1);
        view_week = new DrawWeek(MainActivity.getInstance(),
                view_week.getCountCol(), view_week.getStartNext(),
                view_week.getMounth(), view_week.getYear(), false,
                view_week.getNameDays(),
                MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR);

        week_layout.removeAllViews();

        an1 = new TranslateAnimation(week_layout.getWidth(), 0, 0, 0);

        an1.setDuration(500);
        view_week.setAnimation(an1);
        week_layout.addView(view_week);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                name_mounth.setText(view_week.getNameMounts());
            }
        }, 50);
    }

    public void setColor() {

        ((TextView) root.findViewById(R.id.week_fragment_mounth_name))
                .setTextColor(new MyColor().getColorLabel());

        root.findViewById(R.id.week_fragment_seek_background)
                .setBackgroundColor(new MyColor().getColorComponents());

        root.findViewById(R.id.week_fragment_seek_background_lin)
                .setBackgroundColor(new MyColor().getColorComponents());

        root.findViewById(R.id.week_fragment_top).setBackgroundColor(
                new MyColor().getColorComponents());

        ((TextView) root.findViewById(R.id.week_fragment_text1))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text2))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text3))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text4))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text5))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text6))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text7))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text8))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text9))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text10))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text11))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text12))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text13))
                .setTextColor(new MyColor().getColorLabel());
        ((TextView) root.findViewById(R.id.week_fragment_text14))
                .setTextColor(new MyColor().getColorLabel());

    }

}
