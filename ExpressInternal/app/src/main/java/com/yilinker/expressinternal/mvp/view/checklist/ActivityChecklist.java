package com.yilinker.expressinternal.mvp.view.checklist;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.checklist.ChecklistMainPresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class ActivityChecklist extends BaseActivity implements IChecklistMainView {

    public static final String ARG_JOB_ORDER = "jobOrder";

    private ChecklistMainPresenter presenter;

    private TextView tvWaybillNo;
    private TextView tvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setActionBarLayout(R.layout.layout_toolbar2);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checklist2);

        //TODO Create new presenter or load previous presenter
        if(savedInstanceState == null){

            presenter = new ChecklistMainPresenter();

        }
        else{

            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initializeViews(null);

        presenter.bindView(this);

        presenter.onCreate(getData());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save presenter here
    }

    @Override
    public void showChecklistView(String status, JobOrder jobOrder) {

//        //TODO Show fragment checklist for the current status
//        Fragment fragment = null;
//
//        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){
//
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){
//
//        }
//        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){
//
//        }
//
//        replaceFragment(R.id.flContainer, fragment);
    }

    @Override
    public void setWaybillNoText(String waybillNo) {

        tvWaybillNo.setText(waybillNo);
    }

    @Override
    public void setItemText(String item) {

        tvItem.setText(item);
    }

    @Override
    public void setStatusTitle(String status) {

        setActionBarTitle(status);
    }

    @Override
    public void setActionBarColor(String status) {

        int color = 0;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

            color = R.color.marigold;
        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

            color = R.color.blue_green;

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){

            color = R.color.marigold;
        }

        setActionBarBackgroundColor(color);

    }

    @Override
    public void initializeViews(View parent) {

        tvItem = (TextView) findViewById(R.id.tvItem);
        tvWaybillNo = (TextView) findViewById(R.id.tvWaybillNo);

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    private JobOrder getData(){

        Intent intent = getIntent();

        JobOrder jobOrder = intent.getParcelableExtra(ARG_JOB_ORDER);

        return jobOrder;
    }

}
