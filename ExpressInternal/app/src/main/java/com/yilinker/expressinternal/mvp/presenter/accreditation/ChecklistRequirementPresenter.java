package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.view.accreditation.checklist.ChecklistRequirementViewHolder;

import java.util.ArrayList;
import java.util.List;

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

        AccreditationRequirementData data = (AccreditationRequirementData)object;

        List<String> selectedValues = model.getSelectedValues();

        if(selectedValues == null){

            selectedValues = new ArrayList<>();
        }

        if(data.isSelected()){

            selectedValues.add(data.getKey());
        }
        else{

            selectedValues.remove(data.getKey());
        }

        model.setSelectedValues(selectedValues);

        view().notifyDataChanged(model);
    }

}
