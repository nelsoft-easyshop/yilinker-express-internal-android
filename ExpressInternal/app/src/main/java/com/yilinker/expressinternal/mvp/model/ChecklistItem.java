package com.yilinker.expressinternal.mvp.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J.Bautista on 3/28/16.
 */
public class ChecklistItem implements Parcelable {

    private int id;
    private String title;
    private boolean isChecked;
    private String attachedItem;
    private boolean needData;
    private String extraField;

    public ChecklistItem(){

    }

    protected ChecklistItem(Parcel in) {

        id = in.readInt();
        title = in.readString();
        isChecked = in.readByte() != 0;
        attachedItem = in.readString();
        needData = in.readByte() != 0;
        extraField = in.readString();
    }

    public static final Creator<ChecklistItem> CREATOR = new Creator<ChecklistItem>() {
        @Override
        public ChecklistItem createFromParcel(Parcel in) {
            return new ChecklistItem(in);
        }

        @Override
        public ChecklistItem[] newArray(int size) {
            return new ChecklistItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getAttachedItem() {
        return attachedItem;
    }

    public void setAttachedItem(String attachedItem) {
        this.attachedItem = attachedItem;
    }

    public boolean needData() {
        return needData;
    }

    public void setNeedData(boolean needData) {
        this.needData = needData;
    }

    public String getExtraField() {
        return extraField;
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(attachedItem);
        dest.writeByte((byte) (needData ? 1 : 0));
        dest.writeString(extraField);
    }
}
