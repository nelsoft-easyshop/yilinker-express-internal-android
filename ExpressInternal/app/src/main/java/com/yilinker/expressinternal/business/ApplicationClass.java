package com.yilinker.expressinternal.business;

import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.OAuthentication;
import com.yilinker.expressinternal.BuildConfig;
import com.yilinker.expressinternal.constants.APIConstant;

/**
 * Created by J.Bautista
 */
public class ApplicationClass extends BaseApplication{

    public static final int REQUEST_CODE_REFRESH_TOKEN = 2000;
    public static final String REQUEST_TAG = "requestTag";

    @Override
    public void onCreate() {
        super.onCreate();

        setDomain(BuildConfig.SERVER_URL);
    }

    public static void refreshToken(ResponseHandler handler){

        OAuthentication oAuthentication = new OAuthentication();
        oAuthentication.setClientId(APIConstant.OAUTH_CLIENT_ID);
        oAuthentication.setClientSecret(APIConstant.OAUTH_CLIENT_SECRET);
        oAuthentication.setGrantType(APIConstant.OAUTH_GRANT_TYPE_REFRESHTOKEN);
        oAuthentication.setRefreshToken(ApplicationClass.getInstance().getRefreshToken());

        Request request = RiderAPI.refreshToken(REQUEST_CODE_REFRESH_TOKEN, oAuthentication,handler);

        ApplicationClass.getInstance().getRequestQueue().add(request);
    }

}
