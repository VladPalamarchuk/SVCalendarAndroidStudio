package fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

@SuppressWarnings("ValidFragment")
public class InitialSettingForth extends Fragment implements
		View.OnClickListener {

	
	View root;

	public static InitialSettingForth getInstance() {
		return new InitialSettingForth();
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
		root = inflater.inflate(R.layout.initial_setting_forth, null);

		
		
		
		

		root.findViewById(R.id.initial_setting_next).setOnClickListener(this);
		setText();

		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.initial_setting_next:
			activity.addFragment(InitialSettingFive.getInstance());
			break;
		}
	}

	public void setText() {
	
	}


}
