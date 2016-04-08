package com.yilinker.expressinternal.mvp.presenter.accreditation;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.yilinker.core.api.express.AccreditationApi;
import com.yilinker.core.model.express.internal.request.Accreditation;
import com.yilinker.core.utility.ImageUtility;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.constants.AccreditationConstant;
import com.yilinker.expressinternal.mvp.model.AccreditationInformation;
import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.accreditation.IAccreditationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class AccreditationPresenter extends RequestPresenter<AccreditationInformation, IAccreditationView> implements IAccreditationPresenter{

    private static final String TAG_REQUEST = "request";

    private static final int REQUEST_GET_REQUIREMENTS = 1000;
    private static final int REQUEST_SUBMIT_ACCREDITATION = 1001;

    private List<AccreditationRequirement> requirementList;

    private Uri photoUri;

    private AccreditationRequirement currentItem;

    @Override
    protected void updateView() {


    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_GET_REQUIREMENTS:

                handleGetRequirementsResponse((List<com.yilinker.core.model.express.internal.AccreditationRequirement>) object);
                view().enableSaveButton(true);
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
        view().showScreenLoader(false);
    }

    @Override
    public void onCreate() {
//
//        //TODO Perform API request here
//        requirementList = new ArrayList<>();
//        AccreditationRequirement item = null;
//
//        //Sample Data
//        List<AccreditationRequirementData> sampleData = new ArrayList<>();
//        AccreditationRequirementData data = null;
//        for(int j =0; j < 6; j++){
//
//            data = new AccreditationRequirementData();
//            data.setId(j + 1);
//            data.setLabel(String.format("Option %d", j));
//
//            sampleData.add(data);
//        }
//
//        for(int i = 0; i < 5; i++){
//
//            item = new AccreditationRequirement();
//
//            item.setId(i);
//            item.setLabel(String.format("Label %d", i));
//            item.setType(i + 1);
//            item.setInputValue("Sample Value");
//
//            //Add items
//            item.setData(sampleData);
//
//            requirementList.add(item);
//        }
//
//        view().loadRequirementsList(requirementList);

        model = new AccreditationInformation();

        requestGetRequirements();

    }

    @Override
    public void onSaveButtonClick() {

        if(hasCompleteRequirements()) {

            view().showScreenLoader(true);
            requestSubmitAccreditation();
        }
        else{

            view().showErrorMessageByType(AccreditationConstant.ACCREDITATION_ERROR_INCOMPLETE);
        }
    }

    @Override
    public void onDataUpdate(AccreditationRequirement data) {

        updateData(data);
    }

    @Override
    public void onItemClick(AccreditationRequirement item) {

        currentItem = item;

        if(item.getType() == AccreditationConstant.REQUIREMENT_TYPE_BUTTON){

            //TODO Launch Selector
            //temp
            createImageFile();
            view().launchCamera(photoUri);
        }

    }

    //For Button type
    @Override
    public void onCameraResult() {

        List<String> selectedItems = currentItem.getSelectedValues();

        if(selectedItems == null){

            selectedItems = new ArrayList<>();
        }

        String compressedImage = view().compressImage(photoUri.getEncodedPath());

        selectedItems.add(compressedImage);

        currentItem.setSelectedValues(selectedItems);

        updateData(currentItem);

        view().resetItem(currentItem);
    }

    @Override
    public void onFirstNameTextChanged(String firstName) {

        model.setFirstName(firstName);
    }

    @Override
    public void onLastNameTextChanged(String lastName) {

        model.setLastName(lastName);
    }

    @Override
    public void onBirthdayTextChanged(String birthday) {

        model.setBirthday(birthday);
    }

    @Override
    public void onGenderChanged(String gender) {

        model.setGender(gender);
    }

    private void updateData(AccreditationRequirement item){

//        int index = Collections.binarySearch(requirementList, item, new Comparator<AccreditationRequirement>() {
//            @Override
//            public int compare(AccreditationRequirement lhs, AccreditationRequirement rhs) {
//
//                return rhs.getId() - lhs.getId();
//            }
//        });

        int index = 0;
        for(AccreditationRequirement requirement : requirementList){

            if(item.getId() == requirement.getId())
            {
                break;
            }

            index++;
        }

        requirementList.set(index, item);
    }

    private void requestGetRequirements(){

        Request request = AccreditationApi.getRequirements(REQUEST_GET_REQUIREMENTS, this, new ExpressErrorHandler(this, REQUEST_GET_REQUIREMENTS));
        request.setTag(TAG_REQUEST);

        view().addRequest(request);

    }

    private void handleGetRequirementsResponse(List<com.yilinker.core.model.express.internal.AccreditationRequirement> data){

        requirementList = new ArrayList<>();
        AccreditationRequirement requirement = null;

        int i = 0;
        for(com.yilinker.core.model.express.internal.AccreditationRequirement item : data){

            requirement = new AccreditationRequirement(item);
            requirement.setId(i);

            requirementList.add(requirement);
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

        accreditation.setFirstName(model.getFirstName());
        accreditation.setBirthday(model.getBirthday());
        accreditation.setGender(model.getGender());
        accreditation.setLastName(model.getLastName());

        accreditation.setRequirements(formatRequirements());
        accreditation.setImages(formatImagesRequirement());


        return accreditation;
    }

    private void handleSubmitAccreditationResponse(String message){

        view().showErrorMessage(message);
        view().goToConfirmation();
        //Show success message here
    }

    private void createImageFile(){

        String tempFileName = String.format("image_%s", Long.toString(System.currentTimeMillis()));

        try {

            File folder = new File(Environment.getExternalStorageDirectory().toString(), ImageUtility.TEMP_IMAGE_FOLDER);

            if (!folder.exists()) folder.mkdirs();

            File outputFile = new File(String.format("%s/%s/%s.jpeg", Environment.getExternalStorageDirectory().toString(), ImageUtility.TEMP_IMAGE_FOLDER, tempFileName));

            photoUri = Uri.fromFile(outputFile);

        }
        catch (Exception e){

            Log.d("Image Exception", e.getMessage());

        }

    }

    private String formatRequirements(){

        String requirements = null;
        JSONArray jsonArray = new JSONArray();

        try {

        JSONObject jsonObject = null;
        JSONArray jsonArrayData = null;

        for(AccreditationRequirement requirement : requirementList){

            jsonObject = new JSONObject();
            jsonObject.putOpt(AccreditationConstant.REQUIREMENT_ID, requirement.getKey());
            jsonArrayData = new JSONArray();

            int type = requirement.getType();

            switch (type){

                case AccreditationConstant.REQUIREMENT_TYPE_BUTTON:

//                    jsonArrayData = arrayToJSONArray(requirement.getSelectedValues());
                    break;

                case AccreditationConstant.REQUIREMENT_TYPE_DROPDOWN:

                    jsonArrayData.put(requirement.getInputKey());
                    break;

                case AccreditationConstant.REQUIREMENT_TYPE_CHECKLIST:

                    jsonArrayData = arrayToJSONArray(requirement.getSelectedValues());
                    break;

                case AccreditationConstant.REQUIREMENT_TYPE_INPUTTEXT:

                    jsonArrayData.put(requirement.getInputValue());
                    break;

                case AccreditationConstant.REQUIREMENT_TYPE_CHECKBOX:

                    jsonArrayData.put(requirement.getInputValue());
                    break;
            }

            jsonObject.put(AccreditationConstant.REQUIREMENT_DATA, jsonArrayData);

            jsonArray.put(jsonObject);
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requirements = jsonArray.toString();

        return requirements;
    }

    private List<String> formatImagesRequirement(){

        List<String> images = new ArrayList<>();

        for(AccreditationRequirement requirement : requirementList) {

            if(requirement.getType() == AccreditationConstant.REQUIREMENT_TYPE_BUTTON){

                images.addAll(requirement.getSelectedValues());
            }
        }

        return images;
    }

    private JSONArray arrayToJSONArray(List<String> stringArray){

        JSONArray jsonArray = new JSONArray();
        for(String item : stringArray){

            jsonArray.put(item);
        }

        return jsonArray;
    }

    private boolean hasCompleteRequirements(){

        if(requirementList == null){

            return false;
        }


        if(requirementList.size() == 0){

            return false;
        }
        else {

            boolean isComplete = true;

            for (AccreditationRequirement requirement : requirementList) {

                isComplete = true;

                if (requirement.isRequired()) {

                    int type = requirement.getType();

                    switch (type) {
                        case AccreditationConstant.REQUIREMENT_TYPE_BUTTON:

                            isComplete = completeButtonType(requirement);
                            break;

                        case AccreditationConstant.REQUIREMENT_TYPE_DROPDOWN:

                            isComplete = completeDropdown(requirement);
                            break;

                        case AccreditationConstant.REQUIREMENT_TYPE_CHECKLIST:

                            isComplete = completeChecklistType(requirement);
                            break;

                        case AccreditationConstant.REQUIREMENT_TYPE_INPUTTEXT:

                            isComplete = completeInputType(requirement);
                            break;

                        case AccreditationConstant.REQUIREMENT_TYPE_CHECKBOX:

                            isComplete = completeInputType(requirement);
                            break;
                    }
                }

                if(!isComplete){

                    break;
                }

            }

            return isComplete;
        }


    }

    private boolean completeInputType(AccreditationRequirement requirement){

        String inputValue = requirement.getInputValue();

        boolean isComplete = true;
        if(inputValue == null){

            isComplete = false;
        }
        else {

            isComplete = !inputValue.trim().isEmpty();
        }

        return isComplete;
    }

    private boolean completeDropdown(AccreditationRequirement requirement){

        String inputValue = requirement.getInputKey();

        boolean isComplete = true;
        if(inputValue == null){

            isComplete = false;
        }
        else {

            isComplete = !inputValue.trim().isEmpty();
        }

        return isComplete;
    }

    private boolean completeCheckboxType(AccreditationRequirement requirement){

        String inputValue = requirement.getInputValue();

        boolean isComplete = true;
        if(inputValue == null){

            isComplete = false;
        }
        else {

            isComplete = !inputValue.trim().isEmpty();
        }

        return isComplete;
    }

    private boolean completeChecklistType(AccreditationRequirement requirement){

        List<String> selectedValues = requirement.getSelectedValues();

        boolean isComplete = true;
        if(selectedValues == null){

            isComplete = false;
        }
        else {

            isComplete = selectedValues.size() > 0;
        }

        return isComplete;
    }

    private boolean completeButtonType(AccreditationRequirement requirement){

        List<String> selectedValues = requirement.getSelectedValues();

        boolean isComplete = true;
        if(selectedValues == null){

            isComplete = false;
        }
        else {

            isComplete = selectedValues.size() > 0;
        }

        return isComplete;
    }
}
