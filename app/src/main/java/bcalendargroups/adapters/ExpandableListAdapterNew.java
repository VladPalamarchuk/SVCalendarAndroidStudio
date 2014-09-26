package bcalendargroups.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import bcalendargroups.lazylist.ImageLoader;

public class ExpandableListAdapterNew extends BaseExpandableListAdapter {

    private static final String THIS_IS_YOU = "Это вы";

    private final Context _context;
    private final ArrayList<Object> displayedItems;
    private final ImageLoader imageLoader;

    private final String calendarUserID;

    private View groupHeaderView;

    private boolean showAddTaskButton = false;

    public void setShowAddTaskButton(boolean flag) {
        showAddTaskButton = flag;
    }


    private ChildItemClickListener childItemClickListener;

    public void setChildItemClickListener(ChildItemClickListener childItemClickListener) {
        this.childItemClickListener = childItemClickListener;
    }

    private OnGroupLongClickListener onGroupLongClickListener;

    public void setOnGroupLongClickListener(OnGroupLongClickListener onGroupLongClickListener) {
        this.onGroupLongClickListener = onGroupLongClickListener;
    }

    private OnChildLongClickListener onChildLongClickListener;

    public void setOnChildLongClickListener(OnChildLongClickListener onChildLongClickListener) {
        this.onChildLongClickListener = onChildLongClickListener;
    }


    private OnGroupClickListener onGroupClickListener;

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        this.onGroupClickListener = onGroupClickListener;
    }


    private OnGroupMemberLongClickListener onGroupMemberLongClickListener;

    public void setOnGroupMemberLongClickListener(OnGroupMemberLongClickListener onGroupMemberLongClickListener) {
        this.onGroupMemberLongClickListener = onGroupMemberLongClickListener;
    }

    public ExpandableListAdapterNew(Context context, String calendarUserID) {
        this._context = context;
        this.displayedItems = new ArrayList<Object>();
        this.imageLoader = new ImageLoader(context);
        this.calendarUserID = calendarUserID;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupHeaderView != null)
            groupPosition--;
        if (showAddTaskButton)
            childPosition--;

        return ((SingleUserWithNotes) displayedItems.get(groupPosition)).userNotes.get(childPosition);
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
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
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
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    childItemClickListener.click(userNote);
                }
            });
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onChildLongClickListener != null) {
                    Object o = getGroup(groupPosition);
                    if (o instanceof SingleUserWithNotes) {

                        SingleUserWithNotes userWithNotes = (SingleUserWithNotes) o;
                        final String userID = userWithNotes.id;

                        onChildLongClickListener.longClick(userNote, userID);
                    }
                }
                return false;
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupHeaderView != null)
            groupPosition--;

        final Object object = displayedItems.get(groupPosition);

        if (object instanceof SingleUserWithNotes) {
            return ((SingleUserWithNotes) displayedItems.get(groupPosition)).userNotes.size()
                    + (showAddTaskButton ? 1 : 0);
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupHeaderView != null)
            groupPosition--;

        return displayedItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupHeaderView == null ? displayedItems.size() : displayedItems.size() + 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (groupHeaderView != null && groupPosition == 0) {
            groupHeaderView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (groupHeaderClickListener != null) {
                        groupHeaderClickListener.click();
                    }
                }
            });

            return groupHeaderView;
        }

        final Object object = getGroup(groupPosition);

        if (object instanceof GroupMembersAdapter.GroupMemberItem) {
            // if current object is just group member
            // it has another background and must be inactive

            final GroupMembersAdapter.GroupMemberItem groupMemberItem = (GroupMembersAdapter.GroupMemberItem) object;

            GroupMemberViewHolder holder;

            if (convertView == null || convertView.getTag(R.layout.group_member_item) == null) {

                holder = new GroupMemberViewHolder();

                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.group_member_item, null);

                holder.groupMemberName = (TextView) convertView.findViewById(R.id.groupMemberName);
                holder.groupMemberPhoto = (ImageView) convertView.findViewById(R.id.groupMemberPhoto);

                convertView.setTag(R.layout.group_member_item, holder);
            } else {
                holder = (GroupMemberViewHolder) convertView.getTag(R.layout.group_member_item);
            }

            holder.groupMemberName.setText(groupMemberItem.name);
            imageLoader.DisplayImage(groupMemberItem.photo, holder.groupMemberPhoto);

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onGroupMemberLongClickListener != null) {
                        onGroupMemberLongClickListener.longClick(groupMemberItem);
                    }
                    return false;
                }
            });

            convertView.setBackgroundResource(R.drawable.event_group_member_background);

            return convertView;
        }


        final SingleUserWithNotes userWithNotes = (SingleUserWithNotes) object;
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


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onGroupClickListener != null) {
                    onGroupClickListener.click(groupPosition);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onGroupLongClickListener != null) {
                    onGroupLongClickListener.longClick(userWithNotes);
                    return false;
                }
                return false;
            }
        });

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
                this.displayedItems.add(new SingleUserWithNotes(users.getJSONObject(i)));
            }
            notifyDataSetChanged();
        }
    }

    /**
     * This adapter can show user with notes
     * And just group members, which will not active, but visible
     *
     * @param collection
     */
    public void addGroupMembers(ArrayList<GroupMembersAdapter.GroupMemberItem> collection) {
        if (collection != null) {
            this.removeUnnecessaryItems(collection);
            this.displayedItems.addAll(collection);
            notifyDataSetChanged();
        }
    }


    private final class GroupViewHolder {
        public TextView userTaskItemName;
        public ImageView userTaskItemArrowBottom;
        public ImageView userTaskItemImage;

        public TextView userTaskItemNoteCount;
    }

    private final class GroupMemberViewHolder {
        public TextView groupMemberName;
        public ImageView groupMemberPhoto;
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

    public void setGroupHeaderClickListener(GroupHeaderClickListener groupHeaderClickListener) {
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

    public static interface OnGroupLongClickListener {
        public void longClick(Object o);
    }

    public static interface OnChildLongClickListener {
        public void longClick(Object o, String userID);
    }

    public static interface OnGroupClickListener {
        public void click(int groupPosition);
    }

    public static interface OnGroupMemberLongClickListener {
        public void longClick(GroupMembersAdapter.GroupMemberItem o);
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
        displayedItems.clear();
    }

    private void removeUnnecessaryItems(ArrayList<GroupMembersAdapter.GroupMemberItem> groupMembers) {

        for (Object object : displayedItems) {
            SingleUserWithNotes userWithNote = (SingleUserWithNotes) object;

            for (Iterator<GroupMembersAdapter.GroupMemberItem> iter = groupMembers.iterator(); iter.hasNext(); ) {
                if (iter.next().id.equalsIgnoreCase(userWithNote.id)) {
                    iter.remove();
                }
            }
        }

//        for (Object groupMember : groupMembers) {
//            GroupMembersAdapter.GroupMemberItem groupMemberItem = (GroupMembersAdapter.GroupMemberItem) groupMember;
//
//            for (Iterator<Object> iter = displayedItems.iterator(); iter.hasNext(); ) {
//                SingleUserWithNotesNew userWithNotes = ((SingleUserWithNotesNew) iter.next());
//                if (userWithNotes.id.equalsIgnoreCase(groupMemberItem.id)) {
//                    iter.remove();
//                }
//            }
//        }
    }

}

