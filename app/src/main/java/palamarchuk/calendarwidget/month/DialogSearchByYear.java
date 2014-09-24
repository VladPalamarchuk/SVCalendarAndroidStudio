package palamarchuk.calendarwidget.month;

import java.util.ArrayList;

import com.example.calendar.MainActivity;

import android.R;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DialogSearchByYear {

	Context context;

	YearSelectedListener yearSelectedListener;

	public void setYearSelectedListener(
			YearSelectedListener yearSelectedListener) {
		this.yearSelectedListener = yearSelectedListener;
	}

	public DialogSearchByYear(Context context) {
		this.context = context;
	}

	public void show() {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		ListView list = new ListView(context);
		ArrayList<Integer> array = new ArrayList<Integer>();

		for (int i = 1; i <= 5000; i++) {
			array.add(i);
		}
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(context,
				R.layout.simple_list_item_1, array);
		list.setAdapter(adapter);
		list.setSelection(MainActivity.getNowYear() - 3);

		list.setLayoutParams(new LayoutParams(100, 100));
		dialog.setView(list);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				yearSelectedListener.yearSelected(position);

				dialog.dismiss();
			}
		});

		dialog.setButton(MainActivity.getInstance().language.CANCEL,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.dismiss();
					}
				});
		dialog.show();

	}

	public interface YearSelectedListener {
		public void yearSelected(int res);
	}

}
