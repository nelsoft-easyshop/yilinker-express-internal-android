package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.view.accreditation.inputtext.InputTextRequirementViewHolder;

/**
 * Created by wagnavu on 3/16/16.
 */
public class InputTextRequirementPresenter extends AccreditationRequirementPresenter<InputTextRequirementViewHolder> {

    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
        view().setInputText(model.getInputValue());
    }


    @Override
    public void onValueChanged(Object object) {

        String input = object.toString();
        resetSelectedValue(input);

        view().notifyDataChanged(model);
    }

    private void resetSelectedValue(String value){

        model.setInputValue(value);
    }
}
