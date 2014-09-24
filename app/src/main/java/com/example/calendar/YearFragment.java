package com.example.calendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import palamarchuk.calendarwidget.month.DialogSearchByYear;
import palamarchuk.calendarwidget.month.DialogSearchByYear.YearSelectedListener;

public class YearFragment extends android.support.v4.app.Fragment {

    RelativeLayout year_layout = null;

    int number_year = 0;
    DrawYear view_only_year;
    DrawYear view_events;
    public static YearFragment yearFragment;
    TextView year_name;
    ViewGroup root;
    ImageView search;
    Handler handler;

    ImageView add_event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                // if (view_events.getYear() == view_only_year.getYear()) {
                // Log.e(getClass().toString(), "handler");
                // year_layout.removeAllViews();
                // year_layout.addView(view_events);
                // }

            }
        };

        root = (ViewGroup) inflater.inflate(R.layout.year_fragment, null);
        year_layout = (RelativeLayout) root
                .findViewById(R.id.year_fragment_year);
        number_year = getArguments().getInt(MainActivity.YEAR_FRAGMENT_YEAR);

        view_only_year = new DrawYear(MainActivity.getInstance(), number_year,
                MainActivity.getInstance().language.NAMES_MOUNTH, true);

        search = (ImageView) root.findViewById(R.id.yea_fragment_search_image);
        year_layout.addView(view_only_year);

        add_event = (ImageView) root.findViewById(R.id.year_fragment_add_event);

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

        yearFragment = this;
        year_name = (TextView) root.findViewById(R.id.year_fragment_year_name);

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();

                DialogSearchByYear dsby = new DialogSearchByYear(MainActivity
                        .getInstance());
                dsby.setYearSelectedListener(new YearSelectedListener() {

                    @Override
                    public void yearSelected(int res) {
                        // TODO Auto-generated method stub

                        view_only_year = new DrawYear(MainActivity
                                .getInstance(), res + 1, MainActivity
                                .getInstance().language.NAMES_MOUNTH, true);
                        year_layout.removeAllViews();
                        year_layout.addView(view_only_year);
                    }
                });
                dsby.show();

            }
        });

        setColor();

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.year_fragment_open_left_menu),
                        (Spinner) root.findViewById(R.id.year_fragment_spinner));

        return root;
    }

    public static YearFragment getInstance() {
        return yearFragment;
    }

    public void NextYear() {

        int height = year_layout.getHeight();

        Animation an1;

        an1 = new TranslateAnimation(0, 0, 0, height * -1);
        an1.setDuration(500);
        view_only_year.setAnimation(an1);
        number_year++;
        view_only_year = new DrawYear(MainActivity.getInstance(), number_year,
                MainActivity.getInstance().language.NAMES_MOUNTH, true);
        Animation an2;

        an2 = new TranslateAnimation(0, 0, height, 0);
        an2.setDuration(500);
        view_only_year.setAnimation(an2);
        year_layout.removeAllViews();

        // new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // Looper.prepare();
        // // TODO Auto-generated method stub
        // view_events = new DrawYear(MainActivity.getInstance(),
        // number_year,
        // MainActivity.getInstance().language.NAMES_MOUNTH, true);
        // handler.sendMessage(handler.obtainMessage(1, 1));
        // }
        // }).start();

        year_layout.addView(view_only_year);

    }

    public void PrevYear() {

        int height = year_layout.getHeight();
        Animation an1;
        an1 = new TranslateAnimation(0, 0, 0, height);
        an1.setDuration(500);
        view_only_year.setAnimation(an1);
        number_year--;
        view_only_year = new DrawYear(MainActivity.getInstance(), number_year,
                MainActivity.getInstance().language.NAMES_MOUNTH, true);
        Animation an2;
        an2 = new TranslateAnimation(0, 0, height * -1, 0);
        an2.setDuration(500);
        view_only_year.setAnimation(an2);
        year_layout.removeAllViews();

        // new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // Looper.prepare();
        // // TODO Auto-generated method stub
        // view_events = new DrawYear(MainActivity.getInstance(),
        // number_year,
        // MainActivity.getInstance().language.NAMES_MOUNTH, true);
        //
        // handler.sendMessage(handler.obtainMessage(1, 1));
        // }
        // }).start();
        year_layout.addView(view_only_year);
    }

    public void setColor() {
        MyColor c = new MyColor();

        if (MainActivity.THEME_APPLICATION == DrawMounth.BASE_THEME)
            search.setImageResource(R.drawable.search_base_theme);

        root.findViewById(R.id.year_fragment_top).setBackgroundColor(
                c.getColorComponents());
        ((TextView) root.findViewById(R.id.year_fragment_year_name))
                .setTextColor(c.getColorLabel());

        root.findViewById(R.id.year_line).setBackgroundColor(
                c.getColorBacground());
    }

}
