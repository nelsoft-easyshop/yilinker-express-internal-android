package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.expressinternal.mvp.adapter.BaseMapAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by J.Bautista on 4/11/16.
 */
public abstract class GoogleMapMarkerAdapter<O> extends BaseMapAdapter<MarkerOptions, O, GoogleMap> {

    private HashMap<String, MarkerOptions> markerHashMap;

    public GoogleMapMarkerAdapter(List<O> objects, GoogleMap map) {
        super(objects, map);

        markerHashMap = new HashMap<>();
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
    public O getObjectById(String id) {

        MarkerOptions markerOptions = getMarkerOption(id);

        return getObject(markerOptions);
    }


    public O getObject(Marker marker){

        return getObjectById(marker.getSnippet());

    }

    private MarkerOptions getMarkerOption(String id){

        MarkerOptions markerOptions = markerHashMap.get(id);

        return markerOptions;
    }

}
