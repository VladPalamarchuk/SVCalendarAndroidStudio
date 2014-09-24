package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterListEvent extends ArrayAdapter<OneEventAndWeater> {

    Context context;
    private List<OneEventAndWeater> items;
    boolean isWeek2;
    ViewHolder holder;
    boolean isClick;

    boolean showDivider;

    public AdapterListEvent(Context context, int resourceId,
                            List<OneEventAndWeater> items, boolean isWeek2, boolean isClick,
                            boolean showDivider) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.isWeek2 = isWeek2;
        this.isClick = isClick;

        this.showDivider = showDivider;

    }

    private class ViewHolder {

        TextView start_end;
        TextView title;
        ImageView category_image;
        ImageView pogoda_image;
        TextView pogoda;
        TextView location;
        CheckBox isDone;
        TextView devider_text;
        LinearLayout devider;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        final OneEventAndWeater event = getItem(position);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.one_event_item, null);

            holder.start_end = (TextView) convertView
                    .findViewById(R.id.one_item_event_time_start_end);
            holder.title = (TextView) convertView
                    .findViewById(R.id.one_item_event_name);
            holder.category_image = (ImageView) convertView
                    .findViewById(R.id.one_item_event_image_category);
            holder.pogoda_image = (ImageView) convertView
                    .findViewById(R.id.one_item_event_image_pogoda);
            holder.pogoda = (TextView) convertView
                    .findViewById(R.id.one_item_event_pogoda);
            holder.location = (TextView) convertView
                    .findViewById(R.id.one_item_event_country_and_city);

            holder.isDone = (CheckBox) convertView
                    .findViewById(R.id.one_item_event_isdone);

            holder.devider_text = (TextView) convertView
                    .findViewById(R.id.one_event_item_date_in_divider);
            holder.devider = (LinearLayout) convertView
                    .findViewById(R.id.one_event_item_divider);

            MyColor c = new MyColor();
            holder.start_end.setTextColor(c.getColorFont());
            holder.title.setTextColor(c.getColorFont());
            holder.pogoda.setTextColor(c.getColorFont());

            holder.location.setTextColor(c.getColorFont());

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (isWeek2) {
            holder.start_end.setTextSize(10);
            holder.title.setTextSize(10);
            holder.location.setTextSize(10);
            holder.pogoda.setTextSize(10);
        }

        String s_e = "";
        if (!MainActivity.getInstance().shared.getBoolean(
                MainActivity.SHARED_IS_AMPM, false)) {
            if (event.getOneEvent().getStart_date_hour() < 10)
                s_e = "0";
            s_e += event.getOneEvent().getStart_date_hour() + "";
            s_e += ":";
            if (event.getOneEvent().getStart_date_minute() < 10)
                s_e += "0";
            s_e += event.getOneEvent().getStart_date_minute() + "";

            if (event.getOneEvent().getStart_date_hour() == 0
                    && event.getOneEvent().getStart_date_minute() == 0
                    && event.getOneEvent().getEnd_date_hour() == 23
                    && event.getOneEvent().getEnd_date_minute() == 59) {
                holder.start_end
                        .setText(MainActivity.getInstance().language.ALL_DAY);
            } else
                holder.start_end.setText(s_e);
        } else {
            boolean pm = false;
            if (event.getOneEvent().getStart_date_hour() < 10)
                s_e = "0";
            if (event.getOneEvent().getStart_date_hour() > 12) {
                s_e += String
                        .valueOf(event.getOneEvent().getStart_date_hour() - 12)
                        + "";
                pm = true;
            } else
                s_e += event.getOneEvent().getStart_date_hour() + "";
            s_e += ":";
            if (event.getOneEvent().getStart_date_minute() < 10)
                s_e += "0";
            s_e += event.getOneEvent().getStart_date_minute() + "";

            if (pm)
                s_e += " PM";
            else
                s_e += " AM";
            if (event.getOneEvent().getStart_date_hour() == 0
                    && event.getOneEvent().getStart_date_minute() == 0
                    && event.getOneEvent().getEnd_date_hour() == 23
                    && event.getOneEvent().getEnd_date_minute() == 59) {
                holder.start_end
                        .setText(MainActivity.getInstance().language.ALL_DAY);
            } else
                holder.start_end.setText(s_e);

        }

        new Catigories(Integer.parseInt(event.getOneEvent().getCategory()),
                holder.category_image).setImage();

        if (MainActivity
                .getInstance()
                .getLocationById(MainActivity.getInstance().CITIES,
                        Integer.parseInt(event.getOneEvent().getLocation()))
                .length() > 0) {
            holder.location.setText(MainActivity.getInstance().getLocationById(
                    MainActivity.getInstance().CITIES,
                    Integer.parseInt(event.getOneEvent().getLocation())));
            holder.location.setVisibility(View.VISIBLE);
        } else
            holder.location.setVisibility(View.GONE);

        holder.title.setText(event.getOneEvent().getTitle());

        // if (!isWeek2)
        // holder.isDone.setVisibility(View.VISIBLE);
        // else
        // holder.isDone.setVisibility(View.GONE);

        if (event.getOneEvent().getIsDone() == 1) {
            holder.isDone.setChecked(true);
            holder.title.setPaintFlags(holder.title.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.start_end.setPaintFlags(holder.start_end.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.location.setPaintFlags(holder.location.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.isDone.setChecked(false);

            holder.title.setPaintFlags(holder.title.getPaintFlags()
                    & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.start_end.setPaintFlags(holder.start_end.getPaintFlags()
                    & (~Paint.STRIKE_THRU_TEXT_FLAG));

            holder.location.setPaintFlags(holder.location.getPaintFlags()
                    & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }

        holder.isDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                MainActivity.getInstance().ChangeIsDone(event.getOneEvent());
                int done = 0;
                if (event.getOneEvent().getIsDone() == 0)
                    done = 1;
                event.getOneEvent().setIsDone(done);
                notifyDataSetChanged();
                new PlaySoundButton();
            }
        });

        holder.pogoda_image.setVisibility(View.GONE);
        holder.pogoda.setVisibility(View.GONE);

        event.setImage(holder.pogoda_image);
        event.setText(holder.pogoda);

        event.run();

        if (isClick)
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    new PlaySoundButton();
                    MounthFragment.getInstance().arrayOneEventAndWeaters
                            .clear();
                    MounthFragment.getInstance().arrayOneEventAndWeaters
                            .add(event);
                    MainActivity.getInstance().startOneEventFragment(0);

                }
            });

        try {

            int number_day = event.getOneEvent().getStart_date_day();
            int number_month = event.getOneEvent().getStart_date_mounth();
            int number_year = event.getOneEvent().getStart_date_year();

            if (position > 0) {

                if (number_day != getItem(position - 1).getOneEvent()
                        .getStart_date_day()) {
                    holder.devider.setVisibility(View.VISIBLE);
                    holder.devider_text.setText(MainActivity
                            .FormatDate(number_day)
                            + "-"
                            + MainActivity.FormatDate(number_month)
                            + "-"
                            + MainActivity.FormatDate(number_year));
                } else {
                    holder.devider.setVisibility(View.GONE);
                }

            } else {
                holder.devider.setVisibility(View.VISIBLE);
                holder.devider_text.setText(MainActivity.FormatDate(number_day)
                        + "-" + MainActivity.FormatDate(number_month) + "-"
                        + MainActivity.FormatDate(number_year));

            }

            if (!showDivider)
                holder.devider.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }

        Animation anim = AnimationUtils.loadAnimation(
                MainActivity.getInstance(), R.anim.myanimation);

        convertView.startAnimation(anim);
        holder = null;
        return convertView;
    }

    public void Update() {
        this.notifyDataSetChanged();
    }

}