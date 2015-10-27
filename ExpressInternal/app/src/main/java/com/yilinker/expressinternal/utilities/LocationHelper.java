package com.yilinker.expressinternal.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;

/**
 * Created by J.Bautista
 */
public class LocationHelper {

    public static final int REQUEST_DIALOG_LOCATION_ERROR = 4000;

    public static double getDistance(double latitude1, double longitude1, double latitude2, double longitude2){

        float[] distance = new float[1];

        Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, distance);

        return (double)distance[0];
    }

    public static boolean isLocationServicesEnabled(Context context){

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch(Exception ex) {}
        finally {
            return gps_enabled || network_enabled;
        }
    }

    public static void showLocationErrorDialog(final Context context, final DialogDismissListener listener){


        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                paramDialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                listener.onDialogDismiss(REQUEST_DIALOG_LOCATION_ERROR, null);

            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

}
