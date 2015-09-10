package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

/**
 * Created by J.Bautista
 */
public class CashHistory implements Parcelable {

    private String action;
    private String jobOrderNo;
    private double amount;

    public CashHistory(com.yilinker.core.model.express.internal.CashHistory object){

        this.action = object.getAction();
        this.amount = object.getAmount();

        //temp
        this.jobOrderNo = String.valueOf(object.getJobOrderNo());

    }

    protected CashHistory(Parcel in) {
        action = in.readString();
        jobOrderNo = in.readString();
        amount = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(jobOrderNo);
        dest.writeDouble(amount);
    }



}
