package com.yilinker.expressinternal.controllers.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.imageloader.VolleyImageLoader;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.cashmanagement.ActivityCashManagement;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActivityDashboard extends AppCompatActivity implements View.OnClickListener, FragmentNavigationDrawer.NavigationDrawerCallbacks, ResponseHandler {

    private static final int REQUEST_GET_INFO = 1000;
    private static final int REQUEST_GET_OPEN_JO = 1001;

    private View btnJobOrders;
    private TextView tvDelivery;
    private  TextView tvPickup;
    private TextView tvEarnings;
    private TextView tvToday;
    private TextView tvTotal;
    private TextView tvOnHand;
    private TextView tvTotalJO;
    private TextView tvTotalPickup;
    private TextView tvTotalDelivery;
    private NetworkImageView ivUser;
    private RelativeLayout rlProgress;

    private FragmentNavigationDrawer mNavigationDrawerFragment;
    private Toolbar toolbar;

    private RequestQueue requestQueue;

    private Rider rider;
    private ArrayList<JobOrder> jobOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setUpActionBar();
        initViews();

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestRiderInfo();
        requestOpenJOSummary();

    }

    @Override
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if(position == 1){

            showCashManagement();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnJobOrders:

                goToJobOrderList();
                break;

        }
    }

    private void goToJobOrderList(){

        Intent intent = new Intent(ActivityDashboard.this, ActivityJobOrderList.class);
        intent.putParcelableArrayListExtra(ActivityJobOrderList.ARG_OPEN_JO, jobOrders);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {

        switch (requestCode){

            case REQUEST_GET_INFO:

                rider = new Rider((com.yilinker.core.model.express.internal.Rider)object);
                resetRiderInfo();
                break;

            case REQUEST_GET_OPEN_JO:

                if(object != null){

                handleGetJobOrders((List<com.yilinker.core.model.express.internal.JobOrder>) object);
                resetJOSummary();

            }
            break;

        }


        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(int requestCode, String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    private void initViews(){

        btnJobOrders = findViewById(R.id.btnJobOrders);
        tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        tvEarnings = (TextView) findViewById(R.id.tvEarning);
        tvOnHand = (TextView) findViewById(R.id.tvOnHand);
        tvPickup = (TextView) findViewById(R.id.tvPickup);
        tvToday = (TextView) findViewById(R.id.tvToday);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTotalDelivery = (TextView) findViewById(R.id.tvTotalDelivery);
        tvTotalJO = (TextView) findViewById(R.id.tvTotalJO);
        tvTotalPickup =  (TextView) findViewById(R.id.tvTotalPickup);
        ivUser = (NetworkImageView) findViewById(R.id.ivLoginImage);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        mNavigationDrawerFragment = (FragmentNavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.fragNavDrawer);
        mNavigationDrawerFragment.setUp(R.id.fragNavDrawer, (DrawerLayout) findViewById(R.id.drawer_layout));


        btnJobOrders.setOnClickListener(this);
    }

    private void setUpActionBar(){

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");

    }

    private void showCashManagement(){

        Intent intent = new Intent(ActivityDashboard.this, ActivityCashManagement.class);
        startActivity(intent);

    }

    private void requestRiderInfo(){

        rlProgress.setVisibility(View.VISIBLE);

        Request request = RiderAPI.getRiderInfo(REQUEST_GET_INFO, this);
        request.setTag(ApplicationClass.REQUEST_TAG);
        requestQueue.add(request);
    }

    private void requestOpenJOSummary(){

        rlProgress.setVisibility(View.VISIBLE);
        Request request = JobOrderAPI.getJobOrders(REQUEST_GET_OPEN_JO, "Open", this);
        request.setTag(ApplicationClass.REQUEST_TAG);
        requestQueue.add(request);
    }

    private void resetRiderInfo(){

        tvDelivery.setText(String.valueOf(rider.getCurrentDeliveryJO()));
        tvPickup.setText(String.valueOf(rider.getCurrentPickupJO()));
        tvEarnings.setText(String.format("₱%.02f", rider.getTotalEarning()));
        tvTotal.setText(String.valueOf(rider.getCompletedJO()));
        tvOnHand.setText(String.format("₱%.02f", rider.getCashOnHand()));

        int remainingJO = rider.getCurrentDeliveryJO() + rider.getCurrentPickupJO() - rider.getCompletedJO();
        tvToday.setText(String.valueOf(remainingJO));

        ImageLoader imageLoader = VolleyImageLoader.getInstance(getApplicationContext()).getImageLoader();
        ivUser.setImageUrl(rider.getImageUrl(), imageLoader);
    }

    private void resetJOSummary(){

        //temp
        int pickUpCount = 0;
        int deliveryCount = 0;
        for(JobOrder item : jobOrders){

            if(item.getType().equalsIgnoreCase("pickup")){

                pickUpCount += 1;
            }
            else{

                deliveryCount += 1;
            }
        }

        tvTotalJO.setText(String.valueOf(jobOrders.size()));
        tvTotalPickup.setText(String.valueOf(pickUpCount));
        tvTotalDelivery.setText(String.valueOf(deliveryCount));

    }

    private void handleGetJobOrders(List<com.yilinker.core.model.express.internal.JobOrder> list){

        jobOrders = new ArrayList<>();
        JobOrder jobOrder = null;
        for(com.yilinker.core.model.express.internal.JobOrder item : list){

            jobOrder = new JobOrder(item);

            jobOrders.add(jobOrder);
        }
    }


}