package com.yilinker.expressinternal.mvp.view.startpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityCompleteJODetails;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;

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

                ApplicationClass app = (ApplicationClass)ApplicationClass.getInstance();

                if(app.isLoggedIn()){
                    goToActivityMain();

                }  else {
                    goToActivityStarted();

                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void goToActivityStarted(){
        Intent intent = new Intent(ActivitySplashScreen.this, ActivityGetStarted.class);
        startActivity(intent);
        finish();

    }


    private void goToActivityMain(){
        Intent intent = new Intent(ActivitySplashScreen.this, ActivityMain.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void showLoader(boolean isShown) {

    }
}
