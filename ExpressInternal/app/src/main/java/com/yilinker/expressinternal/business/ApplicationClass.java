package com.yilinker.expressinternal.business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

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
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.BuildConfig;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.mvp.model.Languages;
import com.yilinker.expressinternal.service.LocationService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private static final String CURRENT_RIDER = "rider";
    private static final String CURRENT_LOCALE = "locale";
    private static final String CURRENT_LOCALE_ID = "localeId";

    private static final String KEY_FILTER_AREA = "filterBy";
    private static final String REMAINING_TIME =  "remaining-time";
    private static final String KEY_MOBILE_NUMBER = "registration-mobile-number";

    private Intent intentServiceLocation;

    private Rider rider;

    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        setDomain(BuildConfig.SERVER_URL);
        setLocale();
    }

    private void setLocale() {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        setDomainLocale(pref.getString(CURRENT_LOCALE, "en"));
        setAppLocale(pref.getString(CURRENT_LOCALE, "en"));
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

//        return this.rider;

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        String json = pref.getString(CURRENT_RIDER, "");

        return gson.fromJson(json, Rider.class);

    }

    public void setRider(Rider rider) {

//        this.rider = rider;

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rider);
        editor.putString(CURRENT_RIDER, json);
        editor.apply();

    }

    public void logoutRider() {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = pref.edit();

        editor.remove(CURRENT_RIDER);
        deleteTokens();

    }

    public int getLanguageId() {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        return pref.getInt(CURRENT_LOCALE_ID, 0);
    }

    public String getCurrentLocale() {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        return pref.getString(CURRENT_LOCALE, "en");
    }


    public void setLanguage(Object lang) {

        Languages locale = (Languages) lang;

        setDomainLocale(locale.getLocale());
        setAppLocale(locale.getLocale());
        saveLanguage(locale);

    }

    private void saveLanguage(Languages locale) {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CURRENT_LOCALE, locale.getLocale());
        editor.putInt(CURRENT_LOCALE_ID, locale.getId());
        editor.apply();

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

        Realm realm = Realm.getInstance(this);
        RealmQuery<SyncDBObject> query = realm.where(SyncDBObject.class);

        query.equalTo("isSync", false);

        int count = (int) query.count();

        return count > 0;

    }

    private void setAppLocale(String locale) {

        Resources res = this.getResources();
        Configuration cfg = getBaseContext().getResources().getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        cfg.locale = new Locale(locale);
        res.updateConfiguration(cfg, dm);

    }

    public void setDomainLocale(String locale) {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        if (!pref.contains(CURRENT_LOCALE)) {
            setDomain(BuildConfig.SERVER_URL.replace("en", locale));
        } else {
            setDomain(BuildConfig.SERVER_URL.replace(pref.getString(CURRENT_LOCALE,""),locale));
        }

    }

    public String getSyncDictionary() {

        String dictionary = null;

        SyncDBTransaction dbTransaction = new SyncDBTransaction(this);;
        List<SyncDBObject> requestsList = dbTransaction.getAll(SyncDBObject.class);

        if (requestsList.size() > 0) {

            StringBuilder syncBuilder = new StringBuilder();
            syncBuilder.append("|");

            for (SyncDBObject object : requestsList) {

                if (!object.isSync()) {

                    syncBuilder.append(object.getId());
                    syncBuilder.append("|");
                }
            }

            dictionary = syncBuilder.toString();

        }

        return dictionary;

    }

    //For filter type
    public void resetFilterType(){

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        setFilterType(!isFilterByArea());

    }

    public boolean isFilterByArea(){

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        return pref.getBoolean(KEY_FILTER_AREA, true);

    }

    private void setFilterType(boolean isFilteredByArea){

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(KEY_FILTER_AREA, isFilteredByArea);

        editor.commit();
    }

    public void saveRemainingTime(String remainingTime){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REMAINING_TIME, remainingTime);
        editor.commit();

    }

    public String getRemainingTime(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(REMAINING_TIME,null);

    }


    public void saveMobileNumber(String remainingTime){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_MOBILE_NUMBER, remainingTime);
        editor.commit();

    }

    public String getMobileNumber(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(KEY_MOBILE_NUMBER,null);

    }

}
