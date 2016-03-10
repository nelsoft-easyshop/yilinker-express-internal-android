package com.yilinker.expressinternal.mvp.view.joborderlist.list;

/**
 * Created by J.Bautista on 3/3/16.
 */
public interface ICurrentJobView extends IJobView {

    public void setTimeElapsed(String timeElapsed);
    public void setAddressText(String address);
    public void setDistanceText(String distance);
    public void setAddressLabelText(String status);
    public void setProblematicTypeText(String type);
}
