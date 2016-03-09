package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.InstanceCreator;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by J.Bautista
 */
public class CashHistory implements Parcelable {

    private static final String SERVER_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private String action;
    private String jobOrderNo;
    private double amount;
    private Date date;
    private String type;
    private String waybillNo;
    private double runningTotal;
    private int id;

    public CashHistory(com.yilinker.core.model.express.internal.CashHistory object, int id){

//        this.action = object.getAction();
        this.amount = object.getAmount();
//        this.jobOrderNo = object.getJobOrderNo();
        this.date = DateUtility.convertStringToDate(object.getDate(), SERVER_DATE_FORMAT);
        this.type = object.getType();
        this.waybillNo = object.getWaybillNo();
        this.runningTotal = object.getRunningTotal();
        this.id = id;
        //temp
//        this.jobOrderNo = String.valueOf(object.getJobOrderNo());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(double runningTotal) {
        this.runningTotal = runningTotal;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getJobOrderNo() {
        return jobOrderNo;
    }

    public void setJobOrderNo(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.jobOrderNo);
        dest.writeDouble(this.amount);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeString(this.type);
        dest.writeString(this.waybillNo);
        dest.writeDouble(this.runningTotal);
        dest.writeInt(this.id);
    }

    protected CashHistory(Parcel in) {
        this.action = in.readString();
        this.jobOrderNo = in.readString();
        this.amount = in.readDouble();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.type = in.readString();
        this.waybillNo = in.readString();
        this.runningTotal = in.readDouble();
        this.id = in.readInt();
    }

    public static final Creator<CashHistory> CREATOR = new Creator<CashHistory>() {
        public CashHistory createFromParcel(Parcel source) {
            return new CashHistory(source);
        }

        public CashHistory[] newArray(int size) {
            return new CashHistory[size];
        }
    };
}
