package com.yilinker.expressinternal.mvp.presenter.joborderdetails;

/**
 * Created by jaybryantc on 4/4/16.
 */
public interface ICurrentClaimingJobPresenter {

    void startTimer();
    void stopTimer();
    void claimJobOrder();
    void reportOutOfStock();

}
