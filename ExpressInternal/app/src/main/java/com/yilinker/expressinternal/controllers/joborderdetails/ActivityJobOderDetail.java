package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.model.express.internal.ProblematicJobOrder;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.contact.ActivityContact;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.images.ImagePagerAdapter;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.controllers.navigation.ActivityNavigation;
import com.yilinker.expressinternal.controllers.qrcode.ActivityQRCode;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityJobOderDetail extends BaseActivity implements ResponseHandler{

    public static final String ARG_CURRENT_STATUS = "currentStatus";
    public static final String ARG_JOB_ORDER = "jobOrder";

    private static final String ETA_DATE_FORMAT = "hh:mm aa";

    private static final int STATUS_OPEN = 0;
    private static final int STATUS_CURRENT = 1;
    private static final int STATUS_COMPLETED = 2;
    private static final int STATUS_PROBLEMATIC = 3;

    private static final int REQUEST_UPDATE = 1000;
    private static final int REQUEST_OUT_OF_STOCK = 1001;
    private static final int REQUEST_ACCEPT_JOB = 1002;

    private LinearLayout llContainer;
    private RelativeLayout rlProgress;

    private TextView tvJobOrderNo;
    private TextView tvStatus;
    private TextView tvItem;
    private TextView tvRecipient;
    private TextView tvContactNo;
    private TextView tvETA;
    private TextView tvDistance;
    private TextView tvType;

    private Button btnPositive;
    private Button btnNegative;

    //For Side Menu
    private ImageButton btnContact;
    private ImageButton btnImage;
    private ImageButton btnNavigation;
    private ImageButton btnQrCode;

    //For Pickup
    private TextView tvPickupAddress;
    private TextView tvDropoffAddress;

    //For Delivery
    private TextView tvDeliveryAddress;

    //For Problematic
    private TextView tvCSRName;
    private TextView tvProblemType;
    private TextView tvRemarks;
    private TextView tvViewImages;

    private int currentStatus;
    private JobOrder jobOrder;
    private String newStatus;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderdetail);

        requestQueue = ApplicationClass.getInstance().getRequestQueue();

        //Get data passed by the previous activity
        getData();

        initViews();

        bindViewData();

    }

    private void initViews(){

        llContainer = (LinearLayout) findViewById(R.id.llContainer);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);

        setContentView();

        tvJobOrderNo = (TextView) findViewById(R.id.tvJobOrderNo);
        tvRecipient = (TextView) findViewById(R.id.tvRecipient);
        tvContactNo = (TextView) findViewById(R.id.tvContactNo);
        tvItem = (TextView) findViewById(R.id.tvItem);
        tvETA = (TextView) findViewById(R.id.tvETA);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        //For Delivery
        tvDeliveryAddress = (TextView) findViewById(R.id.tvDeliveryAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);

        //For Pickup
        tvDropoffAddress = (TextView) findViewById(R.id.tvDropoffAddress);
        tvPickupAddress = (TextView) findViewById(R.id.tvPickupAddress);

        //For Problematic
        tvCSRName = (TextView) findViewById(R.id.tvCSRName);
        tvProblemType = (TextView) findViewById(R.id.tvProblemType);
        tvRemarks = (TextView) findViewById(R.id.tvRemarks);
        tvViewImages = (TextView) findViewById(R.id.tvViewImages);

        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);
        btnContact = (ImageButton) findViewById(R.id.btnContact);
        btnImage = (ImageButton) findViewById(R.id.btnImage);
        btnNavigation = (ImageButton) findViewById(R.id.btnMap);
        btnQrCode = (ImageButton) findViewById(R.id.btnQRCode);

        btnNegative.setOnClickListener(this);
        btnPositive.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnNavigation.setOnClickListener(this);
        btnQrCode.setOnClickListener(this);

        setActionBar();

        rlProgress.setVisibility(View.GONE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        requestQueue.cancelAll(ApplicationClass.REQUEST_TAG);
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_UPDATE:

                Toast.makeText(getApplicationContext(), "Job order accepted", Toast.LENGTH_LONG).show();
                goToMainScreen();

                break;

            case REQUEST_ACCEPT_JOB:

                Toast.makeText(getApplicationContext(), "Job order accepted", Toast.LENGTH_LONG).show();
                goToMainScreen();

                break;

            case REQUEST_OUT_OF_STOCK:

                Toast.makeText(getApplicationContext(), "Successfully reported the problem!", Toast.LENGTH_LONG).show();
                finish();
                break;


        }

        rlProgress.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnPositive:

                handlePositiveButtonClick();
                break;

            case R.id.btnNegative:

                handleNegativeButtonClick();
                break;

            case R.id.btnMap:
                showNavigation();
                break;

            case  R.id.btnContact:

                goToContact();
                break;

            case R.id.btnImage:

                showImageGallery(ImagePagerAdapter.TYPE_URL, jobOrder.getImages());
                break;

            case R.id.btnQRCode:

                showQRCode();
                break;

            case R.id.tvViewImages:

                showImageGallery(ImagePagerAdapter.TYPE_URL, jobOrder.getProblematicImages());
                break;


        }
    }

    private void setActionBar(){

        String type = jobOrder.getType();
        String status = jobOrder.getStatus();
        String title = null;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN)){

            title = type;
        }
        else{

            title = status;
        }

        //For Action Bar

        setActionBarTitle(title);

        int actionBarColor = R.color.marigold;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){

            actionBarColor = R.color.orange_red;
        }

        setActionBarBackgroundColor(actionBarColor);

    }

    private void getData(){

        Intent intent = getIntent();
        jobOrder = intent.getParcelableExtra(ARG_JOB_ORDER);
        currentStatus = intent.getIntExtra(ARG_CURRENT_STATUS, STATUS_OPEN);
//        type = jobOrder.getType();

    }

    private void bindViewData(){

//        String type = jobOrder.getType();
//        String status = jobOrder.getStatus();
//
//        if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY)){
//            setOpenDeliveryViews();
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP)){
//            setOpenPickupViews();
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){
//            setCurrentPickupViews();
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){
//            setCurrentDeliveryViews();
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){
//            setClaimingViews();
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){
//            setDropOffViews();
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){
//            setProblematicViews();
//            return;
//        }

        String status = jobOrder.getStatus();
        String type = jobOrder.getType();

        if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY) && currentStatus == STATUS_OPEN){
            setOpenDeliveryViews();
        }
        else if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP) && currentStatus == STATUS_OPEN){
            setOpenPickupViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP) && currentStatus == STATUS_CURRENT){
            setCurrentPickupViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY) && currentStatus == STATUS_CURRENT){
            setCurrentDeliveryViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING) && currentStatus == STATUS_CURRENT) {
            setClaimingViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF) && currentStatus == STATUS_CURRENT){
            setDropOffViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){
            setProblematicViews();
            return;
        }


        //Default
        tvJobOrderNo.setText(jobOrder.getJobOrderNo());
        tvRecipient.setText(jobOrder.getRecipient());

        if(jobOrder.getContactNo() != null)         //temp
            tvContactNo.setText(jobOrder.getContactNo());

        //For Items
        List<String> items  = jobOrder.getItems();
        StringBuilder builder = new StringBuilder();
        for (String item : items){
            builder.append(item);
            builder.append("\n");
        }

//        tvItem.setText(builder.toString());
        tvItem.setText(jobOrder.getPackageDescription());

    }

    private void setOpenDeliveryViews(){

        btnPositive.setText(getString(R.string.joborderdetail_open_accept));
        btnNegative.setText(getString(R.string.joborderdetail_open_decline));

        tvStatus.setText(jobOrder.getStatus());
        tvDistance.setText(String.format("%.02f KM", jobOrder.getDistance() / 1000));
        tvETA.setText(DateUtility.convertDateToString(jobOrder.getEstimatedTimeOfArrival(), ETA_DATE_FORMAT));
        tvDeliveryAddress.setText(jobOrder.getDeliveryAddress());

    }

    private void setOpenPickupViews(){

        btnPositive.setText(getString(R.string.joborderdetail_open_accept));
        btnNegative.setText(getString(R.string.joborderdetail_open_decline));

        tvStatus.setText(jobOrder.getStatus());
        tvDistance.setText(String.format("%.02f KM", jobOrder.getDistance() / 1000));
        tvETA.setText(DateUtility.convertDateToString(jobOrder.getEstimatedTimeOfArrival(), ETA_DATE_FORMAT));
        tvPickupAddress.setText(jobOrder.getPickupAddress());
        tvDropoffAddress.setText(jobOrder.getDropoffAddress());

    }

    private void setCurrentPickupViews(){

        btnPositive.setText(getString(R.string.joborderdetail_pickup));
        btnNegative.setText(getString(R.string.joborderdetail_outofstock));

        tvETA.setText(DateUtility.convertDateToString(jobOrder.getEstimatedTimeOfArrival(), ETA_DATE_FORMAT));
        tvPickupAddress.setText(jobOrder.getPickupAddress());
        tvDropoffAddress.setText(jobOrder.getDropoffAddress());

    }

    private void setCurrentDeliveryViews(){

        btnPositive.setText(getString(R.string.joborderdetail_deliver));
        btnNegative.setText(getString(R.string.joborderdetail_problematic));

        tvETA.setText(DateUtility.convertDateToString(jobOrder.getEstimatedTimeOfArrival(), ETA_DATE_FORMAT));
        tvDeliveryAddress.setText(jobOrder.getDeliveryAddress());

    }

    private void setDropOffViews(){

        btnPositive.setText(getString(R.string.ok));
        btnNegative.setVisibility(View.GONE);


        //Show drop off status
        TextView tvDropOffStatus = (TextView) findViewById(R.id.tvDropoffStatus);

        //TODO Set visibility and text here depending on dropoff status

//        //Hide QR Code button
//        btnQrCode.setVisibility(View.GONE);

        //Hide pickup address
        LinearLayout llPickup = (LinearLayout) findViewById(R.id.llPickup);
        llPickup.setVisibility(View.GONE);


        tvETA.setText(DateUtility.convertDateToString(jobOrder.getEstimatedTimeOfArrival(), ETA_DATE_FORMAT));
        tvDropoffAddress.setText(jobOrder.getDropoffAddress());

    }

    private void setProblematicViews(){

        btnPositive.setVisibility(View.GONE);
        btnNegative.setVisibility(View.GONE);

        tvCSRName.setText(jobOrder.getCsrName());
        tvProblemType.setText(jobOrder.getProblemType());
        tvRemarks.setText(jobOrder.getRemarks());

        ImageButton btnCaution = (ImageButton) findViewById(R.id.btnCaution);
        btnCaution.setVisibility(View.VISIBLE);


        if(jobOrder.getProblematicImages().size() > 0) {
            tvViewImages.setOnClickListener(this);
        }
        else{
            tvViewImages.setVisibility(View.GONE);
        }

    }

    private void setClaimingViews(){

        btnPositive.setText(getString(R.string.joborderdetail_claim));
        btnNegative.setVisibility(View.GONE);

        btnQrCode.setVisibility(View.GONE);

        tvETA.setText(DateUtility.convertDateToString(jobOrder.getEstimatedTimeOfArrival(), ETA_DATE_FORMAT));
        tvPickupAddress.setText(jobOrder.getPickupAddress());
        tvDropoffAddress.setText(jobOrder.getDropoffAddress());

    }

    private void setContentView(){

        //Set the content of the screen
        String status = jobOrder.getStatus();
        String type = jobOrder.getType();
        View contentView = null;
        LayoutInflater inflater = getLayoutInflater();

        if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY) && currentStatus == STATUS_OPEN){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_open_delivery, llContainer, true);

        }
        else if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP) && currentStatus == STATUS_OPEN){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_open_pickup, llContainer, true);

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP) || status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING) || status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_current_pickup, llContainer, true);
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_currentdelivery, llContainer, true);
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_PROBLEMATIC)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_problematic, llContainer, true);
        }

    }

    private void goToChecklist(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityChecklist.class);
        intent.putExtra(ActivityChecklist.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);

    }


    private void goToContact(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityContact.class);
        intent.putExtra(ActivityContact.ARG_NAME, jobOrder.getRecipient());
        intent.putExtra(ActivityContact.ARG_CONTACT_NO, jobOrder.getContactNo());
        startActivity(intent);

    }

    private void showImageGallery(String type, List<String> images){

        if(images.size() > 0) {

            Intent intent = new Intent(ActivityJobOderDetail.this, ActivityImageGallery.class);
            intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, (ArrayList) images);
            intent.putExtra(ActivityImageGallery.ARG_TYPE, type);
            startActivity(intent);
        }
        else{

            Toast.makeText(getApplicationContext(),getString(R.string.joborderdetail_error_no_image), Toast.LENGTH_SHORT).show();
        }
    }

    private void showNavigation(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityNavigation.class);
        intent.putExtra(ActivityNavigation.ARG_DESTINATION_LAT, jobOrder.getLatitude());
        intent.putExtra(ActivityNavigation.ARG_DESTINATION_LONG, jobOrder.getLongitude());
        startActivity(intent);
    }

    private void showQRCode(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityQRCode.class);
        intent.putExtra(ActivityQRCode.ARG_JOB_ORDER, jobOrder);
        startActivity(intent);
    }

    private void handlePositiveButtonClick(){

        String type = jobOrder.getType();
        String status = jobOrder.getStatus();

        if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY) && currentStatus == STATUS_OPEN){

//            requestUpdateStatus(JobOrderConstant.JO_CURRENT_DELIVERY);
            requestAcceptJob(jobOrder.getJobOrderNo());

        }
        else if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP) && currentStatus == STATUS_OPEN){

//            requestUpdateStatus(JobOrderConstant.JO_CURRENT_PICKUP);
            requestAcceptJob(jobOrder.getJobOrderNo());
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            goToChecklist();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            goToChecklist();

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){

            goToChecklist();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){

            onBackPressed();

        }

    }

    private void handleNegativeButtonClick(){

        String type = jobOrder.getType();
        String status = jobOrder.getStatus();

        if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP) && currentStatus == STATUS_OPEN){

            //TODO Show message
            onBackPressed();

        }
        else if(type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY) && currentStatus == STATUS_OPEN){

            //TODO Show message
            onBackPressed();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            showOutOfStockDialog();

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            reportProblematic();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DROPOFF)){


        }

    }

    private void requestUpdateStatus(String newStatus){

        rlProgress.setVisibility(View.VISIBLE);

        this.newStatus = newStatus;
        int requestCode = REQUEST_UPDATE;

        Request request = JobOrderAPI.updateStatus(requestCode, jobOrder.getJobOrderNo(), newStatus, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    private void goToMainScreen(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityJobOrderList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void showOutOfStockDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityJobOderDetail.this);
        builder.setMessage("Report this as out of stock?");

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        reportOutOfStock();
                    }
                });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        builder.show();

    }

    private void reportProblematic(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityProblematic.class);
        intent.putExtra(ActivityProblematic.ARG_JOB_ORDER, jobOrder.getJobOrderNo());
        startActivity(intent);

    }

    private void reportOutOfStock(){

        rlProgress.setVisibility(View.VISIBLE);

        ProblematicJobOrder report = new ProblematicJobOrder();
        report.setProblemType(getString(R.string.problematic_out_of_stock));
        report.setJobOrderNo(jobOrder.getJobOrderNo());
        report.setNotes("Item(s) out of stock");
//        report.setImage(null);

        Request request = JobOrderAPI.reportProblematic(REQUEST_OUT_OF_STOCK, report, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }

    @Override
    protected void handleRefreshToken() {

        int currentRequest = getCurrentRequest();
        switch (currentRequest){

            case REQUEST_UPDATE:

                requestUpdateStatus(newStatus);
                break;

            case REQUEST_OUT_OF_STOCK:

                reportOutOfStock();
                break;

            case REQUEST_ACCEPT_JOB:

                requestAcceptJob(jobOrder.getWaybillNo());
                break;

        }

    }

    private void requestAcceptJob(String waybillNo){

        rlProgress.setVisibility(View.VISIBLE);

        Request request = RiderAPI.acceptJobOrder(REQUEST_ACCEPT_JOB, waybillNo, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        requestQueue.add(request);

    }
}