package com.example.calendar;

import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchPeopleCalendar extends Activity {

    EditText name_surname;
    Button search;
    ListView list;
    ArrayList<User> arrayPeople = new ArrayList<User>();
    Handler handler;
    public static SearchPeopleCalendar searchPeopleCalendar;
    public User friend;
    TextView result;
    ArrayList<String> array = new ArrayList<String>();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            MainActivity.getInstance(), layout.simple_list_item_1, array);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_people_calendar);

        name_surname = (EditText) findViewById(R.id.search_people_name_surname);
        search = (Button) findViewById(R.id.search_people_search_button);
        list = (ListView) findViewById(R.id.search_list_people);
        result = (TextView) findViewById(R.id.search_result);
        list.setAdapter(adapter);
        search.setText(MainActivity.getInstance().language.SEARCH);

        name_surname
                .setHint(MainActivity.getInstance().language.ENTER_NAME_AND_SURNAME);

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String text = name_surname.getText().toString();
                if (text != "") {
                    ShowProgress();
                    arrayPeople.clear();
                    ChangeArray();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Looper.prepare();
                            // TODO Auto-generated method stub

                            arrayPeople = MainActivity.getInstance()
                                    .getPeoplesCalendar(text);

                            handler.sendMessage(handler.obtainMessage(1, 1));
                        }
                    }).start();

                } else {

                    Toast.makeText(
                            MainActivity.getInstance(),
                            MainActivity.getInstance().language.NE_VERNO_ZAP_POLE_VVODA,
                            Toast.LENGTH_SHORT).show();
                    name_surname.setText("");
                }

            }
        });
        setColor();
        setText();
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                AddEventFragment.getInstance().friend = arrayPeople
                        .get(position);

                finish();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                UnShowProgress();
                if (arrayPeople.size() == 0) {
                    result.setText(MainActivity.getInstance().language.SEARCH_DONT_RESULT);
                } else {
                    result.setText(MainActivity.getInstance().language.SEARCH_RESUlT);
                    ChangeArray();

                }

            }
        };

    }

    // public int getCountSymbolInString(String str, String symb) {
    // int res = 0;
    // for (int i = 0; i < str.length(); i++) {
    // if (str.substring(i, i + 1).equals(symb))
    // res++;
    //
    // }
    // return res;
    // }

    public void ShowProgress() {
        findViewById(R.id.search_people_progres).setVisibility(View.VISIBLE);
        findViewById(R.id.search_list_people).setVisibility(View.INVISIBLE);
        result.setVisibility(View.INVISIBLE);

    }

    public void UnShowProgress() {
        findViewById(R.id.search_people_progres).setVisibility(View.INVISIBLE);
        findViewById(R.id.search_list_people).setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
    }

    public void ChangeArray() {
        array.clear();
        for (int i = 0; i < arrayPeople.size(); i++) {
            array.add(arrayPeople.get(i).getName() + " "
                    + arrayPeople.get(i).getSurname() + ", "
                    + arrayPeople.get(i).getPhone() + ", "
                    + arrayPeople.get(i).getEmail());
        }
        adapter.notifyDataSetChanged();
    }

    public static SearchPeopleCalendar getInstance() {
        return searchPeopleCalendar;
    }

    public void setText() {
        name_surname
                .setHint(MainActivity.getInstance().language.ENTER_NAME_AND_SURNAME);
        search.setText(MainActivity.getInstance().language.SEARCH);
    }

    @SuppressLint("NewApi")
    public void setColor() {
        MainActivity.getInstance().setButton(search);
        search.setTextColor(new MyColor().getColorLabel());

        ((RelativeLayout) findViewById(R.id.search_people_root))
                .setBackground(MainActivity.getInstance()
                        .findViewById(R.id.main_root).getBackground());
        name_surname.setHintTextColor(new MyColor().getColorLabel());
        ((RelativeLayout) findViewById(R.id.search_people_background))
                .setBackgroundColor(new MyColor().getColorBacground());
    }
}
