package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by J.Bautista
 */
public class JobOrder implements Parcelable{

    private String jobOrderNo;
    private String recipient;
    private String contactNo;
    private int status;
    private Date estimatedTimeOfArrival;
    private String size;
    private double earning;
    private int type;

    //For location
    private double latitude;
    private double longitude;

    public JobOrder(){

    }

    protected JobOrder(Parcel in) {
        jobOrderNo = in.readString();
        recipient = in.readString();
        contactNo = in.readString();
        status = in.readInt();
        estimatedTimeOfArrival = (java.util.Date) in.readSerializable();
        size = in.readString();
        earning = in.readDouble();
        type = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public String getJobOrderNo() {
        return jobOrderNo;
    }

    public void setJobOrderNo(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getEstimatedTimeOfArrival() {
        return estimatedTimeOfArrival;
    }

    public void setEstimatedTimeOfArrival(Date estimatedTimeOfArrival) {
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getEarning() {
        return earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public static final Creator<JobOrder> CREATOR = new Creator<JobOrder>() {
        @Override
        public JobOrder createFromParcel(Parcel in) {
            return new JobOrder(in);
        }

        @Override
        public JobOrder[] newArray(int size) {
            return new JobOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobOrderNo);
        dest.writeString(recipient);
        dest.writeString(contactNo);
        dest.writeInt(status);
        dest.writeString(size);
        dest.writeSerializable(estimatedTimeOfArrival);
        dest.writeDouble(earning);
        dest.writeInt(type);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
