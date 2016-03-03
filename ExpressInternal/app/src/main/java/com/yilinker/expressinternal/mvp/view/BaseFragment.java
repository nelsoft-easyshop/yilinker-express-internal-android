package com.yilinker.expressinternal.mvp.view;

import android.app.Fragment;

import com.android.volley.Request;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public abstract class BaseFragment extends Fragment implements RequestBaseView {


    @Override
    public void addRequestToQueue(Request request) {

    }

    @Override
    public void cancelRequests(List<String> tags) {

    }

}
