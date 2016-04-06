package com.yilinker.expressinternal.mvp.view.tools;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilinker.expressinternal.R;
import com.yilinker.expressinternal.interfaces.TabItemClickListener;
import com.yilinker.expressinternal.mvp.adapter.BaseViewHolder;
import com.yilinker.expressinternal.mvp.model.Tools;
import com.yilinker.expressinternal.mvp.presenter.tools.ToolsPresenter;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class ToolsViewHolder extends BaseViewHolder<ToolsPresenter> implements View.OnClickListener, IToolsView{

    private TabItemClickListener listener;
    private TextView tvToolsTitle;
    private ImageView ivTools;
    private ImageView ivIndicator;

    public ToolsViewHolder(View itemView, TabItemClickListener listener) {
        super(itemView);

        tvToolsTitle = (TextView) itemView.findViewById(R.id.tvToolsTitle);
        ivTools = (ImageView) itemView.findViewById(R.id.ivToolImage);
        ivIndicator = (ImageView) itemView.findViewById(R.id.ivIndicator);
        this.listener = listener;

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        presenter.onClick();

    }

    @Override
    public void setTitle(String title) {

        tvToolsTitle.setText(title);

    }

    @Override
    public void setIcon(int resourceIcon) {

        ivTools.setImageResource(resourceIcon);
    }

    @Override
    public void showSelected(Tools tool) {

        listener.onTabItemClick(tool.getId());

    }

    @Override
    public void setWarningIcon(int resource) {

        ivIndicator.setVisibility(resource == 0 ? View.GONE : View.VISIBLE);
        ivIndicator.setImageResource(resource);

    }
}
