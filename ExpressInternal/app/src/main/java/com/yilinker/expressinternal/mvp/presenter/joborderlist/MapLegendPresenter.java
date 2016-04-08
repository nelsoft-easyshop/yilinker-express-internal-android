package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.yilinker.expressinternal.mvp.model.MapLegendItem;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.IMapLegendView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 4/7/16.
 */
public class MapLegendPresenter extends BasePresenter<List<MapLegendItem>, IMapLegendView> {


    @Override
    protected void updateView() {

        view().loadItems(model);
    }

    public void onViewCreated(String[] titles, int[] resourceIds){

        List<MapLegendItem> items = createItems(titles, resourceIds);

        setModel(items);
    }

    private List<MapLegendItem> createItems(String[] titles, int[] resourceIds){

        List<MapLegendItem> items = new ArrayList<>();

        MapLegendItem item = null;
        int i = 0;

        for(String title : titles){

            item = new MapLegendItem();

            item.setId(i);
            item.setLabel(title);
            item.setResourceId(resourceIds[i]);

            items.add(item);

            i++;
        }

        return items;
    }
}
