package com.yilinker.expressinternal.mvp.presenter.cashManagement;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.model.CashHistory;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.cashManagement.CashHistoryViewHolder;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by Patrick on 3/3/2016.
 */
public class CashHistoryPresenter extends BasePresenter<CashHistory, CashHistoryViewHolder>{

    private static final String DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";


    @Override
    protected void updateView() {
        view().setFormattedAmount(getFormattedAmount());
        view().setFormattedDate(getFormattedDate());
        view().setFormatterRunningTotal(getFormattedRunningTotal());
        view().setType(model.getType());
        view().setWaybillNumber(getWaybillNumber());
    }


    public String getFormattedDate(){
        return DateUtility.convertDateToString(model.getDate(), DATE_FORMAT);
    }

    public String getFormattedAmount(){
        return PriceFormatHelper.formatPrice(model.getAmount());
    }

    public String getFormattedRunningTotal(){
        return PriceFormatHelper.formatPrice(model.getRunningTotal());
    }

    public String getWaybillNumber(){
        String waybill = model.getWaybillNo();
        if (waybill.isEmpty()){
            waybill = "-";
        }

        return waybill;
    }

}
