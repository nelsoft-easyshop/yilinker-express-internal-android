package com.yilinker.expressinternal.mvp.presenter.checklist;


import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.checklist.ChecklistItemViewHolder;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ChecklistItemPresenter extends BasePresenter<ChecklistItem, ChecklistItemViewHolder>{

    @Override
    protected void updateView() {

        view().setLabelText(model.getTitle());
        view().setCheckDrawable(model.isChecked());
        view().setExtraField(model.getExtraField());
    }


    public void onClick(){

        if(!model.needData()) {
            model.setIsChecked(!model.isChecked());
        }

        view().notifyDataChanged(model);
    }

}
