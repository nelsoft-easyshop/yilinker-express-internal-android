package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yilinker.core.model.express.internal.Location;

/**
 * Created by J.Bautista
 */
public class Warehouse implements Parcelable{

    private int id;
    private int forClaiming;
    private int forPickup;
    private double latitude;
    private double longitude;

    public Warehouse(){


    }

    public Warehouse(com.yilinker.core.model.express.internal.Warehouse warehouse){

        this.id = warehouse.getId();
        this.forClaiming = warehouse.getForClaiming();
        this.forPickup = warehouse.getForPickup();
//        this.latitude = Double.warehouse.getLocation().getLatitude();

        //temp
        this.latitude = 14.12345;
        this.longitude = 121.12133;

    }

    protected Warehouse(Parcel in) {
        id = in.readInt();
        forClaiming = in.readInt();
        forPickup = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForClaiming() {
        return forClaiming;
    }

    public void setForClaiming(int forClaiming) {
        this.forClaiming = forClaiming;
    }

    public int getForPickup() {
        return forPickup;
    }

    public void setForPickup(int forPickup) {
        this.forPickup = forPickup;
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

    public static final Creator<Warehouse> CREATOR = new Creator<Warehouse>() {
        @Override
        public Warehouse createFromParcel(Parcel in) {
            return new Warehouse(in);
        }

        @Override
        public Warehouse[] newArray(int size) {
            return new Warehouse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(forClaiming);
        dest.writeInt(forPickup);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
