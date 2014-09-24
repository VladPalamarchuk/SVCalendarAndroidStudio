package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdadpter extends ArrayAdapter<String> {

    Context context;
    private List<String> items;

    // private final static int MESSAGE_SINGLE = 2;

    public SpinnerAdadpter(Context context, int resourceId, List<String> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
    }

    private class ViewHolder {

        TextView title;
        LinearLayout root;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        String str = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_spiner_item, null);

            holder.title = (TextView) convertView
                    .findViewById(R.id.menu_spinner_text);
            holder.root = (LinearLayout) convertView
                    .findViewById(R.id.menu_spiner_root);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.root.setBackgroundColor(new MyColor().getColorBacground());
        holder.title.setTextColor(new MyColor().getColorLabel());

        if (str.length() == 0)
            convertView.setVisibility(View.GONE);
        else
            convertView.setVisibility(View.VISIBLE);
        holder.title.setText(str);
        holder = null;

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = new ViewHolder();
        String str = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_spiner_item, null);

            holder.title = (TextView) convertView
                    .findViewById(R.id.menu_spinner_text);
            holder.root = (LinearLayout) convertView
                    .findViewById(R.id.menu_spiner_root);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.root.setBackgroundColor(new MyColor().getColorBacground());
        holder.title.setTextColor(new MyColor().getColorFont());
        holder.title.setText(str);

        return convertView;
    }

    public void Update() {
        this.notifyDataSetChanged();
    }

}