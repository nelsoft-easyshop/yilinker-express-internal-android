package com.yilinker.expressinternal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlcoronado on 08/01/2016.
 */
public class PackageType implements Parcelable {

    private int id;
    private String name;
    private List<Sizes> size;

    public PackageType(List<com.yilinker.core.model.express.internal.PackageType> packageType) {

//        this.id = packageType.getId();
//        this.name = packageType.getName();
//        this.size = convertSizes(packageType.getSize());

    }

    private static List<PackageType.Sizes> convertSizes(List<com.yilinker.core.model.express.internal.PackageType.Sizes> list) {

        List<PackageType.Sizes> result = new ArrayList<>();

        PackageType.Sizes group = null;
        for (com.yilinker.core.model.express.internal.PackageType.Sizes item : list) {

            group = new PackageType.Sizes();

            group.setName(item.getName());
            group.setId(item.getId());

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

    public List<Sizes> getSize() {
        return size;
    }

    public void setSize(List<Sizes> size) {
        this.size = size;
    }


    public static class Sizes implements Parcelable {

        private int id;
        private String name;

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
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public Sizes() {
        }

        protected Sizes(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<Sizes> CREATOR = new Parcelable.Creator<Sizes>() {
            public Sizes createFromParcel(Parcel source) {
                return new Sizes(source);
            }

            public Sizes[] newArray(int size) {
                return new Sizes[size];
            }
        };
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

    public PackageType() {
    }

    public PackageType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.size = in.createTypedArrayList(Sizes.CREATOR);
    }

    public static final Parcelable.Creator<PackageType> CREATOR = new Parcelable.Creator<PackageType>() {
        public PackageType createFromParcel(Parcel source) {
            return new PackageType(source);
        }

        public PackageType[] newArray(int size) {
            return new PackageType[size];
        }
    };
}
