package com.example.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DrawClock extends View {

    public DrawClock(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        float x = canvas.getWidth() / 100;
        float y = canvas.getHeight() / 100;
        float X;
        float Y;

        paint.setColor(Color.BLACK);

        ContentValues XY = getXY(50, 0, 50, 50, getAlphaSec());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        float ax = X * x;
        float ay = Y * y;
        XY = getXY(49, 50, 50, 50, -getAlphaSec());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        float bx = X * x;
        float by = Y * y;
        XY = getXY(51, 50, 50, 50, -getAlphaSec());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        float cx = X * x;
        float cy = Y * y;
        XY = getXY(50, 70, 50, 50, getAlphaSec());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        float dx = X * x;
        float dy = Y * y;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(ax, ay);
        path.lineTo(bx, by);
        path.lineTo(dx, dy);
        path.lineTo(cx, cy);
        path.lineTo(ax, ay);
        canvas.drawPath(path, paint);

        XY = getXY(50, 10, 50, 50, getAlphaMin());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        ax = X * x;
        ay = Y * y;
        XY = getXY(48, 50, 50, 50, -getAlphaMin());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        bx = X * x;
        by = Y * y;
        XY = getXY(52, 50, 50, 50, -getAlphaMin());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        cx = X * x;
        cy = Y * y;
        XY = getXY(50, 65, 50, 50, getAlphaMin());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        dx = X * x;
        dy = Y * y;
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(ax, ay);
        path.lineTo(bx, by);
        path.lineTo(dx, dy);
        path.lineTo(cx, cy);
        path.lineTo(ax, ay);
        canvas.drawPath(path, paint);

        XY = getXY(50, 20, 50, 50, getAlphaHour());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        ax = X * x;
        ay = Y * y;
        XY = getXY(48, 50, 50, 50, -getAlphaHour());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        bx = X * x;
        by = Y * y;
        XY = getXY(52, 50, 50, 50, -getAlphaHour());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        cx = X * x;
        cy = Y * y;
        XY = getXY(50, 60, 50, 50, getAlphaHour());
        X = XY.getAsFloat("X");
        Y = XY.getAsFloat("Y");
        dx = X * x;
        dy = Y * y;
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(ax, ay);
        path.lineTo(bx, by);
        path.lineTo(dx, dy);
        path.lineTo(cx, cy);
        path.lineTo(ax, ay);
        canvas.drawPath(path, paint);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(50 * x, 50 * y, 5, paint);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                invalidate();
            }
        }, 1000);

    }

    public ContentValues getXY(float Xo, float Yo, float X, float Y,
                               float alpha_gr) {
        alpha_gr -= 90;
        ContentValues res = new ContentValues();
        float alpha_in_radians = (float) ((alpha_gr * Math.PI) / 180);

        float Rx = Xo - X;
        float Ry = Yo - Y;
        double c = Math.cos(alpha_in_radians);
        double s = Math.sin(alpha_in_radians);

        float Yres = (float) ((X + Rx * c) - (Ry * s));
        float Xres = (float) ((Y + Rx * s) - (Ry * c));

        res.put("X", Xres);
        res.put("Y", Yres);

        return res;

    }

    public int getAlphaSec() {
        return getNowSecond() * 6;

    }

    public float getAlphaMin() {
        return getNowMinute() * 6;
    }

    public float getAlphaHour() {
        return (getNowHour() + getNowMinute() / 60) * 30;
    }

    public static int getNowHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        int h = Integer.parseInt(sdf.format(new Date()));
        if (h > 12)
            h = h - 12;

        return h;
    }

    public static float getNowMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    public static int getNowSecond() {
        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        return Integer.parseInt(sdf.format(new Date()));
    }
}
