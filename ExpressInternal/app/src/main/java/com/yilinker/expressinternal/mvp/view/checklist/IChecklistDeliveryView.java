package com.yilinker.expressinternal.mvp.view.checklist;

import android.net.Uri;

import com.android.volley.Request;
import com.yilinker.expressinternal.model.JobOrder;
import com.yilinker.expressinternal.mvp.model.*;

import java.util.List;

/**
 * Created by J.Bautista on 3/28/16.
 */
public interface IChecklistDeliveryView extends IChecklistBase {

    public void showCaptureImageScreen(List<String> uri, int type);
    public void launchCamera(Uri uri, int type);
    public void showSignatureScreen(String uri);
    public void goToCompleteScreen(JobOrder joborder);
    public void goToMainScreen();
    public void startDeliveryService(DeliveryPackage deliveryPackage);
    public String compressImage(String path);

}
