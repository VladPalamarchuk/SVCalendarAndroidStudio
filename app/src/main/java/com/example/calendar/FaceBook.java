package com.example.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;

import java.util.ArrayList;
import java.util.List;

public class FaceBook {

    ArrayList<OneFriend> arrayFrinds = new ArrayList<OneFriend>();
    public boolean isAlive = true;
    Activity context;

    public FaceBook(Activity context, ArrayList<OneFriend> arrayFrinds) {
        this.arrayFrinds = arrayFrinds;
        this.context = context;
        authorize();
    }

    public final String[] permissions = {"friends_birthday", "user_friends",
            "user_birthday", "friends_location", "friends_location",
            "user_photos"};
    // calendar 1480282278855729

    // redio 287486771405706
    public final String API_KEY = "287486771405706";
    Facebook facebook = new Facebook(API_KEY);

    @SuppressWarnings("deprecation")
    public void authorize() {
        facebook.authorize(context, permissions, Facebook.FORCE_DIALOG_AUTH,
                new DialogListener() {

                    @Override
                    public void onComplete(Bundle values) {

                        ParseFriend();
                        ParseMe();
                    }

                    @Override
                    public void onFacebookError(FacebookError e) {
                        isAlive = false;
                    }

                    @Override
                    public void onError(DialogError e) {
                        isAlive = false;

                    }

                    @Override
                    public void onCancel() {
                        isAlive = false;

                    }
                });
    }

    public void ParseMe() {

        Request request = Request.newMeRequest(facebook.getSession(),
                new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(final GraphUser user,
                                            Response response) {
                        // TODO Auto-generated method stub
                        try {
                            Log.e(getClass().toString(), "response = "
                                    + response);
                            MainActivity.getInstance().shared
                                    .edit()
                                    .putString(
                                            MainActivity.SHARED_FACEBOOK_FIRST_NAME,
                                            user.getFirstName()).commit();
                            MainActivity.getInstance().shared
                                    .edit()
                                    .putString(
                                            MainActivity.SHARED_FACEBOOK_LAST_NAME,
                                            user.getLastName()).commit();

                            Bundle params = new Bundle();
                            params.putBoolean("redirect", false);
                            params.putString("height", "50");
                            params.putString("type", "normal");
                            params.putString("width", "50");

                            new Request(facebook.getSession(), "/me/picture",
                                    params, HttpMethod.GET,
                                    new Request.Callback() {
                                        public void onCompleted(
                                                Response response) {

                                            final String resp = response
                                                    .toString();

                                            new Thread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    // TODO Auto-generated
                                                    // method stub
                                                    String photo = resp.substring(
                                                            resp.indexOf("url") + 6,
                                                            resp.indexOf("height") - 3);
                                                    photo = photo.replace("\\",
                                                            "");
                                                    Log.e("", "photo = "
                                                            + photo);

                                                    new FileLoader()
                                                            .loadFile(
                                                                    photo,
                                                                    MainActivity
                                                                            .getInstance()
                                                                            .getFileName(
                                                                                    photo));
                                                    MainActivity.getInstance().shared
                                                            .edit()
                                                            .putString(
                                                                    MainActivity.SHARED_FACEBOOK_PHOTO,
                                                                    photo)
                                                            .commit();
                                                }
                                            }).start();

                                        }
                                    }).executeAsync();

                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.e(getClass().toString()
                                    + "line = "
                                    + Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber(), "catch:" + e);
                        }

                    }
                });

        Bundle params = new Bundle();
        params.putString("fields",
                " id,photos , first_name, last_name, birthday");

        request.setParameters(params);
        request.executeAsync();
    }

    public void ParseFriend() {

        Request request = Request.newMyFriendsRequest(facebook.getSession(),
                new Request.GraphUserListCallback() {

                    @Override
                    public void onCompleted(List<GraphUser> users,
                                            Response response) {
                        // TODO Auto-generated method stub

                        for (int i = 0; i < users.size(); i++) {

                            String b;
                            if (users.get(i).getBirthday() != null)
                                b = users.get(i).getBirthday();
                            else
                                b = "";

                            if (b != "") {
                                b = b.replace("/", ".");

                                b = b.substring(3, 5) + "." + b.substring(0, 2);
                            }

                            String photo = "http://graph.facebook.com/"
                                    + users.get(i).getId()
                                    + "/picture?type=large";

                            arrayFrinds.add(new OneFriend(
                                    0,
                                    users.get(i).getFirstName(),
                                    users.get(i).getLastName(),
                                    b,
                                    "",
                                    OneFriend.FACEBOOK_TYPE,
                                    ""
                                            + MainActivity.getInstance().shared
                                            .getInt(MainActivity.SHARED_CITY,
                                                    33345), photo));

                        }
                        isAlive = false;
                    }

                });

        Bundle params = new Bundle();
        params.putString("fields",
                " first_name, last_name, birthday, location ");
        request.setParameters(params);
        request.executeAsync();

    }

}
