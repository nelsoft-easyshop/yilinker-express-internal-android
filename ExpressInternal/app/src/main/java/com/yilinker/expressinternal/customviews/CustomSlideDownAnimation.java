package com.yilinker.expressinternal.customviews;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by patrick-villanueva on 4/12/2016.
 */
public class CustomSlideDownAnimation extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View mView;
    private int mEndHeight;
    private int mType;
    private ViewGroup.LayoutParams mLayoutParams;

    public CustomSlideDownAnimation(View view, int duration, int type) {

        setDuration(duration);
        mView = view;
        mEndHeight = mView.getHeight();
        mLayoutParams = ((ViewGroup.LayoutParams) view.getLayoutParams());
        mType = type;
        if (mType == EXPAND) {
            mLayoutParams.height = 0;
        } else {
//            mLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        }

        view.setVisibility(View.VISIBLE);
    }

    public int getHeight() {
        return mView.getHeight();
    }

    public void setHeight(int height) {
        mEndHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if (mType == EXPAND) {
                mLayoutParams.height = (int) (mEndHeight * interpolatedTime);
            } else {
                mLayoutParams.height = (int) (mEndHeight * (1 - interpolatedTime));
            }
            mView.requestLayout();
        } else {
            if (mType == EXPAND) {
//                mLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mView.requestLayout();
            } else {
                mView.setVisibility(View.GONE);
            }

        }
    }
}