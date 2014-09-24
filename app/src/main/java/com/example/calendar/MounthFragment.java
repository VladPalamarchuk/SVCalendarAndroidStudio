package com.example.calendar;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MounthFragment extends android.support.v4.app.Fragment {

    RelativeLayout mounth = null;
    DrawMounth view;
    int number_mounth;
    int number_year;
    static MounthFragment mounthFragment;
    TextView name_mounth;
    TextView selected_date;
    ViewGroup root;
    ImageView mounth_fragment_add_event;
    Handler handlerWeather;
    ImageView mounth_fragment_search;
    ImageView mounth_fragment_open_left_menu;
    RelativeLayout names_day_rel;
    ListView list_events;
    boolean first = true;
    View header;
    boolean first_adapter;
    Bitmap image1;
    Bitmap image2;
    Bitmap image3;
    Bitmap image4;
    public int day = 1;

    getWeatherByDay gwbd;
    ArrayList<OneEventAndWeater> arrayOneEventAndWeaters = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEvent> arrayOneEvent = new ArrayList<OneEvent>();
    AdapterListEvent adapter = new AdapterListEvent(MainActivity.getInstance(),
            R.layout.one_event_item, arrayOneEventAndWeaters, false, true, false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        root = (ViewGroup) inflater.inflate(R.layout.mounth_fragment, null);
        header = root
                .inflate(MainActivity.getInstance(), R.layout.header, null);
        first = true;
        first_adapter = true;

        mounthFragment = this;

        names_day_rel = (RelativeLayout) root
                .findViewById(R.id.mounth_fragment_by_name_day);

        number_year = getArguments().getInt(MainActivity.MOUNTH_FRAGMENT_YEAR);

        number_mounth = getArguments().getInt(
                MainActivity.MOUNTH_FRAGMENT_MOUNTH);

        int number_day = getArguments()
                .getInt(MainActivity.MOUNTH_FRAGMENT_DAY);
        arrayOneEvent.clear();
        arrayOneEventAndWeaters.clear();
        arrayOneEvent = MainActivity.getInstance().getEventsByDate(number_year,
                number_mounth, number_day);

        list_events = (ListView) root
                .findViewById(R.id.mounth_fragmnet_list_evets);
        list_events.addHeaderView(header);
        list_events.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        handlerWeather = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {
                    SetDataHeader((ContentValues) msg.obj);

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };

        // new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // Looper.prepare();
        // arrayOneEventAndWeaters_help.clear();
        // // TODO Auto-generated method stub
        for (int i = 0; i < arrayOneEvent.size(); i++) {
            arrayOneEventAndWeaters.add(new OneEventAndWeater(arrayOneEvent
                    .get(i)));
        }
        adapter.notifyDataSetChanged();

        //
        // handlerWeather.sendMessage(handlerWeather.obtainMessage(1, ""));
        //
        // }
        // }).start();

        mounth = (RelativeLayout) root
                .findViewById(R.id.mounth_fragment_mounth);
        mounth_fragment_add_event = (ImageView) root
                .findViewById(R.id.mounth_fragment_add_event);
        mounth_fragment_search = (ImageView) root
                .findViewById(R.id.mounth_fragment_search);
        mounth_fragment_open_left_menu = (ImageView) root
                .findViewById(R.id.mounth_fragment_open_left_menu);
        name_mounth = (TextView) root
                .findViewById(R.id.mounth_fragment_name_nounth_and_year);

        selected_date = (TextView) root
                .findViewById(R.id.mounth_fragment_selected_date);

        number_year = getArguments().getInt(MainActivity.MOUNTH_FRAGMENT_YEAR);
        names_day_rel = (RelativeLayout) root
                .findViewById(R.id.mounth_fragment_by_name_day);
        number_mounth = getArguments().getInt(
                MainActivity.MOUNTH_FRAGMENT_MOUNTH);

        view = new DrawMounth(MainActivity.getInstance(), number_year,
                number_mounth,
                MainActivity.getInstance().language.NAMES_MOUNTH,
                MainActivity.THEME_APPLICATION, 1);
        mounth.removeAllViews();

        mounth.addView(view);

        DrawNameDay drawNameDay = new DrawNameDay(MainActivity.getInstance());
        names_day_rel.addView(drawNameDay);

        mounth_fragment_add_event
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new PlaySoundButton();
                        // TODO Auto-generated method stub
                        MainActivity.getInstance().startAddEventFragment(false,
                                false, view.getYear(), view.getMounth(), day,
                                0, 0);
                    }
                });
        mounth_fragment_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                DialogFragment picker = new DatePickerMounth();
                picker.show(getFragmentManager(), "datePicker");

            }
        });

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.mounth_fragment_open_left_menu),
                        (Spinner) root
                                .findViewById(R.id.mounth_fragment_spinner));

        if (number_year == MainActivity.getInstance().getNowYear()
                && number_mounth == MainActivity.getInstance().getNowMounth())
            addHeader(number_year, number_mounth, MainActivity.getInstance()
                    .getNowDay());
        else
            addHeader(number_year, number_mounth, 1);

        setColor();

        MainActivity.getInstance().showSoc();

        drawClick(view);

        return root;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub

        super.onDestroyView();

    }

    public static MounthFragment getInstance() {
        return mounthFragment;
    }

    public void NextMounth() {

        int width = mounth.getWidth();
        int height = mounth.getHeight();

        Animation an1;

        an1 = new TranslateAnimation(0, 0, 0, height * -1);
        an1.setDuration(500);
        view.setAnimation(an1);
        if (number_mounth == 12) {
            number_mounth = 1;
            number_year++;
        } else
            number_mounth++;

        view = new DrawMounth(MainActivity.getInstance(), number_year,
                number_mounth,
                MainActivity.getInstance().language.NAMES_MOUNTH,
                MainActivity.THEME_APPLICATION, 1);
        Animation an2;

        an2 = new TranslateAnimation(0, 0, height, 0);
        an2.setDuration(500);
        view.setAnimation(an2);
        mounth.removeAllViews();

        mounth.addView(view);
        drawClick(view);
    }

    public void PrevMounth() {

        if (number_mounth == 1) {
            number_mounth = 12;
            number_year--;
        } else
            number_mounth--;

        int width = mounth.getWidth();
        int height = mounth.getHeight();

        Animation an1;

        an1 = new TranslateAnimation(0, 0, 0, height);

        an1.setDuration(500);
        view.setAnimation(an1);

        view = new DrawMounth(MainActivity.getInstance(), number_year,
                number_mounth,
                MainActivity.getInstance().language.NAMES_MOUNTH,
                MainActivity.THEME_APPLICATION, 1);
        Animation an2;

        an2 = new TranslateAnimation(0, 0, height * -1, 0);
        an2.setDuration(500);
        view.setAnimation(an2);
        mounth.removeAllViews();

        mounth.addView(view);
        drawClick(view);
    }

    public void setColor() {
        MyColor c = new MyColor();
        // root.findViewById(R.id.mounth_fragment_root).setBackgroundColor(
        // c.getColorBacground());
        root.findViewById(R.id.mounth_fragment_top).setBackgroundColor(
                c.getColorComponents());
        ((TextView) root
                .findViewById(R.id.mounth_fragment_name_nounth_and_year))
                .setTextColor(c.getColorLabel());

        ((TextView) root.findViewById(R.id.mounth_fragment_selected_date))
                .setTextColor(c.getColorFont());

        root.findViewById(R.id.mounth_fragment_events).setBackgroundColor(
                c.getColorBacground());
        root.findViewById(R.id.mounth_fragment_background_date)
                .setBackgroundColor(c.getColorComponents());

        if (MainActivity.THEME_APPLICATION == DrawMounth.BASE_THEME)
            ((RelativeLayout) root
                    .findViewById(R.id.mounth_fragment_background_date))
                    .setBackgroundColor(Color.parseColor("#00000000"));

        ((TextView) root.findViewById(R.id.header_prognoz)).setTextColor(c
                .getColorFont());

        ((TextView) root.findViewById(R.id.time1_title)).setTextColor(c
                .getColorFont());
        ((TextView) root.findViewById(R.id.time1_pogoda)).setTextColor(c
                .getColorFont());
        ((TextView) root.findViewById(R.id.time2_title)).setTextColor(c
                .getColorFont());
        ((TextView) root.findViewById(R.id.time2_pogoda)).setTextColor(c
                .getColorFont());

        ((TextView) root.findViewById(R.id.time3_title)).setTextColor(c
                .getColorFont());
        ((TextView) root.findViewById(R.id.time3_pogoda)).setTextColor(c
                .getColorFont());

        ((TextView) root.findViewById(R.id.time4_title)).setTextColor(c
                .getColorFont());
        ((TextView) root.findViewById(R.id.time4_pogoda)).setTextColor(c
                .getColorFont());

        Log.e(getClass().toString(),
                "COLOR = " + new MyColor().getColorComponents());

    }

    public void addHeader(int y, int m, int d) {
        ContentValues cv = new ContentValues();

        boolean isHavePrognosis = false;
        cv.put("isHavePrognosis", isHavePrognosis);
        try {

            ArrayList<OneWeatherDay> array = new ArrayList<OneWeatherDay>(
                    MainActivity.getInstance().WEATHERS);

            for (int i = 0; i < array.size(); i++) {

                if (d == array.get(i).getDay() && m == array.get(i).getMounth()
                        && y == array.get(i).getYear()) {

                    isHavePrognosis = true;
                    cv.put("pogoda1", array.get(i).getWeatersDay().get(0)
                            .getPogoda());
                    cv.put("pogoda2", array.get(i).getWeatersDay().get(1)
                            .getPogoda());
                    cv.put("pogoda3", array.get(i).getWeatersDay().get(2)
                            .getPogoda());
                    cv.put("pogoda4", array.get(i).getWeatersDay().get(3)
                            .getPogoda());

                    image1 = array.get(i).getWeatersDay().get(0).getImage();
                    image2 = array.get(i).getWeatersDay().get(1).getImage();
                    image3 = array.get(i).getWeatersDay().get(2).getImage();
                    image4 = array.get(i).getWeatersDay().get(3).getImage();

                }
            }
            cv.put("start", false);
            cv.put("internet", MainActivity.getInstance().InternetConnection());
            cv.put("isHavePrognosis", isHavePrognosis);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }

        SetDataHeader(cv);
    }

    public void SetDataHeader(ContentValues cv) {

        try {

            TextView title = (TextView) header
                    .findViewById(R.id.header_prognoz);
            if ((Boolean) cv.get("internet")) {

                if ((Boolean) cv.get("start")) {
                    header.findViewById(R.id.header_prognoz_lin).setVisibility(
                            View.GONE);
                    header.findViewById(R.id.header_progress).setVisibility(
                            View.VISIBLE);
                } else {

                    TextView time1_title = (TextView) header
                            .findViewById(R.id.time1_title);
                    TextView time1_pogoda = (TextView) header
                            .findViewById(R.id.time1_pogoda);
                    ImageView time1_image = (ImageView) header
                            .findViewById(R.id.time1_image);

                    TextView time2_title = (TextView) header
                            .findViewById(R.id.time2_title);
                    TextView time2_pogoda = (TextView) header
                            .findViewById(R.id.time2_pogoda);
                    ImageView time2_image = (ImageView) header
                            .findViewById(R.id.time2_image);

                    TextView time3_title = (TextView) header
                            .findViewById(R.id.time3_title);
                    TextView time3_pogoda = (TextView) header
                            .findViewById(R.id.time3_pogoda);
                    ImageView time3_image = (ImageView) header
                            .findViewById(R.id.time3_image);

                    TextView time4_title = (TextView) header
                            .findViewById(R.id.time4_title);
                    TextView time4_pogoda = (TextView) header
                            .findViewById(R.id.time4_pogoda);
                    ImageView time4_image = (ImageView) header
                            .findViewById(R.id.time4_image);

                    String name = MainActivity.getInstance().getCityById(
                            MainActivity.getInstance().CITIES,
                            MainActivity.getInstance().shared.getInt(
                                    MainActivity.SHARED_CITY, 33345));
                    title.setText(MainActivity.getInstance().language.PROGNOZ_POGODI_GOROD
                            + " " + name);
                    header.findViewById(R.id.header_prognoz_lin).setVisibility(
                            View.VISIBLE);
                    header.findViewById(R.id.header_progress).setVisibility(
                            View.GONE);

                    if ((Boolean) cv.get("isHavePrognosis")) {
                        header.findViewById(R.id.header_times).setVisibility(
                                View.VISIBLE);
                        title.setText(MainActivity.getInstance().language.PROGNOZ_POGODI_GOROD
                                + " " + name);

                        time1_title
                                .setText(MainActivity.getInstance().language.TIME1);
                        time1_pogoda.setText((String) cv.get("pogoda1"));

                        time2_title
                                .setText(MainActivity.getInstance().language.TIME2);
                        time2_pogoda.setText((String) cv.get("pogoda2"));

                        time3_title
                                .setText(MainActivity.getInstance().language.TIME3);
                        time3_pogoda.setText((String) cv.get("pogoda3"));

                        time4_title
                                .setText(MainActivity.getInstance().language.TIME4);
                        time4_pogoda.setText((String) cv.get("pogoda4"));

                        time1_image.setImageBitmap(image1);
                        time2_image.setImageBitmap(image2);
                        time3_image.setImageBitmap(image3);
                        time4_image.setImageBitmap(image4);

                    } else {
                        title.setText(MainActivity.getInstance().language.NET_DANIH_PRO_POGODU);
                        header.findViewById(R.id.header_times).setVisibility(
                                View.GONE);
                    }

                }

            } else {

                title.setText(MainActivity.getInstance().language.NO_INTERNET_CONNECTION);

                header.findViewById(R.id.header_prognoz_lin).setVisibility(
                        View.VISIBLE);
                header.findViewById(R.id.header_progress).setVisibility(
                        View.GONE);
                header.findViewById(R.id.header_times).setVisibility(View.GONE);

            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }
    }

    public Drawable loadImage(String url) {
        Drawable drawableLocal;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            drawableLocal = Drawable.createFromStream(is, "srcName");
            is.close();
        } catch (Exception e) {
            drawableLocal = null;
        }

        return drawableLocal;
    }

    public void drawClick(DrawMounth month) {

        Calendar c = Calendar.getInstance();
        c.set(month.getYear(), month.getMounth() - 1, month.selected_day - 1);

        MounthFragment.getInstance().selected_date.setText(MainActivity
                .getInstance().language.NAMES_DAYS_WEEK[c
                .get(Calendar.DAY_OF_WEEK) - 1]
                + ", "
                + month.names_mounth[month.mounth - 1]
                + " "
                + +month.selected_day);

        MounthFragment.getInstance().selected_date
                .startAnimation(AnimationUtils.loadAnimation(
                        MainActivity.getInstance(), R.anim.scaletext));
        MounthFragment.getInstance().addHeader(month.year, month.mounth,
                month.selected_day);

        MounthFragment.getInstance().arrayOneEvent.clear();
        MounthFragment.getInstance().arrayOneEventAndWeaters.clear();
        MounthFragment.getInstance().arrayOneEvent = MainActivity.getInstance()
                .getEventsByDate(month.year, month.mounth, month.selected_day);

        for (int i = 0; i < MounthFragment.getInstance().arrayOneEvent.size(); i++) {
            MounthFragment.getInstance().arrayOneEventAndWeaters
                    .add(new OneEventAndWeater(
                            MounthFragment.getInstance().arrayOneEvent.get(i)));
        }
        MounthFragment.getInstance().adapter.notifyDataSetChanged();
    }

}
