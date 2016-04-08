package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.checklist.ChecklistItemViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistItemPresenter extends BasePresenter<AccreditationRequirementData, ChecklistItemViewHolder>{

    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
        view().setCheckDrawable(model.isSelected());
    }


    public void onClick(){

        model.setIsSelected(!model.isSelected());
        view().notifyDataChanged(model);
    }

}
