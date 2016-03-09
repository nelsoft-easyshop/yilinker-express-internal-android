package com.yilinker.expressinternal.mvp.view.joborderlist.list;

/**
 * Created by J.Bautista on 3/3/16.
 */
public interface IOpenJobView extends IJobView {

    public void setDateCreated(String dateCreated);
    public void setFromAddress(String fromAddress);
    public void setToAddress(String toAddress);
    public void setDistance(String distance);
}
