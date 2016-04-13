package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.GoogleMapConstant;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.adapter.BaseMapAdapter;
import com.yilinker.expressinternal.mvp.model.JobType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class JobOrderMarkerAdapter extends GoogleMapMarkerAdapter<JobOrder> {

    public JobOrderMarkerAdapter(List<JobOrder> objects, GoogleMap map) {
        super(objects, map);

    }

    @Override
    public MarkerOptions createMapMarker(JobOrder object) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng markerLocation = new LatLng(object.getLatitude(), object.getLongitude());

        markerOptions.position(markerLocation);
        markerOptions.title(String.valueOf(GoogleMapConstant.MARKER_TYPE_JOB_ORDERS));

        int resourceId = 0;
        if(object.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

//            resourceId = R.drawable.pin_joborder_delivery;
            resourceId = R.drawable.pin_joborder_pickup_2;
        }
        else if(object.getStatus().equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)) {

//            resourceId = R.drawable.pin_joborder_pickup;
            resourceId = R.drawable.pin_joborder_problematic_2;

        }
        else if(object.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)) {

//            resourceId = R.drawable.pin_joborder_pickup;
            resourceId = R.drawable.pin_branch_2;

        }
        else {

//            resourceId = R.drawable.pin_joborder_pickup;
            resourceId = R.drawable.pin_joborder_delivery_2;

        }

        markerOptions.icon(BitmapDescriptorFactory.fromResource(resourceId));
        markerOptions.snippet(object.getJobOrderNo());

        return markerOptions;
    }


    @Override
    protected void removeFromMap(MarkerOptions marker) {


    }
}
