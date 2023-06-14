package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by Bamdad on 3/27/2019.
 */

public class SeparatorTextView extends androidx.appcompat.widget.AppCompatTextView {
    public SeparatorTextView(Context context) {
        super(context);
    }

    public SeparatorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeparatorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // we use the default paint,
        // our line will be the same color as the text.
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        paint.setAlpha(100);
        int top = getHeight()/2; // start at the vertical centre
        // of the textview
        int left = 0; //start at the left margin
        int right = getWidth(); // we draw all the way to the right
        int bottom = top + 2; // we want the line to be 2 pixel thick
        top = (getHeight() / 2)-4;
        left = 0;
        right = getWidth()+4;
        bottom = top + 4;
        canvas.drawRect(left, top, right, bottom, paint);
    }
}
