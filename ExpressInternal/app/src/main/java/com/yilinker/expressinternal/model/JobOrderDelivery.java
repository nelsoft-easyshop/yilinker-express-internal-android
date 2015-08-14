package com.yilinker.expressinternal.model;

import android.os.Parcel;

/**
 * Created by J.Bautista
 */
public class JobOrderDelivery extends JobOrder {

    private String deliveryAddress;
    private String itemLocation;

    protected JobOrderDelivery(Parcel in) {
        super(in);
    }
}
