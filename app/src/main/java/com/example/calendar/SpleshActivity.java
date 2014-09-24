//package com.example.calendar;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.Window;
//import android.view.WindowManager;
//
//public class SpleshActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splesh);
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                finish();
//            }
//        }, 2000);
//    }
//}
//