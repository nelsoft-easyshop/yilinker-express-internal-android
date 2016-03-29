package com.yilinker.expressinternal.mvp.view.reportproblematic;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.PresenterManager;
import com.yilinker.expressinternal.mvp.presenter.reportproblematic.ReportProblematicPresenter;
import com.yilinker.expressinternal.mvp.presenter.reportproblematic.ReportProblematicSelectTypePresenter;
import com.yilinker.expressinternal.mvp.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class FragmentReportProblematicSelectType extends BaseFragment implements IReportProblematicSelectTypeView{

    private IReportProblematicClickListener itemClickCallBack;
    private ProblematicTypeAdapter adapter;

    private ReportProblematicSelectTypePresenter presenter;


    public static FragmentReportProblematicSelectType createInstance(){
        FragmentReportProblematicSelectType fragment = new FragmentReportProblematicSelectType();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_problematic_select_type, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null){
            presenter = new ReportProblematicSelectTypePresenter();

        }else{
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initializeViews(view);

        presenter.bindView(this);
        presenter.createProblematicTypes(getProblematicTypes());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void initializeViews(View parent) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);

        RecyclerView rvProblematicType = (RecyclerView) parent.findViewById(R.id.rvProblematicType);
        rvProblematicType.setLayoutManager(layoutManager);
        adapter = new ProblematicTypeAdapter(itemClickCallBack);
        rvProblematicType.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    private List<String> getProblematicTypes(){
        List<String> types = new ArrayList<>();
        types.add(getString(R.string.problematic_recipient_not_found));
        types.add(getString(R.string.problematic_delivery_rejected));
        types.add(getString(R.string.problematic_damaged_upon_delivery));
        types.add(getString(R.string.problematic_unable_to_pay));

        return types;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        itemClickCallBack = (IReportProblematicClickListener) activity;
    }

    @Override
    public void showLoader(boolean isShown) {

    }

    @Override
    public void addAllProblematicTypes(List<ProblematicType> problematicTypes) {
        adapter.clearAndAddAll(problematicTypes);
    }
}
