package com.yilinker.expressinternal.controllers.joborderdetails;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.model.JobOrder;

/**
 * Created by J.Bautista
 */
public class ActivityComplete extends BaseActivity implements View.OnClickListener{

    public static final String ARG_JOB_ORDER = "jobOrder";
    public static final String ARG_FROM_HOME = "fromHome";

    private static final String DATE_FORMAT = "hh:mm aa";

    private TextView tvJobOrderNo;
    private RatingBar ratingJob;
    private TextView tvTimeUSed;
    private TextView tvEarned;
    private TextView tvETA;
    private Button btnOk;

    private JobOrder jobOrder;
    private boolean isFromHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        getData();

        initViews();

        bindView();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnOk:

                goBackToHome();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        goBackToHome();
    }

    private void initViews(){

        tvEarned = (TextView) findViewById(R.id.tvEarned);
        tvETA = (TextView) findViewById(R.id.tvETA);
        tvJobOrderNo = (TextView) findViewById(R.id.tvJobOrderNo);
        tvTimeUSed = (TextView) findViewById(R.id.tvTimeUsed);
        ratingJob = (RatingBar) findViewById(R.id.ratingJob);
        btnOk = (Button) findViewById(R.id.btnOk);

        //For Action Bar
        setActionBarTitle(jobOrder.getStatus());
        setActionBarBackgroundColor(R.color.marigold);

        if(isFromHome){
            btnOk.setVisibility(View.GONE);
        }
        else{
            btnOk.setOnClickListener(this);
        }



    }

    private void getData(){

        Intent intent = getIntent();

        jobOrder = intent.getParcelableExtra(ARG_JOB_ORDER);
        isFromHome = intent.getBooleanExtra(ARG_FROM_HOME, false);

    }

    private void bindView(){

        tvEarned.setText(String.format("₱%.02f", jobOrder.getEarning()));
        tvJobOrderNo.setText(jobOrder.getJobOrderNo());
        tvETA.setText(DateUtility.convertDateToString(jobOrder.getTimeDelivered(), DATE_FORMAT));
        ratingJob.setRating((float)jobOrder.getRating());

        long timeUsed = Math.abs((jobOrder.getTimeDelivered().getTime() - jobOrder.getEstimatedTimeOfArrival().getTime())/(3600 * 1000));
        int timeInHours = (int) timeUsed;
        int timeInMins = Math.abs((int) ((timeUsed - timeInHours) / 60));

        tvTimeUSed.setText(String.format("%02dHRS %02dMIN", timeInHours, timeInMins));
    }

    private void goBackToHome(){

        Intent intent = new Intent(ActivityComplete.this, ActivityJobOrderList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

}
