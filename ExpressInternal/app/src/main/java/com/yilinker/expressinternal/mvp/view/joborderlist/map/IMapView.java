package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import java.util.List;

/**
 * Created by J.Bautista
 *
 * Interface for views using maps
 */
public interface IMapView {

    public void initializeMap();
    public void addCurrentLocationMarker(Object marker);
    public void addMarkers(List markers, int type);
    public void removeMarkers(List markers, int type);
    public void centerMap(double latitude, double longitude);
    public void zoomMap(double scale, double centerLatitude, double centerLongitude);
}
