package palamarchuk.calendarwidget.month;

import java.util.Arrays;

import com.example.calendar.DrawMonthInWidget;
import com.example.calendar.MainActivity;

import com.example.calendar.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class BusinessCalendarWidgetMonth extends AppWidgetProvider {

	final String LOG_TAG = "myLogs";
	DrawMonthInWidget dWidget;
	String SHARED_MONTH = "SHARED_MONTH";
	String SHARED_YEAR = "SHARED_YEAR";
	String SHARED_NOW_MOUNTH = "SHARED_NOW_MOUNTH";
	String SHARED_NOW_YEAR = "SHARED_NOW_YEAR";

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.d(LOG_TAG, "onEnabled");  
  
		SharedPreferences shared = context.getSharedPreferences("shared",
				context.MODE_PRIVATE);

		shared.edit().putInt(SHARED_MONTH, MainActivity.getNowMounth())
				.commit();
		shared.edit().putInt(SHARED_YEAR, MainActivity.getNowYear()).commit();
		enableWidgetClick(context);
		
		
		shared.edit().putInt(SHARED_NOW_MOUNTH, MainActivity.getNowMounth())
		.commit();
shared.edit().putInt(SHARED_NOW_YEAR, MainActivity.getNowYear()).commit();
enableWidgetClick(context);
		
		
		
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		// enableWidgetClick(context);
		// Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
		// // saveWidgetId(context, appWidgetIds[0]);
		//

		SharedPreferences shared = context.getSharedPreferences("shared",
				context.MODE_PRIVATE);

		RemoteViews widgetLayoutRemoveViews = new RemoteViews(
				context.getPackageName(), R.layout.calendar_month_widget);

		// next button click
		Intent intentNext = new Intent(context,
				BusinessCalendarWidgetMonth.class);
		intentNext.setAction(WidgetActions.ActionNext);
		PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context,
				0, intentNext, 0);
		widgetLayoutRemoveViews.setOnClickPendingIntent(
				R.id.calendar_month_widget_next, pendingIntentNext);

		// prev
		Intent intentPrev = new Intent(context,
				BusinessCalendarWidgetMonth.class);
		intentPrev.setAction(WidgetActions.ActionPrevious);
		PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context,
				0, intentPrev, 0);
		widgetLayoutRemoveViews.setOnClickPendingIntent(
				R.id.calendar_month_widget_previous, pendingIntentPrev);

		//
		RemoteViews image = new RemoteViews(context.getPackageName(),
				R.layout.layout_image);
		dWidget = new DrawMonthInWidget(context, shared.getInt(SHARED_YEAR,
				MainActivity.getNowYear()), shared.getInt(SHARED_MONTH,
				MainActivity.getNowMounth()));

		shared.edit().putInt(SHARED_NOW_MOUNTH, dWidget.getMounth())
				.commit();
		shared.edit().putInt(SHARED_NOW_YEAR, dWidget.getYear())
				.commit();

		Bitmap b = getSignatureBitmap(dWidget, convertDpToPixel(context, 500),
				convertDpToPixel(context, 500));
		image.setImageViewBitmap(R.id.layout_image_imageview, b);
		widgetLayoutRemoveViews.removeAllViews(R.id.calendar_widget_container);
		
		
		widgetLayoutRemoveViews.addView(R.id.calendar_widget_container, image);

		appWidgetManager.updateAppWidget(appWidgetIds, widgetLayoutRemoveViews);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.d(LOG_TAG, "onDisabled");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		Log.i("", "onReceive");

		SharedPreferences shared = context.getSharedPreferences("shared",
				context.MODE_PRIVATE);
		int m = shared.getInt(SHARED_NOW_MOUNTH, MainActivity.getNowMounth());
		int y = shared.getInt(SHARED_NOW_YEAR, MainActivity.getNowYear());
		
		
		String action = intent.getAction();
		if (action.equals(WidgetActions.ActionNext)) {
			Log.e("Next", "next clicked");

			if (m == 12) {
				m = 1;
				y++;
			} else
				m++; 

			shared.edit().putInt(SHARED_MONTH, m).commit();
			shared.edit().putInt(SHARED_YEAR, y).commit();
			update(context);
			return;
		} else if (action.equals(WidgetActions.ActionPrevious)) {
			Log.e("Prev", "prev clicked");

			if (m == 1) {
				m = 12;
				y--;
			} else
				m--;
			
			shared.edit().putInt(SHARED_MONTH, m).commit();
			shared.edit().putInt(SHARED_YEAR, y).commit();

			update(context);
			return;
		}

		// Bundle extras = intent.getExtras();
		// if (extras != null) {
		// Log.e("", "extras is null, try update widget");

		// }

		// ComponentName widget = new ComponentName(context,
		// BusinessCalendarWidgetMonth.class);
		// AppWidgetManager.getInstance(context).updateAppWidget(widget, null);

		// updateWidgetFromActivity(context, intent);
	}

	public static interface WidgetActions {
		public static String ActionNext = "actionNext";
		public static String ActionPrevious = "actionPrev";
	}

	// private void saveWidgetId(Context context, int widgetId) {
	// Log.i("", "saveWidgetId -> " + widgetId);
	// SharedPreferences sharedPreferences =
	// context.getSharedPreferences(BusinessCalendarWidgetMonth.class.getName(),
	// Context.MODE_PRIVATE);
	// sharedPreferences.edit().putInt(SharedPrefKeys.WIDGET_ID_KEY,
	// widgetId).commit();
	// }

	private void enableWidgetClick(Context context) {
		Log.i("", "enableWidgetClick");
		RemoteViews widgetLayoutRemoveViews = new RemoteViews(
				context.getPackageName(), R.layout.calendar_month_widget);

		// next button click
		Intent intentNext = new Intent(context,
				BusinessCalendarWidgetMonth.class);
		intentNext.setAction(WidgetActions.ActionNext);
		PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context,
				0, intentNext, 0);
		widgetLayoutRemoveViews.setOnClickPendingIntent(
				R.id.calendar_month_widget_next, pendingIntentNext);

		// prev
		Intent intentPrev = new Intent(context,
				BusinessCalendarWidgetMonth.class);
		intentPrev.setAction(WidgetActions.ActionPrevious);
		PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context,
				0, intentPrev, 0);
		widgetLayoutRemoveViews.setOnClickPendingIntent(
				R.id.calendar_month_widget_previous, pendingIntentPrev);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		int[] appWidgetIds = getAppWidgetIds(context, appWidgetManager);

		// Log.i("", "appWidgetIds -> " + appWidgetIds);

		appWidgetManager.updateAppWidget(appWidgetIds, widgetLayoutRemoveViews);
	}

	private int[] getAppWidgetIds(Context context,
			AppWidgetManager appWidgetManager) {
		ComponentName appWidgetComponentName = new ComponentName(
				context.getPackageName(),
				BusinessCalendarWidgetMonth.class.getName());
		return appWidgetManager.getAppWidgetIds(appWidgetComponentName);
	}

	// private void updateWidgetFromActivity(Context context, Intent intent) {
	// int[] widgetIds =
	// intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
	// Log.i("", "widgetIds = " + widgetIds);
	// if (widgetIds != null) {
	// // update widget from activity :)
	// updateAppWidget(context, AppWidgetManager.getInstance(context),
	// widgetIds);
	// }
	// }

	private void update(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(),
				BusinessCalendarWidgetMonth.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

		onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// Setup onClick
		// Intent widgetIntent = new Intent(context,
		// BusinessCalendarWidgetMonth.class);
		// widgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		// widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
		// appWidgetIds);
		//
		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.calendar_month_widget);
		// remoteView.setTextViewText(R.id.mainText, String.valueOf(nextNumb));
		// remoteView.setOnClickPendingIntent(R.id.mainText,
		// widgetPendingIntent);

		// Tell the widget manager
		appWidgetManager.updateAppWidget(appWidgetIds, remoteView);
	}

	private Bitmap getSignatureBitmap(View drawingView, int width, int height) {
		return loadBitmapFromView(drawingView, width, height);

	}
 
	public static Bitmap loadBitmapFromView(View v, int width, int height) {
		Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	public static int convertDpToPixel(Context context, float dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (int) (dp * (metrics.densityDpi / 160f));

	}

}
