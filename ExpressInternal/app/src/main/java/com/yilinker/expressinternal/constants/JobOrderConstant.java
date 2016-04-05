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
    public static final int PROBLEMATIC_RECIPIENT_NOT_FOUND = 100;
    public static final int PROBLEMATIC_REJECTED = 101;
    public static final int PROBLEMATIC_DAMAGED = 102;
    public static final int PROBLEMATIC_UNABLE_TO_PAY = 103;

    //Job Order Type
    public static final String JO_TYPE_PICKUP_KEY = "Type 2";
    public static final String JO_TYPE_DELIVERY_KEY = "Type 1";

    //JO Problem Type
    public static final String PROBLEM_OUT_OF_STOCK = "Out of Stock";

}
