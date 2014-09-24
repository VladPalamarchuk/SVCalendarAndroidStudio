package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class AddFriendActivity extends Activity {
    Intent intent;

    ArrayList<String> contacs = new ArrayList<String>();
    ArrayList<String> contacs_by_adapter = new ArrayList<String>();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            MainActivity.getInstance(),
            android.R.layout.simple_list_item_multiple_choice,
            contacs_by_adapter);

    ArrayList<Integer> arrayPhoneId = new ArrayList<Integer>();
    ArrayList<Integer> arrayVkId = new ArrayList<Integer>();
    ArrayList<Integer> arrayFbId = new ArrayList<Integer>();

    ArrayList<OneFriend> arrayVKFriend = MainActivity.getInstance()
            .getFriendByType(OneFriend.VK_TYPE);
    ArrayList<OneFriend> arrayFbFriend = MainActivity.getInstance()
            .getFriendByType(OneFriend.FACEBOOK_TYPE);
    ArrayList<OneFriend> arrayPhoneFriend = MainActivity.getInstance()
            .getFriendByType(OneFriend.PHONE_TYPE);

    ArrayList<OneFriend> result = AddEventFragment.getInstance().arrayFriends;

    int TYPE_PHONE = 1;
    int TYPE_VK = 2;
    int TYPE_FB = 3;
    int type = 1;

    boolean thread = false;
    Button add;
    ArrayList<String> arrayNamesVk = new ArrayList<String>();
    ArrayList<String> arrayNamesFB = new ArrayList<String>();
    ArrayList<String> arrayNamesPhone = new ArrayList<String>();

    ArrayList<String> PhoneNames = new ArrayList<String>();
    ArrayList<String> PhonePhones = new ArrayList<String>();
    Handler handlerUpdateAdadpter;

    RelativeLayout vk;

    RelativeLayout phone;

    RelativeLayout fb;

    ListView list_contacs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_friend);

        getData();
        findViewById(R.id.add_friend_list).setVisibility(View.INVISIBLE);
        findViewById(R.id.add_friend_progres).setVisibility(View.VISIBLE);
        vk = (RelativeLayout) findViewById(R.id.add_friend_vk);

        phone = (RelativeLayout) findViewById(R.id.add_friend_phone);

        fb = (RelativeLayout) findViewById(R.id.add_friend_fb);
        add = (Button) findViewById(R.id.add_friend_add);
        list_contacs = (ListView) findViewById(R.id.add_friend_list);
        list_contacs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list_contacs.setAdapter(adapter);

        getPhoneContacs();

        intent = getIntent();

        handlerUpdateAdadpter = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                if (type == 1) {
                    contacs.clear();

                    for (int i = 0; i < PhoneNames.size(); i++)
                        contacs.add(PhoneNames.get(i));

                }

                for (int i = 0; i < list_contacs.getCount(); i++)
                    list_contacs.setItemChecked(i, false);

                if (type == 3) {
                    for (int i = 0; i < arrayFbId.size(); i++) {
                        int id = arrayFbId.get(i);

                        for (int j = 0; j < arrayFbFriend.size(); j++) {
                            if (arrayFbFriend.get(j).getId() == id) {
                                list_contacs.setItemChecked(j, true);
                            }
                        }
                    }
                }

                if (type == 2) {
                    for (int i = 0; i < arrayVkId.size(); i++) {
                        int id = arrayVkId.get(i);

                        for (int j = 0; j < arrayVKFriend.size(); j++) {
                            if (arrayVKFriend.get(j).getId() == id) {
                                list_contacs.setItemChecked(j, true);
                            }
                        }
                    }
                }

                if (type == 1)

                {
                    for (int i = 0; i < arrayPhoneId.size(); i++) {
                        int id = arrayPhoneId.get(i);

                        for (int j = 0; j < arrayPhoneFriend.size(); j++) {
                            if (arrayPhoneFriend.get(j).getId() == id) {
                                list_contacs.setItemChecked(j, true);
                            }
                        }
                    }
                }

                contacs_by_adapter.clear();

                contacs_by_adapter.addAll(contacs);
                adapter.notifyDataSetChanged();
                list_contacs.setSelection(0);

                thread = false;
                findViewById(R.id.add_friend_list).setVisibility(View.VISIBLE);
                findViewById(R.id.add_friend_progres).setVisibility(
                        View.INVISIBLE);

            }
        };

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub
                result.clear();

                for (int i = 0; i < arrayPhoneId.size(); i++) {
                    int id = arrayPhoneId.get(i);

                    for (int j = 0; j < arrayPhoneFriend.size(); j++) {
                        if (arrayPhoneFriend.get(j).getId() == id)
                            result.add(arrayPhoneFriend.get(j));
                    }

                }

                for (int i = 0; i < arrayVkId.size(); i++) {
                    int id = arrayVkId.get(i);

                    for (int j = 0; j < arrayVKFriend.size(); j++) {
                        if (arrayVKFriend.get(j).getId() == id)
                            result.add(arrayVKFriend.get(j));
                    }

                }

                for (int i = 0; i < arrayFbId.size(); i++) {
                    int id = arrayFbId.get(i);

                    for (int j = 0; j < arrayFbFriend.size(); j++) {
                        if (arrayFbFriend.get(j).getId() == id)
                            result.add(arrayFbFriend.get(j));
                    }

                }

                finish();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                ((ImageView) findViewById(R.id.add_friend_phone_line))
                        .setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.add_friend_vk_line))
                        .setVisibility(View.INVISIBLE);
                ((ImageView) findViewById(R.id.add_friend_fb_line))
                        .setVisibility(View.INVISIBLE);

                type = 1;
                if (!thread) {
                    findViewById(R.id.add_friend_list).setVisibility(
                            View.INVISIBLE);
                    findViewById(R.id.add_friend_progres).setVisibility(
                            View.VISIBLE);
                    contacs.clear();

                    getPhoneContacs();
                }

            }
        });

        vk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                // TODO Auto-generated method stub

                ((ImageView) findViewById(R.id.add_friend_phone_line))
                        .setVisibility(View.INVISIBLE);
                ((ImageView) findViewById(R.id.add_friend_vk_line))
                        .setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.add_friend_fb_line))
                        .setVisibility(View.INVISIBLE);

                findViewById(R.id.add_friend_list)
                        .setVisibility(View.INVISIBLE);
                findViewById(R.id.add_friend_progres).setVisibility(
                        View.VISIBLE);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        type = 2;

                        getData();
                        contacs.clear();

                        for (int i = 0; i < arrayNamesVk.size(); i++)
                            contacs.add(arrayNamesVk.get(i));

                        Message msg = handlerUpdateAdadpter.obtainMessage(1,
                                "2");
                        handlerUpdateAdadpter.sendMessage(msg);
                    }
                }).start();

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlaySoundButton();
                ((ImageView) findViewById(R.id.add_friend_phone_line))
                        .setVisibility(View.INVISIBLE);
                ((ImageView) findViewById(R.id.add_friend_vk_line))
                        .setVisibility(View.INVISIBLE);
                ((ImageView) findViewById(R.id.add_friend_fb_line))
                        .setVisibility(View.VISIBLE);

                // TODO Auto-generated method stub
                findViewById(R.id.add_friend_list)
                        .setVisibility(View.INVISIBLE);
                findViewById(R.id.add_friend_progres).setVisibility(
                        View.VISIBLE);
                type = 3;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        getData();
                        contacs.clear();
                        for (int i = 0; i < arrayNamesFB.size(); i++)
                            contacs.add(arrayNamesFB.get(i));

                        Message msg = handlerUpdateAdadpter.obtainMessage(1,
                                "3");
                        handlerUpdateAdadpter.sendMessage(msg);
                    }
                }).start();

            }

        });

        list_contacs.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                getArray(type).clear();
                new PlaySoundButton();

                SparseBooleanArray sbArray = list_contacs
                        .getCheckedItemPositions();
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key)) {
                        if (type == 1)
                            getArray(type).add(
                                    arrayPhoneFriend.get(key).getId());
                        if (type == 2)
                            getArray(type).add(arrayVKFriend.get(key).getId());
                        if (type == 3)
                            getArray(type).add(arrayFbFriend.get(key).getId());
                    }
                }
            }
        });

        setComponents();

    }

    @SuppressLint("NewApi")
    public void setComponents() {
        add.setText(MainActivity.getInstance().language.ADD_EVENT_ADD_FRIEND);
        MainActivity.getInstance().setButton(add);
        findViewById(R.id.add_friend_phone).setBackgroundColor(
                new MyColor().getColorComponents());
        findViewById(R.id.add_friend_vk).setBackgroundColor(
                new MyColor().getColorComponents());
        findViewById(R.id.add_friend_fb).setBackgroundColor(
                new MyColor().getColorComponents());

        ((RelativeLayout) findViewById(R.id.add_friend_container))
                .setBackground(MainActivity.getInstance()
                        .findViewById(R.id.main_root).getBackground());

    }

    public void getPhoneContacs() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                thread = true;
                // TODO Auto-generated method stub

                type = 1;

                arrayPhoneFriend = MainActivity.getInstance().getFriendByType(
                        OneFriend.PHONE_TYPE);
                PhoneNames.clear();
                for (int i = 0; i < arrayPhoneFriend.size(); i++) {
                    PhoneNames.add(arrayPhoneFriend.get(i).getFirst_name());
                }

                Message msg = handlerUpdateAdadpter.obtainMessage(1, "1");
                handlerUpdateAdadpter.sendMessage(msg);

            }
        }).start();
    }

    public void getData() {
        arrayVKFriend = MainActivity.getInstance().getFriendByType(
                OneFriend.VK_TYPE);
        arrayFbFriend = MainActivity.getInstance().getFriendByType(
                OneFriend.FACEBOOK_TYPE);
        arrayPhoneFriend = MainActivity.getInstance().getFriendByType(
                OneFriend.PHONE_TYPE);

        arrayNamesVk.clear();
        arrayNamesFB.clear();
        arrayNamesPhone.clear();
        for (int i = 0; i < arrayVKFriend.size(); i++) {
            arrayNamesVk.add(arrayVKFriend.get(i).getFirst_name() + " "
                    + arrayVKFriend.get(i).getLast_name());
        }
        for (int i = 0; i < arrayFbFriend.size(); i++) {
            arrayNamesFB.add(arrayFbFriend.get(i).getFirst_name() + " "
                    + arrayFbFriend.get(i).getLast_name());
        }
        for (int i = 0; i < arrayPhoneFriend.size(); i++) {
            arrayNamesPhone.add(arrayPhoneFriend.get(i).getFirst_name() + " "
                    + arrayPhoneFriend.get(i).getLast_name());
        }
    }

    public ArrayList<Integer> getArray(int type) {
        if (type == TYPE_PHONE)
            return arrayPhoneId;

        if (type == TYPE_VK)
            return arrayVkId;
        if (type == TYPE_FB)
            return arrayFbId;
        return null;
    }
}
