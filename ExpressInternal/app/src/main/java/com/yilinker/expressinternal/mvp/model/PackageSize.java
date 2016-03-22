package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by wagnavu on 3/18/16.
 */
public class PackageSize implements Parcelable {

    private int id;
    private String name;

    public PackageSize(){

    }

    public PackageSize(com.yilinker.core.model.express.internal.PackageType.Sizes object){

        this.id = object.getId();
        this.name = object.getName();
    }

    protected PackageSize(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<PackageSize> CREATOR = new Creator<PackageSize>() {
        @Override
        public PackageSize createFromParcel(Parcel in) {
            return new PackageSize(in);
        }

        @Override
        public PackageSize[] newArray(int size) {
            return new PackageSize[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
