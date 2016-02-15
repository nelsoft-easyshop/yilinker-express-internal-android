package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rlcoronado on 08/01/2016.
 */
public class ShippingFee implements Parcelable {

    private String totalShippingFee;

    public String getTotalShippingFee() {
        return totalShippingFee;
    }

    public void setTotalShippingFee(String totalShippingFee) {
        this.totalShippingFee = totalShippingFee;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalShippingFee);
    }

    public ShippingFee() {
    }

    protected ShippingFee(Parcel in) {
        this.totalShippingFee = in.readString();
    }

    public static final Parcelable.Creator<ShippingFee> CREATOR = new Parcelable.Creator<ShippingFee>() {
        public ShippingFee createFromParcel(Parcel source) {
            return new ShippingFee(source);
        }

        public ShippingFee[] newArray(int size) {
            return new ShippingFee[size];
        }
    };
}
