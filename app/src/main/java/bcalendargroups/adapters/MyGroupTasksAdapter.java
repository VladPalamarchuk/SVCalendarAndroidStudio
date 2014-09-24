package bcalendargroups.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

public class MyGroupTasksAdapter extends ArrayAdapter<Object> implements
		Filterable {

	private Context context;
	private ArrayList<Object> displayedItems;
	private ArrayList<Object> originalItems;

	private static final int resource = R.layout.my_group_tasks_adapter_item;

	public MyGroupTasksAdapter(Context context) {
		super(context, 0);
		this.context = context;
		displayedItems = new ArrayList<Object>();
	}

	public static class MyGroupTasksAdapterItem {

		public String id;
		public String id_event;
		public String title_event;
		public String title_group;
		public String admin;
		public String note;
		public String status_confirmation;
		public String status;

		public MyGroupTasksAdapterItem(JSONObject jsonObject)
				throws JSONException {
			this.id = jsonObject.getString("id");
			this.id_event = jsonObject.getString("id_event");
			this.title_event = jsonObject.getString("title_event");
			this.title_group = jsonObject.getString("title_group");
			this.admin = jsonObject.getString("admin");
			this.note = jsonObject.getString("note");
			this.status_confirmation = jsonObject
					.getString("status_confirmation");
			this.status = jsonObject.getString("status");
		}

		public boolean isConfirm() {
			return this.status.equalsIgnoreCase("1");
		}
	}

	public ArrayList<Object> getCollection() {
		return displayedItems;
	}

	// public boolean containsMyTask(String id) {
	// for (Object object : displayedItems) {
	// MyGroupTasksAdapterItem item = (MyGroupTasksAdapterItem) object;
	// if (Integer.valueOf(item.id) == Integer.valueOf(id))
	// return true;
	// }
	// return false;
	// }

	public boolean containsMyTask(String id) {
		for (Object object : displayedItems) {
			MyGroupTasksAdapterItem item = (MyGroupTasksAdapterItem) object;
			if (Integer.valueOf(id) == Integer.valueOf(item.id)) {
				return true;
			}
		}
		return false;
	}

	public void add(JSONArray jsonArray) throws JSONException {
		final int length = jsonArray.length();
		for (int i = 0; i < length; i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			this.add(new MyGroupTasksAdapterItem(jsonObject));
		}
		notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView myGroupTaskItemGroup;
		public TextView myGroupTaskItemAdmin;
		public TextView myGroupTaskItemEvent;
		public TextView myGroupTaskItemTask;

		public CheckBox myGroupTaskItemCheckBox;

		public Button myGroupTaskItemDecline;
	}

	@Override
	public int getCount() {
		return displayedItems.size();
	}

	@Override
	public boolean isEmpty() {
		return displayedItems.size() == 0;
	}

	@Override
	public void addAll(Collection<?> collection) {
		for (Object o : collection) {
			displayedItems.add(o);
		}
		notifyDataSetChanged();
	}

	@Override
	public void add(Object object) {
		displayedItems.add(object);
	}

	@Override
	public void clear() {
		displayedItems.clear();
	}

	@Override
	public Object getItem(int position) {
		return displayedItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyGroupTasksAdapterItem myGroupTasksAdapterItem = (MyGroupTasksAdapterItem) getItem(position);
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = layoutInflater.inflate(resource, null);

			holder.myGroupTaskItemGroup = (TextView) convertView
					.findViewById(R.id.myGroupTaskItemGroup);
			holder.myGroupTaskItemAdmin = (TextView) convertView
					.findViewById(R.id.myGroupTaskItemAdmin);

			holder.myGroupTaskItemEvent = (TextView) convertView
					.findViewById(R.id.myGroupTaskItemEvent);
			holder.myGroupTaskItemTask = (TextView) convertView
					.findViewById(R.id.myGroupTaskItemTask);

			holder.myGroupTaskItemCheckBox = (CheckBox) convertView
					.findViewById(R.id.myGroupTaskItemCheckBox);

			holder.myGroupTaskItemDecline = (Button) convertView
					.findViewById(R.id.myGroupTaskItemDecline);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.myGroupTaskItemDecline
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (onDeclineTaskListener != null) {
							onDeclineTaskListener
									.decline(myGroupTasksAdapterItem);
						}
					}
				});
		
		
	//	MainActivity.setButton(holder.myGroupTaskItemDecline);

		holder.myGroupTaskItemTask
				.setText(MainActivity.getInstance().language.TASK_BODY + ": "
						+ myGroupTasksAdapterItem.note);
		holder.myGroupTaskItemEvent
				.setText(MainActivity.getInstance().language.GROUP_EVENT + ": "
						+ myGroupTasksAdapterItem.title_event);
		holder.myGroupTaskItemAdmin
				.setText(MainActivity.getInstance().language.ADMIN_GROUP + ": "
						+ myGroupTasksAdapterItem.admin);
		holder.myGroupTaskItemGroup
				.setText(MainActivity.getInstance().language.GROUP + ": "
						+ myGroupTasksAdapterItem.title_group);

		holder.myGroupTaskItemCheckBox.setChecked(myGroupTasksAdapterItem
				.isConfirm());
		MyGroupTasksAdapter.crossOutTextView(holder.myGroupTaskItemTask,
				myGroupTasksAdapterItem.isConfirm());

		holder.myGroupTaskItemCheckBox
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (onClickListener != null) {
							onClickListener.onClick(myGroupTasksAdapterItem,
									holder.myGroupTaskItemCheckBox.isChecked());
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
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
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
		public void onClick(MyGroupTasksAdapterItem myGroupTasksAdapterItem,
                            boolean isChecked);
	}

	private OnDeclineTaskListener onDeclineTaskListener;

	public void setOnDeclineTaskListener(
			OnDeclineTaskListener onDeclineTaskListener) {
		this.onDeclineTaskListener = onDeclineTaskListener;
	}

	public static interface OnDeclineTaskListener {
		public void decline(MyGroupTasksAdapterItem myGroupTasksAdapterItem);
	}

	public static void crossOutTextView(TextView textView, boolean isCrossOut) {
		if (isCrossOut) {
			textView.setPaintFlags(textView.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			textView.setPaintFlags(textView.getPaintFlags()
					& (~Paint.STRIKE_THRU_TEXT_FLAG));
		}
	}
}
