package com.yilinker.expressinternal.mvp.view.checklist;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.model.Package;

import java.util.List;

/**
 * Created by wagnavu on 3/22/16.
 */
public interface IChecklistPickupView extends IChecklistBase {

    public void showConfirmPackageScreen(Package selectedPackage);
    public void goToMainScreen();
    public void startPickupService(Package selectedPackage);
    public void showStatusOptionDialog();
    public String getRiderAreaCode();
    public void addRequest(Request request);
    public void cancelRequest(List<String> requests);

}
