package com.example.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ListEventOfDayFragment extends android.support.v4.app.Fragment {

    static ListEventOfDayFragment listEventOfDayFragment;
    ListView list_event;
    int year, mounth, day;
    ViewGroup root;
    ImageView add_event;
    ArrayList<OneEventAndWeater> arrayOneEventAndWeaters = new ArrayList<OneEventAndWeater>();
    ArrayList<OneEvent> arrayOneEvent = new ArrayList<OneEvent>();
    AdapterListEvent adapter = new AdapterListEvent(MainActivity.getInstance(),
            R.layout.one_event_item, arrayOneEventAndWeaters, false, true, true);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        root = (ViewGroup) inflater.inflate(R.layout.list_event_fragment, null);
        listEventOfDayFragment = this;

        list_event = (ListView) root.findViewById(R.id.list_event_list);

        year = getArguments().getInt(MainActivity.LIST_EVENT_YEAR);

        mounth = getArguments().getInt(MainActivity.LIST_EVENT_MOUNTH);
        add_event = (ImageView) root.findViewById(R.id.list_event_add_event);
        day = getArguments().getInt(MainActivity.LIST_EVENT_DAY);

        add_event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MainActivity.getInstance().startAddEventFragment(false, false,
                        MainActivity.getNowYear(), MainActivity.getNowMounth(),
                        MainActivity.getNowDay(), MainActivity.getNowHour(),
                        MainActivity.getNowMinute());
            }
        });

        arrayOneEvent.clear();
        arrayOneEventAndWeaters.clear();
        arrayOneEvent = MainActivity.getInstance().getAllEvent();
        list_event.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < arrayOneEvent.size(); i++) {
            arrayOneEventAndWeaters.add(new OneEventAndWeater(arrayOneEvent
                    .get(i)));
        }
        adapter.notifyDataSetChanged();

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.list_event_fragment_open_left_menu),
                        (Spinner) root
                                .findViewById(R.id.list_event_fragment_spinner));

        list_event.setSelection(getSelectedPosition(arrayOneEvent));
        list_event.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                MounthFragment.getInstance().arrayOneEventAndWeaters.clear();
                for (int i = 0; i < arrayOneEventAndWeaters.size(); i++)
                    MounthFragment.getInstance().arrayOneEventAndWeaters
                            .add(arrayOneEventAndWeaters.get(i));
                new PlaySoundButton();
                MainActivity.getInstance().startOneEventFragment(position + 1);

            }
        });

        setColor();
        return root;

    }

    static public ListEventOfDayFragment getInstance() {
        return listEventOfDayFragment;
    }

    public void setColor() {
        MyColor c = new MyColor();
        root.findViewById(R.id.list_event_fragment_top).setBackgroundColor(
                c.getColorComponents());

        root.findViewById(R.id.list_event_bacground).setBackgroundColor(
                c.getColorBacground());
    }

    public int getSelectedPosition(ArrayList<OneEvent> array) {

        int res = 0;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getStart_date_year() == MainActivity.getNowYear()
                    && array.get(i).getStart_date_day() == MainActivity
                    .getNowDay()
                    && array.get(i).getStart_date_mounth() == MainActivity
                    .getNowMounth()) {
                return i;
            }
        }

        return res;
    }
}
