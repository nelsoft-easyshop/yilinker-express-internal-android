package com.yilinker.expressinternal.business;

import android.content.Intent;
import android.location.Location;
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
import com.yilinker.expressinternal.service.LocationService;

/**
 * Created by J.Bautista
 */
public class ApplicationClass extends BaseApplication{

    public static final int REQUEST_CODE_REFRESH_TOKEN = 2000;
    public static final String REQUEST_TAG = "requestTag";

    private Intent intentServiceLocation;

    private Location currentLocation;

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

    public Intent getIntentServiceLocation() {
        return intentServiceLocation;
    }

    public void setIntentServiceLocation(Intent intentServiceLocation) {
        this.intentServiceLocation = intentServiceLocation;
    }

    public static void startLocationService(){

        ApplicationClass appClass = (ApplicationClass)BaseApplication.getInstance();

        Intent intent = null;
        if(appClass.getIntentServiceLocation() != null) {

            stopLocationService();
        }

        intent = new Intent(appClass.getApplicationContext(), LocationService.class);

        appClass.setIntentServiceLocation(intent);

        appClass.startService(intent);

    }

    public static void stopLocationService(){

        ApplicationClass appClass = (ApplicationClass)BaseApplication.getInstance();

        if(appClass.getIntentServiceLocation() != null) {

            appClass.stopService(appClass.getIntentServiceLocation());

            appClass.setIntentServiceLocation(null);

        }

    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
