package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.view.accreditation.dropdown.DropdownRequirementViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class DropdownRequirementPresenter extends AccreditationRequirementPresenter<DropdownRequirementViewHolder>{

    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
        view().setSelectorButtonLabel(model.getInputValue());
    }

    @Override
    public void onValueChanged(Object object) {

        AccreditationRequirementData selectedData = (AccreditationRequirementData) object;

        model.setInputValue(selectedData.getLabel());
        model.setInputKey(selectedData.getKey());

        view().notifyDataChanged(model);
    }

    public void onClick(){

        view().setupList(model.getData());
        view().showPopup();
    }


}
