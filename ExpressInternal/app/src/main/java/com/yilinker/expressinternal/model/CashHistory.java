package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.InstanceCreator;
import com.yilinker.core.utility.DateUtility;

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

    public CashHistory(com.yilinker.core.model.express.internal.CashHistory object){

//        this.action = object.getAction();
        this.amount = object.getAmount();
//        this.jobOrderNo = object.getJobOrderNo();
        this.date = DateUtility.convertStringToDate(object.getDate(), SERVER_DATE_FORMAT);
        this.type = object.getType();
        this.waybillNo = object.getWaybillNo();

        //temp
//        this.jobOrderNo = String.valueOf(object.getJobOrderNo());

    }

    protected CashHistory(Parcel in) {
        action = in.readString();
        jobOrderNo = in.readString();
        amount = in.readDouble();
        type = in.readString();
        waybillNo = in.readString();
    }

    public static final Creator<CashHistory> CREATOR = new Creator<CashHistory>() {
        @Override
        public CashHistory createFromParcel(Parcel in) {
            return new CashHistory(in);
        }

        @Override
        public CashHistory[] newArray(int size) {
            return new CashHistory[size];
        }
    };

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
        dest.writeString(action);
        dest.writeString(jobOrderNo);
        dest.writeDouble(amount);
        dest.writeString(type);
        dest.writeString(waybillNo);
    }



}
