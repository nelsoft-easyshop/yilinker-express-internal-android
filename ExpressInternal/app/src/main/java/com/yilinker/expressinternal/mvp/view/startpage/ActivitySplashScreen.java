package com.yilinker.expressinternal.mvp.view.startpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

/**
 * Created by Patrick on 3/9/2016.
 */
public class ActivitySplashScreen extends BaseFragmentActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        initializeViews(null);
    }

    @Override
    public void initializeViews(View parent) {

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplashScreen.this, ActivityGetStarted.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public void showLoader(boolean isShown) {

    }
}
