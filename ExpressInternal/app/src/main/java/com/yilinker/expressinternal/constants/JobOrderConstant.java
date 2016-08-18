package com.yilinker.expressinternal.constants;

/**
 * Created by J.Bautista
 */
public class JobOrderConstant {

    //Job order type
//    public static final int JO_TYPE_PICKUP = 0;
//    public static final int JO_TYPE_DELIVERY = 1;
    public static final String JO_TYPE_PICKUP = "pick-up";
    public static final String JO_TYPE_DELIVERY = "delivery";


    //Job order status TODO Change to actual values
    public static final String JO_OPEN = "Open";
    public static final String JO_CURRENT_PICKUP = "For Pick-up";
    public static final String JO_CURRENT_DELIVERY = "For Delivery";
    public static final String JO_CURRENT_CLAIMING = "For Claiming";
    public static final String JO_CURRENT_DROPOFF = "For Drop Off";
    public static final String JO_COMPLETE = "Completed";
    public static final String JO_PROBLEMATIC = "Problematic";


    //Problematic Job Order Type
    public static final int PROBLEMATIC_RECIPIENT_NOT_FOUND = 12;
    public static final int PROBLEMATIC_REJECTED = 17;
    public static final int PROBLEMATIC_DAMAGED = 23;
    public static final int PROBLEMATIC_UNABLE_TO_PAY = 16;
    public static final int PROBLEMATIC_OUT_OF_STOCK = 111;
    public static final int PROBLEMATIC_OTHERS = 160;
    public static final int PROBLEMATIC_REFUSED_TO_ACCEPT = 114;
    public static final int PROBLEMATIC_NOT_YET_READY = 113;
    public static final int PROBLEMATIC_RECIPIENT_IS_NOT_AROUND = 100;

    //Job Order Type
    public static final String JO_TYPE_PICKUP_KEY = "Type 2";
    public static final String JO_TYPE_DELIVERY_KEY = "Type 1";

}
