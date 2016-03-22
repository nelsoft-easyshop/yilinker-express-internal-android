package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.view.accreditation.checkbox.CheckboxRequirementViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class CheckboxRequirementPresenter extends AccreditationRequirementPresenter<CheckboxRequirementViewHolder>{

    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
        view().setCheckDrawable(getInputValue());
    }

    @Override
    public void onValueChanged(Object object) {

    }

    public void onClick(){

        boolean isSelected = getInputValue();

        model.setInputValue(String.valueOf(!isSelected));

        view().notifyDataChanged(model);
    }

    private boolean getInputValue(){

        String stringStatus = model.getInputValue();
        boolean inputValue = false;

        if(stringStatus != null){

            inputValue = Boolean.parseBoolean(stringStatus);
        }

        return inputValue;
    }
}
