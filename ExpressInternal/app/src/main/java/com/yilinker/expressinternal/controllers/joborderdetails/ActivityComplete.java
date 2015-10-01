package com.yilinker.expressinternal.controllers.joborderdetails;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.interfaces.ResponseHandler;
import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.controllers.joborderlist.ActivityJobOrderList;
import com.yilinker.expressinternal.model.JobOrder;

/**
 * Created by J.Bautista
 */
public class ActivityComplete extends BaseActivity implements View.OnClickListener, ResponseHandler{

    public static final String ARG_JOB_ORDER = "jobOrder";
    public static final String ARG_FROM_HOME = "fromHome";

    private static final String DATE_FORMAT = "hh:mm aa";

    private static final int REQUEST_DETAIL  = 1000;

    private TextView tvJobOrderNo;
    private RatingBar ratingJob;
    private TextView tvTimeUSed;
    private TextView tvEarned;
    private TextView tvETA;
    private Button btnOk;
    private RelativeLayout rlProgress;

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

        if(isFromHome) {
            bindView();
        }
        else{

            requestJODetail();
        }

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
    protected void handleRefreshToken() {

    }

    @Override
    public void onBackPressed() {
        goBackToHome();
    }

    private void initViews(){

        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);
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


        ratingJob.setVisibility(View.GONE);
        rlProgress.setVisibility(View.GONE);

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

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        jobOrder = new JobOrder((com.yilinker.core.model.express.internal.JobOrder) object);
        bindView();

        rlProgress.setVisibility(View.GONE);

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        rlProgress.setVisibility(View.GONE);
    }

    private void requestJODetail(){

        rlProgress.setVisibility(View.VISIBLE);

        Request request = JobOrderAPI.getJobOrderDetailsByJONumber(REQUEST_DETAIL, jobOrder.getJobOrderNo(), this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        RequestQueue requestQueue = ApplicationClass.getInstance().getRequestQueue();
        requestQueue.add(request);

    }
}
