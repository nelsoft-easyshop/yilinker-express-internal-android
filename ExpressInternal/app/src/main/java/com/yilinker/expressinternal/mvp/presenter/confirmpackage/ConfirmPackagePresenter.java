package com.yilinker.expressinternal.mvp.presenter.confirmpackage;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yilinker.core.api.JobOrderAPI;
import com.yilinker.core.api.express.JobOrderApi;
import com.yilinker.core.model.express.internal.ShippingFee;
import com.yilinker.core.model.express.internal.request.PackageRequest;
import com.yilinker.expressinternal.business.ExpressErrorHandler;
import com.yilinker.expressinternal.mvp.model.Package;
import com.yilinker.expressinternal.mvp.model.PackageSize;
import com.yilinker.expressinternal.mvp.model.PackageType;
import com.yilinker.expressinternal.mvp.presenter.base.RequestPresenter;
import com.yilinker.expressinternal.mvp.view.confirmpackage.IConfirmPackageView;
import com.yilinker.expressinternal.utilities.PriceFormatHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/18/16.
 */
public class ConfirmPackagePresenter extends RequestPresenter<Package, IConfirmPackageView> {

    private static final String REQUEST_TAG = "request";
    private static final String NO_AVAILABLE_FEE = "-";
    private static final String NO_SELECTION= "";
    private static final String FILE_PACKAGE_TYPE = "packageType.txt";

    private static final int REQUEST_GET_PACKAGE_TYPES = 1000;
    private static final int REQUEST_CALCULATE_SHIPPING_FEE = 1005;

    //TODO Move this
    private static final int CUSTOM_PACKAGE = 99;

    private List<PackageType> packageTypes;

    private boolean hasResetValues;

    private Package previouslySelectedPackage;

    public ConfirmPackagePresenter(){

        packageTypes = new ArrayList<>();
    }

    @Override
    protected void updateView() {

        view().enabledSaveButton(canContinue());

        if( hasResetValues) {

            view().resetShippingFee(model.getShippingFee());
            view().setHeightText(getStringValue(model.getHeight()));
            view().setWidthText(getStringValue(model.getWidth()));
            view().setLengthText(getStringValue(model.getLength()));
            view().setWeightText(getStringValue(model.getWeight()));

            if(!model.getPackageSize().equalsIgnoreCase(NO_SELECTION)) {
                view().setSelectedPackageSize(model.getPackageSize());
            }

            if(!model.getPackageType().equalsIgnoreCase(NO_SELECTION)) {
                view().setSelectedPackageType(model.getPackageType());
            }

            if(model.getSelectedId() == CUSTOM_PACKAGE){

                view().showCustomPackageView(true);
            }

            hasResetValues = false;
        }
        else{

            view().resetShippingFee(model.getShippingFee());
        }
    }


    public void onCreate(Package selectedPackage){

        if(selectedPackage.getSelectedId() != 0) {

            hasResetValues = true;
            previouslySelectedPackage = selectedPackage;
        }
        else{

            setModel(selectedPackage);
        }

        requestGetPackageType();
    }

    @Override
    public void onSuccess(int requestCode, Object object) {
        super.onSuccess(requestCode, object);

        switch (requestCode){

            case REQUEST_CALCULATE_SHIPPING_FEE:

                handleGetShippingFeeResponse((ShippingFee) object);
                break;

            case REQUEST_GET_PACKAGE_TYPES:

                handleGetPackageTypeResponse((List<com.yilinker.core.model.express.internal.PackageType>) object);
                break;

        }

    }

    @Override
    public void onFailed(int requestCode, String message) {
        super.onFailed(requestCode, message);

        switch (requestCode){

            case REQUEST_CALCULATE_SHIPPING_FEE:

                model.setShippingFee(NO_AVAILABLE_FEE);
                updateView();
                break;

            case REQUEST_GET_PACKAGE_TYPES:

                view().getLocalList(FILE_PACKAGE_TYPE);
                break;

        }

    }


    public void onSaveButtonClick(){

        //TODO Do validation here

        view().goBackToChecklist(model);
    }

    public void onEmptyField(){

        hasResetValues = false;
        cancelRequest();
        model.setShippingFee(NO_AVAILABLE_FEE);
        updateView();
    }

    public void onPackageTypeChanged(PackageType type){

        model.setTypeId(type.getId());
        model.setPackageType(type.getName());

        //Reset shipping fee
        clearValues();

        changePackageSize();

    }

    public void onGetLocalList(String jsonList){


        if(jsonList == null){

            view().showNoPackageTypeError();
            packageTypes.addAll(new ArrayList<PackageType>());
        } else {

            packageTypes.addAll(convertStringToPackageTypeList(jsonList));

        }

        view().loadPackageType(packageTypes);

        showPreviousValues();
    }

    public void onPackageSizeChanged(PackageSize size){

        model.setSelectedId(size.getId());
        model.setPackageSize(size.getName());

        view().setSelectedPackageSize(size.getName());

        requestCalculateShippingFee();
    }

    public void onLengthTextChanged(String length){

        double value = 0;
        if(!length.isEmpty()){

            value = Double.parseDouble(length);
        }

        model.setLength(value);
        calculateShipping();
    }

    public void onWidthTextChanged(String width){

        double value = 0;
        if(!width.isEmpty()){

            value = Double.parseDouble(width);
        }

        model.setWidth(value);
        calculateShipping();
    }

    public void onHeightTextChanged(String height){

        double value = 0;
        if(!height.isEmpty()){

            value = Double.parseDouble(height);
        }

        model.setHeight(value);
        calculateShipping();
    }

    public void onWeightTextChanged(String weight){

        if(weight.equals("0.")){
            return;
        }

        double value = 0;
        if(!weight.isEmpty()){

            value = Double.parseDouble(weight);
        }

        model.setWeight(value);
        calculateShipping();
    }

    private void requestGetPackageType(){

        Request request = JobOrderAPI.getPackages(REQUEST_GET_PACKAGE_TYPES, this);
        request.setTag(REQUEST_TAG);

        view().addRequest(request);
    }

    private void requestCalculateShippingFee(){

        PackageRequest object = formatPackageRequest();

        Request request = JobOrderApi.calculateShippingFee(REQUEST_CALCULATE_SHIPPING_FEE, object, Boolean.toString(false), this, new ExpressErrorHandler(this, REQUEST_CALCULATE_SHIPPING_FEE));
        request.setTag(REQUEST_TAG);

        view().addRequest(request);
    }

    private PackageRequest formatPackageRequest(){

        PackageRequest object = new PackageRequest();

        object.setJobOrderNo(model.getJobOrderNo());
        object.setWeight(String.valueOf(model.getWeight()));
        object.setPackageTypeId(model.getSelectedId());

        if(model.getSelectedId() == CUSTOM_PACKAGE){

            object.setHeight(String.valueOf(model.getHeight()));
            object.setLength(String.valueOf(model.getLength()));
            object.setWidth(String.valueOf(model.getWidth()));
        }

        return object;
    }

    private void handleGetPackageTypeResponse(List<com.yilinker.core.model.express.internal.PackageType> list){

        packageTypes = new ArrayList<>();
        PackageType type = null;

        int i = 1;
        for(com.yilinker.core.model.express.internal.PackageType item : list){

            type = new PackageType(item);

            if(type.getId() == 0) {
                type.setId(i); //temp
            }

            packageTypes.add(type);
            i++;
        }

        savePackageTypeList(packageTypes);
        view().loadPackageType(packageTypes);


        showPreviousValues();
    }

    private void handleGetShippingFeeResponse(ShippingFee object){

        String stringFee = object.getTotalShippingFee();
        String formattedFee = formatShippingFee(stringFee);

        model.setShippingFee(formattedFee);
        updateView();
    }

    private void savePackageTypeList(List<PackageType> packageTypes) {

        String jsonList = convertListToString(packageTypes);
        view().saveListToLocal(jsonList, FILE_PACKAGE_TYPE);
    }

    private List<PackageType> convertStringToPackageTypeList(String jsonString){

        Type listType = new TypeToken<ArrayList<PackageType>>() {
        }.getType();

        List<PackageType> list = new Gson().fromJson(jsonString, listType);

        return list;
    }

    private String convertListToString(List<PackageType> list){

        String jsonString = new Gson().toJson(list);

        return jsonString;
    }

    private boolean canContinue(){

        int typeId = model.getSelectedId();

        if(typeId != 0 && model.getWeight() > 0){

            if(typeId == CUSTOM_PACKAGE){

                if(model.getHeight()> 0 && model.getWidth()> 0 && model.getLength()>0){

                    return true;
                }
            }
            else{

                return true;
            }

        }

        return false;
    }

    private String formatShippingFee(String stringFee){

        double fee = Double.parseDouble(stringFee);

        String formattedFee = PriceFormatHelper.formatPrice(fee);

        return formattedFee;
    }

    private void calculateShipping(){

        cancelRequest();

        if(canContinue()) {

            view().showCalculatingFeeStatus();
            requestCalculateShippingFee();
        }
        else{

            view().enabledSaveButton(false);
            model.setShippingFee(NO_AVAILABLE_FEE);
            updateView();
        }
    }

    private void clearValues(){

        hasResetValues = true;

        model.setShippingFee(NO_AVAILABLE_FEE);
        model.setSelectedId(0);
        model.setPackageSize(NO_SELECTION);
        model.setLength(0);
        model.setWidth(0);
        model.setWeight(0);
        model.setHeight(0);

        updateView();
    }

    private String getStringValue(double value){

        String stringValue = null;

        if(value > 0){

            stringValue = String.valueOf(value);
        }
        else {

            stringValue = NO_SELECTION;
        }

        return stringValue;
    }

    private void cancelRequest(){

        view().cancelRequest(REQUEST_TAG);
    }

    private void changePackageSize(){

        int typeId = model.getTypeId();

        if(typeId == CUSTOM_PACKAGE){

            view().showCustomPackageView(true);
            view().showPackageSizeButton(false);
            model.setSelectedId(typeId);
        }
        else{

            List<PackageSize> sizes = getSizesByTypeId(typeId);

            view().loadPackageSize(sizes);
            view().showCustomPackageView(false);
            view().showPackageSizeButton(true);

        }
    }

    private List<PackageSize> getSizesByTypeId(int id){

        List<PackageSize> sizes = new ArrayList<>();

        for(PackageType item : packageTypes){

            if(item.getId() == id && id != CUSTOM_PACKAGE){

                sizes.addAll(item.getSize());
            }
        }

        return sizes;
    }

    private void showPreviousValues(){

        //Set selected values
        if(hasResetValues) {

            setModel(previouslySelectedPackage);
            changePackageSize();
        }

        view().addEditTextListeners();
    }

}
