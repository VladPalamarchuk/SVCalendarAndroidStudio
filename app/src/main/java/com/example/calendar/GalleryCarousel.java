package com.example.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class GalleryCarousel {

    // public static final String SELECTED_IMAGE_INTENT_KEY = "siik";
    // public static final int GALLERY_CAROUSEL_INTENT_RESULT_KEY = 3;

    private Gallery gallery;
    private GalleryAdapter galleryAdapter;
    private ArrayList<Integer> imagesFromDrawableResFolder = null;
    private Integer selectedImageResource;

    int pos = 0;
    private AlertDialog.Builder builder;

    Context context;

    public GalleryCarousel(Context context) {
        this.context = context;
        initImageResources();
        initPopupWithImages();
    }

    private void initPopupWithImages() {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.galery_dialog, null);

        galleryAdapter = new GalleryAdapter(imagesFromDrawableResFolder);

        gallery = (Gallery) dialogView
                .findViewById(R.id.gallery_dialog_gallery);
        gallery.setAdapter(galleryAdapter);

        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setPositiveButton(
                MainActivity.getInstance().language.ADD_EVENT_SAVE,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity
                                .getInstance()
                                .findViewById(R.id.main_root)
                                .setBackgroundResource(
                                        imagesFromDrawableResFolder.get(pos));

                        MainActivity.getInstance().shared
                                .edit()
                                .putInt(MainActivity.SHARED_BACGROUND_IMAGE,
                                        imagesFromDrawableResFolder.get(pos))
                                .commit();
                    }
                });
        builder.setNegativeButton(
                MainActivity.getInstance().language.ADD_EVENT_CANCEL,
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        new PlaySoundButton();
                    }
                });
        builder.show();
    }

    private class GalleryAdapter extends BaseAdapter {

        private ArrayList<Integer> imagesPath;

        public GalleryAdapter(ArrayList<Integer> imagesPath) {
            if (imagesPath == null) {
                throw new RuntimeException("Gallery adapter cannot apply "
                        + "null collection to constructor");
            }
            this.imagesPath = imagesPath;

        }

        @Override
        public int getCount() {
            return imagesPath.size();

        }

        @Override
        public Integer getItem(int position) {
            return imagesPath.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(new Gallery.LayoutParams(
                    Gallery.LayoutParams.MATCH_PARENT,
                    Gallery.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            InputStream is = MainActivity.getInstance().getResources()
                    .openRawResource(getItem(position));

            Bitmap preview_bitmap = decodeFile(is);
            if (preview_bitmap != null)
                iv.setImageBitmap(preview_bitmap);

            selectedImageResource = getItem(position);

            return iv;
        }
    }

    private Bitmap decodeFile(InputStream is) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 400;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(is, null, o2);
        } catch (Exception e) {
        }
        return null;
    }

    private void initImageResources() {
        imagesFromDrawableResFolder = new ArrayList<Integer>();

        imagesFromDrawableResFolder.add(R.drawable.f1);
        imagesFromDrawableResFolder.add(R.drawable.f2);
        imagesFromDrawableResFolder.add(R.drawable.f3);
        imagesFromDrawableResFolder.add(R.drawable.f4);
        imagesFromDrawableResFolder.add(R.drawable.f5);
        imagesFromDrawableResFolder.add(R.drawable.f6);
        imagesFromDrawableResFolder.add(R.drawable.f7);
        imagesFromDrawableResFolder.add(R.drawable.f8);
        imagesFromDrawableResFolder.add(R.drawable.f9);
        imagesFromDrawableResFolder.add(R.drawable.f10);
        imagesFromDrawableResFolder.add(R.drawable.f11);
        imagesFromDrawableResFolder.add(R.drawable.f12);
        imagesFromDrawableResFolder.add(R.drawable.f13);
        imagesFromDrawableResFolder.add(R.drawable.f14);
        imagesFromDrawableResFolder.add(R.drawable.f15);
        imagesFromDrawableResFolder.add(R.drawable.f16);
        imagesFromDrawableResFolder.add(R.drawable.f17);
        imagesFromDrawableResFolder.add(R.drawable.f18);
        imagesFromDrawableResFolder.add(R.drawable.f19);
        imagesFromDrawableResFolder.add(R.drawable.f20);
        imagesFromDrawableResFolder.add(R.drawable.f21);
        imagesFromDrawableResFolder.add(R.drawable.f22);
        imagesFromDrawableResFolder.add(R.drawable.f23);
        imagesFromDrawableResFolder.add(R.drawable.f24);
        imagesFromDrawableResFolder.add(R.drawable.f25);
        imagesFromDrawableResFolder.add(R.drawable.f26);
        imagesFromDrawableResFolder.add(R.drawable.f27);
        imagesFromDrawableResFolder.add(R.drawable.f28);
        imagesFromDrawableResFolder.add(R.drawable.f29);
        imagesFromDrawableResFolder.add(R.drawable.f30);
        imagesFromDrawableResFolder.add(R.drawable.f31);
        imagesFromDrawableResFolder.add(R.drawable.f32);
        imagesFromDrawableResFolder.add(R.drawable.f33);
        imagesFromDrawableResFolder.add(R.drawable.f34);
        imagesFromDrawableResFolder.add(R.drawable.f35);
        imagesFromDrawableResFolder.add(R.drawable.f36);
        imagesFromDrawableResFolder.add(R.drawable.f37);
        imagesFromDrawableResFolder.add(R.drawable.f38);
        imagesFromDrawableResFolder.add(R.drawable.f39);
        imagesFromDrawableResFolder.add(R.drawable.f40);
        imagesFromDrawableResFolder.add(R.drawable.f41);
        imagesFromDrawableResFolder.add(R.drawable.f42);
        imagesFromDrawableResFolder.add(R.drawable.f43);
        imagesFromDrawableResFolder.add(R.drawable.f44);
        imagesFromDrawableResFolder.add(R.drawable.f45);
        imagesFromDrawableResFolder.add(R.drawable.f46);
        imagesFromDrawableResFolder.add(R.drawable.f47);
        imagesFromDrawableResFolder.add(R.drawable.f47);
        imagesFromDrawableResFolder.add(R.drawable.f48);
        imagesFromDrawableResFolder.add(R.drawable.f48);
        imagesFromDrawableResFolder.add(R.drawable.f49);
        imagesFromDrawableResFolder.add(R.drawable.f50);
        imagesFromDrawableResFolder.add(R.drawable.base_theme_background);
    }
}
