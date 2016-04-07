package com.yilinker.expressinternal.mvp.view.bulkcheckin;

/**
 * Created by J.Bautista on 4/5/16.
 */
public interface IBulkCheckinItemView {

    public void setWaybillNoText(String waybillNo);
    public void setStatusText(boolean isCheckedIn, boolean isProcessed);
}
