package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.IJobListMainView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class JobListMainPresenter extends RequestPresenter<List<JobOrder>, IJobListMainView> {

    private static final int REQUEST_GET_OPEN = 1000;
    private static final int REQUEST_GET_CURRENT = 1001;

    private static final String TYPE_OPEN = "Open";
    private static final String TYPE_CURRENT = "Current";

    private List<JobOrder> completeList;
    private List<TabItem> tabs;
    private int previousTab = 0;

    public JobListMainPresenter(){

        this.tabs = new ArrayList<>();
    }

    @Override
    protected void updateView() {

        view().reloadList(model);
        view().setResultCountText(String.valueOf(model.size()));

    }

    public void onViewCreated(){

        view().showListView(new ArrayList<JobOrder>());
    }

    public void onResume(String type){

        requestGetJobOrders(type);

    }


    public void onTabItemClicked(int jobType){


        changeSelectedTab(jobType);

        if(jobType == 0){

            requestGetJobOrders(TYPE_OPEN);
        }
        else {

            requestGetJobOrders(TYPE_CURRENT);
        }


    }


    public void initializeTabs(String[] tabTitles){

        int size = tabTitles.length;
        TabItem tab = null;

        for(int i = 0; i < size; i++){

            tab = new TabItem();
            tab.setId(i);
            tab.setTitle(tabTitles[i]);
            tab.setSelected(i == 0);

            tabs.add(tab);
        }

        view().loadTabs(tabs);
    }

    public void onFilterChanged(String[] filters){



    }

    public void onSearchWaybill(String waybillNo){

        List<JobOrder> result = new ArrayList<>();

        if(waybillNo.length() == 0){

            result.addAll(completeList);
        }
        else{

            result.addAll(search(waybillNo));
        }

        setModel(result);

    }

    public void onSwitchButtonClicked(){

        view().switchView(model);

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_GET_OPEN:

                handleGetJobOrders(object, 0);
                break;

            case REQUEST_GET_CURRENT:

                handleGetJobOrders(object, 0);
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);


    }

    private void changeSelectedTab(int position){

        //Unselect previous tab
        TabItem previous = tabs.get(previousTab);
        previous.setSelected(false);

        //Select current tab
        TabItem current = tabs.get(position);
        current.setSelected(true);

        previousTab = position;

        view().changeSelectedTab(previous, current);
    }

    private void requestGetJobOrders(String type){

        int requestCode = 0;

        if(type.equals(TYPE_OPEN)){
            requestCode = REQUEST_GET_OPEN;
        }
        else {
            requestCode = REQUEST_GET_CURRENT;
        }

        Request request = JobOrderAPI.getJobOrders(requestCode, type, true, this);

        view().addRequest(request);

    }

    private void handleGetJobOrders(Object object, int type){

        List<com.yilinker.core.model.express.internal.JobOrder> listServer = (ArrayList<com.yilinker.core.model.express.internal.JobOrder>) object;
        completeList = createList(listServer, 0);

        setModel(completeList);
    }

    private List<JobOrder> createList(List<com.yilinker.core.model.express.internal.JobOrder> listServer, int type) {

        List<JobOrder> list = new ArrayList<>();
        JobOrder jobOrder = null;

        int i = 0;
        for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {

            jobOrder = new JobOrder(item);
            jobOrder.setId(i);
            list.add(jobOrder);

            i++;
        }

        return list;

    }

    private List<JobOrder> search(String query){

        List<JobOrder> result = new ArrayList<>();

        for(JobOrder item : completeList){

            String waybillNo = item.getWaybillNo();

            if(waybillNo.contains(query)){

                result.add(item);
            }
        }

        return result;
    }

    private List<JobOrder> filter(String[] query){

        List<JobOrder> result = new ArrayList<>();
        List<String> listQuery = Arrays.asList(query);

        for(JobOrder item : completeList){

            String status = item.getStatus();

            if(listQuery.contains(status)){

                result.add(item);
            }
        }

        return result;
    }

}
