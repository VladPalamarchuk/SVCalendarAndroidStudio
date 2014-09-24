package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

import com.example.calendar.AddEventFragment;
import com.example.calendar.MainActivity;
import com.example.calendar.PlaySoundButton;
import com.example.calendar.R;
import com.example.calendar.SyncGoogleCalendar;
//import com.ipaulpro.afilechooser.utils.FileUtils;

public class InitialSettingSecond extends Fragment implements
		View.OnClickListener {
	View root;

	ImageView google_calendar;
	ImageView microsoft;

	public static InitialSettingSecond getInstance() {
		return new InitialSettingSecond();
	}

	private InitialSettingsActivity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (InitialSettingsActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.initial_setting_second, null);
		root.findViewById(R.id.initial_setting_next).setOnClickListener(this);
		google_calendar = (ImageView) root
				.findViewById(R.id.initial_settings_google_calendar);
		microsoft = (ImageView) root
				.findViewById(R.id.initial_settings_microsft);

		google_calendar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				new SyncGoogleCalendar(activity).showSyncDialog();

			}
		});

		microsoft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.getInstance().startMicrosoft(); 
			}
		});

		setText();
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.initial_setting_next:
			nextClick();
			break;
		}
	}

	// @Click(R.id.initial_setting_next)
	protected void nextClick() {
		activity.addFragment(InitialSettingThird.getInstance());
	}

	public void setText() {

		((TextView) root.findViewById(R.id.initial_setting_preview_text))
				.setText(MainActivity.getInstance().language.SYNC_SERVICE);

	}
}
