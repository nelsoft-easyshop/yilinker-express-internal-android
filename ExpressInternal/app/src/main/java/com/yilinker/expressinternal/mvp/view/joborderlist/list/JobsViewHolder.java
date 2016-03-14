package com.yilinker.expressinternal.mvp.view.joborderlist.list;

import android.view.View;

import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobItemPresenter;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.OpenJobItemPresenter;

/**
 * Created by J.Bautista on 3/3/16.
 */
public abstract class JobsViewHolder<P extends JobItemPresenter>  extends BaseViewHolder<P> {


    public JobsViewHolder(View itemView) {
        super(itemView);

    }

    public abstract void onTick();
}
