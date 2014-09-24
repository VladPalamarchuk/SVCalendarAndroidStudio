package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterListGrEvent extends ArrayAdapter<OneEventGroup> {

    Context context;
    private List<OneEventGroup> items;

    public AdapterListGrEvent(Context context, int resourceId,
                              List<OneEventGroup> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;

    }

    private class ViewHolder {
        TextView start;
        TextView end;
        TextView push;
        TextView title;
        TextView category;
        TextView location;
        TextView file;
        TextView info;
        LinearLayout peoples;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        final OneEventGroup event = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.one_gr_item, null);
            setText(convertView);
            holder.start = (TextView) convertView
                    .findViewById(R.id.gr_event_start_date);
            holder.end = (TextView) convertView
                    .findViewById(R.id.gr_event_end_date);
            holder.push = (TextView) convertView
                    .findViewById(R.id.gr_event_push_date);
            holder.title = (TextView) convertView
                    .findViewById(R.id.gr_event_name);
            holder.category = (TextView) convertView
                    .findViewById(R.id.gr_event_category);
            holder.location = (TextView) convertView
                    .findViewById(R.id.gr_event_location);
            holder.file = (TextView) convertView
                    .findViewById(R.id.gr_event_file);
            holder.info = (TextView) convertView
                    .findViewById(R.id.gr_event_info);
            holder.peoples = (LinearLayout) convertView
                    .findViewById(R.id.gr_event_people_lin);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (event.getTitle().length() > 0) {
            holder.title.setText(event.getTitle());
            convertView.findViewById(R.id.gr_event_name_lin).setVisibility(
                    View.VISIBLE);
        } else {
            convertView.findViewById(R.id.gr_event_name_lin).setVisibility(
                    View.GONE);
        }

        String start = "";
        start = MainActivity.getInstance().FormatDate(
                event.getStart_date_year())
                + "-"
                + MainActivity.getInstance().FormatDate(
                event.getStart_date_mounth())
                + "-"
                + MainActivity.getInstance().FormatDate(
                event.getStart_date_day())
                + " "
                + MainActivity.getInstance().FormatDate(
                event.getStart_date_hour())
                + ":"
                + MainActivity.getInstance().FormatDate(
                event.getStart_date_minute());
        if (start.length() > 0) {
            holder.start.setText(start);
            convertView.findViewById(R.id.gr_event_start_lin).setVisibility(
                    View.VISIBLE);
        } else {
            convertView.findViewById(R.id.gr_event_start_lin).setVisibility(
                    View.GONE);
        }

        String end = "";
        end = MainActivity.getInstance().FormatDate(event.getEnd_date_year())
                + "-"
                + MainActivity.getInstance().FormatDate(
                event.getEnd_date_mounth())
                + "-"
                + MainActivity.getInstance()
                .FormatDate(event.getEnd_date_day())
                + " "
                + MainActivity.getInstance().FormatDate(
                event.getEnd_date_hour())
                + ":"
                + MainActivity.getInstance().FormatDate(
                event.getEnd_date_minute());
        if (end.length() > 0) {
            holder.end.setText(end);
            convertView.findViewById(R.id.gr_event_end_lin).setVisibility(
                    View.VISIBLE);
        } else {
            convertView.findViewById(R.id.gr_event_end_lin).setVisibility(
                    View.GONE);
        }

        String push = "";
        push = event.getPush_time();
        if (push.length() > 0) {
            holder.push.setText(push);
            convertView.findViewById(R.id.gr_event_push_lin).setVisibility(
                    View.VISIBLE);
        } else {
            convertView.findViewById(R.id.gr_event_push_lin).setVisibility(
                    View.GONE);
        }
        try {

            String cat = new Catigories(Integer.parseInt(event.getCategory()),
                    null).getName();
            if (cat.length() > 0) {
                holder.category.setText(cat);
                convertView.findViewById(R.id.gr_event_categ_lin)
                        .setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.gr_event_categ_lin)
                        .setVisibility(View.GONE);
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
            convertView.findViewById(R.id.gr_event_categ_lin).setVisibility(
                    View.GONE);
        }
        String file = event.getFile_path();
        if (file.length() > 0) {
            holder.file.setText(file);
            convertView.findViewById(R.id.gr_event_file_lin).setVisibility(
                    View.VISIBLE);
        } else {
            convertView.findViewById(R.id.gr_event_file_lin).setVisibility(
                    View.GONE);
        }

        String info = event.getInfo();
        if (info.length() > 0) {
            holder.info.setText(info);
            convertView.findViewById(R.id.gr_event_info_lin).setVisibility(
                    View.VISIBLE);
        } else {
            convertView.findViewById(R.id.gr_event_info_lin).setVisibility(
                    View.GONE);
        }
        try {

            String location = MainActivity.getInstance().getLocationById(
                    MainActivity.getInstance().CITIES,
                    Integer.parseInt(event.getLocation()));
            if (location.length() > 0) {
                holder.location.setText(location);
                convertView.findViewById(R.id.gr_event_location_lin)
                        .setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.gr_event_location_lin)
                        .setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
            convertView.findViewById(R.id.gr_event_location_lin).setVisibility(
                    View.GONE);
        }
        holder = null;
        return convertView;
    }

    public void Update() {
        this.notifyDataSetChanged();
    }

    public void setText(View vonvertView) {
        ((TextView) vonvertView.findViewById(R.id.gr_event_name_title))
                .setText(MainActivity.getInstance().language.GR_NAME);
        ((TextView) vonvertView.findViewById(R.id.gr_event_start_date_title))
                .setText(MainActivity.getInstance().language.GR_START);
        ((TextView) vonvertView.findViewById(R.id.gr_event_end_date_title))
                .setText(MainActivity.getInstance().language.GR_END);

        ((TextView) vonvertView.findViewById(R.id.gr_event_push_date_title))
                .setText(MainActivity.getInstance().language.GR_PUSH);

        ((TextView) vonvertView.findViewById(R.id.gr_event_category_title))
                .setText(MainActivity.getInstance().language.GR_CAT);

        ((TextView) vonvertView.findViewById(R.id.gr_event_location_title))
                .setText(MainActivity.getInstance().language.GR_LOC);
        ((TextView) vonvertView.findViewById(R.id.gr_event_file_title))
                .setText(MainActivity.getInstance().language.GR_FILE);

        ((TextView) vonvertView.findViewById(R.id.gr_event_info_title))
                .setText(MainActivity.getInstance().language.GR_INFO);

    }
}