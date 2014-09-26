package bcalendargroups.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import bcalendargroups.lazylist.ImageLoader;

public class GroupMembersAdapter extends ArrayAdapter<Object> implements
        Filterable {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnLongClickListener onLongClickListener;

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    private Context context;
    private ArrayList<Object> displayedItems;
    private ArrayList<Object> originalItems;

    private ImageLoader imageLoader;

    private int resource;

    public GroupMembersAdapter(Context context, int resource) {
        super(context, 0);
        this.context = context;
        this.resource = resource;
        this.displayedItems = new ArrayList<Object>();
        this.imageLoader = new ImageLoader(context);
    }

    public GroupMembersAdapter(Context context) {
        this(context, R.layout.group_member_item);
    }


    public static class GroupMemberItem implements Serializable, Cloneable,
            Comparable<GroupMemberItem> {

        private static final long serialVersionUID = 3L;

        public String id;

        public String name;
        public String surname;
        public String phone;
        public String photo;

        public GroupMemberItem(JSONObject jsonObject) throws JSONException {

            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.surname = jsonObject.getString("surname");
            this.phone = jsonObject.getString("phone");
            this.photo = jsonObject.getString("photo");
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int compareTo(GroupMemberItem memberItem) {
            return this.name.compareTo(memberItem.name);
        }
    }

    public void remove(GroupMemberItem memberItem) {
        for (Iterator<Object> iter = displayedItems.iterator(); iter.hasNext(); ) {
            GroupMemberItem item = (GroupMemberItem) iter.next();
            if (item.id.equalsIgnoreCase(memberItem.id)) {
                iter.remove();
                return;
            }
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
        public ImageView groupMemberPhoto;
    }

    @Override
    public void addAll(Collection<?> collection) {
        for (Object o : collection) {
            this.add(o);
        }
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
            holder.groupMemberPhoto = (ImageView) convertView
                    .findViewById(R.id.groupMemberPhoto);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.DisplayImage(groupMemberItem.photo, holder.groupMemberPhoto);
        holder.groupMemberName.setText(groupMemberItem.name);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(groupMemberItem);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.longClick(groupMemberItem);
                    return true;
                }
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


    public static interface OnClickListener {
        public void onClick(Object o);
    }

    public static interface OnLongClickListener {
        public void longClick(Object o);
    }

}
