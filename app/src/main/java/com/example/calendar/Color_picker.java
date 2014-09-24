package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public class Color_picker extends Activity {
    Intent intent;
    public static final String ASSIGNING_COLOR = "assigning_color";
    public static final int ADD_EVENT = -1;

    public static final int COLOR_BACKGROUND = 0;
    public static final int COLOR_COMPONENTS = 1;
    public static final int COLOR_FONT = 2;
    public static final int COLOR_LABEL = 3;
    public static final int COLOR_EVENT_YEAR = 4;
    public static final int COLOR_EVENT_MOUNTH = 5;
    public static final int COLOR_NOW_DATE = 6;
    public static final int COLOR_HOLIDAY = 7;

    ImageView color_picker_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_color_picker);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        final SVBar svBar = (SVBar) findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        color_picker_ok = (ImageView) findViewById(R.id.color_picker_ok);
        intent = getIntent();
        final int ass_color = intent.getExtras().getInt(ASSIGNING_COLOR);

        color_picker_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                switch (ass_color) {
                    case ADD_EVENT:

                        AddEventFragment.getInstance().color = picker.getColor();
                        AddEventFragment.getInstance().add_event_color_result
                                .setBackgroundColor(picker.getColor());
                        finish();
                        break;

                    case COLOR_BACKGROUND:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_BACKGROUND,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();

                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();

                        finish();
                        break;
                    case COLOR_COMPONENTS:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_COMPONENTS,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;
                    case COLOR_FONT:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_FONT,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;
                    case COLOR_EVENT_YEAR:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_EVENT_YEAR,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;
                    case COLOR_EVENT_MOUNTH:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_EVENT_MOUNTH,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;

                    case COLOR_LABEL:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_LABEL,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;

                    case COLOR_NOW_DATE:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_NOW_DATE,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;

                    case COLOR_HOLIDAY:

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_COLOR_HOLIDAY,
                                        picker.getColor()).commit();
                        ColorPickerFragment.getInstance().UpdateItems();
                        ColorPickerFragment.getInstance().adapter
                                .notifyDataSetChanged();
                        ColorPickerFragment.getInstance().setColor();
                        MainActivity.getInstance().setColor();
                        finish();
                        break;

                }

            }
        });
        setColor();
    }

    @SuppressLint("NewApi")
    public void setColor() {

        findViewById(R.id.color_picker_root).setBackground(
                MainActivity.getInstance().findViewById(R.id.main_root)
                        .getBackground());
        findViewById(R.id.color_picker_background).setBackgroundColor(
                new MyColor().getColorBacground());
    }
}
