package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 *18/2016
 */
public class PackageType implements Parcelable {

    private int id;
    private String name;
    private List<PackageSize> size;


    public PackageType() {

    }

    public PackageType(com.yilinker.core.model.express.internal.PackageType packageType) {

        this.id = packageType.getId();
        this.name = packageType.getName();
        this.size = convertSizes(packageType.getSize());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(size);
    }

    protected PackageType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.size = in.createTypedArrayList(PackageSize.CREATOR);
    }

    public static final Creator<PackageType> CREATOR = new Creator<PackageType>() {
        public PackageType createFromParcel(Parcel source) {
            return new PackageType(source);
        }

        public PackageType[] newArray(int size) {
            return new PackageType[size];
        }
    };


    private static List<PackageSize> convertSizes(List<com.yilinker.core.model.express.internal.PackageType.Sizes> list) {

        List<PackageSize> result = new ArrayList<>();

        PackageSize group = null;
        for (com.yilinker.core.model.express.internal.PackageType.Sizes item : list) {

            group = new PackageSize(item);

            result.add(group);
        }

        return result;
    }


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

    public List<PackageSize> getSize() {
        return size;
    }

    public void setSize(List<PackageSize> size) {
        this.size = size;
    }
}
