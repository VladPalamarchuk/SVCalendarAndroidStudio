package dialogs;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.GroupsEventDialog;
import com.example.calendar.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;
import bcalendargroups.pager.GroupEvents;

public class CreateEvent extends AlertDialog.Builder implements
		QueryMaster.OnCompleteListener {

	private GroupsEventDialog dialogGroup;
	private String groupId;
	private GroupEvents groupEvents;

	public CreateEvent(GroupEvents arg0, String groupId) {
		super(arg0);
		this.groupEvents = arg0;
		this.groupId = groupId;
	}

	@Override
	public void complete(String serverResponse) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(serverResponse);
			if (QueryMaster.isSuccess(jsonObject)) {

				sendUpdateMessage(groupEvents);
				
				QueryMaster.toast(getContext(), "added");
			} else {
				QueryMaster.alert(getContext(), "Event was not created");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			QueryMaster.alert(getContext(),
					QueryMaster.SERVER_RETURN_INVALID_DATA);
		}

	}

	@Override
	public void error(int errorCode) {
		// TODO Auto-generated method stub
		QueryMaster.toast(getContext(), "error");
	}

	public void showDialog() {

		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View dialogView = layoutInflater.inflate(R.layout.add_event_dialog,
		// null);

		dialogGroup = new GroupsEventDialog(getContext());
		View dialogView = dialogGroup.GetView();

		final EditText addTaskName = (EditText) dialogView
				.findViewById(R.id.addTaskName);
		final EditText addTaskDescription = (EditText) dialogView
				.findViewById(R.id.addTaskDescription);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setView(dialogView);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// String taskName = addTaskName.getText().toString();
				// String taskDescription =
				// addTaskDescription.getText().toString();
				try {
					add();
				} catch (JSONException e) {
					e.printStackTrace();
					QueryMaster.alert(getContext(),
							QueryMaster.SERVER_RETURN_INVALID_DATA);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();
	}

	private void sendUpdateMessage(GroupEvents groupEvents) {
		try {
			groupEvents.getSectionsPagerAdapter().update(groupId);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void add() throws JSONException, UnsupportedEncodingException {

		GroupAssignmentActions.groupEventJSON(getContext(), this,
				dialogGroup.getTitle(), dialogGroup.getColor(),
				dialogGroup.getStartDateYear(),
				dialogGroup.getStartDateMonth(), dialogGroup.getStartDateDay(),
				dialogGroup.getStartDateHour(),
				dialogGroup.getStartDateMinute(), dialogGroup.getEndDateYear(),
				dialogGroup.getEndDateMonth(), dialogGroup.getEndDateDay(),
				dialogGroup.getEndDateHour(), dialogGroup.getEndDateMinute(),
				dialogGroup.getPushTime(), dialogGroup.getCategory(),
				dialogGroup.getLocation(), dialogGroup.getInfo(), "", groupId);

	}

}
