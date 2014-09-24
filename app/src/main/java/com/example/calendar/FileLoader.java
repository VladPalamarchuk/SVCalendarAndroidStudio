package com.example.calendar;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileLoader {

    public static File rootDirectory = new File(
            Environment.getExternalStorageDirectory() + "/bCalendar");
    boolean isLoad = true;
    public static String path = Environment.getExternalStorageDirectory() + "/bCalendar/";
    private int READ_TIMEOUT = 60 * 60 * 30; // 30 minute;
    private int CONNECTION_TIMEOUT = 60 * 60 * 30;

    public FileLoader() {

    }


    public void loadFile(String url, String name) {
        Log.e("", "rootDirectory = " + rootDirectory);
        final File storage = new File(Environment.getExternalStorageDirectory()
                + "/bCalendar/" + name);
        URL urlObject = null;
        URLConnection con = null;

        BufferedInputStream dis = null;
        FileOutputStream fos = null;
        final int bufferSize = 1024 * 32;
        byte[] buffer = new byte[bufferSize];
        try {
            urlObject = new URL(url);
            con = urlObject.openConnection();
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            dis = new BufferedInputStream(con.getInputStream());
            fos = new FileOutputStream(storage);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int len = 0;
            while ((len = dis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            bos.close();
            fos.close();
            isLoad = false;
            File f = new File(rootDirectory, name);
            Log.e("", "file exist = " + f.exists());
        } catch (Exception e) {
            Log.e("FileLoader -> load ", e.toString());
        }
    }
}




