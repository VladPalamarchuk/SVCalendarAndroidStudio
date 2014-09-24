package com.example.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

public class GooglePlusActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        PlusClient.OnPeopleLoadedListener {

    private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    Button exit_google_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_google_plus);
        exit_google_plus = (Button) findViewById(R.id.google_plus_exit);
        mPlusClient = new PlusClient.Builder(this, this, this).build();
        mConnectionProgressDialog = ProgressDialog.show(this, "Google plus",
                MainActivity.getInstance().language.QUERY_MASTER_LOADED);

        exit_google_plus.setText(MainActivity.getInstance().language.EXIT);

        exit_google_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlusClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG)
                .show();

        mConnectionProgressDialog.dismiss();

        mPlusClient.loadVisiblePeople(this, null);

        mPlusClient.loadPeople(new OnPeopleLoadedListener() {

            public void onPeopleLoaded(ConnectionResult status,
                                       PersonBuffer personBuffer, String nextPageToken) {
                // TODO Auto-generated method stub

                if (status.getErrorCode() == ConnectionResult.SUCCESS)
                    for (Person person : personBuffer) {
                        try {

                            String name_surname = person.getDisplayName();

                            String name = name_surname.substring(0,
                                    name_surname.indexOf(" "));
                            String surname = name_surname.substring(
                                    name_surname.indexOf(" ") + 1,
                                    name_surname.length());
                            String photo1 = person.getImage().getUrl();
                            final String photo = photo1.substring(0,
                                    photo1.indexOf("sz") - 1);
                            MainActivity.getInstance().shared
                                    .edit()
                                    .putString(
                                            MainActivity.SHARED_GOOGLEPLUS_FIRST_NAME,
                                            name).commit();
                            MainActivity.getInstance().shared
                                    .edit()
                                    .putString(
                                            MainActivity.SHARED_GOOGLEPLUS_LAST_NAME,
                                            surname).commit();
                            MainActivity.getInstance().shared
                                    .edit()
                                    .putString(
                                            MainActivity.SHARED_GOOGLEPLUS_PHOTO,
                                            photo).commit();

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub

                                    new FileLoader().loadFile(photo,
                                            MainActivity.getInstance()
                                                    .getFileName(photo));

                                }
                            }).start();
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(getClass().toString()
                                    + "line = "
                                    + Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber(), "catch:" + e);
                        }

                    }
            }
        }, "me");

        finish();
    }

    @Override
    public void onDisconnected() {
        finish();
        Log.i(TAG, "disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                mPlusClient.connect();
            }
        }
        mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR
                && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }

        if (requestCode == REQUEST_CODE_RESOLVE_ERR
                && responseCode == RESULT_CANCELED) {
            Log.e(getClass().toString(), "CANCEL");

            mConnectionProgressDialog.dismiss();
            finish();
        }

    }

    @Override
    public void onPeopleLoaded(ConnectionResult connectionResult,
                               PersonBuffer personBuffer, String s) {

        if (connectionResult.getErrorCode() == ConnectionResult.SUCCESS) {
            try {
                for (Person person : personBuffer) {
                    // Log.e("", "person: " + person.getDisplayName());
                    // Log.e(getClass().toString(),
                    // "getObjectType: " + person.getObjectType());
                    // Log.e(getClass().toString(), "url: " + person.getUrl());
                    //
                    // Log.e("", "id = " + person.getId());

                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG,
                    "Error listing people: " + connectionResult.getErrorCode());
        }
    }
}
