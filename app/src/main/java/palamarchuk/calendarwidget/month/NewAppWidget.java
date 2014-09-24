package palamarchuk.calendarwidget.month;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Random;

import com.example.calendar.DrawMonthInWidget;
import com.example.calendar.MainActivity;
import com.example.calendar.R;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
	private final static String TAG = "NewAppWidget";
	private static Random rand = new Random();
	public static final String YEAR = "year";
	public static final String MONTH = "month";

	private int mYear;
	private int mMonth;

	@Override
	public void onReceive(Context context, Intent intent) {
		mYear = intent.getIntExtra(YEAR, -1);
		mMonth = intent.getIntExtra(MONTH, -1);
		Log.v(TAG, "onReceive: year=" + mYear + "; month=" + mMonth);
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager mgr,
			int[] appWidgetIds) {
		Log.v(TAG, "onUpdate()");
		ComponentName thisWidget = new ComponentName(context,
				NewAppWidget.class);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.new_app_widget);
		views.removeAllViews(R.id.view_container);

		RemoteViews image = new RemoteViews(context.getPackageName(),
				R.layout.layout_image);
		DrawMonthInWidget dWidget = new DrawMonthInWidget(context, 2014, 9);

		Bitmap b = getSignatureBitmap(dWidget, convertDpToPixel(context, 500),
				convertDpToPixel(context, 500));
		image.setImageViewBitmap(R.id.view_container, b);

		
		
		
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		views.setOnClickPendingIntent(R.id.widget_button_left, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_button_right, pendingIntent);

		mgr.updateAppWidget(thisWidget, views);
	}

	public static int convertDpToPixel(Context context, float dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (int) (dp * (metrics.densityDpi / 160f));

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
}
