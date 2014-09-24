package com.example.calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SettingsFragment extends android.support.v4.app.Fragment {

    String days_holiday = "";
    static SettingsFragment settingsFragment;
    Button start_color_picker;
    Spinner spinner_language;
    Spinner spinner_country;
    Spinner spinner_city;
    String[] languages = {"�������"};
    ArrayAdapter<String> adapter_change_language;
    ArrayAdapter<String> adapter_change_country;
    ArrayAdapter<String> adapter_change_city;
    ArrayList<String> cuntries = new ArrayList<String>();
    ArrayList<String> cityes = new ArrayList<String>();
    Button change_image;
    ViewGroup root;
    CheckBox am_pm;
    Spinner spiner_sound;
    int key_sound = 0;
    Button change_holidey;
    Button push_time_settings;
    Button week_show_day;
    Button export;
    Button import_ical;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        root = (ViewGroup) inflater.inflate(R.layout.settings, null);
        change_image = (Button) root.findViewById(R.id.settings_change_image);
        export = (Button) root.findViewById(R.id.settings_export);
        start_color_picker = (Button) root
                .findViewById(R.id.main_open_color_picker);
        am_pm = (CheckBox) root.findViewById(R.id.settings_ampm_checkbox);
        change_holidey = (Button) root
                .findViewById(R.id.settings_change_holiday);
        spinner_language = (Spinner) root
                .findViewById(R.id.settings_change_language);
        push_time_settings = (Button) root
                .findViewById(R.id.settings_times_push);
        week_show_day = (Button) root.findViewById(R.id.settings_week_show_day);
        spinner_country = (Spinner) root
                .findViewById(R.id.settings_change_country);
        spinner_city = (Spinner) root.findViewById(R.id.settings_change_city);

        import_ical = (Button) root.findViewById(R.id.settings_import_ical);
        start_color_picker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                MainActivity.getInstance().startColorPickerFragment();

            }
        });

        import_ical.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().chooseFileImport();

            }
        });

        export.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().showExportIcal();

            }
        });

        change_holidey.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new PlaySoundButton();

                showChangeHoliday();

            }
        });

        week_show_day.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showWeekShowDay(true);

            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new GalleryCarousel(MainActivity.getInstance());
                new PlaySoundButton();
            }
        });

        getCuntries();

        am_pm.setChecked(MainActivity.getInstance().shared.getBoolean(
                MainActivity.SHARED_IS_AMPM, false));
        am_pm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                MainActivity.getInstance().shared.edit()
                        .putBoolean(MainActivity.SHARED_IS_AMPM, isChecked)
                        .commit();
            }
        });

        adapter_change_language = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_spinner_item, languages);
        adapter_change_language
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_language.setAdapter(adapter_change_language);
        spinner_language
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_LANGUAGE,
                                        position + 1).commit();

                        MainActivity.getInstance().language = new Languages(
                                MainActivity.getInstance().shared.getInt(
                                        MainActivity.getInstance().SHARED_LANGUAGE,
                                        1));
                        MainActivity.getInstance().setText();

                        setText();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

        adapter_change_country = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_spinner_item, cuntries);
        adapter_change_country
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter_change_country);

        spinner_country.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                getCityes(cuntries.get(position));
                adapter_change_city.notifyDataSetChanged();
                Collections.sort(cityes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapter_change_city = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_spinner_item, cityes);
        adapter_change_country
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(adapter_change_city);

        spinner_city.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().shared
                        .edit()
                        .putInt(MainActivity.SHARED_CITY,
                                MainActivity.getInstance().getIdCityByName(
                                        MainActivity.getInstance().CITIES,
                                        cityes.get(position))).commit();

                MainActivity.getInstance().getWeather();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        push_time_settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                initSpinerTimePush(true);
            }
        });

        spinner_country.setSelection(getCuntryPosition(MainActivity
                .getInstance().shared.getInt(MainActivity.SHARED_CITY, 33345)));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                spinner_city.setSelection(getCityPosition(MainActivity
                        .getInstance().shared.getInt(MainActivity.SHARED_CITY,
                                33345)));

            }
        }, 100);
        setColor();

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.settings_fragment_open_left_menu),
                        (Spinner) root.findViewById(R.id.settings_spinner));

        InitSoundSpiner();
        setText();
        return root;
    }

    public static SettingsFragment getInstance() {
        return settingsFragment;
    }

    public void setColor() {

        MyColor c = new MyColor();
        root.findViewById(R.id.settings_background).setBackgroundColor(
                c.getColorBacground());
        MainActivity.setButton(start_color_picker);
        MainActivity.setButton(change_holidey);
        root.findViewById(R.id.settings_fragment_top).setBackgroundColor(
                c.getColorComponents());

        MainActivity.setButton(change_image);
        MainActivity.setButton(push_time_settings);
        MainActivity.setButton(week_show_day);

        MainActivity.setButton(export);
    }

    public void getCuntries() {

        ArrayList<OneCity> AllCuntries = MainActivity.getInstance()
                .getTheCountry(MainActivity.getInstance().CITIES);

        for (int i = 0; i < AllCuntries.size(); i++) {
            cuntries.add(AllCuntries.get(i).getCountry().toString());
        }

    }

    public void getCityes(String country) {
        cityes.clear();
        ArrayList<OneCity> a = MainActivity.getInstance().getCitiesByCountry(
                MainActivity.getInstance().CITIES, country);

        for (int i = 0; i < a.size(); i++) {
            cityes.add(a.get(i).getName());
        }
    }

    public int getCuntryPosition(int id) {
        String name = MainActivity.getInstance().getCountryById(
                MainActivity.getInstance().CITIES, id);
        int res = 0;
        for (int i = 0; i < cuntries.size(); i++) {
            if (cuntries.get(i).equals(name))
                return i;
        }

        return res;
    }

    public int getCityPosition(int id) {
        String name = MainActivity.getInstance().getCityById(
                MainActivity.getInstance().CITIES, id);
        int res = 0;
        for (int i = 0; i < cityes.size(); i++) {
            if (cityes.get(i).equals(name))
                return i;
        }

        return res;
    }

    public ArrayList<String> getListAssets() {
        ArrayList<String> res = new ArrayList<String>();
        AssetManager aMan = MainActivity.getInstance().getAssets();
        try {
            String[] s = aMan.list("");

            int index = 0;
            for (int i = 0; i < s.length; i++) {
                if (s[i].indexOf(".mp3") > 0) {
                    s[i] = s[i].substring(0, s[i].indexOf("."));
                    res.add(s[i]);
                    index++;
                }
            }
            Collections.sort(res);

            return res;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // String[] s = { "Not have sound" };
        return res;
    }

    public void setText() {
        start_color_picker
                .setText(MainActivity.getInstance().language.CHANGE_COLOR);
        change_image
                .setText(MainActivity.getInstance().language.CHANGE_BACGROUND_IMAGE);
        spinner_language
                .setPrompt(MainActivity.getInstance().language.SETTINGS_CHANGE_LANGUAGE);
        spinner_country
                .setPrompt(MainActivity.getInstance().language.SETTINGS_CHANGE_COUNTRY);
        spinner_city
                .setPrompt(MainActivity.getInstance().language.SETTINGS_CHANGE_CITY);
        ((TextView) root.findViewById(R.id.settings_sound_title))
                .setText(MainActivity.getInstance().language.SETTINGS_SOUND_TITLE);

        ((TextView) root.findViewById(R.id.settings_change_holidays))
                .setText(MainActivity.getInstance().language.CHANGE_HOLIDAYS);

        ((TextView) root.findViewById(R.id.settings_times_push_title))
                .setText(MainActivity.getInstance().language.TIMES_PUSH);

        setButtonHolidaysText();

        ((TextView) root.findViewById(R.id.settings_week_show_day_title))
                .setText(MainActivity.getInstance().language.WEEK_SHOW_DAY);

        export.setText(MainActivity.getInstance().language.EXPORT);

        initSpinerTimePush(false);
        showWeekShowDay(false);
    }

    public void InitSoundSpiner() {

        spiner_sound = (Spinner) root.findViewById(R.id.settings_sounds);
        ArrayList<String> spinner_array = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_spinner_item, spinner_array);

        spinner_array.add(MainActivity.getInstance().language.NOT_SOUND);
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "1");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "2");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "3");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "4");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "5");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "6");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "7");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "8");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "9");
        spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
                + "10");

        spiner_sound.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spiner_sound.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                key_sound++;
                if (key_sound > 2) {
                    MainActivity.getInstance().shared.edit()
                            .putInt(MainActivity.SHARED_SOUND_BUTTON, position)
                            .commit();
                    new PlaySoundButton();
                    Log.e(getClass().toString(), "play item selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spiner_sound.setSelection(MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_SOUND_BUTTON, 0));

    }

    public void showChangeHoliday() {
        AlertDialog dialog = new Builder(MainActivity.getInstance())
                .create();
        final ListView list = new ListView(MainActivity.getInstance());
        ArrayList<String> array = new ArrayList<String>(
                Arrays.asList(MainActivity.getInstance().language.NAMES_DAYS_WEEK));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_list_item_multiple_choice, array);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);
        dialog.setView(list);
        dialog.setButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        ArrayList<Integer> a = MainActivity.getHolidays();
        for (int i = 0; i < a.size(); i++) {
            list.setItemChecked(a.get(i), true);
        }

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                days_holiday = "";
                SparseBooleanArray sbArray = list.getCheckedItemPositions();
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key)) {

                        days_holiday += key + ",";

                    }
                }

                if (days_holiday.length() > 1)
                    days_holiday = days_holiday.substring(0,
                            days_holiday.length() - 1);

                MainActivity.getInstance().shared
                        .edit()
                        .putString(MainActivity.getInstance().SHARED_HOLIDAYS,
                                days_holiday).commit();

                setButtonHolidaysText();

            }
        });
        dialog.show();

    }

    public void setButtonHolidaysText() {
        ArrayList<Integer> a = MainActivity.getHolidays();
        if (a.size() > 0) {
            String text = "";
            for (int i = 0; i < a.size(); i++) {
                text += MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[a
                        .get(i)] + ",";
            }

            text = text.substring(0, text.length() - 1);
            change_holidey.setText(text);
        } else {

            change_holidey
                    .setText(MainActivity.getInstance().language.NO_SELECTION);
        }

    }

    int time = 60000 * 15;

    public void initSpinerTimePush(boolean isShow) {
        AlertDialog dialog = new Builder(MainActivity.getInstance())
                .create();
        ArrayList<String> array = new ArrayList<String>();
        ListView list = new ListView(MainActivity.getInstance());
        String m = MainActivity.getInstance().language.MINUT;
        String h = MainActivity.getInstance().language.HOUR;
        String hs = MainActivity.getInstance().language.HOURS;
        array.add("5 " + m);
        array.add("10 " + m);
        array.add("15 " + m);
        array.add("30 " + m);
        array.add("45 " + m);
        array.add("1 " + h);
        array.add("1 " + h + " 30 " + m);
        array.add("2 " + hs);
        array.add("3 " + hs);
        array.add("24 " + hs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_list_item_multiple_choice, array);
        list.setAdapter(adapter);
        dialog.setView(list);

        int time_shared = MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_TIME_PUSH, 15 * 60000);
        int pos = 2;

        switch (time_shared) {
            case 60000 * 5:
                pos = 0;
                break;
            case 60000 * 10:
                pos = 1;
                break;
            case 60000 * 15:
                pos = 2;
                break;
            case 60000 * 30:
                pos = 3;
                break;
            case 60000 * 45:
                pos = 4;
                break;
            case 3600000:
                pos = 5;
                break;
            case 5400000:
                pos = 6;
                break;
            case 3600000 * 2:
                pos = 7;
                break;
            case 3600000 * 3:
                pos = 8;
                break;
            case 3600000 * 24:
                pos = 9;
                break;
        }

        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(pos, true);

        push_time_settings.setText(array.get(pos));

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                switch (position) {
                    case 0:
                        time = 60000 * 5;
                        break;
                    case 1:
                        time = 60000 * 10;
                        break;
                    case 2:
                        time = 60000 * 15;
                        break;
                    case 3:
                        time = 60000 * 30;
                        break;
                    case 4:
                        time = 60000 * 45;
                        break;
                    case 5:
                        time = 3600000;
                        break;
                    case 6:
                        time = 5400000;
                        break;
                    case 7:
                        time = 3600000 * 2;
                        break;
                    case 8:
                        time = 3600000 * 3;
                        break;
                    case 9:
                        time = 3600000 * 24;
                        break;

                }

            }
        });

        dialog.setButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().shared.edit()
                        .putInt(MainActivity.SHARED_TIME_PUSH, time).commit();
                initSpinerTimePush(false);
            }
        });

        if (isShow)
            dialog.show();

    }

    ArrayList<Integer> array = new ArrayList<Integer>();

    public void showWeekShowDay(boolean isShow) {

        AlertDialog dialog = new Builder(MainActivity.getInstance())
                .create();

        ListView list = new ListView(MainActivity.getInstance());
        String m = MainActivity.getInstance().language.MINUT;
        String h = MainActivity.getInstance().language.HOUR;
        String hs = MainActivity.getInstance().language.HOURS;
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);
        array.add(6);
        array.add(7);
        array.add(8);
        array.add(9);
        array.add(10);
        array.add(11);
        array.add(12);
        array.add(13);
        array.add(14);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                MainActivity.getInstance(),
                android.R.layout.simple_list_item_multiple_choice, array);
        list.setAdapter(adapter);
        dialog.setView(list);

        int pos = MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_WEEK_SHOW, 7) - 1;

        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(pos, true);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                MainActivity.getInstance().shared
                        .edit()
                        .putInt(MainActivity.SHARED_WEEK_SHOW,
                                array.get(position)).commit();
            }
        });

        dialog.setButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                showWeekShowDay(false);
            }
        });

        week_show_day.setText(String.valueOf(pos + 1));
        if (isShow)
            dialog.show();

    }
}
