package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J.Bautista
 */
public class BluetoothPrinter implements Parcelable{

    private String name;
    private String address;

    public BluetoothPrinter(){


    }

    protected BluetoothPrinter(Parcel in) {
        name = in.readString();
        address = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
    }

    public static final Creator<BluetoothPrinter> CREATOR = new Creator<BluetoothPrinter>() {
        @Override
        public BluetoothPrinter createFromParcel(Parcel in) {
            return new BluetoothPrinter(in);
        }

        @Override
        public BluetoothPrinter[] newArray(int size) {
            return new BluetoothPrinter[size];
        }
    };

}
