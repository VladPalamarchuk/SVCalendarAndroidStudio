package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.provider.DialogTools;
import android.provider.GoogleCalendar;
import android.provider.SaveCalendar;
import android.provider.VEventWrapper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.CalendarService.CalendarEventLoadedListener;
import com.example.calendar.NewCurrency.OnLoadComplete;
import com.example.calendar.Stock.OnStocksLoadedListener;
import com.example.calendar.Tips.OpenMenu;
import com.example.calendar.Tips.OpenMenuActions;
import com.example.calendar.getWeatherByDay.OnCompleteListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import bcalendargroups.DrawerListActivity2;
import bcalendargroups.QueryMaster;
import filechooser.FileChooserActivity;
import fragments.InitialSettingsActivity;
import palamarchuk.calendarwidget.month.BusinessCalendarWidgetMonth;
import palamarchuk.calendarwidget.month.SharedPrefKeys;
import pushdialogs.EventConfirmation;
import pushdialogs.GroupConfirmation;

public class MainActivity extends FragmentActivity {

    private MyDrawerListener myDrawerListener;
    private OnDrawerOpenTipListener onDrawerOpenTipListener;

    public HashMap<String, Float> CURRENS_MAP;

    public static int THEME_APPLICATION = 2;

    public static ImageLoader imageLoader;

    DrawerLayout drawerLayout;

    public static SharedPreferences shared;
    boolean FirstStart = false;
    Intent intent;
    RelativeLayout right_menu;
    RelativeLayout left_menu;
    RelativeLayout vk;
    boolean ispush;
    RelativeLayout fb;
    RelativeLayout gp;
    Handler showError;
    OneEvent event_buffer = null;
    ListView list_gr_event;
    RelativeLayout add_event;
    // RelativeLayout add_group_event;
    RelativeLayout contacs;
    RelativeLayout settings;
    RelativeLayout exit;
    RelativeLayout stock_rel;
    Bitmap image;
    RelativeLayout help;
    RelativeLayout about;
    RelativeLayout rss;
    ArrayList<OneStock> arrayList = new ArrayList<OneStock>();
    ViewGroup root;
    ArrayList<OneStock> STOCKS;
    OneEvent bufferForUpdate = null;
    RelativeLayout currens;
    boolean isGroupEvent = false;

    ArrayList<OneWeatherDay> WEATHERS = new ArrayList<OneWeatherDay>();

    public static ArrayList<OneCity> CITIES = new ArrayList<OneCity>();
    final public static String SHARED_FIRST_START = "SHARED_FIRST_START";
    public Languages language;
    final public static String SHARED_COLOR_BACKGROUND = "SHARED_COLOR_BACKGROUND";
    final public static String SHARED_COLOR_COMPONENTS = "SHARED_COLOR_COMPONENTS";
    final public static String SHARED_COLOR_FONT = "SHARED_COLOR_FONT";

    final public static String SHARED_SAVED_LOGIN_PASSWORD = "SHARED_SAVED_LOGIN_PASSWORD";

    final public static String SHARED_IS_SYNC_GOOGLE_CALENDAR = "SHARED_IS_SYNC_GOOGLE_CALENDAR";
    final public static String SHARED_CALENDARS_ID = "SHARED_CALENDARS_ID";
    final public static String SHARED_SOUND_NOTIFY = "SHARED_SOUND_NOTIFY";
    final public static String SHARED_COLOR_EVENT_YEAR = "SHARED_COLOR_EVENT_YEAR";
    final public static String SHARED_COLOR_EVENT_MOUNTH = "SHARED_COLOR_EVENT_MOUNTH";
    final public static String SHARED_COLOR_EVENT_WEEK = "SHARED_COLOR_EVENT_WEEK";
    final public static String SHARED_COLOR_LABEL = "SHARED_COLOR_LABEL";
    final public static String SHARED_COLOR_NOW_DATE = "SHARED_COLOR_NOW_DATE";
    final public static String SHARED_COLOR_HOLIDAY = "SHARED_COLOR_HOLIDAY";
    final public static String SHARED_TIME_PUSH = "SHARED_TIME_PUSH";
    final public static String SHARED_WEEK_SHOW = "SHARED_WEEK_SHOW";
    final public static String SHARED_CITY = "SHARED_CITY";
    final public static String SHARED_SOUND_BUTTON = "SHARED_SOUND_BUTTON";
    final public static String SHARED_LANGUAGE = "SHARED_LANGUAGE";
    final public static String SHARED_ALARM_SOUND = "SHARED_ALARM_SOUND";
    final public static String SHARED_GMAIL_EMAIL = "SHARED_EMAIL";
    final public static String SHARED_GMAIL_PASSWORD = "SHARED_GMAIL_PASSWORD";
    final public static String SHARED_VK_PHOTO = "SHARED_VK_PHOTO";
    final public static String SHARED_VK_FIRST_NAME = "SHARED_VK_FIRST_NAME";
    final public static String SHARED_VK_LAST_NAME = "SHARED_VK_LAST_NAME";
    final public static String SHARED_FACEBOOK_PHOTO = "SHARED_FACEBOOK_PHOTO";
    final public static String SHARED_RSS = "SHARED_RSS";
    final public static String SHARED_FACEBOOK_FIRST_NAME = "SHARED_FACEBOOK_FIRST_NAME";
    final public static String SHARED_FACEBOOK_LAST_NAME = "SHARED_FACEBOOK_LAST_NAME";
    final public static String SHARED_GOOGLEPLUS_PHOTO = "SHARED_GOOGLEPLUS_PHOTO";
    final public static String SHARED_GOOGLEPLUS_FIRST_NAME = "SHARED_GOOGLEPLUS_FIRST_NAME";
    final public static String SHARED_GOOGLEPLUS_LAST_NAME = "SHARED_GOOGLEPLUS_LAST_NAME";
    final public static String SHARED_BACGROUND_IMAGE = "SHARED_BACGROUND_IMAGE";
    final public static String SHARED_IS_AMPM = "SHARED_IS_AMPM";
    final public static String SHARED_HOLIDAYS = "SHARED_HOLIDAYS";

    ArrayList<OneStatus> STATUSES = new ArrayList<OneStatus>();
    String[] dialog_actions;
    public ShowDialog showDialog = new ShowDialog();
    public static MainActivity mainActivity;

    public String[] fragments = {"com.example.calendar.MounthFragment",
            "com.example.calendar.YearFragment",
            "com.example.calendar.WeekFragment",
            "com.example.calendar.ColorPickerFragment",
            "com.example.calendar.AddEventFragment",
            "com.example.calendar.ListEventOfDayFragment",
            "com.example.calendar.SettingsFragment",
            "com.example.calendar.OneEventFragment",
            "com.example.calendar.ListContacFragment",
            "com.example.calendar.WeekFragment2",
            "com.example.calendar.MounthFragment2",
            "com.example.calendar.StartNullFragment"};

    boolean isBuffered = false;
    public Fragment mounthFragment = null;
    public Fragment yearFragment = null;
    public Fragment weekFragment = null;
    public Fragment colorPickerFragment = null;
    public Fragment addEventFragment = null;
    public Fragment listEventOfDayFragment = null;
    public Fragment settingsFragment = null;
    public Fragment oneEventFragment = null;
    public Fragment listContactFragment = null;
    public Fragment weekFragment2 = null;
    public Fragment mounthFragment2 = null;
    public Fragment nullFragment = null;
    public int status_id = 0;
    final static int STATUS_YEAR = 0;
    final public static int STATUS_MOUNTH = 1;
    final public static int STATUS_MOUNTH2 = 2;
    final public static int STATUS_WEEK = 3;
    final public static int STATUS_WEEK2 = 4;
    final public static int STATUS_LIST_EVENT = 5;
    final public static int COLOR_PICKER = 6;
    final public static int STATUS_ADD_EVENT = 7;
    final public static int STATUS_SETTINGS = 8;
    final public static int STATUS_ONE_EVENT = 9;
    final public static int STATUS_LIST_CONTACT = 10;
    public User USER;
    static DBStorage dbStorage;
    static SQLiteDatabase DataBase;

    public static ArrayList<OneFriend> arrayOneFriendFacebook = new ArrayList<OneFriend>();

    ArrayList<OneCurrency> CurrencyByBank = new ArrayList<OneCurrency>();
    ArrayList<OneCurrency> allCurrency = new ArrayList<OneCurrency>();
    ArrayList<String> namesBanks = new ArrayList<String>();

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;
    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 111;
    ArrayList<OneEventGroup> arrayEventFromRightMenu = new ArrayList<OneEventGroup>();
    AdapterListGrEvent adapterListGrEvent;
    Button createGroup;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handlePush(intent);
    }

    private void handlePush(Intent intent) {

        isGroupEvent = intent.getBooleanExtra("ISGROUPEVENT", false);
        if (isGroupEvent) {
            // if GroupEvents activated

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                // group confirm dialog expected
                if (bundle.getBoolean(GcmIntentService.GROUP_CONFIRMATION)
                        && bundle
                        .getString(GcmIntentService.GROUP_CONFIRMATION_ID_GROUP) != null
                        && bundle
                        .getString(GcmIntentService.GROUP_CONFIRMATION_ID_EVENT) == null) {

                    String msg = intent
                            .getStringExtra(GcmIntentService.GROUP_MESSAGE);

                    String group_id = intent
                            .getStringExtra(GcmIntentService.GROUP_CONFIRMATION_ID_GROUP);

                    if (USER != null) {
                        new GroupConfirmation(this, msg, group_id,
                                String.valueOf(USER.getId()))
                                .setOnGroupSubmitListener(onGroupSubmitListener)
                                .show();
                    }
                } else if (bundle
                        .getBoolean(GcmIntentService.GROUP_CONFIRMATION)
                        && bundle
                        .getString(GcmIntentService.GROUP_CONFIRMATION_ID_EVENT) != null) {

                    String msg = intent
                            .getStringExtra(GcmIntentService.GROUP_MESSAGE);
                    String event_id = intent
                            .getStringExtra(GcmIntentService.GROUP_CONFIRMATION_ID_EVENT);
                    String group_id = intent
                            .getStringExtra(GcmIntentService.GROUP_CONFIRMATION_ID_GROUP);
                    String note_id = intent
                            .getStringExtra(GcmIntentService.GROUP_CONFIRMATION_ID_NOTE);

                    if (USER != null) {
                        new EventConfirmation(this, group_id, event_id,
                                note_id, msg, String.valueOf(USER.getId()))
                                .show();
                    }
                } else if (bundle.getBoolean(GcmIntentService.IS_JUST_MESSAGE)) {
                    QueryMaster.alert(this,
                            bundle.getString(GcmIntentService.GROUP_MESSAGE));
                }
            }

        }
    }

    private GroupConfirmation.OnGroupSubmitListener onGroupSubmitListener = new GroupConfirmation.OnGroupSubmitListener() {

        @Override
        public void submit() {
            new DrawerListActivity2(findViewById(R.id.main_right_menu2),
                    USER.getId() + "").init();
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);


        intent = getIntent();

        handlePush(intent);

        mainActivity = this;
        // dbStorage = new DBStorage(this, "default.db");
        // DB = dbStorage.getWritableDatabase();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // add_group_event = (RelativeLayout)
        // findVi ewById(R.id.add_event_group_rel);

        root = (ViewGroup) getWindow().getDecorView().findViewById(
                android.R.id.content);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();

        currens = (RelativeLayout) findViewById(R.id.currens_title_rel);
        shared = getSharedPreferences("shared", MODE_PRIVATE);
        language = new Languages(shared.getInt(SHARED_LANGUAGE, 1));
        add_event = (RelativeLayout) findViewById(R.id.add_event_rel);
        contacs = (RelativeLayout) findViewById(R.id.contacs_rel);
        rss = (RelativeLayout) findViewById(R.id.rss_rel);
        settings = (RelativeLayout) findViewById(R.id.settings_rel);
        exit = (RelativeLayout) findViewById(R.id.exit_rel);
        right_menu = (RelativeLayout) findViewById(R.id.main_right_menu1);
        left_menu = (RelativeLayout) findViewById(R.id.main_left_menu);
        vk = (RelativeLayout) findViewById(R.id.vk_rel);
        FirstStart = shared.getBoolean(SHARED_FIRST_START, true);
        about = (RelativeLayout) findViewById(R.id.about_rel);
        fb = (RelativeLayout) findViewById(R.id.fb_rel);
        gp = (RelativeLayout) findViewById(R.id.gp_rel);
        help = (RelativeLayout) findViewById(R.id.help_rel);
        arrayEventFromRightMenu = new ArrayList<OneEventGroup>();
        adapterListGrEvent = new AdapterListGrEvent(MainActivity.getInstance(),
                R.layout.one_gr_item, arrayEventFromRightMenu);
        list_gr_event = (ListView) findViewById(R.id.groupTasksList);
        list_gr_event.setAdapter(adapterListGrEvent);
        stock_rel = (RelativeLayout) findViewById(R.id.stock_title_rel);
        CreateDir();

        myDrawerListener = new MyDrawerListener();

        drawerLayout.setDrawerListener(myDrawerListener);

        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showHelp();
                drawerLayout.closeDrawers();
                new PlaySoundButton();
            }
        });

        gp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                Intent intent = new Intent(MainActivity.getInstance(),
                        GooglePlusActivity.class);
                startActivity(intent);
            }
        });

        rss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // drawerLayout.closeDrawers();
                new PlaySoundButton();
                showRssList(MainActivity.getInstance());

            }
        });
        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                drawerLayout.closeDrawers();
                showAbout();
                new PlaySoundButton();
            }
        });

        stock_rel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                // showStockSettings();

                showStock(MainActivity.getInstance());

            }
        });
        currens.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                // showDialog(123);

                showCurrency(MainActivity.getInstance());
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startSettingsFragment();
                drawerLayout.closeDrawers();
                new PlaySoundButton();
            }
        });

        contacs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startListContactFragment();
                drawerLayout.closeDrawers();
                new PlaySoundButton();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Synchronize();
                StopSynchronize(t);
                drawerLayout.closeDrawers();
                new PlaySoundButton();

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        SendToken(USER.getId(), "");
                    }
                }).start();

                shared.edit().putString("login", "").commit();
                shared.edit().putString("password", "").commit();
                shared.edit().putString("email", "").commit();
                shared.edit().putString("name", "").commit();
                shared.edit().putString("surname", "").commit();
                shared.edit().putString("phone", "").commit();
                shared.edit().putString("src", "").commit();
                shared.edit().putInt("id", -1).commit();

                shared.edit().putString(SHARED_FACEBOOK_PHOTO, "").commit();
                shared.edit().putString(SHARED_VK_PHOTO, "").commit();
                shared.edit().putString(SHARED_GOOGLEPLUS_PHOTO, "").commit();

                intent = new Intent(MainActivity.getInstance(),
                        AutorizationActivity.class);
                startActivityForResult(intent, 2301);
                startNullFragment();

            }
        });

        add_event.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (status_id != STATUS_ADD_EVENT)
                    startAddEventFragment(false, false, getNowYear(),
                            getNowMounth(), getNowDay(), getNowHour(),
                            getNowMinute());
                else {
                    AddEventFragment.getInstance().isGroup = false;
                    AddEventFragment.getInstance().setVisibilityComponents();
                }
                new PlaySoundButton();
                drawerLayout.closeDrawers();

            }
        });
        showError = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                int mes = (Integer) msg.obj;

                String text = "";
                switch (mes) {
                    case 200:
                        text = language._200;
                        break;
                    case 201:
                        text = language._201;
                        break;

                    case 202:
                        text = language._202;
                        break;

                    case 203:
                        text = language._203;
                        break;

                    case 204:
                        text = language._204;
                        break;

                    case 205:
                        text = language._205;
                        break;

                    case 206:
                        text = language._206;
                        break;
                    case 207:
                        text = language._207;
                        break;

                    case 208:
                        text = language._208;
                        break;

                    case 209:
                        text = language._209;
                        break;

                    case 1:

                        if (status_id == STATUS_MOUNTH)
                            MounthFragment.getInstance().mounth.invalidate();
                        else
                            startMounthFragment(getNowYear(), getNowMounth(),
                                    getNowDay());
                        findViewById(R.id.main_splesh).setVisibility(View.GONE);

                        new DrawerListActivity2(
                                findViewById(R.id.main_right_menu2), USER.getId()
                                + "").init();
                        checkFirstStartAndShowTips();
                        break;

                    case 11:

                        // if (status_id == STATUS_MOUNTH)
                        // MounthFragment.getInstance().mounth.invalidate();
                        // else
                        // startMounthFragment(getNowYear(), getNowMounth(),
                        // getNowDay());
                        findViewById(R.id.main_splesh).setVisibility(View.GONE);

                        new DrawerListActivity2(
                                findViewById(R.id.main_right_menu2), USER.getId()
                                + "").init();
                        checkFirstStartAndShowTips();
                        break;

                    case 123:
                        // setTextCurrents(CurrencyByBank);
                        // ((TextView) findViewById(R.id.currens_name_bank))
                        // .setText(namesBanks.get(shared.getInt("bank", 0)));

                        break;

                }

                if (text != "")
                    Toast.makeText(MainActivity.getInstance(), text,
                            Toast.LENGTH_SHORT).show();
            }
        };

        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                drawerLayout.closeDrawers();

                if (InternetConnection()) {
                    arrayOneFriendFacebook.clear();
                    final FaceBook f = new FaceBook(MainActivity.getInstance(),
                            arrayOneFriendFacebook);
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
                            new AddBirthday(arrayOneFriendFacebook).Add();
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

                } else
                    Toast.makeText(MainActivity.getInstance(),
                            language.NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT)
                            .show();
            }
        });

        mounthFragment = Fragment.instantiate(this, fragments[0]);
        yearFragment = Fragment.instantiate(this, fragments[1]);
        weekFragment = Fragment.instantiate(this, fragments[2]);
        colorPickerFragment = Fragment.instantiate(this, fragments[3]);
        addEventFragment = Fragment.instantiate(this, fragments[4]);
        weekFragment2 = Fragment.instantiate(this, fragments[9]);
        mounthFragment2 = Fragment.instantiate(this, fragments[10]);
        oneEventFragment = Fragment.instantiate(this, fragments[7]);
        nullFragment = Fragment.instantiate(this, fragments[11]);

        vk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                drawerLayout.closeDrawers();
                if (InternetConnection()) {
                    ArrayList<OneFriend> result = new ArrayList<OneFriend>();
//                    new VK(MainActivity.getInstance(), drawerLayout, result,
//                            false, null);
                } else
                    Toast.makeText(MainActivity.getInstance(),
                            language.NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT)
                            .show();

                new PlaySoundButton();

            }
        });

        setColor();

        setText();

        String login = shared.getString("login", "");
        String password = shared.getString("password", "");
        int id_user = shared.getInt("id", 0);
        String src = shared.getString("src", "");
        String email = shared.getString("email", "");
        String name = shared.getString("name", "");
        String surname = shared.getString("surname", "");
        String phone = shared.getString("phone", "");

        if (login.length() > 0) {

            USER = new User(login, password, id_user, src, email, name,
                    surname, phone);
            dbStorage = new DBStorage(MainActivity.getInstance(),
                    shared.getString("db", "user.db"));

            DataBase = dbStorage.getWritableDatabase();
            getCities();
            getPhoneContacs();
            Synchronize();
            StartSynchronize(t);
            getWeather();
            googleCalendarSync();
            try {

                stopService(new Intent(MainActivity.getInstance(),
                        MyService.class));
                startService(new Intent(MainActivity.getInstance(),
                        MyService.class));
            } catch (Exception e) {
                // TODO: handle exception
                Log.e(getClass().toString()
                        + "line = "
                        + Thread.currentThread().getStackTrace()[2]
                        .getLineNumber(), "catch:" + e);
            }
        }

        ispush = intent.getBooleanExtra(MyService.IS_PUSH_LOCALY, false);
        if (ispush) {

            new DrawerListActivity2(findViewById(R.id.main_right_menu2),
                    USER.getId() + "").init();

            int id = intent.getExtras().getInt("IDEVENT");
            String title = intent.getExtras().getString("TITLE");
            String info = intent.getExtras().getString("INFO");
            Push(id, title, info);

            final OneEvent event = getEventById(id);

            startMounthFragment(getNowYear(), getNowMounth(), getNowDay());

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    MounthFragment.getInstance().arrayOneEventAndWeaters
                            .clear();
                    MounthFragment.getInstance().arrayOneEventAndWeaters
                            .add(new OneEventAndWeater(event));
                    startOneEventFragment(0);
                    showError(11);
                }
            }, 1000);
        }

        isGroupEvent = intent.getBooleanExtra("ISGROUPEVENT", false);
        if (isGroupEvent) {
            new DrawerListActivity2(findViewById(R.id.main_right_menu2),
                    USER.getId() + "").init();

            findViewById(R.id.main_splesh).setVisibility(View.GONE);
            startMounthFragment(getNowYear(), getNowMounth(), getNowDay());

            QueryMaster.alert(this, "isGroup");

            if (intent.getExtras().getBoolean(
                    GcmIntentService.GROUP_CONFIRMATION)) {

                QueryMaster.alert(this, "isGroup 2");
                String msg = intent
                        .getStringExtra(GcmIntentService.GROUP_MESSAGE);
                String group_id = intent
                        .getStringExtra(GcmIntentService.GROUP_CONFIRMATION_ID_GROUP);

                if (USER != null) {
                    new GroupConfirmation(this, msg, group_id,
                            String.valueOf(USER.getId())).show();
                }
            }

        }

        if (!isGroupEvent && !ispush) {

            intent = new Intent(this, AutorizationActivity.class);

            if (login.length() == 0) {
                startActivityForResult(intent, 2301);
            } else {

                startMounthFragment(getNowYear(), getNowMounth(), getNowDay());

                findViewById(R.id.main_splesh).setVisibility(View.GONE);

                new DrawerListActivity2(findViewById(R.id.main_right_menu2),
                        USER.getId() + "").init();

            }

        }

        setBacground();
        getStocks();
        getCurrens();

//        startActivity(new Intent(this, SpleshActivity.class));

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        try {

            Synchronize();

            dbStorage.close();

            if (!shared.getBoolean(SHARED_SAVED_LOGIN_PASSWORD, false)) {

                Synchronize();
                StopSynchronize(t);
                shared.edit().putString("login", "").commit();
                shared.edit().putString("password", "").commit();
                shared.edit().putString("email", "").commit();
                shared.edit().putString("name", "").commit();
                shared.edit().putString("surname", "").commit();
                shared.edit().putString("phone", "").commit();
                shared.edit().putString("src", "").commit();
                shared.edit().putInt("id", -1).commit();
                shared.edit().putString(SHARED_FACEBOOK_PHOTO, "").commit();
                shared.edit().putString(SHARED_VK_PHOTO, "").commit();
                shared.edit().putString(SHARED_GOOGLEPLUS_PHOTO, "").commit();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1665 && data != null) {
            try {
                importIcal(AddEventFragment.getRealPathFromURI(data.getData()));

                AddEventFragment.getRealPathFromURI(data.getData());
            } catch (Exception e) {

                if (data == null)
                    Log.e(getClass().toString(), "catch: data null e = " + e);
                else
                    Log.e(getClass().toString(), "catch: data  not null e = "
                            + e);

            }
        }

        if (requestCode == 2301) {
            stopService(new Intent(MainActivity.getInstance(), MyService.class));

            shared.edit().putString("login", USER.getLogin()).commit();
            shared.edit().putString("password", USER.getPassword()).commit();
            shared.edit().putString("email", USER.getEmail()).commit();
            shared.edit().putString("name", USER.getName()).commit();
            shared.edit().putString("surname", USER.getSurname()).commit();
            shared.edit().putString("phone", USER.getPhone()).commit();
            shared.edit().putString("src", USER.getSrc()).commit();
            shared.edit().putInt("id", USER.getId()).commit();
            shared.edit().putString(SHARED_VK_FIRST_NAME, "").commit();
            shared.edit().putString(SHARED_VK_LAST_NAME, "").commit();
            shared.edit().putString(SHARED_VK_PHOTO, "").commit();

            shared.edit().putString(SHARED_FACEBOOK_FIRST_NAME, "").commit();
            shared.edit().putString(SHARED_FACEBOOK_LAST_NAME, "").commit();
            shared.edit().putString(SHARED_FACEBOOK_PHOTO, "").commit();

            shared.edit().putString(SHARED_GOOGLEPLUS_FIRST_NAME, "").commit();
            shared.edit().putString(SHARED_GOOGLEPLUS_LAST_NAME, "").commit();
            shared.edit().putString(SHARED_GOOGLEPLUS_PHOTO, "").commit();

            if (USER.getSrc().equalsIgnoreCase("206")) {
                dbStorage = new DBStorage(MainActivity.getInstance(),
                        USER.getLogin() + "DB.db");
                DataBase = dbStorage.getWritableDatabase();

                getCities();
                getPhoneContacs();
                Synchronize();
                showError(1);
                StartSynchronize(t);
                shared.edit().putString("db", USER.getLogin() + "DB.db")
                        .commit();
                startService(new Intent(MainActivity.getInstance(),
                        MyService.class));

                // Log.e(getClass().toString(), "database created localy");

                // Log.e(getClass().toString(), "user id = " + USER.getId());

                // new DrawerListActivity(findViewById(R.id.main_right_menu2))
                // .init();
                // new DrawerListActivity(findViewById(R.id.main_right_menu2),
                // USER.getId() + "").init();
                // checkFirstStartAndShowTips();
            } else {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        FileLoader fl = new FileLoader();

                        // Log.e(getClass().toString(),
                        // "load file: src = " + USER.getSrc()
                        // + "   file name = "
                        // + getFileName(USER.getSrc()));
                        fl.loadFile(USER.getSrc(), getFileName(USER.getSrc()));

                        while (fl.isLoad) {
                            try {
                                Thread.sleep(300);
                            } catch (Exception e) {
                                // TODO: handle exception
                                Log.e(getClass().toString()
                                        + "line = "
                                        + Thread.currentThread()
                                        .getStackTrace()[2]
                                        .getLineNumber(), "catch:" + e);
                            }
                        }
                        // Log.e(getClass().toString(),
                        // "database loaded from internet");
                        try {
                            dbStorage = new DBStorage(MainActivity
                                    .getInstance(), getFileName(USER.getSrc()));
                            DataBase = dbStorage.getWritableDatabase();
                            copyDataBase(getFileName(USER.getSrc()));

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            // e.printStackTrace();
                            Log.e("", "copyDataBase error " + e);
                            throw new RuntimeException(e);
                        }

                        getCities();
                        getPhoneContacs();
                        Synchronize();
                        showError(1);
                        StartSynchronize(t);
                        shared.edit()
                                .putString("db", getFileName(USER.getSrc()))
                                .commit();

                        startService(new Intent(MainActivity.getInstance(),
                                MyService.class));

                        // new DrawerListActivity(
                        // findViewById(R.id.main_right_menu2)).init();

                        // checkFirstStartAndShowTips();

                        try {
                            Thread.interrupted();
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(getClass().toString()
                                    + "line = "
                                    + Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber(), "catch:" + e);
                        }
                    }
                }).start();

            }
            getWeather();
            googleCalendarSync();
        }

    }

    public static MainActivity getInstance() {
        return mainActivity;

    }

    public static String MOUNTH_FRAGMENT_YEAR = "mfy";
    public static String MOUNTH_FRAGMENT_MOUNTH = "mfm";
    public static String MOUNTH_FRAGMENT_DAY = "mfd";

    public void startMounthFragment(int year, int mounth, int day) {
        if (status_id != STATUS_MOUNTH) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (mounthFragment == null)
                mounthFragment = Fragment.instantiate(this, fragments[0]);

            Bundle bundle = new Bundle();
            bundle.putInt(MOUNTH_FRAGMENT_YEAR, year);
            bundle.putInt(MOUNTH_FRAGMENT_MOUNTH, mounth);
            bundle.putInt(MOUNTH_FRAGMENT_DAY, day);
            mounthFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, mounthFragment);
            fragmentTransaction.commit();
            status_id = STATUS_MOUNTH;
            STATUSES.add(new OneStatus(status_id, year, mounth, day));
        }
    }

    public static String MOUNTH_FRAGMENT2_YEAR = "mf2y";
    public static String MOUNTH_FRAGMENT2_MOUNTH = "mf2m";

    public void startMounthFragment2(int year, int mounth) {
        if (status_id != STATUS_MOUNTH2) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (mounthFragment2 == null)
                mounthFragment2 = Fragment.instantiate(this, fragments[10]);

            Bundle bundle = new Bundle();
            bundle.putInt(MOUNTH_FRAGMENT2_YEAR, year);
            bundle.putInt(MOUNTH_FRAGMENT2_MOUNTH, mounth);

            mounthFragment2.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, mounthFragment2);
            fragmentTransaction.commit();
            status_id = STATUS_MOUNTH2;
            STATUSES.add(new OneStatus(status_id, year, mounth, 0));
        }
    }

    public void startListContactFragment() {
        if (status_id != STATUS_LIST_CONTACT) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (listContactFragment == null)
                listContactFragment = Fragment.instantiate(this, fragments[8]);

            fragmentTransaction.replace(R.id.main, listContactFragment);
            fragmentTransaction.commit();
            status_id = STATUS_LIST_CONTACT;
            STATUSES.add(new OneStatus(status_id, 0, 0, 0));
        }
    }

    public static String WEEK_FRAGMENT2_YEAR = "wf2y";
    public static String WEEK_FRAGMENT2_MOUNTH = "wf2m";
    public static String WEEK_FRAGMENT2_DAY = "wf2d";

    public void startWeekFragment2(int year, int mounth, int day) {
        if (status_id != STATUS_WEEK2) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (weekFragment2 == null)
                weekFragment2 = Fragment.instantiate(this, fragments[9]);

            Bundle bundle = new Bundle();
            bundle.putInt(WEEK_FRAGMENT2_YEAR, year);
            bundle.putInt(WEEK_FRAGMENT2_MOUNTH, mounth);
            bundle.putInt(WEEK_FRAGMENT2_DAY, day);

            weekFragment2.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, weekFragment2);
            fragmentTransaction.commit();
            status_id = STATUS_WEEK2;

            STATUSES.add(new OneStatus(status_id, year, mounth, day));
        }
    }

    public static String YEAR_FRAGMENT_YEAR = "yfy";
    public static String YEAR_FRAGMENT_NAMES_MOUNTH = "yfn";

    public void startYearFragment(int year, String[] names_of_mounth) {
        if (status_id != STATUS_YEAR) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (yearFragment == null)
                yearFragment = Fragment.instantiate(this, fragments[1]);

            Bundle bundle = new Bundle();
            bundle.putInt(YEAR_FRAGMENT_YEAR, year);
            bundle.putStringArray(YEAR_FRAGMENT_NAMES_MOUNTH, names_of_mounth);
            yearFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, yearFragment);
            fragmentTransaction.commit();
            status_id = STATUS_YEAR;
            STATUSES.add(new OneStatus(status_id, year, 0, 0));
        }
    }

    public void startNullFragment() {
        status_id = -1;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (nullFragment == null)
            nullFragment = Fragment.instantiate(this, fragments[11]);

        fragmentTransaction.replace(R.id.main, nullFragment);
        fragmentTransaction.commit();

    }

    public static String WEEK_FRAGMENT_COUNT_COL = "cc";
    public static String WEEK_FRAGMENT_COUNT_ROW = "cr";
    public static String WEEK_FRAGMENT_FIRST_DAY = "fd";
    public static String WEEK_FRAGMENT_NUMBER_MOUNTH = "nm";
    public static String WEEK_FRAGMENT_YEAR = "y";
    public static String WEEK_FRAGMENT_PREV = "pr";
    public static String WEEK_FRAGMENT_NAME_DAYS = "nd";

    public void startWeekFragment(int count_col, int count_row, int first_day,
                                  int number_mounth, int year, boolean prev, String[] name_days) {
        if (status_id != STATUS_WEEK) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (weekFragment == null)
                weekFragment = Fragment.instantiate(this, fragments[2]);

            Bundle bundle = new Bundle();

            bundle.putInt(WEEK_FRAGMENT_COUNT_COL, count_col);
            bundle.putInt(WEEK_FRAGMENT_COUNT_ROW, count_row);
            bundle.putInt(WEEK_FRAGMENT_FIRST_DAY, first_day);
            bundle.putInt(WEEK_FRAGMENT_NUMBER_MOUNTH, number_mounth);
            bundle.putInt(WEEK_FRAGMENT_YEAR, year);
            bundle.putBoolean(WEEK_FRAGMENT_PREV, prev);
            bundle.putStringArray(WEEK_FRAGMENT_NAME_DAYS, name_days);
            weekFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, weekFragment);
            fragmentTransaction.commit();
            status_id = STATUS_WEEK;
            STATUSES.add(new OneStatus(status_id, year, number_mounth,
                    first_day));
        }
    }

    public void startColorPickerFragment() {
        if (status_id != COLOR_PICKER) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (colorPickerFragment == null)
                colorPickerFragment = Fragment.instantiate(this, fragments[3]);

            fragmentTransaction.replace(R.id.main, colorPickerFragment);
            fragmentTransaction.commit();
            status_id = COLOR_PICKER;
            STATUSES.add(new OneStatus(status_id, 0, 0, 0));
        }
    }

    public void startSettingsFragment() {
        if (status_id != STATUS_SETTINGS) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (settingsFragment == null)
                settingsFragment = Fragment.instantiate(this, fragments[6]);

            fragmentTransaction.replace(R.id.main, settingsFragment);
            fragmentTransaction.commit();
            status_id = STATUS_SETTINGS;
            STATUSES.add(new OneStatus(status_id, 0, 0, 0));
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();

        new PlaySoundButton();
        if (STATUSES.size() <= 1) {
            finish();
        } else {
            STATUSES.remove(STATUSES.size() - 1);
            OneStatus s = STATUSES.get(STATUSES.size() - 1);
            switch (s.getId()) {
                case STATUS_ADD_EVENT:
                    startAddEventFragment(false, false, s.getYear(), s.getMounth(),
                            s.getDay(), -1, -1);
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_YEAR:
                    startYearFragment(s.getYear(), language.NAMES_MOUNTH);
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_MOUNTH:
                    startMounthFragment(s.getYear(), s.getMounth(), s.getDay());
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_WEEK:
                    startWeekFragment(5, 15, s.getDay(), s.getMounth(),
                            s.getYear(), false, language.NAMES_DAYS_WEEK);
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case COLOR_PICKER:
                    startColorPickerFragment();
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_LIST_EVENT:
                    startListEventOfDay(s.getYear(), s.getMounth(), s.getDay());

                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_SETTINGS:
                    startSettingsFragment();

                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_ONE_EVENT:
                    startOneEventFragment(s.getPosition());

                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_LIST_CONTACT:
                    startListContactFragment();

                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_WEEK2:
                    startWeekFragment2(s.getYear(), s.getMounth(), s.getDay());
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

                case STATUS_MOUNTH2:
                    startMounthFragment2(s.getYear(), s.getMounth());
                    STATUSES.remove(STATUSES.size() - 1);

                    break;

            }

        }

    }

    ;

    public Fragment getFragment(int index) {
        return Fragment.instantiate(this, fragments[index]);
    }

    public static String ADD_EVENT_YEAR = "aey";
    public static String ADD_EVENT_MOUNTH = "aem";
    public static String ADD_EVENT_DAY = "aed";
    public static String ADD_EVENT_HOUR = "aeh";
    public static String ADD_EVENT_MINUTE = "aemin";
    public static String ADD_EVENT_ISGROUP = "aeisgroup";
    public static String ADD_EVENT_ISUPDATE = "aeisupdate";

    public void startAddEventFragment(boolean isGroup, boolean isUpdate,
                                      int year, int mounth, int day, int hour, int minute) {
        if (status_id != STATUS_ADD_EVENT) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            // if (addEventFragment == null)
            addEventFragment = Fragment.instantiate(this, fragments[4]);

            Bundle bundle = new Bundle();
            bundle.putInt(ADD_EVENT_YEAR, year);
            bundle.putInt(ADD_EVENT_MOUNTH, mounth);
            bundle.putInt(ADD_EVENT_DAY, day);
            bundle.putInt(ADD_EVENT_HOUR, hour);
            bundle.putInt(ADD_EVENT_MINUTE, minute);
            bundle.putBoolean(ADD_EVENT_ISGROUP, isGroup);
            bundle.putBoolean(ADD_EVENT_ISUPDATE, isUpdate);

            addEventFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, addEventFragment);
            fragmentTransaction.commit();
            status_id = STATUS_ADD_EVENT;
            STATUSES.add(new OneStatus(status_id, year, mounth, day));
        }
    }

    public static String LIST_EVENT_YEAR = "ley";
    public static String LIST_EVENT_MOUNTH = "lem";
    public static String LIST_EVENT_DAY = "led";

    public void startListEventOfDay(int year, int mounth, int day) {
        if (status_id != STATUS_LIST_EVENT) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (listEventOfDayFragment == null)
                listEventOfDayFragment = Fragment.instantiate(this,
                        fragments[5]);

            Bundle bundle = new Bundle();
            bundle.putInt(LIST_EVENT_YEAR, year);
            bundle.putInt(LIST_EVENT_MOUNTH, mounth);
            bundle.putInt(LIST_EVENT_DAY, day);
            listEventOfDayFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, listEventOfDayFragment);
            fragmentTransaction.commit();
            status_id = STATUS_LIST_EVENT;
            STATUSES.add(new OneStatus(status_id, year, mounth, day));
        }
    }

    public static String ONE_EVENT_FRAGMENT_POSITION = "oefp";

    public void startOneEventFragment(int position) {
        if (status_id != STATUS_ONE_EVENT) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if (oneEventFragment == null)
                oneEventFragment = Fragment.instantiate(this, fragments[7]);

            Bundle bundle = new Bundle();
            bundle.putInt(ONE_EVENT_FRAGMENT_POSITION, position);

            oneEventFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main, oneEventFragment);
            fragmentTransaction.commit();
            status_id = STATUS_ONE_EVENT;
            STATUSES.add(new OneStatus(status_id, position));
        }
    }

    public void AddEventToDB(int calID, final String title, final int color,
                             final String start_date, final String end_date,
                             final String category, final String location, final String info,
                             final String file_path, final String push_time, final String sound,
                             final int isDone) {

        MyDate start = new MyDate(start_date);
        MyDate end = new MyDate(end_date);

        OneEvent e = new OneEvent(0, title, color, start.getYear(),
                start.getMounth(), start.getDay(), start.getHour(),
                start.getMinute(), end.getYear(), end.getMounth(),
                end.getDay(), end.getHour(), end.getMinute(), category,
                location, info, 0, file_path, push_time, sound, isDone);

        if (calID != -1)
            new CalendarService(this).sendEvent(e, calID);

        AddEventToDB1(title, color, start_date, end_date, category, location,
                info, file_path, push_time, sound, isDone);

    }

    // public void setEqualPercent(int root_id) {
    // int min_percent = 0;
    // Cursor c = DB.rawQuery("select min(status) from "
    // + dbStorage.EVENT_TABLE + " where root_event = " + root_id,
    // null);
    // if (c.moveToFirst()) {
    // min_percent = c.getInt(0);
    // }
    // c.close();
    // ContentValues cv = new ContentValues();
    // cv.put("status", min_percent);
    // DB.update(dbStorage.EVENT_TABLE, cv, "root_event = " + root_id, null);
    //
    // }

    static public String FormatDate(int zn) {
        if (zn < 10)
            return "0" + zn;
        return zn + "";
    }

    // public int getMaxId() {
    // int res = 0;
    // Cursor c = DB.rawQuery("select max(id) from  " + dbStorage.EVENT_TABLE,
    // null);
    //
    // if (c.moveToFirst()) {
    // res = c.getInt(0);
    //
    // }
    // c.close();
    // return res;
    // }

    public void AddEventToDB1(String title, int color, String start_date,
                              String end_date, String category, String location, String info,
                              String file_path, String push_time, String sound, int isDone) {
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("color", color);
        cv.put("category", category);
        cv.put("location", location);
        cv.put("info", info);
        cv.put("isDone", isDone);
        cv.put("sound", sound);

        String start_milisec = String.valueOf(StringSmallToMilisec(start_date
                .substring(0, 10)));
        String end_milisec = String.valueOf(StringSmallToMilisec(end_date
                .substring(0, 10)));
        String start_milisec_long = String
                .valueOf(StringSmallToMilisec(start_date));
        String end_milisec_long = String
                .valueOf(StringSmallToMilisec(end_date));

        cv.put("end_milisec", end_milisec);
        cv.put("start_milisec", start_milisec);

        cv.put("start_milisec_long", start_milisec_long);
        cv.put("end_milisec_long", end_milisec_long);

        cv.put("time_push", push_time);
        MyDate date_start = new MyDate(start_date);
        cv.put("start_date_year", date_start.getYear());
        cv.put("start_date_mounth", date_start.getMounth());
        cv.put("start_date_day", date_start.getDay());
        cv.put("start_date_hour", date_start.getHour());
        cv.put("start_date_minute", date_start.getMinute());

        MyDate date_end = new MyDate(end_date);
        cv.put("end_date_year", date_end.getYear());
        cv.put("end_date_mounth", date_end.getMounth());
        cv.put("end_date_day", date_end.getDay());
        cv.put("end_date_hour", date_end.getHour());
        cv.put("end_date_minute", date_end.getMinute());

        cv.put("file_path", file_path);

        cv.put("status", 1);

        try {

            DataBase.insert(dbStorage.EVENT_TABLE, null, cv);

            Cursor cccc = DataBase.rawQuery("select MAX(id) from "
                    + DBStorage.EVENT_TABLE, null);

            if (cccc.moveToFirst()) {

                int id = cccc.getInt(0);

                Log.e(getClass().toString(), "add new  event  id  = " + id);

            }

        } catch (Exception e) {
        } finally {
        }
        cv.clear();

    }

    public static int getNowYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowMounth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public void setColor() {

        // ((LinearLayout) findViewById(R.id.left_menu_lin))
        // .setBackgroundColor(-2130706432);

        if (THEME_APPLICATION == DrawMounth.BASE_THEME) {

            ((ImageView) findViewById(R.id.add_event_image))
                    .setImageResource(R.drawable.add_event_base_theme);

            ((ImageView) findViewById(R.id.gp_image))
                    .setImageResource(R.drawable.gp_bace_theme);

            ((ImageView) findViewById(R.id.fb_image))
                    .setImageResource(R.drawable.facebook_base_theme);

            ((ImageView) findViewById(R.id.vk_image))
                    .setImageResource(R.drawable.vk_base_theme);

            ((ImageView) findViewById(R.id.contacs_image))
                    .setImageResource(R.drawable.contacs_base_theme);

            ((ImageView) findViewById(R.id.settings_image))
                    .setImageResource(R.drawable.settings_base_theme);

            ((ImageView) findViewById(R.id.help_image))
                    .setImageResource(R.drawable.help_base_theme);

            ((ImageView) findViewById(R.id.stock_image))
                    .setImageResource(R.drawable.stocks_base_theme);

            ((ImageView) findViewById(R.id.currens_image))
                    .setImageResource(R.drawable.currens_base_theme);

            ((ImageView) findViewById(R.id.rss_image))
                    .setImageResource(R.drawable.rss_base_theme);

            ((ImageView) findViewById(R.id.exit_image))
                    .setImageResource(R.drawable.exit_base_theme);

            ((ImageView) findViewById(R.id.about_image))
                    .setImageResource(R.drawable.about_base_theme);

            findViewById(R.id.add_event_line).setVisibility(View.GONE);
            findViewById(R.id.contacs_line).setVisibility(View.GONE);
            findViewById(R.id.settings_line).setVisibility(View.GONE);
            findViewById(R.id.soc_seti_line).setVisibility(View.GONE);
            findViewById(R.id.help_line).setVisibility(View.GONE);
            findViewById(R.id.stock_line).setVisibility(View.GONE);
            findViewById(R.id.currens_line).setVisibility(View.GONE);
            findViewById(R.id.rss_line).setVisibility(View.GONE);
            findViewById(R.id.exitt_line).setVisibility(View.GONE);
            findViewById(R.id.about_line).setVisibility(View.GONE);

            ((RelativeLayout) findViewById(R.id.main_left_menu))
                    .setBackgroundResource(R.drawable.base_theme_background);

            ((RelativeLayout) findViewById(R.id.main_left_menu_bacground))
                    .setBackgroundColor(-2130706432);

            ((RelativeLayout) findViewById(R.id.main_right_menu1))
                    .setBackgroundResource(R.drawable.base_theme_background);

            ((RelativeLayout) findViewById(R.id.main_right_menu_bacground))
                    .setBackgroundColor(-2130706432);

        }

    }

    public void ClearTableInDatabase(String name_table) {

        try {
            DataBase.delete(name_table, null, null);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
        Synchronize();

    }

    public boolean isEventSaved(OneEvent e) {

        Cursor c = DataBase.rawQuery("select * from " + DBStorage.EVENT_TABLE
                + " where " + "start_date_year = " + e.getStart_date_year()
                + " AND start_date_mounth = " + e.getStart_date_mounth()
                + " AND start_date_day = " + e.getStart_date_day() +

                " AND start_date_hour = " + e.getStart_date_hour()
                + " AND start_date_minute = " + e.getStart_date_minute() +

                " AND end_date_year = " + e.getEnd_date_year()
                + " AND end_date_mounth = " + e.getEnd_date_mounth()
                + " AND end_date_day = " + e.getEnd_date_day()
                + " AND end_date_hour = " + e.getEnd_date_hour()
                + " AND end_date_minute = " + e.getEnd_date_minute()
                + " AND title = \"" + e.getTitle() + "\";", null);

        if (c.getCount() == 0) {
            // Log.e(getClass().toString(),
            // "event: "+e.getTitle()+" no saved in DB");
            return false;
        } else {
            c.close();

            // /
            // Log.e(getClass().toString(),
            // "event: "+e.getTitle()+" saved in DB");

            return true;
        }

    }

    public OneEvent getOneEventByDate(int start_year, int start_mounth,
                                      int start_day, int start_hour, int start_minute, int end_year,
                                      int end_mounth, int end_day, int end_hour, int end_minute) {
        OneEvent result = null;

        Cursor c = DataBase.rawQuery("select * from " + DBStorage.EVENT_TABLE
                + " where" + " start_date_year = " + start_year
                + "AND start_date_mounth = " + start_mounth
                + "AND start_date_day = " + start_day
                + "AND start_date_hour = " + start_hour
                + "AND start_date_minute = " + start_minute
                + " AND end_date_year = " + end_year + "AND end_date_mounth = "
                + end_mounth + "AND end_day_ = " + end_day
                + "AND end_date_hour = " + end_hour + "AND end_date_minute = "
                + end_minute + ";", null);

        if (c.moveToFirst()) {
            result = new OneEvent(c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("title")), c.getInt(c
                    .getColumnIndex("color")), c.getInt(c
                    .getColumnIndex("start_date_year")), c.getInt(c
                    .getColumnIndex("start_date_mounth")), c.getInt(c
                    .getColumnIndex("start_date_day")), c.getInt(c
                    .getColumnIndex("start_date_hour")), c.getInt(c
                    .getColumnIndex("start_date_minute")), c.getInt(c
                    .getColumnIndex("end_date_year")), c.getInt(c
                    .getColumnIndex("end_date_mounth")), c.getInt(c
                    .getColumnIndex("end_date_day")), c.getInt(c
                    .getColumnIndex("end_date_hour")), c.getInt(c
                    .getColumnIndex("end_date_minute")), c.getString(c
                    .getColumnIndex("category")), c.getString(c
                    .getColumnIndex("location")), c.getString(c
                    .getColumnIndex("info")), c.getInt(c
                    .getColumnIndex("status")), c.getString(c
                    .getColumnIndex("file_path")), c.getString(c
                    .getColumnIndex("time_push")), c.getString(c
                    .getColumnIndex("sound")), c.getInt(c
                    .getColumnIndex("isDone")));
        }
        c.close();

        return result;

    }

    public ArrayList<OneEvent> getEventsByDate(int year, int mounth, int day) {
        ArrayList<OneEvent> result = new ArrayList<OneEvent>();

        try {

            String date = FormatDate(year) + "-" + FormatDate(mounth) + "-"
                    + FormatDate(day);

            long date_milisec = StringSmallToMilisec(date);

            Cursor c = DataBase
                    .rawQuery("select * from " + dbStorage.EVENT_TABLE + " "
                            + "where start_milisec<=" + date_milisec
                            + " and end_milisec>= " + date_milisec, null);
            if (c.moveToFirst()) {
                int id_index = c.getColumnIndex("id");
                int title_index = c.getColumnIndex("title");
                int color_index = c.getColumnIndex("color");
                int start_date_year_index = c.getColumnIndex("start_date_year");
                int start_date_mounth_index = c
                        .getColumnIndex("start_date_mounth");
                int start_date_day_index = c.getColumnIndex("start_date_day");
                int start_date_hour_index = c.getColumnIndex("start_date_hour");
                int start_date_minute_index = c
                        .getColumnIndex("start_date_minute");
                int end_date_year_index = c.getColumnIndex("end_date_year");
                int end_date_mounth_index = c.getColumnIndex("end_date_mounth");
                int end_date_day_index = c.getColumnIndex("end_date_day");
                int end_date_hour_index = c.getColumnIndex("end_date_hour");
                int end_date_minute_index = c.getColumnIndex("end_date_minute");
                int category_index = c.getColumnIndex("category");
                int location_index = c.getColumnIndex("location");
                int info_index = c.getColumnIndex("info");
                int status_index = c.getColumnIndex("status");
                int file_path_index = c.getColumnIndex("file_path");
                int time_push_index = c.getColumnIndex("time_push");
                int sound_index = c.getColumnIndex("sound");
                int isDone_index = c.getColumnIndex("isDone");
                int start_milisec_index = c.getColumnIndex("start_milisec");
                int end_milisec_index = c.getColumnIndex("end_milisec");

                do {

                    long start_milisec = Long.parseLong(c
                            .getString(start_milisec_index));
                    long end_milisec = Long.parseLong(c
                            .getString(end_milisec_index));

                    if (end_milisec > start_milisec) {
                        if (start_milisec == date_milisec) {

                            result.add(new OneEvent(c.getInt(id_index), c
                                    .getString(title_index), c
                                    .getInt(color_index), year, mounth, day, c
                                    .getInt(start_date_hour_index), c
                                    .getInt(start_date_minute_index), year,
                                    mounth, day, 23, 59, c
                                    .getString(category_index), c
                                    .getString(location_index), c
                                    .getString(info_index), c
                                    .getInt(status_index),

                                    c.getString(file_path_index), c
                                    .getString(time_push_index), c
                                    .getString(sound_index), c
                                    .getInt(isDone_index)));

                        } else if (end_milisec == date_milisec) {

                            result.add(new OneEvent(c.getInt(id_index), c
                                    .getString(title_index), c
                                    .getInt(color_index), year, mounth, day, 7,
                                    0, year, mounth, day, c
                                    .getInt(end_date_hour_index), c
                                    .getInt(end_date_minute_index), c
                                    .getString(category_index), c
                                    .getString(location_index), c
                                    .getString(info_index), c
                                    .getInt(status_index),

                                    c.getString(file_path_index), c
                                    .getString(time_push_index), c
                                    .getString(sound_index), c
                                    .getInt(isDone_index)));
                        } else {

                            result.add(new OneEvent(c.getInt(id_index), c
                                    .getString(title_index), c
                                    .getInt(color_index), year, mounth, day, 0,
                                    0, year, mounth, day, 23, 59, c
                                    .getString(category_index), c
                                    .getString(location_index), c
                                    .getString(info_index), c
                                    .getInt(status_index),

                                    c.getString(file_path_index), c
                                    .getString(time_push_index), c
                                    .getString(sound_index), c
                                    .getInt(isDone_index)));
                        }
                    } else {

                        result.add(new OneEvent(c.getInt(id_index), c
                                .getString(title_index), c.getInt(color_index),
                                year, mounth, day, c
                                .getInt(start_date_hour_index), c
                                .getInt(start_date_minute_index), year,
                                mounth, day, c.getInt(end_date_hour_index), c
                                .getInt(end_date_minute_index), c
                                .getString(category_index), c
                                .getString(location_index), c
                                .getString(info_index), c
                                .getInt(status_index),

                                c.getString(file_path_index), c
                                .getString(time_push_index), c
                                .getString(sound_index), c
                                .getInt(isDone_index)));
                    }

                } while (c.moveToNext());
            }

            c.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
        return result;
    }

    public OneEvent getEventById(int id) {
        OneEvent result = null;

        Cursor c = DataBase.rawQuery("select * from " + dbStorage.EVENT_TABLE
                + " where  id =" + id + " ;", null);

        if (c.moveToFirst()) {

            result = new OneEvent(c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("title")), c.getInt(c
                    .getColumnIndex("color")), c.getInt(c
                    .getColumnIndex("start_date_year")), c.getInt(c
                    .getColumnIndex("start_date_mounth")), c.getInt(c
                    .getColumnIndex("start_date_day")), c.getInt(c
                    .getColumnIndex("start_date_hour")), c.getInt(c
                    .getColumnIndex("start_date_minute")), c.getInt(c
                    .getColumnIndex("end_date_year")), c.getInt(c
                    .getColumnIndex("end_date_mounth")), c.getInt(c
                    .getColumnIndex("end_date_day")), c.getInt(c
                    .getColumnIndex("end_date_hour")), c.getInt(c
                    .getColumnIndex("end_date_minute")), c.getString(c
                    .getColumnIndex("category")), c.getString(c
                    .getColumnIndex("location")), c.getString(c
                    .getColumnIndex("info")), c.getInt(c
                    .getColumnIndex("status")), c.getString(c
                    .getColumnIndex("file_path")), c.getString(c
                    .getColumnIndex("time_push")), c.getString(c
                    .getColumnIndex("sound")), c.getInt(c
                    .getColumnIndex("isDone")));

            c.close();

            return result;
        } else {
            c.close();

            return result;
        }

    }

    public ArrayList<OneEvent> getEventsIcludingDate(int year, int mounth,
                                                     int day, int hour, int minute) {
        ArrayList<OneEvent> result = new ArrayList<OneEvent>();

        Cursor c;

        if (hour != -1 && minute != -1) {

            c = DataBase.rawQuery("select * from " + dbStorage.EVENT_TABLE
                    + " " + "where start_date_year<=" + year
                    + " AND start_date_mounth<=" + mounth
                    + " AND start_date_day<=" + day + " AND start_date_hour<="
                    + hour + " AND start_date_minute<=" + minute
                    + " AND end_date_year>=" + year + " AND end_date_mounth>="
                    + mounth + " AND end_date_day>=" + day
                    + " AND end_date_hour>=" + hour + " AND end_date_minute>="
                    + minute, null);
        } else {

            c = DataBase.rawQuery("select * from " + dbStorage.EVENT_TABLE
                    + " " + "where start_date_year<=" + year
                    + " AND start_date_mounth<=" + mounth
                    + " AND start_date_day<=" + day + " AND end_date_year>="
                    + year + " AND end_date_mounth>=" + mounth
                    + " AND end_date_day>=" + day, null);
        }

        if (c.moveToFirst()) {
            do {

                result.add(new OneEvent(c.getInt(c.getColumnIndex("id")), c
                        .getString(c.getColumnIndex("title")), c.getInt(c
                        .getColumnIndex("color")), c.getInt(c
                        .getColumnIndex("start_date_year")), c.getInt(c
                        .getColumnIndex("start_date_mounth")), c.getInt(c
                        .getColumnIndex("start_date_day")), c.getInt(c
                        .getColumnIndex("start_date_hour")), c.getInt(c
                        .getColumnIndex("start_date_minute")), c.getInt(c
                        .getColumnIndex("end_date_year")), c.getInt(c
                        .getColumnIndex("end_date_mounth")), c.getInt(c
                        .getColumnIndex("end_date_day")), c.getInt(c
                        .getColumnIndex("end_date_hour")), c.getInt(c
                        .getColumnIndex("end_date_minute")), c.getString(c
                        .getColumnIndex("category")), c.getString(c
                        .getColumnIndex("location")), c.getString(c
                        .getColumnIndex("info")), c.getInt(c
                        .getColumnIndex("status")), c.getString(c
                        .getColumnIndex("file_path")), c.getString(c
                        .getColumnIndex("time_push")), c.getString(c
                        .getColumnIndex("sound")), c.getInt(c
                        .getColumnIndex("isDone"))));
            } while (c.moveToNext());
            c.close();

            return result;
        } else {
            c.close();

            return result;
        }

    }

    public void AddButton(final ViewGroup root, final Button button_root) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ImageView image = new ImageView(MainActivity.getInstance());

                LayoutParams params = new LayoutParams(button_root.getWidth(),
                        button_root.getHeight());
                image.setLayoutParams(params);
                int[] l = new int[2];
                button_root.getLocationOnScreen(l);
                image.setX(l[1]);
                image.setY(l[0]);

                image.setImageResource(R.drawable.b);
                image.setScaleType(ScaleType.FIT_XY);
                root.addView(image);
            }
        }, 50);

    }

    public int getCountInTable(String name) {

        Cursor c = DataBase.rawQuery("select * from " + name, null);

        return c.getCount();
    }

    public ArrayList<OneCity> getCitiesByCountry(
            ArrayList<OneCity> all_one_city, String country) {
        ArrayList<OneCity> result = new ArrayList<OneCity>();

        for (int i = 0; i < all_one_city.size(); i++) {
            if (all_one_city.get(i).getCountry().equalsIgnoreCase(country)) {
                result.add(all_one_city.get(i));
            }
        }

        return result;

    }

    public ArrayList<OneCity> getTheCountry(ArrayList<OneCity> all) {

        if (all.size() == 0)
            all = new ParseCity().Parsing_data();

        ArrayList<OneCity> result = new ArrayList<OneCity>();

        result.add(new OneCity(all.get(0).getId(), all.get(0).getRegion(), all
                .get(0).getCountry(), all.get(0).getName()));

        for (int i = 1; i < all.size(); i++) {
            if (all.get(i).getCountry().equals(all.get(i - 1).getCountry()))
                i++;
            else {

                result.add(all.get(i));
            }
        }

        return result;

    }

    public void getCities() {

        ParseCity parser = new ParseCity();
        CITIES = parser.Parsing_data();

    }

    public int getIdCityByName(ArrayList<OneCity> all, String name) {

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().equals(name))
                return all.get(i).getId();
        }

        return 0;
    }

    public String getCityById(ArrayList<OneCity> all, int id) {

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == id)
                return all.get(i).getName();
        }

        return "";
    }

    public String getCountryById(ArrayList<OneCity> all, int id) {

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == id)
                return all.get(i).getCountry();
        }

        return "";
    }

    public boolean InternetConnection() {
        Context context = MainActivity.getInstance();
        ConnectivityManager connectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = connectionManager.getActiveNetworkInfo();
        if (connection == null) {

            return false;
        } else {
            return true;
        }
    }

    public String getLocationById(ArrayList<OneCity> all, int id) {
        try {

            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == id)
                    return all.get(i).getCountry() + ", "
                            + all.get(i).getName();
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }
        return "";
    }

    public void AddFriendToDb(OneFriend friend) {

        ContentValues cv = new ContentValues();
        cv.put("first_name", friend.getFirst_name());
        cv.put("last_name", friend.getLast_name());
        cv.put("date_birthday", friend.getDate_birthday());
        cv.put("type", friend.getType());
        cv.put("contact", friend.getContact());
        cv.put("photo", friend.getPhoto());
        cv.put("location", friend.getLocation());
        DataBase.insert(dbStorage.FRIENDS_TABLE, null, cv);
        cv.clear();

    }

    public boolean isFriendSaved(OneFriend friend) {

        Cursor c = DataBase.rawQuery("select * from " + DBStorage.FRIENDS_TABLE
                + " where " + " first_name ='" + friend.getFirst_name()
                + "' AND  last_name ='" + friend.getLast_name()
                + "' AND date_birthday ='" + friend.getDate_birthday()
                + "'  and type =" + friend.getType() + " and contact = '"
                + friend.getContact() + "';", null);

        if (c.getCount() > 0) {
            c.close();

            return true;
        } else {
            c.close();

            return false;
        }
    }

    public ArrayList<OneFriend> getFriendByType(int type) {
        ArrayList<OneFriend> res = new ArrayList<OneFriend>();

        Cursor c = DataBase.rawQuery("select * from " + dbStorage.FRIENDS_TABLE
                + " where type = " + type, null);

        if (c.moveToFirst()) {
            do {

                res.add(new OneFriend(c.getInt(c.getColumnIndex("id")), c
                        .getString(c.getColumnIndex("first_name")), c
                        .getString(c.getColumnIndex("last_name")), c
                        .getString(c.getColumnIndex("date_birthday")), c
                        .getString(c.getColumnIndex("contact")), c.getInt(c
                        .getColumnIndex("type")), c.getString(c
                        .getColumnIndex("location")), c.getString(c
                        .getColumnIndex("photo"))));

            } while (c.moveToNext());
            c.close();
        }

        ArrayList<String> arrayFriend = new ArrayList<String>();
        for (int i = 0; i < res.size(); i++) {
            arrayFriend.add(res.get(i).getFirst_name() + " "
                    + res.get(i).getLast_name());

        }

        Collections.sort(res, new FriendsSortComparator());

        return res;

    }

    public class FriendsSortComparator implements Comparator<OneFriend> {
        public int compare(OneFriend o1, OneFriend o2) {
            return o1.getFirst_name().compareTo(o2.getFirst_name());
        }
    }

    public ArrayList<OneEvent> getCountEventDayHour(int year, int mounth,
                                                    int day, int hour) {

        long date_min = StringToMilisec(FormatDate(year) + "-"
                + FormatDate(mounth) + "-" + FormatDate(day) + " "
                + FormatDate(hour) + ":00");
        long date_max = StringToMilisec(FormatDate(year) + "-"
                + FormatDate(mounth) + "-" + FormatDate(day) + " "
                + FormatDate(hour) + ":59");

        Log.e(getClass().toString(), "date_min = " + FormatDate(year) + "-"
                + FormatDate(mounth) + "-" + FormatDate(day) + " "
                + FormatDate(hour) + ":00");

        Log.e(getClass().toString(), "date_max = " + FormatDate(year) + "-"
                + FormatDate(mounth) + "-" + FormatDate(day) + " "
                + FormatDate(hour) + ":59");
        ArrayList<OneEvent> result = new ArrayList<OneEvent>();

        String select = "select * from " + dbStorage.EVENT_TABLE + "   where "
                + "start_milisec_long>=" + date_min + " and end_milisec_long>="
                + date_min;
        // Log.e(getClass().toString(), "select = " + select);

        Cursor c = DataBase.rawQuery(select, null);
        Log.e(getClass().toString(), "c.count = " + c.getCount());
        if (c.moveToFirst()) {
            do {

                result.add(new OneEvent(c.getInt(c.getColumnIndex("id")), c
                        .getString(c.getColumnIndex("title")), c.getInt(c
                        .getColumnIndex("color")), c.getInt(c
                        .getColumnIndex("start_date_year")), c.getInt(c
                        .getColumnIndex("start_date_mounth")), c.getInt(c
                        .getColumnIndex("start_date_day")), c.getInt(c
                        .getColumnIndex("start_date_hour")), c.getInt(c
                        .getColumnIndex("start_date_minute")), c.getInt(c
                        .getColumnIndex("end_date_year")), c.getInt(c
                        .getColumnIndex("end_date_mounth")), c.getInt(c
                        .getColumnIndex("end_date_day")), c.getInt(c
                        .getColumnIndex("end_date_hour")), c.getInt(c
                        .getColumnIndex("end_date_minute")), c.getString(c
                        .getColumnIndex("category")), c.getString(c
                        .getColumnIndex("location")), c.getString(c
                        .getColumnIndex("info")), c.getInt(c
                        .getColumnIndex("status")), c.getString(c
                        .getColumnIndex("file_path")), c.getString(c
                        .getColumnIndex("time_push")), c.getString(c
                        .getColumnIndex("sound")), c.getInt(c
                        .getColumnIndex("isDone"))));
            } while (c.moveToNext());
            c.close();

            return result;
        } else {
            c.close();

            return result;
        }
    }

    public String getFileName(String path) {
        int n = path.lastIndexOf("/");
        return path.substring(n + 1, path.length());
    }

    boolean onClick = false;
    boolean isTips = false;

    public void setOpenLeftMenu(ImageView image_back, final Spinner spinner) {
        final ArrayList<String> array = new ArrayList<String>();
        array.add(language.YEAR_PREVIEW);
        array.add(language.MOUNT_PREVIEW);
        array.add(language.MOUNT_PREVIEW2);
        array.add(language.WEEK_PREVIEW);
        array.add(language.WEEK_PREVIEW2);
        array.add(language.EVENT_NOW_DAY_LABEL);
        array.add("");

        if (THEME_APPLICATION == DrawMounth.BASE_THEME) {
            image_back.setImageResource(R.drawable.open_menu_base_them);
        }

        spinner.getSelectedItemPosition();
        final SpinnerAdadpter adapter = new SpinnerAdadpter(
                MainActivity.getInstance(), R.layout.menu_spiner_item, array);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                if (onClick) {
                    onClick = false;
                    switch (position) {
                        case 0:
                            startYearFragment(getNowYear(), language.NAMES_MOUNTH);
                            new PlaySoundButton();
                            break;

                        case 1:
                            startMounthFragment(getNowYear(), getNowMounth(),
                                    getNowDay());
                            new PlaySoundButton();
                            break;
                        case 2:
                            startMounthFragment2(getNowYear(), getNowMounth());
                            new PlaySoundButton();
                            break;
                        case 3:
                            startWeekFragment(7, 15, getNowDay(), getNowMounth(),
                                    getNowYear(), false, language.NAMES_DAYS_WEEK);
                            new PlaySoundButton();
                            break;
                        case 4:
                            startWeekFragment2(getNowYear(), getNowMounth(),
                                    getNowDay());
                            new PlaySoundButton();
                            break;
                        case 5:
                            startListEventOfDay(getNowYear(), getNowMounth(),
                                    getNowDay());
                            new PlaySoundButton();
                            break;
                        default:
                            break;
                    }

                    if (isTips) {
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                isTips = false;
                                startMounthFragment(getNowYear(),
                                        getNowMounth(), getNowDay());

                                if (TYPE_TIP == TYPE_TIP_CHANGE_VIEW)
                                    createEventTip();
                                Log.e(getClass().toString(), "create event tip");

                            }
                        }, 3000);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("", "onNothingSelected");
            }
        });

        spinner.setAdapter(adapter);

        image_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClick = true;

                if (TYPE_TIP == TYPE_TIP_CHANGE_VIEW) {
                    isTips = true;
                    Log.e(getClass().toString(), "remove changeCalendarView");
                    root.removeView(changeCalendarView);
                }

                new PlaySoundButton();
                spinner.setSelection(6);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        onClick = true;
                        spinner.performClick();
                    }
                }, 50);

            }
        });

    }

    public void setText() {

        ((TextView) findViewById(R.id.contacs_text))
                .setText(language.CONTACTI_LABEL);

        ((TextView) findViewById(R.id.add_event_text))
                .setText(language.ADD_EVENT);

        ((TextView) findViewById(R.id.settings_text))
                .setText(language.SETTINGS);
        // ((TextView) findViewById(R.id.add_event_group_text))
        // .setText(language.ADD_GROUP_EVENT);
        ((TextView) findViewById(R.id.help_text)).setText(language.HELP);
        ((TextView) findViewById(R.id.currens_text))
                .setText(language.CURRENS_KURS);

        ((TextView) findViewById(R.id.add_group_text))
                .setText(language.ADD_GROUP);

        ((TextView) findViewById(R.id.exit_text)).setText(language.EXIT);

        ((TextView) findViewById(R.id.stock_text)).setText(language.STOCKS);
        ((TextView) findViewById(R.id.about_text))
                .setText(language.ABOUT_TITLE);

    }

    public void AddFriendEvent(ArrayList<OneFriend> oneFriends) {

        // DataBase.beginTransaction();

        Log.e(getClass().toString(), "AddFriendEvent " + oneFriends.size()
                + " = " + oneFriends.size());

        Cursor c = DataBase.rawQuery("select MAX(id) from "
                + DBStorage.EVENT_TABLE, null);

        if (c.moveToFirst()) {

            int id = c.getInt(0);

            Log.e(getClass().toString(), "add " + oneFriends.size()
                    + " friend to event id = " + id);

            for (int i = 0; i < oneFriends.size(); i++) {
                ContentValues cv = new ContentValues();

                cv.put("first_name", oneFriends.get(i).getFirst_name());

                cv.put("last_name", oneFriends.get(i).getLast_name());
                cv.put("id_event", id);
                DataBase.insert(DBStorage.FRIENDS_IN_THE_EVENT, null, cv);
            }

            Log.e(getClass().toString(), "get friends by event id = " + id
                    + "  counth = " + getFriendsByIdEvent(id).size());

        }
        c.close();

        // DataBase.endTransaction();

    }

    public void ContacsToLog() {

        Cursor cursor = DataBase.rawQuery("select * from "
                + dbStorage.FRIENDS_TABLE, null);

        if (cursor.moveToFirst()) {
            do {

                Log.e(getClass().toString(),
                        "first="
                                + cursor.getString(cursor
                                .getColumnIndex("first_name"))
                                + "|last="
                                +

                                cursor.getString(cursor
                                        .getColumnIndex("last_name")) + ".");

            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public ArrayList<OneFriend> getFriendsByIdEvent(int id) {

        ContacsToLog();
        ArrayList<OneFriend> res = new ArrayList<OneFriend>();

        Cursor c = DataBase.rawQuery("select * from "
                        + DBStorage.FRIENDS_IN_THE_EVENT + " where id_event = " + id,
                null);
        Cursor cursor;
        if (c.moveToFirst()) {
            do {

                // int id_user = c.getInt(c.getColumnIndex("id_friend"));

                String first = c.getString(c.getColumnIndex("first_name"));

                String last = c.getString(c.getColumnIndex("last_name"));

                Log.e(getClass().toString(), "getFriendsByIdEvent first = "
                        + first);

                Log.e(getClass().toString(), "getFriendsByIdEvent last = "
                        + last);

                cursor = DataBase.rawQuery("select * from "
                        + DBStorage.FRIENDS_TABLE + " where first_name  = '"
                        + first + "' and last_name = '" + last + "'", null);
                if (cursor.moveToFirst()) {

                    res.add(new OneFriend(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor
                                    .getColumnIndex("first_name")),
                            cursor.getString(cursor.getColumnIndex("last_name")),
                            cursor.getString(cursor
                                    .getColumnIndex("date_birthday")),
                            cursor.getString(cursor.getColumnIndex("contact")),
                            cursor.getInt(cursor.getColumnIndex("type")),
                            cursor.getString(cursor.getColumnIndex("location")),
                            cursor.getString(cursor.getColumnIndex("photo"))));
                }

            } while (c.moveToNext());

            c.close();
        }

        return res;
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName()
                    .equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void Push(final int id, final String title, final String info) {

        Log.e(getClass().toString(), "push id event = " + id + "   tittle = "
                + title);

        AlertDialog.Builder ad;
        final ArrayList<OneFriend> arrayFriend = new ArrayList<OneFriend>();

        Log.e(getClass().toString(), "getFriendsByIdEvent(id) return "
                + getFriendsByIdEvent(id).size());

        arrayFriend.addAll(getFriendsByIdEvent(id));

        Log.e(getClass().toString(),
                "Push arrayFriend size = " + arrayFriend.size());

        if (arrayFriend.size() > 0) {
            ad = new AlertDialog.Builder(MainActivity.getInstance());
            ad.setTitle(language.NOTIFICATION_FRIENDS);

            ad.setPositiveButton(language.YES, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    try {

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                for (int i = 0; i < arrayFriend.size(); i++) {

                                    String contact = arrayFriend.get(i)
                                            .getContact();

                                    if (contact.indexOf("@") >= 0) {
                                        try {
                                            new GMailSender().sendMail(title,
                                                    info, contact, contact);
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    } else {
                                        if (contact.length() >= 10
                                                && contact.length() <= 13) {
                                            sendSMS(contact, title);

                                        }
                                    }
                                }
                                try {
                                    Thread.interrupted();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    Log.e(getClass().toString()
                                            + "line = "
                                            + Thread.currentThread()
                                            .getStackTrace()[2]
                                            .getLineNumber(), "catch:"
                                            + e);
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
            ad.setNegativeButton(language.NO, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });

            ad.show();
        }
    }

    public void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
            }

        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    public void getPhoneContacs() {
        deletePhoneContact();

        ArrayList<OneFriend> array = new PhoneContacs(this).getContacs();

        for (int i = 0; i < array.size(); i++) {
            AddFriendToDb(array.get(i));
        }

    }

    public void deletePhoneContact() {

        DataBase.delete(dbStorage.FRIENDS_TABLE, "type = "
                + OneFriend.PHONE_TYPE, null);

    }

    public void DeleteEvent(final View v, final OneEvent e, boolean dialog) {

        if (dialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(language.CHANGE_DELETE);

            builder.setPositiveButton(language.ALL_DELETE,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            DataBase.delete(
                                    dbStorage.EVENT_TABLE,
                                    "title = ? and location = ? and info = ? and color = ?",

                                    new String[]{e.getTitle(),
                                            e.getLocation(), e.getInfo(),
                                            String.valueOf(e.getColor())});

                            Toast.makeText(MainActivity.getInstance(),
                                    language.DELETE_COMPLETE,
                                    Toast.LENGTH_SHORT).show();
                            v.invalidate();

                        }
                    });
            builder.setNegativeButton(language.ONE_DELETE,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            DataBase.delete(dbStorage.EVENT_TABLE, "id = ?",

                                    new String[]{String.valueOf(e.getId())});

                            Toast.makeText(MainActivity.getInstance(),
                                    language.DELETE_COMPLETE,
                                    Toast.LENGTH_SHORT).show();
                            v.invalidate();
                        }
                    });
            builder.show();
        } else

            DataBase.delete(dbStorage.EVENT_TABLE, "id = ?",

                    new String[]{String.valueOf(e.getId())});

        v.invalidate();

    }

    public void CopyEvent(View v, OneEvent e, boolean show) {

        isBuffered = true;

        event_buffer = e;

        if (show)
            Toast.makeText(MainActivity.getInstance(), language.COPY_COMPLETE,
                    Toast.LENGTH_SHORT).show();

    }

    public void CutEvent(View v, OneEvent e, boolean show) {

        isBuffered = true;

        event_buffer = e;

        DeleteEvent(v, e, false);

        if (show)
            Toast.makeText(MainActivity.getInstance(), language.CUT_COMPLETE,
                    Toast.LENGTH_SHORT).show();

    }

    public void PasteEvent(final View v, int year, int mount, int day,
                           int hour, int minute) {
        isBuffered = false;

        String old_start_date = getStringDate(
                event_buffer.getStart_date_year(),
                event_buffer.getStart_date_mounth(),
                event_buffer.getStart_date_day(),
                event_buffer.getStart_date_hour(),
                event_buffer.getStart_date_minute());

        String old_end_date = getStringDate(event_buffer.getEnd_date_year(),
                event_buffer.getEnd_date_mounth(),
                event_buffer.getEnd_date_day(),
                event_buffer.getEnd_date_hour(),
                event_buffer.getEnd_date_minute());
        String old_push_date = event_buffer.getPush_time();
        String new_start_date = getStringDate(year, mount, day, hour, minute);

        long raz = StringToMilisec(new_start_date)
                - StringToMilisec(old_start_date);

        String new_end_date = MilisecToDate(StringToMilisec(old_end_date) + raz);

        String new_push_date = MilisecToDate(StringToMilisec(old_push_date)
                + raz);

        AddEventToDB(-1, event_buffer.getTitle(), event_buffer.getColor(),
                new_start_date, new_end_date, event_buffer.getCategory(),
                event_buffer.getLocation(), event_buffer.getInfo(),
                event_buffer.getFile_path(), new_push_date,
                event_buffer.getSound(), 0);

        v.invalidate();

    }

    public void ChangeIsDone(OneEvent oneEvent) {

        Cursor c = DataBase.rawQuery("select * from " + dbStorage.EVENT_TABLE
                + " where id=" + oneEvent.getId(), null);
        c.moveToFirst();
        int isDone;
        if (c.getInt(c.getColumnIndex("isDone")) == 0)
            isDone = 1;
        else
            isDone = 0;

        ContentValues cv = new ContentValues();
        cv.put("isDone", isDone);
        DataBase.update(dbStorage.EVENT_TABLE, cv, "id  = ?",
                new String[]{String.valueOf(oneEvent.getId())});

        c = DataBase.rawQuery("select * from " + dbStorage.EVENT_TABLE
                + " where id=" + oneEvent.getId(), null);
        c.moveToFirst();

    }

    // public int getFirstDayNowWeek() {
    // int res = 0;
    // Calendar c = Calendar.getInstance();
    // c.set(getNowYear(), getNowMounth() - 1, getNowDay() - 1);
    // int now_day = c.get(Calendar.DAY_OF_WEEK);
    //
    // if (now_day == 1)
    // return getNowDay();
    // else {
    // if (getNowDay() >= now_day)
    // return getNowDay() - now_day + 1;
    // else
    // {
    //
    // }
    //
    // }
    //
    // return res;
    // }

    // public void getId() {
    // ArrayList<Integer> a = new ArrayList<Integer>();
    // Cursor c = DB.rawQuery("select * from " + dbStorage.EVENT_TABLE, null);
    // if (c.moveToFirst()) {
    //
    // do {
    //
    // Log.e("", "id = " + c.getInt(c.getColumnIndex("id")));
    // } while (c.moveToNext());
    // }
    // }

    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case 0:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(language.DIALOG_TITLE);

                dialog_actions = new String[]{language.DIALOG_ADD_EVENT};

                builder.setItems(dialog_actions,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                // TODO Auto-generated method stub

                                showDialog.ChoiseOfAction(dialog_actions, item);

                            }
                        });
                builder.setCancelable(true);
                return builder.create();

            case 1:

                builder = new AlertDialog.Builder(this);
                builder.setTitle(language.DIALOG_TITLE);

                dialog_actions = new String[]{language.DIALOG_ADD_EVENT,
                        language.DIALOG_CHANGE_EVENT, language.DIALOG_COPY_EVENT,
                        language.DIALOG_CUT_EVENT, language.DIALOG_DELETE_EVENT};

                builder.setCancelable(true);
                builder.setItems(dialog_actions,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                // TODO Auto-generated method stub

                                showDialog.ChoiseOfAction(dialog_actions, item);

                            }
                        });

                return builder.create();

            case 2:

                builder = new AlertDialog.Builder(this);
                builder.setTitle(language.DIALOG_TITLE);

                dialog_actions = new String[]{language.DIALOG_ADD_EVENT,
                        language.DIALOG_CHANGE_EVENT, language.DIALOG_PASTE_EVENT,

                        language.DIALOG_COPY_EVENT, language.DIALOG_CUT_EVENT,
                        language.DIALOG_DELETE_EVENT};

                builder.setCancelable(true);
                builder.setItems(dialog_actions,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                // TODO Auto-generated method stub

                                showDialog.ChoiseOfAction(dialog_actions, item);

                            }
                        });

                return builder.create();

            case 3:

                builder = new AlertDialog.Builder(this);
                builder.setTitle(language.DIALOG_TITLE);

                dialog_actions = new String[]{language.DIALOG_ADD_EVENT,
                        language.DIALOG_PASTE_EVENT

                };

                builder.setCancelable(true);
                builder.setItems(dialog_actions,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                // TODO Auto-generated method stub

                                showDialog.ChoiseOfAction(dialog_actions, item);

                            }
                        });

                return builder.create();

            case DIALOG_GET_GOOGLE_PLAY_SERVICES:

                // int available = GooglePlayServicesUtil
                // .isGooglePlayServicesAvailable(this);
                // if (available == ConnectionResult.SUCCESS) {
                // return null;
                // }
                // if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
                // return GooglePlayServicesUtil.getErrorDialog(available, this,
                // REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
                // }
                return new AlertDialog.Builder(this).setMessage("dialog")
                        .setCancelable(true).create();

            // case 123:

            // builder = new AlertDialog.Builder(this);
            // builder.setTitle(language.SELECT_BANK);
            //
            // if (namesBanks.size() == 0) {
            // if (allCurrency.size() != 0)
            // namesBanks = getAllNameBanks(allCurrency);
            // }
            //
            // dialog_actions = new String[namesBanks.size()];
            // dialog_actions = namesBanks.toArray(dialog_actions);
            // builder.setCancelable(true);
            // builder.setItems(dialog_actions,
            // new DialogInterface.OnClickListener() {
            // @Override
            // public void onClick(DialogInterface dialog, int item) {
            // // TODO Auto-generated method stub
            //
            // shared.edit().putInt("bank", item).commit();
            // showCurrency();
            // }
            // });
            //
            // return builder.create();

            default:
                return null;
        }

    }

    public String getStringDate(int year, int mounth, int day, int hour,
                                int minute) {
        String res = "" + year;
        if (mounth < 10)
            res += "-0" + mounth;
        else
            res += "0" + mounth;

        if (day < 10)
            res += "-0" + day + " ";
        else
            res += "-" + day + " ";

        if (hour >= 0 && minute >= 0) {
            if (hour < 10)
                res += "0" + hour;
            else
                res += "" + hour;

            if (minute < 10)
                res += ":0" + minute;
            else
                res += ":" + minute;
        } else
            res += "12:00";

        return res;
    }

    static public long StringToMilisec(String DateInString) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long res = 0;
        String DateInMilisec = "";
        try {
            Date date;
            try {
                date = formatDate.parse(DateInString);
                DateInMilisec = "" + date.getTime();
                res = Long.parseLong(DateInMilisec);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String MilisecToDate(long DaateInMilisec) {
        long i = DaateInMilisec;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(i);
        String s = formatDate.format(date);
        return s;
    }

    // private void writeToFile(ArrayList<String> data) {
    //
    // BufferedWriter out;
    // try {
    //
    // FileWriter fileWriter = new FileWriter(Environment
    // .getExternalStorageDirectory().getPath() + "/f.txt");
    //
    // out = new BufferedWriter(fileWriter);
    //
    // for (int i = 0; i < data.size(); i++) {
    // out.write(data.get(i));
    // }
    //
    // out.close();
    // } catch (IOException e) {
    // Log.e("Exception", "File write failed: " + e.toString());
    // }
    //
    // }

    public boolean Registration(String login, String password,
                                String password2, String email, String name, String surname,
                                String phone, String bDate, String country, String city) {
        if (InternetConnection()) {
            if (password.equals(password2)) {

                // data={"action":"users.register","data":{"login":"loginn23","password":"fcbrfktdf","password2":"fcbrfktdf",
                // "email":"loginn333@gmail.com","name":"asdfsadf","surname":"dsafsadfa","phone":"54416498"}}

                String data = "{\"action\":\"users.register\",\"data\":{\"login\":\"";
                data += login;
                data += "\",\"password\":\"";
                data += password;
                data += "\",\"password2\":\"";
                data += password2;
                data += "\",\"email\":\"";
                data += email;
                data += "\",\"name\":\"";
                data += name;
                data += "\",\"surname\":\"";
                data += surname;
                data += "\",\"phone\":\"";
                data += phone;

                data += "\",\"country\":\"";
                data += country;

                data += "\",\"city\":\"";
                data += city;

                data += "\",\"date\":\"";
                data += bDate;

                data += "\"}}";

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://calendar.studiovision.com.ua/main.php");
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                            2);
                    nameValuePairs.add(new BasicNameValuePair("data", data));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                            "UTF-8"));
                    HttpResponse response = httpclient.execute(httppost);
                    String resp = convertStreamToString(response);

                    Log.e(getClass().toString(), "response registration:"
                            + resp);

                    int mes = 0;
                    try {
                        mes = new JSONObject(resp).getInt("message");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (mes == 204) {
                        return true;
                    } else {
                        showError(mes);
                        return false;
                    }

                } catch (ClientProtocolException e) {
                    return false;
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                    return false;
                }
            } else
                return false;
        } else
            return false;

    }

    public boolean SendToken(int id, String token) {
        if (InternetConnection()) {

            String data = "{\"action\":\"users.tokens\",\"data\":{\"id\":\"";
            data += id;
            data += "\",\"token\":\"";
            data += token + "\"}}";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://calendar.studiovision.com.ua/main.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("data", data));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String resp = convertStreamToString(response);

                int mes = 0;
                try {
                    mes = new JSONObject(resp).getInt("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e(getClass().toString(), "sendToken - id = " + id
                        + "   token = " + token);

                if (mes == 204) {
                    return true;
                } else {
                    showError(mes);
                    return false;
                }

            } catch (ClientProtocolException e) {
                return false;
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block

                Log.e("SendToken", "send request cath:" + e);
                return false;
            }

        } else
            return false;

    }

    public boolean Authorization(String login, String password,
                                 boolean isRememberPassword) {
        if (InternetConnection()) {
            String data = "{\"action\":\"users.auth\",\"data\":{\"";
            data += "login\":\"" + login + "\",\"password\":\"" + password
                    + "\"}}";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://calendar.studiovision.com.ua/main.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("data", data));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                String resp = convertStreamToString(response);

                Log.e(getClass().toString(), "authrization response: " + resp);
                int mes = 0;
                try {
                    mes = new JSONObject(resp).getJSONObject("user").getInt(
                            "message");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (mes == 204) {
                    try {
                        String user = new JSONObject(resp).getString("user");
                        int id = new JSONObject(user).getInt("id");
                        String email = new JSONObject(user).getString("email");
                        String src = new JSONObject(user).getString("file");
                        String name = new JSONObject(user).getString("name");
                        String surname = new JSONObject(user)
                                .getString("surname");
                        String phone = new JSONObject(user).getString("phone");

                        USER = new User(login, password, id, src, email, name,
                                surname, phone);

                        new sendRegistrationID(USER.getId());

                        shared.edit()
                                .putBoolean(SHARED_SAVED_LOGIN_PASSWORD,
                                        isRememberPassword).commit();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    return true;
                } else {
                    showError(mes);
                    return false;
                }

            } catch (ClientProtocolException e) {
                Log.e("Authorization", "send request cath:" + e);
                return false;
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("Authorization", "send request cath:" + e);
                return false;
            }
        } else
            return false;
    }

    public boolean RemindPassword(String email) {
        if (InternetConnection()) {
            // data={"action":"users.lost-password","data":{"email":"loginn333@gmail.com"}}
            String data = "{\"action\":\"users.lost-password\",\"data\":{\"email\":\"";
            data += email + "\"}}";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://calendar.studiovision.com.ua/main.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("data", data));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                String resp = convertStreamToString(response);

                int mes = 0;
                try {
                    mes = new JSONObject(resp).getInt("message");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (mes == 204) {

                    return true;
                } else {
                    showError(mes);
                    return false;
                }

            } catch (ClientProtocolException e) {
                Log.e("RemindPassword", "send request cath:" + e);

                // TODO Auto-generated catch block
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("RemindPassword", "send request cath:" + e);
                return false;
            }
        } else
            return false;

    }

    static String convertStreamToString(HttpResponse response) {

        String s = "";
        try {
            s = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public void exportDatabse(String databaseName) {
        try {

            String path_file = Environment.getExternalStorageDirectory()
                    + "/bCalendar/" + USER.getLogin() + "DB.db";

            File f = new File(path_file);
            if (f.delete())
                Log.e(getClass().toString(), "fileDeleted");

            File sd = new File(Environment.getExternalStorageDirectory()
                    + "/bCalendar/");
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {

                String currentDBPath = "//data//" + getPackageName()
                        + "//databases//" + databaseName + "";

                String backupDBPath = databaseName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {

                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

            }
        } catch (Exception e) {

        }
    }

    public void SendIdByFile(String user_id, String src) {

        if (InternetConnection()) {
            String data = "{\"action\":\"users.uploadbase\",\"data\":{\"id\":\""
                    + user_id + "\",\"baseurl\":\"" + src + "\"}}";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://calendar.studiovision.com.ua/main.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("data", data));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                String resp = convertStreamToString(response);

            } catch (ClientProtocolException e) {
                Log.e("Authorization", "send request cath:" + e);
                // return false;
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("Authorization", "send request cath:" + e);
                // return false;
            }
        }
    }

    public void showError(int mes) {
        Message m = showError.obtainMessage(1, mes);
        showError.sendMessage(m);
    }

    private void copyDataBase(String name) throws Exception {
        // Open your local db as the input stream
        File f = new File(Environment.getExternalStorageDirectory()
                + "/bCalendar/" + name);

        if (!f.exists()) {
            throw new RuntimeException("Source database file not exists");
        }

        FileInputStream myInput = new FileInputStream(f);

        // Path to the just created empty db
        String outFileName = "/data/data/" + getPackageName() + "/databases/"
                + name;
        File outDatabase = new File(outFileName);
        if (!outDatabase.exists()) {
            outDatabase.createNewFile();
        }

        if (!outDatabase.exists()) {
            throw new RuntimeException("Destination database file not created");
        }

        // Open the empty db as the output stream

        OutputStream myOutput = null;
        try {
            myOutput = new FileOutputStream(outDatabase);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Unable open file -> "
                    + outDatabase.getPath());
        }

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private void CreateDir() {

        File file = new File(Environment.getExternalStorageDirectory()
                + "/bCalendar");
        if (!file.exists()) {
            final boolean result = file.mkdir();
            if (result) {
            } else {
            }
        } else {
        }
    }

    public void Synchronize() {
        try {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    try {

                        exportDatabse(USER.getLogin() + "DB.db");
                        String path_file = Environment
                                .getExternalStorageDirectory()
                                + "/bCalendar/"
                                + USER.getLogin() + "DB.db";

                        new FileTransfer().uploadFile(
                                MainActivity.getInstance(),
                                String.valueOf(USER.getId()), path_file);

                        File file = new File(path_file);
                        file.delete();
                        try {
                            Thread.interrupted();
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(getClass().toString()
                                    + "line = "
                                    + Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber(), "catch:" + e);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e(getClass().toString()
                                + "line = "
                                + Thread.currentThread().getStackTrace()[2]
                                .getLineNumber(), "catch:" + e);
                    }
                }
            }).start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    Timer t = new Timer();

    public void StartSynchronize(Timer timer) {
        StopSynchronize(timer);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                googleCalendarSync();
                Synchronize();

            }
        }, 1, 30 * 1000 * 60);

    }

    public void StopSynchronize(Timer timer) {
        try {
            timer.cancel();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }

    }

    public ArrayList<User> getPeoplesCalendar(String search_text) {
        ArrayList<User> result = new ArrayList<User>();

        if (InternetConnection()) {

            // data={"action":"users.search","data":{"name":"q","surname":"q"}}
            String data = "{\"action\":\"users.search\",\"data\":{\"search\":\""
                    + search_text + "\"}}";

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://calendar.studiovision.com.ua/main.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("data", data));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                String resp = convertStreamToString(response);

                try {
                    JSONArray jArray = new JSONArray(resp);
                    for (int i = 0; i < jArray.length(); i++) {
                        int id = jArray.getJSONObject(i).getInt("id");
                        String user_name = jArray.getJSONObject(i).getString(
                                "name");
                        String user_surname = jArray.getJSONObject(i)
                                .getString("surname");
                        String user_email = jArray.getJSONObject(i).getString(
                                "email");
                        String user_phone = jArray.getJSONObject(i).getString(
                                "phone");
                        String user_login = jArray.getJSONObject(i).getString(
                                "login");
                        result.add(new User(user_login, "", id, "", user_email,
                                user_name, user_surname, user_phone));

                    }

                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                int mes = 0;
                try {
                    mes = new JSONObject(resp).getJSONObject("user").getInt(
                            "message");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                showError(mes);

            } catch (ClientProtocolException e) {
                Log.e("getPeoplesCalendar", "send request cath:" + e);
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("getPeoplesCalendar", "send request cath:" + e);
            }
        }

        return result;
    }

    // public ArrayList<OneCurrency> getCurrencyByBank(
    // ArrayList<OneCurrency> allCurrency, String name_bank) {
    // ArrayList<OneCurrency> res = new ArrayList<OneCurrency>();
    // for (int i = 0; i < allCurrency.size(); i++) {
    //
    // if (allCurrency.get(i).getBank().equalsIgnoreCase(name_bank))
    // res.add(allCurrency.get(i));
    //
    // }
    // return res;
    // }

    // public ArrayList<String> getAllNameBanks(ArrayList<OneCurrency>
    // allCurrency) {
    // ArrayList<String> res = new ArrayList<String>();
    //
    // for (int i = 0; i < allCurrency.size(); i++) {
    //
    // if (!res.contains(allCurrency.get(i).getBank())) {
    // res.add(allCurrency.get(i).getBank());
    // }
    // }
    //
    // return res;
    // }

    // public boolean sendEventToServer(int root, String title, int color,
    // int start_date_year, int start_date_mounth, int start_date_day,
    // int start_date_hour, int start_date_minute, int end_date_year,
    // int end_date_mounth, int end_date_day, int end_date_hour,
    // int end_date_minute, String time_push, String category,
    // String location, String info, int status, String file_path,
    // String sound, int isDone, String friends, String administrators) {
    // if (InternetConnection()) {
    //
    // String data = "{\"action\":\"users.insert-data\",\"data\":"
    // + "{\"title\":\"" + title + "\",";
    // data += "\"color\":\"" + color + "\",";
    // data += "\"start_date_year\":\"" + start_date_year + "\",";
    // data += "\"start_date_mounth\":\"" + start_date_mounth + "\",";
    // data += "\"start_date_day\":\"" + start_date_day + "\",";
    // data += "\"start_date_hour\":\"" + start_date_hour + "\",";
    // data += "\"start_date_minute\":\"" + start_date_minute + "\",";
    // data += "\"end_date_year\":\"" + end_date_year + "\",";
    // data += "\"end_date_mounth\":\"" + end_date_mounth + "\",";
    // data += "\"end_date_day\":\"" + end_date_day + "\",";
    // data += "\"end_date_hour\":\"" + end_date_hour + "\",";
    // data += "\"end_date_minute\":\"" + end_date_minute + "\",";
    // data += "\"category\":\"" + category + "\",";
    // data += "\"location\":\"" + location + "\",";
    // data += "\"info\":\"" + info + "\",";
    // data += "\"status\":\"" + status + "\",";
    // data += "\"file_path\":\"" + file_path + "\",";
    // data += "\"time_push\":\"" + time_push + "\",";
    // data += "\"sound\":\"" + sound + "\",";
    // data += "\"isDone\":\"" + isDone + "\",";
    // data += "\"friends\":\"" + friends + "\",";
    // data += "\"root\":\"" + root + "\",";
    // data += "\"administrators\":\"" + administrators + "\"}}";
    //
    // try {
    //
    // HttpClient httpclient = new DefaultHttpClient();
    // HttpPost httppost = new HttpPost(
    // "http://calendar.studiovision.com.ua/main.php");
    //
    // List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
    // 2);
    // nameValuePairs.add(new BasicNameValuePair("data", data));
    // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
    // "UTF-8"));
    //
    // HttpResponse response = httpclient.execute(httppost);
    // String resp = convertStreamToString(response);
    //
    // } catch (Exception e) {
    // // TODO: handle exception
    // Log.e("", "sendEventToServer cath: " + e);
    //
    // }
    //
    // }
    //
    // return false;
    // }

    // public void setTextCurrents(ArrayList<OneCurrency> a) {
    // if (a.size() == 3) {
    // for (int i = 0; i < a.size(); i++) {
    // if (a.get(i).getName().equalsIgnoreCase("USD")) {
    // ((TextView) findViewById(R.id.usd_buy)).setText(a.get(i)
    // .getBuy());
    // ((TextView) findViewById(R.id.usd_sale)).setText(a.get(i)
    // .getSale());
    // }
    //
    // if (a.get(i).getName().equalsIgnoreCase("EUR")) {
    // ((TextView) findViewById(R.id.eur_buy)).setText(a.get(i)
    // .getBuy());
    // ((TextView) findViewById(R.id.eur_sale)).setText(a.get(i)
    // .getSale());
    // }
    //
    // if (a.get(i).getName().equalsIgnoreCase("RUB")) {
    // ((TextView) findViewById(R.id.rub_buy)).setText(a.get(i)
    // .getBuy());
    // ((TextView) findViewById(R.id.rub_sale)).setText(a.get(i)
    // .getSale());
    // }
    // }
    // }
    // }

    // public void showCurrency() {
    // if (allCurrency.size() == 0) {
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // try {
    // allCurrency = new Currency().getCurrencies();
    // namesBanks = getAllNameBanks(allCurrency);
    // CurrencyByBank = getCurrencyByBank(allCurrency,
    // allCurrency.get(shared.getInt("bank", 0))
    // .getBank());
    // showError(123);
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // try {
    // Thread.interrupted();
    // } catch (Exception e) {
    // // TODO: handle exception
    // Log.e(getClass().toString()
    // + "line = "
    // + Thread.currentThread().getStackTrace()[2]
    // .getLineNumber(), "catch:" + e);
    // }
    // }
    // }).start();
    // } else {
    // // Log.e("", "allCurrency.size() != 0");
    // // Log.e("", "shared bank = " + shared.getInt("bank", 0));
    // CurrencyByBank = getCurrencyByBank(allCurrency,
    // namesBanks.get(shared.getInt("bank", 0)));
    // showError(123);
    // }
    // }

    public void getWeather() {

        try {
            WEATHERS.clear();
            int day = getNowDay();
            int mounth = getNowMounth();
            int year = getNowYear();

            for (int i = 0; i < 10; i++) {
                try {

                    final getWeatherByDay gwbd = new getWeatherByDay(
                            shared.getInt(SHARED_CITY, 33345), year, mounth,
                            day);

                    gwbd.setOnCompleteListener(new OnCompleteListener() {

                        @Override
                        public void Complete() {
                            // TODO Auto-generated method stub
                            WEATHERS.add(gwbd.GetOneWeatherDay());

                        }
                    });
                    gwbd.Parse();

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }

                String date = FormatDate(year) + "-" + FormatDate(mounth) + "-"
                        + FormatDate(day) + " 12:00";
                date = MilisecToDate(StringToMilisec(date) + 86400000);

                day = new MyDate(date).getDay();
                mounth = new MyDate(date).getMounth();
                year = new MyDate(date).getYear();

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + " line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch: " + e);
        }

    }

    public void GetListThread() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

                Thread[] threadArray = threadSet.toArray(new Thread[threadSet
                        .size()]);
                Log.e(getClass().toString(), "Thread count = "
                        + threadArray.length);
                for (int i = 0; i < threadArray.length; i++) {
                    Log.e(getClass().toString(),
                            "name = " + threadArray[i].getName()
                                    + "priority = "
                                    + threadArray[i].getPriority());
                }
                GetListThread();
            }
        }, 1000);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void showSoc() {

        try {

            String photo_vk = shared.getString(SHARED_VK_PHOTO, "");

            if (photo_vk.length() > 0)
                ((ImageView) findViewById(R.id.vk_image))
                        .setImageBitmap(getCroppedBitmap(BitmapFactory
                                .decodeFile(new FileLoader().path
                                        + getFileName(photo_vk))));

            String photo_fb = shared.getString(SHARED_FACEBOOK_PHOTO, "");

            if (photo_fb.length() > 0)
                ((ImageView) findViewById(R.id.fb_image))
                        .setImageBitmap(getCroppedBitmap(BitmapFactory
                                .decodeFile(new FileLoader().path
                                        + getFileName(shared.getString(
                                        SHARED_FACEBOOK_PHOTO, "")))));

            String photo_gp = shared.getString(SHARED_GOOGLEPLUS_PHOTO, "");

            if (photo_gp.length() > 0)
                ((ImageView) findViewById(R.id.gp_image))
                        .setImageBitmap(getCroppedBitmap(BitmapFactory
                                .decodeFile(new FileLoader().path
                                        + getFileName(shared.getString(
                                        SHARED_GOOGLEPLUS_PHOTO, "")))));

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + " line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);
        }
    }

    public void showListGrEvent() {
        for (int i = 0; i < 100; i++) {
            OneEventGroup oneEvent = new OneEventGroup(1, "�������� ������� "
                    + i, 1, 2014, 1, 1, 1, 1, 2014, 2, 1, 1, 1, "1", "33445",
                    "esuper event", 1, "Image)", "2014-01-01 20:48", "", 1,
                    " ", " ");
            arrayEventFromRightMenu.add(oneEvent);
        }
        adapterListGrEvent.notifyDataSetChanged();
    }

    public static void setButton(Button button) {
        RoundRectShape rect = new RoundRectShape(new float[]{50, 50, 50, 50,
                50, 50, 50, 50}, null, null);
        ShapeDrawable bg = new ShapeDrawable(rect);
        bg.getPaint().setColor(new MyColor().getColorComponents());
        button.setBackgroundDrawable(bg);
        button.setTextColor(new MyColor().getColorLabel());
    }

    public static void setButtonAvtoriz(Button button) {
        RoundRectShape rect = new RoundRectShape(new float[]{50, 50, 50, 50,
                50, 50, 50, 50}, null, null);
        ShapeDrawable bg = new ShapeDrawable(rect);
        bg.getPaint().setColor(Color.parseColor("#cecece"));
        button.setBackgroundDrawable(bg);
        button.setTextColor(Color.WHITE);
    }

    public static void setRelativeColor(RelativeLayout button, int color) {
        RoundRectShape rect = new RoundRectShape(new float[]{50, 50, 50, 50,
                50, 50, 50, 50}, null, null);
        ShapeDrawable bg = new ShapeDrawable(rect);
        bg.getPaint().setColor(color);
        button.setBackgroundDrawable(bg);
    }

    public void setBacground() {
        int res = shared.getInt(SHARED_BACGROUND_IMAGE, 0);

        if (THEME_APPLICATION == DrawMounth.BASE_THEME && res == 0)
            res = R.drawable.base_theme_background;

        findViewById(R.id.main_root).setBackgroundResource(res);

    }

    private void updateWidget(Context context) {
        Intent intent = new Intent(this, BusinessCalendarWidgetMonth.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

        int[] ids = {getWidgetId(context)};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    private int getWidgetId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                BusinessCalendarWidgetMonth.class.getName(),
                Context.MODE_PRIVATE);
        int widgetId = sharedPreferences.getInt(SharedPrefKeys.WIDGET_ID_KEY,
                -1);
        Log.i("", "getWidgetId -> " + widgetId);
        return widgetId;
    }

    static public long StringSmallToMilisec(String yyyy_mm_dd) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        long res = 0;
        String DateInMilisec = "";
        try {
            Date date;
            try {
                date = formatDate.parse(yyyy_mm_dd);
                DateInMilisec = "" + date.getTime();
                res = Long.parseLong(DateInMilisec);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    static public long StringVerySmallToMilisec(String yyyy_mm) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM");
        long res = 0;
        String DateInMilisec = "";
        try {
            Date date;
            try {
                date = formatDate.parse(yyyy_mm);
                DateInMilisec = "" + date.getTime();
                res = Long.parseLong(DateInMilisec);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void addTemplate(String template) {

        ContentValues cv = new ContentValues();
        cv.put("text", template);

        DataBase.insert(dbStorage.TEMPLATE_TABLE, null, cv);

    }

    public ArrayList<String> getTemplats() {
        ArrayList<String> res = new ArrayList<String>();

        res.add(language.HAPPY_BIRTHDAY_TEMPLEATE);
        res.add(language.NEW_YEAR_TEMPLEATE);
        res.add(language.MERY_CHRISTMAS_TEMPLEATE);
        res.add(language.MARCH_8_TEMPLEATE);
        res.add(language.DAY_WIN_TEMPLEATE);
        res.add(language.HAPPY_VALENTINE_TEMPLEATE);

        Cursor c = DataBase.rawQuery("select * from "
                + dbStorage.TEMPLATE_TABLE, null);

        if (c.moveToFirst()) {
            do {
                res.add(c.getString(c.getColumnIndex("text")));
            } while (c.moveToNext());
        }

        return res;
    }

    public void ShowTempleate(final EditText edit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ArrayList<String> a = getTemplats();
        dialog_actions = new String[a.size()];
        dialog_actions = a.toArray(dialog_actions);
        builder.setTitle(language.CHANGE_TEMPLATE);
        builder.setCancelable(true);
        builder.setItems(dialog_actions, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                try {
                    edit.setText(dialog_actions[which]);
                    new PlaySoundButton();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }
            }
        });

        builder.setNegativeButton(language.CREATE_TEMPLATE,
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ShowAddTemplate(edit);
                        new PlaySoundButton();
                    }
                });
        builder.show();
    }

    public void ShowAddTemplate(final EditText root_edit) {
        final EditText edit = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(edit);

        builder.setTitle(language.ENTER_TEMPLATE);
        builder.setPositiveButton(language.SAVE, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                if (edit.getText().toString().length() > 0)
                    addTemplate(edit.getText().toString());
                ShowTempleate(root_edit);

            }
        });

        builder.setNegativeButton(language.CANCEL, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ShowTempleate(root_edit);
                new PlaySoundButton();
            }
        });
        builder.show();
    }

    @SuppressWarnings("deprecation")
    public void showStock(final Context context) {
        if (getIndexStocksCompany().size() == 0 || STOCKS == null
                || STOCKS.size() == 0) {
            showStockSettings(context);
        } else {

            AlertDialog dialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = (View) inflater.inflate(R.layout.stocks_view, null);

            ((TextView) view.findViewById(R.id.name_title))
                    .setText(language.STOCK_NAME);
            ((TextView) view.findViewById(R.id.change_in_percent_title))
                    .setText(language.STOCK_PERCENT);
            ((TextView) view.findViewById(R.id.value_title))
                    .setText(language.STOCK_VALUE);
            ((TextView) view.findViewById(R.id.price_title))
                    .setText(language.STOCK_BOOK_BALUE);
            ((TextView) view.findViewById(R.id.ask_title))
                    .setText(language.STOCK_ASK);

            ListView list = (ListView) view.findViewById(R.id.stock_list);

            ArrayList<OneStock> stockUser = new Stock().getStocksByIndexes(
                    STOCKS, getIndexStocksCompany());
            StocksAdapter adapter = new StocksAdapter(context,
                    R.layout.one_stock, stockUser);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri
                            .parse("http://finance.yahoo.com/stock-center/"));
                    startActivity(i);

                }
            });

            dialog.setTitle(MainActivity.getInstance().language.STOCKS);

            dialog.setButton(language.ADD_COMPANY,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            showStockSettings(context);
                            dialog.dismiss();

                        }
                    });

            dialog.setView(view);
            dialog.show();

        }

    }

    public class StocksAdapter extends ArrayAdapter<OneStock> {

        Context context;

        public StocksAdapter(Context context, int resource,
                             List<OneStock> objects) {
            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        public class ViewHolder {

            TextView name;
            TextView ask;

            TextView changeInPercent;
            TextView bookValue;
            TextView value;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = new ViewHolder();
            OneStock stock = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.one_stock, null);

                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.ask = (TextView) convertView.findViewById(R.id.ask);
                holder.changeInPercent = (TextView) convertView
                        .findViewById(R.id.changePercent);

                holder.bookValue = (TextView) convertView
                        .findViewById(R.id.bookValue);

                holder.value = (TextView) convertView.findViewById(R.id.value);

            } else
                holder = (ViewHolder) convertView.getTag();

            try {
                holder.ask.setText(stock.getAsk());
            } catch (Exception e) {

            }
            try {
                holder.name.setText(stock.getName());
            } catch (Exception e) {

            }

            try {
                holder.changeInPercent.setText(stock.getChange_percent());
            } catch (Exception e) {

            }

            try {
                holder.bookValue.setText(stock.getBook_value() + " "
                        + stock.getCurrency());
            } catch (Exception e) {

            }
            try {// 1878044

                String val = stock.getVolume();
                String text = "";
                int k = 0;
                for (int i = val.length() - 1; i > -1; i--) {
                    text = val.charAt(i) + text;
                    k++;
                    if (k == 3) {
                        if (i != 0)
                            text = "," + text;
                        k = 0;
                    }

                }

                holder.value.setText(text);
            } catch (Exception e) {

            }

            return convertView;
        }
    }

    public void showStockSettings(final Context context) {

        final ProgressDialog progressDialog = ProgressDialog.show(context,
                language.LOADED, language.QUERY_MASTER_LOADED);
        progressDialog.show();

        final Stock stock = new Stock();

        final Handler handler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                AlertDialog dialog = new AlertDialog.Builder(context).create();

                final ListView list = new ListView(context);
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                ArrayList<String> names_company = stock.getNamesCompany(STOCKS);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_list_item_multiple_choice,
                        names_company);
                list.setAdapter(adapter);

                ArrayList<Integer> savedIndex = getIndexStocksCompany();
                for (int i = 0; i < savedIndex.size(); i++) {
                    list.setItemChecked(savedIndex.get(i), true);
                }

                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub

                        ArrayList<Integer> indexCompanies = new ArrayList<Integer>();
                        SparseBooleanArray sbArray = list
                                .getCheckedItemPositions();
                        for (int i = 0; i < sbArray.size(); i++) {
                            int key = sbArray.keyAt(i);
                            if (sbArray.get(key)) {

                                indexCompanies.add(key);

                            }
                        }

                        putIndexStocksCompany(indexCompanies);

                    }
                });
                progressDialog.dismiss();
                dialog.setTitle(language.SHOOSED_COMPANY);
                dialog.setView(list);
                dialog.setButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        showStock(context);
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        };

        if (STOCKS == null || STOCKS.size() == 0)
            stock.loadedStocks();
        else
            handler.sendMessage(handler.obtainMessage(1, 1));

        stock.setListener(new OnStocksLoadedListener() {

            @Override
            public void OnLoaded() {
                // TODO Auto-generated method stub

                STOCKS = new ArrayList<OneStock>();
                STOCKS = stock.getAllStocks();
                handler.sendMessage(handler.obtainMessage(1, 1));

            }
        });

    }

    public void putIndexStocksCompany(ArrayList<Integer> array) {
        try {

            DataBase.delete(dbStorage.STOCKS_TABLE, null, null);

            ContentValues cv = new ContentValues();

            for (int i = 0; i < array.size(); i++) {
                cv.put("id", array.get(i));
                DataBase.insert(dbStorage.STOCKS_TABLE, null, cv);
                cv.clear();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }

    public ArrayList<Integer> getIndexStocksCompany() {
        ArrayList<Integer> res = new ArrayList<Integer>();

        try {

            Cursor c = DataBase.rawQuery("select * from "
                    + dbStorage.STOCKS_TABLE, null);
            if (c.moveToFirst()) {
                do {

                    res.add(c.getInt(c.getColumnIndex("id")));

                } while (c.moveToNext());
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
        return res;

    }

    public void getStocks() {

        final Stock s = new Stock();
        s.setListener(new OnStocksLoadedListener() {

            @Override
            public void OnLoaded() {
                // TODO Auto-generated method stub
                STOCKS = s.getAllStocks();
            }
        });
        s.loadedStocks();

    }

    public void showHelp() {

        String title = "";
        String text = "";

        switch (status_id) {
            case STATUS_YEAR:

                title = language.YEAR_PREVIEW;
                text = language.YEAR_PREVIEW_HELP;

                break;

            case STATUS_MOUNTH:

                title = language.MOUNT_PREVIEW;
                text = language.MOUNTH_PREVIEW_HELP;

                break;

            case STATUS_MOUNTH2:

                title = language.MOUNT_PREVIEW2;
                text = language.MOUNTH2_PREVIEW_HELP;

                break;

            case STATUS_WEEK:

                title = language.WEEK_PREVIEW;
                text = language.WEEK_PREVIEW_HELP;

                break;

            case STATUS_WEEK2:

                title = language.WEEK_PREVIEW2;
                text = language.WEEK2_PREVIEW_HELP;

                break;

            case STATUS_LIST_EVENT:

                title = language.EVENT_NOW_DAY_LABEL;
                text = language.LIST_EVENT_HELP;

                break;

            case COLOR_PICKER:

                title = language.COLOR_COMPONENTS;
                text = language.COLOR_PICKER_HELP;

                break;

            case STATUS_ADD_EVENT:

                title = language.ADD_EVENT;
                text = language.ADD_EVENT_HELP;

                break;
            case STATUS_SETTINGS:

                title = language.SETTINGS;
                text = language.SETTINGS_HELP;

                break;

            case STATUS_ONE_EVENT:

                title = language.ONE_EVENT;
                text = language.ONE_EVENT_HELP;

                break;

            case STATUS_LIST_CONTACT:

                title = language.CONTACTI_LABEL;
                text = language.LIST_CONTACT_HELP;

                break;

            default:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.getInstance());
        LayoutInflater inflate = (LayoutInflater) MainActivity.getInstance()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout root = (LinearLayout) inflate.inflate(
                R.layout.help_window, null);

        ((TextView) root.findViewById(R.id.help_text)).setText(text);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setNegativeButton("Ok", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
            }
        });
        builder.setView(root);
        builder.show();

    }

    public void showAbout() {

        // AlertDialog builder = new AlertDialog.Builder(
        // MainActivity.getInstance()).create();
        final AlertDialog builder = new AlertDialog.Builder(mainActivity)
                .create();

        LayoutInflater inflate = (LayoutInflater) MainActivity.getInstance()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout root = (RelativeLayout) inflate.inflate(R.layout.about,
                null);

        root.findViewById(R.id.about_fb).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new PlaySoundButton();
                        new shareFaceBook(
                                language.ABOUT_TEXT,
                                "http://cs618721.vk.me/v618721217/107ee/oeo3PWGapqA.jpg",
                                "https://www.google.com.ua/?hl=ru&gws_rd=ssl");
                        builder.dismiss();
                    }
                });

        root.findViewById(R.id.about_vk).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new PlaySoundButton();
//                        new shareVK(
//                                MainActivity.getInstance(),
//                                drawerLayout,
//                                language.ABOUT_TEXT,
//                                "http://cs618721.vk.me/v618721217/107ee/oeo3PWGapqA.jpg",
//                                "https://www.google.com.ua/?hl=ru&gws_rd=ssl");

                        builder.dismiss();

                    }
                });

        ((TextView) root.findViewById(R.id.about_title))
                .setText(language.ABOUT_TEXT);

        ((TextView) root.findViewById(R.id.about_share))
                .setText("about_share");
        builder.setTitle(language.ABOUT_TITLE);
        builder.setCancelable(true);
        builder.setView(root);
        builder.show();

    }

    String res = "";
    ArrayList<String> array_spinner = new ArrayList<String>();
    ArrayList<String> array_src = new ArrayList<String>();

    public void AddRssAdress(OneRssAdress oneAdress) {

        try {

            ContentValues cv = new ContentValues();

            cv.put("name", oneAdress.getName());
            cv.put("src", oneAdress.getSrc());

            DataBase.insert(dbStorage.RSS_TABLE, null, cv);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }

    }

    public ArrayList<OneRssAdress> getRssAdress() {
        ArrayList<OneRssAdress> result = new ArrayList<OneRssAdress>();

        try {

            Cursor c = DataBase.rawQuery(
                    "select * from " + dbStorage.RSS_TABLE, null);
            if (c.moveToFirst()) {
                do {

                    result.add(new OneRssAdress(c.getString(c
                            .getColumnIndex("name")), c.getString(c
                            .getColumnIndex("src"))));
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
        return result;

    }

    public ArrayList<String> getNameRss(ArrayList<OneRssAdress> arrayAdresses) {
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < arrayAdresses.size(); i++) {
            res.add(arrayAdresses.get(i).getName());
        }
        return res;
    }

    public void showRssList(final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final ArrayList<OneRssAdress> rssAdresses = getRssAdress();
        ArrayList<String> names_rss = getNameRss(rssAdresses);
        ListView list = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, names_rss);
        list.setAdapter(adapter);

        dialog.setTitle("RSS");
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                new RSS(context, rssAdresses.get(position).getSrc());
            }
        });

        dialog.setCancelable(true);
        dialog.setPositiveButton(language.ADD_RSS, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                showAddRss(context);
            }
        });

        if (rssAdresses.size() == 0) {
            TextView text = new TextView(context);
            text.setText(language.NO_ADD_RSS);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            text.setLayoutParams(params);
            RelativeLayout rel = new RelativeLayout(this);
            rel.addView(text);

            dialog.setView(rel);
        } else
            dialog.setView(list);

        dialog.show();

    }

    @SuppressWarnings({"deprecation", "deprecation"})
    public void showAddRss(final Context context) {

        if (InternetConnection()) {
            AlertDialog builder = new AlertDialog.Builder(context).create();
            LayoutInflater inflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout root = (LinearLayout) inflate.inflate(
                    R.layout.set_rss, null);

            ((TextView) root.findViewById(R.id.set_rss_text))
                    .setText(language.SET_RSS_TEXT);
            final EditText edit_rss = (EditText) root
                    .findViewById(R.id.set_rss_edit);
            edit_rss.setHint(language.SET_RSS_HINT);

            ((TextView) root.findViewById(R.id.set_rss_hint))
                    .setText(language.SET_RSS_HINT);
            array_spinner.clear();
            array_src.clear();
            Spinner spiner_rss = (Spinner) root
                    .findViewById(R.id.set_rss_spinner);

            array_spinner.add("BUSINESS STANDART RSS: Top Stories");
            array_spinner.add("BUSINESS COMPANY: All Company Stories");
            array_spinner
                    .add("BUSINESS ECONOMY & POLICY: All Economy & Policy Stories");
            array_spinner.add("BUSINESS FINANCE: All Finance Stories");
            array_spinner.add("BEYOND BUSINESS: All Beyond Business Stories");
            array_spinner.add("BUSINESS OPINION: All Opinion Stories");
            array_spinner.add("BUSINESS INTERNATIONAL: All News-ANI Stories");
            array_spinner.add("BUSINESS POLITICS: All Politics Stories");
            array_spinner
                    .add("BUSINESS ELECTIONS 2014: All Elections 2014 Stories");
            array_spinner.add("���������� ������: ����� ������ �������");
            array_spinner.add("LIGA.NET: �������");
            array_spinner.add("LIGA.NET: ������");

            array_src
                    .add("http://www.business-standard.com/rss/home_page_top_stories.rss");
            array_src
                    .add("http://www.business-standard.com/rss/company-101.rss");
            array_src
                    .add("http://www.business-standard.com/rss/economy-policy-102.rss");
            array_src
                    .add("http://www.business-standard.com/rss/finance-103.rss ");
            array_src
                    .add("http://www.business-standard.com/rss/beyond-business-104.rss");
            array_src
                    .add("http://www.business-standard.com/rss/opinion-105.rss");
            array_src
                    .add("http://www.business-standard.com/rss/news-ani-152.rss");
            array_src
                    .add("http://www.business-standard.com/rss/politics-155.rss");
            array_src
                    .add("http://www.business-standard.com/rss/elections-2014-164.rss");
            array_src.add("http://www.pravda.com.ua/rus/rss/view_mainnews/");
            array_src.add("http://news.liga.net/top/rss.xml ");
            array_src.add("http://biz.liga.net/all/rss.xml ");

            builder.setTitle(language.RSS_SETTINGS);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1, array_spinner);

            spiner_rss.setAdapter(adapter);

            spiner_rss.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    try {
                        res = array_src.get(position);
                        new PlaySoundButton();
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

            edit_rss.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    // TODO Auto-generated method stub
                    if (edit_rss.getText().toString().length() == 0) {
                        edit_rss.setText("http://");
                    }
                }
            });
            builder.setButton("OK", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    new PlaySoundButton();
                    String name = "";

                    if (edit_rss.getText().toString().length() > 7) {

                        try {
                            res = edit_rss.getText().toString();
                            name = res.substring(7);
                            name = name.substring(0, name.indexOf("/"));

                        } catch (Exception e) {

                        }

                    } else {
                        for (int i = 0; i < array_src.size(); i++) {
                            if (array_src.get(i).equals(res)) {
                                name = array_spinner.get(i);
                            }
                        }
                    }

                    AddRssAdress(new OneRssAdress(name, res));

                    showRssList(context);

                }
            });

            builder.setView(root);
            builder.show();

        } else {
            Toast.makeText(MainActivity.getInstance(),
                    language.NO_INTERNET_CONNECTION, Toast.LENGTH_LONG).show();
        }

    }

    int TYPE_TIP = 0;
    final int TYPE_TIP_MENU = 1;
    final int TYPE_TIP_CHANGE_VIEW = 2;
    final int TYPE_TIP_ADD_EVENT = 3;

    private View tipMenuView;

    protected void openMenuTip() {
        OpenMenu openMenu = new OpenMenu(MainActivity.getInstance());
        tipMenuView = openMenu.getView();
        openMenu.setOpenMenuActions(new OpenMenuActions() {

            @Override
            public void skip() {
                // TODO Auto-generated method stub
                changeCalendarViewTip();
            }

            @Override
            public void next() {
                // TODO Auto-generated method stub

            }
        });

        root.addView(tipMenuView);
        TYPE_TIP = TYPE_TIP_MENU;
        openMenu.start();
    }

    private View changeCalendarView;

    protected void changeCalendarViewTip() {

        TYPE_TIP = TYPE_TIP_CHANGE_VIEW;
        Tips.ChangeCalendarView tip1 = new Tips.ChangeCalendarView(this);
        changeCalendarView = tip1.getView();
        tip1.setOpenMenuActions(new OpenMenuActions() {

            @Override
            public void skip() {
                // TODO Auto-generated method stub
                createEventTip();
            }

            @Override
            public void next() {
                // TODO Auto-generated method stub

            }
        });
        root.addView(changeCalendarView);
    }

    public View createEventView;

    protected void createEventTip() {
        Tips.CreateEvent tip = new Tips.CreateEvent(this);
        createEventView = tip.getView();
        tip.setOpenMenuActions(new OpenMenuActions() {

            @Override
            public void skip() {
                // TODO Auto-generated method stub
                root.removeView(createEventView);
            }

            @Override
            public void next() {
                // TODO Auto-generated method stub

            }
        });
        root.addView(createEventView);
        TYPE_TIP = TYPE_TIP_ADD_EVENT;

    }

    /**
     * FIRST START OF APPLICATION
     */
    private void checkFirstStartAndShowTips() {
        Log.e(getClass().toString(), "------------------isSettingsSaved = "
                + isSettingsSaved());
        if (!isSettingsSaved()) {
            startActivity(new Intent(this, InitialSettingsActivity.class));

        } else
            setSettings();
    }

    private boolean openMenuTipExpected = false;

    public class MyDrawerListener implements DrawerLayout.DrawerListener {

        public void setOpenMenuTipExpected() {
            openMenuTipExpected = true;
        }

        @Override
        public void onDrawerStateChanged(int arg0) {
            showDialog.StopTimer();

        }

        @Override
        public void onDrawerSlide(View arg0, float arg1) {
            showDialog.StopTimer();
            if (openMenuTipExpected) {
                root.removeView(tipMenuView);
            }
        }

        @Override
        public void onDrawerOpened(View arg0) {
            showDialog.StopTimer();
            // showCurrency();
            showSoc();

            if (openMenuTipExpected) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        drawerLayout.closeDrawers();
                    }
                }, 500);

            }
        }

        @Override
        public void onDrawerClosed(View arg0) {
            showDialog.StopTimer();
            if (openMenuTipExpected) {
                openMenuTipExpected = false;
                changeCalendarViewTip();
            }

        }
    }

    public static interface AppLifeCycle {
        public static final String FIRST_START = "fs";
    }

    public void UpdateEvent(OneEvent event) {
        bufferForUpdate = event;
        startAddEventFragment(false, true, getNowYear(), getNowMounth(),
                getNowDay(), getNowHour(), getNowMinute());
    }

    public void DeleteEventByID(int id) {

        DataBase.delete(dbStorage.EVENT_TABLE, "id = ?",
                new String[]{String.valueOf(id)});

    }

    public static ArrayList<Integer> getHolidays() {
        ArrayList<Integer> res = new ArrayList<Integer>();

        String holidays = shared.getString(SHARED_HOLIDAYS, "");

        if (holidays.length() > 0) {

            if (holidays.length() == 1) {
                res.add(Integer.parseInt(holidays));
                return res;
            } else {

                String[] a = holidays.split(",");

                for (int i = 0; i < a.length; i++) {
                    res.add(Integer.parseInt(a[i]));
                }

            }

        }

        return res;
    }

    static public OneFriend getOneFriendByNameSurnameType(String name_surname,
                                                          int type) {
        try {

            OneFriend res = null;
            String name = name_surname.substring(0, name_surname.indexOf(" "));
            String surname = name_surname.substring(
                    name_surname.indexOf(" ") + 1, name_surname.length());

            Cursor c = DataBase.rawQuery("select * from "
                    + dbStorage.FRIENDS_TABLE + " where first_name = '" + name
                    + "' and " + "last_name = '" + surname + "'   and  type = "
                    + type, null);

            if (c.moveToFirst()) {
                do {
                    res = new OneFriend(c.getInt(c.getColumnIndex("id")),
                            c.getString(c.getColumnIndex("first_name")),
                            c.getString(c.getColumnIndex("last_name")),
                            c.getString(c.getColumnIndex("date_birthday")),
                            c.getString(c.getColumnIndex("contact")),
                            c.getInt(c.getColumnIndex("type")), c.getString(c
                            .getColumnIndex("location")), c.getString(c
                            .getColumnIndex("photo")));
                } while (c.moveToNext());
            }

            return res;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("", "catch:" + e);
        }

        return null;
    }

    public boolean isSettingsSaved() {

        return DataBase.rawQuery("select * from " + dbStorage.SETTINGS_TABLE,
                null).moveToFirst();

    }

    public void startTips() {

        try {

            openMenuTip();
            myDrawerListener.setOpenMenuTipExpected();
            shared.edit().putString(AppLifeCycle.FIRST_START, "0").commit();

            saveSettings();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }

    public void saveSettings() {

        DataBase.delete(dbStorage.SETTINGS_TABLE, null, null);
        /*
         * db.execSQL("create table " + SETTINGS_TABLE + " (language integer,
		 * location integer, sound_key integer, is_am_pm integer, days_holiday
		 * integer);");
		 */
        ContentValues cv = new ContentValues();

        cv.put("location", shared.getInt(MainActivity.SHARED_CITY, 33345));
        cv.put("language", shared.getInt(MainActivity.SHARED_LANGUAGE, 1));
        cv.put("sound_key", shared.getInt(MainActivity.SHARED_SOUND_BUTTON, 0));

        if (shared.getBoolean(SHARED_IS_AMPM, false))
            cv.put("is_am_pm", 1);
        else
            cv.put("is_am_pm", 0);
        cv.put("days_holiday", shared.getString(SHARED_HOLIDAYS, ""));
        DataBase.insert(dbStorage.SETTINGS_TABLE, null, cv);
        Synchronize();

    }

    public void setSettings() {

        Cursor c = DataBase.rawQuery("select * from "
                + dbStorage.SETTINGS_TABLE, null);

        if (c.moveToFirst()) {
            shared.edit()
                    .putInt(SHARED_CITY, c.getInt(c.getColumnIndex("location")))
                    .commit();

            shared.edit()
                    .putInt(SHARED_LANGUAGE,
                            c.getInt(c.getColumnIndex("language"))).commit();

            shared.edit()
                    .putInt(SHARED_SOUND_BUTTON,
                            c.getInt(c.getColumnIndex("sound_key"))).commit();

            if (c.getInt(c.getColumnIndex("is_am_pm")) == 1)
                shared.edit().putBoolean(SHARED_IS_AMPM, true).commit();
            else
                shared.edit().putBoolean(SHARED_IS_AMPM, false).commit();

            shared.edit()
                    .putString(SHARED_HOLIDAYS,
                            c.getString(c.getColumnIndex("days_holiday")))
                    .commit();

        }

    }

    public void showCurrency(final Context context) {
        if (getInteresing() == null || CURRENS_MAP == null
                || CURRENS_MAP.size() == 0 || getInteresing().size() == 0) {
            showCurrensSettings(context);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(context).create();

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.currency_view, null);

            ListView list = (ListView) view.findViewById(R.id.cur_view_list);

            ArrayList<OneCurrency> arrayOneCurrency = new NewCurrency(context)
                    .getCurrencyByNameAndListNameCurrency(CURRENS_MAP,
                            getNameYourCurrency(), getInteresing());

            CurrencyAdapter adapter = new CurrencyAdapter(context,
                    R.layout.one_currency, arrayOneCurrency);

            list.setAdapter(adapter);

            dialog.setTitle(language.CURRENS_KURS);

            ((TextView) view.findViewById(R.id.view_currency_buy))
                    .setText(language.BUY);
            ((TextView) view.findViewById(R.id.view_currency_sale))
                    .setText(language.SALE);
            ((TextView) view.findViewById(R.id.view_currency_name))
                    .setText(language.CURRENS_NAME);

            dialog.setButton(language.CURRENS_SETTINGS,
                    new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            showCurrensSettings(context);

                        }
                    });

            dialog.setView(view);
            dialog.show();

        }

    }

    public void showCurrensSettings(final Context context) {

        if (InternetConnection()) {
            final ProgressDialog progressDialog = ProgressDialog.show(context,
                    language.LOADED, language.QUERY_MASTER_LOADED);
            progressDialog.show();

            final NewCurrency cur = new NewCurrency(context);

            final Handler handler = new Handler() {
                @SuppressWarnings("deprecation")
                @Override
                public void handleMessage(Message msg) {
                    // TODO Auto-generated method stub
                    super.handleMessage(msg);
                    progressDialog.dismiss();
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .create();

                    LayoutInflater mInflater = (LayoutInflater) context
                            .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    View view = mInflater.inflate(R.layout.currens_settings,
                            null);

                    ((TextView) view
                            .findViewById(R.id.currens_settings_choose_your_correns))
                            .setText(language.CHOOSE_YOUR_CURRENCY);
                    final Spinner spin = (Spinner) view
                            .findViewById(R.id.currens_settings_choose_your_correns_spinner);
                    ArrayList<String> arrayNamesCur = cur
                            .getNamesCurr(CURRENS_MAP);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            MainActivity.getInstance(),
                            android.R.layout.simple_list_item_1, arrayNamesCur);
                    spin.setAdapter(adapter);

                    final ListView list = (ListView) view
                            .findViewById(R.id.currens_settings_shoose_interesing);
                    list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    final ArrayList<String> names_cur = cur
                            .getNamesCurr(CURRENS_MAP);
                    ArrayAdapter<String> adapter_list = new ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_list_item_multiple_choice,
                            names_cur);
                    list.setAdapter(adapter_list);

                    spin.setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {
                            // TODO Auto-generated method stub

                            for (int i = 0; i < names_cur.size(); i++) {
                                list.setItemChecked(i, false);
                            }

                            list.setSelection(0);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.setView(view);
                    dialog.setTitle(language.SETTINGS_CURRENS);

                    ArrayList<String> array = getInteresing();

                    String nameCur = "";

                    try {

                        nameCur = getNameYourCurrency();

                        for (int i = 0; i < names_cur.size(); i++) {
                            if (names_cur.get(i).equalsIgnoreCase(nameCur))
                                spin.setSelection(i);
                        }

                    } catch (Exception e) {
                        // TODO: handle exception

                    }

                    if (array != null) {
                        for (int i = 0; i < array.size(); i++) {
                            for (int j = 0; j < names_cur.size(); j++) {
                                if (array.get(i).equals(names_cur.get(j))) {
                                    list.setItemChecked(j, true);
                                }
                            }
                        }
                    }

                    list.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            // TODO Auto-generated method stub

                            String interesing = "";
                            SparseBooleanArray sbArray = list
                                    .getCheckedItemPositions();
                            for (int i = 0; i < sbArray.size(); i++) {
                                int key = sbArray.keyAt(i);
                                if (sbArray.get(key)) {

                                    interesing += names_cur.get(key) + ",";

                                }
                            }

                            if (interesing.length() > 0)
                                interesing = interesing.substring(0,
                                        interesing.length() - 1);

                            putCurrencyToDB((String) spin.getSelectedItem(),
                                    interesing);

                        }
                    });

                    dialog.setButton("Ok",
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    showCurrency(context);
                                    dialog.dismiss();

                                }
                            });
                    dialog.show();
                }
            };

            if (CURRENS_MAP == null || CURRENS_MAP.size() == 0)
                cur.parse();
            else
                handler.sendMessage(handler.obtainMessage(1, 1));

            cur.setOnLoadCompleteListener(new OnLoadComplete() {

                @Override
                public void Complete() {
                    // TODO Auto-generated method stub
                    CURRENS_MAP = new HashMap<String, Float>();

                    CURRENS_MAP = cur.getMapCur();
                    handler.sendMessage(handler.obtainMessage(1, 1));
                }
            });

        } else
            Toast.makeText(this, language.NO_INTERNET_CONNECTION,
                    Toast.LENGTH_LONG).show();
    }

    public void putCurrencyToDB(String name_your_currency,
                                String interesing_currency) {

        try {

            ContentValues cv = new ContentValues();
            cv.put("your_currency", name_your_currency);
            cv.put("interesing_currency", interesing_currency);

            DataBase.delete(dbStorage.CURRENS_TABLE, null, null);

            DataBase.insert(dbStorage.CURRENS_TABLE, null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }

    public String getNameYourCurrency() {

        try {

            Cursor c = DataBase.rawQuery("select * from "
                    + dbStorage.CURRENS_TABLE, null);

            if (c.moveToFirst())
                return c.getString(c.getColumnIndex("your_currency"));
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
            return null;
        }
    }

    public ArrayList<String> getInteresing() {

        try {

            Cursor c = DataBase.rawQuery("select * from "
                    + dbStorage.CURRENS_TABLE, null);

            String interesing = "";

            if (c.moveToFirst())
                interesing = c.getString(c
                        .getColumnIndex("interesing_currency"));

            if (interesing != null && interesing.length() > 0) {

                return new ArrayList<String>(Arrays.asList(interesing
                        .split(",")));
            } else {

                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(getClass().toString() + "line = "
                            + Thread.currentThread().getStackTrace()[2].getLineNumber(),
                    "catch:" + e);

            return null;
        }
    }

    public void getCurrens() {
        try {

            final NewCurrency cur = new NewCurrency(this);
            cur.setOnLoadCompleteListener(new OnLoadComplete() {

                @Override
                public void Complete() {
                    // TODO Auto-generated method stub

                    CURRENS_MAP = cur.getMapCur();
                }
            });
            cur.parse();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }

    public class CurrencyAdapter extends ArrayAdapter<OneCurrency> {

        Context context;

        public CurrencyAdapter(Context context, int resource,
                               List<OneCurrency> objects) {
            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        public class ViewHolder {

            TextView name;
            TextView buy;

            TextView sale;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = new ViewHolder();
            OneCurrency currency = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.one_currency, null);

                holder.name = (TextView) convertView
                        .findViewById(R.id.one_currency_name);
                holder.buy = (TextView) convertView
                        .findViewById(R.id.one_currency_buy);
                holder.sale = (TextView) convertView
                        .findViewById(R.id.one_currency_sale);

            } else
                holder = (ViewHolder) convertView.getTag();

            try {

                holder.name.setText(currency.getName());
            } catch (Exception e) {

                // Log.e(getClass().toString(), "cath:" +e);

            }

            try {
                holder.buy.setText(currency.getBuy());
            } catch (Exception e) {
                // Log.e(getClass().toString(), "cath:" + e);
            }//

            try {
                holder.sale.setText(currency.getSale());
            } catch (Exception e) {
                // Log.e(getClass().toString(), "cath:" +e);
            }

            return convertView;
        }
    }

    public void addGoogleCalendarEvents(ArrayList<Integer> calendarsID) {

        try {

            final CalendarService cs = new CalendarService(this);

            cs.setCalendarEventLoadedListener(new CalendarEventLoadedListener() {

                @Override
                public void onCompleteLoaded() {
                    // TODO Auto-generated method stub
                    final ArrayList<OneEvent> events = cs.getEvents();

                    Log.e(getClass().toString(),
                            "events.size() = " + events.size());

                    if (MainActivity.DataBase != null) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                for (int i = 0; i < events.size(); i++) {

                                    OneEvent e = events.get(i);

                                    String start_date = FormatDate(e
                                            .getStart_date_year())
                                            + "-"
                                            + FormatDate(e
                                            .getStart_date_mounth())
                                            + "-"
                                            + FormatDate(e.getStart_date_day())
                                            + " "
                                            + FormatDate(e.getStart_date_hour())
                                            + ":"
                                            + FormatDate(e
                                            .getStart_date_minute());

                                    String end_date = FormatDate(e
                                            .getEnd_date_year())
                                            + "-"
                                            + FormatDate(e.getEnd_date_mounth())
                                            + "-"
                                            + FormatDate(e.getEnd_date_day())
                                            + " "
                                            + FormatDate(e.getEnd_date_hour())
                                            + ":"
                                            + FormatDate(e.getEnd_date_minute());

                                    if (!isEventSaved(e)) {

                                        ArrayList<Integer> arrayCalendarsId = MainActivity
                                                .getInstance()
                                                .getSavedCalendarsID();
                                        int calId = -1;
                                        if (arrayCalendarsId.size() > 0)
                                            calId = arrayCalendarsId.get(0);

                                        AddEventToDB(calId, e.getTitle(),
                                                e.getColor(), start_date,
                                                end_date, e.getCategory(),
                                                e.getLocation(), e.getInfo(),
                                                e.getFile_path(),
                                                e.getPush_time(), e.getSound(),
                                                e.getIsDone());
                                    }

                                }
                            }
                        }).start();
                    }

                }
            });

            cs.readCalendar(this, calendarsID);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }
    }

    public boolean putCalendarsId(ArrayList<Integer> IDs) {
        String str_id = "";
        try {

            try {

                for (int i = 0; i < IDs.size(); i++) {

                    if (!str_id.contains(String.valueOf(IDs.get(i))))
                        str_id += IDs.get(i) + ",";
                }
                if (str_id.length() > 1)
                    str_id = str_id.substring(0, str_id.length() - 1);

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("class:" + getClass().toString(), "catch:" + e);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return shared.edit().putString(SHARED_CALENDARS_ID, str_id).commit();

    }

    public ArrayList<Integer> getSavedCalendarsID() {
        ArrayList<Integer> res = new ArrayList<Integer>();

        try {

            String str = shared.getString(SHARED_CALENDARS_ID, "");

            if (str.length() > 0) {
                ArrayList<String> myList = new ArrayList<String>(
                        Arrays.asList(str.split(",")));

                for (int i = 0; i < myList.size(); i++) {

                    try {

                        res.add(Integer.parseInt(myList.get(i)));
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e("class:" + getClass().toString(), "catch:" + e);
                    }
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("class:" + getClass().toString(), "catch:" + e);
        }

        return res;
    }

    public void googleCalendarSync() {
        try {

            if (shared.getBoolean(SHARED_IS_SYNC_GOOGLE_CALENDAR, true)) {
                ArrayList<Integer> array = getSavedCalendarsID();
                if (array.size() > 0)
                    addGoogleCalendarEvents(array);

            } else {
                new SyncGoogleCalendar(MainActivity.getInstance())
                        .showSyncQuestion();
            }

        } catch (Exception e) {
        }
    }

    public GoogleCalendar getSelectedCalendar(String calendarName) {
        // if (calendarSpinner.getSelectedItem() != null && calendars != null) {

        for (GoogleCalendar cal : getCalendars()) {

            if (cal.getDisplayName().contains(calendarName))
                return cal;
        }
        return null;
    }

    public List<GoogleCalendar> getCalendars() {

        Cursor c = this.getContentResolver().query(
                GoogleCalendar.getContentURI(), null, null, null, null);
        List<GoogleCalendar> calendars = new ArrayList<GoogleCalendar>(
                c.getCount());

        while (c.moveToNext()) {
            GoogleCalendar cal = GoogleCalendar.retrieve(c);
            Cursor cursor = this.getContentResolver().query(
                    VEventWrapper.getContentURI(), new String[]{},
                    CalendarContract.Events.CALENDAR_ID + " = ?",
                    new String[]{Integer.toString(cal.getId())}, null);
            cal.setEntryCount(cursor.getCount());
            cursor.close();
            calendars.add(cal);
        }
        c.close();

        return calendars;
    }

    String nameCalendar = "";

    public void showExportIcal() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        Spinner spin = new Spinner(this);
        final ArrayList<String> namesCalendar = new CalendarService(this)
                .getNamesCalendar();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesCalendar);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                nameCalendar = namesCalendar.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        dialog.setView(spin);
        dialog.setTitle(language.CHOOSE_CALENDAR);
        dialog.setNegativeButton(language.CANCEL,
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        dialog.setPositiveButton("Ok", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                DialogTools.runWithProgress(
                        MainActivity.getInstance(),
                        new SaveCalendar(MainActivity.getInstance(),
                                MainActivity.getInstance().getSelectedCalendar(
                                        nameCalendar)), false,
                        ProgressDialog.STYLE_HORIZONTAL);
            }
        });

        dialog.show();

    }

    public void chooseFileImport() {

        startActivityForResult(new Intent(this, FileChooserActivity.class),
                1665);

    }

    public void importIcal(String path) {

        ImportIcal iCal = new ImportIcal();
        // OneEvent[] arrayEvent = iCal.readFile(
        // path.substring(path.lastIndexOf("/"), path.length()),
        // AddEventFragment.getFileName(path));

        // QueryMaster
        // .alert(this,
        // "path = "
        // + path.substring(path.lastIndexOf("/"),
        // path.length()) + "  name file = "
        // + AddEventFragment.getFileName(path));
    }

    public ArrayList<OneEvent> getAllEvent() {
        ArrayList<OneEvent> result = new ArrayList<OneEvent>();

        Cursor c = DataBase.rawQuery("select * from " + dbStorage.EVENT_TABLE
                + "  ORDER BY start_milisec", null);
        if (c.moveToFirst()) {

            do {

                result.add(new OneEvent(c.getInt(c.getColumnIndex("id")), c
                        .getString(c.getColumnIndex("title")), c.getInt(c
                        .getColumnIndex("color")), c.getInt(c
                        .getColumnIndex("start_date_year")), c.getInt(c
                        .getColumnIndex("start_date_mounth")), c.getInt(c
                        .getColumnIndex("start_date_day")), c.getInt(c
                        .getColumnIndex("start_date_hour")), c.getInt(c
                        .getColumnIndex("start_date_minute")), c.getInt(c
                        .getColumnIndex("end_date_year")), c.getInt(c
                        .getColumnIndex("end_date_mounth")), c.getInt(c
                        .getColumnIndex("end_date_day")), c.getInt(c
                        .getColumnIndex("end_date_hour")), c.getInt(c
                        .getColumnIndex("end_date_minute")), c.getString(c
                        .getColumnIndex("category")), c.getString(c
                        .getColumnIndex("location")), c.getString(c
                        .getColumnIndex("info")), c.getInt(c
                        .getColumnIndex("status")), c.getString(c
                        .getColumnIndex("file_path")), c.getString(c
                        .getColumnIndex("time_push")), c.getString(c
                        .getColumnIndex("sound")), c.getInt(c
                        .getColumnIndex("isDone"))));

            } while (c.moveToNext());
        }

        c.close();

        return result;
    }

    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    public int getCountEventToday(Context context, int year, int month, int day) {

        SQLiteDatabase DB;
        String name_db;
        Cursor c = null;
        shared = context.getSharedPreferences("shared", MODE_PRIVATE);

        if (MainActivity.DataBase == null || MainActivity.dbStorage == null) {
            shared = getSharedPreferences("shared", MODE_PRIVATE);
            name_db = shared.getString("db", "default.db");
            DB = new DBStorage(this, name_db).getWritableDatabase();
            c = DB.rawQuery("select * from "
                    + MainActivity.dbStorage.EVENT_TABLE
                    + " where start_date_year=" + year
                    + "  and start_date_mounth = " + month
                    + "   and  start_date_day = " + day, null);

        } else {
            c = MainActivity.DataBase.rawQuery("select * from "
                    + MainActivity.dbStorage.EVENT_TABLE
                    + " where start_date_year=" + year
                    + "  and start_date_mounth = " + month
                    + "   and  start_date_day = " + day, null);
        }

        return c.getCount();
    }


    public void startMicrosoft() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.outlook.Z7");
        if (intent != null) {
            /* we found the activity now start the activity */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
		    /* bring user to the market or let them choose an app? */
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.outlook.Z7"));
            startActivity(intent);
        }
    }

}
