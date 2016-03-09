package com.yilinker.expressinternal.mvp.view.joborderlist;

import android.view.View;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.joborderlist.JobsTabPresenter;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainTabPresenter;
import com.yilinker.expressinternal.mvp.view.ITabView;

/**
 * Created by J.Bautista on 3/3/16.
 */
public class JobsTabViewHolder extends BaseViewHolder<JobsTabPresenter> implements ITabView, View.OnClickListener {

    private TextView tvTitle;
    private TabItemClickListener listener;

    public JobsTabViewHolder(View itemView, TabItemClickListener listener) {
        super(itemView);

        tvTitle = (TextView) itemView.findViewById(R.id.tvTabTitle);

        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void setTitle(String title) {

        tvTitle.setText(title);
    }

    @Override
    public void setIcon(int resourceIcon) {

    }

    @Override
    public void setSelected(boolean isSelected) {

        tvTitle.setSelected(isSelected);
        itemView.setSelected(isSelected);
    }

    @Override
    public void showSelected(TabItem tab) {

        listener.onTabItemClick(tab.getId());
    }

    @Override
    public void onClick(View v) {

        presenter.onClick();
    }
}
