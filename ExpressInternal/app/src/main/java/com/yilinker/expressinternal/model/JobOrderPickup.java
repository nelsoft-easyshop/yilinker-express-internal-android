package com.yilinker.expressinternal.model;

import android.os.Parcel;

/**
 * Created by J.Bautista
 */
public class JobOrderPickup extends JobOrder{

    private String pickupAddress;
    private String dropoffAddress;
    private double distance;

    protected JobOrderPickup(Parcel in) {
        super(in);
    }
}
