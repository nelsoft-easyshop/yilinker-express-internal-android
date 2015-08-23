package com.yilinker.expressinternal.controllers.joborderlist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.adapters.AdapterTab;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityJobOderDetail;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityProblematic;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityScanner;
import com.yilinker.expressinternal.interfaces.MenuItemClickListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.TabModel;
import com.yilinker.expressinternal.utilities.DrawableHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ActivityJobOrderList extends BaseActivity implements TabItemClickListener, ResponseHandler, View.OnClickListener, MenuItemClickListener, RecyclerViewClickListener<JobOrder>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //For API requests
    private static final int REQUEST_GET_OPEN= 1000;
    private static final int REQUEST_GET_CURRENT = 1001;
    private static final int REQUEST_GET_COMPLETE = 1002;
    private static final int REQUEST_GET_PROBLEMATIC = 1003;

    //For Tabs
    private static final int TAB_OPEN = 0;
    private static final int TAB_CURRENT = 1;
    private static final int TAB_COMPLETE = 2;
    private static final int TAB_PROBLEMATIC = 3;

    private static final int VIEW_LIST = 1;
    private static final int VIEW_MAP = 2;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ViewSwitcher viewSwitcher;
    private RecyclerView rvTab;

    private AdapterTab adapterTab;

    private List<TabModel> tabItems;
    private List<Object> listObjects;

    //For JO list
    private AdapterJobOrderList adapterJobOrderList;
    private List<JobOrder> jobOrderList;
    private RecyclerView rvJobOrder;

    //For menu
    private AdapterJobOrderMenu adapterJobOrderMenu;
    private List<String> menuItems;

    //For View Switcher
    private int currentView = VIEW_LIST;

    //For maps
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private View warehouseMarker;
    private TextView tvDelivery;
    private TextView tvPickup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderlist);

        initViews();

        buildGoogleApiClient();

        setUpMapIfNeeded();

        requestData(TAB_OPEN);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }


    private void initViews(){

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        rvTab = (RecyclerView) findViewById(R.id.rvTab);

        //Set tab items
        setTab();

        setMenu();

        setListAdapter();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if(currentView == VIEW_MAP) {

            mMap.clear();

            LatLng markerLocation = null;
            for (JobOrder item : jobOrderList){

                markerLocation = new LatLng(item.getLatitude(), item.getLongitude());

                mMap.addMarker(new MarkerOptions().position(markerLocation).title("Marker")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_joborder_pickup));

            }

            //For warehouse
            if(warehouseMarker == null){

                setWarehousePin();
            }

            //Temp
            LatLng warehouseLocation = new LatLng(14.122323, 121.34232);
            mMap.addMarker(new MarkerOptions().position(warehouseLocation).title("Marker")).setIcon(BitmapDescriptorFactory.fromBitmap(DrawableHelper.createDrawableFromView(getWindowManager(), warehouseMarker)));


            //Center map to rider's location
            LatLng location = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title("Marker")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_current_location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

        }

    }


    //Set warehouse pin
    private void setWarehousePin(){

        warehouseMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pin_warehouse, null);
        tvDelivery = (TextView) warehouseMarker.findViewById(R.id.tvDelivery);
        tvPickup = (TextView) warehouseMarker.findViewById(R.id.tvPickup);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onTabItemClick(int position) {

        requestData(position);

        //temp
        reloadList(position);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

    }

    @Override
    public void onFailed(int requestCode, String message) {

    }

    //Request data from the server
    private void requestData(int requestCode){



    }

    //Reload the list
    private void reloadList(int type){

        adapterJobOrderList.stopTimer();

        switch (type){

            case TAB_OPEN:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_OPEN ,this);
                break;

            case TAB_CURRENT:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_CURRENT ,this);
                break;

            case TAB_COMPLETE:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_COMPLETE ,this);
                break;

            case TAB_PROBLEMATIC:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_PROBLEMATIC ,this);
                break;

        }

        rvJobOrder.setAdapter(adapterJobOrderList);

        //Start timer if the selected tab is for current JO
        if(type == TAB_CURRENT){

            adapterJobOrderList.startTimer();
        }

    }

    //Reload the map
    private void reloadMap(){


        //TODO Add markers
        setUpMap();

    }


    private void setTab(){


        tabItems = new ArrayList<TabModel>();
        String[] arrayTabItems = getResources().getStringArray(R.array.tab_items);

        TabModel tab;
        int i = 0;

        for(String item : arrayTabItems){

            tab = new TabModel();
            tab.setId(i);
            tab.setTitle(item);
            tabItems.add(tab);
            i++;
        }

        tabItems.get(0).setIsSelected(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapterTab = new AdapterTab(R.layout.layout_tab_item, tabItems, this);
        adapterTab.setEqualWidth(getWindowManager(), 4);
        rvTab.setLayoutManager(layoutManager);
        rvTab.setAdapter(adapterTab);

    }

    private void setMenu(){

        View customView = getLayoutInflater().inflate(R.layout.layout_main_menu, null);

        String[] arrayMenuItems = getResources().getStringArray(R.array.array_menu_items);
        menuItems = Arrays.asList(arrayMenuItems);

        //Initialize elements
        RecyclerView rvMenu = (RecyclerView) customView.findViewById(R.id.rvMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setLayoutManager(layoutManager);

        adapterJobOrderMenu = new AdapterJobOrderMenu(menuItems, this);

        rvMenu.setAdapter(adapterJobOrderMenu);
        rvMenu.setHasFixedSize(true);

        ImageView ivMenu = (ImageView) customView.findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(this);

        PopupWindow popupWindow = new PopupWindow(customView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


        setMenu(popupWindow);
    }


    @Override
    public void onMenuItemClick(int position) {

        switch (position){

            case 0:

                switchView();
                break;

            case 6:

                showScanner();
                break;

        }

        dismissMenu();
    }

    private void switchView(){

        if(currentView == VIEW_LIST){
            menuItems.set(0, getString(R.string.menu_listview));
            currentView = VIEW_MAP;

            reloadMap();

        }
        else{

            menuItems.set(0, getString(R.string.menu_mapview));
            currentView = VIEW_LIST;
        }

        adapterJobOrderMenu.notifyItemChanged(0);
        viewSwitcher.showNext();
    }

    private void showScanner(){

        Intent intent = new Intent(this, ActivityScanner.class);
        startActivity(intent);

    }

    private void setListAdapter(){

        jobOrderList = new ArrayList<JobOrder>();

        //Temp
        double latitude = 14.1230;
        double longitude = 121.0120;
        for(int i = 0; i < 100; i++){

            JobOrder jo = new JobOrder();
            jo.setJobOrderNo("JO121213131" + i);

            Calendar tempDate = Calendar.getInstance();
            tempDate.set(Calendar.MONTH, 9);
            tempDate.set(Calendar.DATE, 1);
            tempDate.set(Calendar.YEAR, 2015);

            latitude += 0.001;
            longitude += 0.001;

            jo.setLatitude(latitude);
            jo.setLongitude(longitude);
            jo.setEstimatedTimeOfArrival(tempDate.getTime());
            jo.setStatus(JobOrderConstant.JO_COMPLETE);
            jo.setRecipient("Joan Bautista");
            jo.setContactNo("+639951234567");
            jobOrderList.add(jo);
        }

        rvJobOrder = (RecyclerView) findViewById(R.id.rvJobOrder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvJobOrder.setLayoutManager(layoutManager);

        adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_OPEN ,this);


        rvJobOrder.setAdapter(adapterJobOrderList);

    }

    @Override
    public void onItemClick(int position, JobOrder object) {

        Toast.makeText(getApplicationContext(), object.getJobOrderNo(), Toast.LENGTH_LONG).show();

        int currentTab = adapterTab.getCurrentTab();
        switch(currentTab){

            case TAB_OPEN:

                goToDetail(object, currentTab);
                break;

            case TAB_CURRENT:

                goToDetail(object, currentTab);
                break;

            case TAB_COMPLETE:

                goToDetail(object, currentTab);
                break;

            case TAB_PROBLEMATIC:

                reportProblematic(object);
                break;

        }

    }

    //Filter list
    private void filterList(){

    }


    @Override
    public void onConnected(Bundle bundle) {

        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        mGoogleApiClient.connect();

    }

    private void goToDetail(JobOrder jobOrder, int tab){

        Intent intent = null;

        if(tab == TAB_COMPLETE){

            intent = new Intent(ActivityJobOrderList.this, ActivityComplete.class);
            intent.putExtra(ActivityComplete.ARG_JOB_ORDER, jobOrder);
        }
        else{

            intent = new Intent(ActivityJobOrderList.this, ActivityJobOderDetail.class);
            intent.putExtra(ActivityJobOderDetail.ARG_JOB_ORDER, jobOrder);
        }


        startActivity(intent);

    }

    private void reportProblematic(JobOrder jobOrder){

        Intent intent = new Intent(ActivityJobOrderList.this, ActivityProblematic.class);
        intent.putExtra(ActivityProblematic.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);

    }

}
