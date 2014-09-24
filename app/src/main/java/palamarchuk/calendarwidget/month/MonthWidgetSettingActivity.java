package palamarchuk.calendarwidget.month;

import com.example.calendar.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MonthWidgetSettingActivity extends Activity {

	private Button buttonOK;

	private String LOG_TAG = "WidgetSetting";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_widget_setting);

		Log.d(LOG_TAG, "onCreate config");

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		int widgetID = 0;
		if (extras != null) {
			widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

		setResult(RESULT_OK, resultValue);

		buttonOK = (Button) findViewById(R.id.activity_widget_setting_ok);
		buttonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
 
		// finish();
	}
}
