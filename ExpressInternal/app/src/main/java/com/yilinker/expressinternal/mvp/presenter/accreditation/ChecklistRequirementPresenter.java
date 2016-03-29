package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.view.accreditation.checklist.ChecklistRequirementViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistRequirementPresenter extends AccreditationRequirementPresenter<ChecklistRequirementViewHolder>{

    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());

    }

    @Override
    public void bindView(ChecklistRequirementViewHolder view) {
        super.bindView(view);

        view().setChecklistContent(model.getData());
    }

    @Override
    public void onValueChanged(Object object) {

    }

    public void onInitList(){

        view().setChecklistContent(model.getData());
    }
}
