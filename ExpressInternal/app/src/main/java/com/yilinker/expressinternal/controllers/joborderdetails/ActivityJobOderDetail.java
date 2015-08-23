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

import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.controllers.checklist.ActivityChecklist;
import com.yilinker.expressinternal.controllers.contact.ActivityContact;
import com.yilinker.expressinternal.controllers.images.ActivityImageGallery;
import com.yilinker.expressinternal.controllers.navigation.ActivityNavigation;
import com.yilinker.expressinternal.model.JobOrder;

/**
 * Created by J.Bautista
 */
public class ActivityJobOderDetail extends BaseActivity implements ResponseHandler{

    public static final String ARG_JOB_ORDER = "jobOrder";

    private LinearLayout llContainer;

    private TextView tvJobOrderNo;
    private TextView tvStatus;
    private TextView tvItem;
    private TextView tvRecipient;
    private TextView tvContactNo;
    private TextView tvType;
    private Button btnPositive;
    private Button btnNegative;

    //For Side Menu
    private ImageButton btnContact;
    private ImageButton btnImage;
    private ImageButton btnNavigation;

    //For Pickup
    private TextView tvPickupAddress;
    private TextView tvDropoffAddress;

    //For Completed
    private RatingBar ratingBar;
    private TextView tvTimeUsed;
    private TextView tvEarned;

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
        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);
        btnContact = (ImageButton) findViewById(R.id.btnContact);
        btnImage = (ImageButton) findViewById(R.id.btnImage);
        btnNavigation = (ImageButton) findViewById(R.id.btnMap);

        //For Action Bar
        setTitle("For Pickup");
        setActionBarBackgroundColor(R.color.marigold);

        btnNegative.setOnClickListener(this);
        btnPositive.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnNavigation.setOnClickListener(this);
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

                goToChecklist();
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


        }
    }

    private void getData(){

        Intent intent = getIntent();
        jobOrder = intent.getParcelableExtra(ARG_JOB_ORDER);
        type = jobOrder.getType();

    }

    private void bindViewData(){


        tvJobOrderNo.setText(jobOrder.getJobOrderNo());

    }

    private void setContentView(){

        //Set the content of the screen
        int status = jobOrder.getStatus();
        View contentView = null;
        LayoutInflater inflater = getLayoutInflater();


        if(type == JobOrderConstant.JO_TYPE_PICKUP && status == JobOrderConstant.JO_OPEN){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_open_pickup, llContainer, true);
        }
        else if (type == JobOrderConstant.JO_TYPE_DELIVERY){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_delivery, llContainer, true);
        }
        else if (status == JobOrderConstant.JO_COMPLETE){

            contentView = inflater.inflate(R.layout.layout_joborderdetail_complete, llContainer, true);
        }
        else{

            contentView = inflater.inflate(R.layout.layout_joborderdetail_current_pickup, llContainer, true);
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
        startActivity(intent);
    }

    private void showNavigation(){

        Intent intent = new Intent(ActivityJobOderDetail.this, ActivityNavigation.class);
        startActivity(intent);
    }
}
