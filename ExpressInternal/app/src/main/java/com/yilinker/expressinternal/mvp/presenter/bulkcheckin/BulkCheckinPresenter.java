package com.yilinker.expressinternal.mvp.presenter.bulkcheckin;

import com.android.volley.Request;
import com.yilinker.core.api.RiderAPI;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;
import com.yilinker.expressinternal.mvp.presenter.base.IQRCodePresenter;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.bulkcheckin.IBulkCheckinView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 4/5/16.
 */
public class BulkCheckinPresenter extends RequestPresenter<List<BulkCheckinItem>, IBulkCheckinView> implements IQRCodePresenter {

    private List<String> requestTags;

    private int counter = 1 ;

    @Override
    protected void updateView() {


    }


    @Override
    public void onQRCodeScan(String text) {

        BulkCheckinItem item = createItem(text);

        view().addItem(item);

        if(model == null){

            model = new ArrayList<>();
        }

        model.add(item);

        requestAcceptJob(item.getWaybillNo(), item.getId());

        counter ++;
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        BulkCheckinItem item = findItemById(requestCode);
        item.setIsProcessed(true);
        item.setIsCheckedIn(true);

        view().updateItem(item);
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        BulkCheckinItem item = findItemById(requestCode);
        item.setIsProcessed(true);
        item.setIsCheckedIn(false);

        view().updateItem(item);
    }

    public void onPause(){

        view().cancelRequest(requestTags);
    }

    private BulkCheckinItem createItem(String text){

        BulkCheckinItem item = new BulkCheckinItem();
        item.setId(counter);
        item.setWaybillNo(text);

        return item;
    }

    private void requestAcceptJob(String waybillNo, int id) {

        Request request = RiderAPI.acceptJobOrderByWaybillNo(id, waybillNo, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        view().addRequest(request);
    }

    private BulkCheckinItem findItemById(int id){

        BulkCheckinItem item = null;

        if(model != null) {

            for(BulkCheckinItem object : model){

                if(object.getId() == id){

                    return object;
                }

            }

        }

        return  item;

    }
}
