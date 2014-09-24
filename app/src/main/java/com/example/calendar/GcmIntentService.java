package com.example.calendar;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	// NotificationCompat.Builder builder;

	public static final String GROUP_CONFIRMATION = "GROUP_CONFIRMATION";

	public static final String GROUP_MESSAGE = "GROUP_CONFIRMATION_MESSAGE";
	public static final String GROUP_CONFIRMATION_ID_GROUP = "GROUP_CONFIRMATION_ID_GROUP";
	public static final String GROUP_CONFIRMATION_ID_EVENT = "GROUP_CONFIRMATION_ID_EVENT";

	public static final String GROUP_CONFIRMATION_ID_NOTE = "GROUP_CONFIRMATION_ID_NOTE";
	public static final String IS_JUST_MESSAGE = "IS_JUST_MESSAGE";
	
	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		Log.e("", "extras.toString() = " + extras.toString());

		if (!extras.isEmpty()) {

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				// sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				// sendNotification("Deleted messages on server: "
				// + extras.toString());

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				sendNotification(extras);
				// Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle extras) {

		Log.e(getClass().toString(), "sendNotification gcmintentservice");

		Intent intent = new Intent(this, MainActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// intent.putExtra("ISPUSH", false);

		String msg = extras.getString("message");
		String type = extras.getString("types");

		if (type.equalsIgnoreCase("groups")) {
			intent.putExtra("ISGROUPEVENT", true);
			
			Log.e(getClass().toString(), "type.equalsIgnoreCase(\"groups\")");
			
			if (extras.containsKey("id_group") && extras.containsKey("is_group_confirm")) {
				
				Log.e(getClass().toString(), "type.equalsIgnoreCase(\"id_group\")");
				
				final String groupID = extras.getString("id_group");
				 
				Log.e(getClass().toString(), "groupID = " + groupID);
				
				intent.putExtra(GROUP_CONFIRMATION, true);
				intent.putExtra(GROUP_MESSAGE, msg);
				intent.putExtra(GROUP_CONFIRMATION_ID_GROUP, groupID);
				
			} 
			 
			if (extras.containsKey("id_event") && extras.containsKey("id_group")
					&& extras.containsKey("is_event_confirm")) {
				
				Log.e(getClass().toString(), "type.equalsIgnoreCase(\"id_event\")");
				
				final String eventID = extras.getString("id_event");
				final String groupID = extras.getString("id_group");
				final String noteID = extras.getString("id_note");
				
				intent.putExtra(GROUP_CONFIRMATION, true);
				intent.putExtra(GROUP_MESSAGE, msg);
				intent.putExtra(GROUP_CONFIRMATION_ID_EVENT, eventID);
				intent.putExtra(GROUP_CONFIRMATION_ID_GROUP, groupID);
				intent.putExtra(GROUP_CONFIRMATION_ID_NOTE, noteID);
			}
			
			if(extras.containsKey("is_just_message")){
				Log.e(getClass().toString(), "is_just_message");
				
				intent.putExtra(GROUP_CONFIRMATION, true);
				intent.putExtra(GROUP_MESSAGE, msg);
				intent.putExtra(IS_JUST_MESSAGE, true);
			}
		}

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.icon)
				.setContentTitle("bCalendar")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		long[] pattern = { 500, 500, 500, 500, 500, 500 };
		mBuilder.setVibrate(pattern);

		mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);

		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
