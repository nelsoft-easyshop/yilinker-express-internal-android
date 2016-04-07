package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListPresenter;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityJobDetailsMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/1/16.
 */
public class FragmentJobListMap extends Fragment implements IJobListMapView, OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String ARG_JOBS = "jobs";

    private static final int TYPE_JOB_ORDERS = 100;
    private static final int TYPE_WAREHOUSE = 101;
    private static final int TYPE_LOCATION = 102;

    private JobListPresenter presenter;

    private JobOrderMarkerAdapter mapAdapter;

    private GoogleApiClient mGoogleApiClient;
    private MapView mapView;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        buildGoogleApiClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobs_map, container, false);

        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls

        MapsInitializer.initialize(this.getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null){

            presenter = new JobListPresenter();
            presenter.setModel(new ArrayList<JobOrder>());
        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        initializeView(view);
        initializeJobsAdapter();

        presenter.setModel((ArrayList) getArguments().getParcelableArrayList(ARG_JOBS));
        presenter.bindView(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

//        zoomMap(10, 14.123121, 121.123424);

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void loadJobOrderList(List<JobOrder> jobOrders) {

        mapAdapter.clearMap();
        addMarkers(jobOrders, TYPE_JOB_ORDERS);
    }


    @Override
    public void loadWarehouses(List list) {

    }


    @Override
    public void showJobOrderDetails(JobOrder joborder) {

    }

    @Override
    public void showLoader(boolean isVisible) {

    }


    @Override
    public void initializeMap() {

    }

    @Override
    public void addCurrentLocationMarker(Object marker) {

    }

    @Override
    public void addMarkers(List objects, int type) {

        switch (type){

            case TYPE_JOB_ORDERS:

                mapAdapter.addMarkers(objects);
                break;

        }



    }

    @Override
    public void removeMarkers(List markers, int type) {

    }

    @Override
    public void centerMap(double latitude, double longitude) {


    }

    @Override
    public void zoomMap(double scale, double centerLatitude, double centerLongitude) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(centerLatitude, centerLongitude), (float)scale);
        map.moveCamera(cameraUpdate);

    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    private void setUpMap(){

    }

    private void initializeJobsAdapter(){

        mapAdapter = new JobOrderMarkerAdapter(new ArrayList<JobOrder>(), map);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        JobOrder jobOrder = mapAdapter.getObject(marker);
        showJobDetails(jobOrder);

        return false;
    }

    private void showJobDetails(JobOrder jobOrder){

        Intent intent = new Intent(getActivity(), ActivityJobDetailsMain.class);
        intent.putExtra(ActivityJobDetailsMain.ARG_JOB, jobOrder);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        mGoogleApiClient.connect();
    }

    private void setupMap(){


    }

    private void initializeView(View parent){

        ImageView btnLegend = (ImageView) parent.findViewById(R.id.btnLegend);

        btnLegend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMapLegend(v);
            }
        });
    }

    private void showMapLegend(View anchor){

        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        location[1] = location[1] - anchor.getHeight();

        FragmentDialogMapLegend dialog = FragmentDialogMapLegend.createInstance(location[1]);

        dialog.show(getChildFragmentManager(), "legend");
    }
}
