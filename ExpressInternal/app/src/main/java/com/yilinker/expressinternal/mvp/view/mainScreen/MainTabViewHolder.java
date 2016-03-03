package com.yilinker.expressinternal.mvp.view.mainScreen;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.MainTab;
import com.yilinker.expressinternal.mvp.presenter.mainScreen.MainTabPresenter;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class MainTabViewHolder extends BaseViewHolder<MainTabPresenter> implements View.OnClickListener, IMainTabView{

    private TabItemClickListener listener;
    private ImageView ivTabIcon;
    private TextView tvTabTitle;

    public MainTabViewHolder(View itemView, TabItemClickListener listener) {
        super(itemView);

        this.ivTabIcon = (ImageView) itemView.findViewById(R.id.ivTabIcon);
        this.tvTabTitle = (TextView) itemView.findViewById(R.id.tvTabTitle);
        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        presenter.onClick();

    }

    @Override
    public void setTitle(String title) {

        tvTabTitle.setText(title);

    }

    @Override
    public void setIcon(int resourceIcon) {

        ivTabIcon.setImageResource(resourceIcon);
    }

    @Override
    public void setSelected(boolean isSelected) {

        //Set the color of the title's text depending on state
        tvTabTitle.setSelected(isSelected);

        //Set the background of the tab item depending on state
        itemView.setSelected(isSelected);

    }

    @Override
    public void showSelected(MainTab tab) {

        listener.onTabItemClick(tab.getId());
    }
}
