package com.yilinker.expressinternal.controllers.joborderdetails;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.base.BaseActivity;
import com.yilinker.expressinternal.constants.JobOrderConstant;

/**
 * Created by J.Bautista
 */
public class ActivityProblematic extends BaseActivity {

    public static final String ARG_JOB_ORDER = "jobOrder";
    private static final String FRAGMENT_STATE = "fragment";

    private String jobOrderNo;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set the layout of the actionbar
        setActionBarLayout(R.layout.layout_actionbar_yellow);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problematic);

        getData();

        initViews();

        if(savedInstanceState != null) {
            fragment = getFragmentManager().getFragment(savedInstanceState, FRAGMENT_STATE);
//            replaceFragment(fragment, true);
        } else {
            showOptions();
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getFragmentManager().putFragment(outState, FRAGMENT_STATE, fragment);
    }


    @Override
    public void onBackPressed() {

        FragmentManager manager = getFragmentManager();

        if(manager.getBackStackEntryCount() > 0){

            manager.popBackStack();
        }
        else{

            super.onBackPressed();
        }


    }

    private void initViews(){


        //For Action Bar
        setTitle(getString(R.string.actionbar_title_problematic));
        setActionBarBackgroundColor(R.color.marigold);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack){

        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction =  manager.beginTransaction();

        transaction.replace(R.id.flFragment, fragment);

        if(addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }


    private void showOptions(){

        Fragment fragment = FragmentProblematicOptions.createInstance(new Bundle());
        replaceFragment(fragment, false);

    }

    private void showReportForm(int type){

        Bundle bundle = new Bundle();
        bundle.putInt(FragmentReportForm.ARG_TYPE, type);
        bundle.putString(FragmentReportForm.ARG_JONUMBER, jobOrderNo);

//        FragmentReportForm fragment = FragmentReportForm.createInstance(bundle);

        fragment = FragmentReportForm.createInstance(bundle);

        replaceFragment(fragment, true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id){

            case R.id.btnDamage:

                showReportForm(JobOrderConstant.PROBLEMATIC_DAMAGED);
                break;

            case R.id.btnNoPayment:

                showReportForm(JobOrderConstant.PROBLEMATIC_UNABLE_TO_PAY);
                break;

            case R.id.btnNotFound:

                showReportForm(JobOrderConstant.PROBLEMATIC_RECIPIENT_NOT_FOUND);
                break;

            case R.id.btnRejected:

                showReportForm(JobOrderConstant.PROBLEMATIC_REJECTED);
                break;

        }
    }

    @Override
    protected void handleRefreshToken() {

    }

    private void getData(){

        Intent intent = getIntent();
        jobOrderNo = intent.getStringExtra(ARG_JOB_ORDER);
    }


}
