package com.yilinker.expressinternal.mvp.presenter.bulkcheckin;

import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.bulkcheckin.BulkCheckinViewHolder;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class BulkCheckinItemPresenter extends RequestPresenter<BulkCheckinItem, BulkCheckinViewHolder> {

    @Override
    protected void updateView() {

        view().setStatusText(model.isCheckedIn());
        view().setWaybillNoText(model.getWaybillNo());

    }


}
