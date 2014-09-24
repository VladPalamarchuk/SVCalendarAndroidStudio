package pushdialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EventConfirmation extends AlertDialog.Builder implements
		QueryMaster.OnCompleteListener {

	private final Context context;
	private final String groupID;
	private final String eventID;
	private final String message;
	private final String userID;
	private final String noteID;

	private Bundle bundle;

	public EventConfirmation(Context context, String groupID, String eventID,
			String noteID, String message, String userID) {
		super(context);
		this.context = context;
		this.groupID = groupID;
		this.eventID = eventID;
		this.message = message;
		this.userID = userID;
		this.noteID = noteID;

		QueryMaster.alert(context, "EventConfirmation");
	}

	@Override
	public void complete(String serverResponse) {
	//	QueryMaster.alert(context, serverResponse);
		try {
			JSONObject json = new JSONObject(serverResponse);
			if (QueryMaster.isSuccess(json)) {
				QueryMaster.toast(context, "Now you can see your event");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			QueryMaster.alert(context, QueryMaster.SERVER_RETURN_INVALID_DATA);
		}
	}

	@Override
	public void error(int errorCode) {
		QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
	}

	@Override
	public AlertDialog show() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle("Confirm action");
		/**
		 * positive handler beside
		 */
		builder.setPositiveButton("Ok", null);
		builder.setNegativeButton("Cancel", null);

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						accept();
						alertDialog.dismiss();
					}
				});
		alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						decline();
						alertDialog.dismiss();
					}
				});

		return null;
	}

	private void decline() {
		try {
			GroupAssignmentActions.confirmEventParticipation(context, this,
					groupID, eventID, userID, noteID, "-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void accept() {
		try {
			GroupAssignmentActions.confirmEventParticipation(context, this,
					groupID, eventID, userID, noteID, "1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
