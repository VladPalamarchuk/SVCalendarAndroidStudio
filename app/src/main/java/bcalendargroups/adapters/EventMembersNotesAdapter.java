package bcalendargroups.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.pager.GroupEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventMembersNotesAdapter extends ArrayAdapter<Object> implements
		Filterable {

	private static final String YOUR_NOTE = MainActivity.getInstance().language.NOTES_TEXT_TWO_POINT;

	private Context context;
	private ArrayList<Object> displayedItems;
	private ArrayList<Object> originalItems;

	private int resource = R.layout.event_members_notes;

	private ArrayList<Integer> checkedIds;
	private ArrayList<Object> checkedItems;

	private String groupID;
	private String eventID;

	public EventMembersNotesAdapter(Context context, String groupID,
			String eventID) {
		super(context, 0);
		this.context = context;
		this.eventID = eventID;
		this.groupID = groupID;
		displayedItems = new ArrayList<Object>();
		checkedIds = new ArrayList<Integer>();
		checkedItems = new ArrayList<Object>();
	}

	public ArrayList<Object> getCheckedItems() {
		Log.e("", "checkedItems.size = " + checkedItems.size());
		return checkedItems;
	}

	public static final class EventMembersNotesItem {

		public String id;
		public String name;
		public String phone;
		public String note;

		public EventMembersNotesItem(JSONObject jsonObject)
				throws JSONException {
			this.id = jsonObject.getString("id");
			this.name = jsonObject.getString("name");
			this.phone = jsonObject.getString("phone");
		}

		public static JSONArray getJsonArray(ArrayList<Object> items,
				String groupId, String eventId) {
			// template [{"id":"2","id_group":"2", "id_event":
			// "2","notes":"note1123123123123"}]
			JSONArray jsonArray = new JSONArray();
			for (Object item : items) {
				JSONObject jsonObject = ((EventMembersNotesItem) item)
						.getJsonObject(groupId, eventId);
				jsonArray.put(jsonObject);
			}
			return jsonArray;
		}

		public JSONObject getJsonObject(String groupId, String eventId) {
			Map<String, Object> oneMember = new HashMap<String, Object>();
			oneMember.put("id", this.id);
			oneMember.put("id_group", groupId);
			oneMember.put("id_event", eventId);
			oneMember.put("notes", this.note);

			return new JSONObject(oneMember);
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

			this.add(new EventMembersNotesItem(jsonObject));
		}
		notifyDataSetChanged();
	}

	private final class ViewHolder {
		public CheckBox eventMembersNoteCheckBox;
		public TextView eventMembersNoteTitle;
		public Button eventMembersNoteAdd;
		public TextView eventMembersNoteBody;
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
		final EventMembersNotesItem eventMembersNotesItem = (EventMembersNotesItem) getItem(position);

		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = layoutInflater.inflate(resource, null);

			holder.eventMembersNoteCheckBox = (CheckBox) convertView
					.findViewById(R.id.eventMembersNoteCheckBox);
			holder.eventMembersNoteTitle = (TextView) convertView
					.findViewById(R.id.eventMembersNoteTitle);
			holder.eventMembersNoteAdd = (Button) convertView
					.findViewById(R.id.eventMembersNoteAdd);
			holder.eventMembersNoteBody = (TextView) convertView
					.findViewById(R.id.eventMembersNoteBody);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.eventMembersNoteAdd
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						editTextDialog(context, eventMembersNotesItem);
					}
				});

		// holder.eventMembersNoteBody.setText("К-во примечаний: "
		// + eventMembersNotesItem.notes.size());

		holder.eventMembersNoteCheckBox
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (contains(eventMembersNotesItem.id)) {
							checkedIds.remove(Integer
									.valueOf(eventMembersNotesItem.id));
							holder.eventMembersNoteCheckBox.setChecked(false);

							checkedItems.remove(eventMembersNotesItem);

						} else {
							checkedIds.add(Integer
									.valueOf(eventMembersNotesItem.id));
							holder.eventMembersNoteCheckBox.setChecked(true);

							checkedItems.add(eventMembersNotesItem);
						}
					}
				});

		if (eventMembersNotesItem.note != null) {
			holder.eventMembersNoteBody.setText(YOUR_NOTE
					+ eventMembersNotesItem.note);
		}

		holder.eventMembersNoteTitle.setText(eventMembersNotesItem.name + " "
				+ eventMembersNotesItem.phone);

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
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				displayedItems = (ArrayList<Object>) results.values;
				notifyDataSetChanged();
			}
		};
		return filter;
	}

	private boolean contains(String i) {
		return checkedIds.contains(Integer.valueOf(i));
	}

	/**
	 * Global
	 * 
	 * @param context
	 */
	public void getDialog() {

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ListView dialogListView = (ListView) layoutInflater.inflate(
				R.layout.simple_listview, null);

		final EventMembersNotesAdapter eventMembersNotesAdapter = this;

		dialogListView.setAdapter(eventMembersNotesAdapter);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogListView);

		builder.setPositiveButton(
				MainActivity.getInstance().language.ADD_MEMBER,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						JSONArray jsonArray = EventMembersNotesItem
								.getJsonArray(eventMembersNotesAdapter
										.getCheckedItems(), groupID, eventID);
						try {
							GroupAssignmentActions.addPeopleToEventInGroup(
									jsonArray, context,
									new QueryMaster.OnCompleteListener() {
										@Override
										public void complete(
												String serverResponse) {
											QueryMaster
													.toast(context,
															MainActivity
																	.getInstance().language.ADDED);

											((GroupEvents) context)
					 								.getSectionsPagerAdapter()
													.notifyDataSetChanged();

										}

										@Override
										public void error(int errorCode) {
											// QueryMaster.alert(context,
											// "error");
										}
									});
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						} catch (JSONException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
						// [{"id":"2","id_group":"2", "id_event":
						// "2","notes":"note1123123123123"}]
						// ArrayList<Object> items = (ArrayList<Object>)
						// eventMembersNotesAdapter.getCheckedItems();

					}
				});

		builder.setNegativeButton(MainActivity.getInstance().language.CANCEL,
				null);
		builder.show();

		try {
			getGroupMembers(context);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Add note to person Dialog
	 * 
	 * @param context
	 * @param eventMembersNotesItem
	 */
	private void editTextDialog(Context context,
			final EventMembersNotesItem eventMembersNotesItem) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final EditText editTextView = (EditText) layoutInflater.inflate(
				R.layout.simple_edit_text, null);
		editTextView
				.setHint(MainActivity.getInstance().language.ENTER_TEXT_NOTES);
		editTextView.setSingleLine(true);
		editTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(editTextView);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (editTextView.getText().toString().length() != 0) {
					eventMembersNotesItem.note = editTextView.getText()
							.toString();
				}
				notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();

	}

	/**
	 * Show group members to add some of them to event
	 * 
	 * @param context
	 * @throws java.io.UnsupportedEncodingException
	 * @throws org.json.JSONException
	 */
	private void getGroupMembers(final Context context)
			throws UnsupportedEncodingException, JSONException {

		final QueryMaster.OnCompleteListener listener = new QueryMaster.OnCompleteListener() {
			@Override
			public void complete(String serverResponse) {
				// QueryMaster.alert(context, serverResponse);
				try {
					JSONObject jsonObject = new JSONObject(serverResponse);
					if (jsonObject.has("data")) {
						JSONArray data = jsonObject.getJSONArray("data");

						add(data);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}

			@Override
			public void error(int errorCode) {
				QueryMaster.alert(context,
						"EventMembersNotesAdapter.getGroupMembers error");
			}
		};

		GroupAssignmentActions.getGroupMembers(context, groupID, listener);
	}
}
