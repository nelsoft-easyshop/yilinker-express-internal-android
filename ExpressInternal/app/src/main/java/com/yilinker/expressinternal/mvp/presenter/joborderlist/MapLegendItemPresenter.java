package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.mvp.model.MapLegendItem;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.MapLegendItemViewHolder;

/**
 * Created by J.Bautista on 4/7/16.
 */
public class MapLegendItemPresenter extends BasePresenter<MapLegendItem, MapLegendItemViewHolder> {


    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());
        view().setImage(model.getResourceId());
    }
}
