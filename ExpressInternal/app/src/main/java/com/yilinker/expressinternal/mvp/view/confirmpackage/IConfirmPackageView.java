package com.yilinker.expressinternal.mvp.view.confirmpackage;

import com.android.volley.Request;
import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.model.Package;

import java.util.List;

/**
 * Created by J.Bautista on 3/18/16.
 */
public interface IConfirmPackageView {

    public void loadPackageType(List<PackageType> list);
    public void loadPackageSize(List<PackageSize> list);
    public void resetShippingFee(String fee);
    public void goBackToChecklist(Package selectedPcakage);
    public void showCustomPackageView(boolean isShown);
    public void showPackageSizeButton(boolean isShown);
    public void addRequest(Request request);
    public void getLocalList(String fileName);
    public void saveListToLocal(String list, String fileName);
    public void setSelectedPackageType(String packageType);
    public void setSelectedPackageSize(String packageSize);
    public void setWidthText(String width);
    public void setHeightText(String height);
    public void setWeightText(String weight);
    public void setLengthText(String length);
    public void showErrorMessage(String message);
    public void showNoPackageTypeError();
    public void cancelRequest(String tag);
    public void showCalculatingFeeStatus();
    public void enabledSaveButton(boolean isEnabled);
    public void enabledPackageTypeButton(boolean isEnabled);
    public void enabledPackageSizeButton(boolean isEnabled);
    public void addEditTextListeners();
}
