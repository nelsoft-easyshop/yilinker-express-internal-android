package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.button.ButtonImageViewHolder;

/**
 * Created by J.Bautista on 3/29/16.
 */
public class ButtonImagePresenter extends BasePresenter<ButtonRequirementImage, ButtonImageViewHolder> {


    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
    }


    public void onViewClick(){

        view().viewImage(model.getFilePath());
    }

    public void onDeleteClick(){

        view().deleteItem(model);
    }
}
