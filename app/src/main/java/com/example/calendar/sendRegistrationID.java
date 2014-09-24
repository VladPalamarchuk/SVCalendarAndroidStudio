package com.example.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class sendRegistrationID {
    int id;

    public sendRegistrationID(int id) {
        this.id = id;

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(MainActivity.getInstance());
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "697133829173";
    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid;

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(MainActivity.getInstance());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,
                        MainActivity.getInstance(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {

        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);


                    msg = regid;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(final Object msg) {
                super.onPostExecute(msg);
                Log.e("msg", "id user = " + id + "msg = " + msg.toString());

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        MainActivity.getInstance()
                                .SendToken(id, msg.toString());
                    }
                }).start();

            }
        }.execute(null, null, null);

    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            // Log.i(TAG, "App version changed.");
            return "";
        }
        Log.e("registrationId", registrationId);
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {

        return MainActivity.getInstance().getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }
}
