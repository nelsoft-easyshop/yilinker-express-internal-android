package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.view.accreditation.button.ButtonRequirementViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ButtonRequirementPresenter extends AccreditationRequirementPresenter<ButtonRequirementViewHolder>{

    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
    }

    @Override
    public void onValueChanged(Object object) {

    }
}
