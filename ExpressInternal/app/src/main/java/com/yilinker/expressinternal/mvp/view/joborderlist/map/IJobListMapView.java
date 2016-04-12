package com.yilinker.expressinternal.mvp.view.joborderlist.map;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.Branch;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.model.Warehouse;
import com.yilinker.expressinternal.mvp.view.joborderlist.list.IJobListView;

import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public interface IJobListMapView<M extends JobOrder> extends IMapView, IJobListView {

    void loadWarehouses(List<Warehouse> warehouses);
    void loadBranches(List<Branch> branches);
    void addRequest(Request request);
}
