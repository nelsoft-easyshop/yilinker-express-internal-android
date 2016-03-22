package com.yilinker.expressinternal.mvp.view.checklist;

import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.model.JobOrder;

import java.util.List;

/**
 * Created by wagnavu on 3/22/16.
 */
public interface IChecklistMainView {

    public void showChecklistView(String status, JobOrder jobOrder, List<ChecklistItem> items);
    public void setWaybillNoText(String waybillNo);
    public void setItemText(String item);
    public void setActionBarTitle(String status);
}