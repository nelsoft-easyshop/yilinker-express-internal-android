package com.yilinker.expressinternal.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J.Bautista
 */
public class ChecklistItem implements Parcelable {

    private int id;
    private String title;
    private boolean isChecked;
    private Bundle attachedItem;
    private boolean needData;

    public ChecklistItem(){

    }

    protected ChecklistItem(Parcel in) {

        id = in.readInt();
        title = in.readString();
        isChecked = in.readByte() != 0;
        attachedItem = in.readBundle();
        needData = in.readByte() != 0;
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

    public Bundle getAttachedItem() {
        return attachedItem;
    }

    public void setAttachedItem(Bundle attachedItem) {
        this.attachedItem = attachedItem;
    }

    public boolean needData() {
        return needData;
    }

    public void setNeedData(boolean needData) {
        this.needData = needData;
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
        dest.writeBundle(attachedItem);
        dest.writeByte((byte) (needData ? 1 : 0));
    }
}
