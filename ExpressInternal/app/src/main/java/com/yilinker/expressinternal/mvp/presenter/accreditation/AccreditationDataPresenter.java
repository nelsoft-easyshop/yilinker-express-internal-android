package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.AccreditationDataViewHolder;

/**
 * Created by wagnavu on 3/17/16.
 */
public class AccreditationDataPresenter extends BasePresenter<AccreditationRequirementData, AccreditationDataViewHolder> {


    @Override
    protected void updateView() {

        view().setLabel(model.getLabel());
    }

    public void onClick(){

        view().selectItem(model);
    }
}
