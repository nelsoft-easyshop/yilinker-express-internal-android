package com.yilinker.expressinternal.mvp.presenter.confirmpackage;

import com.yilinker.expressinternal.mvp.model.PackageType;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.confirmpackage.PackageTypeViewHolder;

/**
 * Created by J.Bautista on 3/21/16.
 */
public class PackageTypePresenter extends BasePresenter<PackageType, PackageTypeViewHolder> {

    @Override
    protected void updateView() {

        view().setLabelText(model.getName());
    }

    public void onClick(){

        view().selectItem(model);
    }

}
