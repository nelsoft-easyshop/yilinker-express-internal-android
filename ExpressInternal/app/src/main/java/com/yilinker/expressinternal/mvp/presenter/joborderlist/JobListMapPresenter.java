package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.expressinternal.business.ApplicationClass;
import com.yilinker.expressinternal.constants.JobOrderConstant;
import com.yilinker.expressinternal.model.Branch;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.model.Rider;
import com.yilinker.expressinternal.model.Warehouse;
import com.yilinker.expressinternal.mvp.presenter.base.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.map.IJobListMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/7/16.
 */
public class JobListMapPresenter extends RequestPresenter<List<JobOrder>, IJobListMapView> {

    private static final int REQUEST_WAREHOUSE = 1000;

    private Branch riderBranch;

    @Override
    protected void updateView() {

        view().loadJobOrderList(model);
    }

    public void onViewCreated(Branch riderBranch){

        loadBranches(riderBranch);
    }

    public void onReloadBranches(Branch branch){

        loadBranches(branch);
    }

    public void onLoadJobOrders(){

        requestGetWarehouse();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_WAREHOUSE:

                //handleGetWarehouseResponse((List<com.yilinker.core.model.express.internal.Warehouse>)object);
                handleGetWarehouseResponse((com.yilinker.core.model.express.internal.Warehouse)object);
                break;
        }
    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);


    }

    private void requestGetWarehouse(){

        Request request = JobOrderAPI.getWarehouses(REQUEST_WAREHOUSE, this);
        request.setTag(ApplicationClass.REQUEST_TAG);

        view().addRequest(request);
    }

//    private void handleGetWarehouseResponse(List<com.yilinker.core.model.express.internal.Warehouse> list){
//
//        List<Warehouse> warehouses = new ArrayList<>();
//        Warehouse warehouse = null;
//
//        for(com.yilinker.core.model.express.internal.Warehouse item : list){
//
//            warehouse = new Warehouse(item);
//
//            warehouses.add(warehouse);
//        }
//
//        view().loadWarehouses(warehouses);
//    }

    private void handleGetWarehouseResponse(com.yilinker.core.model.express.internal.Warehouse object){

        List<Warehouse> warehouses = new ArrayList<>();
        Warehouse warehouse = new Warehouse(object);

        warehouses.add(warehouse);

        view().loadWarehouses(warehouses);
    }

    private List<Branch> createBranchList(){

        List<Branch> branches = new ArrayList<>();

        branches.add(riderBranch);

        return branches;
    }

    private void loadBranches(Branch branch){

        this.riderBranch = branch;

        List<Branch> branches = createBranchList();
        view().loadBranches(branches);
    }

}
