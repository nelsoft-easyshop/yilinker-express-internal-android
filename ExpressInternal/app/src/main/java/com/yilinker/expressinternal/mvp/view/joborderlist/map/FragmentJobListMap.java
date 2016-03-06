package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.List;

/**
 * Created by J.Bautista on 3/1/16.
 */
public class FragmentJobListMap extends Fragment implements IJobListMapView<JobOrder>{

    public static final String ARG_JOBS = "jobs";

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobs_map, container, false);

        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void loadJobOrderList(List<JobOrder> jobOrders) {

    }

    @Override
    public void showJobOrderDetails(JobOrder joborder) {

    }


    @Override
    public void initializeMap() {

    }

    @Override
    public void addCurrentLocationMarker(JobOrder marker) {

    }

    @Override
    public void addMarkers(List<JobOrder> markers) {

    }

    @Override
    public void removeMarkers(List<JobOrder> markers) {

    }

    @Override
    public void centerMap(double latitude, double longitude) {

    }

    @Override
    public void zoomMap(double scale, double centerLatitude, double centerLongitude) {

    }

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//


    private void setUpMap(){

    }
}
