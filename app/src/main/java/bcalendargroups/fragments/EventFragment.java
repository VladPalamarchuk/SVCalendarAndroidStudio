package bcalendargroups.fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bcalendargroups.AddEventComment;
import bcalendargroups.FragmentHelper;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.SimpleEditText;
import bcalendargroups.adapters.EventCommentsAdapter;
import bcalendargroups.adapters.ExpandableListAdapterNew;
import bcalendargroups.adapters.GroupMembersAdapter;
import bcalendargroups.pager.GroupEvents;
import bcalendargroups.pager.SingleEvent;
import dialogs.CreateEvent;
import dialogs.MyGroupTasks;
import dialogs.MyGroupTasks.OnScreenUpdateListener;
import dialogs.NoteConfigurationDialog;

@EFragment(R.layout.single_event_fragment)
@OptionsMenu(R.menu.event_fragment)
public class EventFragment extends Fragment implements
        QueryMaster.OnCompleteListener, EventCommentsAdapter.OnClickListener,
        OnScreenUpdateListener {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String SINGLE_EVENT_KEY = "sek";

    @ViewById(R.id.singleEventFragmentUserTasksExpandableListView)
    protected ExpandableListView userTasksExpandableListView;

    @ViewById(R.id.singleEventCommentListView)
    protected ListView singleEventCommentListView;

    @ViewById(R.id.content)
    protected View contentView;

    private String eventId;

    private EventCommentsAdapter commentsAdapter;

    private ExpandableListAdapterNew expandableListAdapter;

    private Activity activity;

    private MyGroupTasks myGroupTasks;

    /**
     * Returns a new instance of this fragment for the given section number.
     */

    public static Fragment newInstance(SingleEvent singleEvent) {
        EventFragment fragment = new EventFragment_();
        Bundle args = new Bundle();
        args.putSerializable(SINGLE_EVENT_KEY, singleEvent);
        fragment.setArguments(args);
        return fragment;
    }

//    @OptionsItem(R.id.add_members_to_group)
//    protected void addUserToGroup() {
//        String groupId = ((GroupEvents) getParentActivity()).getGroupId();
//        new SearchPeople(getParentActivity(), groupId).searchPeopleDialog();
//    }

//    @OptionsItem(R.id.add_members_to_event)
//    protected void addUserToEvent() {
//        new EventMembersNotesAdapter(getParentActivity(),
//                ((GroupEvents) getParentActivity()).getGroupId(), eventId)
//                .getDialog();
//    }

//    @OptionsItem(R.id.show_list_members_group)
//    protected void showListMembersGroup() {
//        new ShowGroupMembersList(getParentActivity(),
//                ((GroupEvents) getParentActivity()).getGroupId()).show();
//
//    }

    @OptionsItem(R.id.create_event)
    protected void createEvent() {
        final String groupId = ((GroupEvents) getParentActivity()).getGroupId();
        new CreateEvent(((GroupEvents) getParentActivity()), groupId)
                .showDialog();
    }

    @OptionsItem(R.id.show_list_my_tasks)
    protected void showMyTasks() {
        if (myGroupTasks == null) {
            final String groupID = ((GroupEvents) getParentActivity())
                    .getGroupId();
            myGroupTasks = new MyGroupTasks(activity,
                    String.valueOf(MainActivity.getInstance().USER.getId()),
                    groupID);
        }
        myGroupTasks.show();
    }

    @OptionsItem(R.id.action_group_setting)
    protected void groupSetting() {
        ArrayList<GroupMembersAdapter.GroupMemberItem> groupMemberItems = ((GroupEvents) getActivity()).getGroupMembers();
        String groupID = ((GroupEvents) getActivity()).getGroupId();
        String groupTitle = ((GroupEvents) getActivity()).getGroupTitle();
        GroupEvents.startGroupSettingActivity(getActivity(), groupID, groupTitle, groupMemberItems);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        ((GroupEvents) activity).isAdmin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (expandableListAdapter == null) {
            expandableListAdapter = new ExpandableListAdapterNew(getParentActivity(),
                    ((GroupEvents) getActivity()).getCalendarUserID());

            setUserPermission();
        }

        if (commentsAdapter == null) {
            commentsAdapter = new EventCommentsAdapter(getParentActivity(),
                    R.layout.event_comment_item);
            commentsAdapter.setOnClickListener(this);
        }

        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (((GroupEvents) activity).isAdmin()) {
            menu.findItem(R.id.show_list_my_tasks).setVisible(false);
        } else {
            menu.findItem(R.id.create_event).setVisible(false);
//            menu.findItem(R.id.add_members_to_group).setVisible(false);
//            menu.findItem(R.id.add_members_to_event).setVisible(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle args = getArguments();
        SingleEvent singleEvent = (SingleEvent) args
                .getSerializable(SINGLE_EVENT_KEY);
        eventId = singleEvent.id;

        try {
            String groupId = ((GroupEvents) getParentActivity()).getGroupId();
            GroupAssignmentActions.getEventInfo(getParentActivity(), this,
                    eventId, groupId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @AfterViews
    protected void afterViews() {
        userTasksExpandableListView.setAdapter(expandableListAdapter);
        singleEventCommentListView.setAdapter(commentsAdapter);

        Drawable translucentBG = prepareTranslucentBackground();
        contentView.setBackgroundDrawable(translucentBG);
    }

    @Override
    public void complete(String serverResponse) {
        // QueryMaster.alert(getParentActivity(), serverResponse);
        try {
            JSONObject jsonObject = new JSONObject(serverResponse);
            parseData(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            QueryMaster.alert(getParentActivity(),
                    QueryMaster.SERVER_RETURN_INVALID_DATA);
        }

    }

    @Override
    public void error(int errorCode) {
        QueryMaster.alert(getParentActivity(), QueryMaster.ERROR_MESSAGE);
    }

    private void parseData(JSONObject jsonObject) throws JSONException {
        if (QueryMaster.isSuccess(jsonObject)) {

            JSONObject data = jsonObject.getJSONObject("data");

            JSONObject eventInfo = !data.isNull("event_info") ? data
                    .getJSONObject("event_info") : null;

            JSONArray users = !data.isNull("users") ? data
                    .getJSONArray("users") : null;
            JSONArray comment = !data.isNull("comment") ? data
                    .getJSONArray("comment") : null;

            commentsAdapter.clear();
            commentsAdapter.add(comment);

            expandableListAdapter.clear();
            expandableListAdapter.notifyDataSetChanged();

//			expandableListAdapter.setGroupHeaderView(getHeaderView(
//					getParentActivity(), eventInfo));

            // pizda polnaia

            // only admin can add user tasks


            expandableListAdapter.add(users);

            final ArrayList<GroupMembersAdapter.GroupMemberItem> groupMemberItems =
                    ((GroupEvents) getActivity()).getGroupMembers();

            expandableListAdapter.addGroupMembers(groupMemberItems);

        } else {
            QueryMaster.alert(getParentActivity(),
                    getString(R.string.data_not_found));
        }
    }

    @Override
    public void addComment() {
        String userId = String.valueOf(MainActivity.getInstance().USER.getId());
        new AddEventComment(getParentActivity(), userId, eventId).showDialog();

    }

    private void setUserPermission() {
        if (((GroupEvents) getParentActivity()).isAdmin()) {

            expandableListAdapter.setShowAddTaskButton(true);
            expandableListAdapter
                    .setChildHeaderClickListener(new ExpandableListAdapterNew.ChildHeaderClickListener() {

                        @Override
                        public void click(ExpandableListAdapterNew.SingleUserWithNotes userWithNotes) {
                            // click ADD TASK in childView
                            new SimpleEditText(
                                    (GroupEvents) getParentActivity(),
                                    eventId + "", userWithNotes.id).show();
                        }
                    });

            expandableListAdapter.setOnGroupLongClickListener(new ExpandableListAdapterNew.OnGroupLongClickListener() {
                @Override
                public void longClick(Object o) {
                    final ExpandableListAdapterNew.SingleUserWithNotes userWithNotes = (ExpandableListAdapterNew.SingleUserWithNotes) o;
                    final String userID = userWithNotes.id;

                    final String message = "Remove user from event?";

                    QueryMaster.question(getParentActivity(), message,
                            "Yes", new RemoveUserFromEvent(eventId, userID), "Cancel", null);
                }
            });

            expandableListAdapter.setOnGroupClickListener(new ExpandableListAdapterNew.OnGroupClickListener() {
                @Override
                public void click(int groupPosition) {
                    if (userTasksExpandableListView.isGroupExpanded(groupPosition)) {
                        userTasksExpandableListView.collapseGroup(groupPosition);
                    } else {
                        userTasksExpandableListView.expandGroup(groupPosition);
                    }
                }
            });

            expandableListAdapter.setOnChildLongClickListener(new ExpandableListAdapterNew.OnChildLongClickListener() {
                @Override
                public void longClick(Object o, String userID) {
                    final String groupID = ((GroupEvents) getParentActivity()).getGroupId();

                    final NoteConfigurationDialog configurationDialog = new NoteConfigurationDialog(getParentActivity(),
                            (ExpandableListAdapterNew.SingleUserNote) o, userID, groupID);

                    configurationDialog.setScreenUpdateListener(screenUpdateListener);
                    configurationDialog.show();
                }
            });

            expandableListAdapter.setOnGroupMemberLongClickListener(new ExpandableListAdapterNew.OnGroupMemberLongClickListener() {
                @Override
                public void longClick(GroupMembersAdapter.GroupMemberItem groupMemberItem) {

                    final String groupID = ((GroupEvents) getParentActivity()).getGroupId();
                    final String eventID = eventId;
                    final String userID = groupMemberItem.id;

                    final String message = "Are you want add user to event?";

                    QueryMaster.question(getParentActivity(), message, "Yes",
                            new AddUserToEvent(eventID, userID, groupID), "No", null);
                }
            });

        } else {
            // not admin
            final String groupID = ((GroupEvents) getParentActivity())
                    .getGroupId();

            myGroupTasks = new MyGroupTasks(
                    activity,
                    String.valueOf(MainActivity.getInstance().USER.getId()),
                    groupID);
            myGroupTasks.setOnScreenUpdateListener(this);
            myGroupTasks.load();

            // user can confirm task directly here
            expandableListAdapter
                    .setChildItemClickListener(new ExpandableListAdapterNew.ChildItemClickListener() {

                        @Override
                        public void click(Object object) {
                            // if this task is owned by user
                            // user can confirm it here
                            ExpandableListAdapterNew.SingleUserNote userNote = (ExpandableListAdapterNew.SingleUserNote) object;
                            if (myGroupTasks.getAdapter().containsMyTask(userNote.id_note)) {

                                myGroupTasks.new ConfirmThisTask(userNote,
                                        eventId).showDialog();
                            }
                        }
                    });
        }

    }

    private NoteConfigurationDialog.ScreenUpdateListener screenUpdateListener = new NoteConfigurationDialog.ScreenUpdateListener() {
        @Override
        public void update() {
            FragmentHelper.updateFragment(getFragmentManager());
        }
    };


    private class AddUserToEvent implements View.OnClickListener {

        private final String eventID;
        private final String userID;
        private final String groupID;

        private AddUserToEvent(String eventID, String userID, String groupID) {
            this.eventID = eventID;
            this.userID = userID;
            this.groupID = groupID;
        }

        @Override
        public void onClick(View view) {
            try {
                GroupAssignmentActions.addPeopleToEventInGroup(getParentActivity(), this.userID, this.groupID, this.eventID, onCompleteListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        private QueryMaster.OnCompleteListener onCompleteListener = new QueryMaster.OnCompleteListener() {
            @Override
            public void complete(String serverResponse) {
                final String message = "Participation request was send to user";
                QueryMaster.alert(getParentActivity(), message);
//                FragmentHelper.updateFragment(getFragmentManager());
            }

            @Override
            public void error(int errorCode) {
                QueryMaster.alert(getParentActivity(), QueryMaster.ERROR_MESSAGE);
            }
        };
    }

    private class RemoveUserFromEvent implements View.OnClickListener {

        private final String eventID;
        private final String userID;

        private RemoveUserFromEvent(String eventID, String userID) {
            this.eventID = eventID;
            this.userID = userID;
        }

        @Override
        public void onClick(View view) {
            try {
                GroupAssignmentActions.removeUserFromEvent(getParentActivity(), eventID, userID, onCompleteListener);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        private QueryMaster.OnCompleteListener onCompleteListener = new QueryMaster.OnCompleteListener() {
            @Override
            public void complete(String serverResponse) {
                FragmentHelper.updateFragment(getFragmentManager());
            }

            @Override
            public void error(int errorCode) {
                QueryMaster.alert(getParentActivity(), QueryMaster.ERROR_MESSAGE);
            }
        };
    }

    private final QueryMaster.OnCompleteListener commentComplete = new QueryMaster.OnCompleteListener() {
        @Override
        public void complete(String serverResponse) {
            try {
                JSONObject json = new JSONObject(serverResponse);
                if (QueryMaster.isSuccess(json)) {
                    QueryMaster.alert(getParentActivity(),
                            getString(R.string.comment_was_send));
                } else {
                    QueryMaster.alert(getParentActivity(),
                            getString(R.string.comment_was_not_sended));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                QueryMaster.alert(getParentActivity(),
                        QueryMaster.SERVER_RETURN_INVALID_DATA);
            }
        }

        @Override
        public void error(int errorCode) {
            QueryMaster.alert(getParentActivity(), QueryMaster.ERROR_MESSAGE);
        }
    };

    private Drawable prepareTranslucentBackground() {
        Drawable commentBG = getResources().getDrawable(
                R.drawable.comment_background);
//        commentBG.setAlpha(240);
        return commentBG;
    }

    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

//	public View getHeaderView(Context context, JSONObject obj) {
//
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//
//		View root = (ViewGroup) inflater.inflate(R.layout.one_event_fragment,
//				null);
//
//		root.findViewById(R.id.one_event_fragment_top).setVisibility(View.GONE);
//		OneEvent e = new OneEvent(0, "", Color.RED, 1, 1, 1, 1, 1, 1, 1, 1, 1,
//				1, "1", "33345", "", 0, "", "", "", 0, "");
//		// QueryMaster.alert(context, obj.toString());
//		try {
//
//			e = new OneEvent(0, obj.getString("title"), Integer.parseInt(obj
//					.getString("color")), Integer.parseInt(obj
//					.getString("start_date_year")), Integer.parseInt(obj
//					.getString("start_date_mounth")), Integer.parseInt(obj
//					.getString("start_date_day")), Integer.parseInt(obj
//					.getString("start_date_hour")), Integer.parseInt(obj
//					.getString("start_date_minute")), Integer.parseInt(obj
//					.getString("start_date_year")), Integer.parseInt(obj
//					.getString("start_date_mounth")), Integer.parseInt(obj
//					.getString("start_date_day")), Integer.parseInt(obj
//					.getString("start_date_hour")), Integer.parseInt(obj
//					.getString("start_date_minute")),
//					obj.getString("category"), obj.getString("location"),
//					obj.getString("info"), 0, "", obj.getString("time_push"),
//					"", 0, "");
//
//		} catch (Exception e2) {
//			// QueryMaster.alert(context, e2.toString());
//
//		}
//
//		((TextView) root.findViewById(R.id.one_event_fragment_start_date_label))
//				.setText(MainActivity.getInstance().language.ONE_EVENT_START_DATE_LABEL);
//		((TextView) root.findViewById(R.id.one_event_fragment_end_date_label))
//				.setText(MainActivity.getInstance().language.ONE_EVENT_END_DATE_LABEL);
//
//		String start = "";
//		String end = "";
//
//		if (MainActivity.getInstance().shared.getBoolean(
//				MainActivity.SHARED_IS_AMPM, false)) {
//
//			if (e.getStart_date_hour() > 12) {
//				start = e.getStart_date_year() + "-"
//						+ MainActivity.FormatDate(e.getStart_date_mounth())
//						+ "-" + MainActivity.FormatDate(e.getEnd_date_day())
//						+ " "
//						+ MainActivity.FormatDate(e.getStart_date_hour() - 12)
//						+ ":"
//						+ MainActivity.FormatDate(e.getStart_date_minute())
//						+ " PM";
//			} else {
//				start = e.getStart_date_year() + "-"
//						+ MainActivity.FormatDate(e.getStart_date_mounth())
//						+ "-" + MainActivity.FormatDate(e.getEnd_date_day())
//						+ " " + MainActivity.FormatDate(e.getStart_date_hour())
//						+ ":"
//						+ MainActivity.FormatDate(e.getStart_date_minute())
//						+ " AM";
//			}
//
//			if (e.getEnd_date_hour() > 12) {
//				end = e.getEnd_date_year() + "-"
//						+ MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
//						+ MainActivity.FormatDate(e.getEnd_date_day()) + " "
//						+ MainActivity.FormatDate(e.getEnd_date_hour() - 12)
//						+ ":" + MainActivity.FormatDate(e.getEnd_date_minute())
//						+ " PM";
//			} else {
//				end = e.getEnd_date_year() + "-"
//						+ MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
//						+ MainActivity.FormatDate(e.getEnd_date_day()) + " "
//						+ MainActivity.FormatDate(e.getEnd_date_hour()) + ":"
//						+ MainActivity.FormatDate(e.getEnd_date_minute())
//						+ " AM";
//			}
//
//		} else {
//
//			start = e.getStart_date_year() + "-"
//					+ MainActivity.FormatDate(e.getStart_date_mounth()) + "-"
//					+ MainActivity.FormatDate(e.getEnd_date_day()) + " "
//					+ MainActivity.FormatDate(e.getStart_date_hour()) + ":"
//					+ MainActivity.FormatDate(e.getStart_date_minute());
//
//			end = e.getEnd_date_year() + "-"
//					+ MainActivity.FormatDate(e.getEnd_date_mounth()) + "-"
//					+ MainActivity.FormatDate(e.getEnd_date_day()) + " "
//					+ MainActivity.FormatDate(e.getEnd_date_hour()) + ":"
//					+ MainActivity.FormatDate(e.getEnd_date_minute());
//
//		}
//
//		((TextView) root.findViewById(R.id.one_event_fragment_start_date))
//				.setText(start);
//		((TextView) root.findViewById(R.id.one_event_fragment_end_date))
//				.setText(end);
//		Catigories c = new Catigories(Integer.parseInt(e.getCategory()),
//				(ImageView) root
//						.findViewById(R.id.one_event_fragment_category_image));
//
//		c.setImage();
//		((TextView) root.findViewById(R.id.one_event_fragment_title)).setText(e
//				.getTitle());
//
//		((TextView) root.findViewById(R.id.one_event_fragment_location))
//				.setText(MainActivity.getInstance().getLocationById(
//						MainActivity.getInstance().CITIES,
//						Integer.parseInt(e.getLocation())));
//
//		((TextView) root.findViewById(R.id.one_event_fragment_info)).setText(e
//				.getInfo());
//
//		((RelativeLayout) root.findViewById(R.id.one_event_fragment_color_rel))
//				.setBackgroundColor(e.getColor());
//
//		if (e.getInfo().length() > 0)
//			((TextView) root.findViewById(R.id.one_event_fragment_info))
//					.setVisibility(View.VISIBLE);
//
//		if (e.getFile_path().length() == 0) {
//			root.findViewById(R.id.one_event_fragment_file_image)
//					.setVisibility(View.GONE);
//		}
//		return root;
//	}

    public Activity getParentActivity() {
        return activity;
    }

    @Override
    public void updateScreen() {
        ((GroupEvents) getParentActivity()).updateAdapter();
    }

}
