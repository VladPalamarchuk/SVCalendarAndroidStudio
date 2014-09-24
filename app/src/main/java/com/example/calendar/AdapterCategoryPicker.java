package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterCategoryPicker extends ArrayAdapter<Integer> {

    Context context;
    private List<Integer> items;

    // private final static int MESSAGE_SINGLE = 2;

    public AdapterCategoryPicker(Context context, int resourceId,
                                 List<Integer> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
    }


    private class ViewHolder {

        ImageView image;
        TextView title;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        Integer id = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater
                    .inflate(R.layout.category_pivker_item, null);

            holder.image = (ImageView) convertView
                    .findViewById(R.id.category_picker_item_image);
            holder.title = (TextView) convertView
                    .findViewById(R.id.category_picker_item_title);
            holder.title.setTextColor(new MyColor().getColorComponents());
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setTextColor(new MyColor().getColorFont());
        Catigories c = new Catigories(id, holder.image);
        holder.title.setText(c.getName());
        c.setImage();

        holder = null;

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = new ViewHolder();
        Integer id = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater
                    .inflate(R.layout.category_pivker_item, null);

            holder.image = (ImageView) convertView
                    .findViewById(R.id.category_picker_item_image);
            holder.title = (TextView) convertView
                    .findViewById(R.id.category_picker_item_title);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setTextColor(new MyColor().getColorFont());
        Catigories c = new Catigories(id, holder.image);
        holder.title.setText(c.getName());
        c.setImage();

        holder = null;

        return convertView;
        //return super.getDropDownView(position, convertView, parent);
    }

    public void Update() {
        this.notifyDataSetChanged();
    }

}