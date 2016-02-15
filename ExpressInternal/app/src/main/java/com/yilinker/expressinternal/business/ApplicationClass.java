package com.yilinker.expressinternal.business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.helper.FileHelper;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.OAuthentication;
import com.yilinker.core.model.express.internal.JobOrder;
import com.yilinker.expressinternal.BuildConfig;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.service.LocationService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by J.Bautista
 */
public class ApplicationClass extends BaseApplication {

    public static final int REQUEST_CODE_REFRESH_TOKEN = 2000;
    public static final String REQUEST_TAG = "requestTag";
    public static final String CURRENT_LIST = "currentList.txt";
    public static final String SYNC_ITEMS = "hasSyncItems";

    private Intent intentServiceLocation;

    private Rider rider;

    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        setDomain(BuildConfig.SERVER_URL);
    }

    public static void refreshToken(ResponseHandler handler) {

        ApplicationClass applicationClass = (ApplicationClass) BaseApplication.getInstance();

        OAuthentication oAuthentication = new OAuthentication();
        oAuthentication.setClientId(applicationClass.getString(R.string.client_id));
        oAuthentication.setClientSecret(applicationClass.getString(R.string.client_secret));
        oAuthentication.setGrantType(APIConstant.OAUTH_GRANT_TYPE_REFRESHTOKEN);
        oAuthentication.setRefreshToken(applicationClass.getRefreshToken());

        Request request = RiderAPI.refreshToken(REQUEST_CODE_REFRESH_TOKEN, oAuthentication, handler);

        applicationClass.getRequestQueue().add(request);
    }

    public Intent getIntentServiceLocation() {
        return intentServiceLocation;
    }

    public void setIntentServiceLocation(Intent intentServiceLocation) {
        this.intentServiceLocation = intentServiceLocation;
    }

    public static void startLocationService() {

        ApplicationClass appClass = (ApplicationClass) BaseApplication.getInstance();

        Intent intent = null;
        if (appClass.getIntentServiceLocation() != null) {

            stopLocationService();
        }

        intent = new Intent(appClass.getApplicationContext(), LocationService.class);

        appClass.setIntentServiceLocation(intent);

        appClass.startService(intent);

    }

    public static void stopLocationService() {

        ApplicationClass appClass = (ApplicationClass) BaseApplication.getInstance();

        if (appClass.getIntentServiceLocation() != null) {

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

    public Rider getRider() {

        return this.rider;

    }

    public void setRider(Rider rider) {

        this.rider = rider;
    }

    public static void saveLocalCurrentListData(Context context, Object object) {

        String jsonString = new Gson().toJson(object);

        try {

            FileHelper.writeFile(context, String.format("%s/%s",
                    context.getFilesDir(), ApplicationClass.CURRENT_LIST),
                    jsonString);

        } catch (IOException e) {

        }
    }

    // Get data from local storage
    public static List<JobOrder> getLocalData(Context context) {

        String jsonStr = null;
        List<JobOrder> obj = new ArrayList<>();

        try {

            jsonStr = FileHelper.readFile(context, String.format(
                    "%s/%s", context.getFilesDir(),
                    ApplicationClass.CURRENT_LIST));

            if (jsonStr != null) {

                Type listType = new TypeToken<ArrayList<JobOrder>>() {
                }.getType();

                obj = new Gson().fromJson(jsonStr, listType);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;

    }


//    public void setHasItemsForSyncing(boolean forSyncing) {
//
//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());
//
//        SharedPreferences.Editor editor = pref.edit();
//
//        if(forSyncing) {
//            editor.putBoolean(SYNC_ITEMS, true);
//        } else {
//            editor.remove(SYNC_ITEMS);
//        }
//
//        editor.commit();
//
//    }

    public boolean hasItemsForSyncing() {

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());
//
//        return pref.contains(SYNC_ITEMS);

        Realm realm = Realm.getInstance(this);

        RealmQuery<SyncDBObject> query = realm.where(SyncDBObject.class);

        query.equalTo("isSync", false);


//        SyncDBTransaction dbTransaction = new SyncDBTransaction(this);

//        int count = dbTransaction.getAll(SyncDBObject.class).size();

        int count = (int)query.count();

        return count > 0;

    }
}
