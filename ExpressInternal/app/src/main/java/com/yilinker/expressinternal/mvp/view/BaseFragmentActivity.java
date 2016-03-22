package com.yilinker.expressinternal.mvp.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public abstract class BaseFragmentActivity extends AppCompatActivity implements RequestBaseView {


    @Override
    public void cancelRequests(List<String> tags) {

        ApplicationClass applicationClass = (ApplicationClass) ApplicationClass.getInstance();
        RequestQueue queue = applicationClass.getRequestQueue();

        for(String tag : tags){

            queue.cancelAll(tag);
        }

    }

    @Override
    public void addRequestToQueue(Request request) {

        RequestQueue requestQueue  = ApplicationClass.getInstance().getRequestQueue();
        requestQueue.add(request);


    }

    protected void replaceFragment(int containerId, Fragment fragment){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(containerId, fragment);

        transaction.commit();
    }


}
