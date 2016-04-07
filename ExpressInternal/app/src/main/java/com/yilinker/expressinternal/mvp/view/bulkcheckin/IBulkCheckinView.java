package com.yilinker.expressinternal.mvp.view.bulkcheckin;

import com.android.volley.Request;
import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;

import java.util.List;

/**
 * Created by J.Bautista on 4/5/16.
 */
public interface IBulkCheckinView {

    public void addItem(BulkCheckinItem item);
    public void updateItem(BulkCheckinItem item);
    public void addRequest(Request request);
    public void cancelRequest(List<String> requests);
}
