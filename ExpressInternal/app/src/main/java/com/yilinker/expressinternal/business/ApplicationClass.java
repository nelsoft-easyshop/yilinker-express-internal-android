package com.yilinker.expressinternal.business;

import com.yilinker.core.base.BaseApplication;
import com.yilinker.expressinternal.BuildConfig;

/**
 * Created by J.Bautista
 */
public class ApplicationClass extends BaseApplication{


    @Override
    public void onCreate() {
        super.onCreate();

        setDomain(BuildConfig.SERVER_URL);
    }

}
