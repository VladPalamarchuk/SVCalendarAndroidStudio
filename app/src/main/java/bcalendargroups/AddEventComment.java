package bcalendargroups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import bcalendargroups.pager.GroupEvents;

import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.example.calendar.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventComment {

	private Context context;
	private String eventID;
	private String userID;

	public AddEventComment(Context context, String userID, String eventID) {
		this.context = context;
		this.eventID = eventID;
		this.userID = userID;
	}

	/**
	 * Add comment dialog
	 */

	public void showDialog() {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final EditText dialogView = (EditText) layoutInflater.inflate(
				R.layout.simple_edit_text, null);
		dialogView.setHint("Enter text comment");

		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(dialogView);
		builder.setPositiveButton("Ok", null);
		builder.setNegativeButton("Cancel", null);

		final AlertDialog alertDialog = builder.create();
		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							String message = dialogView.getText().toString();
							if (!message.isEmpty()) {
								addCommentEvent(message);

								alertDialog.dismiss();
							} else {
								ShakeAnimator animator = new ShakeAnimator();
								animator.animate(dialogView);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							QueryMaster.alert(context,
									QueryMaster.SERVER_RETURN_INVALID_DATA);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
					}
				});
	}

	/**
	 * handle adding comment to event
	 */
	private QueryMaster.OnCompleteListener addCommentComplete = new QueryMaster.OnCompleteListener() {
		@Override
		public void complete(String serverResponse) {
			try {
				JSONObject json = new JSONObject(serverResponse);
				if (QueryMaster.isSuccess(json)) {
					if (context instanceof GroupEvents) {
						FragmentHelper.updateFragment(((GroupEvents) context)
								.getSupportFragmentManager());
						GroupEvents.hideKeyboard((GroupEvents) context);

						((GroupEvents) context).getSectionsPagerAdapter()
								.notifyDataSetChanged();

					}
					// QueryMaster.toast(context, R.string.comment_was_send);
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

	private void addCommentEvent(String message) throws JSONException,
			UnsupportedEncodingException {
		GroupAssignmentActions.addEventComment(context, addCommentComplete,
				eventID, userID, message, getNowDate());
	}

	public static String getNowDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
}
