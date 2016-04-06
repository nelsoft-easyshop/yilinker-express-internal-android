package com.yilinker.expressinternal.mvp.view.joborderdetails;

import com.yilinker.expressinternal.mvp.view.base.RequestBaseView;

/**
 * Created by Patrick on 3/17/2016.
 */
public interface ICompleteJODetailsView extends RequestBaseView {

    public void showErrorMessage(String errorMessage);
    public void setJobOrderNumber(String jobOrderNumber);
    public void setType(String type);
    public void setOverallRating(int rating);
    public void setTimeUsed(String timeUsed);
    public void setEarning(String earning);



}
