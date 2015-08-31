package com.yilinker.expressinternal.business;

import com.android.volley.toolbox.ImageLoader;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.expressinternal.BuildConfig;

/**
 * Created by J.Bautista
 */
public class ApplicationClass extends BaseApplication{

    public static final String REQUEST_TAG = "requestTag";


    @Override
    public void onCreate() {
        super.onCreate();

        setDomain(BuildConfig.SERVER_URL);
    }


}
