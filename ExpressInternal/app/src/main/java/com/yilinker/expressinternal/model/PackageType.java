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

    /**
     * For data intent
     */

    private String typeName;
    private String typeId;
    private String sizeId;
    private String height;
    private String length;
    private String width;
    private String weight;
    private String shippingFee;

    public PackageType(com.yilinker.core.model.express.internal.PackageType packageType) {

        this.id = packageType.getId();
        this.name = packageType.getName();
        this.size = convertSizes(packageType.getSize());

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
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

    public PackageType() {
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
        dest.writeString(this.typeName);
        dest.writeString(this.typeId);
        dest.writeString(this.sizeId);
        dest.writeString(this.height);
        dest.writeString(this.length);
        dest.writeString(this.width);
        dest.writeString(this.weight);
        dest.writeString(this.shippingFee);
    }

    protected PackageType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.size = in.createTypedArrayList(Sizes.CREATOR);
        this.typeName = in.readString();
        this.typeId = in.readString();
        this.sizeId = in.readString();
        this.height = in.readString();
        this.length = in.readString();
        this.width = in.readString();
        this.weight = in.readString();
        this.shippingFee = in.readString();
    }

    public static final Creator<PackageType> CREATOR = new Creator<PackageType>() {
        public PackageType createFromParcel(Parcel source) {
            return new PackageType(source);
        }

        public PackageType[] newArray(int size) {
            return new PackageType[size];
        }
    };
}
