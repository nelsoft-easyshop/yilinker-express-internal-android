package com.yilinker.expressinternal.mvp.presenter.joborderlist;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.JobType;
import com.yilinker.expressinternal.mvp.model.TabItem;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.joborderlist.IJobListMainView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by J.Bautista on 3/2/16.
 */
public class JobListMainPresenter extends RequestPresenter<List<JobOrder>, IJobListMainView> {

    private static final String TAG_REQUEST = "request";

    private static final int REQUEST_GET_OPEN = 1000;
    private static final int REQUEST_GET_CURRENT = 1001;

    private static final String TYPE_OPEN = "Open";
    private static final String TYPE_CURRENT = "Current";

    private List<JobOrder> completeList;
    private List<TabItem> tabs;
    private List<JobType> types;
    private int previousTab = 0;

    private List<String> selectedFilter;
    private String searchString;

    private String currentTab = TYPE_OPEN;

    public JobListMainPresenter(){

        this.tabs = new ArrayList<>();
        this.types = new ArrayList<>();
        this.selectedFilter = new ArrayList<>();
        model = new ArrayList<>();
    }

    @Override
    protected void updateView() {

        if(view() != null) {
            view().reloadList(model);
            view().setResultCountText(String.valueOf(model.size()));
        }

    }

    public void onViewCreated(){

        view().showListView(model);
    }

    public void onResume(){

//        if(model == null) {
            requestGetJobOrders(currentTab);
//        }

    }

    public void onPause(){

        cancelRequests();
    }

    public void onRefresh(){

        requestGetJobOrders(currentTab);
    }

    public void onTabItemClicked(int jobType){

        //Clear list
        setModel(new ArrayList<JobOrder>());

        //Cancel previous request
        cancelRequests();

        changeSelectedTab(jobType);

        if(jobType == 0){

            currentTab = TYPE_OPEN;

        }
        else {

            currentTab = TYPE_CURRENT;
        }

        requestGetJobOrders(currentTab);

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

        view().loadJobTabs(tabs);
    }

    public void initializeFilter(String[] filterLabel){

        int size = filterLabel.length;
        JobType type = null;

        for(int i = 0; i < size; i++){

            type = new JobType();
            type.setId(i);
            type.setLabel(filterLabel[i]);
            type.setIsChecked(true);

            types.add(type);
            selectedFilter.add(filterLabel[i]);
        }

        view().loadJobTypes(types);
    }

    public void onFilterChanged(JobType type){

        if(type.isChecked()){

            selectedFilter.add(type.getLabel());
        }
        else{

            selectedFilter.remove(type.getLabel());
        }

        List<JobOrder> result = new ArrayList<>();
        result.addAll(filter());

        setModel(result);
    }

    public void onSearchWaybill(String waybillNo){

        List<JobOrder> result = new ArrayList<>();

        searchString = waybillNo;

        if(searchString.length() == 0){

            result.addAll(completeList);
        }
        else{

            //Search then filter

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

                handleGetJobOrders(object);
                break;

            case REQUEST_GET_CURRENT:

                handleGetJobOrders(object);
                break;

        }

        if(view() != null) {
            view().showLoader(false);
        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        if(view() != null) {
            view().showLoader(false);
        }
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

        view().showLoader(true);


        int requestCode = 0;
        if(type.equals(TYPE_OPEN)){
            requestCode = REQUEST_GET_OPEN;
        }
        else {
            requestCode = REQUEST_GET_CURRENT;
        }

        Request request = JobOrderAPI.getJobOrders(requestCode, type, true, this);
        request.setTag(TAG_REQUEST);

        view().addRequest(request);

    }

    private void handleGetJobOrders(Object object){

        List<com.yilinker.core.model.express.internal.JobOrder> listServer = (ArrayList<com.yilinker.core.model.express.internal.JobOrder>) object;

        //Clear current
        model.clear();

        completeList = createList(listServer, 0);


        //Filter result
        List<JobOrder> result = new ArrayList<>();
        result.addAll(filter());

        setModel(result);
    }

    private List<JobOrder> createList(List<com.yilinker.core.model.express.internal.JobOrder> listServer, int type) {

        List<JobOrder> list = new ArrayList<>();
        JobOrder jobOrder = null;

        int i = 0;
        for (com.yilinker.core.model.express.internal.JobOrder item : listServer) {

            jobOrder = new JobOrder(item);
            jobOrder.setId(i);

            //temp
            jobOrder.setLatitude(14.123234 + (1.0 * i));
            jobOrder.setLongitude(121.123232 + (1.0 * i));

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

    private List<JobOrder> filter(){

        List<JobOrder> result = new ArrayList<>();
        List<JobOrder> searchResult = new ArrayList<>();

        //Perform search filter
        if(searchString != null){

            searchResult.addAll(search(searchString));

        }
        else{

            searchResult.addAll(completeList);
        }

        for(JobOrder item : searchResult){

            String status = item.getStatus();

            if(selectedFilter.contains(status)){

                result.add(item);
            }
        }

        return result;
    }

    private void cancelRequests(){

        //Cancel all request
        ArrayList<String> request = new ArrayList<>();
        request.add(TAG_REQUEST);

        view().cancelRequests(request);

    }

}
