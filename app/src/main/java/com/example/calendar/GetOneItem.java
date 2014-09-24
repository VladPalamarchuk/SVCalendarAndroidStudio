package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calendar.R.layout;

import java.util.ArrayList;

public class GetOneItem extends Activity {
    ArrayList<OneEventAndWeater> items = new ArrayList<OneEventAndWeater>();
    AdapterListEvent adapter = new AdapterListEvent(MainActivity.getInstance(),
            layout.one_event_item, items, true, false, false);
    Intent intent;
    ListView list;
    TextView title;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_get_one_item);

        list = (ListView) findViewById(R.id.get_one_item_list);
        title = (TextView) findViewById(R.id.get_one_item_title);
        intent = getIntent();
        int year = intent.getExtras().getInt("YEAR");
        int mounth = intent.getExtras().getInt("MOUNTH");
        int day = intent.getExtras().getInt("DAY");
        final int type = intent.getExtras().getInt("TYPE");
        list.setAdapter(adapter);

        findViewById(R.id.get_one_item_bacground).setBackgroundColor(
                new MyColor().getColorBacground());
        findViewById(R.id.get_one_item_root).setBackground(
                MainActivity.getInstance().findViewById(R.id.main_root)
                        .getBackground());

        ArrayList<OneEvent> events = MainActivity.getInstance()
                .getEventsByDate(year, mounth, day);

        for (int i = 0; i < events.size(); i++) {
            items.add(new OneEventAndWeater(events.get(i)));
        }
        adapter.notifyDataSetChanged();
        title.setText(MainActivity.getInstance().language.DIALOG_CHANGE_EVENTS);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                Log.e("", "item selected type = " + type);
                switch (type) {
                    case ShowDialog.TYPE_DELETE:
                        finish();
                        OneEvent e = null;
                        e = items.get(position).getOneEvent();
                        MainActivity.getInstance().DeleteEvent(
                                ShowDialog.getInstance().v, e, true);

                        break;
                    case ShowDialog.TYPE_CUT:
                        finish();
                        e = items.get(position).getOneEvent();
                        MainActivity.getInstance().CutEvent(
                                ShowDialog.getInstance().v, e, true);

                        break;

                    case ShowDialog.TYPE_COPY:
                        finish();
                        e = items.get(position).getOneEvent();
                        MainActivity.getInstance().CopyEvent(
                                ShowDialog.getInstance().v, e, true);

                        break;

                    case ShowDialog.TYPE_CHANGE:
                        finish();
                        final OneEvent ev = items.get(position).getOneEvent();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                MainActivity.getInstance().UpdateEvent(ev);
                            }
                        }, 100);

                        break;

                }
            }
        });

    }
}
