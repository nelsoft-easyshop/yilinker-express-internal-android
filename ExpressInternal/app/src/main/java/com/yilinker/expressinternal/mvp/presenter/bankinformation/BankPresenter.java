package com.yilinker.expressinternal.mvp.presenter.bankinformation;

import com.yilinker.expressinternal.mvp.model.BankInformation;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.bankinformation.BankViewHolder;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public class BankPresenter extends BasePresenter<BankInformation, BankViewHolder> {

    @Override
    protected void updateView() {

        view().setBankName(model.getBankName());
        view().setAccountName(model.getAccountName());
        view().setAccountNumber(model.getAccountNumber());
        //TODO add setter for logo here
    }

    public void setDropDownVisibility(){

        if (model.isDropDownOpen()){

            view().setDropDownOpen(false);
            model.setDropDownOpen(false);

        }else{

            view().setDropDownOpen(true);
            model.setDropDownOpen(true);

        }
    }
}
