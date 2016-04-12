package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.view.base.RequestBaseView;

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
    void showToast(String message);
    void goToCompleteScreen(JobOrder jobOrder, boolean offline);
    void startClaimService(Package pack);
    boolean ifUpdated(String jobOrderNo);
    void showSyncStatus(boolean updated);
    void startSyncService();
    void initializeReceivers();
    void showNoInternetConnection();
    void restartMain();

}
