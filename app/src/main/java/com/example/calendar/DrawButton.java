package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class DrawButton extends View {

    public DrawButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Path clipPath = new Path();
        float radius = 10.0f;
        float padding = radius / 2;
        int w = this.getWidth();
        int h = this.getHeight();
        clipPath.addRoundRect(new RectF(padding, padding, w - padding, h
                - padding), radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);

    }
}
