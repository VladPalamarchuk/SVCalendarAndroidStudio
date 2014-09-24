package com.example.calendar;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class WeekFragment2 extends android.support.v4.app.Fragment {

    public static WeekFragment2 weekfragment2;

    ListView list_pn;
    ListView list_vt;
    ListView list_sr;
    ListView list_ch;
    ListView list_pt;
    ListView list_sb;
    ListView list_vs;
    TextView name;
    ImageView next_mounth;
    ImageView prev_mounth;
    public static ViewGroup root;
    RelativeLayout mounth;
    private static Handler handler;
    ImageView search;
    int _year, _mounth, _day;

    Bitmap image1;
    Bitmap image2;
    Bitmap image3;
    Bitmap image4;

    ImageView add_event;

    View header1;
    View header2;
    View header3;
    View header4;
    View header5;
    View header6;
    View header7;

    ArrayList<OneEventAndWeater> array_pn = new ArrayList<OneEventAndWeater>();

    ArrayList<OneEventAndWeater> array_pn1 = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEventAndWeater> array_vt1 = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEventAndWeater> array_sr1 = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEventAndWeater> array_ch1 = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEventAndWeater> array_pt1 = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEventAndWeater> array_sb1 = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEventAndWeater> array_vs1 = new ArrayList<OneEventAndWeater>();

    AdapterListEvent adapter_pn = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_pn,
            true, true, false);

    ArrayList<OneEventAndWeater> array_vt = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter_vt = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_vt,
            true, true, false);

    ArrayList<OneEventAndWeater> array_sr = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter_sr = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_sr,
            true, true, false);

    ArrayList<OneEventAndWeater> array_ch = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter_ch = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_ch,
            true, true, false);

    ArrayList<OneEventAndWeater> array_pt = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter_pt = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_pt,
            true, true, false);

    ArrayList<OneEventAndWeater> array_sb = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter_sb = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_sb,
            true, true, false);

    ArrayList<OneEventAndWeater> array_vs = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter_vs = new AdapterListEvent(
            MainActivity.getInstance(), R.layout.one_event_item, array_vs,
            true, true, false);

    RelativeLayout root_rel;
    DrawMounth2 drawMounth2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        array_pn1 = new ArrayList<OneEventAndWeater>();
        array_vt1 = new ArrayList<OneEventAndWeater>();
        array_sr1 = new ArrayList<OneEventAndWeater>();
        array_ch1 = new ArrayList<OneEventAndWeater>();
        array_pt1 = new ArrayList<OneEventAndWeater>();
        array_sb1 = new ArrayList<OneEventAndWeater>();
        array_vs1 = new ArrayList<OneEventAndWeater>();

        array_vs = new ArrayList<OneEventAndWeater>();
        array_sb = new ArrayList<OneEventAndWeater>();
        array_pt = new ArrayList<OneEventAndWeater>();
        array_ch = new ArrayList<OneEventAndWeater>();
        array_sr = new ArrayList<OneEventAndWeater>();
        array_vt = new ArrayList<OneEventAndWeater>();
        array_pn = new ArrayList<OneEventAndWeater>();

        adapter_vs = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_vs,
                true, true, false);
        adapter_sb = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_sb,
                true, true, false);

        adapter_pt = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_pt,
                true, true, false);

        adapter_ch = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_ch,
                true, true, false);

        adapter_sr = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_sr,
                true, true, false);
        adapter_vt = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_vt,
                true, true, false);
        adapter_pn = new AdapterListEvent(
                MainActivity.getInstance(), R.layout.one_event_item, array_pn,
                true, true, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.week_fragment2, null);

        setView();
        weekfragment2 = this;
        final int number_year = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT2_YEAR);
        final int number_mounth = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT2_MOUNTH);
        final int number_day = getArguments().getInt(
                MainActivity.WEEK_FRAGMENT2_DAY);

        search = (ImageView) root
                .findViewById(R.id.week_fragment2_search_image);

        mounth = (RelativeLayout) root.findViewById(R.id.week_frgament2_mounth);
        drawMounth2 = new DrawMounth2(MainActivity.getInstance(), number_year,
                number_mounth, number_day);
        add_event = (ImageView) root
                .findViewById(R.id.week_fragment2_add_event);

        add_event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MainActivity.getInstance().startAddEventFragment(false, false,
                        MainActivity.getNowYear(), MainActivity.getNowMounth(),
                        MainActivity.getNowDay(), MainActivity.getNowHour(),
                        MainActivity.getNowMinute());
            }
        });
        mounth.addView(drawMounth2);

        DrawLinesWeek2 view = new DrawLinesWeek2(MainActivity.getInstance());
        DrawNameDay drawNameDay = new DrawNameDay(MainActivity.getInstance());
        ((RelativeLayout) root
                .findViewById(R.id.week_frgament2_name_day_mounth))
                .addView(drawNameDay);

        root_rel = (RelativeLayout) root
                .findViewById(R.id.week_fragment2_root_rel);

        root_rel.addView(view);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setContentTrue(number_year, number_mounth, number_day);
                setAdapters();
            }
        }, 100);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.e(getClass().toString(), "handler message");

                ContentValues cv = (ContentValues) msg.obj;

                if (!cv.getAsBoolean("isEvents")) {
                    ShowProgres();
                } else {

                    ((TextView) root.findViewById(R.id.week_fragment2_pn_title))
                            .setText(cv.getAsString("T1"));
                    ((TextView) root.findViewById(R.id.week_fragment2_vt_title))
                            .setText(cv.getAsString("T2"));
                    ((TextView) root.findViewById(R.id.week_fragment2_sr_title))
                            .setText(cv.getAsString("T3"));
                    ((TextView) root.findViewById(R.id.week_fragment2_ch_title))
                            .setText(cv.getAsString("T4"));
                    ((TextView) root.findViewById(R.id.week_fragment2_pt_title))
                            .setText(cv.getAsString("T5"));
                    ((TextView) root.findViewById(R.id.week_fragment2_sb_title))
                            .setText(cv.getAsString("T6"));
                    ((TextView) root.findViewById(R.id.week_fragment2_vs_title))
                            .setText(cv.getAsString("T7"));

                    // array_pn.clear();
                    // for (int i = 0; i < array_pn1.size(); i++)
                    // array_pn.add(array_pn1.get(i));

                    // array_vt.clear();
                    // for (int i = 0; i < array_vt1.size(); i++)
                    // array_vt.add(array_vt1.get(i));

                    // array_sr.clear();
                    // for (int i = 0; i < array_sr1.size(); i++)
                    // array_sr.add(array_sr1.get(i));
                    // array_ch.clear();
                    // for (int i = 0; i < array_ch1.size(); i++)
                    // array_ch.add(array_ch1.get(i));
                    // array_pt.clear();
                    // for (int i = 0; i < array_pt1.size(); i++)
                    // array_pt.add(array_pt1.get(i));
                    // array_sb.clear();
                    // for (int i = 0; i < array_sb1.size(); i++)
                    // array_sb.add(array_sb1.get(i));
                    // array_vs.clear();
                    // for (int i = 0; i < array_vs1.size(); i++)
                    // array_vs.add(array_vs1.get(i));

                    array_pn.addAll(array_pn1);
                    array_vt.addAll(array_vt1);
                    array_sr.addAll(array_sr1);
                    array_ch.addAll(array_ch1);
                    array_pt.addAll(array_pt1);
                    array_sb.addAll(array_sb1);
                    array_vs.addAll(array_vs1);

                    NotifyAdapters();
                    unShowProgres();
                }
            }
        };

        next_mounth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                root.findViewById(R.id.week_frgament2_mounth).setVisibility(
                        View.INVISIBLE);
                root.findViewById(R.id.progres_mounth).setVisibility(
                        View.VISIBLE);

                if (_mounth == 12) {
                    _mounth = 1;
                    _year++;

                } else {
                    _mounth++;

                }

                mounth.removeView(drawMounth2);

                drawMounth2 = new DrawMounth2(MainActivity.getInstance(),
                        _year, _mounth, 1);
                setContentTrue(_year, _mounth, 1);
                mounth.addView(drawMounth2);
                root.findViewById(R.id.week_frgament2_mounth).setVisibility(
                        View.VISIBLE);
                root.findViewById(R.id.progres_mounth).setVisibility(
                        View.INVISIBLE);

            }
        });

        prev_mounth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                root.findViewById(R.id.week_frgament2_mounth).setVisibility(
                        View.INVISIBLE);
                root.findViewById(R.id.progres_mounth).setVisibility(
                        View.VISIBLE);

                if (_mounth == 1) {
                    _mounth = 12;
                    _year--;

                } else {
                    _mounth--;

                }

                mounth.removeView(drawMounth2);
                drawMounth2 = new DrawMounth2(MainActivity.getInstance(),
                        _year, _mounth, 1);
                mounth.addView(drawMounth2);
                setContentTrue(_year, _mounth, 1);
                root.findViewById(R.id.week_frgament2_mounth).setVisibility(
                        View.VISIBLE);
                root.findViewById(R.id.progres_mounth).setVisibility(
                        View.INVISIBLE);

            }
        });

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.week_fragment2_open_left_menu),
                        (Spinner) root
                                .findViewById(R.id.week2_fragment_spinner)
                );

        setColor();
        return root;
    }

    public static WeekFragment2 getInstance() {
        return weekfragment2;
    }

    public void setColor() {
        MyColor c = new MyColor();
        root.findViewById(R.id.week_fragmant2_bacground).setBackgroundColor(
                c.getColorBacground());
        root.findViewById(R.id.week_fragment2_top).setBackgroundColor(
                c.getColorComponents());

    }

    public void setContent(int year, int mounth, int day) {

        this._year = year;
        this._mounth = mounth;
        this._day = day;

        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();

                Log.e(getClass().toString(),
                        "week fragment 2 set content thread start");

                ContentValues cv = new ContentValues();
                Message m = handler.obtainMessage(1, cv);

                cv.put("isEvents", false);

                handler.sendMessage(m);

                int count = new GetCountDay().getCountDay(_year, _mounth);
                array_pn1.clear();
                array_vt1.clear();
                array_sr1.clear();
                array_ch1.clear();
                array_pt1.clear();
                array_sb1.clear();
                array_vs1.clear();
                String text1;
                String text2;
                String text3;
                String text4;
                String text5;
                String text6;
                String text7;

                ArrayList<OneEvent> a = MainActivity.getInstance()
                        .getEventsByDate(_year, _mounth, _day);

                for (int i = 0; i < a.size(); i++) {
                    array_pn1.add(new OneEventAndWeater(a.get(i)));

                }

                text1 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[0]
                        + " " + _day);

                final int y_pn = _year;
                final int m_pn = _mounth;
                final int d_pn = _day;

                root.findViewById(R.id.week_fragment2_pn_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_pn, m_pn, d_pn);

                            }
                        });

                _day++;
                if (_day <= count) {
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_vt1.add(new OneEventAndWeater(a.get(i)));
                    }
                } else {
                    if (_mounth == 12) {
                        _mounth = 1;
                        _year++;
                        _day = 1;
                    } else {
                        _mounth++;
                        _day = 1;
                    }
                    count = new GetCountDay().getCountDay(_year, _mounth);
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_vt1.add(new OneEventAndWeater(a.get(i)));
                    }
                }
                text2 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[1]
                        + " " + _day);

                final int y_vt = _year;
                final int m_vt = _mounth;
                final int d_vt = _day;

                root.findViewById(R.id.week_fragment2_vt_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_vt, m_vt, d_vt);

                            }
                        });

                _day++;
                if (_day <= count) {
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_sr1.add(new OneEventAndWeater(a.get(i)));
                    }
                } else {
                    if (_mounth == 12) {
                        _mounth = 1;
                        _year++;
                        _day = 1;
                    } else {
                        _mounth++;
                        _day = 1;
                    }
                    count = new GetCountDay().getCountDay(_year, _mounth);
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_sr1.add(new OneEventAndWeater(a.get(i)));
                    }

                }
                text3 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[2]
                        + " " + _day);

                final int y_sr = _year;
                final int m_sr = _mounth;
                final int d_sr = _day;

                root.findViewById(R.id.week_fragment2_sr_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_sr, m_sr, d_sr);

                            }
                        });
                _day++;
                if (_day <= count) {
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_ch1.add(new OneEventAndWeater(a.get(i)));
                    }
                } else {
                    if (_mounth == 12) {
                        _mounth = 1;
                        _year++;
                        _day = 1;
                    } else {
                        _mounth++;
                        _day = 1;
                    }
                    count = new GetCountDay().getCountDay(_year, _mounth);
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_ch1.add(new OneEventAndWeater(a.get(i)));
                    }

                }
                text4 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[3]
                        + " " + _day);
                final int y_ch = _year;
                final int m_ch = _mounth;
                final int d_ch = _day;

                root.findViewById(R.id.week_fragment2_ch_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_ch, m_ch, d_ch);

                            }
                        });
                _day++;
                if (_day <= count) {
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_pt1.add(new OneEventAndWeater(a.get(i)));
                    }
                } else {
                    if (_mounth == 12) {
                        _mounth = 1;
                        _year++;
                        _day = 1;
                    } else {
                        _mounth++;
                        _day = 1;
                    }
                    count = new GetCountDay().getCountDay(_year, _mounth);
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_pt1.add(new OneEventAndWeater(a.get(i)));
                    }

                }
                text5 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[4]
                        + " " + _day);
                final int y_pt = _year;
                final int m_pt = _mounth;
                final int d_pt = _day;

                root.findViewById(R.id.week_fragment2_pt_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_pt, m_pt, d_pt);

                            }
                        });

                _day++;
                if (_day <= count) {
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_sb1.add(new OneEventAndWeater(a.get(i)));
                    }
                } else {
                    if (_mounth == 12) {
                        _mounth = 1;
                        _year++;
                        _day = 1;
                    } else {
                        _mounth++;
                        _day = 1;
                    }
                    count = new GetCountDay().getCountDay(_year, _mounth);
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_sb1.add(new OneEventAndWeater(a.get(i)));
                    }

                }
                text6 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[5]
                        + " " + _day);

                final int y_sb = _year;
                final int m_sb = _mounth;
                final int d_sb = _day;

                root.findViewById(R.id.week_fragment2_sb_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_sb, m_sb, d_sb);

                            }
                        });
                _day++;
                if (_day <= count) {
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_vs1.add(new OneEventAndWeater(a.get(i)));
                    }
                } else {
                    if (_mounth == 12) {
                        _mounth = 1;
                        _year++;
                        _day = 1;
                    } else {
                        _mounth++;
                        _day = 1;
                    }
                    count = new GetCountDay().getCountDay(_year, _mounth);
                    a = MainActivity.getInstance().getEventsByDate(_year,
                            _mounth, _day);
                    for (int i = 0; i < a.size(); i++) {
                        array_vs1.add(new OneEventAndWeater(a.get(i)));
                    }

                }
                text7 = (MainActivity.getInstance().language.NAMES_DAYS_WEEK[6]
                        + " " + _day);

                final int y_vs = _year;
                final int m_vs = _mounth;
                final int d_vs = _day;

                root.findViewById(R.id.week_fragment2_vs_title)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                MainActivity.getInstance().startListEventOfDay(
                                        y_vs, m_vs, d_vs);

                            }
                        });
                cv.clear();
                m = handler.obtainMessage(1, cv);

                cv.put("isEvents", true);
                cv.put("T1", text1);
                cv.put("T2", text2);
                cv.put("T3", text3);
                cv.put("T4", text4);
                cv.put("T5", text5);
                cv.put("T6", text6);
                cv.put("T7", text7);

                handler.sendMessage(m);

                Log.e(getClass().toString(),
                        "week fragment 2 set content thread end");
            }
        }).start();

    }

    public void setView() {
        list_pn = (ListView) root.findViewById(R.id.week_fragment2_pn);
        list_vt = (ListView) root.findViewById(R.id.week_fragment2_vt);
        list_sr = (ListView) root.findViewById(R.id.week_fragment2_sr);
        list_ch = (ListView) root.findViewById(R.id.week_fragment2_ch);
        list_pt = (ListView) root.findViewById(R.id.week_fragment2_pt);
        list_sb = (ListView) root.findViewById(R.id.week_fragment2_sb);
        list_vs = (ListView) root.findViewById(R.id.week_fragment2_vs);
        name = (TextView) root.findViewById(R.id.week_fragment2_name);
        next_mounth = (ImageView) root.findViewById(R.id.week_fragment2_pravo);
        prev_mounth = (ImageView) root.findViewById(R.id.week_fragment2_levo);
        list_pn.setDividerHeight(0);
        list_vt.setDividerHeight(0);
        list_sr.setDividerHeight(0);
        list_ch.setDividerHeight(0);
        list_pt.setDividerHeight(0);
        list_sb.setDividerHeight(0);
        list_vs.setDividerHeight(0);

    }

    public void setAdapters() {
        list_pn.setAdapter(adapter_pn);
        list_vt.setAdapter(adapter_vt);
        list_sr.setAdapter(adapter_sr);
        list_ch.setAdapter(adapter_ch);
        list_pt.setAdapter(adapter_pt);
        list_sb.setAdapter(adapter_sb);
        list_vs.setAdapter(adapter_vs);
        NotifyAdapters();
    }

    public void NotifyAdapters() {
        adapter_pn.notifyDataSetChanged();
        adapter_vt.notifyDataSetChanged();
        adapter_sr.notifyDataSetChanged();
        adapter_ch.notifyDataSetChanged();
        adapter_pt.notifyDataSetChanged();
        adapter_sb.notifyDataSetChanged();
        adapter_vs.notifyDataSetChanged();
    }

    public void ShowProgres() {
        root.findViewById(R.id.week_fragment2_pn).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_pn_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_pn).setVisibility(View.VISIBLE);

        root.findViewById(R.id.week_fragment2_vt).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_vt_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_vt).setVisibility(View.VISIBLE);

        root.findViewById(R.id.week_fragment2_sr).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_sr_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_sr).setVisibility(View.VISIBLE);

        root.findViewById(R.id.week_fragment2_ch).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_ch_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_ch).setVisibility(View.VISIBLE);

        root.findViewById(R.id.week_fragment2_pt).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_pt_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_pt).setVisibility(View.VISIBLE);

        root.findViewById(R.id.week_fragment2_sb).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_sb_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_sb).setVisibility(View.VISIBLE);

        root.findViewById(R.id.week_fragment2_vs).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.week_fragment2_vs_title).setVisibility(
                View.INVISIBLE);
        root.findViewById(R.id.progres_vs).setVisibility(View.VISIBLE);
    }

    public void unShowProgres() {
        root.findViewById(R.id.week_fragment2_pn).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_pn_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_pn).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.week_fragment2_vt).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_vt_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_vt).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.week_fragment2_sr).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_sr_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_sr).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.week_fragment2_ch).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_ch_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_ch).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.week_fragment2_pt).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_pt_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_pt).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.week_fragment2_sb).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_sb_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_sb).setVisibility(View.INVISIBLE);

        root.findViewById(R.id.week_fragment2_vs).setVisibility(View.VISIBLE);
        root.findViewById(R.id.week_fragment2_vs_title).setVisibility(
                View.VISIBLE);
        root.findViewById(R.id.progres_vs).setVisibility(View.INVISIBLE);
    }

    public void setContentTrue(int year, int mounth, int day) {

        Calendar c = Calendar.getInstance();

        c.set(year, mounth - 1, day - 1);
        int number_day = c.get(Calendar.DAY_OF_WEEK);
        if (number_day == 1) {
            setContent(year, mounth, day);
        } else {

            if (day >= number_day) {

                day = day - number_day + 1;
                setContent(year, mounth, day);
            } else {

                int count_day = 0;
                if (mounth != 1) {
                    count_day = new GetCountDay().getCountDay(year, mounth - 1);
                    mounth--;
                    setContent(year, mounth, count_day - number_day + 2);

                } else {
                    count_day = new GetCountDay().getCountDay(year - 1, 12);
                    year--;
                    mounth = 12;
                    setContent(year, mounth, count_day - number_day + 2);

                }

            }
        }

    }

    public void addHeader(View header, AdapterListEvent adapter, int y, int m,
                          int d) {
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
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e
            );
        }

        SetDataHeader(cv, header, adapter);
    }

    public void SetDataHeader(ContentValues cv, View header,
                              AdapterListEvent adapter) {

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
                                    MainActivity.SHARED_CITY, 33345)
                    );
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
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e
            );
        }
    }

}
