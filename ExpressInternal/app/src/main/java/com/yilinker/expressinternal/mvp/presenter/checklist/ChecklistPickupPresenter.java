package com.yilinker.expressinternal.mvp.presenter.checklist;

import com.yilinker.expressinternal.model.ChecklistItem;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.checklist.IChecklistPickupView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/22/16.
 */
public class ChecklistPickupPresenter extends RequestPresenter<JobOrder, IChecklistPickupView> {


    private List<ChecklistItem> checklistItems;

    @Override
    protected void updateView() {



    }

    public void onViewCreated(JobOrder jobOrder, String[] titles){

        setModel(jobOrder);

        createChecklist(titles);
    }

    private void createChecklist(String[] titles){

        checklistItems = new ArrayList<>();
        ChecklistItem item = null;

        for(String title : titles){

            item = new ChecklistItem();

            item.setTitle(title);
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
}
