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

    public Rider(){


    }

    public Rider(com.yilinker.core.model.express.internal.Rider rider){

        name = rider.getName();
        imageUrl = rider.getImageUrl();
        currentDeliveryJO = rider.getCurrentDeliveryJO();
        currentPickupJO = rider.getCurrentPickupJO();
        cashOnHand = rider.getCashOnHand();
        totalEarning = rider.getTotalEarning();
        completedJO = rider.getCompletedJO();
        currentJO = rider.getCurrentJO();

    }

    protected Rider(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        currentDeliveryJO = in.readInt();
        currentPickupJO = in.readInt();
        cashOnHand = in.readDouble();
        totalEarning = in.readDouble();
        completedJO = in.readInt();
        currentJO = in.readInt();
    }

    public static final Creator<Rider> CREATOR = new Creator<Rider>() {
        @Override
        public Rider createFromParcel(Parcel in) {
            return new Rider(in);
        }

        @Override
        public Rider[] newArray(int size) {
            return new Rider[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(currentDeliveryJO);
        dest.writeInt(currentPickupJO);
        dest.writeDouble(cashOnHand);
        dest.writeDouble(totalEarning);
        dest.writeInt(completedJO);
        dest.writeInt(currentJO);
    }
}
