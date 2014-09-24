package com.example.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.calendar.LoadImage.OnLoadImageListener;

import java.io.File;
import java.util.ArrayList;

public class ListContacFragment extends android.support.v4.app.Fragment {

    static ListContacFragment oneLIsContacFragment;

    ViewGroup root;
    ListView list_contacs;
    ArrayList<OneFriend> contacs = new ArrayList<OneFriend>();

    RelativeLayout phone;
    RelativeLayout vk;
    RelativeLayout fb;
    FriendAdapter adapter;

    int type = OneFriend.PHONE_TYPE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        root = (ViewGroup) inflater.inflate(R.layout.list_contacs, null);
        oneLIsContacFragment = this;
        getPhoneContacs();

        adapter = new FriendAdapter(MainActivity.getInstance(),
                R.layout.one_friend_list, contacs);

        // adapter = new FriendAdapter(MainActivity.getInstance(),
        // R.layout.one_friend_list, contacs);
        list_contacs = (ListView) root.findViewById(R.id.list_contacs_list);
        phone = (RelativeLayout) root.findViewById(R.id.list_contacs_phone);
        vk = (RelativeLayout) root.findViewById(R.id.list_contacs_vk);
        fb = (RelativeLayout) root.findViewById(R.id.list_contacs_fb);

        list_contacs.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        MainActivity
                .getInstance()
                .setOpenLeftMenu(
                        (ImageView) root
                                .findViewById(R.id.list_contact_fragment_open_left_menu),
                        (Spinner) root.findViewById(R.id.contacs_spinner));

        phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                type = OneFriend.PHONE_TYPE;
                contacs.clear();
                getPhoneContacs();

                adapter.notifyDataSetChanged();
                list_contacs.setSelection(0);
                root.findViewById(R.id.vk_line).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.fb_line).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.phone_line).setVisibility(View.VISIBLE);

            }
        });

        vk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                type = OneFriend.VK_TYPE;
                root.findViewById(R.id.vk_line).setVisibility(View.VISIBLE);
                root.findViewById(R.id.fb_line).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.phone_line)
                        .setVisibility(View.INVISIBLE);

                contacs.clear();

                contacs.addAll(MainActivity.getInstance().getFriendByType(
                        OneFriend.VK_TYPE));

                adapter.notifyDataSetChanged();

                list_contacs.setSelection(0);

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                type = OneFriend.FACEBOOK_TYPE;
                root.findViewById(R.id.vk_line).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.fb_line).setVisibility(View.VISIBLE);
                root.findViewById(R.id.phone_line)
                        .setVisibility(View.INVISIBLE);

                contacs.clear();
                contacs.addAll(MainActivity.getInstance().getFriendByType(
                        OneFriend.FACEBOOK_TYPE));
                adapter.notifyDataSetChanged();
                list_contacs.setSelection(0);

            }
        });
        setColor();

        list_contacs.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                new PlaySoundButton();
                showOneContact(contacs.get(position));

            }

        });
        return root;

    }

    static public ListContacFragment getInstance() {
        return oneLIsContacFragment;
    }

    public void setColor()

    {
        MyColor c = new MyColor();
        vk.setBackgroundColor(c.getColorComponents());
        fb.setBackgroundColor(c.getColorComponents());
        phone.setBackgroundColor(c.getColorComponents());

        root.findViewById(R.id.list_contacs_fragment_top).setBackgroundColor(
                c.getColorComponents());
        root.findViewById(R.id.list_contacs_background).setBackgroundColor(
                c.getColorBacground());
    }

    public void getPhoneContacs() {

        contacs.clear();

        contacs.addAll(MainActivity.getInstance().getFriendByType(
                OneFriend.PHONE_TYPE));

    }

    public void showOneContact(final OneFriend oneFriend) {

        if (oneFriend != null) {
            AlertDialog dialog = new AlertDialog.Builder(
                    MainActivity.getInstance()).create();

            LayoutInflater inflate = (LayoutInflater) MainActivity
                    .getInstance().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout root = (LinearLayout) inflate.inflate(
                    R.layout.one_friend, null);

            TextView name_title = ((TextView) root
                    .findViewById(R.id.one_friend_name_title));
            TextView contact_title = ((TextView) root
                    .findViewById(R.id.one_friend_contact_title));
            TextView surname_title = ((TextView) root
                    .findViewById(R.id.one_friend_surname_title));
            TextView bdate_title = ((TextView) root
                    .findViewById(R.id.one_friend_bdate_title));
            TextView locatin_title = ((TextView) root
                    .findViewById(R.id.one_friend_location_title));

            TextView name = ((TextView) root.findViewById(R.id.one_friend_name));
            TextView contact = ((TextView) root
                    .findViewById(R.id.one_friend_contact));
            TextView surname = ((TextView) root
                    .findViewById(R.id.one_friend_surname));
            TextView bdate = ((TextView) root
                    .findViewById(R.id.one_friend_bdate));
            TextView locatin = ((TextView) root
                    .findViewById(R.id.one_friend_location));
            final ImageView img = ((ImageView) root
                    .findViewById(R.id.one_friend_foto));

            if (oneFriend.getFirst_name().length() > 0) {
                name_title.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                name_title.setText(MainActivity.getInstance().language.NAME);
                name.setText(oneFriend.getFirst_name());
            }

            if (oneFriend.getLast_name().length() > 0) {
                surname_title.setVisibility(View.VISIBLE);
                surname.setVisibility(View.VISIBLE);
                surname_title
                        .setText(MainActivity.getInstance().language.SURNAME);
                surname.setText(oneFriend.getLast_name());
            }

            if (oneFriend.getContact().length() > 0) {
                contact_title.setVisibility(View.VISIBLE);
                contact.setVisibility(View.VISIBLE);
                contact_title
                        .setText(MainActivity.getInstance().language.CONTACT);
                contact.setText(oneFriend.getContact());
            }

            if (oneFriend.getDate_birthday().length() > 0) {
                bdate_title.setVisibility(View.VISIBLE);
                bdate.setVisibility(View.VISIBLE);
                bdate_title.setText(MainActivity.getInstance().language.BDATE);
                bdate.setText(oneFriend.getDate_birthday());
            }

            try {

                if (oneFriend.getLocation().length() > 0) {

                    String loc = MainActivity.getInstance().getCountryById(
                            MainActivity.getInstance().CITIES,
                            Integer.parseInt(oneFriend.getLocation()))
                            + ", "
                            + MainActivity.getInstance().getCityById(
                            MainActivity.getInstance().CITIES,
                            Integer.parseInt(oneFriend.getLocation()));
                    Log.e(getClass().toString(),
                            "location = " + oneFriend.getLocation());
                    Log.e(getClass().toString(), "loc = " + loc);
                    if (loc.length() > 0) {
                        locatin_title.setVisibility(View.VISIBLE);
                        locatin.setVisibility(View.VISIBLE);
                        locatin_title
                                .setText(MainActivity.getInstance().language.ONE_EVENT_LOCATION_LABEL);
                        locatin.setText(loc);
                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e(getClass().toString()
                        + "line = "
                        + Thread.currentThread().getStackTrace()[2]
                        .getLineNumber(), "catch:" + e);
            }

            if (oneFriend.getPhoto().length() > 0
                    && oneFriend.getType() != OneFriend.PHONE_TYPE) {

                final LoadImage loadImage = new LoadImage(oneFriend.getPhoto());

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        super.handleMessage(msg);

                        try {

                            img.setVisibility(View.VISIBLE);
                            img.setImageBitmap(loadImage.getImage());

                            if (!MainActivity.getInstance()
                                    .InternetConnection())
                                img.setVisibility(View.GONE);

                        } catch (Exception e) {
                            Log.e(getClass().toString(), "catc: = " + e);
                        }

                    }
                };
                loadImage.setOnLoadImageListener(new OnLoadImageListener() {

                    @Override
                    public void OnCompleteLoad() {
                        // TODO Auto-generated method stub

                        handler.sendMessage(handler.obtainMessage(1, 1));
                    }
                });
                loadImage.load();
            } else if (oneFriend.getType() == OneFriend.PHONE_TYPE) {
                try {

                    File imgFile = new File(oneFriend.getPhoto());
                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
                                .getAbsolutePath());

                        img.setVisibility(View.VISIBLE);
                        img.setImageBitmap(myBitmap);

                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }
            }

            dialog.setButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });

            if (oneFriend.getType() == OneFriend.FACEBOOK_TYPE) {
                locatin.setVisibility(View.GONE);
                locatin_title.setVisibility(View.GONE);
            }

            dialog.setView(root);

            if (!MainActivity.getInstance().InternetConnection())
                img.setVisibility(View.GONE);
            dialog.show();
        }

    }
}
