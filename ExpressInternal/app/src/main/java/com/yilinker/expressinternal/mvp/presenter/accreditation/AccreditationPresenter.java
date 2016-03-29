package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.android.volley.Request;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.express.AccreditationApi;
import com.yilinker.core.model.express.internal.request.Accreditation;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.constants.AccreditationConstant;
import com.yilinker.expressinternal.mvp.model.AccreditationInformation;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirementData;
import com.yilinker.expressinternal.mvp.presenter.BasePresenter;
import com.yilinker.expressinternal.mvp.presenter.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.IAccreditationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class AccreditationPresenter extends RequestPresenter<AccreditationInformation, IAccreditationView> implements IAccreditationPresenter{

    private static final String TAG_REQUEST = "request";

    private static final int REQUEST_GET_REQUIREMENTS = 1000;
    private static final int REQUEST_SUBMIT_ACCREDITATION = 1001;

    private List<AccreditationRequirement> requirementList;

    @Override
    protected void updateView() {

    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_GET_REQUIREMENTS:

                handleGetRequirementsResponse((List<com.yilinker.core.model.express.internal.AccreditationRequirement>) object);
                break;

            case REQUEST_SUBMIT_ACCREDITATION:

                handleSubmitAccreditationResponse(object.toString());
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        view().showErrorMessage(message);
    }

    @Override
    public void onCreate() {

        //TODO Perform API request here
        requirementList = new ArrayList<>();
        AccreditationRequirement item = null;

        //Sample Data
        List<AccreditationRequirementData> sampleData = new ArrayList<>();
        AccreditationRequirementData data = null;
        for(int j =0; j < 6; j++){

            data = new AccreditationRequirementData();
            data.setId(j + 1);
            data.setLabel(String.format("Option %d", j));

            sampleData.add(data);
        }

        for(int i = 0; i < 5; i++){

            item = new AccreditationRequirement();

            item.setId(i);
            item.setLabel(String.format("Label %d", i));
            item.setType(i + 1);
            item.setInputValue("Sample Value");

            //Add items
            item.setData(sampleData);

            requirementList.add(item);
        }

        view().loadRequirementsList(requirementList);

    }

    @Override
    public void onSaveButtonClick() {

        //TODO Validation here

        //temp
        StringBuilder builder = new StringBuilder();
        String inputValue = null;

        for(AccreditationRequirement item : requirementList){

            inputValue = item.getInputValue();

            if(inputValue != null) {

                builder.append(inputValue);
                builder.append(",");
            }

        }

        view().showErrorMessage(builder.toString());
    }

    @Override
    public void onDataUpdate(AccreditationRequirement data) {

        updateData(data);
    }

    private void updateData(AccreditationRequirement item){

        int index = Collections.binarySearch(requirementList, item, new Comparator<AccreditationRequirement>() {
            @Override
            public int compare(AccreditationRequirement lhs, AccreditationRequirement rhs) {

                return rhs.getId() - lhs.getId();
            }
        });

        requirementList.set(index, item);
    }

    private void requestGetRequirements(){

        Request request = AccreditationApi.getRequirements(REQUEST_GET_REQUIREMENTS, this, new ExpressErrorHandler(this, REQUEST_GET_REQUIREMENTS));
        request.setTag(TAG_REQUEST);

        view().addRequest(request);

    }

    private void handleGetRequirementsResponse(List<com.yilinker.core.model.express.internal.AccreditationRequirement> data){

        List<AccreditationRequirement> list = new ArrayList<>();
        AccreditationRequirement requirement = null;

        int i = 0;
        for(com.yilinker.core.model.express.internal.AccreditationRequirement item : data){

            requirement = new AccreditationRequirement(item);
            requirement.setId(i);

            list.add(requirement);
            i++;
        }

        view().loadRequirementsList(requirementList);
    }

    private void requestSubmitAccreditation(){

        Accreditation accreditation = formatRequest();

        Request request = AccreditationApi.submitAccreditation(REQUEST_SUBMIT_ACCREDITATION, accreditation,this, new ExpressErrorHandler(this, REQUEST_GET_REQUIREMENTS));
        request.setTag(TAG_REQUEST);

        view().addRequest(request);
    }

    private Accreditation formatRequest(){

        Accreditation accreditation = new Accreditation();

        return accreditation;
    }

    private void handleSubmitAccreditationResponse(String message){

        //Show success message here
    }
}
