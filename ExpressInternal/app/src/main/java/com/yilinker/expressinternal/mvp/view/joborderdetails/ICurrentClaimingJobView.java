package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;
import com.yilinker.expressinternal.mvp.model.Package;

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
    void showOutdated();
    void sync();

}
