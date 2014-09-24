package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawLinesWeek2 extends View {

    public DrawLinesWeek2(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        paint.setColor(Color.BLACK);

        canvas.drawLine(width / 2, 0, width / 2, height, paint);
        canvas.drawLine(0, height / 4, width, height / 4, paint);
        canvas.drawLine(0, height / 2, width, height / 2, paint);
        canvas.drawLine(0, (height / 4) * 3, width, (height / 4) * 3, paint);
    }
}
