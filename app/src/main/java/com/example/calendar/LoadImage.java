package com.example.calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImage {

    Bitmap btm;
    String src;

    OnLoadImageListener onLoadImageListener;

    public void setOnLoadImageListener(OnLoadImageListener onLoadImageListener) {
        this.onLoadImageListener = onLoadImageListener;
    }

    LoadImage(String src) {
        this.src = src;
    }

    public void load() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();

                    connection.setInstanceFollowRedirects(true);
                    HttpURLConnection.setFollowRedirects(true);

                    connection.setDoInput(true);
                    connection.connect();

                    boolean redirect = false;

                    int status = connection.getResponseCode();
                    if (status != HttpURLConnection.HTTP_OK) {
                        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                                || status == HttpURLConnection.HTTP_MOVED_PERM
                                || status == HttpURLConnection.HTTP_SEE_OTHER)
                            redirect = true;
                    }


                    if (redirect) {
                        String newUrl = connection.getHeaderField("Location");
                        connection = (HttpURLConnection) new URL(newUrl).openConnection();
                    }


                    InputStream input = connection.getInputStream();
                    btm = BitmapFactory.decodeStream(input);

                } catch (IOException e) {
                    e.printStackTrace();

                }
                onLoadImageListener.OnCompleteLoad();
            }
        }).start();
    }

    public interface OnLoadImageListener {
        public void OnCompleteLoad();
    }

    public Bitmap getImage() {
        return btm;
    }

}
