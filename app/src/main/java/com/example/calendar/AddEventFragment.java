package com.example.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import filechooser.Constants;
import filechooser.FileChooserActivity;

//import com.ipaulpro.afilechooser.utils.FileUtils;

public class AddEventFragment extends android.support.v4.app.Fragment {

    ViewGroup root;
    int update_id;
    ArrayList<OneFriend> arrayFriends = new ArrayList<OneFriend>();

    public static final int ADD_EVENT_CHANGE_FILE = 1234;
    public static final int ADD_FRIEND = 2345;
    public static final int ADD_EVENT_CHANGE_SOUND = 3456;
    static AddEventFragment addEventFragment;
    Button open_start_date_dialog;
    Button open_end_date_dialog;
    int spin_city_key = 0;
    int spin_country_key = 0;
    Button add_file;
    Button paste_template;
    Button add_event_psuh_date;
    TextView add_event_psuh_date_label;
    User friend;
    Spinner repeat;
    TextView add_sound_label;
    Button add_sound;

    CheckBox pn_check;
    CheckBox vt_check;
    CheckBox sr_check;
    CheckBox ch_check;
    CheckBox pt_check;
    CheckBox sb_check;
    Button add_gr_friend;
    CheckBox vs_check;
    Spinner choose_calendar;

    boolean isGroup = false;
    boolean isUpdate = false;

    TextView add_friend_label;
    Button add_friend;

    String start_date = MainActivity.getNowDate().substring(0, 10);
    String start_time = MainActivity.getNowDate().substring(11, 16);
    String end_date = MainActivity
            .getInstance()
            .MilisecToDate(
                    MainActivity.getInstance().StringToMilisec(
                            MainActivity.getNowDate()) + 3600000)
            .substring(0, 10);
    String end_time = MainActivity
            .getInstance()
            .MilisecToDate(
                    MainActivity.getInstance().StringToMilisec(
                            MainActivity.getNowDate()) + 3600000)
            .substring(11, 16);
    String file_path = "";
    Boolean isStartDate = false;

    Button add_event_change_color;
    int color = Color.RED;
    Spinner spinner_country;
    Spinner spinner_city;
    ArrayAdapter<String> adapter_change_country;
    ArrayAdapter<String> adapter_change_city;
    ArrayAdapter<String> adapter_repeat;
    String friends = "";
    String administrators = "";
    Button add_event_save;
    EditText edit_title;
    EditText edit_info;
    boolean push_time = false;
    String sound = "0";
    String push_date;
    String[] repeats;
    String push_date_time;
    int year, mounth, day;
    String location = MainActivity.getInstance().shared.getInt(
            MainActivity.SHARED_CITY, 33345) + "";
    Button add_event_cancel;
    Spinner spiner_categories;
    String category;
    RelativeLayout add_event_color_result;
    ArrayList<Integer> itemsCategory = new ArrayList<Integer>();
    AdapterCategoryPicker adapter = new AdapterCategoryPicker(
            MainActivity.getInstance(), R.layout.category_pivker_item,
            itemsCategory);
    ArrayList<String> cuntries = new ArrayList<String>();
    ArrayList<String> cityes = new ArrayList<String>();
    Bundle bundle;
    LinearLayout lin_gr_people;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        bundle = savedInstanceState;
        String string_day = String.valueOf(MainActivity.getInstance()
                .getNowDay());
        if (string_day.length() == 1)
            string_day = "0" + MainActivity.getInstance().getNowDay();

        String string_mounth = String.valueOf(MainActivity.getInstance()
                .getNowMounth());
        if (string_mounth.length() == 1)
            string_mounth = "0" + MainActivity.getInstance().getNowMounth();
        repeats = MainActivity.getInstance().language.ADD_EVENT_REPEATS;
        start_date = MainActivity.getInstance().getNowYear() + "-"
                + string_mounth + "-" + string_day;

        end_date = MainActivity.getInstance().getNowYear() + "-"
                + string_mounth + "-" + string_day;

        year = getArguments().getInt(MainActivity.ADD_EVENT_YEAR);
        mounth = getArguments().getInt(MainActivity.ADD_EVENT_MOUNTH);
        day = getArguments().getInt(MainActivity.ADD_EVENT_DAY);
        int hour = getArguments().getInt(
                MainActivity.getInstance().ADD_EVENT_HOUR);
        int minute = getArguments().getInt(
                MainActivity.getInstance().ADD_EVENT_MINUTE);

        isGroup = getArguments().getBoolean(MainActivity.ADD_EVENT_ISGROUP,
                false);

        isUpdate = getArguments().getBoolean(MainActivity.ADD_EVENT_ISUPDATE,
                false);

        if (year > 0 && mounth > 0 && day > 0) {

            start_date = "";

            start_date = String.valueOf(year) + "-";
            if (mounth < 10)
                start_date += "0" + mounth;
            else
                start_date += "" + mounth;

            if (day < 10)
                start_date += "-0" + day;
            else
                start_date += "-" + day;

            end_date = start_date;

        }

        // start_time = "12:00";
        // end_time = "14:00";
        //
        // if (hour > 0 && minute > 0) {
        // start_time = "";
        //
        // if (hour < 10)
        // start_time = "0" + hour;
        // else
        // start_time = "" + hour;
        //
        // if (minute < 10)
        // start_time += ":0" + minute;
        // else
        // start_time += ":" + minute;
        //
        // hour++;
        //
        // if (hour < 10)
        // end_time = "0" + hour;
        // else
        // end_time = "" + hour;
        //
        // if (minute < 10)
        // end_time += ":0" + minute;
        // else
        // end_time += ":" + minute;
        //
        // }

        root = (ViewGroup) inflater.inflate(R.layout.add_event_fragment, null);
        addEventFragment = this;
        open_start_date_dialog = (Button) root
                .findViewById(R.id.add_event_start_date);
        lin_gr_people = (LinearLayout) root.findViewById(R.id.group_peoples);
        add_event_psuh_date = (Button) root
                .findViewById(R.id.add_event_psuh_date);
        repeat = (Spinner) root.findViewById(R.id.add_event_repeat);
        add_event_psuh_date_label = (TextView) root
                .findViewById(R.id.add_event_push_date_label);

        add_sound_label = (TextView) root
                .findViewById(R.id.add_event_sound_title);
        add_sound = (Button) root.findViewById(R.id.add_event_sound);

        pn_check = (CheckBox) root.findViewById(R.id.add_event_repeat_pn);
        vt_check = (CheckBox) root.findViewById(R.id.add_event_repeat_vt);
        sr_check = (CheckBox) root.findViewById(R.id.add_event_repeat_sr);
        ch_check = (CheckBox) root.findViewById(R.id.add_event_repeat_ch);
        pt_check = (CheckBox) root.findViewById(R.id.add_event_repeat_pt);
        sb_check = (CheckBox) root.findViewById(R.id.add_event_repeat_sb);
        vs_check = (CheckBox) root.findViewById(R.id.add_event_repeat_vs);

        choose_calendar = (Spinner) root
                .findViewById(R.id.add_event_chose_calendar);

        setSpinerShooseCalendar(choose_calendar);

        add_gr_friend = (Button) root
                .findViewById(R.id.add_event_add_group_friend);
        open_end_date_dialog = (Button) root
                .findViewById(R.id.add_event_end_date);
        paste_template = (Button) root
                .findViewById(R.id.add_event_add_template);
        add_event_change_color = (Button) root
                .findViewById(R.id.add_event_change_color);
        add_event_save = (Button) root.findViewById(R.id.add_event_save);
        edit_title = (EditText) root
                .findViewById(R.id.add_event_edit_text_tittle);
        edit_title.setText("");

        add_event_cancel = (Button) root.findViewById(R.id.add_event_cancel);
        add_friend = (Button) root.findViewById(R.id.add_event_add_friend);

        add_friend_label = (TextView) root
                .findViewById(R.id.add_event_add_friend_label);

        edit_info = (EditText) root.findViewById(R.id.add_event_info);
        add_file = (Button) root.findViewById(R.id.add_event_add_file);
        spiner_categories = (Spinner) root
                .findViewById(R.id.add_event_category);
        add_event_color_result = (RelativeLayout) root
                .findViewById(R.id.add_event_color_result);

        spinner_country = (Spinner) root
                .findViewById(R.id.add_event_change_country);
        spinner_city = (Spinner) root.findViewById(R.id.add_event_change_city);

        adapter_change_country = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_spinner_item, cuntries);
        adapter_change_country
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter_change_country);
//        getCuntries();

        add_event_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().onBackPressed();

            }
        });

        add_gr_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(MainActivity.getInstance(),
                        SearchPeopleCalendar.class);
                startActivityForResult(intent, 1884);

            }
        });
        add_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                Intent i = new Intent(MainActivity.getInstance(),
                        AddFriendActivity.class);
                startActivityForResult(i, ADD_FRIEND);

            }
        });

        spinner_country.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                spin_country_key++;
                if (spin_country_key > 1) {
                    getCityes(cuntries.get(position));
                    Collections.sort(cityes);
                    adapter_change_city.notifyDataSetChanged();

                }

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

                spin_city_key++;
                if (spin_city_key > 1) {

                    if (position < cityes.size()) {
                        location = MainActivity.getInstance().getIdCityByName(
                                MainActivity.getInstance().CITIES,
                                cityes.get(position))
                                + "";
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        itemsCategory.clear();
        for (int i = 1; i <= 5; i++) {
            itemsCategory.add(i);
        }
        spiner_categories.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spiner_categories.setSelection(0);

        spiner_categories
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub

                        category = String.valueOf(position + 1) + "";
                        color = new Catigories(position + 1, null).getColor();

                        MainActivity.getInstance().setRelativeColor(
                                add_event_color_result, color);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

        adapter_repeat = new ArrayAdapter<String>(MainActivity.getInstance(),
                android.R.layout.simple_spinner_item, repeats);

        adapter_repeat
                .setDropDownViewResource(android.R.layout.simple_spinner_item);
        repeat.setAdapter(adapter_repeat);
        repeat.setPrompt(MainActivity.getInstance().language.ADD_EVENT_REPEAT_PROMT);
        repeat.setSelection(0);

        repeat.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if (position == 6) {
                    root.findViewById(R.id.add_event_repeat_days)
                            .setVisibility(View.VISIBLE);

                    MyDate md = new MyDate(start_date);

                    Calendar c = Calendar.getInstance();
                    c.set(md.getYear(), md.getMounth() - 1, md.getDay() - 1);

                    switch (c.get(Calendar.DAY_OF_WEEK)) {
                        case 1:
                            pn_check.setChecked(true);
                            break;
                        case 2:
                            vt_check.setChecked(true);
                            break;
                        case 3:
                            sr_check.setChecked(true);
                            break;
                        case 4:
                            ch_check.setChecked(true);
                            break;
                        case 5:
                            pt_check.setChecked(true);
                            break;
                        case 6:
                            sb_check.setChecked(true);
                            break;
                        case 7:
                            vs_check.setChecked(true);
                            break;

                    }

                } else
                    root.findViewById(R.id.add_event_repeat_days)
                            .setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        open_start_date_dialog.setText(start_date + " " + start_time);
        open_end_date_dialog.setText(end_date + " " + end_time);

        open_start_date_dialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                isStartDate = true;
                push_time = false;
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        open_end_date_dialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                isStartDate = false;
                push_time = false;

                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });
        add_event_change_color.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.getInstance(),
                        Color_picker.class);
                intent.putExtra(Color_picker.ASSIGNING_COLOR,
                        Color_picker.ADD_EVENT);
                startActivity(intent);
            }
        });

        paste_template.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new PlaySoundButton();
                MainActivity.getInstance().ShowTempleate(edit_title);

            }
        });

        add_event_psuh_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                push_time = true;

                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        add_event_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                String title = edit_title.getText().toString();
                String start_date = open_start_date_dialog.getText().toString();
                String end_date = open_end_date_dialog.getText().toString();
                if (title.length() != 0 && start_date.length() != 0
                        && end_date.length() != 0) {

                    push_date_time = (String) add_event_psuh_date.getText();
                    AddEventToDBbyRepeat(title, color, start_date, end_date,
                            category, location, edit_info.getText().toString(),
                            file_path, push_date_time, sound,
                            repeat.getSelectedItemPosition(), 0, arrayFriends);
                    title = "";
                    edit_info.setText("");
                    edit_title.setText("");
                    Toast.makeText(MainActivity.getInstance(),
                            MainActivity.getInstance().language.EVENT_ADD,
                            Toast.LENGTH_LONG).show();

                    // MainActivity.getInstance().AddFriendEvent(arrayFriends);

                    MyDate md = new MyDate(start_date);
                    MainActivity.getInstance().startMounthFragment(
                            md.getYear(), md.getMounth(), md.getDay());

                } else {
                    Toast.makeText(MainActivity.getInstance(),
                            MainActivity.getInstance().language.EVENT_NOT_ADD,
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        add_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                startGallery(ADD_EVENT_CHANGE_FILE);

            }
        });

        add_sound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                startGallery(ADD_EVENT_CHANGE_SOUND);

            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                spinner_country.setSelection(getCuntryPosition(MainActivity
                        .getInstance().shared.getInt(MainActivity.SHARED_CITY,
                                33345)));
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        spinner_city.setSelection(getCityPosition(MainActivity
                                .getInstance().shared.getInt(
                                        MainActivity.SHARED_CITY, 33345)));

                    }
                }, 50);

            }
        }, 50);

        SetColor();
        setText();
        setVisibilityComponents();
        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.add_event_fragment_open_left_menu),
                        (Spinner) root.findViewById(R.id.add_event_spinner));

        if (isUpdate) {
            setUpdateData();
        }

        return root;

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    static public AddEventFragment getInstance() {
        return addEventFragment;
    }

    public void SetColor() {

        MyColor color = new MyColor();

        root.findViewById(R.id.add_event_fragment_top).setBackgroundColor(
                color.getColorComponents());

        root.findViewById(R.id.add_event_background).setBackgroundColor(
                color.getColorBacground());

        ((TextView) root.findViewById(R.id.add_event_start_date_label))
                .setTextColor(color.getColorFont());
        ((TextView) root.findViewById(R.id.add_event_end_date_label))
                .setTextColor(color.getColorFont());

        MainActivity.setButton(add_sound);

        add_sound_label.setTextColor(color.getColorFont());

        MainActivity.setButton(open_start_date_dialog);

        MainActivity.setButton(open_end_date_dialog);

        MainActivity.setButton(add_event_change_color);

        MainActivity.setButton(add_event_save);

        MainActivity.setButton(paste_template);
        MainActivity.setButton(add_event_cancel);

        ((TextView) root.findViewById(R.id.add_event_add_file_label))
                .setTextColor(color.getColorFont());

        MainActivity.setButton(add_file);

        ((TextView) root.findViewById(R.id.add_event_location_label))
                .setTextColor(color.getColorFont());

        ((TextView) root.findViewById(R.id.add_event_category_title))
                .setTextColor(color.getColorFont());

        add_friend_label.setTextColor(color.getColorFont());

        MainActivity.setButton(add_friend);

        MainActivity.setButton(add_event_psuh_date);
        add_event_psuh_date_label.setTextColor(color.getColorFont());

        MainActivity.setButton(((Button) root
                .findViewById(R.id.add_event_add_group_friend)));

        ((TextView) root.findViewById(R.id.add_event_repeat_title))
                .setTextColor(color.getColorFont());
    }

    public void setText() {
        Languages l = MainActivity.getInstance().language;
        ((TextView) root.findViewById(R.id.add_event_add_file_label))
                .setText(l.ADD_EVENT_ADD_FILE);

        add_event_cancel.setText(l.ADD_EVENT_CANCEL);

        add_event_save.setText(l.ADD_EVENT_SAVE);
        add_event_psuh_date_label.setText(l.PUSH_TIME);
        int push_shared = MainActivity.getInstance().shared.getInt(
                MainActivity.SHARED_TIME_PUSH, 15 * 60000);
        String text = ""
                + MilisecToDate(String
                .valueOf(StringToMilisec(open_start_date_dialog
                        .getText().toString()) - push_shared));
        add_event_psuh_date.setText(text);

        add_event_change_color.setText(l.ADD_EVENT_COLOR);

        ((TextView) root.findViewById(R.id.add_event_spinner_calendar_title))
                .setText(l.SHOOSE_CALENDAR_TITTLE);

        ((TextView) root.findViewById(R.id.add_event_start_date_label))
                .setText(l.ADD_EVENT_START_DATE_LABEL);

        ((TextView) root.findViewById(R.id.add_event_end_date_label))
                .setText(l.ADD_EVENT_END_DATE_LABEL);

        ((TextView) root.findViewById(R.id.add_event_location_label))
                .setText(l.ADD_EEVENT_LOCATION_LABEL);

        ((TextView) root.findViewById(R.id.add_event_category_title))
                .setText(l.CATEGORY_TITLE);
        edit_title.setHint(l.ADD_EVENT_TITLE_HINT);
        edit_info.setHint(l.ADD_EVENT_INFO_HINT);
        paste_template
                .setText(MainActivity.getInstance().language.PASTE_TEMPLATE);
        add_friend_label
                .setText(MainActivity.getInstance().language.ADD_EVENT_ADD_FRIEND_LABEL);

        add_sound_label
                .setText(MainActivity.getInstance().language.ADD_EVENT_SOUND_TITLE);
        add_sound
                .setText(MainActivity.getInstance().language.ADD_EVENT_SOUND_TITLE_STANDART);

        String sound_shared = MainActivity.getInstance().shared.getString(
                MainActivity.SHARED_ALARM_SOUND, "");

        if (sound_shared.length() > 0) {
            add_sound.setText(getFileName(sound_shared));
            sound = sound_shared;
        }

        ((TextView) root.findViewById(R.id.add_event_repeat_title))
                .setText(MainActivity.getInstance().language.ADD_EVENT_REPEAT_TITLE);

        ((TextView) root.findViewById(R.id.add_event_people_gruop))
                .setText(MainActivity.getInstance().language.PEOPLES);

        pn_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[0]);
        vt_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[1]);
        sr_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[2]);
        ch_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[3]);
        pt_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[4]);
        sb_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[5]);
        vs_check.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR[6]);

    }

    public void setVisibilityComponents() {

        if (isGroup) {
            ((TextView) root.findViewById(R.id.add_event_repeat_title))
                    .setVisibility(View.GONE);
            repeat.setVisibility(View.GONE);
            root.findViewById(R.id.add_event_adding_friends).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_add_friend_label).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_add_friend).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_repeat_title).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_repeat_days).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_sound_title).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_sound).setVisibility(View.GONE);
            root.findViewById(R.id.add_event_add_friend_label).setVisibility(
                    View.GONE);
            root.findViewById(R.id.add_event_people_group).setVisibility(
                    View.VISIBLE);

        } else {
            ((TextView) root.findViewById(R.id.add_event_repeat_title))
                    .setVisibility(View.VISIBLE);
            repeat.setVisibility(View.VISIBLE);
            root.findViewById(R.id.add_event_adding_friends).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_add_friend_label).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_add_friend).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_repeat_title).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_repeat_days).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_sound_title).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_sound).setVisibility(View.VISIBLE);
            root.findViewById(R.id.add_event_add_friend_label).setVisibility(
                    View.VISIBLE);
            root.findViewById(R.id.add_event_people_group).setVisibility(
                    View.GONE);

        }
    }

    static public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = MainActivity.getInstance().getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    static public String getFileName(String name) {
        int n = name.lastIndexOf("/");
        return name.substring(n + 1, name.length());
    }

    public void startGallery(int key) {

        // Intent target = FileUtils.createGetContentIntent();
        // // Create the chooser Intent
        // Intent intent = Intent.createChooser(target, "Select a file");
        // try {
        // startActivityForResult(intent, key);
        // } catch (ActivityNotFoundException e) {
        // }

        startActivityForResult(new Intent(MainActivity.getInstance(),
                FileChooserActivity.class), key);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1884) {
            try {

                lin_gr_people.setVisibility(View.VISIBLE);

                TextView text = new TextView(MainActivity.getInstance());
                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 2, 0, 2);
                text.setTextSize(14);
                text.setTextColor(new MyColor().getColorFont());
                text.setLayoutParams(params);
                text.setText(friend.getName() + " " + friend.getSurname());

                lin_gr_people.addView(text);

                friends += friend.getId() + ",";
                administrators += friend.getId() + ",";
            } catch (Exception e) {
                // TODO: handle exception
                Log.e(getClass().toString()
                        + "line = "
                        + Thread.currentThread().getStackTrace()[2]
                        .getLineNumber(), "catch:" + e);
            }
        }

        if (requestCode == ADD_EVENT_CHANGE_FILE) {
            try {
                // file_path = getRealPathFromURI(data.getData());
                file_path = data.getStringExtra(Constants.KEY_FILE_SELECTED);
                add_file.setText(getFileName(file_path));

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        if (requestCode == ADD_EVENT_CHANGE_SOUND) {
            try {
                // sound = getRealPathFromURI(data.getData());
                sound = data.getStringExtra(Constants.KEY_FILE_SELECTED);
                add_sound.setText(getFileName(sound));

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        if (requestCode == ADD_FRIEND) {
            addFriendLabel();
        }

    }

    public void getCuntries() {
        cuntries.clear();
        ArrayList<OneCity> AllCuntries = MainActivity.getInstance()
                .getTheCountry(MainActivity.getInstance().CITIES);

        for (int i = 0; i < AllCuntries.size(); i++) {
            cuntries.add(AllCuntries.get(i).getCountry().toString());
        }
        adapter_change_country.notifyDataSetChanged();

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
            if (cuntries.get(i).equals(name)) {
                return i;
            }
        }
        return res;
    }

    public int getCityPosition(int id) {
        Log.e(getClass().toString(), "getCityPosition");
        String name = MainActivity.getInstance().getCityById(
                MainActivity.getInstance().CITIES, id);
        int res = 0;
        for (int i = 0; i < cityes.size(); i++) {
            if (cityes.get(i).equals(name)) {

                Log.e(getClass().toString(), "getCityPosition i = " + i);
                return i;
            }
        }
        return res;
    }

    ArrayList<Integer> delFriend = new ArrayList<Integer>();
    ArrayList<OneFriend> promFriends = new ArrayList<OneFriend>();

    public void addFriendLabel() {

        Log.e(getClass().toString(), "add label arrayFriends.size() = "
                + arrayFriends.size());

        final LinearLayout root_adding_friends = (LinearLayout) root
                .findViewById(R.id.add_event_adding_friends);

        if (arrayFriends.size() > 0) {
            root_adding_friends.setVisibility(View.VISIBLE);
            add_friend_label
                    .setText(MainActivity.getInstance().language.ADD_EVENT_ADD_FRIEND_LABEL_2);

            add_friend
                    .setText(MainActivity.getInstance().language.ADD_EVENT_ADD_FRIEND);

            for (int i = 0; i < arrayFriends.size(); i++) {
                TextView text = new TextView(MainActivity.getInstance());
                text.setTextColor(new MyColor().getColorFont());
                LayoutParams params = new LayoutParams(
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT);
                text.setText(arrayFriends.get(i).getFirst_name() + " "
                        + arrayFriends.get(i).getLast_name());
                final RelativeLayout rel = new RelativeLayout(
                        MainActivity.getInstance());
                rel.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                params.setMargins(20, 10, 10, 10);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                text.setLayoutParams(params);
                params = new LayoutParams(
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT);
                TextView del = new TextView(MainActivity.getInstance());
                del.setText("X");
                del.setTypeface(Typeface.DEFAULT_BOLD);
                del.setTextColor(Color.RED);

                del.setTextSize(16);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.setMargins(0, 10, 10, 0);
                del.setLayoutParams(params);

                rel.setId(i);

                rel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        root_adding_friends.removeView(rel);
                        for (int j = 0; j < arrayFriends.size(); j++) {

                            if (j != rel.getId()) {
                                promFriends.add(arrayFriends.get(j));
                            }

                        }

                        arrayFriends = new ArrayList<OneFriend>(promFriends);
                    }
                });

                rel.addView(del);
                rel.addView(text);

                root_adding_friends.addView(rel);
            }

        } else {
            root_adding_friends.setVisibility(View.GONE);
            add_friend.setText("...");
        }

    }

    public void AddEventToDBbyRepeat(final String title, final int color,
                                     final String start_date, final String end_date,
                                     final String category, final String location, final String info,
                                     final String file_path, final String push_time, final String sound,
                                     final int repeat_type, final int isDone,
                                     final ArrayList<OneFriend> arrayOneFriend) {

        if (isUpdate)
            MainActivity.getInstance().DeleteEventByID(update_id);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                String start_date_milisec = String
                        .valueOf(StringToMilisec(start_date));
                String end_date_milisec = String
                        .valueOf(StringToMilisec(end_date));
                String push_date_milisec = String
                        .valueOf(StringToMilisec(push_time));

                // try {
                //
                // if (friends.lastIndexOf(",") == friends.length() - 1)
                // friends = friends.substring(0, friends.length() - 1);
                //
                // if (administrators.lastIndexOf(",") == administrators
                // .length() - 1)
                // administrators = administrators.substring(0,
                // administrators.length() - 1);
                //
                // } catch (Exception e) {
                // // TODO: handle exception
                // Log.e(getClass().toString()
                // + "line = "
                // + Thread.currentThread().getStackTrace()[2]
                // .getLineNumber(), "catch:" + e);
                // }

                switch (repeat_type) {
                    case 0:

                        if (!isGroup) {
                            MainActivity.getInstance().AddEventToDB(
                                    new CalendarService(MainActivity.getInstance())
                                            .getIdCalendarByName(choose_calendar
                                                    .getSelectedItem().toString()),
                                    title, color, start_date, end_date, category,
                                    location, info, file_path, push_time, sound,
                                    isDone);

                            MainActivity.getInstance().AddFriendEvent(
                                    arrayOneFriend);

                            MainActivity.getInstance().Synchronize();
                        } else {
//						final MyDate sd = new MyDate(start_date);
//						final MyDate ed = new MyDate(end_date);
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//
//								MainActivity
//										.getInstance()
//										.sendEventToServer(
//												MainActivity.getInstance().USER
//														.getId(), title, color,
//												sd.getYear(), sd.getMounth(),
//												sd.getDay(), sd.getHour(),
//												sd.getMinute(), ed.getYear(),
//												ed.getMounth(), ed.getDay(),
//												ed.getHour(), ed.getMinute(),
//												push_time, category, location,
//												info, 100, file_path, sound,
//												isDone, friends, administrators);
//
//							}
//						}).start();

                        }
                        break;

                    case 1:

                        for (int i = 0; i < 365; i++) {

                            if (!isGroup) {
                                MainActivity
                                        .getInstance()
                                        .AddEventToDB(
                                                new CalendarService(MainActivity
                                                        .getInstance())
                                                        .getIdCalendarByName(choose_calendar
                                                                .getSelectedItem()
                                                                .toString()),
                                                title, color,
                                                MilisecToDate(start_date_milisec),
                                                MilisecToDate(end_date_milisec),
                                                category, location, info,
                                                file_path,
                                                MilisecToDate(push_date_milisec),
                                                sound, 0);

                                MainActivity.getInstance().AddFriendEvent(
                                        arrayOneFriend);

                            } else {
//							final MyDate sd = new MyDate(start_date);
//							final MyDate ed = new MyDate(end_date);
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//
//									MainActivity
//											.getInstance()
//											.sendEventToServer(
//													MainActivity.getInstance().USER
//															.getId(), title,
//													color, sd.getYear(),
//													sd.getMounth(),
//													sd.getDay(), sd.getHour(),
//													sd.getMinute(),
//													ed.getYear(),
//													ed.getMounth(),
//													ed.getDay(), ed.getHour(),
//													ed.getMinute(), push_time,
//													category, location, info,
//													100, file_path, sound,
//													isDone, friends,
//													administrators);
//
//								}
//							}).start();

                            }

                            start_date_milisec = String.valueOf(Long
                                    .parseLong(start_date_milisec) + 86400000);
                            end_date_milisec = String.valueOf(Long
                                    .parseLong(end_date_milisec) + 86400000);
                            push_date_milisec = String.valueOf(Long
                                    .parseLong(push_date_milisec) + 86400000);

                        }
                        MainActivity.getInstance().Synchronize();
                        break;

                    case 2:

                        for (int i = 0; i < 365; i++) {

                            if (!isGroup) {
                                MainActivity
                                        .getInstance()
                                        .AddEventToDB(
                                                new CalendarService(MainActivity
                                                        .getInstance())
                                                        .getIdCalendarByName(choose_calendar
                                                                .getSelectedItem()
                                                                .toString()),
                                                title, color,
                                                MilisecToDate(start_date_milisec),
                                                MilisecToDate(end_date_milisec),
                                                category, location, info,
                                                file_path,
                                                MilisecToDate(push_date_milisec),
                                                sound, isDone);

                                MainActivity.getInstance().AddFriendEvent(
                                        arrayOneFriend);
                            } else {
//							final MyDate sd = new MyDate(start_date);
//							final MyDate ed = new MyDate(end_date);
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//
//									MainActivity
//											.getInstance()
//											.sendEventToServer(
//													MainActivity.getInstance().USER
//															.getId(), title,
//													color, sd.getYear(),
//													sd.getMounth(),
//													sd.getDay(), sd.getHour(),
//													sd.getMinute(),
//													ed.getYear(),
//													ed.getMounth(),
//													ed.getDay(), ed.getHour(),
//													ed.getMinute(), push_time,
//													category, location, info,
//													100, file_path, sound,
//													isDone, friends,
//													administrators);
//
//								}
//							}).start();

                            }

                            start_date_milisec = String.valueOf(Long
                                    .parseLong(start_date_milisec) + 2 * 86400000);
                            end_date_milisec = String.valueOf(Long
                                    .parseLong(end_date_milisec) + 2 * 86400000);
                            push_date_milisec = String.valueOf(Long
                                    .parseLong(push_date_milisec) + 2 * 86400000);
                        }
                        MainActivity.getInstance().Synchronize();
                        break;

                    case 3:

                        for (int i = 0; i < 365; i++) {

                            if (!isGroup) {
                                MainActivity
                                        .getInstance()
                                        .AddEventToDB(
                                                new CalendarService(MainActivity
                                                        .getInstance())
                                                        .getIdCalendarByName(choose_calendar
                                                                .getSelectedItem()
                                                                .toString()),
                                                title, color,
                                                MilisecToDate(start_date_milisec),
                                                MilisecToDate(end_date_milisec),
                                                category, location, info,
                                                file_path,
                                                MilisecToDate(push_date_milisec),
                                                sound, isDone);

                                MainActivity.getInstance().AddFriendEvent(
                                        arrayOneFriend);
                            } else {
                                //final MyDate sd = new MyDate(start_date);
//							final MyDate ed = new MyDate(end_date);
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//
//									MainActivity
//											.getInstance()
//											.sendEventToServer(
//													MainActivity.getInstance().USER
//															.getId(), title,
//													color, sd.getYear(),
//													sd.getMounth(),
//													sd.getDay(), sd.getHour(),
//													sd.getMinute(),
//													ed.getYear(),
//													ed.getMounth(),
//													ed.getDay(), ed.getHour(),
//													ed.getMinute(), push_time,
//													category, location, info,
//													100, file_path, sound,
//													isDone, friends,
//													administrators);
//
//								}
//							}).start();
                            }

                            start_date_milisec = String.valueOf(Long
                                    .parseLong(start_date_milisec) + 7 * 86400000);
                            end_date_milisec = String.valueOf(Long
                                    .parseLong(end_date_milisec) + 7 * 86400000);
                            push_date_milisec = String.valueOf(Long
                                    .parseLong(push_date_milisec) + 7 * 86400000);
                        }
                        MainActivity.getInstance().Synchronize();
                        break;

                    case 4:
                        String s = start_date;
                        String e = end_date;
                        String p = push_time;

                        for (int i = 0; i < 12; i++) {

                            if (!isGroup) {
                                MainActivity
                                        .getInstance()
                                        .AddEventToDB(
                                                new CalendarService(MainActivity
                                                        .getInstance())
                                                        .getIdCalendarByName(choose_calendar
                                                                .getSelectedItem()
                                                                .toString()),
                                                title, color, s, e, category,
                                                location, info, file_path, p,
                                                sound, isDone);

                                MainActivity.getInstance().AddFriendEvent(
                                        arrayOneFriend);
                            } else {
//							final MyDate sd = new MyDate(start_date);
//							final MyDate ed = new MyDate(end_date);
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//
//									MainActivity
//											.getInstance()
//											.sendEventToServer(
//													MainActivity.getInstance().USER
//															.getId(), title,
//													color, sd.getYear(),
//													sd.getMounth(),
//													sd.getDay(), sd.getHour(),
//													sd.getMinute(),
//													ed.getYear(),
//													ed.getMounth(),
//													ed.getDay(), ed.getHour(),
//													ed.getMinute(), push_time,
//													category, location, info,
//													100, file_path, sound,
//													isDone, friends,
//													administrators);
//
//								}
//							}).start();
                            }

                            MyDate ms = new MyDate(s);
                            MyDate me = new MyDate(e);
                            MyDate mp = new MyDate(p);

                            int ms_m = ms.getMounth();
                            int ms_y = ms.getYear();
                            if (ms_m == 12) {
                                ms_m = 1;
                                ms_y++;
                            } else
                                ms_m++;

                            String s_y;
                            String s_m;
                            String s_d;
                            String s_h;
                            String s_min;

                            s_y = "" + ms_y;
                            if (ms_m < 10)
                                s_m = "0" + ms_m;
                            else
                                s_m = "" + ms_m;

                            if (ms.getDay() < 10)
                                s_d = "0" + ms.getDay();
                            else
                                s_d = "" + ms.getDay();

                            if (ms.getHour() < 10)
                                s_h = "0" + ms.getHour();
                            else
                                s_h = "" + ms.getHour();

                            if (ms.getMinute() < 10)
                                s_min = "0" + ms.getMinute();
                            else
                                s_min = "" + ms.getMinute();

                            s = s_y + "-" + s_m + "-" + s_d + " " + s_h + ":"
                                    + s_min;

                            int me_m = me.getMounth();
                            int me_y = me.getYear();
                            if (me_m == 12) {
                                me_m = 1;
                                me_y++;
                            } else
                                me_m++;

                            String e_y;
                            String e_m;
                            String e_d;
                            String e_h;
                            String e_min;

                            e_y = "" + me_y;
                            if (me_m < 10)
                                e_m = "0" + me_m;
                            else
                                e_m = "" + me_m;

                            if (me.getDay() < 10)
                                e_d = "0" + me.getDay();
                            else
                                e_d = "" + me.getDay();

                            if (me.getHour() < 10)
                                e_h = "0" + me.getHour();
                            else
                                e_h = "" + me.getHour();

                            if (me.getMinute() < 10)
                                e_min = "0" + me.getMinute();
                            else
                                e_min = "" + me.getMinute();

                            e = e_y + "-" + e_m + "-" + e_d + " " + e_h + ":"
                                    + e_min;

                            int mp_m = mp.getMounth();
                            int mp_y = mp.getYear();
                            if (mp_m == 12) {
                                mp_m = 1;
                                mp_y++;
                            } else
                                mp_m++;

                            String p_y;
                            String p_m;
                            String p_d;
                            String p_h;
                            String p_min;

                            p_y = "" + mp_y;
                            if (me_m < 10)
                                p_m = "0" + mp_m;
                            else
                                p_m = "" + mp_m;

                            if (mp.getDay() < 10)
                                p_d = "0" + mp.getDay();
                            else
                                p_d = "" + mp.getDay();

                            if (mp.getHour() < 10)
                                p_h = "0" + mp.getHour();
                            else
                                p_h = "" + mp.getHour();

                            if (mp.getMinute() < 10)
                                p_min = "0" + mp.getMinute();
                            else
                                p_min = "" + mp.getMinute();

                            p = p_y + "-" + p_m + "-" + p_d + " " + p_h + ":"
                                    + p_min;

                        }
                        MainActivity.getInstance().Synchronize();
                        break;

                    case 5:

                        s = start_date;
                        e = end_date;
                        p = push_time;

                        for (int i = 0; i < 1; i++) {
                            if (!isGroup) {
                                MainActivity
                                        .getInstance()
                                        .AddEventToDB(
                                                new CalendarService(MainActivity
                                                        .getInstance())
                                                        .getIdCalendarByName(choose_calendar
                                                                .getSelectedItem()
                                                                .toString()),
                                                title, color, s, e, category,
                                                location, info, file_path, p,
                                                sound, isDone);

                                MainActivity.getInstance().AddFriendEvent(
                                        arrayOneFriend);
                            } else {
//							final MyDate sd = new MyDate(start_date);
//							final MyDate ed = new MyDate(end_date);
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//
//									MainActivity
//											.getInstance()
//											.sendEventToServer(
//													MainActivity.getInstance().USER
//															.getId(), title,
//													color, sd.getYear(),
//													sd.getMounth(),
//													sd.getDay(), sd.getHour(),
//													sd.getMinute(),
//													ed.getYear(),
//													ed.getMounth(),
//													ed.getDay(), ed.getHour(),
//													ed.getMinute(), push_time,
//													category, location, info,
//													100, file_path, sound,
//													isDone, friends,
//													administrators);
//
//								}
//							}).start();
                            }
                            MyDate ms = new MyDate(s);
                            MyDate me = new MyDate(e);
                            MyDate mp = new MyDate(p);

                            int sy = ms.getYear() + 1;
                            int ey = me.getYear() + 1;
                            int py = mp.getYear() + 1;

                            s = s.replace("" + ms.getYear(), "" + sy);

                            e = e.replace("" + me.getYear(), "" + ey);

                            p = p.replace("" + mp.getYear(), "" + py);

                        }

                        break;

                    case 6:

                        s = start_date;
                        e = end_date;
                        p = push_time;
                        ArrayList<Integer> days = new ArrayList<Integer>();
                        if (pn_check.isChecked())
                            days.add(1);
                        if (vt_check.isChecked())
                            days.add(2);
                        if (sr_check.isChecked())
                            days.add(3);
                        if (ch_check.isChecked())
                            days.add(4);
                        if (pt_check.isChecked())
                            days.add(5);
                        if (sb_check.isChecked())
                            days.add(6);
                        if (vs_check.isChecked())
                            days.add(7);

                        for (int i = 0; i < days.size(); i++) {

                            String s_mil = String.valueOf(StringToMilisec(s));
                            String e_mil = String.valueOf(StringToMilisec(e));
                            String p_mil = String.valueOf(StringToMilisec(p));

                            MyDate ms = new MyDate(s);
                            Calendar c = Calendar.getInstance();
                            c.set(ms.getYear(), ms.getMounth() - 1, ms.getDay() - 1);
                            while (c.get(Calendar.DAY_OF_WEEK) != days.get(i)) {
                                s_mil = String.valueOf(Long.parseLong(s_mil) + 86400000);
                                e_mil = String.valueOf(Long.parseLong(e_mil) + 86400000);
                                p_mil = String.valueOf(Long.parseLong(p_mil) + 86400000);

                                String prom = MilisecToDate(s_mil);
                                ms = new MyDate(prom);
                                c.set(ms.getYear(), ms.getMounth() - 1,
                                        ms.getDay() - 1);

                            }
                            for (int j = 0; j < 53; j++) {
                                if (!isGroup) {
                                    MainActivity
                                            .getInstance()
                                            .AddEventToDB(
                                                    new CalendarService(
                                                            MainActivity
                                                                    .getInstance())
                                                            .getIdCalendarByName(choose_calendar
                                                                    .getSelectedItem()
                                                                    .toString()),
                                                    title, color,
                                                    MilisecToDate(s_mil),
                                                    MilisecToDate(e_mil), category,
                                                    location, info, file_path,
                                                    MilisecToDate(p_mil), sound,
                                                    isDone);

                                    MainActivity.getInstance().AddFriendEvent(
                                            arrayOneFriend);
                                } else {
                                    // final MyDate sd = new MyDate(start_date);
                                    // final MyDate ed = new MyDate(end_date);
                                    // new Thread(new Runnable() {
                                    // @Override
                                    // public void run() {
                                    // // TODO Auto-generated method stub
                                    //
                                    // MainActivity
                                    // .getInstance()
                                    // .sendEventToServer(
                                    // MainActivity
                                    // .getInstance().USER
                                    // .getId(),
                                    // title, color,
                                    // sd.getYear(),
                                    // sd.getMounth(),
                                    // sd.getDay(),
                                    // sd.getHour(),
                                    // sd.getMinute(),
                                    // ed.getYear(),
                                    // ed.getMounth(),
                                    // ed.getDay(),
                                    // ed.getHour(),
                                    // ed.getMinute(),
                                    // push_time, category,
                                    // location, info, 100,
                                    // file_path, sound,
                                    // isDone, friends,
                                    // administrators);
                                    //
                                    // }
                                    // }).start();

                                }

                                s_mil = String.valueOf(Long.parseLong(s_mil) + 7 * 86400000);
                                e_mil = String.valueOf(Long.parseLong(e_mil) + 7 * 86400000);
                                p_mil = String.valueOf(Long.parseLong(p_mil) + 7 * 86400000);

                            }
                        }
                        MainActivity.getInstance().Synchronize();

                        break;

                }
            }
        }).start();

    }

    public long StringToMilisec(String DateInString) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long res = 0;
        String DateInMilisec = "";
        try {
            Date date;
            try {
                date = formatDate.parse(DateInString);
                DateInMilisec = "" + date.getTime();
                res = Long.parseLong(DateInMilisec);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String MilisecToDate(String DaateInMilisec) {
        long i = Long.parseLong(DaateInMilisec);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(i);
        String s = formatDate.format(date);
        return s;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setUpdateData() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                if (MainActivity.getInstance().bufferForUpdate != null) {
                    OneEvent event = MainActivity.getInstance().bufferForUpdate;

                    edit_title.setText(event.getTitle());
                    edit_info.setText(event.getInfo());
                    String sd = MainActivity.FormatDate(event
                            .getStart_date_year())
                            + "-"
                            + MainActivity.FormatDate(event
                            .getStart_date_mounth())
                            + "-"
                            + MainActivity
                            .FormatDate(event.getStart_date_day())
                            + " "
                            + MainActivity.FormatDate(event
                            .getStart_date_hour())
                            + ":"
                            + MainActivity.FormatDate(event
                            .getStart_date_minute());
                    String ed = MainActivity.FormatDate(event
                            .getEnd_date_year())
                            + "-"
                            + MainActivity.FormatDate(event
                            .getEnd_date_mounth())
                            + "-"
                            + MainActivity.FormatDate(event.getEnd_date_day())
                            + " "
                            + MainActivity.FormatDate(event.getEnd_date_hour())
                            + ":"
                            + MainActivity.FormatDate(event
                            .getEnd_date_minute());
                    open_start_date_dialog.setText(sd);
                    open_end_date_dialog.setText(ed);
                    add_event_psuh_date.setText(event.getPush_time());
                    spiner_categories.setSelection(Integer.parseInt(event
                            .getCategory()) - 1);
                    MainActivity.getInstance().setRelativeColor(
                            add_event_color_result, event.getColor());
                    spinner_country.setSelection(getCuntryPosition(Integer
                            .parseInt(event.getLocation())));

                    if (event.getFile_path().length() > 5)
                        add_file.setText(event.getFile_path());

                    if (event.getSound().length() > 5)
                        add_sound.setText(event.getSound());
                    update_id = event.getId();

                }

            }
        }, 110);
    }

    public void setSpinerShooseCalendar(Spinner spiner) {

        ArrayList<String> namesCalendar = new CalendarService(
                MainActivity.getInstance()).getNamesCalendarContainsGMail();

        if (namesCalendar.size() == 0) {
            namesCalendar
                    .add(MainActivity.getInstance().language.TEXT_WHEN_NO_CALENDAR);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.getInstance(),
                android.R.layout.simple_list_item_1, namesCalendar);
        spiner.setAdapter(adapter);
    }
}
