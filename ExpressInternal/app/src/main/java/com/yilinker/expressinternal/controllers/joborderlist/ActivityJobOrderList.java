package com.yilinker.expressinternal.controllers.joborderlist;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.controllers.qrscanner.ActivityScanner;
import com.yilinker.expressinternal.interfaces.MenuItemClickListener;
import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.TabModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ActivityJobOrderList extends BaseActivity implements TabItemClickListener, ResponseHandler, View.OnClickListener, MenuItemClickListener, RecyclerViewClickListener<JobOrder>{

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

    private AdapterJobOrderTab adapterJobOrderTab;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderlist);

        initViews();

        setUpMapIfNeeded();

        requestData(TAB_OPEN);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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
        adapterJobOrderTab = new AdapterJobOrderTab(R.layout.layout_tab_item, tabItems, this);
        rvTab.setLayoutManager(layoutManager);
        rvTab.setAdapter(adapterJobOrderTab);

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
        for(int i = 0; i < 100; i++){

            JobOrder jo = new JobOrder();
            jo.setJobOrderNo("JO121213131" + i);

            Calendar tempDate = Calendar.getInstance();
            tempDate.set(Calendar.MONTH, 9);
            tempDate.set(Calendar.DATE, 1);
            tempDate.set(Calendar.YEAR, 2015);

            jo.setEstimatedTimeOfArrival(tempDate.getTime());
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

    }

    //Filter list
    private void filterList(){

    }


}
