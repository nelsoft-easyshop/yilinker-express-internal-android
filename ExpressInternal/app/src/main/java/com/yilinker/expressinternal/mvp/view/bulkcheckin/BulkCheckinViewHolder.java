package com.yilinker.expressinternal.mvp.view.bulkcheckin;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.bulkcheckin.BulkCheckinItemPresenter;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class BulkCheckinViewHolder extends BaseViewHolder<BulkCheckinItemPresenter> implements IBulkCheckinItemView{

    private TextView tvWaybillNo;
    private TextView tvStatus;

    public BulkCheckinViewHolder(View itemView) {
        super(itemView);

        tvWaybillNo = (TextView) itemView.findViewById(R.id.tvWaybillNo);
        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
    }


    @Override
    public void setWaybillNoText(String waybillNo) {

        tvWaybillNo.setText(waybillNo);
    }

    @Override
    public void setStatusText(boolean isCheckedIn, boolean isProcessed) {

        Resources resources = itemView.getResources();
        String text = null;
        int color = R.color.orange_red;

        if(isProcessed){

            if (isCheckedIn) {

                text = resources.getString(R.string.bulkcheckin_success);
                color = R.color.blue_green;
            } else {

                text = resources.getString(R.string.bulkcheckin_failed);
            }

        }
        else {

            text = resources.getString(R.string.bulkcheckin_submitting);
        }

        tvStatus.setText(text);

        //Set color
        tvStatus.setTextColor(itemView.getResources().getColor(color));
    }
}
