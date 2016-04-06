package com.yilinker.expressinternal.mvp.view.bulkcheckin;

import com.yilinker.expressinternal.mvp.model.BulkCheckinItem;

/**
 * Created by J.Bautista on 4/5/16.
 */
public interface IBulkCheckinView {

    public void addItem(BulkCheckinItem item);
    public void updateItem(BulkCheckinItem item);
}
