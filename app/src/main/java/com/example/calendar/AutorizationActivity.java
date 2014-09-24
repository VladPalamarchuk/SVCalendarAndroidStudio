package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class AutorizationActivity extends Activity {
    Context context;
    Handler handler;
    EditText login_autoriz;
    EditText password_autoriz;
    EditText login_registr;
    EditText email_registr;

    EditText password_registr;
    EditText password_confirm_registr;
    EditText email_forgot;
    EditText name_registr;
    EditText surname_registr;
    EditText phone_registr;
    public String date_birthday = "";
    Button enter_autoriz;
    Button forgot_autoriz;
    Button registr_autoriz;
    Button enter_registr;
    Button remind_password;
    RelativeLayout bDay;
    String country = "";
    String city = "";
    Spinner country_spiner;
    Spinner city_spiner;
    CheckBox remember_password;
    ArrayList<String> array_country;
    ArrayAdapter<String> country_adapter;

    ArrayList<String> array_city;
    ArrayAdapter<String> city_adapter;
    public static AutorizationActivity activity;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_autorization);
        login_autoriz = (EditText) findViewById(R.id.autorization_login);
        password_autoriz = (EditText) findViewById(R.id.autorization_password);
        context = getApplicationContext();
        activity = this;
        enter_autoriz = (Button) findViewById(R.id.autorization_enter);
        forgot_autoriz = (Button) findViewById(R.id.autorization_get_password);
        registr_autoriz = (Button) findViewById(R.id.autorization_registration);
        login_registr = (EditText) findViewById(R.id.autorization_registration_login);
        name_registr = (EditText) findViewById(R.id.autorization_registration_name);
        surname_registr = (EditText) findViewById(R.id.autorization_registration_surname);
        phone_registr = (EditText) findViewById(R.id.autorization_registration_phone);
        remember_password = (CheckBox) findViewById(R.id.remember_password);
        email_registr = (EditText) findViewById(R.id.autorization_registration_email);
        password_registr = (EditText) findViewById(R.id.autorization_registration_pasword_1);
        password_confirm_registr = (EditText) findViewById(R.id.autorization_registration_pasword_2);
        enter_registr = (Button) findViewById(R.id.autorization_registration_enter);
        email_forgot = (EditText) findViewById(R.id.autorization_forgot_password_email);
        remind_password = (Button) findViewById(R.id.autorization_forgot_password);
        bDay = (RelativeLayout) findViewById(R.id.autorization_registration_date);

        country_spiner = (Spinner) findViewById(R.id.autorization_registration_country);
        array_country = new ArrayList<String>();
        country_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array_country);
        city_spiner = (Spinner) findViewById(R.id.autorization_registration_city);
        array_city = new ArrayList<String>();
        city_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array_city);

        country_spiner.setAdapter(country_adapter);
        city_spiner.setAdapter(city_adapter);

        int c = new Color().parseColor("#000000");

        DrawClock drawClock = new DrawClock(this);
        ((RelativeLayout) findViewById(R.id.authorization_clock))
                .addView(drawClock);
        login_autoriz.setHintTextColor(c);
        password_autoriz.setHintTextColor(c);
        login_registr.setHintTextColor(c);
        email_registr.setHintTextColor(c);
        password_registr.setHintTextColor(c);
        password_confirm_registr.setHintTextColor(c);
        email_forgot.setHintTextColor(c);
        name_registr.setHintTextColor(c);
        surname_registr.setHintTextColor(c);
        phone_registr.setHintTextColor(c);

        phone_registr.addTextChangedListener(new CustomTextWatcher(
                phone_registr));

        phone_registr.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                if (hasFocus)
                    if (phone_registr.getText().toString().length() < 3)
                        phone_registr.setText("+");
            }
        });

        getCuntries();
        country_spiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                try {

                    getCityes(array_country.get(position));
                    city_adapter.notifyDataSetChanged();
                    Collections.sort(array_city);

                    country = array_country.get(position);
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        city_spiner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                try {

                    city = array_city.get(position);

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        SetText();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                int key = (Integer) msg.obj;

                switch (key) {
                    case 0:
                        UnShowProgres();
                        break;

                    case 1:
                        ShowProgres();
                        break;

                    case 2:
                        UnShowProgres();
                        Toast.makeText(MainActivity.getInstance(),
                                MainActivity.getInstance().language.ERROR_AUTORIZ,
                                Toast.LENGTH_SHORT).show();
                        login_autoriz.setText("");
                        password_autoriz.setText("");
                        break;

                    case 3:
                        UnShowProgres();
                        Toast.makeText(
                                MainActivity.getInstance(),
                                MainActivity.getInstance().language.PASSWORD_REMINDED,
                                Toast.LENGTH_SHORT).show();
                        email_forgot.setText("");

                        break;

                    case 4:

                        findViewById(R.id.autorization_progres).setVisibility(
                                View.INVISIBLE);

                        ((RelativeLayout) findViewById(R.id.autorization_registration_rel))
                                .setVisibility(View.VISIBLE);

                        Toast.makeText(MainActivity.getInstance(),
                                MainActivity.getInstance().language.DONT_REGISTR,
                                Toast.LENGTH_SHORT).show();

                        break;

                    case 5:

                        Toast.makeText(
                                MainActivity.getInstance(),
                                MainActivity.getInstance().language.SUCCESS_REGISTR,
                                Toast.LENGTH_LONG).show();

                        finish();
                        break;

                }
            }
        };

        bDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                ShowDate();

            }
        });

        enter_autoriz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                if (MainActivity.getInstance().InternetConnection()) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            Looper.prepare();

                            SendMessage(1);

                            boolean autoriz = MainActivity.getInstance()
                                    .Authorization(
                                            login_autoriz.getText().toString(),
                                            password_autoriz.getText()
                                                    .toString(),
                                            remember_password.isChecked());
                            if (autoriz) {
                                finish();
                            } else {
                                SendMessage(2);
                            }
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
                } else {

                    Toast.makeText(
                            MainActivity.getInstance(),
                            MainActivity.getInstance().language.NO_INTERNET_CONNECTION,
                            Toast.LENGTH_SHORT).show();

                }

            }

        });

        forgot_autoriz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                findViewById(R.id.autorization_main_rel).setVisibility(
                        View.GONE);
                findViewById(R.id.autorization_forgot_rel).setVisibility(
                        View.VISIBLE);

            }
        });

        remind_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                if (MainActivity.getInstance().InternetConnection()) {

                    if (new CheckEmail().Check(email_forgot.getText()
                            .toString())) {
                        ShowProgres();

                        new Thread(new Runnable() {
                            public void run() {

                                if (MainActivity.getInstance().RemindPassword(
                                        email_forgot.getText().toString()))
                                    SendMessage(3);
                                try {
                                    Thread.interrupted();

                                } catch (Exception e2) {
                                    // TODO: handle exception
                                    Log.e(getClass().toString()
                                            + "line = "
                                            + Thread.currentThread()
                                            .getStackTrace()[2]
                                            .getLineNumber(), "catch:"
                                            + e2);
                                }
                            }
                        }).start();

                    } else {
                        Toast.makeText(
                                MainActivity.getInstance(),
                                MainActivity.getInstance().language.NO_CORRECT_EMAIL,
                                Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(
                            MainActivity.getInstance(),
                            MainActivity.getInstance().language.NO_INTERNET_CONNECTION,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        registr_autoriz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                findViewById(R.id.autorization_main_rel).setVisibility(
                        View.GONE);
                findViewById(R.id.autorization_registration_rel).setVisibility(
                        View.VISIBLE);

            }
        });

        enter_registr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                if (MainActivity.getInstance().InternetConnection()) {

                    if (new CheckEmail().Check(email_registr.getText()
                            .toString())) {

                        if (password_registr
                                .getText()
                                .toString()
                                .equals(password_confirm_registr.getText()
                                        .toString())) {

                            if (login_registr.getText().toString().length() != 0
                                    || password_registr.getText().toString()
                                    .length() != 0

                                    ) {

                                if (CheckPhone(phone_registr.getText()
                                        .toString())
                                        || phone_registr.getText().toString()
                                        .length() == 0) {

                                    if (login_registr.getText().toString()
                                            .contains(" ")
                                            || password_registr.getText()
                                            .toString().contains(" ")
                                            || name_registr.getText()
                                            .toString().contains(" ")
                                            || surname_registr.getText()
                                            .toString().contains(" ")
                                            || phone_registr.getText()
                                            .toString().contains(" ")) {
                                        SendMessage(4);
                                    } else {
                                        ShowProgres();

                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method
                                                // stub
                                                Looper.prepare();

                                                if (MainActivity
                                                        .getInstance()
                                                        .Registration(
                                                                login_registr
                                                                        .getText()
                                                                        .toString(),
                                                                password_registr
                                                                        .getText()
                                                                        .toString(),
                                                                password_confirm_registr
                                                                        .getText()
                                                                        .toString(),
                                                                email_registr
                                                                        .getText()
                                                                        .toString(),
                                                                name_registr
                                                                        .getText()
                                                                        .toString(),
                                                                surname_registr
                                                                        .getText()
                                                                        .toString(),
                                                                phone_registr
                                                                        .getText()
                                                                        .toString(),
                                                                date_birthday,
                                                                country, city)) {

                                                    if (MainActivity
                                                            .getInstance()
                                                            .Authorization(
                                                                    login_registr
                                                                            .getText()
                                                                            .toString(),
                                                                    password_registr
                                                                            .getText()
                                                                            .toString(),
                                                                    remember_password
                                                                            .isChecked()))

                                                        SendMessage(5);

                                                } else {

                                                    SendMessage(4);
                                                }

                                                try {
                                                    Thread.interrupted();
                                                } catch (Exception e) {
                                                    // TODO: handle exception
                                                    Log.e(getClass().toString()
                                                                    + "line = "
                                                                    + Thread.currentThread()
                                                                    .getStackTrace()[2]
                                                                    .getLineNumber(),
                                                            "catch:" + e);
                                                }

                                            }
                                        }).start();
                                    }

                                } else {
                                    Toast.makeText(
                                            MainActivity.getInstance(),
                                            MainActivity.getInstance().language.ERROR_PHONE,
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else

                            {
                                Toast.makeText(
                                        MainActivity.getInstance(),
                                        MainActivity.getInstance().language.EDIT_ALL_FIELD,
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else

                        {
                            Toast.makeText(
                                    MainActivity.getInstance(),
                                    MainActivity.getInstance().language.PASSWORD_DONT_SOME,
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(
                                MainActivity.getInstance(),
                                MainActivity.getInstance().language.NO_CORRECT_EMAIL,
                                Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(
                            MainActivity.getInstance(),
                            MainActivity.getInstance().language.NO_INTERNET_CONNECTION,
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

        MainActivity.setButtonAvtoriz(enter_autoriz);
        MainActivity.setButtonAvtoriz(forgot_autoriz);
        MainActivity.setButtonAvtoriz(registr_autoriz);
        MainActivity.setButtonAvtoriz(enter_registr);
        MainActivity.setButtonAvtoriz(remind_password);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        name_registr.setText("");
        phone_registr.setText("");
        password_autoriz.setText("");
        password_confirm_registr.setText("");
        password_registr.setText("");
        email_forgot.setText("");
        email_registr.setText("");
        login_autoriz.setText("");
        login_registr.setText("");
        surname_registr.setText("");

        if (findViewById(R.id.autorization_main_rel).getVisibility() == View.GONE) {
            findViewById(R.id.autorization_main_rel)
                    .setVisibility(View.VISIBLE);
            findViewById(R.id.autorization_forgot_rel).setVisibility(View.GONE);
            findViewById(R.id.autorization_registration_rel).setVisibility(
                    View.GONE);
            findViewById(R.id.autorization_progres).setVisibility(View.GONE);
        } else {
            MainActivity.getInstance().finish();
        }

    }

    @SuppressLint("NewApi")
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();

    }

    public void ShowProgres() {

        findViewById(R.id.autorization_main_rel).setVisibility(View.GONE);
        findViewById(R.id.autorization_registration_rel).setVisibility(
                View.GONE);
        findViewById(R.id.autorization_forgot_rel).setVisibility(View.GONE);
        findViewById(R.id.autorization_progres).setVisibility(View.VISIBLE);
    }

    public void UnShowProgres() {

        findViewById(R.id.autorization_main_rel).setVisibility(View.VISIBLE);
        // findViewById(R.id.autorization_registration_rel).setVisibility(
        // View.VISIBLE);
        // findViewById(R.id.autorization_forgot_rel).setVisibility(View.VISIBLE);
        findViewById(R.id.autorization_progres).setVisibility(View.INVISIBLE);
    }

    public void SendMessage(int key) {
        Message m = handler.obtainMessage(1, key);
        handler.sendMessage(m);

    }

    public void SetText() {

        remember_password
                .setText(MainActivity.getInstance().language.REMEMBER_PASSWORD);
        login_autoriz
                .setHint(MainActivity.getInstance().language.ENTER_YOUR_LOGIN);
        login_registr
                .setHint(MainActivity.getInstance().language.ENTER_YOUR_LOGIN);

        ((TextView) findViewById(R.id.autorization_registration_date_text))
                .setText(MainActivity.getInstance().language.DATE_BIRTHDAY);

        ((TextView) findViewById(R.id.filed_all_fields)).setText(MainActivity
                .getInstance().language.REQUIRED_FIELDS);

        password_autoriz
                .setHint(MainActivity.getInstance().language.ENTER_YOUR_PASSWORD);
        password_registr
                .setHint(MainActivity.getInstance().language.ENTER_YOUR_PASSWORD);
        password_confirm_registr
                .setHint(MainActivity.getInstance().language.ENTER_CONFIRM_PASSWORD);
        email_forgot
                .setHint(MainActivity.getInstance().language.ENTER_YOUR_EMAIL);
        email_registr
                .setHint(MainActivity.getInstance().language.ENTER_YOUR_EMAIL);

        enter_autoriz.setText(MainActivity.getInstance().language.ENTER);
        enter_registr
                .setText(MainActivity.getInstance().language.ZAREGISTRATION);
        remind_password
                .setText(MainActivity.getInstance().language.REMIND_PASSWORD);
        registr_autoriz
                .setText(MainActivity.getInstance().language.REGISTRATION);

        forgot_autoriz
                .setText(MainActivity.getInstance().language.FORGOT_YOUR_PASSWORD);

        name_registr
                .setHint(MainActivity.getInstance().language.ENETER_YOUR_NAME);
        surname_registr
                .setHint(MainActivity.getInstance().language.ENETER_YOUR_SURNAME);
        phone_registr
                .setHint(MainActivity.getInstance().language.ENETER_YOUR_PHONE);

        if (MainActivity.getInstance().shared.getString("login", "").length() > 0) {
            login_autoriz.setText(MainActivity.getInstance().shared.getString(
                    "login", ""));
            login_autoriz.requestFocus();
        }

    }

    public boolean CheckPhone(String phone) {
        if (phone.length() >= 15)
            return true;
        return false;
    }

    public static AutorizationActivity getInstance() {
        return activity;
    }

    public void ShowDate() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final DatePicker datePicker = new DatePicker(this);
        datePicker.setCalendarViewShown(false);
        dialog.setView(datePicker);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                date_birthday = datePicker.getYear()
                        + "-"
                        + MainActivity.getInstance().FormatDate(
                        datePicker.getMonth())
                        + "-"
                        + MainActivity.getInstance().FormatDate(
                        datePicker.getDayOfMonth());
                ((TextView) findViewById(R.id.autorization_registration_date_text))
                        .setText(date_birthday);

            }
        });

        dialog.show();
    }

    public void getCuntries() {
//
//        if (MainActivity.CITIES.size() == 0) {
//            ParseCity parser = new ParseCity();
//            MainActivity.CITIES = parser.Parsing_data();
//            getCuntries();
//
//        } else {
//            array_country.clear();
//            ArrayList<OneCity> AllCuntries = MainActivity.getInstance()
//                    .getTheCountry(MainActivity.CITIES);
//
//            for (int i = 0; i < AllCuntries.size(); i++) {
//                array_country.add(AllCuntries.get(i).getCountry().toString());
//            }
//            country_adapter.notifyDataSetChanged();
//        }
    }

    public void getCityes(String country) {
        array_city.clear();
        ArrayList<OneCity> a = MainActivity.getInstance().getCitiesByCountry(
                MainActivity.CITIES, country);

        for (int i = 0; i < a.size(); i++) {
            array_city.add(a.get(i).getName());
        }
    }

}
