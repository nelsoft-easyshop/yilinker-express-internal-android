package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by J.Bautista on 3/28/16.
 */
public class DeliveryPackage implements Parcelable {

    private String jobOrderNo;
    private String waybillNo;
    private List<String> images;
    private String signature;
    private boolean isUpdated;

    protected DeliveryPackage(Parcel in) {

        jobOrderNo = in.readString();
        waybillNo = in.readString();
        images = in.createStringArrayList();
        signature = in.readString();
        isUpdated = in.readByte() != 0;
    }

    public static final Creator<DeliveryPackage> CREATOR = new Creator<DeliveryPackage>() {
        @Override
        public DeliveryPackage createFromParcel(Parcel in) {
            return new DeliveryPackage(in);
        }

        @Override
        public DeliveryPackage[] newArray(int size) {
            return new DeliveryPackage[size];
        }
    };

    public String getJobOrderNo() {
        return jobOrderNo;
    }

    public void setJobOrderNo(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(jobOrderNo);
        dest.writeString(waybillNo);
        dest.writeStringList(images);
        dest.writeString(signature);
        dest.writeByte((byte) (isUpdated ? 1 : 0));
    }
}
