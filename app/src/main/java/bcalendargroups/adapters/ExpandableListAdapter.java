package bcalendargroups.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import bcalendargroups.lazylist.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private final String THIS_IS_YOU = "Это вы";

	private final Context _context;
	private final ArrayList<SingleUserWithNotes> users;
	private final ImageLoader imageLoader;

	private final String calendarUserID;

	private View groupHeaderView;

	private boolean showAddTaskButton = false;

	public void setShowAddTaskButton(boolean flag) {
		showAddTaskButton = flag;
	}

	private ChildItemClickListener childItemClickListener;

	public void setChildItemClickListener(
			ChildItemClickListener childItemClickListener) {
		this.childItemClickListener = childItemClickListener;
	}

	public ExpandableListAdapter(Context context, String calendarUserID) {
		this._context = context;
		this.users = new ArrayList<SingleUserWithNotes>();
		this.imageLoader = new ImageLoader(context);
		this.calendarUserID = calendarUserID;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		if (groupHeaderView != null)
			groupPosition--;
		if (showAddTaskButton)
			childPosititon--;

		return users.get(groupPosition).userNotes.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (showAddTaskButton && childPosition == 0) {
			Button btn = new Button(_context);
			btn.setText(MainActivity.getInstance().language.ADD_TASK);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (childHeaderClickListener != null) {
						childHeaderClickListener
								.click((SingleUserWithNotes) getGroup(groupPosition));
					}
				}
			});

			return btn;
		}

		final SingleUserNote userNote = (SingleUserNote) getChild(
				groupPosition, childPosition);
		final ChildViewHolder holder;

		if (convertView == null || convertView.getTag() == null) {
			holder = new ChildViewHolder();

			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.adapter_user_tasks_sub_item, null);

			holder.adapterUserTasksItemSubItemTitle = (TextView) convertView
					.findViewById(R.id.adapterUserTasksItemSubItemTitle);
			holder.userTaskItemIsStatus = (ImageView) convertView
					.findViewById(R.id.userTaskItemIsStatus);

			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}

		holder.adapterUserTasksItemSubItemTitle.setText(userNote.note);

		if (userNote.isConfirm()) {
			holder.userTaskItemIsStatus.setImageResource(R.drawable.task_done);
		} else if (userNote.isAdopted()) {
			holder.userTaskItemIsStatus
					.setImageResource(R.drawable.task_confirmated);
		} else {
			holder.userTaskItemIsStatus
					.setImageResource(R.drawable.task_deviated);
		}

		if (childItemClickListener != null) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					childItemClickListener.click(userNote);
				}
			});
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupHeaderView != null)
			groupPosition--;

		if (showAddTaskButton) {
			return users.get(groupPosition).userNotes.size() + 1;
		} else {
			return users.get(groupPosition).userNotes.size();
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groupHeaderView != null)
			groupPosition--;

		return users.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupHeaderView == null ? users.size() : users.size() + 1;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (groupHeaderView != null && groupPosition == 0) {
			groupHeaderView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (groupHeaderClickListener != null) {
						groupHeaderClickListener.click();
					}
				}
			});

			return groupHeaderView;
		}

		final SingleUserWithNotes userWithNotes = (SingleUserWithNotes) getGroup(groupPosition);
		final GroupViewHolder holder;

		if (convertView == null || convertView.getTag() == null) {
			holder = new GroupViewHolder();

			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.adapter_user_tasks_item, null);

			holder.userTaskItemName = (TextView) convertView
					.findViewById(R.id.userTaskItemName);
			holder.userTaskItemArrowBottom = (ImageView) convertView
					.findViewById(R.id.userTaskItemArrow);
			holder.userTaskItemImage = (ImageView) convertView
					.findViewById(R.id.userTaskItemImage);

			holder.userTaskItemNoteCount = (TextView) convertView
					.findViewById(R.id.userTaskItemNotesCountTextView);

			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}

		if (userWithNotes.id.equalsIgnoreCase(calendarUserID)) {
			holder.userTaskItemName.setText(userWithNotes.name + THIS_IS_YOU);
		} else {
			holder.userTaskItemName.setText(userWithNotes.name);
		}

		if (userWithNotes.userNotes != null) {
			holder.userTaskItemArrowBottom.setVisibility(View.VISIBLE);
			holder.userTaskItemNoteCount.setText(String
					.valueOf(userWithNotes.userNotes.size()));
		} else {
			holder.userTaskItemArrowBottom.setVisibility(View.GONE);
		}

		imageLoader.DisplayImage(userWithNotes.photo, holder.userTaskItemImage);

		return convertView;
	}

	public void setGroupHeaderView(View view) {
		groupHeaderView = view;
	}

	public void removeHeaderView() {
		groupHeaderView = null;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void add(JSONArray users) throws JSONException {
		if (users != null) {
			for (int i = 0; i < users.length(); i++) {
				this.users.add(new SingleUserWithNotes(users.getJSONObject(i)));
			}
			notifyDataSetChanged();
		}
	}

	private final class GroupViewHolder {
		public TextView userTaskItemName;
		public ImageView userTaskItemArrowBottom;
		public ImageView userTaskItemImage;

		public TextView userTaskItemNoteCount;
	}

	private final class ChildViewHolder {
		public TextView adapterUserTasksItemSubItemTitle;
		public ImageView userTaskItemIsStatus;
	}

	public static class SingleUserWithNotes implements
			Comparable<SingleUserWithNotes> {

		public String id;
		public String name;
		public String photo;

		public ArrayList<SingleUserNote> userNotes;

		public SingleUserWithNotes(JSONObject jsonObject) throws JSONException {
			this.id = jsonObject.getString("id");
			this.name = jsonObject.getString("name");
			this.photo = jsonObject.getString("photo");
			this.userNotes = new ArrayList<SingleUserNote>();

			JSONArray notes = jsonObject.has("notes") ? jsonObject
					.getJSONArray("notes") : null;
			this.initNotes(notes);
		}

		private void initNotes(JSONArray notes) throws JSONException {
			if (notes == null)
				return;

			for (int i = notes.length() - 1; i >= 0; i--) {
				userNotes.add(new SingleUserNote(notes.getJSONObject(i)));
			}
		}

		@Override
		public int compareTo(SingleUserWithNotes another) {
			return id.compareTo(another.id);
		}
	}

	private GroupHeaderClickListener groupHeaderClickListener;

	public void setChildHeaderClickListener(
			ChildHeaderClickListener childHeaderClickListener) {
		this.childHeaderClickListener = childHeaderClickListener;
	}

	public void setGroupHeaderClickListener(
			GroupHeaderClickListener groupHeaderClickListener) {
		this.groupHeaderClickListener = groupHeaderClickListener;
	}

	private ChildHeaderClickListener childHeaderClickListener;

	public static interface GroupHeaderClickListener {
		public void click();
	}

	public static interface ChildHeaderClickListener {
		public void click(SingleUserWithNotes userWithNotes);
	}

	public static interface ChildItemClickListener {
		public void click(Object object);
	}

	public static class SingleUserNote {

		public final String note;
		public final String status_confirmation;
		public final String status;
		public final String id_note;

		public SingleUserNote(JSONObject jsonObject) throws JSONException {
			this.status_confirmation = jsonObject
					.getString("status_confirmation");
			this.status = jsonObject.getString("status");
			this.note = jsonObject.getString("note");
			this.id_note = jsonObject.getString("id_note");
		}

		public boolean isConfirm() {
			return this.status.equalsIgnoreCase("1");
		}

		public boolean isAdopted() {
			return this.status_confirmation.equalsIgnoreCase("1");
		}
	}

	public void clear() {
		users.clear();
	}

}
