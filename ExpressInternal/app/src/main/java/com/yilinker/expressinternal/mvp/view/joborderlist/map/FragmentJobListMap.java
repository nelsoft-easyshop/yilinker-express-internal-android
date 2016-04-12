package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.GoogleMapConstant;
import com.yilinker.expressinternal.model.Branch;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.model.Warehouse;
import com.yilinker.expressinternal.mvp.presenter.base.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListMapPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobListPresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;
import com.yilinker.expressinternal.mvp.view.joborderdetails.ActivityJobDetailsMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/1/16.
 */
public class FragmentJobListMap extends BaseFragment implements IJobListMapView, OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    public static final String ARG_JOBS = "jobs";
    public static final String ARG_FOR_DROPOFF = "dropOffCount";

    private JobListMapPresenter presenter;

    private JobOrderMarkerAdapter jobOrderMarkerAdapter;
    private BranchMapMarkerAdapter branchMarkerAdapter;
    private WarehouseMarkerAdapter warehouseMarkerAdapter;

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

        UiSettings uiSettings = map.getUiSettings();

        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);

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

            presenter = new JobListMapPresenter();
            presenter.setModel(new ArrayList<JobOrder>());

            initializeViews(view);

            presenter.setModel((ArrayList) getArguments().getParcelableArrayList(ARG_JOBS));
            presenter.bindView(this);

            presenter.onViewCreated(getRiderBranch());

        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
            presenter.bindView(this);

        }

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

        jobOrderMarkerAdapter.clearMap();
        addMarkers(jobOrders, GoogleMapConstant.MARKER_TYPE_JOB_ORDERS);

        presenter.onLoadJobOrders();
    }


    @Override
    public void loadBranches(List list) {

        addMarkers(list, GoogleMapConstant.MARKER_TYPE_BRANCH);
    }

    @Override
    public void loadWarehouses(List list) {

        addMarkers(list, GoogleMapConstant.MARKER_TYPE_WAREHOUSE);
    }


    @Override
    public void showJobOrderDetails(JobOrder joborder) {

    }

    @Override
    public void initializeViews(View parent) {

        ImageView btnLegend = (ImageView) parent.findViewById(R.id.btnLegend);
        ImageView btnRefresh = (ImageView) parent.findViewById(R.id.btnRefresh);

        btnLegend.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        //Initialize map adapters
        initializeJobsAdapter();
        initializeBranchAdapter();
        initializeWarehouseAdapter();
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

            case GoogleMapConstant.MARKER_TYPE_JOB_ORDERS:

                jobOrderMarkerAdapter.addMarkers(objects);
                break;

            case GoogleMapConstant.MARKER_TYPE_BRANCH:

                branchMarkerAdapter.addMarkers(objects);
                break;

            case GoogleMapConstant.MARKER_TYPE_WAREHOUSE:

                warehouseMarkerAdapter.addMarkers(objects);
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



    @Override
    public boolean onMarkerClick(Marker marker) {


        int type = Integer.parseInt(marker.getTitle());

        switch (type){

            case GoogleMapConstant.MARKER_TYPE_JOB_ORDERS:

                JobOrder jobOrder = jobOrderMarkerAdapter.getObject(marker);
                showJobDetails(jobOrder);
                break;

        }

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


    @Override
    public void addRequest(Request request) {

        addRequestToQueue(request);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnLegend:

                showMapLegend(v);
                break;

            case R.id.btnRefresh:

                refresh();
                break;
        }

    }


    public void reloadBranchDetails(){

        presenter.onReloadBranches(getRiderBranch());
    }

    private void setupMap(){


    }

    private void showMapLegend(View anchor){

        int[] location = new int[2];
        anchor.getLocationOnScreen(location);

        location[1] = location[1] - anchor.getHeight();

        FragmentDialogMapLegend dialog = FragmentDialogMapLegend.createInstance(location[1]);

        dialog.show(getChildFragmentManager(), "legend");
    }

    private Branch getRiderBranch() {

        ApplicationClass appClass = (ApplicationClass)ApplicationClass.getInstance();
        Branch branch = appClass.getRider().getBranch();

        return branch;
    }

    private void refresh(){

        map.clear();

        SwipeRefreshLayout.OnRefreshListener listener = (SwipeRefreshLayout.OnRefreshListener)getParentFragment();
        listener.onRefresh();

    }

    private void initializeJobsAdapter(){

        jobOrderMarkerAdapter = new JobOrderMarkerAdapter(new ArrayList<JobOrder>(), map);
    }

    private void initializeBranchAdapter(){

        branchMarkerAdapter = new BranchMapMarkerAdapter(getActivity(), new ArrayList<Branch>(), map);
    }

    private void initializeWarehouseAdapter(){

        warehouseMarkerAdapter = new WarehouseMarkerAdapter(getActivity(), new ArrayList<Warehouse>(), map);
    }

}
