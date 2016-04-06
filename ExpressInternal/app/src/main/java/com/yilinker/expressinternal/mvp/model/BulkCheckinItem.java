package com.yilinker.expressinternal.mvp.model;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class BulkCheckinItem {

    private int id;
    private String waybillNo;
    private boolean isCheckedIn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setIsCheckedIn(boolean isCheckedIn) {
        this.isCheckedIn = isCheckedIn;
    }
}
