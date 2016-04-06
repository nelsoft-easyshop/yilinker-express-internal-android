package com.yilinker.expressinternal.mvp.presenter.confirmpackage;

import com.yilinker.expressinternal.mvp.model.PackageSize;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.confirmpackage.PackageSizeViewHolder;

/**
 * Created by J.Bautista on 3/21/16.
 */
public class PackageSizePresenter extends BasePresenter<PackageSize, PackageSizeViewHolder> {

    @Override
    protected void updateView() {

        view().setLabelText(model.getName());
    }


    public void onClick(){

        view().selectItem(model);
    }
}
