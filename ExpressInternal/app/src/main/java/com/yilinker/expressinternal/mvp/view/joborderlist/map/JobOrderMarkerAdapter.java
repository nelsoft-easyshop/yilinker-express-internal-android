package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.adapter.BaseMapAdapter;
import com.yilinker.expressinternal.mvp.model.JobType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class JobOrderMarkerAdapter extends BaseMapAdapter<MarkerOptions, JobOrder, GoogleMap> {

    private HashMap<String, MarkerOptions> markerHashMap;

    public JobOrderMarkerAdapter(List<JobOrder> objects, GoogleMap map) {
        super(objects, map);

        markerHashMap = new HashMap<>();
    }

    @Override
    public MarkerOptions createMapMarker(JobOrder object) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng markerLocation = new LatLng(object.getLatitude(), object.getLongitude());

        markerOptions.position(markerLocation);

        int resourceId = 0;
        if(object.getStatus().equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            resourceId = R.drawable.pin_joborder_delivery;
        }
        else {

            resourceId = R.drawable.pin_joborder_pickup;
        }

        markerOptions.icon(BitmapDescriptorFactory.fromResource(resourceId));
        markerOptions.snippet(object.getJobOrderNo());

        return markerOptions;
    }

    @Override
    protected void addToMap(MarkerOptions marker) {

       Marker mapMarker = getMap().addMarker(marker);

       markerHashMap.put(marker.getSnippet(), marker);
    }

    @Override
    protected void removeFromMap(MarkerOptions marker) {


    }

    @Override
    public void clearMap() {
        getMap().clear();
    }

    @Override
    public JobOrder getObjectById(String id) {

        MarkerOptions markerOptions = markerHashMap.get(id);

        return getObject(markerOptions);
    }

    public JobOrder getObject(Marker marker){

        return getObjectById(marker.getSnippet());

    }
}
