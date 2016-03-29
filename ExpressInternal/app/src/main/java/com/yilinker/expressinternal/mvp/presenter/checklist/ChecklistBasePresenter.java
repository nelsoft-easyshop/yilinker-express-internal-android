package com.yilinker.expressinternal.mvp.presenter.checklist;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistBase;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by J.Bautista on 3/23/16.
 */
public abstract class ChecklistBasePresenter<V extends IChecklistBase> extends RequestPresenter<JobOrder, V> {

    protected static final String REQUEST_TAG = "request";

    private List<ChecklistItem> checklistItems;

    protected ChecklistItem currentItem;

    public abstract void onCompleteButtonClick();

    @Override
    protected void updateView() {

        if(checklistItems != null) {
            view().enableCompleteButton(hasCompletedChecklist());
        }
    }

    public void onPause(){

        List<String> requests = new ArrayList<>();
        requests.add(REQUEST_TAG);

        view().cancelRequest(requests);

    }

    public void onViewCreated(JobOrder jobOrder, String[] titles, String[] titlesWithData, String paymentField){

        setModel(jobOrder);

        createChecklist(titles, titlesWithData, paymentField);

        view().loadChecklistItems(checklistItems);
    }

    public void onUpdateChecklistItem(ChecklistItem item){

        updateItem(item);

        updateView();
    }

    private void createChecklist(String[] titles, String[] titlesWithData, String paymentField){

        String dataDictionary = Arrays.toString(titlesWithData);

        checklistItems = new ArrayList<>();
        ChecklistItem item = null;

        int i = 0;
        for(String title : titles){

            item = new ChecklistItem();
            item.setId(i);
            item.setTitle(title);

            item.setNeedData(dataDictionary.contains(title));

            if(title.equalsIgnoreCase(paymentField) && model.getAmountToCollect() > 0){

                item.setExtraField(PriceFormatHelper.formatPrice(model.getAmountToCollect()));
            }

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
