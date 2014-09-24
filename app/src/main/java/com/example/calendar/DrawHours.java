package com.example.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class DrawHours extends View {

    boolean isAMPM = false;

    public DrawHours(Context context, boolean isAMPM) {
        super(context);
        // TODO Auto-generated constructor stub

        this.isAMPM = isAMPM;

    }

    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        paint.setColor(new MyColor().getColorBacground());
        canvas.drawRect(0, 0, width, height, paint);
        //	paint.setColor(new MyColor().getColorFont());

        float y = ((float) canvas.getHeight()) / 100;
        float x = ((float) canvas.getWidth()) / 100;
        float f = (((float) canvas.getHeight()) / 35);
        paint.setTextSize(f);
        int r = 0;
        int hours = 8;
        for (float i = 0; i < 99; i += 100 / 17) {

            r++;

            if (r > 3) {
                String text;
                if (hours < 10)
                    text = "0" + hours;
                else
                    text = String.valueOf(hours);
                if (isAMPM && Integer.parseInt(text) > 12) {
                    text = String.valueOf(Integer.parseInt(text) - 12);
                    if (Integer.parseInt(text) < 10)
                        text = "0" + text;

                }
                String text1 = text + ":00";
                int size = 35;
                while (paint.measureText(text1) > width - 10) {
                    paint.setTextSize((float) (height / size));
                    size++;
                }
                float s = paint.getTextSize();


                paint.setColor(new MyColor().getColorFont());
                if (isAMPM && hours == 8)
                    canvas.drawText("AM", 21 * x,
                            (float) ((i - 2) * y + 0.6 * y), paint);

                if (isAMPM && hours == 12)
                    canvas.drawText("PM", 21 * x,
                            (float) ((i - 2) * y + 0.6 * y), paint);

                canvas.drawText(text, 20 * x, (float) (i * y + 0.6 * y), paint);
                float ots = paint.measureText(text);
                paint.setTextSize((float) (paint.getTextSize() / 1.5));
                canvas.drawText("00", 20 * x + ots, (float) (i * y + 0.1 * y),
                        paint);
                hours++;
                paint.setTextSize(s);
            }
        }

    }
}
