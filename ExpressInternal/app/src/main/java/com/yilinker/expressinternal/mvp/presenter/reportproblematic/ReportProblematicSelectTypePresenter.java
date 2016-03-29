package com.yilinker.expressinternal.mvp.presenter.reportproblematic;

import com.yilinker.expressinternal.mvp.model.ProblematicType;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.reportproblematic.IReportProblematicSelectTypeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick-villanueva on 3/28/2016.
 */
public class ReportProblematicSelectTypePresenter extends BasePresenter<List<ProblematicType>, IReportProblematicSelectTypeView> implements IReportProblematicSelectTypePresenter{

    @Override
    protected void updateView() {

        view().addAllProblematicTypes(model);
    }

    @Override
    public void createProblematicTypes(List<String> problematicTypes) {

        List<ProblematicType> types = new ArrayList<>();
        for (int i = 0; i<problematicTypes.size(); i++){
            ProblematicType type = new ProblematicType();
            type.setType(problematicTypes.get(i));
            type.setId(i);
            types.add(type);
        }

        setModel(types);
    }
}
