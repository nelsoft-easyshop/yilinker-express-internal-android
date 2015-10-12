package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import com.yilinker.core.model.express.internal.ProblemDetail;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.constants.JobOrderConstant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class JobOrder implements Parcelable{

    private static HashMap<String, String> JOB_ORDER_TYPE;

    private static final String SERVER_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private String jobOrderNo;
    private String recipient;
    private String contactNo;
    private String status;
    private Date estimatedTimeOfArrival;
    private String size;
    private double earning;
    private String type;

    //For location
    private double latitude;
    private double longitude;

    private String pickupAddress;
    private String dropoffAddress;
    private double distance;
    private String deliveryAddress;
    private String itemLocation;
    private String branchName;
    private Date timeDelivered;
    private int rating;
    private List<String> images;
    private List<String> items;
    private String waybillNo;
    private String packageDescription;

    //For Problematic
    private String csrName;
    private String problemType;
    private String remarks;
    private List<String> problematicImages;

    private double amountToCollect;

    static {

        //Temp
        JOB_ORDER_TYPE = new HashMap<>();

        JOB_ORDER_TYPE.put(JobOrderConstant.JO_TYPE_PICKUP_KEY, "Pickup");
        JOB_ORDER_TYPE.put(JobOrderConstant.JO_TYPE_DELIVERY_KEY, "Delivery");

    }

    public JobOrder(){

    }

    public JobOrder(com.yilinker.core.model.express.internal.JobOrder jobOrder){

        jobOrderNo = jobOrder.getJobOrderNo();
        recipient = jobOrder.getRecipientName();
        contactNo = jobOrder.getRecipientContactNo();
        size = jobOrder.getPackageSize();
        earning = jobOrder.getEarnings();
//        latitude = Double.valueOf(jobOrder.getLocation().getLatitude());
//        longitude = Double.valueOf(jobOrder.getLocation().getLongitude());
        latitude = jobOrder.getLocation().getLatitude();
        longitude = jobOrder.getLocation().getLongitude();
        branchName = jobOrder.getBranchName();
        deliveryAddress = jobOrder.getDeliveryAddress();
        pickupAddress = jobOrder.getPickupAddr();
        dropoffAddress = jobOrder.getDropoffAddr();
        status = jobOrder.getJobOrderStatus();
        timeDelivered = DateUtility.convertStringToDate(jobOrder.getTimeDelivered(), SERVER_DATE_FORMAT);
        rating = jobOrder.getRating();
        images = jobOrder.getImages();
        type = jobOrder.getJobOrderType();
//        items = jobOrder.getItems();
        items = new ArrayList<>();
        estimatedTimeOfArrival = DateUtility.convertStringToDate(jobOrder.getEta(), SERVER_DATE_FORMAT);
        packageDescription = jobOrder.getPackageDescription();
        amountToCollect = jobOrder.getAmountToCollect();

        //temp
//        estimatedTimeOfArrival = Calendar.getInstance().getTime();
//        type = JOB_ORDER_TYPE.get(jobOrder.getJobOrderType());
        waybillNo = jobOrder.getTrackingNo();

        //For Problematic
        ProblemDetail problemDetail = jobOrder.getProblemDetails();
        if(problemDetail != null) {

            csrName = problemDetail.getCsrName();
            problemType = problemDetail.getProblemType();
            remarks = problemDetail.getProblemRemarks();
            problematicImages = problemDetail.getImages();
        }

    }

    protected JobOrder(Parcel in) {

        jobOrderNo = in.readString();
        recipient = in.readString();
        contactNo = in.readString();
        status = in.readString();
        size = in.readString();
        estimatedTimeOfArrival = (java.util.Date) in.readSerializable();
        earning = in.readDouble();
        type = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        pickupAddress = in.readString();
        dropoffAddress = in.readString();
        distance = in.readDouble();
        deliveryAddress = in.readString();
        itemLocation = in.readString();
        branchName = in.readString();
        timeDelivered = (java.util.Date) in.readSerializable();
        rating = in.readInt();
        amountToCollect = in.readDouble();

        images = new ArrayList<>();
        in.readStringList(images);

        items = new ArrayList<>();
        in.readStringList(items);
        waybillNo = in.readString();
        packageDescription = in.readString();

        csrName = in.readString();
        problemType = in.readString();
        remarks = in.readString();

        problematicImages = new ArrayList<>();
        in.readStringList(problematicImages);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Date getTimeDelivered() {
        return timeDelivered;
    }

    public void setTimeDelivered(Date timeDelivered) {
        this.timeDelivered = timeDelivered;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getAmountToCollect() {
        return amountToCollect;
    }

    public void setAmountToCollect(double amountToCollect) {
        this.amountToCollect = amountToCollect;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getCsrName() {
        return csrName;
    }

    public void setCsrName(String csrName) {
        this.csrName = csrName;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<String> getProblematicImages() {
        return problematicImages;
    }

    public void setProblematicImages(List<String> problematicImages) {
        this.problematicImages = problematicImages;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
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
        dest.writeString(status);
        dest.writeString(size);
        dest.writeSerializable(estimatedTimeOfArrival);
        dest.writeDouble(earning);
        dest.writeString(type);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(pickupAddress);
        dest.writeString(dropoffAddress);
        dest.writeDouble(distance);
        dest.writeString(deliveryAddress);
        dest.writeString(itemLocation);
        dest.writeString(branchName);
        dest.writeSerializable(timeDelivered);
        dest.writeInt(rating);
        dest.writeDouble(amountToCollect);
        dest.writeStringList(images);
        dest.writeStringList(items);
        dest.writeString(waybillNo);
        dest.writeString(packageDescription);
        dest.writeString(csrName);
        dest.writeString(problemType);
        dest.writeString(remarks);
        dest.writeStringList(problematicImages);
    }
}
