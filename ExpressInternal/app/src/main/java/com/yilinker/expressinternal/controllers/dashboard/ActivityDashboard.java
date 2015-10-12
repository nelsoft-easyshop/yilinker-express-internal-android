package com.yilinker.expressinternal.controllers.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.base.BaseApplication;
import com.yilinker.core.constants.ErrorMessages;
import com.yilinker.core.imageloader.VolleyImageLoader;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.Login;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.APIConstant;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.cashmanagement.ActivityCashManagement;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.gcm.RegistrationIntentService;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.service.LocationService;

import java.util.ArrayList;
import java.util.List;

public class ActivityDashboard extends AppCompatActivity implements View.OnClickListener, FragmentNavigationDrawer.NavigationDrawerCallbacks, ResponseHandler {

    private static final String TAG = "ActivityDashboard";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private static final int REQUEST_GET_INFO = 1000;
    private static final int REQUEST_GET_OPEN_JO = 1001;

    private View btnJobOrders;
    private TextView tvDelivery;
    private  TextView tvPickup;
    private TextView tvDropoff;
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

    private int currentRequest;
    private boolean isRetrievingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        registrGCM();

        setUpActionBar();
        initViews();

        ApplicationClass appClass = (ApplicationClass)BaseApplication.getInstance();

        requestQueue = appClass.getRequestQueue();

        //Start location service
        appClass.startLocationService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestRiderInfo();
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

                requestOpenJOSummary();

                break;

            case REQUEST_GET_OPEN_JO:

                if(object != null){

                    handleGetJobOrders((List<com.yilinker.core.model.express.internal.JobOrder>) object);
                    resetJOSummary();
                    rlProgress.setVisibility(View.GONE);
                }

                break;

            case ApplicationClass.REQUEST_CODE_REFRESH_TOKEN:

                Login login = (Login) object;

                BaseApplication appClass = ApplicationClass.getInstance();
                appClass.saveAccessToken(login.getAccess_token());
                appClass.saveRefreshToken(login.getRefresh_token());

                if(currentRequest == REQUEST_GET_INFO){

                    requestRiderInfo();
                }
                else{

                    requestOpenJOSummary();
                }

                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {

        if(message.equalsIgnoreCase(ErrorMessages.ERR_EXPIRED_TOKEN)){

            if(!isRetrievingToken) {
                isRetrievingToken = true;
                ApplicationClass.refreshToken(this);
                return;
            }
        }

        if(!message.equalsIgnoreCase(APIConstant.ERR_NO_ENTRIES_FOUND)) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        rlProgress.setVisibility(View.GONE);
    }

    private void initViews(){

        btnJobOrders = findViewById(R.id.btnJobOrders);
        tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        tvDropoff = (TextView) findViewById(R.id.tvDropoff);
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

        currentRequest = REQUEST_GET_INFO;
    }

    private void requestOpenJOSummary(){

        rlProgress.setVisibility(View.VISIBLE);
        Request request = JobOrderAPI.getJobOrders(REQUEST_GET_OPEN_JO, "Open", false, this);
        request.setTag(ApplicationClass.REQUEST_TAG);
        requestQueue.add(request);

        currentRequest = REQUEST_GET_OPEN_JO;
    }

    private void resetRiderInfo(){

        tvDelivery.setText(String.valueOf(rider.getCurrentDeliveryJO()));
        tvPickup.setText(String.valueOf(rider.getCurrentPickupJO()));
//        tvDropoff.setText(String.format("₱%.02f", rider.getTotalEarning()));
        tvDropoff.setText(String.valueOf(rider.getCurrentDropoff()));
        tvTotal.setText(String.valueOf(rider.getCompletedJO()));
        tvOnHand.setText(String.format("₱%.02f", rider.getCashOnHand()));

        int remainingJO = rider.getCurrentDeliveryJO() + rider.getCurrentPickupJO() + rider.getCurrentDropoff() - rider.getCompletedJO();
        tvToday.setText(String.valueOf(remainingJO));

        ImageLoader imageLoader = VolleyImageLoader.getInstance(getApplicationContext()).getImageLoader();
        ivUser.setImageUrl(rider.getImageUrl(), imageLoader);
    }

    private void resetJOSummary(){

        //temp
        int pickUpCount = 0;
        int deliveryCount = 0;
        for(JobOrder item : jobOrders){

            if(item.getType().equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP)){

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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registrGCM(){

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }

    }


}