package bcalendargroups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import bcalendargroups.pager.GroupEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import palamarchuk.calendarwidget.month.SharedPrefKeys;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import java.io.UnsupportedEncodingException;

public class DrawerListActivity2 {

	private ListView groupTasksList;
	private GroupTaskListAdapter groupTaskListAdapter;

	private RelativeLayout createNewGroup;

	/**
	 * UNIQUE USER ID IN CALENDAR SERVER SYSTEM
	 */
	private String userId;

	private View drawerView;

	public DrawerListActivity2(View drawerView, String userId) {
		this.drawerView = drawerView;
		this.userId = userId;

	}

	public void init() {
		createNewGroup = (RelativeLayout) drawerView
				.findViewById(R.id.createNewGroup);
		createNewGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addGroupDialog();
			}
		});

		groupTasksList = (ListView) drawerView
				.findViewById(R.id.groupTasksList);

		groupTaskListAdapter = new GroupTaskListAdapter(
				MainActivity.getInstance(), R.layout.assignment_list_item);

		groupTaskListAdapter
				.setOnGroupTaskClickListener(new GroupTaskListAdapter.OnGroupTaskClickListener() {
					@Override
					public void onClick(
							GroupTaskListAdapter.AssignmentListItem assignmentListItem) {
						// startSingleGroupAssignmentActivity(assignmentListItem);

						Intent groupEvents = new Intent(getActivity(),
								GroupEvents.class);

						groupEvents.putExtra(GroupEvents.IS_ADMIN,
								assignmentListItem.isAdmin);
						groupEvents.putExtra(GroupEvents.GROUP_ID_BUNDLE,
								assignmentListItem.id);
						groupEvents.putExtra(GroupEvents.CALENDAR_USER_ID,
								userId);
						groupEvents.putExtra(GroupEvents.GROUP_TITLE,
								assignmentListItem.title);

						getActivity().startActivity(groupEvents);

					}
				});

		groupTasksList.setAdapter(groupTaskListAdapter);

		// create group example
		try {
			loadGroups();
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void loadGroups() throws JSONException,
			UnsupportedEncodingException {

		GroupAssignmentActions.getMyGroups(getActivity(), userId,
				new QueryMaster.OnCompleteListener() {
					@Override
					public void complete(String serverResponse) {
						try {
							JSONObject jsonObject = new JSONObject(
									serverResponse);
							if (QueryMaster.isSuccess(jsonObject)) {

								JSONObject groups = jsonObject
										.getJSONObject("groups");

								JSONArray myGroups = !groups.isNull("myGroups") ? groups
										.getJSONArray("myGroups") : null;
								JSONArray groupsMyParticipation = !groups
										.isNull("groupsWithMe") ? groups
										.getJSONArray("groupsWithMe") : null;

								groupTaskListAdapter.clear();

								groupTaskListAdapter
										.add(new GroupTaskListAdapter.GroupTaskDivider(
												MainActivity.getInstance().language.MY_GROUP));
								groupTaskListAdapter.addMyGroups(myGroups);
								groupTaskListAdapter
										.add(new GroupTaskListAdapter.GroupTaskDivider(
												MainActivity.getInstance().language.GROUPS_HAVE_ME));
								groupTaskListAdapter
										.addGroupsMyParticipation(groupsMyParticipation);
								// QueryMaster.alert(getActivity(), "111");
							}

						} catch (JSONException e) {
							e.printStackTrace();
							QueryMaster.alert(
									getActivity(),
									MainActivity.getInstance().language.QUERY_MASTER_INVALID_DATA);
						}
					}

					@Override
					public void error(int errorCode) {
						QueryMaster.alert(
								getActivity(),
								MainActivity.getInstance().language.QUERY_MASTER_INVALID_DATA);
					}
				});
	}

	private void startSingleGroupAssignmentActivity(
			GroupTaskListAdapter.AssignmentListItem assignmentListItem) {
		startSingleGroupAssignmentActivity(assignmentListItem.title,
				assignmentListItem.id);

	}

	private void startSingleGroupAssignmentActivity(String title, String id) {
		// Intent intent = new Intent(MainActivity.getInstance(),
		// SingleGroup.class);
		// intent.putExtra(SingleGroup.BundleKeys.TITLE_BUNDLE_KEY, title);
		// intent.putExtra(SingleGroup.BundleKeys.ID_BUNDLE_KEY, id);
		//
		// MainActivity.getInstance().startActivity(intent);

		Intent i = new Intent(MainActivity.getInstance(), GroupEvents.class);
		i.putExtra(GroupEvents.GROUP_ID_BUNDLE, id);

		MainActivity.getInstance().startActivity(i);

	}

	private Activity getActivity() {
		return MainActivity.getInstance();
	}

	private void addGroupDialog() {
		Context context = getActivity();
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final EditText dialogViewEditText = (EditText) layoutInflater.inflate(
				R.layout.simple_edit_text, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogViewEditText);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				createGroup(dialogViewEditText.getText().toString(), userId);
			}
		});
		builder.setNegativeButton(MainActivity.getInstance().language.CANCEL,
				null);
		builder.show();
	}

	private void createGroup(String title, String adminId) {

		QueryMaster.OnCompleteListener listener = new QueryMaster.OnCompleteListener() {
			@Override
			public void complete(String serverResponse) {
				try {
					loadGroups();
				} catch (JSONException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}

			@Override
			public void error(int errorCode) {
				QueryMaster.alert(getActivity(), "ERROR");
			}
		};
		try {
			GroupAssignmentActions.createGroup(getActivity(), listener, title,
					adminId);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
