package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.InstanceCreator;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class CashDetail implements Parcelable{

    private double cashOnHand;
    private double cashLimit;
    private List<CashHistory> cashHistory;

    public CashDetail(com.yilinker.core.model.express.internal.CashDetail object){

        this.cashOnHand = object.getCashOnHand();
        this.cashLimit = object.getCashLimit();

        List<com.yilinker.core.model.express.internal.CashHistory> list = object.getCashHistory();
        cashHistory = new ArrayList<>();
        int id = 1;
        for(com.yilinker.core.model.express.internal.CashHistory item : list){

            cashHistory.add(new CashHistory(item,id));
            id++;
        }

    }

    protected CashDetail(Parcel in) {
        cashOnHand = in.readDouble();
        cashLimit = in.readDouble();
        cashHistory = in.createTypedArrayList(CashHistory.CREATOR);
    }

    public static final Creator<CashDetail> CREATOR = new Creator<CashDetail>() {
        @Override
        public CashDetail createFromParcel(Parcel in) {
            return new CashDetail(in);
        }

        @Override
        public CashDetail[] newArray(int size) {
            return new CashDetail[size];
        }
    };

    public double getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(double cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public double getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(double cashLimit) {
        this.cashLimit = cashLimit;
    }

    public List<CashHistory> getCashHistory() {
        return cashHistory;
    }

    public void setCashHistory(List<CashHistory> cashHistory) {
        this.cashHistory = cashHistory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(cashOnHand);
        dest.writeDouble(cashLimit);
        dest.writeTypedList(cashHistory);
    }
}
