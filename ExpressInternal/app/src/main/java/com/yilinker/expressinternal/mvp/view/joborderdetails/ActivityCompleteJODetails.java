package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CompleteJODetailsPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;
import com.yilinker.expressinternal.mvp.view.mainScreen.ActivityMain;

/**
 * Created by Patrick on 3/17/2016.
 */
public class ActivityCompleteJODetails extends BaseFragmentActivity implements ICompleteJODetailsView, View.OnClickListener{

    public final static String KEY_JOB_ORDER_NUMBER = "job-order-number";
    public final static String KEY_JOB_ORDER = "job-order";

    private CompleteJODetailsPresenter presenter;

    private String joborderNumber;
    private JobOrder jobOrder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_2);

        if (savedInstanceState == null){
            presenter = new CompleteJODetailsPresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        getData();
        initializeViews(null);

    }

    @Override
    public void initializeViews(View parent) {
        Button btnWow = (Button) findViewById(R.id.btnWow);
        btnWow.setOnClickListener(this);
    }

    private void getData(){

//        joborderNumber = getIntent().getStringExtra(KEY_JOB_ORDER_NUMBER);
        jobOrder = getIntent().getParcelableExtra(KEY_JOB_ORDER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
        presenter.setModel(jobOrder);
//        presenter.requestJODetails(joborderNumber);
    }

    @Override
    public void showLoader(boolean isShown) {
        //TODO add loader here
    }

    @Override
    public void showErrorMessage(String errorMessage) {

        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setJobOrderNumber(String jobOrderNumber) {
//        TextView tvJobOrderNumber = (TextView) findViewById(R.id.tvJobOrderNo);
//        tvJobOrderNumber.setText(jobOrderNumber);
    }

    @Override
    public void setType(String type) {
    }

    @Override
    public void setOverallRating(int rating) {
//        RatingBar rbRating = (RatingBar) findViewById(R.id.ratingJob);
//        rbRating.setRating((float) rating);
    }

    @Override
    public void setTimeUsed(String timeUsed) {
//        TextView tvTimeUsed = (TextView) findViewById(R.id.tvTimeUsed);
//        tvTimeUsed.setText(timeUsed);
    }

    @Override
    public void setEarning(String earning) {
        TextView tvEarned = (TextView) findViewById(R.id.tvEarned);
        tvEarned.setText(earning);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnWow:
                Intent intent = new Intent(ActivityCompleteJODetails.this, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }

    }
}
