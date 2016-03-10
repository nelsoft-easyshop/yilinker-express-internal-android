package com.yilinker.expressinternal.mvp.view.cashManagement;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.model.CashHistory;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.cashManagement.CashHistoryPresenter;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

/**
 * Created by Patrick on 3/3/2016.
 */
public class CashHistoryViewHolder extends BaseViewHolder<CashHistoryPresenter> implements ICashHistoryView {

//    private TextView tvAction;
    private TextView tvAmount;
    private TextView tvType;
    private TextView tvWaybillNo;
    private TextView tvRunningTotal;

    public CashHistoryViewHolder(View view) {
        super(view);

//        tvAction = (TextView) view.findViewById(R.id.tvAction);
        tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvType = (TextView) view.findViewById(R.id.tvType);
        tvWaybillNo = (TextView) view.findViewById(R.id.tvWaybillNo);
        tvRunningTotal = (TextView) view.findViewById(R.id.tvRunningTotal);
    }

    @Override
    public void setFormattedDate(String date) {
//        tvAction.setText(date);

    }

    @Override
    public void setFormattedAmount(String amount, boolean isPositive) {
        ApplicationClass applicationClass = (ApplicationClass) ApplicationClass.getInstance();
        tvAmount.setText(String.format("%s  %s", applicationClass.getString(R.string.cashmanagement_amount),amount));

        changeAmountColor(isPositive);
    }

    @Override
    public void setFormatterRunningTotal(String runningTotal) {
        tvRunningTotal.setText(runningTotal);
    }

    @Override
    public void setWaybillNumber(String waybillNumber) {
        tvWaybillNo.setText(waybillNumber);
    }

    @Override
    public void setType(String type) {
        tvType.setText(type);
    }

    private void changeAmountColor(boolean isPositive){

        int color = 0;

        if(isPositive){

            color = R.color.apple_green;
        }
        else{

            color = R.color.orange_yellow;
        }

        Resources resources = ApplicationClass.getInstance().getResources();

        tvAmount.setTextColor(resources.getColor(color));

    }

}
