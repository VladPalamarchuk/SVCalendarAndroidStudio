package com.example.calendar;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterColorPicker extends ArrayAdapter<OneColorPicker> {

    Context context;
    private List<OneColorPicker> items;

    // private final static int MESSAGE_SINGLE = 2;

    public AdapterColorPicker(Context context, int resourceId,
                              List<OneColorPicker> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
    }

    private class ViewHolder {
        RelativeLayout result_color;
        TextView title;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        OneColorPicker rowItem = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.color_picker_item, null);
            new ViewHolder();
            holder.result_color = (RelativeLayout) convertView
                    .findViewById(R.id.color_picker_item_color_result);
            holder.title = (TextView) convertView
                    .findViewById(R.id.color_picker_item_title);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.title.setTextColor(new MyColor().getColorFont());

        holder.title.setText(rowItem.getTitle());

        //	holder.result_color.setBackgroundColor(rowItem.getColor());
        MainActivity.getInstance().setRelativeColor(holder.result_color, rowItem.getColor());
        holder = null;

        return convertView;
    }

    public void Update() {
        this.notifyDataSetChanged();
    }


}