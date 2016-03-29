package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ProblematicType implements Parcelable {

    private int id;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.type);
    }

    public ProblematicType() {
    }

    protected ProblematicType(Parcel in) {
        this.id = in.readInt();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<ProblematicType> CREATOR = new Parcelable.Creator<ProblematicType>() {
        public ProblematicType createFromParcel(Parcel source) {
            return new ProblematicType(source);
        }

        public ProblematicType[] newArray(int size) {
            return new ProblematicType[size];
        }
    };
}
