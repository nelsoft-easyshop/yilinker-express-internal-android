package com.yilinker.expressinternal.mvp.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public abstract class BaseFragmentActivity extends AppCompatActivity implements RequestBaseView {



    @Override
    public void cancelRequests(List<String> tags) {


    }

    @Override
    public void addRequestToQueue(Request request) {



    }



}
