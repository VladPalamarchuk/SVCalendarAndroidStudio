package com.example.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PhoneContacs {

    Context context;
    String name = "";
    String phone = "";
    String email = "";
    String photo_uri = null;
    int key = 0;

    public PhoneContacs(Context context) {
        this.context = context;
    }

    public ArrayList<OneFriend> getContacs() {
        ArrayList<OneFriend> result = new ArrayList<OneFriend>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(Contacts.CONTENT_URI, null,
                null, null, null);

        if (cur.moveToFirst()) {

            while (cur.moveToNext()) {

                String id = cur.getString(cur
                        .getColumnIndex(Contacts._ID));
                name = cur
                        .getString(cur
                                .getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));

                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }
                    pCur.close();

                    Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    while (emailCur.moveToNext()) {

                        email = emailCur
                                .getString(emailCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur
                                .getString(emailCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                    }
                    emailCur.close();

                    String photo_path = Environment
                            .getExternalStorageDirectory()
                            + "/bCalendar/"
                            + id
                            + ".png";

                    try {
                        InputStream is = openPhoto(Long.parseLong(id));

                        if (is != null) {

                            OutputStream os = new FileOutputStream(photo_path);

                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                os.write(buffer, 0, bytesRead);
                            }
                            is.close();
                            // flush OutputStream to write any buffered data to
                            // file
                            os.flush();
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    result.add(new OneFriend(0, name, " ", "", phone,
                            OneFriend.PHONE_TYPE, "", photo_path));
                    photo_uri = null;
                }

            }

        }
        return result;
    }

    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
                contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri,
                Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}
