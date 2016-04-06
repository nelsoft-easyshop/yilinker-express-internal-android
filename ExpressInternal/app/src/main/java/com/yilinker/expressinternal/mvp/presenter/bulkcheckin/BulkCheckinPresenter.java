package com.yilinker.expressinternal.mvp.presenter.bulkcheckin;

import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;
import com.yilinker.expressinternal.mvp.presenter.base.IQRCodePresenter;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.bulkcheckin.IBulkCheckinView;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class BulkCheckinPresenter extends RequestPresenter<BulkCheckinItem, IBulkCheckinView> implements IQRCodePresenter {


    private int counter = 1 ;

    @Override
    protected void updateView() {


    }


    @Override
    public void onQRCodeScan(String text) {

        view().addItem(createItem(text));

        counter ++;
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

    }

    private BulkCheckinItem createItem(String text){

        BulkCheckinItem item = new BulkCheckinItem();
        item.setId(counter);
        item.setWaybillNo(text);

        return item;
    }
}
