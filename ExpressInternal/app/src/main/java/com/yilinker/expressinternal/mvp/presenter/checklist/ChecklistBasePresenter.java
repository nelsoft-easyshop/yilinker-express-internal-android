package com.yilinker.expressinternal.mvp.presenter.checklist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by J.Bautista on 3/23/16.
 */
public abstract class ChecklistBasePresenter<V extends IChecklistBase> extends RequestPresenter<JobOrder, V> {

    private List<ChecklistItem> checklistItems;

    protected ChecklistItem currentItem;

    public abstract void onCompleteButtonClick();

    @Override
    protected void updateView() {

        if(checklistItems != null) {
            view().enableCompleteButton(hasCompletedChecklist());
        }
    }

    public void onViewCreated(JobOrder jobOrder, String[] titles, String[] titlesWithData){

        setModel(jobOrder);

        createChecklist(titles, titlesWithData);

        view().loadChecklistItems(checklistItems);
    }

    public void onUpdateChecklistItem(ChecklistItem item){

        updateItem(item);

        updateView();
    }

    private void createChecklist(String[] titles, String[] titlesWithData){

        String dataDictionary = Arrays.toString(titlesWithData);

        checklistItems = new ArrayList<>();
        ChecklistItem item = null;

        int i = 0;
        for(String title : titles){

            item = new ChecklistItem();
            item.setId(i);
            item.setTitle(title);

            item.setNeedData(dataDictionary.contains(title));

            checklistItems.add(item);

            i++;
        }

    }

    private boolean hasCompletedChecklist(){

        boolean hasCompleted = true;

        for(ChecklistItem item : checklistItems){

            if(!item.isChecked()){

                hasCompleted = false;
                break;
            }
        }

        return hasCompleted;
    }


    private void updateItem(ChecklistItem item){

        checklistItems.set(item.getId(), item);
    }
}
