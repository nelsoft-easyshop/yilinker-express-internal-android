package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Patrick on 3/1/2016.
 */
public class MainTab implements Parcelable {

    private int id;
    private String title;
    private boolean isSelected;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }

    public MainTab() {
    }

    protected MainTab(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MainTab> CREATOR = new Parcelable.Creator<MainTab>() {
        public MainTab createFromParcel(Parcel source) {
            return new MainTab(source);
        }

        public MainTab[] newArray(int size) {
            return new MainTab[size];
        }
    };
}
