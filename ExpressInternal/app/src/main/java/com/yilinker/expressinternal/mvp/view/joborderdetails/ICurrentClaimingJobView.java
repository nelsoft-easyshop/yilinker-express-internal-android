package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.mvp.view.RequestBaseView;

/**
 * Created by jaybryantc on 4/4/16.
 */
public interface ICurrentClaimingJobView extends IJobDetailsView, RequestBaseView {

    void setDateAcceptedLabel(String dateAccepted);
    void setTimeElapsedLabel(String timeElapsed);
    void setClaimingAddressLabel(String address);
    void setContactNumberLabel(String contactNumber);
    void setItemLabel(String item);
    void showOutOfStock();
    void showClaimSuccessful();
    void showToast(String message);

}
