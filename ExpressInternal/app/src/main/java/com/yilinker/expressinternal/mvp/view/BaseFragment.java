package com.yilinker.expressinternal.mvp.view;

import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.Request;
import com.yilinker.expressinternal.business.ApplicationClass;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public abstract class BaseFragment extends Fragment implements RequestBaseView {


    @Override
    public void addRequestToQueue(Request request) {

        ApplicationClass applicationClass = (ApplicationClass) ApplicationClass.getInstance();
        applicationClass.getRequestQueue().add(request);
    }

    @Override
    public void cancelRequests(List<String> tags) {

    }

}
