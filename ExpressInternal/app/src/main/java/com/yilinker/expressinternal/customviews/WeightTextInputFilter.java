package com.yilinker.expressinternal.customviews;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class WeightTextInputFilter implements InputFilter{

    private double maximumInput;
    private OnLimitReachedListener listener;

    public interface OnLimitReachedListener{

        public void onLimitReachedListener();
    }

    public WeightTextInputFilter(double maximumInput, OnLimitReachedListener listener){

        this.maximumInput = maximumInput;
        this.listener = listener;
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String newText = dest.toString() + source.toString();

        if(newText.equals(".")){
            return "0.";
        }

        try {
            double number = Double.parseDouble(newText);

            if (hasReachedLimit(number)) {

                listener.onLimitReachedListener();
                return "";
            }
        }
        catch (NumberFormatException e){

            return null;
        }

        return null;
    }

    private boolean hasReachedLimit(double input){

        return input >= maximumInput;
    }
}
