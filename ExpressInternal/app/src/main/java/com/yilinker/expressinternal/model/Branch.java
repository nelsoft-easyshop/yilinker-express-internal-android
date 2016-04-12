package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yilinker.core.model.express.internal.Location;

/**
 * Created by J.Bautista on 4/11/16.
 */
public class Branch implements Parcelable {

    private String name;
    private double latitude;
    private double longitude;
    private int forDropoffCount;
    private int forClaimingCount;

    public Branch(){

    }

    public Branch(com.yilinker.core.model.express.internal.Branch object){

        this.name = object.getName();

        Location location = object.getLocation();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    protected Branch(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        forDropoffCount = in.readInt();
        forClaimingCount = in.readInt();
    }

    public static final Creator<Branch> CREATOR = new Creator<Branch>() {
        @Override
        public Branch createFromParcel(Parcel in) {
            return new Branch(in);
        }

        @Override
        public Branch[] newArray(int size) {
            return new Branch[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getForDropoffCount() {
        return forDropoffCount;
    }

    public void setForDropoffCount(int forDropoffCount) {
        this.forDropoffCount = forDropoffCount;
    }

    public int getForClaimingCount() {
        return forClaimingCount;
    }

    public void setForClaimingCount(int forClaimingCount) {
        this.forClaimingCount = forClaimingCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(forDropoffCount);
        dest.writeInt(forClaimingCount);
    }
}
