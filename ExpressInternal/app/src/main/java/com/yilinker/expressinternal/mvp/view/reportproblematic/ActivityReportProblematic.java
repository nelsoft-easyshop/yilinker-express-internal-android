package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.reportproblematic.ReportProblematicPresenter;
import com.yilinker.expressinternal.mvp.view.BaseActivity;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ActivityReportProblematic extends BaseActivity implements IReportProblematicView, IReportProblematicClickListener{

    private ReportProblematicPresenter presenter;
    public static final String ARG_JOB_ORDER_NO = "jobOrder";

    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setActionBarLayout(R.layout.layout_problematic_toolbar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_problematic);

        if (savedInstanceState == null){
            presenter = new ReportProblematicPresenter();
        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);

            if (presenter==null) {
                presenter = PresenterManager.getInstance().restorePresenter(savedInstance);
            }

        }

        presenter.bindView(this);
        initializeViews(null);
        setFragment();
        getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);

    }

    private void getData(){
        Intent intent = getIntent();
        presenter.setJobOrderNumber(intent.getStringExtra(ARG_JOB_ORDER_NO));
    }

    @Override
    public void initializeViews(View parent) {
        super.initializeViews(parent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        savedInstance = new Bundle();
        PresenterManager.getInstance().savePresenter(presenter, savedInstance);
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = getFragmentManager();

        if(manager.getBackStackEntryCount() > 1){

            manager.popBackStack();
            Log.i("RESULT",""+manager.getBackStackEntryCount());
        }
        else{

            Log.i("RESULT","back pressed");
            super.onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    private void setFragment(){
        FragmentReportProblematicSelectType fragment = FragmentReportProblematicSelectType.createInstance();
        replaceFragment(fragment);
    }

    @Override
    public void onProblematicTypeClick(ProblematicType problematicType) {

        presenter.goToConfirmProblematic(problematicType);
    }

    @Override
    public void replaceFragment(Fragment fragment) {

        FragmentManager manager = getFragmentManager();

        android.app.FragmentTransaction transaction =  manager.beginTransaction();
        transaction.replace(R.id.flContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        Log.i("RESULT","added to backstack");

    }

    @Override
    public void goToConfirmProblematic(ProblematicType problematicType, String jobOrderNo) {
        //TODO create instance of report problematic details
//        Toast.makeText(getApplicationContext(),problematicType.getType(),Toast.LENGTH_SHORT).show();
        FragmentReportProblematicForm fragment = FragmentReportProblematicForm.createInstance(problematicType, jobOrderNo);
        replaceFragment(fragment);
    }

}
