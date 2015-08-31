package com.yilinker.expressinternal.controllers.joborderlist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.adapters.AdapterTab;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
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
import com.yilinker.expressinternal.utilities.LocationHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ActivityJobOrderList extends BaseActivity implements TabItemClickListener, ResponseHandler, View.OnClickListener, MenuItemClickListener, RecyclerViewClickListener<JobOrder>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    //For passing Open JOs from DashBoard
    public static final String ARG_OPEN_JO = "openJO";

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

    //For Filter
    private static final int NO_FILTER = 0;
    private static final int FILTER_ALL = 1;
    private static final int FILTER_PICKUP = 2;
    private static final int FILTER_DELIVERY = 3;
    private static final int FILTER_CLAIMING = 4;
    private static final int FILTER_DROPOFF =5;

    //View Type
    private static final int VIEW_LIST = 1;
    private static final int VIEW_MAP = 2;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ViewSwitcher viewSwitcher;
    private RecyclerView rvTab;

    private AdapterTab adapterTab;

    private List<TabModel> tabItems;
    private List<Object> listObjects;

    private RelativeLayout rlProgress;

    //For JO list
    private AdapterJobOrderList adapterJobOrderList;
    private List<JobOrder> jobOrderList;            //Filtered job orders and binded with the adapter
    private List<JobOrder> completeList;            //Complete list of job orders
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

    private boolean shouldReload;
    private int filter;

    private RequestQueue requestQueue;

    private HashMap<String, JobOrder> mapMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderlist);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        mapMarkers = new HashMap<>();

        initViews();

        buildGoogleApiClient();

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();

        if(shouldReload){



        }
        else {

            //Data from Dashboard
            getData();
            reloadList(AdapterJobOrderList.TYPE_OPEN);
            shouldReload = true;
        }
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
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }


    private void initViews(){

        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);
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

                mMap.setOnMarkerClickListener(this);

                setUpMap();
                centerToLocation();
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
            mapMarkers.clear();

            LatLng markerLocation = null;
            Marker marker = null;
            MarkerOptions markerOptions = null;
            for (JobOrder item : jobOrderList){

                markerLocation = new LatLng(item.getLatitude(), item.getLongitude());

                markerOptions = new MarkerOptions().position(markerLocation).title("Marker");

                marker = mMap.addMarker(markerOptions);

                int markerResId = R.drawable.pin_joborder_pickup;
                if(item.getType().equalsIgnoreCase("delivery")){
                    markerResId = R.drawable.pin_joborder_delivery;
                }

                marker.setIcon(BitmapDescriptorFactory.fromResource(markerResId));

                mapMarkers.put(marker.getId(), item);

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


            mMap.setMyLocationEnabled(true);
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

        jobOrderList.clear();
        completeList.clear();

        adapterJobOrderList.notifyDataSetChanged();

        requestGetJobOrders();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        if(object != null) {

            List<JobOrder> list = new ArrayList<>();
            List<com.yilinker.core.model.express.internal.JobOrder> listServer = (ArrayList<com.yilinker.core.model.express.internal.JobOrder>) object;

            for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {

                list.add(new JobOrder(item));

            }

            jobOrderList.addAll(list);
            completeList.addAll(list);

            int type = 0;

            switch (requestCode) {

                case REQUEST_GET_OPEN:

                    type = AdapterJobOrderList.TYPE_OPEN;
                    break;

                case REQUEST_GET_CURRENT:

                    type = AdapterJobOrderList.TYPE_CURRENT;
                    break;

                case REQUEST_GET_COMPLETE:

                    type = AdapterJobOrderList.TYPE_COMPLETE;
                    break;

                case REQUEST_GET_PROBLEMATIC:

                    type = AdapterJobOrderList.TYPE_PROBLEMATIC;
                    break;

            }


            reloadList(type);
        }
        else{

            rlProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailed(int requestCode, String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    //Request data from the server
    private void requestGetJobOrders(){

        rlProgress.setVisibility(View.VISIBLE);

        int currentTab = adapterTab.getCurrentTab();
        String type = null;
        int requestCode = 0;


        switch (currentTab){

            case TAB_OPEN:

                type = "Open";
                requestCode = REQUEST_GET_OPEN;
                break;

            case TAB_CURRENT:

                type = "Current";
                requestCode = REQUEST_GET_CURRENT;
                break;

            case TAB_COMPLETE:

                type = "Completed";
                requestCode = REQUEST_GET_COMPLETE;
                break;

            case TAB_PROBLEMATIC:

                type = "Problematic";
                requestCode = REQUEST_GET_PROBLEMATIC;
                break;

        }

        Request request = JobOrderAPI.getJobOrders(requestCode, type, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    //Reload the list
    private void reloadList(int type){

        adapterJobOrderList.stopTimer();


    switch (type){

        case AdapterJobOrderList.TYPE_OPEN:


            adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_OPEN ,this);
            break;


        case AdapterJobOrderList.TYPE_CURRENT:

            adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_CURRENT ,this);
            break;


        case AdapterJobOrderList.TYPE_COMPLETE:

            adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_COMPLETE ,this);
            break;

        case AdapterJobOrderList.TYPE_PROBLEMATIC:

            adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_PROBLEMATIC ,this);
            break;

    }

        rvJobOrder.setAdapter(adapterJobOrderList);

        //Start timer if the selected tab is for current JO
        if(adapterTab.getCurrentTab() == TAB_CURRENT){

            adapterJobOrderList.startTimer();
        }

        //For Filter
        if(filter != NO_FILTER){

            filterList(filter);
            filter = NO_FILTER;
        }

        setDistance();


        //For Map View
        if(currentView == VIEW_MAP){

            reloadMap();
        }

        rlProgress.setVisibility(View.GONE);
    }

    //Reload the map
    private void reloadMap(){

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

            case 1:

                adapterTab.setCurrentTab(TAB_CURRENT);
                filterList(FILTER_ALL);
                break;

            case 2:

//                filterList(FILTER_PICKUP);
                adapterTab.setCurrentTab(TAB_CURRENT);
                filter = FILTER_PICKUP;
                onTabItemClick(TAB_CURRENT);
                break;

            case 3:

//                filterList((FILTER_DELIVERY));
                adapterTab.setCurrentTab(TAB_CURRENT);
                filter = FILTER_DELIVERY;
                onTabItemClick(TAB_CURRENT);
                break;

            case 4:

//                filterList(FILTER_CLAIMING);
                adapterTab.setCurrentTab(TAB_CURRENT);
                filter = FILTER_CLAIMING;
                onTabItemClick(TAB_CURRENT);
                break;

            case 5:

//                filterList(FILTER_DROPOFF);
                adapterTab.setCurrentTab(TAB_CURRENT);
                filter = FILTER_DROPOFF;
                onTabItemClick(TAB_CURRENT);
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

        completeList = new ArrayList<>();
        jobOrderList = new ArrayList<JobOrder>();

        rvJobOrder = (RecyclerView) findViewById(R.id.rvJobOrder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvJobOrder.setLayoutManager(layoutManager);

        adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_OPEN ,this);

        rvJobOrder.setAdapter(adapterJobOrderList);

    }

    @Override
    public void onItemClick(int position, JobOrder object) {

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
    private void filterList(int type){

        String filter = null;
        switch (type){

            case FILTER_ALL:

                requestGetJobOrders();
                return;

            case FILTER_CLAIMING:

                filter = JobOrderConstant.JO_CURRENT_CLAIMING;
                break;

            case FILTER_DROPOFF:

                filter = JobOrderConstant.JO_CURRENT_DROPOFF;
                break;

            case FILTER_PICKUP:

                filter = JobOrderConstant.JO_CURRENT_PICKUP;
                break;

            case FILTER_DELIVERY:

                filter = JobOrderConstant.JO_CURRENT_DELIVERY;
                break;

        }

        List<JobOrder> result = new ArrayList<>();
        for(JobOrder item : completeList){

            if(item.getStatus().equalsIgnoreCase(filter)){

                result.add(item);
            }

        }

        jobOrderList.clear();
        jobOrderList.addAll(result);
        adapterJobOrderList.notifyDataSetChanged();

        if(currentView == VIEW_MAP){

            reloadMap();
        }

    }


    @Override
    public void onConnected(Bundle bundle) {

        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null && adapterTab.getCurrentTab() == TAB_OPEN ){

            setDistance();
        }

        centerToLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        mGoogleApiClient.connect();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        JobOrder jobOrder = mapMarkers.get(marker.getId());

        if(jobOrder != null){

            goToDetail(jobOrder, adapterTab.getCurrentTab());
        }

        return true;
    }

    private void goToDetail(JobOrder jobOrder, int tab){

        Intent intent = null;

        if(tab == TAB_COMPLETE){

            intent = new Intent(ActivityJobOrderList.this, ActivityComplete.class);
            intent.putExtra(ActivityComplete.ARG_JOB_ORDER, jobOrder);
            intent.putExtra(ActivityComplete.ARG_FROM_HOME, true);
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

    private void getData(){

        Intent intent = getIntent();

        List<JobOrder> list = intent.getParcelableArrayListExtra(ARG_OPEN_JO);

        if(list != null) {

            jobOrderList.addAll(list);
            completeList.addAll(list);
            adapterJobOrderList.notifyDataSetChanged();
        }
        else{

            requestGetJobOrders();
        }

    }

    private void setDistance(){

        if(mLastLocation != null) {
            for (JobOrder item : jobOrderList) {

                item.setDistance(LocationHelper.getDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(), item.getLatitude(), item.getLongitude()));

            }

            adapterJobOrderList.notifyDataSetChanged();

        }
    }

    private void centerToLocation(){

        if(mLastLocation != null) {
            LatLng location = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        }
    }

}
