package com.yilinker.expressinternal.controllers.joborderdetails;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;

/**
 * Created by J.Bautista
 */
public class ActivityComplete extends BaseActivity{

    public static final String ARG_JOB_ORDER = "jobOrder";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        initViews();
    }


    private void initViews(){

        //For Action Bar
        setTitle("For Pickup");
        setActionBarBackgroundColor(R.color.marigold);
    }

}
