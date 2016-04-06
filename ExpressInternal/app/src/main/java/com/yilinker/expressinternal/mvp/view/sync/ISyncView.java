package com.yilinker.expressinternal.mvp.view.sync;

import com.android.volley.Request;
import com.yilinker.expressinternal.dao.SyncDBObject;

/**
 * Created by jaybryantc on 4/6/16.
 */
public interface ISyncView {
    void showNotification();
    void updateSyncStatus(boolean syncing);
    void setProgress(int progress);
    void showResults(int success, int failed);
    void updateObject(SyncDBObject object);
    void deleteObject(SyncDBObject object);
    void stopService();
    void addToRequestQueue(Request request);
    void purgeData();
}
