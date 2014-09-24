package com.example.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class MounthFragment2 extends android.support.v4.app.Fragment {

    ViewGroup root;
    static MounthFragment2 mounthFragment2;
    RelativeLayout mounth;
    DrawMounth drawMounth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        root = (ViewGroup) inflater.inflate(R.layout.mounth_fragment2, null);
        mounth = (RelativeLayout) root
                .findViewById(R.id.mounth_fragment2_mounth);

        int number_year = getArguments().getInt(
                MainActivity.MOUNTH_FRAGMENT2_YEAR);
        int number_mounth = getArguments().getInt(
                MainActivity.MOUNTH_FRAGMENT2_MOUNTH);

        drawMounth = new DrawMounth(MainActivity.getInstance(), number_year,
                number_mounth,
                MainActivity.getInstance().language.NAMES_MOUNTH,
                MainActivity.THEME_APPLICATION, 1);

        DrawNameDay drawNameDay = new DrawNameDay(MainActivity.getInstance());

        ((RelativeLayout) root.findViewById(R.id.mounth_fragment2_name_days))
                .addView(drawNameDay);

        mounth.addView(drawMounth);

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.mounth_fragment2_open_left_menu),
                        (Spinner) root
                                .findViewById(R.id.mounth_fragment2_spinner));

        setColor();
        return root;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub

        super.onDestroyView();

    }

    public static MounthFragment2 getInstance() {
        return mounthFragment2;
    }

    public void setColor() {
        MyColor c = new MyColor();
        root.findViewById(R.id.mounth_fragment2_bacground).setBackgroundColor(
                c.getColorBacground());
        root.findViewById(R.id.mounth_fragment2_top).setBackgroundColor(
                c.getColorComponents());
    }

}
