package com.yilinker.expressinternal.mvp.view.checklist;


import com.yilinker.expressinternal.mvp.model.ChecklistItem;

import java.util.List;

/**
 * Created by wagnavu on 3/22/16.
 */
public interface IChecklistBase {

    public void loadChecklistItems(List<ChecklistItem> items);
    public void updateItem(ChecklistItem item);
    public void enableCompleteButton(boolean isEnabled);
    public void showMessage(String message);

}
