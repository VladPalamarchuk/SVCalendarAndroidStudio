package bcalendargroups;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
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

import com.example.calendar.DrawMounth;
import com.example.calendar.MainActivity;
import com.example.calendar.R;
import com.google.android.gms.drive.internal.t;

import java.util.ArrayList;

public final class GroupTaskListAdapter extends ArrayAdapter<Object> implements
		Filterable {

	// click listener
	private OnGroupTaskClickListener onGroupTaskClickListener;

	private Context context;
	private ArrayList<Object> displayedItems;
	private ArrayList<Object> originalItems;

	private int resource;

	public GroupTaskListAdapter(Context context, int resource) {
		super(context, 0);
		this.context = context;
		this.resource = resource;
		displayedItems = new ArrayList<Object>();
	}

	public static final class AssignmentListItem {
		public String title;
		public String id;
		
		public boolean isAdmin;

		public AssignmentListItem(JSONObject jsonObject) throws JSONException {
			this.title = jsonObject.getString("title");
			this.id = jsonObject.getString("id");
			this.isAdmin = false;
		}

		public AssignmentListItem(JSONObject jsonObject, boolean isAdmin)
				throws JSONException {
			this.title = jsonObject.getString("title");
			this.id = jsonObject.getString("id");
			this.isAdmin = isAdmin;
		}
	}

	public static final class GroupTaskDivider {

		public String dividerName;

		public GroupTaskDivider(String dividerName) {
			this.dividerName = dividerName;
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

			this.add(new AssignmentListItem(jsonObject));
		}
		notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView title;
	}

	@Override
	public void add(Object object) {
		displayedItems.add(object);
		notifyDataSetChanged();
	}

	public void insertAsFirst(Object object) {
		displayedItems.add(0, object);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return displayedItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Object object = getItem(position);

		if (object instanceof GroupTaskDivider) {
			GroupTaskDivider groupTaskDivider = (GroupTaskDivider) object;
			TextView textView = new TextView(context);

			if (MainActivity.THEME_APPLICATION == DrawMounth.BASE_THEME) {
				textView.setTextColor(Color.WHITE);
			}
 
			textView.setText(groupTaskDivider.dividerName);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			textView.setGravity(Gravity.CENTER_HORIZONTAL);

			return textView;
		} else {
			final AssignmentListItem assignmentListItem = (AssignmentListItem) object;
			ViewHolder holder;

			if (convertView == null || convertView.getTag() == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = layoutInflater.inflate(resource, null);

				

				holder.title = (TextView) convertView
						.findViewById(R.id.assignment_list_item_title);
				if (MainActivity.THEME_APPLICATION == DrawMounth.BASE_THEME) {
					holder.title.setTextColor(Color.WHITE);
				}

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			try {
				holder.title.setText(assignmentListItem.title);
			} catch (NullPointerException ex) {

			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onGroupTaskClickListener != null) {
						onGroupTaskClickListener.onClick(assignmentListItem);
					}
				}
			});

			return convertView;
		}
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

	public void setOnGroupTaskClickListener(
			OnGroupTaskClickListener onGroupTaskClickListener) {
		this.onGroupTaskClickListener = onGroupTaskClickListener;
	}

	public static interface OnGroupTaskClickListener {
		public void onClick(AssignmentListItem assignmentListItem);
	}

	public void addMyGroups(JSONArray jsonArray) throws JSONException {
		if (jsonArray != null) {
			final int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				this.add(new AssignmentListItem(jsonObject, true));
			}
			notifyDataSetChanged();
		}
	}

	public void addGroupsMyParticipation(JSONArray jsonArray)
			throws JSONException {
		if (jsonArray != null) {
			final int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				this.add(new AssignmentListItem(jsonObject, false));
			}
			notifyDataSetChanged();
		}
	}

}
