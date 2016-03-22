package com.yilinker.expressinternal.mvp.view.checklist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.view.BaseActivity;

import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class ActivityChecklist extends BaseActivity implements IChecklistMainView {

    private TextView tvWaybillNo;
    private TextView tvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Create new presenter or load previous presenter
        if(savedInstanceState == null){


        }
        else{



        }

        initializeViews(null);

        getData();

        //TODO Call OnCreate method of the presenter
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save presenter here
    }

    @Override
    public void showChecklistView(String status, JobOrder jobOrder, List<ChecklistItem> items) {

        //TODO Show fragment checklist for the current status
        Fragment fragment = null;

        if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_PICKUP)){

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_DELIVERY)){

        }
        else if(status.equalsIgnoreCase(JobOrderConstant.JO_CURRENT_CLAIMING)){

        }

//        replaceFragment();
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
    public void initializeViews(View parent) {

    }

    @Override
    public void showLoader(boolean isShown) {

    }

    private void getData(){

    }

    private void setActionBar(){

    }
}
