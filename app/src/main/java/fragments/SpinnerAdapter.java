package fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;

    private int resourceId;
    private int dropDownResourceId;

    public SpinnerAdapter(Context context, int resourceId,
                          int dropDownResourceId,
                          ArrayList<String> items) {
        super(context, resourceId, items);

        this.resourceId = resourceId;
        this.dropDownResourceId = dropDownResourceId;
        this.context = context;
    }


    private class ViewHolder {
        TextView title;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();


        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater
                    .inflate(resourceId, null);
            convertView.setPadding(10, 10, 10, 10);

            holder.title = (TextView) convertView.findViewById(android.R.id.text1);
            holder.title.setTextColor(Color.WHITE);
            holder.title.setGravity(Gravity.CENTER);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setText(getItem(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater
                    .inflate(dropDownResourceId, null);
            convertView.setPadding(10, 10, 10, 10);

            holder.title = (TextView) convertView.findViewById(android.R.id.text1);
//            holder.title.setTextColor(Color.WHITE);
            holder.title.setGravity(Gravity.CENTER);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setText(getItem(position));

        return convertView;
    }
}
