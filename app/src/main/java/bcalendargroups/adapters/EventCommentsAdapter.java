package bcalendargroups.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.R;

import java.util.ArrayList;


public class EventCommentsAdapter extends ArrayAdapter<Object> implements Filterable {

    private Context context;
    private ArrayList<Object> displayedItems;
    private ArrayList<Object> originalItems;

    private int resource;

    public EventCommentsAdapter(Context context, int resource) {
        super(context, 0);
        this.context = context;
        this.resource = resource;
        displayedItems = new ArrayList<Object>();
    }

    public static class EventCommentsAdapterItem {

        public String text;
        public String date;
        public String name;

        public EventCommentsAdapterItem(JSONObject jsonObject) throws JSONException {
            this.text = jsonObject.getString("text");
            this.date = jsonObject.getString("date");
            this.name = jsonObject.getString("name");
        }
    }

    @Override
    public void clear() {
        displayedItems.clear();
    }

    @Override
    public int getCount() {
        return displayedItems.size() + 1;
    }

    public void add(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null || jsonArray.length() == 0)
            return;

        final int length = jsonArray.length();
        for (int i = length - 1; i >= 0; i--) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.add(new EventCommentsAdapterItem(jsonObject));
        }
        notifyDataSetChanged();
    }


    private class ViewHolder {
        public TextView eventCommentMessage;
        public TextView eventCommentAuthor;
        public TextView eventCommentDate;
    }

    @Override
    public void add(Object object) {
        displayedItems.add(object);
    }

    @Override
    public Object getItem(int position) {
        return displayedItems.get(--position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * first view will be add comment button
         */
        if (position == 0) {
            Button button = new Button(context);
            button.setText("Add comment");
            button.setTextColor(Color.WHITE);
            button.setOnClickListener(new AddCommentClick());

            return button;
        }

        final EventCommentsAdapterItem eventCommentsAdapterItem = (EventCommentsAdapterItem) getItem(position);
        ViewHolder holder;

        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(resource, null);

            holder.eventCommentMessage = (TextView) convertView.findViewById(R.id.eventCommentMessage);
            holder.eventCommentAuthor = (TextView) convertView.findViewById(R.id.eventCommentAuthor);
            holder.eventCommentDate = (TextView) convertView.findViewById(R.id.eventCommentDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.eventCommentMessage.setText(eventCommentsAdapterItem.text);
        holder.eventCommentAuthor.setText(eventCommentsAdapterItem.name);
        holder.eventCommentDate.setText(eventCommentsAdapterItem.date);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public static interface OnClickListener {
        public void addComment();
    }

    private class AddCommentClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.addComment();
            }
        }
    }


}
