package com.yilinker.expressinternal.mvp.view.checklist;

import android.net.Uri;

import com.android.volley.Request;
import com.yilinker.expressinternal.mvp.model.*;

import java.util.List;

/**
 * Created by J.Bautista on 3/28/16.
 */
public interface IChecklistDeliveryView extends IChecklistBase {

    public void showCaptureImageScreen(String uri);
    public void launchCamera(Uri uri, int type);
    public void showSignatureScreen(String uri);
    public void addRequest(Request request);
    public void cancelRequest(List<String> requests);
    public void goToMainScreen();
    public void startDeliveryService(DeliveryPackage deliveryPackage);

}
