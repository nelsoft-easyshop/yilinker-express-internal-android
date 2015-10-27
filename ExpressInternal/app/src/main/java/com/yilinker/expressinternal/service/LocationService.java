package com.yilinker.expressinternal.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.business.ApplicationClass;

/**
 * Created by J.Bautista
 */
public class LocationService extends Service implements ResponseHandler{

    private static final String TAG = "LocationService";

    private static final String REQUEST_TAG_LOCATION = "location";

    private static final int REQUEST_SEND_LOCATION = 5000;

    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 0.0f;

    private LocationManager mLocationManager = null;

    private LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)};

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

    }

    @Override
    public void onFailed(int requestCode, String message) {

    }


    private class LocationListener implements android.location.LocationListener{

        Location mLastLocation;

        public LocationListener(String provider)
        {

            mLastLocation = new Location(provider);
        }
        @Override
        public void onLocationChanged(Location location)
        {

            mLastLocation.set(location);

            if(location != null) {

                ((ApplicationClass) BaseApplication.getInstance()).setCurrentLocation(location);

//                Toast.makeText(getApplicationContext(), "Location Changed", Toast.LENGTH_LONG).show();

                requestSendLocation(location);
            }
        }
        @Override
        public void onProviderDisabled(String provider)
        {

        }
        @Override
        public void onProviderEnabled(String provider)
        {

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }

    private void requestSendLocation(Location location){


        Request request = RiderAPI.sendLocation(REQUEST_SEND_LOCATION, location.getLatitude(), location.getLongitude(), this);
        request.setTag(REQUEST_TAG_LOCATION);

        RequestQueue requestQueue = ApplicationClass.getInstance().getRequestQueue();

        requestQueue.add(request);

    }
}
