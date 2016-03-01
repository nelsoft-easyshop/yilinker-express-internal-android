package com.yilinker.expressinternal.controllers.joborderlist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityComplete;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityJobOderDetail;
import com.yilinker.expressinternal.controllers.joborderdetails.ActivityProblematic;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityAcknowledge;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityScanner;
import com.yilinker.expressinternal.controllers.qrscanner.ActivitySingleScanner;
import com.yilinker.expressinternal.dao.SyncDBObject;
import com.yilinker.expressinternal.dao.SyncDBTransaction;
import com.yilinker.expressinternal.interfaces.DialogDismissListener;
import com.yilinker.expressinternal.interfaces.MenuItemClickListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.TabModel;
import com.yilinker.expressinternal.model.Warehouse;
import com.yilinker.expressinternal.utilities.DrawableHelper;
import com.yilinker.expressinternal.utilities.LocationHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ActivityJobOrderList extends BaseActivity implements TabItemClickListener, ResponseHandler, View.OnClickListener, MenuItemClickListener, RecyclerViewClickListener<JobOrder>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, DialogDismissListener {

    //For passing Open JOs from DashBoard
    public static final String ARG_OPEN_JO = "openJO";
    public static final String ARG_CURRENT_LIST = "currentJO";
    private static final String SERVER_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    //For API requests
    private static final int REQUEST_GET_OPEN = 1000;
    private static final int REQUEST_GET_CURRENT = 1001;
    private static final int REQUEST_GET_COMPLETE = 1002;
    private static final int REQUEST_GET_PROBLEMATIC = 1003;
    private static final int REQUEST_GET_WAREHOUSES = 1004;

    //

    public static final int REQUEST_SEARCH_QR_CODE = 8000;


    //For Tabs
    private static final int TAB_OPEN = 0;
    private static final int TAB_CURRENT = 1;
    private static final int TAB_COMPLETED = 2;
    private static final int TAB_PROBLEMATIC = 3;

    //For Filter
    private static final int NO_FILTER = 0;
    private static final int FILTER_ALL = 1;
    private static final int FILTER_PICKUP = 2;
    private static final int FILTER_DELIVERY = 3;
    private static final int FILTER_CLAIMING = 4;
    private static final int FILTER_DROPOFF = 5;

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
    private RelativeLayout rlReload;

    //For JO list
    private AdapterJobOrderList adapterJobOrderList;
    private List<JobOrder> filteredList = new ArrayList<>(); // Filtered joborderlist
    private List<JobOrder> jobOrderList;            //Filtered job orders and binded with the adapter
    private List<JobOrder> completeList;            //Complete list of job orders
    private List<SyncDBObject> requestsList = new ArrayList<>();     //list of failed requests for syncing
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
    private ImageView ivScanQr;


    private EditText etSearchField;
    private TextView tvNoResults;

    private boolean shouldReload;
    private int filter;

    private RequestQueue requestQueue;
    private SyncDBTransaction dbTransaction;

    private HashMap<String, JobOrder> mapMarkers;

    private boolean filterByBranch;
    private boolean isReloading;

    private View actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderlist);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();
        dbTransaction = new SyncDBTransaction(this);
        mapMarkers = new HashMap<>();

        initViews();

        buildGoogleApiClient();

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();

        if (shouldReload) {

            reloadList(adapterTab.getCurrentTab(), false);

        } else {

            //Data from Dashboard
            getData();

            resetTabCount();

            reloadList(AdapterJobOrderList.TYPE_OPEN, false);
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

        int id = v.getId();
        switch (id) {

            case R.id.ivSwitch:

                if (!isReloading) {
                    switchFilter();
                }

                break;

            case R.id.iv_scanTrackingCode:
                showSingleScanner();
                break;

            case R.id.rlReload:

                rlReload.setVisibility(View.GONE);
                requestGetJobOrders();
                break;
        }

    }


    private void initViews() {

        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);
        rlReload = (RelativeLayout) findViewById(R.id.rlReload);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        rvTab = (RecyclerView) findViewById(R.id.rvTab);
        ivScanQr = (ImageView) findViewById(R.id.iv_scanTrackingCode);
        etSearchField = (EditText) findViewById(R.id.et_searchField);
        tvNoResults = (TextView) findViewById(R.id.tvNoResults);


        rlReload.setVisibility(View.GONE);
        rlReload.setOnClickListener(this);

        ivScanQr.setOnClickListener(this);
        etSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    filterJobOrderList(v.getText().toString().trim());
                }

                return false;
            }
        });

        etSearchField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Do nothing
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                filterJobOrderList(keyword);
            }
        });

        //Set tab items
        setTab();

        setMenu();

        setListAdapter();

        setActionBar();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
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
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if (currentView == VIEW_MAP) {

            mMap.clear();
            mapMarkers.clear();

            LatLng markerLocation = null;
            Marker marker = null;
            MarkerOptions markerOptions = null;
            for (JobOrder item : jobOrderList) {

                markerLocation = new LatLng(item.getLatitude(), item.getLongitude());

                markerOptions = new MarkerOptions().position(markerLocation).title("Marker");

                marker = mMap.addMarker(markerOptions);

                int markerResId = R.drawable.pin_joborder_pickup;
                if (item.getType().equalsIgnoreCase(getString(R.string.joborder_list_delivery))) {
                    markerResId = R.drawable.pin_joborder_delivery;
                }

                marker.setIcon(BitmapDescriptorFactory.fromResource(markerResId));

                mapMarkers.put(marker.getId(), item);

            }


//            //Temp
//            LatLng warehouseLocation = new LatLng(14.122323, 121.34232);
//            mMap.addMarker(new MarkerOptions().position(warehouseLocation).title("Marker")).setIcon(BitmapDescriptorFactory.fromBitmap(DrawableHelper.createDrawableFromView(getWindowManager(), warehouseMarker)));

            if (mLastLocation != null) {
                //Center map to rider's location
                LatLng location = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(location).title("Marker")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_current_location));

            }

            mMap.setMyLocationEnabled(true);

            requestWarehouseLocation();
        }

    }


    //Set warehouse pin
    private void setWarehousePin() {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SEARCH_QR_CODE && resultCode == RESULT_OK) {

//            rvJobOrder.findViewHolderForAdapterPosition
//                    (Integer.valueOf(data.getStringExtra(ARG_OPEN_JO))).itemView.performClick();

            etSearchField.setText(data.getStringExtra(ARG_OPEN_JO));

        }
    }

    @Override
    public void onTabItemClick(int position) {

        //Stop previous request
        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);

        //Set visibility of Switch Filter Button
        showSwitchButton(position == TAB_OPEN);

        jobOrderList.clear();
        completeList.clear();
        clearFilter();

        adapterJobOrderList.notifyDataSetChanged();

        rlReload.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);

        requestGetJobOrders();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        if (object != null) {

            int type = 0;

            switch (requestCode) {

                case REQUEST_GET_WAREHOUSES:

//                    List<com.yilinker.core.model.express.internal.Warehouse> list = (List<com.yilinker.core.model.express.internal.Warehouse>) object;
//
//                    List<Warehouse> warehouseList = new ArrayList<>();
//                    Warehouse warehouse = null;
//
//                    for(com.yilinker.core.model.express.internal.Warehouse item : list){
//                        warehouse = new Warehouse(item);
//                        warehouseList.add(warehouse);
//                    }

                    //temp
                    List<Warehouse> warehouseList = new ArrayList<>();
                    Warehouse warehouse = new Warehouse((com.yilinker.core.model.express.internal.Warehouse) object);
                    warehouseList.add(warehouse);

                    addWarehousePins(warehouseList);

                    rlProgress.setVisibility(View.GONE);
                    return;

                case REQUEST_GET_OPEN:

                    type = AdapterJobOrderList.TYPE_OPEN;
                    break;

                case REQUEST_GET_CURRENT:

                    requestsList = dbTransaction.getAll(SyncDBObject.class);
                    type = AdapterJobOrderList.TYPE_CURRENT;

                    break;

                case REQUEST_GET_COMPLETE:

                    type = AdapterJobOrderList.TYPE_COMPLETE;
                    break;

                case REQUEST_GET_PROBLEMATIC:

                    type = AdapterJobOrderList.TYPE_PROBLEMATIC;
                    break;


            }

            jobOrderList.clear();
            completeList.clear();


//            List<JobOrder> list = new ArrayList<>();
//            List<com.yilinker.core.model.express.internal.JobOrder> listServer = (ArrayList<com.yilinker.core.model.express.internal.JobOrder>) object;
//
//            for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {
//
//                JobOrder jo = new JobOrder(item);
//
//                //check jo if it's for syncing
//
//                if (requestCode == REQUEST_GET_CURRENT) {
//                    for (int i = 0; i < requestsList.size(); i++) {
//                        if (requestsList.get(i).getId().equals(jo.getJobOrderNo())
//                                || requestsList.get(i).getId().equals(jo.getWaybillNo())) {
//                            if (!requestsList.get(i).isSync())                 //check sync status
//                                jo.setForSyncing(true);
//                        }
//                    }
//
//                }
//
//
//                list.add(jo);
//
//            }
//
//            jobOrderList.addAll(list);
//            completeList.addAll(list);
//
//            //For Tab Count
//            int count = listServer.size();
//            tabItems.get(adapterTab.getCurrentTab()).setCount(count);
//            resetTabCount();
//
//            reloadList(type, false);

            List<com.yilinker.core.model.express.internal.JobOrder> listServer = (ArrayList<com.yilinker.core.model.express.internal.JobOrder>) object;
            createList(listServer, type);


            //Save current JO data to local storage for offline function
            if (requestCode == REQUEST_GET_CURRENT) {
                ApplicationClass.saveLocalCurrentListData(this, listServer); //save to text
            }

        } else {

            rlProgress.setVisibility(View.GONE);
        }

        isReloading = false;

    }

    @Override
    protected void handleRefreshToken() {

        int currentRequest = getCurrentRequest();

        switch (currentRequest) {

            case REQUEST_GET_WAREHOUSES:

                requestWarehouseLocation();
                break;

            default:
                requestGetJobOrders();
        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        if (!message.equalsIgnoreCase(APIConstant.ERR_NO_ENTRIES_FOUND)) {


            switch (requestCode) {

                case REQUEST_GET_OPEN:

                    //TODO: Local list for offline viewing
//                loadLocalJobOrderList(AdapterJobOrderList.TYPE_OPEN);
                    rlReload.setVisibility(View.VISIBLE);

                    break;

                case REQUEST_GET_CURRENT:

                    List<com.yilinker.core.model.express.internal.JobOrder> listLocal = ApplicationClass.getLocalData(this);
                    createList(listLocal, AdapterJobOrderList.TYPE_CURRENT);
                    rlReload.setVisibility(View.GONE);

                    break;

                case REQUEST_GET_COMPLETE:

                    //TODO: Local list for offline viewing
//                loadLocalJobOrderList(AdapterJobOrderList.TYPE_COMPLETE);
                    rlReload.setVisibility(View.VISIBLE);

                    break;

                case REQUEST_GET_PROBLEMATIC:

                    //TODO: Local list for offline viewing
//                loadLocalJobOrderList(AdapterJobOrderList.TYPE_PROBLEMATIC);
                    rlReload.setVisibility(View.VISIBLE);

                    break;

                default:
                    //TODO Load a view showing no Entries Found message
//                    rlReload.setVisibility(View.VISIBLE);
                    break;

            }
            tvNoResults.setVisibility(View.GONE);
            rlProgress.setVisibility(View.GONE);

        } else {
            rlProgress.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
            isReloading = false;
        }

    }

    //Request data from the server
    private void requestGetJobOrders() {

        rlProgress.setVisibility(View.VISIBLE);

        int currentTab = adapterTab.getCurrentTab();
        String type = null;
        int requestCode = 0;


        switch (currentTab) {

            case TAB_OPEN:

                type = getString(R.string.job_order_list_open);
                requestCode = REQUEST_GET_OPEN;
                break;

            case TAB_CURRENT:

                type = getString(R.string.job_order_list_current);
                requestCode = REQUEST_GET_CURRENT;
                break;

            case TAB_COMPLETED:

                type = getString(R.string.job_order_list_completed);
                requestCode = REQUEST_GET_COMPLETE;
                break;

            case TAB_PROBLEMATIC:

                type = getString(R.string.job_order_list_problematic);
                requestCode = REQUEST_GET_PROBLEMATIC;
                break;

        }

        Request request = null;

        if (currentTab == TAB_OPEN) {

            request = JobOrderAPI.getJobOrders(requestCode, type, filterByBranch, this);
        } else {

            request = JobOrderAPI.getJobOrders(requestCode, type, true, this);
        }

        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void filterJobOrderList(String keyword) {

        filteredList.clear();

        for (int i = 0; i < jobOrderList.size(); i++) {

            if ((jobOrderList.get(i).getWaybillNo().toLowerCase()).contains(keyword.toLowerCase())) {
                filteredList.add(jobOrderList.get(i));
            }
        }

        int type = 0;

        if (adapterTab.getCurrentTab() == 0) {
            type = AdapterJobOrderList.TYPE_OPEN;
        } else if (adapterTab.getCurrentTab() == 1) {
            type = AdapterJobOrderList.TYPE_CURRENT;
        } else if (adapterTab.getCurrentTab() == 2) {
            type = AdapterJobOrderList.TYPE_COMPLETE;
        } else {
            type = AdapterJobOrderList.TYPE_PROBLEMATIC;
        }

        reloadList(type, true);


    }


    //Reload the list
    private void reloadList(int type, boolean isFiltered) {


        adapterJobOrderList.stopTimer();

        List<JobOrder> tempOrigJobOrderList = jobOrderList;

        if (isFiltered) {
            jobOrderList = filteredList;
        }


        switch (type) {

            case AdapterJobOrderList.TYPE_OPEN:


                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_OPEN, this);
                break;


            case AdapterJobOrderList.TYPE_CURRENT:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_CURRENT, this);
                break;


            case AdapterJobOrderList.TYPE_COMPLETE:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_COMPLETE, this);
                break;

            case AdapterJobOrderList.TYPE_PROBLEMATIC:

                adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_PROBLEMATIC, this);
                break;

        }

        rvJobOrder.setAdapter(adapterJobOrderList);

//        By Joan: I have removed this part since the timer is hidden
//        //Start timer if the selected tab is for current JO
//        if (adapterTab.getCurrentTab() == TAB_CURRENT || adapterTab.getCurrentTab() == TAB_PROBLEMATIC) {
//
//            adapterJobOrderList.startTimer();
//        }

        //For Filter
        if (filter != NO_FILTER) {

            filterList(filter);
            filter = NO_FILTER;
        }

        setDistance();


        //For Map View
        if (currentView == VIEW_MAP) {

            reloadMap();
        }

        // show no results found text when waybill number does not exist on list
        if (jobOrderList.size() < 1) {
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            tvNoResults.setVisibility(View.GONE);
        }

        if (isFiltered) {

            adapterJobOrderList.notifyDataSetChanged();
            jobOrderList = tempOrigJobOrderList;

        }

        if (!etSearchField.getText().toString().equals("") && !isFiltered) {

            filterJobOrderList(etSearchField.getText().toString());

        }

        rlProgress.setVisibility(View.GONE);
        rlReload.setVisibility(View.GONE);
    }

    //Reload the map
    private void reloadMap() {

        if (LocationHelper.isLocationServicesEnabled(getApplicationContext())) {
            setUpMap();
        } else {

            LocationHelper.showLocationErrorDialog(ActivityJobOrderList.this, this);
        }

    }


    private void setTab() {


        tabItems = new ArrayList<TabModel>();
        String[] arrayTabItems = getResources().getStringArray(R.array.tab_items);

        TabModel tab;
        int i = 0;

        for (String item : arrayTabItems) {

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

    private void setMenu() {

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


        switch (position) {

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

//            case 4:
//
////                filterList(FILTER_CLAIMING);
//                adapterTab.setCurrentTab(TAB_CURRENT);
//                filter = FILTER_CLAIMING;
//                onTabItemClick(TAB_CURRENT);
//                break;

            case 4:

//                filterList(FILTER_DROPOFF);
                adapterTab.setCurrentTab(TAB_CURRENT);
                filter = FILTER_DROPOFF;
                onTabItemClick(TAB_CURRENT);
                break;

            case 5:

                showScanner();
                break;

            case 6:

                goToAcknowledgeScreen();
                break;

        }

        dismissMenu();
    }

    private void goToAcknowledgeScreen() {

        Intent intent = new Intent(ActivityJobOrderList.this, ActivityAcknowledge.class);
        startActivity(intent);

    }

    private void switchView() {

        if (currentView == VIEW_LIST) {
            menuItems.set(0, getString(R.string.menu_listview));
            currentView = VIEW_MAP;

            reloadMap();

        } else {

            menuItems.set(0, getString(R.string.menu_mapview));
            currentView = VIEW_LIST;
        }

        adapterJobOrderMenu.notifyItemChanged(0);
        viewSwitcher.showNext();
    }

    private void showSingleScanner() {

        Intent intent = new Intent(this, ActivitySingleScanner.class);
        intent.putParcelableArrayListExtra(ARG_CURRENT_LIST, (ArrayList<? extends Parcelable>) jobOrderList);
        startActivityForResult(intent, REQUEST_SEARCH_QR_CODE);

    }

    private void showScanner() {

        Intent intent = new Intent(this, ActivityScanner.class);
        startActivity(intent);

    }

    private void setListAdapter() {

        completeList = new ArrayList<>();
        jobOrderList = new ArrayList<JobOrder>();

        rvJobOrder = (RecyclerView) findViewById(R.id.rvJobOrder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvJobOrder.setLayoutManager(layoutManager);

        adapterJobOrderList = new AdapterJobOrderList(jobOrderList, AdapterJobOrderList.TYPE_OPEN, this);

        rvJobOrder.setAdapter(adapterJobOrderList);

    }

    @Override
    public void onItemClick(int position, JobOrder object) {

        int currentTab = adapterTab.getCurrentTab();

        goToDetail(object, currentTab);
    }

    //Filter list
    private void filterList(int type) {

        String filter = null;
        switch (type) {

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
        for (JobOrder item : completeList) {

            if (item.getStatus().equalsIgnoreCase(filter)) {

                result.add(item);
            }

        }

        jobOrderList.clear();
        jobOrderList.addAll(result);
        adapterJobOrderList.notifyDataSetChanged();

        if (currentView == VIEW_MAP) {

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


        if (mLastLocation != null && adapterTab.getCurrentTab() == TAB_OPEN) {

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

        if (jobOrder != null) {

            goToDetail(jobOrder, adapterTab.getCurrentTab());
        }

        return true;
    }

    private void goToDetail(JobOrder jobOrder, int tab) {

        Intent intent = null;

        if (tab == TAB_COMPLETED) {

            intent = new Intent(ActivityJobOrderList.this, ActivityComplete.class);
            intent.putExtra(ActivityComplete.ARG_JOB_ORDER, jobOrder);
            intent.putExtra(ActivityComplete.ARG_FROM_HOME, true);

        } else {

            intent = new Intent(ActivityJobOrderList.this, ActivityJobOderDetail.class);
            intent.putExtra(ActivityJobOderDetail.ARG_JOB_ORDER, jobOrder);
            intent.putExtra(ActivityJobOderDetail.ARG_CURRENT_STATUS, adapterTab.getCurrentTab());
        }


        startActivity(intent);

    }

    private void reportProblematic(JobOrder jobOrder) {

        Intent intent = new Intent(ActivityJobOrderList.this, ActivityProblematic.class);
        intent.putExtra(ActivityProblematic.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);

    }

    private void getData() {

        Intent intent = getIntent();

        List<JobOrder> list = intent.getParcelableArrayListExtra(ARG_OPEN_JO);

        if (list != null) {

            jobOrderList.addAll(list);
            completeList.addAll(list);
            adapterJobOrderList.notifyDataSetChanged();
        } else {

            requestGetJobOrders();
        }

    }

    private void setDistance() {

        if (mLastLocation != null) {
            for (JobOrder item : jobOrderList) {

                if (item.getLatitude() > 0 && item.getLongitude() > 0)
                    item.setDistance(LocationHelper.getDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(), item.getLatitude(), item.getLongitude()));

            }

            adapterJobOrderList.notifyDataSetChanged();

        }
    }

    private void centerToLocation() {

        if (mLastLocation != null) {
            LatLng location = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        }
    }

    private void requestWarehouseLocation() {

        rlProgress.setVisibility(View.VISIBLE);

        Request request = JobOrderAPI.getWarehouses(REQUEST_GET_WAREHOUSES, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void addWarehousePins(List<Warehouse> list) {

        //For warehouse
        if (warehouseMarker == null) {

            setWarehousePin();
        }

        if (list != null) {

            LatLng warehouseLocation = null;
            TextView tvPickup = null;
            TextView tvClaiming = null;

            for (Warehouse item : list) {

                warehouseLocation = new LatLng(item.getLatitude(), item.getLongitude());

                tvPickup = (TextView) warehouseMarker.findViewById(R.id.tvPickup);
                tvClaiming = (TextView) warehouseMarker.findViewById(R.id.tvDelivery);

                tvPickup.setText(String.valueOf(item.getForPickup()));
                tvClaiming.setText(String.valueOf(item.getForClaiming()));

                mMap.addMarker(new MarkerOptions().position(warehouseLocation).title("Warehouse")).setIcon(BitmapDescriptorFactory.fromBitmap(DrawableHelper.createDrawableFromView(getWindowManager(), warehouseMarker)));

            }

        }


    }

    private void resetTabCount() {

        TabModel tab = tabItems.get(adapterTab.getCurrentTab());
        tab.setCount(jobOrderList.size());
        adapterTab.notifyItemChanged(adapterTab.getCurrentTab());

    }

    private void setActionBar() {

        actionBar = getActionBarView();

        Button ivSwitch = (Button) actionBar.findViewById(R.id.ivSwitch);
        ivSwitch.setOnClickListener(this);

        ivSwitch.setVisibility(View.VISIBLE);

    }

    private void switchFilter() {

        isReloading = true;

        filterByBranch = !filterByBranch;
        int resId = R.string.filter_area;
        int resColor = R.drawable.bg_btn_marigold_rounded;

        if (filterByBranch) {
            resId = R.string.filter_branch;
            resColor = R.drawable.bg_btn_orangeyellow_rounded;
        }

        Button ivSwitch = (Button) actionBar.findViewById(R.id.ivSwitch);

        ivSwitch.setBackgroundResource(resColor);
        ivSwitch.setText(getString(resId));

        requestGetJobOrders();

    }

    private void clearFilter() {

        etSearchField.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


    }

    private void showSwitchButton(boolean isShown) {

        Button ivSwitch = (Button) actionBar.findViewById(R.id.ivSwitch);

        int visibility = View.GONE;
        if (isShown) {
            visibility = View.VISIBLE;
        }

        ivSwitch.setVisibility(visibility);
    }


    @Override
    public void onDialogDismiss(int requestCode, Bundle bundle) {

        switch (requestCode) {

            case LocationHelper.REQUEST_DIALOG_LOCATION_ERROR:

                switchView();
                break;

        }

    }

//    private void loadLocalJobOrderList() {
//
////        List<JobOrder> list = new ArrayList<>();
////        requestsList = dbTransaction.getAll(SyncDBObject.class);
////        List<com.yilinker.core.model.express.internal.JobOrder> listServer = ApplicationClass.getLocalData(this);
////
////        for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {
////
////            JobOrder jo = new JobOrder(item);
////
////            //check jo if it's for syncing
////                for (int i = 0; i < requestsList.size(); i++) {
////                    if (requestsList.get(i).getId().equals(jo.getJobOrderNo())
////                            || requestsList.get(i).getId().equals(jo.getWaybillNo())) {
////                        if (!requestsList.get(i).isSync())                 //check sync status
////                            jo.setForSyncing(true);
////                    }
////                }
////
////            list.add(jo);
////
////        }
//
//
//        List<JobOrder> list = new ArrayList<>();
//        List<com.yilinker.core.model.express.internal.JobOrder> listServer = ApplicationClass.getLocalData(this);
//
//        //Create search dictionary
//        String searchDictionary = getSearchDictionary();
//
//        for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {
//
//            JobOrder jo = new JobOrder(item);
//
//            //check jo if it's for syncing
//            String waybillNo = jo.getWaybillNo();
//            String joNumber = jo.getJobOrderNo();
//
//            if(searchDictionary.contains(waybillNo) || searchDictionary.contains(joNumber)){
//                jo.setForSyncing(true);
//            }
//
//            list.add(jo);
//
//        }
//
//        jobOrderList.addAll(list);
//
//        //For Tab Count
//        int count = listServer.size();
//        tabItems.get(adapterTab.getCurrentTab()).setCount(count);
//        resetTabCount();
//
//        reloadList(AdapterJobOrderList.TYPE_CURRENT, false);
//
//    }

    private void createList(List<com.yilinker.core.model.express.internal.JobOrder> listServer, int type) {

        List<JobOrder> list = new ArrayList<>();

        //Create search dictionary
        String searchDictionary = getSearchDictionary();

        for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {

            JobOrder jo = new JobOrder(item);


            if (searchDictionary != null) {
                //check jo if it's for syncing
                String waybillNo = String.format("|%s|", jo.getWaybillNo());
                String joNumber = String.format("|%s|", jo.getJobOrderNo());

                if (searchDictionary.contains(waybillNo) || searchDictionary.contains(joNumber)) {
                    jo.setForSyncing(true);
                }
            }

            list.add(jo);

        }

        jobOrderList.addAll(list);
        completeList.addAll(list);

        //For Tab Count
        int count = listServer.size();
        tabItems.get(adapterTab.getCurrentTab()).setCount(count);
        resetTabCount();

        reloadList(type, false);

    }

    private String getSearchDictionary() {

        String dictionary = null;

        requestsList = dbTransaction.getAll(SyncDBObject.class);

        if (requestsList.size() > 0) {

            StringBuilder syncBuilder = new StringBuilder();
            syncBuilder.append("|");

            for (SyncDBObject object : requestsList) {

                if (!object.isSync()) {

                    syncBuilder.append(object.getId());
                    syncBuilder.append("|");
                }
            }

            dictionary = syncBuilder.toString();

        }

        return dictionary;

    }


}
