package com.example.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class GetPhotoByNumber {
    Context context;

    public GetPhotoByNumber(Context context) {
        this.context = context;
    }

    public Uri getPhoto(String number) {

        return getPhotoUri(Long
                .parseLong(fetchContactIdFromPhoneNumber(number)));
    }

    private String fetchContactIdFromPhoneNumber(String phoneNumber) {
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{PhoneLookup.DISPLAY_NAME, PhoneLookup._ID},
                null, null, null);

        String contactId = "";

        if (cursor.moveToFirst()) {
            do {
                contactId = cursor.getString(cursor
                        .getColumnIndex(PhoneLookup._ID));
            } while (cursor.moveToNext());
        }

        return contactId;
    }

    private Uri getPhotoUri(long contactId) {
        ContentResolver contentResolver = context.getContentResolver();

        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }
}
