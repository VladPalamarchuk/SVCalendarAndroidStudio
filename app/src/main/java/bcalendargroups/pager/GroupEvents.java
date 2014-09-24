package bcalendargroups.pager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import bcalendargroups.QueryMaster;
import bcalendargroups.activities.EditGroupActivity;
import bcalendargroups.activities.EditGroupActivity_;
import bcalendargroups.fragments.EventFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calendar.MainActivity;
import com.example.calendar.R;

import dialogs.CreateEvent;

import java.io.UnsupportedEncodingException;

public class GroupEvents extends FragmentActivity implements
        ActionBar.TabListener, QueryMaster.OnCompleteListener {

    public static final String GROUP_ID_BUNDLE = "GIB";

    public static final String IS_ADMIN = "IS_ADMIN";

    public static final String CALENDAR_USER_ID = "CALENDAR_USER_ID";

    public static final String GROUP_TITLE = "GROUP_TITLE";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this
     * becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter getSectionsPagerAdapter() {
        return mSectionsPagerAdapter;
    }

    protected SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section
     * contents.
     */
    protected ViewPager mViewPager;

    private String groupId = "";
    private ActionBar actionBar;

    private String calendarUserID;

    public String getCalendarUserID() {
        return calendarUserID;
    }

    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

//    public static GroupEvents instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_events_view_pager);

//        instance = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.containsKey(GROUP_ID_BUNDLE) ? bundle
                    .getString(GROUP_ID_BUNDLE) : null;

            isAdmin = bundle.getBoolean(IS_ADMIN);

            if (bundle.containsKey(CALENDAR_USER_ID)) {
                calendarUserID = bundle.getString(CALENDAR_USER_ID);
            } else {
                throw new RuntimeException(
                        "GroupEvents.class expected parameters in bundle by CALENDAR_USER_ID key");
            }
            ActionBar bar = getActionBar();
            if (bar != null) {
                bar.setTitle(bundle.getString(GROUP_TITLE));
                bar.setDisplayHomeAsUpEnabled(true);
            }
        }

        // Set up the action bar.
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager());

        try {
            mSectionsPagerAdapter.parse(groupId, this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                        actionBar.setSelectedNavigationItem(position);

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        instance = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void complete(String serverResponse) {
        // QueryMaster.alert(this, serverResponse);

        try {
            JSONObject jsonObject = new JSONObject(serverResponse);
            if (QueryMaster.isSuccess(jsonObject)) {
                JSONArray data = jsonObject.getJSONArray("data");

                /**
                 * set events displayed in ViewPager just title and id of every
                 * event
                 */
                mSectionsPagerAdapter.setEventsList(data);
                /**
                 * update and initialize fragments
                 */
                mSectionsPagerAdapter.notifyDataSetChanged();
                initTabs();
            } else {
                if (isAdmin) {
                    showQuestionCreateEvent();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            QueryMaster.alert(this, QueryMaster.SERVER_RETURN_INVALID_DATA);
        }
    }

    @Override
    public void error(int errorCode) {
        QueryMaster.alert(this, QueryMaster.ERROR_MESSAGE);
    }

    public void initTabs() {
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    public String getGroupId() {
        return groupId;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView()
                .getRootView().getWindowToken(), 0);
    }

    public void showQuestionCreateEvent() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(MainActivity.getInstance().language.EMTY_GROUP_EVENT_QUESTION);
        dialog.setPositiveButton(MainActivity.getInstance().language.YES,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new CreateEvent(GroupEvents.this, groupId).showDialog();
                    }
                });

        /**
         * GO {@link bcalendargroups.activities.EditGroupActivity}
         */
        dialog.setNegativeButton(MainActivity.getInstance().language.NO_GO_GROUP_SETTING,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GroupEvents.this,
                                EditGroupActivity_.class);
                        intent.putExtra(EditGroupActivity.GROUP_ID, groupId);

                        startActivity(intent);
                    }
                });

        dialog.show();
    }

    public void updateTabs() {
        actionBar.removeAllTabs();
        initTabs();
    }

    public void updateAdapter() {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
}
