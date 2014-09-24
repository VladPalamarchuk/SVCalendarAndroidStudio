package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

@SuppressLint("ValidFragment")
public class InitialSettingThird extends Fragment implements
		View.OnClickListener {

	ImageView stocks;
	ImageView currens;
	ImageView rss;

	public static InitialSettingThird getInstance() {
		return new InitialSettingThird();
	}

	private InitialSettingsActivity activity;

	View root;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (InitialSettingsActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.initial_setting_third, null);

		root.findViewById(R.id.initial_setting_next).setOnClickListener(this);

		stocks = (ImageView) root.findViewById(R.id.initial_settings_stocks);
		currens = (ImageView) root.findViewById(R.id.initial_settings_currens);
		rss = (ImageView) root.findViewById(R.id.initial_settings_rss);

		stocks.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.getInstance().showStock(activity);
			}
		});

		rss.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MainActivity.getInstance().showRssList(activity);
			}
		});
		
		currens.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.getInstance().showCurrency(activity);
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
		
		
		InitialSettingsActivity.getInstance().finish();
	}

	public void setText() {
		

		((TextView) root.findViewById(R.id.initial_setting_preview_text))
				.setText(MainActivity.getInstance().language.INITIAL_SETTONGS_STOCKS_CURRENS_RSS);
		

		((TextView) root.findViewById(R.id.initial_settings_currens_tittle))
				.setText(MainActivity.getInstance().language.CURRENS_KURS);

		((TextView) root.findViewById(R.id.initial_settings_rss_tittle))
				.setText("RSS");

		((TextView) root.findViewById(R.id.initial_settings_stocks_tittle))
				.setText(MainActivity.getInstance().language.STOCKS);
	}
}
