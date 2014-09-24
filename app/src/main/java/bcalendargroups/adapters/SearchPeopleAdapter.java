package bcalendargroups.adapters;

import android.content.Context;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;




public class SearchPeopleAdapter extends ArrayAdapter<Object> implements Filterable {

    private Context context;
    private ArrayList<Object> displayedItems;
    private ArrayList<Object> originalItems;

    private int resource;

    public ArrayList<Integer> getCheckedIds() {
        return checkedIds;
    }

    private ArrayList<Integer> checkedIds;

    public void setCheckedIds(ArrayList<Integer> checkedIds) {
		this.checkedIds = checkedIds;
	}

	public SearchPeopleAdapter(Context context, int resource) {
        super(context, 0);
        this.context = context;
        this.resource = resource;
        displayedItems = new ArrayList<Object>();
        checkedIds = new ArrayList<Integer>();
    }

    public static class SearchPeopleItem {
        public String name;
        public String id;

        public SearchPeopleItem(JSONObject jsonObject) throws JSONException {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
        }
    }

    @Override
    public void clear() {
        displayedItems.clear();
        checkedIds.clear();
    }

    @Override
    public int getCount() {
        return displayedItems.size();
    }

    public void add(JSONArray jsonArray) throws JSONException {
        final int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            this.add(new SearchPeopleItem(jsonObject));
        }
        notifyDataSetChanged();
    }


    private class ViewHolder {
        public TextView name;
        public CheckBox checkBox;
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
        final SearchPeopleItem searchPeopleItem = (SearchPeopleItem) getItem(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(resource, null);


            holder.name = (TextView) convertView.findViewById(R.id.search_people_item_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.search_people_item_check_box);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contains(searchPeopleItem.id)) {
                    checkedIds.remove(Integer.valueOf(searchPeopleItem.id));
                    holder.checkBox.setChecked(false);
                } else {
                    checkedIds.add(Integer.valueOf(searchPeopleItem.id));
                    holder.checkBox.setChecked(true);
                }
            }
        });

        holder.name.setText(searchPeopleItem.name);

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

    private boolean contains(String i) {
        return checkedIds.contains(Integer.valueOf(i));
    }

    public String getCheckedIdsAsString() {
        return checkedIds.toString().replace("[", "").replace("]", "").replace(" ", "");
    }
    
}