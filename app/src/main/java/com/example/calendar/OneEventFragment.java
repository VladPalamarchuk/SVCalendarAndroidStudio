package com.example.calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class OneEventFragment extends android.support.v4.app.Fragment {

    static OneEventFragment oneEventFragment;

    TextView start_date_label;
    TextView start_date;
    TextView end_date_label;
    TextView end_date;
    ImageView change_event;
    // TextView location_label;
    TextView location;
    TextView info;
    TextView info_label;

    TextView title;
    TextView title_label;

    TextView prognoz_label;
    TextView prognoz;
    TextView file_label;
    TextView file;
    ViewGroup root;
    ImageView image_pogoda;

    TextView category;
    TextView category_label;
    ImageView image_category;
    OneEvent e = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        root = (ViewGroup) inflater.inflate(R.layout.one_event_fragment, null);
        oneEventFragment = this;
        int position = getArguments().getInt(
                MainActivity.ONE_EVENT_FRAGMENT_POSITION, 0);
        try {

            e = MounthFragment.getInstance().arrayOneEventAndWeaters.get(
                    position).getOneEvent();

        } catch (Exception e) {
            // TODO: handle exception

            return root;
        }

        category = (TextView) root
                .findViewById(R.id.one_event_fragment_category);
        category_label = (TextView) root
                .findViewById(R.id.one_event_fragment_category_label);
        image_category = (ImageView) root
                .findViewById(R.id.one_event_fragment_category_image);
        change_event = (ImageView) root
                .findViewById(R.id.one_event_fragment_change_event);
        start_date_label = (TextView) root
                .findViewById(R.id.one_event_fragment_start_date_label);
        start_date = (TextView) root
                .findViewById(R.id.one_event_fragment_start_date);
        end_date_label = (TextView) root
                .findViewById(R.id.one_event_fragment_end_date_label);
        end_date = (TextView) root
                .findViewById(R.id.one_event_fragment_end_date);
        // location_label = (TextView) root
        // .findViewById(R.id.one_event_fragment_location_label);
        location = (TextView) root
                .findViewById(R.id.one_event_fragment_location);
        info = (TextView) root.findViewById(R.id.one_event_fragment_info);
        info_label = (TextView) root
                .findViewById(R.id.one_event_fragment_info_label);
        image_pogoda = (ImageView) root
                .findViewById(R.id.one_event_fragment_wether_image);

        MounthFragment.getInstance().arrayOneEventAndWeaters.get(position)
                .setImage(image_pogoda);
        title = (TextView) root.findViewById(R.id.one_event_fragment_title);
        title_label = (TextView) root
                .findViewById(R.id.one_event_fragment_title_label);

        // prognoz_label = (TextView) root
        // .findViewById(R.id.one_event_fragment_weather_label);
        prognoz = (TextView) root.findViewById(R.id.one_event_fragment_weather);

        MounthFragment.getInstance().arrayOneEventAndWeaters.get(position)
                .setText(prognoz);

        // file_label = (TextView) root
        // .findViewById(R.id.one_event_fragment_file_label);
        file = (TextView) root.findViewById(R.id.one_event_fragment_file);

        file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (file.getText().length() > 0) {
                    File file = new File(e.getFile_path());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "*/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });

        change_event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                MainActivity.getInstance().UpdateEvent(e);

            }
        });
        setText();

        String start = "";
        String end = "";

        if (MainActivity.getInstance().shared.getBoolean(
                MainActivity.SHARED_IS_AMPM, false)) {

            if (e.getStart_date_hour() > 12) {
                start = e.getStart_date_year() + "-"
                        + MainActivity.FormatDate(e.getStart_date_mounth())
                        + "-" + MainActivity.FormatDate(e.getEnd_date_day())
                        + " "
                        + MainActivity.FormatDate(e.getStart_date_hour() - 12)
                        + ":"
                        + MainActivity.FormatDate(e.getStart_date_minute())
                        + " PM";
            } else {
                start = e.getStart_date_year() + "-"
                        + MainActivity.FormatDate(e.getStart_date_mounth())
                        + "-" + MainActivity.FormatDate(e.getEnd_date_day())
                        + " " + MainActivity.FormatDate(e.getStart_date_hour())
                        + ":"
                        + MainActivity.FormatDate(e.getStart_date_minute())
                        + " AM";
            }

            if (e.getEnd_date_hour() > 12) {
                end = e.getEnd_date_year() + "-"
                        + MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
                        + MainActivity.FormatDate(e.getEnd_date_day()) + " "
                        + MainActivity.FormatDate(e.getEnd_date_hour() - 12)
                        + ":" + MainActivity.FormatDate(e.getEnd_date_minute())
                        + " PM";
            } else {
                end = e.getEnd_date_year() + "-"
                        + MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
                        + MainActivity.FormatDate(e.getEnd_date_day()) + " "
                        + MainActivity.FormatDate(e.getEnd_date_hour()) + ":"
                        + MainActivity.FormatDate(e.getEnd_date_minute())
                        + " AM";
            }

        } else {

            start = e.getStart_date_year() + "-"
                    + MainActivity.FormatDate(e.getStart_date_mounth()) + "-"
                    + MainActivity.FormatDate(e.getEnd_date_day()) + " "
                    + MainActivity.FormatDate(e.getStart_date_hour()) + ":"
                    + MainActivity.FormatDate(e.getStart_date_minute());

            end = e.getEnd_date_year() + "-"
                    + MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
                    + MainActivity.FormatDate(e.getEnd_date_day()) + " "
                    + MainActivity.FormatDate(e.getEnd_date_hour()) + ":"
                    + MainActivity.FormatDate(e.getEnd_date_minute());

        }

        start_date.setText(start);
        end_date.setText(end);
        Catigories c = new Catigories(Integer.parseInt(e.getCategory()),
                image_category);
        category.setText(c.getName());
        c.setImage();
        title.setText(e.getTitle());

        location.setText(MainActivity.getInstance().getLocationById(
                MainActivity.getInstance().CITIES,
                Integer.parseInt(e.getLocation())));

        file.setText(MainActivity.getInstance().getFileName(e.getFile_path()));
        info.setText(e.getInfo());

        getFriend();

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.one_event_fragment_open_left_menu),
                        (Spinner) root
                                .findViewById(R.id.One_event_fragment_spinner));

        ((RelativeLayout) root.findViewById(R.id.one_event_fragment_color_rel))
                .setBackgroundColor(e.getColor());

        if (e.getInfo().length() > 0)
            info.setVisibility(View.VISIBLE);

        if (e.getFile_path().length() == 0) {
            root.findViewById(R.id.one_event_fragment_file_image)
                    .setVisibility(View.GONE);
        }
        setColor();
        return root;

    }

    static public OneEventFragment getInstance() {
        return oneEventFragment;
    }

    public void setText() {
        start_date_label
                .setText(MainActivity.getInstance().language.ONE_EVENT_START_DATE_LABEL);
        end_date_label
                .setText(MainActivity.getInstance().language.ONE_EVENT_END_DATE_LABEL);
        //
        // info_label
        // .setText(MainActivity.getInstance().language.ONE_EVENT_INFO_LABEL);
        // location_label
        // .setText(MainActivity.getInstance().language.ONE_EVENT_LOCATION_LABEL);
        // title_label
        // .setText(MainActivity.getInstance().language.ONE_EVENT_TITLE_LABEL);
        // prognoz_label
        // .setText(MainActivity.getInstance().language.ONE_EVENT_PROGNOZ_LABEL);
        // file_label
        // .setText(MainActivity.getInstance().language.ONE_EVENT_FILE_LABEL);
        //
        // category_label
        // .setText(MainActivity.getInstance().language.ONE_EVENT_CATEGORY_LABEL);
    }

    public void getFriend() {
        LinearLayout lin_root = (LinearLayout) root
                .findViewById(R.id.one_event_fragment_adding_friends_root);
        lin_root.setVisibility(View.GONE);
        ArrayList<OneFriend> array = MainActivity.getInstance()
                .getFriendsByIdEvent(e.getId());

        Log.e(getClass().toString(),
                "one Event get friend by id = "
                        + e.getId()
                        + "   return size = "
                        + MainActivity.getInstance()
                        .getFriendsByIdEvent(e.getId()).size());

        if (array.size() > 0) {
            lin_root.setVisibility(View.VISIBLE);
            ((TextView) root
                    .findViewById(R.id.one_event_fragment_adding_friends_title))
                    .setTextColor(new MyColor().getColorFont());
            ((TextView) root
                    .findViewById(R.id.one_event_fragment_adding_friends_title))
                    .setText(MainActivity.getInstance().language.ADD_EVENT_ADD_FRIEND_LABEL_2);

            for (int i = 0; i < array.size(); i++) {
                TextView text = new TextView(MainActivity.getInstance());
                text.setTextColor(new MyColor().getColorFont());
                LayoutParams params = new LayoutParams(
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.app.ActionBar.LayoutParams.WRAP_CONTENT);
                text.setText(array.get(i).getFirst_name() + " "
                        + array.get(i).getLast_name());

                text.setLayoutParams(params);

                ((LinearLayout) root.findViewById(R.id.one_event_friends_root2))
                        .addView(text);
            }

        }
    }

    public void setColor() {

        MyColor c = new MyColor();
        root.findViewById(R.id.one_event_fragment_top).setBackgroundColor(
                c.getColorComponents());

        root.findViewById(R.id.one_event_fragment_background)
                .setBackgroundColor(c.getColorBacground());
    }
}
