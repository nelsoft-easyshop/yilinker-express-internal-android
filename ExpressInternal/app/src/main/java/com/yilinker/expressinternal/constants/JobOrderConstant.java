package com.yilinker.expressinternal.constants;

/**
 * Created by J.Bautista
 */
public class JobOrderConstant {

    //Job order type
    public static final int JO_TYPE_PICKUP = 0;
    public static final int JO_TYPE_DELIVERY = 1;


    //Job order status TODO Change to actual values
    public static final int JO_OPEN = 0;
    public static final int JO_CURRENT_PICKUP = 1;
    public static final int JO_CURRENT_DELIVERY = 2;
    public static final int JO_CURRENT_CLAIMING = 3;
    public static final int JO_CURRENT_DROPOFF = 4;
    public static final int JO_COMPLETE = 5;
    public static final int JO_PROBLEMATIC = 6;

    //Problematic Job Order Type
    public static final int PROBLEMATIC_RECIPIENT_NOT_FOUND = 100;
    public static final int PROBLEMATIC_REJECTED = 101;
    public static final int PROBLEMATIC_DAMAGED = 102;
    public static final int PROBLEMATIC_UNABLE_TO_PAY = 103;

}
