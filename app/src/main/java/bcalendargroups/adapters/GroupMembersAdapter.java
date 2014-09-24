package bcalendargroups.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.R;

import java.util.ArrayList;

public class GroupMembersAdapter extends ArrayAdapter<Object> implements
        Filterable {

    private Context context;
    private ArrayList<Object> displayedItems;
    private ArrayList<Object> originalItems;

    private int resource;

    public GroupMembersAdapter(Context context, int resource) {
        super(context, 0);
        this.context = context;
        this.resource = resource;
        displayedItems = new ArrayList<Object>();
    }

    public GroupMembersAdapter(Context context) {
        this(context, R.layout.group_member_item);
    }


    public static class GroupMemberItem {
        public String id;

        public String name;
        public String surname;
        public String phone;

        public GroupMemberItem(JSONObject jsonObject) throws JSONException {

            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.surname = jsonObject.getString("surname");
            this.phone = jsonObject.getString("phone");
        }
    }

    @Override
    public void clear() {
        displayedItems.clear();
    }

    @Override
    public int getCount() {
        return displayedItems.size();
    }

    public void add(JSONArray jsonArray) throws JSONException {
        final int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.add(new GroupMemberItem(jsonObject));
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView groupMemberName;
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
        final GroupMemberItem groupMemberItem = (GroupMemberItem) getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(resource, null);

            holder.groupMemberName = (TextView) convertView
                    .findViewById(R.id.groupMemberName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.groupMemberName.setText(groupMemberItem.name);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
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
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                displayedItems = (ArrayList<Object>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

}
