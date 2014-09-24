//package com.example.calendar;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.Log;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.perm.kate.api.Auth;
//
//public class shareVK {
//    String name;
//    String picture;
//    WebView webview;
//    String link;
//    Context context;
//    public final String ID = "4323506";
//    public final String KEY = "MA16QJ4F71vMbsLmKVAR";
//    ViewGroup rooGroup;
//
//    public shareVK(Context context, ViewGroup rooGroup, String name,
//                   String picture, String link) {
//        this.context = context;
//        this.name = name;
//        this.picture = picture;
//        this.rooGroup = rooGroup;
//        this.link = link;
//        ShowWebView();
//    }
//
//    private void parseUrl(String url) {
//        try {
//            if (url == null)
//                return;
//            if (url.startsWith(Auth.redirect_url)) {
//
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
//                                String url = "https://api.vk.com/method/wall.post?owner_id="
//                                        + USER_ID
//                                        + "&message="
//                                        + name
//                                        + " "
//                                        + link
//                                        + "&from_group=1&attachments="
//                                        + picture + "&access_token=" + TOKEN;
//
//                                Log.e(getClass().toString(), "url = " + url);
//
//                                webview.loadUrl(url);
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
//
//                    rooGroup.removeView(webview);
//
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
//        }
//    }
//
//    public void ShowWebView() {
//
//        webview = new WebView(context);
//        String url = Auth.getUrl(ID, Auth.getSettings());
//        webview.setWebViewClient(new VkontakteWebViewClient());
//        webview.loadUrl(url);
//
//        rooGroup.addView(webview);
//
//    }
//}
