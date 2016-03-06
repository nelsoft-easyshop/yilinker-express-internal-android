package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import java.util.List;

/**
 * Created by J.Bautista
 *
 * Interface for views using maps
 */
public interface IMapView<M> {

    public void initializeMap();
    public void addCurrentLocationMarker(M marker);
    public void addMarkers(List<M> markers);
    public void removeMarkers(List<M> markers);
    public void centerMap(double latitude, double longitude);
    public void zoomMap(double scale, double centerLatitude, double centerLongitude);
}
