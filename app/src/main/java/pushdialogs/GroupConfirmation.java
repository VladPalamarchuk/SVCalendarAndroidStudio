package pushdialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import bcalendargroups.GroupAssignmentActions;
import bcalendargroups.QueryMaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class GroupConfirmation extends AlertDialog.Builder implements
		QueryMaster.OnCompleteListener {

	private Context context;
	private final String groupID;
	private final String userID;
	private String message;

	private boolean isAccept;

	public GroupConfirmation(Context context, String message, String groupID,
			String userID) {
		super(context);
		this.context = context;
		this.message = message;
		this.groupID = groupID;
		this.userID = userID;
	//	QueryMaster.alert(context, "GroupConfirmation");
	}

	@Override
	public void complete(String serverResponse) {
		//QueryMaster.alert(context, serverResponse);
		try {
			JSONObject json = new JSONObject(serverResponse);
			if (QueryMaster.isSuccess(json)) {

				QueryMaster.toast(context,
						isAccept ? "Now you can see your group"
								: "You have decline group");
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
		isAccept = false;
		try {
			GroupAssignmentActions.confirmGroupParticipation(context, this,
					groupID, userID, "0");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private QueryMaster.OnCompleteListener acceptListener = new QueryMaster.OnCompleteListener() {

		@Override
		public void complete(String serverResponse) {
			//QueryMaster.alert(context, serverResponse);
			try {
				JSONObject json = new JSONObject(serverResponse);
				if (QueryMaster.isSuccess(json)) {
					QueryMaster.toast(context, "Now you can see your group");
					if (onGroupSubmitListener != null) {
						onGroupSubmitListener.submit();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				QueryMaster.alert(context,
						QueryMaster.SERVER_RETURN_INVALID_DATA);
			}
		}

		@Override
		public void error(int errorCode) {
			QueryMaster.alert(context, QueryMaster.ERROR_MESSAGE);
		}
	};

	private void accept() {
		isAccept = true;
		try {
			GroupAssignmentActions.confirmGroupParticipation(context,
					acceptListener, groupID, userID, "1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private OnGroupSubmitListener onGroupSubmitListener;

	public GroupConfirmation setOnGroupSubmitListener(
			OnGroupSubmitListener onGroupSubmitListener) {
		this.onGroupSubmitListener = onGroupSubmitListener;
		return this;
	}

	public static interface OnGroupSubmitListener {
		public void submit();
	}
}
