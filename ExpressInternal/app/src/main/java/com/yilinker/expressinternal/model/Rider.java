package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

/**
 * Created by J.Bautista
 */
public class Rider implements Parcelable {

    private String name;
    private String imageUrl;
    private int currentDeliveryJO;
    private int currentPickupJO;
    private double cashOnHand;
    private double totalEarning;
    private int completedJO;
    private int currentJO;
    private int currentDropoff;
    private String areaCode;
    private String accreditationStatus;

    public Rider(){


    }

    public Rider(com.yilinker.core.model.express.internal.Rider rider){

        name = rider.getName();
        imageUrl = rider.getImageUrl();
        currentDeliveryJO = rider.getCurrentDeliveryJO();
        currentPickupJO = rider.getCurrentPickupJO();
        cashOnHand = rider.getCashOnHand();
//        totalEarning = rider.getTotalEarning();
        completedJO = rider.getCompletedJO();
        currentJO = rider.getCurrentJO();
        currentDropoff = rider.getCurrentDropoffJO();
        areaCode = rider.getAreaCode();
        accreditationStatus = rider.getAccreditationStatus();
    }

    public String getAaccreditationStatus() {
        return accreditationStatus;
    }

    public void setAaccreditationStatus(String aaccreditationStatus) {
        this.accreditationStatus = aaccreditationStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCurrentDeliveryJO() {
        return currentDeliveryJO;
    }

    public void setCurrentDeliveryJO(int currentDeliveryJO) {
        this.currentDeliveryJO = currentDeliveryJO;
    }

    public int getCurrentPickupJO() {
        return currentPickupJO;
    }

    public void setCurrentPickupJO(int currentPickupJO) {
        this.currentPickupJO = currentPickupJO;
    }

    public double getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(double cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public int getCompletedJO() {
        return completedJO;
    }

    public void setCompletedJO(int completedJO) {
        this.completedJO = completedJO;
    }

    public int getCurrentJO() {
        return currentJO;
    }

    public void setCurrentJO(int currentJO) {
        this.currentJO = currentJO;
    }

    public int getCurrentDropoff() {
        return currentDropoff;
    }

    public void setCurrentDropoff(int currentDropoff) {
        this.currentDropoff = currentDropoff;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.currentDeliveryJO);
        dest.writeInt(this.currentPickupJO);
        dest.writeDouble(this.cashOnHand);
        dest.writeDouble(this.totalEarning);
        dest.writeInt(this.completedJO);
        dest.writeInt(this.currentJO);
        dest.writeInt(this.currentDropoff);
        dest.writeString(this.areaCode);
        dest.writeString(this.accreditationStatus);
    }

    protected Rider(Parcel in) {
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.currentDeliveryJO = in.readInt();
        this.currentPickupJO = in.readInt();
        this.cashOnHand = in.readDouble();
        this.totalEarning = in.readDouble();
        this.completedJO = in.readInt();
        this.currentJO = in.readInt();
        this.currentDropoff = in.readInt();
        this.areaCode = in.readString();
        this.accreditationStatus = in.readString();
    }

    public static final Creator<Rider> CREATOR = new Creator<Rider>() {
        public Rider createFromParcel(Parcel source) {
            return new Rider(source);
        }

        public Rider[] newArray(int size) {
            return new Rider[size];
        }
    };
}
