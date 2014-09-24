package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class DrawNameDay extends View {

    String[] names;

    public DrawNameDay(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

        this.names = MainActivity.getInstance().language.NAMES_DAYS_WEEK_SOKR;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        int height = canvas.getHeight();
        int width = canvas.getWidth();

//		paint.setColor(new MyColor().getColorBacground());
//		canvas.drawRect(0, 0, width, height, paint);
//		paint.setColor(new MyColor().getColorFont());

        float y = ((float) canvas.getHeight()) / 100;
        float x = ((float) canvas.getWidth()) / 100;
        float f = (((float) canvas.getHeight()) / 2);
        paint.setTextSize(f);
        int day = 0;
        for (float i = 0; i < 99; i += 100 / 7) {
            if (day < 7)
                canvas.drawText(names[day], (float) (i + 100 / 16.5) * x, 50 * y + f / 2, paint);
            day++;
        }

    }
}
