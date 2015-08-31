package com.yilinker.expressinternal.controllers.joborderdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.contact.ActivityContact;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.navigation.ActivityNavigation;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista
 */
public class ActivityJobOderDetail extends BaseActivity implements ResponseHandler{

    public static final String ARG_JOB_ORDER = "jobOrder";

    private static final String ETA_DATE_FORMAT = "hh:mm aa";

    private LinearLayout llContainer;

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

    private JobOrder jobOrder;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joborderdetail);

        //Get data passed by the previous activity
        getData();

        initViews();

        bindViewData();

    }

    private void initViews(){

        llContainer = (LinearLayout) findViewById(R.id.llContainer);

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

        setActionBar();

    }


    @Override
    public void onSuccess(int requestCode, Object object) {

    }

    @Override
    public void onFailed(int requestCode, String message) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnPositive:

//                goToChecklist();
                break;

            case R.id.btnNegative:

                break;

            case R.id.btnMap:
                showNavigation();
                break;

            case  R.id.btnContact:

                goToContact();
                break;

            case R.id.btnImage:

                showImageGallery();
                break;

            case R.id.btnQRCode:

                Toast.makeText(getApplicationContext(), "Job Order QR Code", Toast.LENGTH_LONG).show();
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
        setActionBarBackgroundColor(R.color.marigold);

    }

    private void getData(){

        Intent intent = getIntent();
        jobOrder = intent.getParcelableExtra(ARG_JOB_ORDER);
//        type = jobOrder.getType();

    }

    private void bindViewData(){

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

        tvItem.setText(builder.toString());

        String type = jobOrder.getType();
        String status = jobOrder.getStatus();

        if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY)){
            setOpenDeliveryViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP)){
            setOpenPickupViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){
            setCurrentPickupViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){
            setCurrentDeliveryViews();
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){
            setCurrentDeliveryViews();
        }

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

        btnPositive.setText(getString(R.string.joborderdetail_open_accept));
        btnNegative.setText(getString(R.string.joborderdetail_open_decline));

        tvStatus.setText(jobOrder.getStatus());
        tvDistance.setText(String.format("%.02f KM", jobOrder.getDistance() / 1000));
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

        if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_DELIVERY)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_open_delivery, llContainer, true);

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_OPEN) && type.equalsIgnoreCase(JobOrderConstant.JO_TYPE_PICKUP)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_open_pickup, llContainer, true);

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_current_pickup, llContainer, true);
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_currentdelivery, llContainer, true);
        }

    }

    private void goToChecklist(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityChecklist.class);
        startActivity(intent);

    }


    private void goToContact(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityContact.class);
        intent.putExtra(ActivityContact.ARG_NAME, jobOrder.getRecipient());
        intent.putExtra(ActivityContact.ARG_CONTACT_NO, jobOrder.getContactNo());
        startActivity(intent);

    }

    private void showImageGallery(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityImageGallery.class);
        intent.putStringArrayListExtra(ActivityImageGallery.ARG_IMAGES, (ArrayList)jobOrder.getImages());
        startActivity(intent);
    }

    private void showNavigation(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityNavigation.class);
        intent.putExtra(ActivityNavigation.ARG_DESTINATION_LAT, jobOrder.getLatitude());
        intent.putExtra(ActivityNavigation.ARG_DESTINATION_LONG, jobOrder.getLongitude());
        startActivity(intent);
    }
}
