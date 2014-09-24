package palamarchuk.calendarwidget.month;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

/**
 * Implementation of App Widget functionality. App Widget Configuration
 * implemented in {@link OneByOneWidgetConfigureActivity
 * OneByOneWidgetConfigureActivity}
 */
public class OneByOneWidget extends AppWidgetProvider {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd");

	@Override
	public void onEnabled(Context context) {
		// Setting alarm to update at 00:00
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		// Log.v("XXX", sdf.format(calendar.getTime()));
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		// Log.v("XXX", sdf.format(calendar.getTime()));

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int[] widgetIds = AppWidgetManager.getInstance(
				context.getApplicationContext()).getAppWidgetIds(
				new ComponentName(context.getApplicationContext(),
						OneByOneWidget.class));
		Intent i = new Intent(context, OneByOneWidget.class);
		i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);

		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setInexactRepeating(AlarmManager.RTC,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// Disable alarm
		// Log.v("XXX", "onDeleted()");
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int[] widgetIds = AppWidgetManager.getInstance(
				context.getApplicationContext()).getAppWidgetIds(
				new ComponentName(context.getApplicationContext(),
						OneByOneWidget.class));
		Intent i = new Intent(context, OneByOneWidget.class);
		i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);

		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pi);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		Log.v("XXX", "Updated");
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
		}
	}

	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		try {

			// CharSequence widgetText =
			// OneByOneWidgetConfigureActivity.loadTitlePref(context,
			// appWidgetId);
			// Construct the RemoteViews object
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.one_by_one_widget);
			// Date d = new Date();
			Calendar calendar = Calendar.getInstance();
			views.setTextViewText(R.id.date_text_view,
					dateFormat.format(calendar.getTime()));
			views.setTextViewText(R.id.event_text_view, String
					.valueOf(MainActivity.getInstance().getCountEventToday(
							context, calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH))));
			// views.setTextViewText(R.id.appwidget_text, widgetText);

			// Instruct the widget manager to update the widget
			appWidgetManager.updateAppWidget(appWidgetId, views);

		} catch (Exception e) {
			// TODO: handle exception

		}
	}
}
