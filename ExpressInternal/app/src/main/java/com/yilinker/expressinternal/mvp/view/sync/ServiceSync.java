package com.yilinker.expressinternal.mvp.view.sync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.mvp.presenter.sync.SyncPresenter;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

/**
 * Created by jaybryantc on 4/6/16.
 */
public class ServiceSync extends Service implements ISyncView {

    private static final int NOTIFICATION_ID = 5984;

    public static final String SYNC = "sync";

    private ApplicationClass appClass;

    private SyncDBTransaction transaction;

    private Realm realm;

    private RequestQueue requestQueue;

    private NotificationManager manager;
    private Notification.Builder builder;

    private SyncPresenter presenter;

    private List<SyncDBObject> objects;

    @Override
    public void onCreate() {
        super.onCreate();

        this.initializeObjects();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setupNotification();

        objects = transaction.getAll(SyncDBObject.class);

        if (objects == null) {

            stopSelf();

            manager.cancelAll();


        } else {

            presenter.setModel(objects);
            presenter.bindView(this);
            presenter.startRequest(objects.get(0), 0);

        }

        return START_STICKY;
    }

    private void initializeObjects() {

        appClass = (ApplicationClass) getApplicationContext();

        realm = Realm.getInstance(this);

        transaction = new SyncDBTransaction(this);

        requestQueue = Volley.newRequestQueue(this);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        presenter = new SyncPresenter();

    }

    private void setupNotification() {

        builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setSmallIcon(R.drawable.ic_sync);
        builder.setContentTitle("Syncing data");
        builder.setProgress(100, 0, true);
        manager.notify(NOTIFICATION_ID, builder.build());

    }

    @Override
    public void updateSyncStatus(boolean syncing) {

        if (syncing != appClass.isSyncing(this)) {

            appClass.setIsSyncing(syncing);
            sendBroadcast(new Intent(SYNC));

        }

    }

    @Override
    public void setProgress(int progress) {

//        builder.setProgress(100, progress, false);
        builder.setProgress(objects.size(), progress, false);
        builder.setContentText((progress * 100 / objects.size()) + "%");
        manager.notify(NOTIFICATION_ID, builder.build());

    }


    @Override
    public void showResults(int success, int failed) {

        builder.setContentTitle("Syncing is done.");
        builder.setContentText("Success : " + success + " " + " Failed : " + failed);
        builder.setOngoing(false);
        builder.setAutoCancel(true);
        manager.notify(NOTIFICATION_ID, builder.build());

    }

    @Override
    public void showNotification() {

        manager.notify(NOTIFICATION_ID, builder.build());

    }

    @Override
    public void updateObject(SyncDBObject object) {

        realm.beginTransaction();
        transaction.update(object);
        realm.commitTransaction();

    }

    @Override
    public void deleteObject(SyncDBObject object) {

        realm.beginTransaction();
        transaction.delete(object);
        realm.commitTransaction();

    }

    @Override
    public void stopService() {

        stopSelf();

    }

    @Override
    public void purgeData(){

        HashMap<String, Object> mapQuery = new HashMap<>();
        mapQuery.put("isSync", true);

//        transaction.deleteAll(SyncDBObject.class);

        transaction.deleteAll(SyncDBObject.class, mapQuery);

        //Clean cache. Remove images
        if(!appClass.hasItemsForSyncing())
            ImageUtility.clearCachedImages(this);

    }

    @Override
    public void addToRequestQueue(Request request) {

        requestQueue.add(request);

    }

}
