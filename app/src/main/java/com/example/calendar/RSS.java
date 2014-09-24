package com.example.calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("HandlerLeak")
public class RSS {

    String src;
    ArrayList<OneRss> result = new ArrayList<OneRss>();
    public OnCompleteListener completeListener;

    boolean isItem = false;
    boolean isTitle = false;
    boolean isLink = false;
    boolean isDiscription = false;
    boolean isDate = false;
    boolean isFullText = false;
    boolean isAuthor = false;

    Context context;

    public void setOnCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    ProgressDialog progressDialog;
    Handler handler;

    public RSS(final Context context, String src) {
        this.src = src;
        this.context = context;
        progressDialog = ProgressDialog.show(context, "RSS", "Loading RSS");

        Parse();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                AlertDialog dialog = new AlertDialog.Builder(context).create();

                ListView list = new ListView(context);
                AdapterRSS adapter = new AdapterRSS(context, R.layout.one_rss,
                        result);
                list.setAdapter(adapter);
                dialog.setView(list);
                dialog.setCancelable(true);

                if (result.size() > 0) {
                    dialog.setTitle(result.get(0).getChanel_name());
                }

                if (result.size() == 0)
                    Toast.makeText(context, "Error RSS adress",
                            Toast.LENGTH_LONG).show();
                else

                    dialog.show();

            }
        };
        this.setOnCompleteListener(new OnCompleteListener() {

            @Override
            public void OnComplete() {
                // TODO Auto-generated method stub
                progressDialog.dismiss();

                handler.sendMessage(handler.obtainMessage(1, 1));

            }
        });

    }

    String chanel_name = "";
    String chanel_link = "";

    public void Parse() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {

                    URL url = new URL(src);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    InputStream stream = conn.getInputStream();
                    XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory
                            .newInstance();
                    XmlPullParser xpp = xmlFactoryObject.newPullParser();

                    xpp.setInput(stream, null);

                    String item_title = "";
                    String item_link = "";
                    String item_disription = "";
                    String item_date = "";
                    String full_text = "";
                    String author = "";
                    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                        switch (xpp.getEventType()) {

                            case XmlPullParser.START_DOCUMENT:

                                break;

                            case XmlPullParser.START_TAG:

                                if (xpp.getName().equalsIgnoreCase("item"))
                                    isItem = true;
                                if (xpp.getName().equalsIgnoreCase("title"))
                                    isTitle = true;
                                if (xpp.getName().equalsIgnoreCase("link"))
                                    isLink = true;
                                if (xpp.getName().equalsIgnoreCase("description"))
                                    isDiscription = true;
                                if (xpp.getName().equalsIgnoreCase("pubDate"))
                                    isDate = true;
                                if (xpp.getName().equalsIgnoreCase("fulltext"))
                                    isFullText = true;
                                if (xpp.getName().equalsIgnoreCase("author"))
                                    isAuthor = true;

                                break;
                            case XmlPullParser.END_TAG:

                                if (xpp.getName().equalsIgnoreCase("item")) {
                                    isItem = false;

                                    result.add(new OneRss(chanel_name, chanel_link,
                                            item_title, item_link, item_disription,
                                            item_date, full_text, author));
                                }
                                if (xpp.getName().equalsIgnoreCase("title"))
                                    isTitle = false;
                                if (xpp.getName().equalsIgnoreCase("link"))
                                    isLink = false;
                                if (xpp.getName().equalsIgnoreCase("description"))
                                    isDiscription = false;
                                if (xpp.getName().equalsIgnoreCase("pubDate"))
                                    isDate = false;
                                if (xpp.getName().equalsIgnoreCase("fulltext"))
                                    isFullText = false;
                                if (xpp.getName().equalsIgnoreCase("author"))
                                    isAuthor = false;

                                break;
                            case XmlPullParser.TEXT:

                                if (!isItem && isTitle && chanel_name.length() == 0) {
                                    chanel_name = xpp.getText();
                                }

                                if (!isItem && isLink && chanel_link.length() == 0) {
                                    chanel_link = xpp.getText();
                                }

                                if (isAuthor)
                                    author = xpp.getText();
                                if (isTitle)
                                    item_title = xpp.getText();
                                if (isLink)
                                    item_link = xpp.getText();
                                if (isDate)
                                    item_date = xpp.getText();
                                if (isDiscription)
                                    item_disription = xpp.getText();
                                if (isFullText)
                                    full_text = xpp.getText();

                                break;

                            default:
                                break;
                        }
                        xpp.next();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e(getClass().toString()
                            + "line = "
                            + Thread.currentThread().getStackTrace()[2]
                            .getLineNumber(), "catch:" + e);
                }

                completeListener.OnComplete();

            }
        }).start();
    }

    public static interface OnCompleteListener {
        public void OnComplete();
    }

    public ArrayList<OneRss> getArrayRss() {
        return result;
    }

    public class OneRss {
        String chanel_name;
        String chanel_link;

        String item_title;
        String item_link;
        String item_disription;
        String item_date;
        String full_text;
        String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getFull_text() {
            return full_text;
        }

        public void setFull_text(String full_text) {
            this.full_text = full_text;
        }

        public String getChanel_name() {
            return chanel_name;
        }

        public OneRss(String chanel_name, String chanel_link,
                      String item_title, String item_link, String item_disription,
                      String item_date, String full_text, String author) {
            super();
            this.chanel_name = chanel_name;
            this.chanel_link = chanel_link;

            this.full_text = full_text;
            this.author = author;
            this.item_title = item_title;
            this.item_link = item_link;
            this.item_disription = item_disription;
            this.item_date = item_date;
        }

        public void setChanel_name(String chanel_name) {
            this.chanel_name = chanel_name;
        }

        public String getChanel_link() {
            return chanel_link;
        }

        public void setChanel_link(String chanel_link) {
            this.chanel_link = chanel_link;
        }

        public String getItem_title() {
            return item_title;
        }

        public void setItem_title(String item_title) {
            this.item_title = item_title;
        }

        public String getItem_link() {
            return item_link;
        }

        public void setItem_link(String item_link) {
            this.item_link = item_link;
        }

        public String getItem_disription() {
            return item_disription;
        }

        public void setItem_disription(String item_disription) {
            this.item_disription = item_disription;
        }

        public String getItem_date() {
            return item_date;
        }

        public void setItem_date(String item_date) {
            this.item_date = item_date;
        }

    }

    class AdapterRSS extends ArrayAdapter<OneRss> {

        Context context;
        ArrayList<OneRss> items = new ArrayList<OneRss>();

        public AdapterRSS(Context context, int resourceId, List<OneRss> items) {
            super(context, resourceId, items);
            this.items = (ArrayList<OneRss>) items;
            this.context = context;

        }

        private class ViewHolder {

            TextView title;
            TextView description;
            TextView full_text;
            TextView date;
            TextView autor;

        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            final OneRss item = getItem(position);
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.one_rss, null);

                holder.autor = (TextView) convertView
                        .findViewById(R.id.one_rss_full_autor);
                holder.title = (TextView) convertView
                        .findViewById(R.id.one_rss_title);
                holder.full_text = (TextView) convertView
                        .findViewById(R.id.one_rss_full_text);
                holder.date = (TextView) convertView
                        .findViewById(R.id.one_rss_full_date);
                holder.description = (TextView) convertView
                        .findViewById(R.id.one_rss_description);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            if (item.getItem_title().length() > 0)
                holder.title.setText(HTMLToString(item.getItem_title()));
            else
                holder.title.setVisibility(View.GONE);

            if (item.getAuthor().length() > 0)
                holder.autor.setText(HTMLToString(item.getAuthor()));
            else
                holder.autor.setVisibility(View.GONE);

            if (item.getItem_date().length() > 0)
                holder.date.setText(HTMLToString(item.getItem_date()));
            else
                holder.date.setVisibility(View.GONE);

            if (item.getItem_disription().length() > 0)
                holder.description.setText(HTMLToString(item
                        .getItem_disription()));
            else
                holder.description.setVisibility(View.GONE);

            if (item.getFull_text().length() > 0) {
                holder.full_text.setText(HTMLToString(item.getFull_text()));

                holder.description.setTypeface(null, Typeface.BOLD_ITALIC);

                holder.full_text.setTypeface(null, Typeface.ITALIC);

            } else {
                holder.full_text.setVisibility(View.GONE);
                holder.description.setTypeface(null, Typeface.ITALIC);
            }

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String url = item.getItem_link();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }
            });

            return convertView;
        }

        public void Update() {
            this.notifyDataSetChanged();
        }

    }

    public String HTMLToString(String html) {
        return Html.fromHtml(html).toString();
    }

}
