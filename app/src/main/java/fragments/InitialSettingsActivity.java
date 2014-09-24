package fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

//@EActivity(R.layout.activity_settings)
public class InitialSettingsActivity extends FragmentActivity {

	private android.support.v4.app.FragmentManager fragmentManager;

	private int backgroundResourceId = R.drawable.ic_launcher;

	private int FRAGMENT_CONTAINER = R.id.initial_setting_fragment_container;
	public static InitialSettingsActivity activity;
	public static final String FRAGMENT_TITLE = "ft";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_settings);

		fragmentManager = getSupportFragmentManager();
		activity = this;
		ArrayList<String> languages = new ArrayList<String>();
		languages.add("Russian");

		addFragment(InitialSettingFirst
				.getInstance(
						MainActivity.getInstance().language.CHANGE_LANGUAGE_COUNTRY_CITY,
						languages.toArray(new String[languages.size()]),
						languages.toArray(new String[languages.size()])));

	}

	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		try {
			ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
			activityManager.getMemoryInfo(memoryInfo);

			MainActivity.getInstance().startTips();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("class:" + getClass().toString(), "catch:" + e);
		}

	} 

	protected void initFragmentManager() {
		fragmentManager = getSupportFragmentManager();
	}

	public static InitialSettingsActivity getInstance() {
		return activity;
	}

	// protected void addFirstFragment() {
	// addFragment(InitialSettingFirst.getInstance());
	// }

	public void addFragment(Fragment fragment) {
		fragmentManager.beginTransaction()
				.replace(FRAGMENT_CONTAINER, fragment).addToBackStack(null)
				.commit();
	}

	public static ArrayList<String> arrayToArrayList(String[] array) {
		ArrayList<String> result = new ArrayList<String>();
		for (String s : array) {
			result.add(s);
		}
		return result;
	}
}
