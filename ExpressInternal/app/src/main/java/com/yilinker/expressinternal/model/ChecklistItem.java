package com.yilinker.expressinternal.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by J.Bautista
 */
public class ChecklistItem implements Parcelable {

    private String title;
    private boolean isChecked;
    private Bundle attachedItem;

    public ChecklistItem(){

    }

    protected ChecklistItem(Parcel in) {
        title = in.readString();
        isChecked = in.readByte() != 0;
        attachedItem = in.readBundle();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeBundle(attachedItem);
    }
}
