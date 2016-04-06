package com.yilinker.expressinternal.mvp.view.startpage;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.view.base.BaseFragmentActivity;

/**
 * Created by Patrick on 3/9/2016.
 */
public class ActivityGetStarted extends BaseFragmentActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        initializeViews(null);
    }

    @Override
    public void initializeViews(View parent) {

        Button btnGetStarted = (Button) findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(this);

        setViewPager();


    }

    private void setViewPager(){

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerSlides);
        CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.pagerIndicator);

        TypedArray images = getResources().obtainTypedArray(R.array.get_started_images);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getApplicationContext(),images);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setClipChildren(false);
        pageIndicator.setViewPager(viewPager);
    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGetStarted:

                Intent goToLogIn = new Intent(this, com.yilinker.expressinternal.controllers.login.ActivityLogin.class);
                startActivity(goToLogIn);
                break;

            default:
                break;
        }
    }
}
