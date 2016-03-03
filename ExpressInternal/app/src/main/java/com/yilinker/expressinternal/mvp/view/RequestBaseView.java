package com.yilinker.expressinternal.mvp.view;

import com.android.volley.Request;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 *
 * Base class for views that needs to perform API requests
 */
public interface RequestBaseView {

    public void initializeViews();
    public void addRequestToQueue(Request request);
    public void cancelRequests(List<String> tags);
}
