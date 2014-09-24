package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class FriendAdapter extends ArrayAdapter<OneFriend> {

    Context context;
    List<OneFriend> items;

    public FriendAdapter(Context context, int resourceId, List<OneFriend> items) {
        super(context, resourceId, items);

        this.context = context;
        this.items = items;
    }

    private class ViewHolder {

        ImageView img;
        TextView name_surname;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        final OneFriend friend = getItem(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.one_friend_list, null);

            holder.img = (ImageView) convertView
                    .findViewById(R.id.one_friend_list_photo);

            holder.name_surname = (TextView) convertView
                    .findViewById(R.id.one_friend_list_name_surname);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.name_surname.setText(friend.getFirst_name() + " "
                + friend.getLast_name());

        holder.img.setImageResource(R.drawable.no_photo);
        try {

            if (friend.getPhoto().length() > 0 && friend.getType() != OneFriend.PHONE_TYPE) {
                MainActivity.imageLoader.displayImage(friend.getPhoto(),
                        holder.img);

            }

            if (friend.getType() == OneFriend.PHONE_TYPE) {
                File imgFile = new File(friend.getPhoto());
                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
                            .getAbsolutePath());

                    holder.img.setImageBitmap(myBitmap);

                }
            }

        } catch (Exception e) {
            holder.img.setImageResource(R.drawable.no_photo);
        }
        Animation anim = AnimationUtils.loadAnimation(
                MainActivity.getInstance(), R.anim.myanimation);

        convertView.startAnimation(anim);
        return convertView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.items.size();
    }

    public void Update(List<OneFriend> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

}