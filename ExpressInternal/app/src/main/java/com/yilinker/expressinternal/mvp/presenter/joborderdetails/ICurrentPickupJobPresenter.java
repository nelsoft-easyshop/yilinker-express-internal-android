package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

/**
 * Created by Patrick on 3/10/2016.
 */
public interface ICurrentPickupJobPresenter {

    public void reportOutOfStock(String problemType, String notes);
    public void onPause();
    public void startTimer();
    void sync(boolean syncable, boolean isConnected);
    void onSync(boolean restartMain);
}
