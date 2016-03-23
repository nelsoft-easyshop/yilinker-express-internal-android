package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.joborderdetails.CompleteJODetailsPresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragmentActivity;

/**
 * Created by Patrick on 3/17/2016.
 */
public class ActivityCompleteJODetails extends BaseFragmentActivity implements ICompleteJODetailsView{

    public final static String KEY_JOB_ORDER_NUMBER = "job-order-number";

    private CompleteJODetailsPresenter presenter;

    private String joborderNumber;
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

        presenter.bindView(this);
    }

    @Override
    public void initializeViews(View parent) {
        //TODO add views to be initialize here
    }

    private void getData(){

//        joborderNumber = getIntent().getStringExtra(KEY_JOB_ORDER_NUMBER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        //TODO add type to xml
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
//        TextView tvEarned = (TextView) findViewById(R.id.tvEarned);
//        tvEarned.setText(earning);
    }
}
