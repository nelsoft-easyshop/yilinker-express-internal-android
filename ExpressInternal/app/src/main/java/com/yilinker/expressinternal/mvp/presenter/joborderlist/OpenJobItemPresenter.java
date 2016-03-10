package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.core.utility.DateUtility;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.OpenJobsViewHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class OpenJobItemPresenter extends JobItemPresenter<OpenJobsViewHolder> {

    @Override
    protected void updateView() {

        view().setWaybillNo(model.getWaybillNo());
        view().setSize(model.getSize());
        view().setStatus(model.getStatus());
        view().setEarning(formatEarning(model.getEarning()));
        view().setDateCreated(formatDateCreated(Calendar.getInstance().getTime()));
        view().setFromAddress(model.getPickupAddress());
        view().setToAddress(model.getDeliveryAddress());
    }

    //TODO Move this method so this can be reuse
    private String formatEarning(double earning){

        NumberFormat formatter = new DecimalFormat("#,###.00");
        String formattedEarning = formatter.format(earning);

        return String.format("P %s", formattedEarning);
    }

    private String formatDateCreated(Date date){

        String format = "dd MMM - hh:mm aa";

        String stringDate = DateUtility.convertDateToString(date, format);

        return stringDate;
    }

    public void onClick(){

        view().showDetails(model);
    }

}
