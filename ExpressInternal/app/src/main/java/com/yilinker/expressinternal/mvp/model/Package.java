package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J.Bautista on 3/18/16.
 */
public class Package implements Parcelable{

    private String jobOrderNo;
    private int typeId;
    private int selectedId;
    private double height;
    private double length;
    private double width;
    private double weight;
    private String shippingFee;
    private String packageSize;
    private String packageType;

    public Package(){

    }

    protected Package(Parcel in) {
        jobOrderNo = in.readString();
        typeId = in.readInt();
        selectedId = in.readInt();
        height = in.readDouble();
        length = in.readDouble();
        width = in.readDouble();
        weight = in.readDouble();
        shippingFee = in.readString();
        packageSize = in.readString();
        packageType = in.readString();
    }

    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel in) {
            return new Package(in);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };

    public String getJobOrderNo() {
        return jobOrderNo;
    }

    public void setJobOrderNo(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(jobOrderNo);
        dest.writeInt(typeId);
        dest.writeInt(selectedId);
        dest.writeDouble(height);
        dest.writeDouble(length);
        dest.writeDouble(width);
        dest.writeDouble(weight);
        dest.writeString(shippingFee);
        dest.writeString(packageSize);
        dest.writeString(packageType);
    }
}
