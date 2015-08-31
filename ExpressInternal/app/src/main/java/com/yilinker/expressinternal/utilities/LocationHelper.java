package com.yilinker.expressinternal.utilities;

import android.location.Location;

/**
 * Created by J.Bautista
 */
public class LocationHelper {

    public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2){

        float[] distance = new float[1];

        Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, distance);

        return (double)distance[0];
    }

}
