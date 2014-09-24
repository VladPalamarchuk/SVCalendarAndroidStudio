package bcalendargroups.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.R;

import java.util.ArrayList;


public class EventsByGroupAdapter extends ArrayAdapter<Object> implements Filterable {


    private OnCheckedListener onCheckedListener;

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    private OnEventClick onEventClick;

    public void setOnEventClick(OnEventClick onEventClick) {
        this.onEventClick = onEventClick;
    }

    private Context context;
    private ArrayList<Object> displayedItems;
    private ArrayList<Object> originalItems;

    private int resource;

    private ArrayList<Integer> checkedIds;

    public EventsByGroupAdapter(Context context, int resource) {
        super(context, 0);
        this.context = context;
        this.resource = resource;
        displayedItems = new ArrayList<Object>();
        checkedIds = new ArrayList<Integer>();
    }

    public static class EventsByGroupItem {

        public String title;
        public String id;
        private String status;

        private JSONObject jsonObject;

        public EventsByGroupItem(JSONObject jsonObject) throws JSONException {
            this.id = jsonObject.getString("id");
            this.title = jsonObject.getString("title");
            this.status = jsonObject.getString("status");

            this.jsonObject = jsonObject;
        }

        @Override
        public String toString() {
            return jsonObject.toString();
        }

        public boolean isDone() {
            return status.equalsIgnoreCase("1");
        }
    }

    @Override
    public int getCount() {
        return displayedItems.size();
    }

    public void add(JSONArray jsonArray) throws JSONException {
        final int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.add(new EventsByGroupItem(jsonObject));
        }
        notifyDataSetChanged();
    }


    private class ViewHolder {
        public TextView name;
//        public CheckBox checkBox;
    }

    @Override
    public void add(Object object) {
        displayedItems.add(object);
    }

    @Override
    public Object getItem(int position) {
        return displayedItems.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EventsByGroupItem eventsByGroupItem = (EventsByGroupItem) getItem(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(resource, null);

            holder.name = (TextView) convertView.findViewById(R.id.assignment_by_group_item_title);
//            holder.checkBox = (CheckBox) convertView.findViewById(R.id.assignment_by_group_item_checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(eventsByGroupItem.title);

        if (eventsByGroupItem.isDone()) {
            holder.name.setPaintFlags(holder.name.getPaintFlags()
                    | (Paint.STRIKE_THRU_TEXT_FLAG));

//            holder.checkBox.setChecked(true);
        } else {
            holder.name.setPaintFlags(holder.name.getPaintFlags()
                    & (~Paint.STRIKE_THRU_TEXT_FLAG));

//            holder.checkBox.setChecked(false);
        }

//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (contains(eventsByGroupItem.id)) {
//                    checkedIds.remove(Integer.valueOf(eventsByGroupItem.id));
//                    holder.checkBox.setChecked(false);
//
//                    if (onCheckedListener != null) {
//                        onCheckedListener.checked(eventsByGroupItem, false);
//                    }
//                } else {
//                    checkedIds.add(Integer.valueOf(eventsByGroupItem.id));
//                    holder.checkBox.setChecked(true);
//
//                    if (onCheckedListener != null) {
//                        onCheckedListener.checked(eventsByGroupItem, true);
//                    }
//                }
//            }
//        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEventClick != null) {
                    onEventClick.click(eventsByGroupItem);
                }
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Object> filteredCollection = new ArrayList<Object>();

                if (originalItems == null) {
                    originalItems = new ArrayList<Object>(displayedItems);
                }
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = originalItems.size();
                    filterResults.values = originalItems;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (Object originalItem : originalItems) {

                    }
                    filterResults.count = filteredCollection.size();
                    filterResults.values = filteredCollection;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                displayedItems = (ArrayList<Object>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public static interface OnCheckedListener {
        public void checked(EventsByGroupItem eventsByGroupItem, boolean isChecked);
    }


    public static interface OnEventClick {
        public void click(EventsByGroupItem eventsByGroupItem);
    }

    private boolean contains(String i) {
        return checkedIds.contains(Integer.valueOf(i));
    }

    public String getCheckedIdsAsString() {
        return checkedIds.toString().replace("[", "").replace("]", "").replace(" ", "");
    }
}
