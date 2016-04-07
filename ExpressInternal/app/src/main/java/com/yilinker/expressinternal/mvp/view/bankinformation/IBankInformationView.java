package com.yilinker.expressinternal.mvp.view.bankinformation;

import com.yilinker.expressinternal.mvp.model.BankInformation;
import com.yilinker.expressinternal.mvp.view.RequestBaseView;

import java.util.List;

/**
 * Created by patrick-villanueva on 4/6/2016.
 */
public interface IBankInformationView extends RequestBaseView{

    public void addAllBanks(List<BankInformation> banks);
    public void showErrorMessage(String errorMessage);
}
