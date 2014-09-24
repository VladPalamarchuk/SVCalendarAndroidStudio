//package com.example.calendar;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
//
//import com.perm.kate.api.Auth;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class VK {
//    public final String ID = "4323506";
//    public final String KEY = "MA16QJ4F71vMbsLmKVAR";
//    WebView webview;
//    Context context;
//    ViewGroup rootView;
//    ArrayList<OneFriend> result;
//    RelativeLayout rel;
//    Handler isFriendsParsed;
//    AlertDialog dialog;
//    boolean isCloseRoot;
//
//    public VK(Context context, final ViewGroup rootView,
//              ArrayList<OneFriend> result, final boolean isCloseRoot, final AlertDialog dialog) {
//        this.result = result;
//        this.context = context;
//        this.rootView = rootView;
//        this.dialog = dialog;
//        ShowWebView();
//        this.isCloseRoot = isCloseRoot;
//        isFriendsParsed = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                rootView.removeView(rel);
//                if (isCloseRoot) {
//                    dialog.dismiss();
//                }
//            }
//        };
//    }
//
//    public void ShowWebView() {
//        rel = new RelativeLayout(context);
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT);
//        rel.setLayoutParams(params);
//        webview = new WebView(context);
//        webview.setLayoutParams(params);
//        webview.setWebViewClient(new VkontakteWebViewClient());
//
//        rel.addView(webview);
//        String url = Auth.getUrl(ID, Auth.getSettings());
//        webview.loadUrl(url);
//        rootView.addView(rel);
//
//    }
//
//    class VkontakteWebViewClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//
//        }
//
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//
//            parseUrl(url);
//
//            if (url.contains("access_denied")) {
//                rootView.removeView(rel);
//
//                if (isCloseRoot)
//                    dialog.dismiss();
//            }
//
//        }
//    }
//
//    private void parseUrl(String url) {
//        try {
//            if (url == null)
//                return;
//            if (url.startsWith(Auth.redirect_url)) {
//                if (!url.contains("error=") && url.contains("access")) {
//                    String[] auth = Auth.parseRedirectUrl(url);
//                    final String TOKEN = auth[0];
//                    final String USER_ID = auth[1];
//
//                    new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//
//                            try {
//
//                                HttpClient client = new DefaultHttpClient();
//                                HttpPost httpPost = new HttpPost(
//                                        "https://api.vk.com/method/users.get?user_ids="
//                                                + USER_ID + "&fields=photo");
//                                HttpResponse response = client
//                                        .execute(httpPost);
//                                String resp = MainActivity.getInstance()
//                                        .convertStreamToString(response);
//
//                                JSONObject jsonObject = new JSONObject(resp);
//                                JSONArray jArray = jsonObject
//                                        .getJSONArray("response");
//
//                                for (int i = 0; i < jArray.length(); i++) {
//
//                                    JSONObject locaObject = jArray
//                                            .getJSONObject(i);
//                                    String first_name = locaObject
//                                            .getString("first_name");
//                                    String last_name = locaObject
//                                            .getString("last_name");
//                                    final String photo = locaObject
//                                            .getString("photo");
//
//                                    MainActivity.getInstance().shared
//                                            .edit()
//                                            .putString(
//                                                    MainActivity.SHARED_VK_FIRST_NAME,
//                                                    first_name).commit();
//                                    MainActivity.getInstance().shared
//                                            .edit()
//                                            .putString(
//                                                    MainActivity.SHARED_VK_LAST_NAME,
//                                                    last_name).commit();
//                                    MainActivity.getInstance().shared
//                                            .edit()
//                                            .putString(
//                                                    MainActivity.SHARED_VK_PHOTO,
//                                                    photo).commit();
//
//                                    new FileLoader().loadFile(photo,
//                                            MainActivity.getInstance()
//                                                    .getFileName(photo));
//
//                                }
//
//                            } catch (Exception e) {
//                                // TODO: handle exception
//                                Log.e(getClass().toString()
//                                        + "line = "
//                                        + Thread.currentThread()
//                                        .getStackTrace()[2]
//                                        .getLineNumber(), "catch:" + e);
//                            }
//
//                        }
//                    }).start();
//                    rootView.removeView(rel);
//
//                    if (isCloseRoot) {
//                        dialog.dismiss();
//                    }
//                    new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            Looper.prepare();
//                            // TODO Auto-generated method stub
//                            result = null;
//                            result = new ParseFriendsVkontakte(
//                                    "https://api.vk.com/method/friends.get?user_id="
//                                            + USER_ID
//                                            + "&fields=first_name,last_name,photo_200_orig,contacts,bdate,city"
//                                            + "&access_token=" + TOKEN,
//                                    OneFriend.VK_TYPE).Parse();
//
//                            while (result == null) {
//
//                                try {
//                                    Thread.sleep(200);
//                                } catch (Exception e) {
//
//                                }
//                            }
//                            AddBirthday addBirthday = new AddBirthday(result);
//                            addBirthday.Add();
//
//                        }
//                    }).start();
//
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
