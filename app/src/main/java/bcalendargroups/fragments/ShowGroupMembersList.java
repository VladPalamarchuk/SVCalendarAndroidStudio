package bcalendargroups.fragments;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.adapters.GroupMembersAdapter;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

public class ShowGroupMembersList  {

	private Context context;
	private String groupId;
	private GroupMembersAdapter groupMembersAdapter;
	private LoadGroupMembers loadGroupMembers = new LoadGroupMembers();
	private AlertDialog alertDialog;

	public ShowGroupMembersList(Context context, String groupId) {
		this.context = context;
		this.groupId = groupId;
	}

	public void show() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		ListView list = new ListView(context);
		groupMembersAdapter = new GroupMembersAdapter(context,
				R.layout.group_member_item);

		list.setAdapter(groupMembersAdapter);

		loadGroupMembers();

		dialog.setView(list);

		dialog.setPositiveButton("Ok", null);

		alertDialog = dialog.show();
	}

	private void loadGroupMembers() {
		try {
			loadGroupMembers.loadGroupMembers(groupId);
		} catch (JSONException e) {
			e.printStackTrace();
			QueryMaster.alert(context,
					MainActivity.getInstance().language.SEARCH_NULL);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public class LoadGroupMembers {

		/**
		 * Load list of members by current group
		 * 
		 * @param groupId
		 *            id of current group
		 * @throws org.json.JSONException
		 * @throws java.io.UnsupportedEncodingException
		 */
		private void loadGroupMembers(String groupId) throws JSONException,
				UnsupportedEncodingException {
			GroupAssignmentActions.getGroupMembers(context, groupId,
					onLoadGroupMembersComplete);
		}

		private QueryMaster.OnCompleteListener onLoadGroupMembersComplete = new QueryMaster.OnCompleteListener() {
			@Override
			public void complete(String serverResponse) {
				try {
					JSONObject jsonObject = new JSONObject(serverResponse);
					if (QueryMaster.isSuccess(jsonObject)) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");

						groupMembersAdapter.clear();
						groupMembersAdapter.add(jsonArray);
					} else {
						alertDialog.dismiss();
						
						QueryMaster.toast(context,
								MainActivity.getInstance().language.NO_MEMBERS);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}

			@Override
			public void error(int errorCode) {

			}
		};
	}
}