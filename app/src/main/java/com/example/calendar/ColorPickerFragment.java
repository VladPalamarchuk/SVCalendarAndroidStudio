package com.example.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ColorPickerFragment extends android.support.v4.app.Fragment {

    static ColorPickerFragment colorPickerFragment;
    ArrayList<OneColorPicker> items = new ArrayList<OneColorPicker>();

    AdapterColorPicker adapter = new AdapterColorPicker(
            MainActivity.getInstance(), 1, items);
    public OneColorPicker color_bacground;
    public OneColorPicker color_components;
    public OneColorPicker color_font;
    public OneColorPicker color_label;
    public OneColorPicker color_event_year;
    public OneColorPicker color_event_mount;
    public OneColorPicker color_now_date;
    public OneColorPicker color_holiday;
    ListView list;
    ViewGroup root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        root = (ViewGroup) inflater.inflate(R.layout.color_picker_fragment,
                null);
        colorPickerFragment = this;
        list = (ListView) root.findViewById(R.id.color_picker_fragment_list);
        UpdateItems();

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.getInstance(),
                        Color_picker.class);
                intent.putExtra(Color_picker.ASSIGNING_COLOR, position);
                startActivity(intent);
                new PlaySoundButton();

            }
        });
        setColor();

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.color_picker_fragment_open_left_menu),
                        (Spinner) root.findViewById(R.id.color_picker_spinner));

        return root;

    }

    static public ColorPickerFragment getInstance() {
        return colorPickerFragment;
    }

    public void UpdateItems() {

        color_bacground = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_BACKGROUND,
                MainActivity.SHARED_COLOR_BACKGROUND,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_BACKGROUND, Color.WHITE));

        color_components = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_COMPONENTS,
                MainActivity.SHARED_COLOR_COMPONENTS,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_COMPONENTS, Color.BLUE));

        color_font = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_FONT,
                MainActivity.SHARED_COLOR_FONT,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_FONT, Color.BLACK));

        color_label = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_LABEL,
                MainActivity.SHARED_COLOR_LABEL,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_LABEL, Color.WHITE));

        color_event_year = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_EVENT_YEAR,
                MainActivity.SHARED_COLOR_EVENT_YEAR,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_EVENT_YEAR, Color.BLUE));

        color_event_mount = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_EVENT_MOUNTH,
                MainActivity.SHARED_COLOR_EVENT_MOUNTH,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_EVENT_MOUNTH, Color.BLUE));

        color_now_date = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_NOW_DATE,
                MainActivity.SHARED_COLOR_NOW_DATE,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_NOW_DATE, Color.RED));

        color_holiday = new OneColorPicker(
                MainActivity.getInstance().language.COLOR_HOLIDAY,
                MainActivity.SHARED_COLOR_HOLIDAY,
                MainActivity.getInstance().shared.getInt(
                        MainActivity.SHARED_COLOR_HOLIDAY, Color.RED));

        items.clear();
        items.add(color_bacground);
        items.add(color_components);
        items.add(color_font);
        items.add(color_label);
        items.add(color_event_year);
        items.add(color_event_mount);
        items.add(color_now_date);
        items.add(color_holiday);
    }

    public void setColor() {
        MyColor color = new MyColor();
        root.findViewById(R.id.color_picker_bacground).setBackgroundColor(
                color.getColorBacground());
        root.findViewById(R.id.color_picker_fragment_top).setBackgroundColor(
                color.getColorComponents());
    }
}
