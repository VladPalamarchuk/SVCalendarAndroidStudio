package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.calendar.AddEventFragment;
import com.example.calendar.Languages;
import com.example.calendar.MainActivity;
import com.example.calendar.OneCity;
import com.example.calendar.ParseCity;
import com.example.calendar.PlaySoundButton;
import com.example.calendar.R;
import com.ipaulpro.afilechooser.utils.FileUtils;

@SuppressLint("ValidFragment")
public class InitialSettingFirst extends Fragment implements
		View.OnClickListener {

	View root;
	public final static int ADD_ALARM_SOUND_KEY = 12345;
	private Spinner soundSpinner;
	private Button alarmSoundButton;
	private Spinner timeFormatSpinner;
	CheckBox ch1, ch2, ch3, ch4, ch5, ch6, ch7;

	private static final String LANGUAGES_BUNDLE_KEY = "lang";
	private static final String COUNTRIES_BUNDLE_KEY = "countries";

	private Spinner languagesSpinner;

	public static InitialSettingFirst getInstance(String title,
			String[] languages, String[] countries) {
		InitialSettingFirst fragment = new InitialSettingFirst();

		Bundle bundle = new Bundle();
		bundle.putStringArray(LANGUAGES_BUNDLE_KEY, languages);
		bundle.putStringArray(COUNTRIES_BUNDLE_KEY, countries);
		bundle.putString(InitialSettingsActivity.FRAGMENT_TITLE, title);

		fragment.setArguments(bundle);
		return fragment;
	}

	private InitialSettingsActivity activity;

	private String title;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (InitialSettingsActivity) activity;

		this.title = getArguments().getString(
				InitialSettingsActivity.FRAGMENT_TITLE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.initial_setting_first, null);

		root.findViewById(R.id.initial_setting_next).setOnClickListener(this);

		languagesSpinner = (Spinner) root
				.findViewById(R.id.initial_setting_first_choose_lang_spinner);

		initLangSpinner();
		// initCountriesSpinner();
		// initCitySpinner();

		soundSpinner = (Spinner) root
				.findViewById(R.id.initial_setting_sound_spinner);
		alarmSoundButton = (Button) root
				.findViewById(R.id.initial_setting_second_alarm_sound_button);

		ch1 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_monday_checkbox);
		ch2 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_tuesday_checkbox);
		ch3 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_wednesday_checkbox);
		ch4 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_thursday_checkbox);
		ch5 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_friday_checkbox);
		ch6 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_saturday_checkbox);
		ch7 = (CheckBox) root
				.findViewById(R.id.initial_setting_forth_sunday_checkbox);

		ch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});

		ch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});

		ch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});

		ch4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});

		ch5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});

		ch6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});
		ch7.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedCheckedDays();
			}
		});

		timeFormatSpinner = (Spinner) root
				.findViewById(R.id.initial_setting_third_time_format_spinner);

		ArrayList<String> timeFormats = new ArrayList<String>();
		timeFormats.add("AM/PM");
		timeFormats.add("24 " + MainActivity.getInstance().language.HOURS);

		SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity,
				android.R.layout.simple_spinner_item,
				android.R.layout.simple_spinner_dropdown_item, timeFormats);

		timeFormatSpinner.setAdapter(spinnerAdapter);

		timeFormatSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub

						boolean isChecked = false;

						if (position == 0)
							isChecked = true;

						MainActivity.getInstance().shared
								.edit()
								.putBoolean(MainActivity.SHARED_IS_AMPM,
										isChecked).commit();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		initSoundSpinner();
		initAlarmSound();
		setText();
		return root;
	}

	protected void initLangSpinner() {
		String[] languages = getArguments()
				.getStringArray(LANGUAGES_BUNDLE_KEY);

		List<String> langArray = InitialSettingsActivity
				.arrayToArrayList(languages);

		SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity,
				android.R.layout.simple_spinner_item,
				android.R.layout.simple_spinner_dropdown_item,
				(ArrayList<String>) langArray);
		languagesSpinner.setAdapter(spinnerAdapter);
		languagesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub

						MainActivity.shared
								.edit()
								.putInt(MainActivity.SHARED_LANGUAGE,
										position + 1).commit();
						MainActivity.getInstance().language = new Languages(
								position + 1);

						setText();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
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
		activity.addFragment(InitialSettingFive.getInstance());
	}

	public String getTitle() {
		return title;
	}

	public void setText() {

		((TextView) root.findViewById(R.id.initial_setting_preview_text))
				.setText(MainActivity.getInstance().language.FIRST_SETTINGS_SCREEN_TITTLE);
		ch1.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[0]);
		ch2.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[1]);
		ch3.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[2]);
		ch4.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[3]);
		ch5.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[4]);
		ch6.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[5]);
		ch7.setText(MainActivity.getInstance().language.NAMES_DAYS_WEEK[6]);

		((TextView) root.findViewById(R.id.initial_setting_second_click_sound))
				.setText(MainActivity.getInstance().language.SETTINGS_SOUND_TITLE);

		((TextView) root.findViewById(R.id.initial_setting_second_alarm_sound))
				.setText(MainActivity.getInstance().language.NOTIFY_SOUND);

		((TextView) root.findViewById(R.id.initial_setting_first_choose_lang))
				.setText(MainActivity.getInstance().language.CHANGE_LANG);

		((TextView) root.findViewById(R.id.initial_setting_third_time_format))
				.setText(MainActivity.getInstance().language.CHOOSE_TIME_FORMATE);

		((TextView) root.findViewById(R.id.initial_setting_third_holiday))
				.setText(MainActivity.getInstance().language.CHANGE_HOLIDAYS);  

	} 

	private void initSoundSpinner() {
		ArrayList<String> spinner_array = new ArrayList<String>();

		spinner_array.add(MainActivity.getInstance().language.NOT_SOUND);
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "1");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "2");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "3");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "4");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "5");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "6");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "7");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "8");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "9");
		spinner_array.add(MainActivity.getInstance().language.SOUND_SETTINGS
				+ "10");

		soundSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				MainActivity.getInstance().shared.edit()
						.putInt(MainActivity.SHARED_SOUND_BUTTON, position)
						.commit();
				new PlaySoundButton();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity,
				android.R.layout.simple_spinner_item,
				android.R.layout.simple_spinner_dropdown_item, spinner_array);

		soundSpinner.setAdapter(spinnerAdapter);

		soundSpinner.setSelection(0);
	}

	private void initAlarmSound() {

		alarmSoundButton.setText("...");

		alarmSoundButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent target = FileUtils.createGetContentIntent();
				Intent intent = Intent.createChooser(target, "");
				try {
					startActivityForResult(intent, ADD_ALARM_SOUND_KEY);

				} catch (ActivityNotFoundException e) {
				}

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		String file_path = "";
		if (requestCode == ADD_ALARM_SOUND_KEY) {
			try {

				file_path = AddEventFragment.getRealPathFromURI(data.getData());
				alarmSoundButton.setText(AddEventFragment
						.getFileName(file_path));

				MainActivity.getInstance().shared.edit()
						.putString(MainActivity.SHARED_ALARM_SOUND, file_path)
						.commit();
			} catch (Exception e) {

			}
		}

	}

	public void savedCheckedDays() {
		String res = "";

		try {
			if (ch1.isChecked())
				res += "0,";
			if (ch2.isChecked())
				res += "1,";
			if (ch3.isChecked())
				res += "2,";
			if (ch4.isChecked())
				res += "3,";
			if (ch5.isChecked())
				res += "4,";
			if (ch6.isChecked())
				res += "5,";
			if (ch7.isChecked())
				res += "6,";

			res = res.substring(0, res.length() - 1);
			MainActivity.getInstance().shared.edit()
					.putString(MainActivity.getInstance().SHARED_HOLIDAYS, res)
					.commit();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("class:" + getClass().toString(), "catch:" + e);
		}
	}

}
