package com.example.calendar;

import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class shareFaceBook {

    public final String[] permissions = {"publish_stream", "friends_birthday",
            "user_friends", "user_birthday", "friends_location",
            "friends_location", "user_photos"};
    public final String API_KEY = "287486771405706";
    Facebook facebook = new Facebook(API_KEY);

    public shareFaceBook(String name, String picture, String link) {
        Share(name, picture, link);
    }

    @SuppressWarnings("deprecation")
    public void Share(final String name, final String image, final String link) {

        Log.d("Facebook", "Start Authorization");
        facebook.authorize(MainActivity.getInstance(), permissions,
                new DialogListener() {
                    @Override
                    public void onComplete(Bundle values) {
                        Bundle messageBunlde = new Bundle();
                        messageBunlde.putString("name", name);
                        messageBunlde.putString("picture", image);
                        messageBunlde.putString("link", link);

                        facebook.dialog(MainActivity.getInstance(), "feed",
                                messageBunlde, new DialogListener() {
                                    @Override
                                    public void onComplete(Bundle values) {
                                        Log.d("Facebook", "TNX");
                                    }

                                    @Override
                                    public void onFacebookError(FacebookError e) {
                                        Log.d("facebook",
                                                "Facebook error, try again later");
                                    }

                                    @Override
                                    public void onError(DialogError e) {
                                        Log.d("facebook",
                                                "Facebook error, try again later");
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.d("facebook",
                                                "Authorization canceled");
                                    }
                                });

                    }

                    @Override
                    public void onFacebookError(FacebookError e) {

                    }

                    @Override
                    public void onError(DialogError e) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

}
