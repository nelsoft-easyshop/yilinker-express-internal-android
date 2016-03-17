package com.yilinker.expressinternal.mvp.view.joborderdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){
            presenter = new CompleteJODetailsPresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

        }

        setContentView(R.layout.activity_complete);

        getData();
        initializeViews(null);

    }

    @Override
    public void initializeViews(View parent) {
        //TODO add views to be initialize here
    }

    private void getData(){

        joborderNumber = getIntent().getStringExtra(KEY_JOB_ORDER_NUMBER);
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
        presenter.requestJODetails(joborderNumber);
    }

    @Override
    public void showLoader(boolean isShown) {
        //TODO add loader here
    }

    @Override
    public void showErrorMessage(String errorMessage) {

        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }
}
