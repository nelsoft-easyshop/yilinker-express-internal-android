package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yilinker.core.model.express.internal.ProblemDetail;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.constants.JobOrderConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class JobOrder implements Parcelable{

    private static HashMap<String, String> JOB_ORDER_TYPE;

    private static final String SERVER_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private int id;
    private String jobOrderNo;
    private String recipientName;
    private String recipientContactNo;
    private String shipperName;
    private String shipperContactNo;
    private String status;
    private Date estimatedTimeOfArrival;
    private String size;
    private double earning;
    private String type;
    private boolean isOpen;
    private String areaCode;

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
    private int jobOrderTypeId;

    //For Problematic
    private String csrName;
    private String problemType;
    private String remarks;
    private List<String> problematicImages;

    private double amountToCollect;

    private boolean forSyncing;

    private Date dateCreated;
    private Date dateAccepted;


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
        recipientName = jobOrder.getRecipientName();
        recipientContactNo = jobOrder.getRecipientContactNo();
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
//        items = new ArrayList<>();
        estimatedTimeOfArrival = DateUtility.convertStringToDate(jobOrder.getEta(), SERVER_DATE_FORMAT);
        packageDescription = jobOrder.getPackageDescription();
        amountToCollect = jobOrder.getAmountToCollect();
        dateAccepted = DateUtility.convertStringToDate(jobOrder.getDateAccepted(), SERVER_DATE_FORMAT);
        dateCreated = DateUtility.convertStringToDate(jobOrder.getDateCreated(), SERVER_DATE_FORMAT);
        jobOrderTypeId = jobOrder.getJobOrderTypeId();
        shipperContactNo = jobOrder.getShipperContactNo();
        shipperName = jobOrder.getShipperName();

        isOpen = jobOrder.isOpen();
        areaCode = jobOrder.getAreaCode();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobOrderNo() {
        return jobOrderNo;
    }

    public void setJobOrderNo(String jobOrderNo) {
        this.jobOrderNo = jobOrderNo;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientContactNo() {
        return recipientContactNo;
    }

    public void setRecipientContactNo(String recipientContactNo) {
        this.recipientContactNo = recipientContactNo;
    }

    public int getJobOrderTypeId() {
        return jobOrderTypeId;
    }

    public void setJobOrderTypeId(int jobOrderTypeId) {
        this.jobOrderTypeId = jobOrderTypeId;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

    public boolean isForSyncing() {
        return forSyncing;
    }

    public void setForSyncing(boolean forSyncing) {
        this.forSyncing = forSyncing;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateAccepted() {
        return dateAccepted;
    }

    public void setDateAccepted(Date dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    public String getShipperContactNo() {
        return shipperContactNo;
    }

    public void setShipperContactNo(String shipperContactNo) {
        this.shipperContactNo = shipperContactNo;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.jobOrderNo);
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientContactNo);
        dest.writeString(this.shipperName);
        dest.writeString(this.shipperContactNo);
        dest.writeString(this.status);
        dest.writeLong(estimatedTimeOfArrival != null ? estimatedTimeOfArrival.getTime() : -1);
        dest.writeString(this.size);
        dest.writeDouble(this.earning);
        dest.writeString(this.type);
        dest.writeByte(isOpen ? (byte) 1 : (byte) 0);
        dest.writeString(this.areaCode);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.pickupAddress);
        dest.writeString(this.dropoffAddress);
        dest.writeDouble(this.distance);
        dest.writeString(this.deliveryAddress);
        dest.writeString(this.itemLocation);
        dest.writeString(this.branchName);
        dest.writeLong(timeDelivered != null ? timeDelivered.getTime() : -1);
        dest.writeInt(this.rating);
        dest.writeStringList(this.images);
        dest.writeStringList(this.items);
        dest.writeString(this.waybillNo);
        dest.writeString(this.packageDescription);
        dest.writeInt(this.jobOrderTypeId);
        dest.writeString(this.csrName);
        dest.writeString(this.problemType);
        dest.writeString(this.remarks);
        dest.writeStringList(this.problematicImages);
        dest.writeDouble(this.amountToCollect);
        dest.writeByte(forSyncing ? (byte) 1 : (byte) 0);
        dest.writeLong(dateCreated != null ? dateCreated.getTime() : -1);
        dest.writeLong(dateAccepted != null ? dateAccepted.getTime() : -1);
    }

    protected JobOrder(Parcel in) {
        this.id = in.readInt();
        this.jobOrderNo = in.readString();
        this.recipientName = in.readString();
        this.recipientContactNo = in.readString();
        this.shipperName = in.readString();
        this.shipperContactNo = in.readString();
        this.status = in.readString();
        long tmpEstimatedTimeOfArrival = in.readLong();
        this.estimatedTimeOfArrival = tmpEstimatedTimeOfArrival == -1 ? null : new Date(tmpEstimatedTimeOfArrival);
        this.size = in.readString();
        this.earning = in.readDouble();
        this.type = in.readString();
        this.isOpen = in.readByte() != 0;
        this.areaCode = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.pickupAddress = in.readString();
        this.dropoffAddress = in.readString();
        this.distance = in.readDouble();
        this.deliveryAddress = in.readString();
        this.itemLocation = in.readString();
        this.branchName = in.readString();
        long tmpTimeDelivered = in.readLong();
        this.timeDelivered = tmpTimeDelivered == -1 ? null : new Date(tmpTimeDelivered);
        this.rating = in.readInt();
        this.images = in.createStringArrayList();
        this.items = in.createStringArrayList();
        this.waybillNo = in.readString();
        this.packageDescription = in.readString();
        this.jobOrderTypeId = in.readInt();
        this.csrName = in.readString();
        this.problemType = in.readString();
        this.remarks = in.readString();
        this.problematicImages = in.createStringArrayList();
        this.amountToCollect = in.readDouble();
        this.forSyncing = in.readByte() != 0;
        long tmpDateCreated = in.readLong();
        this.dateCreated = tmpDateCreated == -1 ? null : new Date(tmpDateCreated);
        long tmpDateAccepted = in.readLong();
        this.dateAccepted = tmpDateAccepted == -1 ? null : new Date(tmpDateAccepted);
    }

    public static final Creator<JobOrder> CREATOR = new Creator<JobOrder>() {
        public JobOrder createFromParcel(Parcel source) {
            return new JobOrder(source);
        }

        public JobOrder[] newArray(int size) {
            return new JobOrder[size];
        }
    };
}
