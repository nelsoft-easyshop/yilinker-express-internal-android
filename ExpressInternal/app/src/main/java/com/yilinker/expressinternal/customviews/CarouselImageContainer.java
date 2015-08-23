package com.yilinker.expressinternal.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by J.Bautista
 */
public class CarouselImageContainer extends LinearLayout {

    private float scale = 1.0f;

    public CarouselImageContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselImageContainer(Context context) {
        super(context);
    }

    public void setScaleBoth(float scale)
    {
        this.scale = scale;
        this.invalidate(); 	// If you want to see the scale every time you set
        // scale you need to have this line here,
        // invalidate() function will call onDraw(Canvas)
        // to redraw the view for you
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // The main mechanism to display scale animation, you can customize it
        // as your needs
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w/2, h/2);

        super.onDraw(canvas);
    }
}
