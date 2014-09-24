package fragments;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calendar.AddBirthday;
import com.example.calendar.FaceBook;
import com.example.calendar.GooglePlusActivity;
import com.example.calendar.MainActivity;
import com.example.calendar.OneFriend;
import com.example.calendar.R;
import com.example.calendar.Tips;
//import com.example.calendar.VK;

@SuppressWarnings("ValidFragment")
public class InitialSettingFive extends Fragment implements
		View.OnClickListener {

	ViewGroup root;
	ImageView vk;
	ImageView fb;
	ImageView gp;

	public static InitialSettingFive getInstance() {
		return new InitialSettingFive();
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
		root = (ViewGroup) inflater
				.inflate(R.layout.initial_setting_five, null);

		vk = (ImageView) root.findViewById(R.id.five_settings_vk);
		fb = (ImageView) root.findViewById(R.id.five_settings_fb);
		gp = (ImageView) root.findViewById(R.id.five_settings_gp);

		vk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog dialog = new AlertDialog.Builder(
						InitialSettingsActivity.getInstance()).create();
				RelativeLayout rel = new RelativeLayout(InitialSettingsActivity
						.getInstance());

				ProgressBar progres = new ProgressBar(InitialSettingsActivity
						.getInstance());
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						50, 50);
				params.addRule(RelativeLayout.CENTER_IN_PARENT,
						RelativeLayout.TRUE);
				rel.addView(progres);
				dialog.setView(rel);

				dialog.show();

				if (MainActivity.getInstance().InternetConnection()) {
					ArrayList<OneFriend> result = new ArrayList<OneFriend>();
//					new VK(MainActivity.getInstance(), rel, result, true,
//							dialog);
				} else
					Toast.makeText(
							MainActivity.getInstance(),
							MainActivity.getInstance().language.NO_INTERNET_CONNECTION,
							Toast.LENGTH_SHORT).show();

			}
		});

		fb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (MainActivity.getInstance().InternetConnection()) {
					final FaceBook f = new FaceBook(InitialSettingsActivity
							.getInstance(), MainActivity.arrayOneFriendFacebook);
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							while (f.isAlive) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								continue;
							}
							new AddBirthday(MainActivity.arrayOneFriendFacebook)
									.Add();
							try {
								Thread.interrupted();
							} catch (Exception e) {
								// TODO: handle exception
								Log.e(getClass().toString()
										+ "line = "
										+ Thread.currentThread()
												.getStackTrace()[2]
												.getLineNumber(), "catch:" + e);
							}
						}
					}).start();

				}

				else
					Toast.makeText(
							MainActivity.getInstance(),
							MainActivity.getInstance().language.NO_INTERNET_CONNECTION,
							Toast.LENGTH_SHORT).show();
			}

		});

		gp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InitialSettingsActivity
						.getInstance(), GooglePlusActivity.class);
				startActivity(intent);
			}
		});

		((TextView) root.findViewById(R.id.initial_setting_preview_text))
				.setText(MainActivity.getInstance().language.CHOOSE_SET);

		root.findViewById(R.id.initial_setting_next).setOnClickListener(this);
		setText();

		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.initial_setting_next:
			// MainActivity.getInstance().setText();
			//
			// try {
			//
			// ((TextView) Tips.view.findViewById(R.id.tip1_text1))
			// .setText(MainActivity.getInstance().language.TIP1_TEXT1);
			// ((TextView) Tips.view.findViewById(R.id.tip1_text2))
			// .setText(MainActivity.getInstance().language.TIP1_TEXT2);
			//
			// } catch (Exception e) {
			// }
			//
			// InitialSettingsActivity.getInstance().finish();
			activity.addFragment(InitialSettingSecond.getInstance());
			break;
		}
	}

	public void setText() {

	}

}
