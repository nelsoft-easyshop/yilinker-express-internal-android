package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Warehouse;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.IJobListView;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IJobListMapView<M extends JobOrder> extends IMapView, IJobListView {

    public void loadWarehouses(List<Warehouse> warehouses);

}
