package com.yilinker.expressinternal.mvp.view.checklist;

import android.content.res.Resources;

import com.yilinker.expressinternal.interfaces.RecyclerViewClickListener;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.ChecklistItem;
import com.yilinker.expressinternal.mvp.presenter.checklist.ChecklistBasePresenter;
import com.yilinker.expressinternal.mvp.view.base.BaseFragment;

/**
 * Created by wagnavu on 3/23/16.
 */
public abstract class ChecklistBaseFragment<P extends ChecklistBasePresenter> extends BaseFragment implements RecyclerViewClickListener<ChecklistItem> {

    protected static final String ARG_JOB_ORDER = "joborder";

    protected P presenter;



    protected JobOrder getData(){

        JobOrder jobOrder = getArguments().getParcelable(ARG_JOB_ORDER);

        return jobOrder;
    }

    protected String[] getTitles(int arrayRes){

        Resources resources = getResources();

        String[] titles = resources.getStringArray(arrayRes);

        return titles;
    }

    protected String[] getTitleWithData(int arrayRes){

        Resources resources = getResources();

        String[] titles = resources.getStringArray(arrayRes);

        return titles;
    }
}
